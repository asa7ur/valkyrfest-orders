package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.UserRepository;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final MessageSource messageSource;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        logger.info("OAuth2 login attempt successful for email: {}", email);

        if (email == null || !userRepository.existsByEmail(email)) {
            logger.warn("OAuth2 authentication failed: Email {} is not registered in the local database", email);
            String errorMsg = messageSource.getMessage("msg.login.oauth2.notRegistered", new Object[]{email}, LocaleContextHolder.getLocale());
            throw new OAuth2AuthenticationException(errorMsg);
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        logger.info("User {} successfully authenticated and security context updated", email);
        response.sendRedirect("/");
    }
}