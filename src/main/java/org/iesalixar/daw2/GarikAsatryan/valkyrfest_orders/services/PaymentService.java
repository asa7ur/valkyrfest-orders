package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Order;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;
    private final PdfGeneratorService pdfGeneratorService;
    private final EmailService emailService;

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Value("${app.url}")
    private String appUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public String createStripeSession(Order order) throws Exception {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setClientReferenceId(order.getId().toString())
                .setSuccessUrl(appUrl + "/order/success/" + order.getId())
                .setCancelUrl(appUrl + "/order/checkout")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(order.getTotalPrice().movePointRight(2).longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Pedido Valkyrfest #" + order.getId())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    @Transactional
    public void processWebhookEvent(String payload, String sigHeader) throws Exception {
        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

        if ("checkout.session.completed".equals(event.getType())) {
            Optional<Session> sessionOptional = event.getDataObjectDeserializer().getObject().map(o -> (Session) o);

            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                String orderIdStr = session.getClientReferenceId();

                if (orderIdStr != null) {
                    try {
                        Long orderId = Long.parseLong(orderIdStr);

                        // 1. Usamos el nuevo método para asegurar la persistencia del estado PAID
                        Order order = orderService.confirmPayment(orderId);
                        System.out.println("LOG: Pedido #" + orderId + " marcado como PAID.");

                        // 2. Generación de PDF y envío de Email (ahora dentro de la transacción para evitar errores Lazy)
                        try {
                            byte[] pdfBytes = pdfGeneratorService.generateOrderPdf(order);
                            emailService.sendOrderConfirmationEmail(order, pdfBytes);
                            System.out.println("LOG: Email enviado a Mailtrap con éxito.");
                        } catch (Exception e) {
                            // Si falla el email, capturamos el error para que NO haga rollback del pago
                            System.err.println("ADVERTENCIA: Pago registrado pero falló el email: " + e.getMessage());
                        }

                    } catch (Exception e) {
                        System.err.println("ERROR CRÍTICO procesando el webhook: " + e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                } else {
                    System.err.println("ADVERTENCIA: No hay clientReferenceId en la sesión de Stripe.");
                }
            }
        }
    }
}