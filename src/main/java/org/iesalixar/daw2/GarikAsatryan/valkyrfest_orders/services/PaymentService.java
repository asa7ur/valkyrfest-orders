package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Order;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * IMPORTANTE: Añadimos @Transactional para evitar errores de carga perezosa (Lazy)
     * al acceder a los tickets y campings durante la generación del PDF.
     */
    @Transactional
    public void processWebhookEvent(String payload, String sigHeader) throws Exception {
        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

        if ("checkout.session.completed".equals(event.getType())) {
            Optional<Session> sessionOptional = event.getDataObjectDeserializer().getObject().map(o -> (Session) o);

            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                String orderIdStr = session.getClientReferenceId();

                if (orderIdStr != null) {
                    Long orderId = Long.parseLong(orderIdStr);
                    System.out.println("DEBUG: Procesando pago para el pedido ID: " + orderId);

                    orderService.getOrderById(orderId).ifPresentOrElse(order -> {
                        try {
                            // 1. Cambiamos el estado
                            order.setStatus(OrderStatus.PAID);
                            orderService.saveOrder(order);
                            System.out.println("DEBUG: Estado del pedido #" + orderId + " cambiado a PAID.");

                            // 2. Generamos el PDF (esto fallaba antes por el Lazy loading)
                            byte[] pdfBytes = pdfGeneratorService.generateOrderPdf(order);
                            System.out.println("DEBUG: PDF generado con éxito.");

                            // 3. Enviamos el correo
                            emailService.sendOrderConfirmationEmail(order, pdfBytes);
                            System.out.println("LOG: Pedido #" + orderId + " completado y correo enviado.");

                        } catch (Exception e) {
                            System.err.println("ERROR CRÍTICO procesando pedido #" + orderId + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }, () -> System.err.println("ERROR: No se encontró el pedido con ID: " + orderId));
                } else {
                    System.err.println("ADVERTENCIA: Webhook recibido sin clientReferenceId.");
                }
            }
        }
    }
}