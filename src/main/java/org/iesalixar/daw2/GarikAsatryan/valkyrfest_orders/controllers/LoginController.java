package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MessageSource messageSource;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        HttpServletRequest request,
                        Model model) {

        // Error de sesión (OAuth2)
        String sessionError = (String) request.getSession().getAttribute("errorMessage");
        if (sessionError != null) {
            model.addAttribute("errorMessage", sessionError);
            request.getSession().removeAttribute("errorMessage");
        }

        // Error tradicional (?error)
        if (error != null && sessionError == null) {
            model.addAttribute("errorMessage", messageSource.getMessage("msg.login.error", null, LocaleContextHolder.getLocale()));
        }

        // Logout exitoso (?logout)
        if (logout != null) {
            model.addAttribute("successMessage", messageSource.getMessage("msg.login.logout", null, LocaleContextHolder.getLocale()));
        }

        return "login";
    }
}