package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Value("${app.url}")
    private String appUrl;

    /**
     * Envía el correo de activación al usuario (Texto plano).
     */
    public void sendRegistrationConfirmationEmail(String to, String firstName, String token) {
        String confirmationUrl = appUrl + "/register/confirm?token=" + token;

        String subject = messageSource.getMessage("msg.register.email.subject", null, LocaleContextHolder.getLocale());
        String bodyTemplate = messageSource.getMessage("msg.register.email.body", null, LocaleContextHolder.getLocale());

        String body = java.text.MessageFormat.format(bodyTemplate, firstName, confirmationUrl);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(body);

        mailSender.send(email);
    }

    /**
     * Envía un correo con las entradas en formato PDF adjunto.
     *
     * @param order    El pedido pagado.
     * @param pdfBytes El contenido del PDF generado.
     */
    public void sendOrderConfirmationEmail(Order order, byte[] pdfBytes) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();

        // El 'true' indica que es un mensaje "multipart" (con adjuntos)
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String to = order.getUser().getEmail();
        String firstName = order.getUser().getFirstName();

        // Obtenemos los textos de messages.properties
        String subject = messageSource.getMessage("msg.order.email.subject", new Object[]{order.getId()}, LocaleContextHolder.getLocale());
        String bodyTemplate = messageSource.getMessage("msg.order.email.body", null, LocaleContextHolder.getLocale());
        String body = java.text.MessageFormat.format(bodyTemplate, firstName, order.getId());

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true permite usar HTML en el cuerpo del correo

        // Adjuntamos el PDF dándole un nombre de archivo
        String fileName = "Valkyrfest_Pedido_" + order.getId() + ".pdf";
        helper.addAttachment(fileName, new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }
}