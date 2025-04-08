package com.devolution.saas.core.security.infrastructure.config;

import com.devolution.saas.common.filter.RateLimitFilter;
import com.devolution.saas.common.filter.RequestLoggingFilter;
import com.devolution.saas.core.security.infrastructure.filter.ApiKeyAuthenticationFilter;
import com.devolution.saas.core.security.infrastructure.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

import java.util.Arrays;
import java.util.List;

/**
 * Configuration de sécurité pour l'application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;
    private RequestLoggingFilter requestLoggingFilter;
    private RateLimitFilter rateLimitFilter;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Configure la chaîne de filtres de sécurité.
     *
     * @param http                        Configuration HTTP
     * @param jwtAuthenticationFilter     Filtre d'authentification JWT
     * @param apiKeyAuthenticationFilter  Filtre d'authentification par API Key
     * @param requestLoggingFilter        Filtre de journalisation des requêtes
     * @param rateLimitFilter             Filtre de limitation du taux de requêtes
     * @param jwtAuthenticationEntryPoint Point d'entrée pour l'authentification JWT
     * @return Chaîne de filtres de sécurité
     * @throws Exception Si une erreur survient
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   ApiKeyAuthenticationFilter apiKeyAuthenticationFilter,
                                                   RequestLoggingFilter requestLoggingFilter,
                                                   RateLimitFilter rateLimitFilter,
                                                   JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.apiKeyAuthenticationFilter = apiKeyAuthenticationFilter;
        this.requestLoggingFilter = requestLoggingFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter, RequestLoggingFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, RateLimitFilter.class)
                .addFilterAfter(apiKeyAuthenticationFilter, JwtAuthenticationFilter.class)
                .build();
    }

    /**
     * Configure le gestionnaire d'authentification.
     *
     * @param authenticationConfiguration Configuration d'authentification
     * @return Gestionnaire d'authentification
     * @throws Exception Si une erreur survient
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configure l'encodeur de mot de passe.
     *
     * @return Encodeur de mot de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la source de configuration CORS.
     *
     * @return Source de configuration CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
