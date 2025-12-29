package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Value("${app.url}")
    private String appUrl;

    /**
     * Envía el correo de activación al usuario.
     */
    public void sendRegistrationConfirmationEmail(String to, String firstName, String token) {
        // Construimos la URL completa de activación
        String confirmationUrl = appUrl + "/register/confirm?token=" + token;

        // Obtenemos el asunto y el cuerpo traducidos
        String subject = messageSource.getMessage("msg.register.email.subject", null, LocaleContextHolder.getLocale());
        String bodyTemplate = messageSource.getMessage("msg.register.email.body", null, LocaleContextHolder.getLocale());

        // Reemplazamos los marcadores {0} y {1} por el nombre y la URL
        String body = java.text.MessageFormat.format(bodyTemplate, firstName, confirmationUrl);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(body);

        mailSender.send(email);
    }
}