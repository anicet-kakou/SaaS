package com.devolution.saas.common.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtre qui génère et gère un ID de corrélation pour chaque requête HTTP.
 * L'ID de corrélation est utilisé pour suivre une requête à travers différents composants et services.
 */
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId = getCorrelationId(request);

        // Ajouter l'ID de corrélation au contexte MDC pour la journalisation
        MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

        // Ajouter l'ID de corrélation à la réponse
        response.addHeader(CORRELATION_ID_HEADER, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Nettoyer le contexte MDC après le traitement de la requête
            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }

    /**
     * Récupère l'ID de corrélation de la requête ou en génère un nouveau si nécessaire.
     *
     * @param request La requête HTTP
     * @return L'ID de corrélation
     */
    private String getCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = generateCorrelationId();
        }

        return correlationId;
    }

    /**
     * Génère un nouvel ID de corrélation.
     *
     * @return Un nouvel ID de corrélation
     */
    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
