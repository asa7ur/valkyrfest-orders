package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Captura todas las excepciones de tipo AppException que ocurran en la App.
     */
    @ExceptionHandler(AppException.class)
    public String handleAppException(AppException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        // Traducir el mensaje usando la llave y el idioma actual del usuario
        String errorMessage = messageSource.getMessage(
                ex.getMessageKey(),
                ex.getArgs(),
                LocaleContextHolder.getLocale()
        );

        // Añadir el mensaje de error para que se muestre en la siguiente vista
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

        // Redirigir automáticamente a la página de la que venía el usuario (Referer)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
