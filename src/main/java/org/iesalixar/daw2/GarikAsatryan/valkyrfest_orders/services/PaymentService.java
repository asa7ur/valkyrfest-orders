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

    public void processWebhookEvent(String payload, String sigHeader) throws Exception {
        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

        if ("checkout.session.completed".equals(event.getType())) {
            // Usamos Optional para evitar errores si el objeto no se deserealiza bien
            Optional<Session> sessionOptional = event.getDataObjectDeserializer().getObject().map(o -> (Session) o);

            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                String orderIdStr = session.getClientReferenceId();

                if (orderIdStr != null) {
                    Long orderId = Long.parseLong(orderIdStr);
                    orderService.getOrderById(orderId).ifPresent(order -> {
                        try {
                            order.setStatus(OrderStatus.PAID);
                            orderService.saveOrder(order);

                            byte[] pdfBytes = pdfGeneratorService.generateOrderPdf(order);
                            emailService.sendOrderConfirmationEmail(order, pdfBytes);

                            System.out.println("LOG: Pedido #" + orderId + " procesado con éxito.");
                        } catch (Exception e) {
                            System.err.println("ERROR CRÍTICO en post-pago: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                } else {
                    System.err.println("ADVERTENCIA: Se recibió un pago de Stripe sin clientReferenceId. ¿Es una sesión antigua?");
                }
            }
        }
    }
}