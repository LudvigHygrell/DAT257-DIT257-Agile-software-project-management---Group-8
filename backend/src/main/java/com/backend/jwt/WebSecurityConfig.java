package com.backend.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.backend.ApplicationProperties;

/**
 * Security configuration for incoming requests.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private ApplicationProperties properties;

    private final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    /**
     * Filters incoming requests (blocking "bad" ones).
     * @param http Security object to filter.
     * @return Object containing the assigned filters.
     * @throws Exception Thrown if building the filter fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> {
                    authorize // Authenticate all but create and login
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers("/api/users/create").permitAll()
                        .anyRequest().authenticated();
                    
                    if (properties.getEmailProperties().isVerified()) {
                        authorize.requestMatchers("/api/email/confirm/**").permitAll();
                    }

                    if (properties.inDebug()) {
                        authorize.requestMatchers("/api/debug/**").permitAll();
                    }
                })
            .sessionManagement(session -> session // Use stateless sessions
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptionHandling -> // Report denied access to log output
                exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                    log.error("ACCESS DENIED: {}", accessDeniedException.getMessage());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }));
        // Authorize using JWT
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Part of security configuration.
     * <p>
     * Not used, behaviour is default.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Specifies how passwords are encoded.
     * <p>
     * Current encoding is BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
