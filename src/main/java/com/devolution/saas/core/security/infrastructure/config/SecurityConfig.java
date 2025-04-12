package com.devolution.saas.core.security.infrastructure.config;

import com.devolution.saas.common.filter.RateLimitFilter;
import com.devolution.saas.common.filter.RequestLoggingFilter;
import com.devolution.saas.core.security.infrastructure.filter.ApiKeyAuthenticationFilter;
import com.devolution.saas.core.security.infrastructure.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.List;

/**
 * Configuration de sécurité pour l'application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${cors.allowed-methods}")
    private List<String> allowedMethods;

    @Value("${cors.allowed-headers}")
    private List<String> allowedHeaders;

    @Value("${cors.exposed-headers}")
    private List<String> exposedHeaders;

    @Value("${cors.allow-credentials:false}")
    private boolean allowCredentials;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    // Les filtres sont injectés via les paramètres de la méthode securityFilterChain

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
        // Configuration de la chaîne de filtres de sécurité
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
     * Utilise les propriétés définies dans les fichiers de configuration.
     *
     * @return Source de configuration CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Configurer les origines autorisées
        configuration.setAllowedOrigins(allowedOrigins);

        // Configurer les méthodes autorisées
        configuration.setAllowedMethods(allowedMethods);

        // Configurer les en-têtes autorisés
        configuration.setAllowedHeaders(allowedHeaders);

        // Configurer les en-têtes exposés
        configuration.setExposedHeaders(exposedHeaders);

        // Configurer l'autorisation des cookies
        configuration.setAllowCredentials(allowCredentials);

        // Configurer la durée de mise en cache des résultats de pré-vérification
        configuration.setMaxAge(maxAge);

        // Enregistrer la configuration pour tous les chemins
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
