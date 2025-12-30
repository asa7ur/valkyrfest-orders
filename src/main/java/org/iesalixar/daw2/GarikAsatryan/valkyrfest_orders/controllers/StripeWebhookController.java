package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.controllers;

import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PaymentService paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            // Delegamos toda la lógica al Service
            paymentService.processWebhookEvent(payload, sigHeader);
            return ResponseEntity.ok("Evento procesado correctamente");

        } catch (Exception e) {
            // Si la firma no es válida o hay error de lógica, devolvemos error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el Webhook: " + e.getMessage());
        }
    }
}