package com.devolution.saas.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Filtre CORS personnalisé pour un contrôle plus précis des requêtes cross-origin.
 * Ce filtre est exécuté avant les autres filtres de sécurité pour s'assurer que
 * les en-têtes CORS sont correctement définis avant tout traitement de sécurité.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CustomCorsFilter implements Filter {

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

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Récupérer l'origine de la requête
        String origin = request.getHeader("Origin");

        // Vérifier si l'origine est autorisée
        if (origin != null && isAllowedOrigin(origin)) {
            // Définir les en-têtes CORS
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", String.join(",", allowedMethods));
            response.setHeader("Access-Control-Allow-Headers", String.join(",", allowedHeaders));
            response.setHeader("Access-Control-Expose-Headers", String.join(",", exposedHeaders));
            response.setHeader("Access-Control-Max-Age", String.valueOf(maxAge));

            if (allowCredentials) {
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }

            // Journaliser les requêtes CORS (en mode DEBUG)
            log.debug("Requête CORS autorisée depuis l'origine: {}", origin);
        } else if (origin != null) {
            // Journaliser les requêtes CORS rejetées (en mode WARN)
            log.warn("Requête CORS rejetée depuis l'origine non autorisée: {}", origin);
        }

        // Pour les requêtes OPTIONS (pré-vérification CORS), renvoyer OK immédiatement
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Pour les autres requêtes, continuer la chaîne de filtres
            chain.doFilter(req, res);
        }
    }

    /**
     * Vérifie si l'origine est autorisée.
     *
     * @param origin Origine de la requête
     * @return true si l'origine est autorisée, false sinon
     */
    private boolean isAllowedOrigin(String origin) {
        return allowedOrigins.contains(origin) || allowedOrigins.contains("*");
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("Initialisation du filtre CORS personnalisé");
        log.info("Origines autorisées: {}", allowedOrigins);
        log.info("Méthodes autorisées: {}", allowedMethods);
        log.info("En-têtes autorisés: {}", allowedHeaders);
        log.info("En-têtes exposés: {}", exposedHeaders);
        log.info("Autorisation des cookies: {}", allowCredentials);
        log.info("Durée de mise en cache: {} secondes", maxAge);
    }

    @Override
    public void destroy() {
        log.info("Destruction du filtre CORS personnalisé");
    }
}
