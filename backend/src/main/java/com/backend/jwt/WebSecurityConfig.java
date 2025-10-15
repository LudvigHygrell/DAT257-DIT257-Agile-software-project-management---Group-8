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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.backend.ApplicationProperties;

import java.util.Arrays;

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
            .cors(cors -> cors.configure(http)) // Enable CORS
            .authorizeHttpRequests(authorize -> {
                    authorize // Authenticate all but create and login
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers("/api/users/create").permitAll()
                        .requestMatchers("/api/charities/**").permitAll()
                        .requestMatchers("/api/files/public/**").permitAll()
                        .requestMatchers("/api/comments/list").permitAll();
                    
                    if (properties.getEmailProperties().isVerified()) {
                        authorize.requestMatchers("/api/email/confirm/**").permitAll();
                    }

                    if (properties.inDebug()) {
                        authorize.requestMatchers("/api/debug/**").permitAll();
                    }
                    authorize.anyRequest().authenticated();
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

    /**
     * CORS configuration to allow frontend requests from different origins.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",  // Vite dev server
            "http://localhost:3000",  // Alternative React dev server
            "http://127.0.0.1:5173",
            "http://127.0.0.1:3000"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
