package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto.UserRegistrationDTO;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.User;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.exceptions.AppException;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services.RegistrationService;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services.UserService;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services.VerificationTokenService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final MessageSource messageSource;
    private final VerificationTokenService verificationTokenService;
    private final UserService userService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping
    public String registerUserAccount(
            @Valid @ModelAttribute("user") UserRegistrationDTO registrationDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            registrationService.registerUser(registrationDTO);
        } catch (AppException e) {
            String errorMessage = messageSource.getMessage(e.getMessageKey(), e.getArgs(), LocaleContextHolder.getLocale());
            result.rejectValue("email", e.getMessageKey(), errorMessage);
            return "register";
        }

        String successMsg = messageSource.getMessage("msg.register.success", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", successMsg);

        return "redirect:/login";
    }

    // NUEVO MÉTODO: Este es el que "escucha" el clic del email
    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        return verificationTokenService.getVerificationToken(token)
                .map(verificationToken -> {
                    // Verificamos si el token ha caducado
                    if (verificationToken.isExpired()) {
                        String errorMsg = messageSource.getMessage("msg.register.error.invalidToken", null, LocaleContextHolder.getLocale());
                        redirectAttributes.addFlashAttribute("errorMessage", errorMsg);
                        return "redirect:/login";
                    }

                    // ACTIVAMOS AL USUARIO
                    User user = verificationToken.getUser();
                    user.setEnabled(true);
                    userService.saveUser(user); // Este método ya lo tienes en tu UserService

                    // Borramos el token para que no se use dos veces
                    verificationTokenService.deleteToken(verificationToken);

                    String successMsg = messageSource.getMessage("msg.register.activationSuccess", null, LocaleContextHolder.getLocale());
                    redirectAttributes.addFlashAttribute("successMessage", successMsg);
                    return "redirect:/login";
                })
                .orElseGet(() -> {
                    String errorMsg = messageSource.getMessage("msg.register.error.invalidToken", null, LocaleContextHolder.getLocale());
                    redirectAttributes.addFlashAttribute("errorMessage", errorMsg);
                    return "redirect:/login";
                });
    }
}