package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.config;

import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.handlers.CustomLoginSuccessHandler;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.handlers.CustomOAuth2FailureHandler;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.handlers.CustomOAuth2SuccessHandler;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/stripe/webhook")
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/register/**", "/login", "/css/**", "/js/**", "/images/**", "/stripe/webhook").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customLoginSuccessHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler(customOAuth2FailureHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        // Usamos el método passwordEncoder() definido arriba
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}