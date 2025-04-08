package com.devolution.saas.common.filter;

import com.devolution.saas.core.audit.application.service.AuditService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtre pour la journalisation des requêtes HTTP.
 * Ce filtre enregistre les détails de chaque requête et réponse HTTP.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final AuditService auditService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Génération d'un ID de requête unique
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        // Enregistrement de l'heure de début
        long startTime = System.currentTimeMillis();

        // Wrapping de la requête et de la réponse pour pouvoir lire leur contenu
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            // Journalisation de la requête entrante
            logRequest(requestWrapper);

            // Exécution de la chaîne de filtres
            filterChain.doFilter(requestWrapper, responseWrapper);

            // Journalisation de la réponse sortante
            logResponse(requestWrapper, responseWrapper, System.currentTimeMillis() - startTime);
        } finally {
            // Copie du contenu de la réponse dans la réponse originale
            responseWrapper.copyBodyToResponse();

            // Nettoyage du MDC
            MDC.remove("requestId");
        }
    }

    /**
     * Journalise les détails de la requête HTTP.
     *
     * @param request Requête HTTP
     */
    private void logRequest(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        log.info("Requête entrante: {} {} (IP: {}, User-Agent: {})",
                method,
                queryString != null ? uri + "?" + queryString : uri,
                clientIp,
                userAgent);
    }

    /**
     * Journalise les détails de la réponse HTTP.
     *
     * @param request       Requête HTTP
     * @param response      Réponse HTTP
     * @param executionTime Temps d'exécution en millisecondes
     */
    private void logResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long executionTime) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        log.info("Réponse sortante: {} {} - {} ({} ms)",
                method,
                uri,
                status,
                executionTime);

        // Audit des requêtes API
        if (uri.startsWith("/api/") && !uri.contains("/auth/")) {
            String action = "API_REQUEST";
            String description = method + " " + uri;
            String entityType = "API";
            String entityId = uri;

            // Récupération de l'utilisateur authentifié
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = null;
            UUID userId = null;

            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
            }

            // Création du log d'audit
            auditService.createAuditLog(
                    action,
                    description,
                    entityType,
                    entityId,
                    null,
                    null
            );
        }
    }

    /**
     * Récupère l'adresse IP du client.
     *
     * @param request Requête HTTP
     * @return Adresse IP du client
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/actuator/") ||
                path.contains("/swagger-ui/") ||
                path.contains("/v3/api-docs") ||
                path.contains("/favicon.ico");
    }
}
