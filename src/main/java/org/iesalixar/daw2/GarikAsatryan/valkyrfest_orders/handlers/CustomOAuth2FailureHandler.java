package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2FailureHandler.class);
    private final MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        logger.error("OAuth2 Authentication failed: {}", exception.getMessage());

        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        String errorMsg = messageSource.getMessage("msg.login.oauth2.error", null, LocaleContextHolder.getLocale());
        request.getSession().setAttribute("errorMessage", errorMsg);

        response.sendRedirect("/login?error=true");
    }
}