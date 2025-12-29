package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto.UserRegistrationDTO;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.exceptions.AppException;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services.RegistrationService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final MessageSource messageSource;

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
}
