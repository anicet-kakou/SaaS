package com.devolution.saas.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * Utilitaires pour les requêtes HTTP.
 */
public class HttpRequestUtils {

    /**
     * Récupère l'adresse IP du client.
     *
     * @param request Requête HTTP
     * @return Adresse IP du client
     */
    public static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Extrait la clé API de la requête HTTP.
     *
     * @param request Requête HTTP
     * @return Clé API ou null
     */
    public static String resolveApiKey(HttpServletRequest request) {
        // Vérification dans l'en-tête X-API-Key
        String apiKey = request.getHeader("X-API-Key");
        if (StringUtils.hasText(apiKey)) {
            return apiKey;
        }

        // Vérification dans le paramètre de requête api_key
        return request.getParameter("api_key");
    }

    /**
     * Extrait le jeton JWT de la requête HTTP.
     *
     * @param request Requête HTTP
     * @return Jeton JWT ou null
     */
    public static String resolveJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Vérifie si la requête doit être filtrée (exclue du traitement).
     *
     * @param request Requête HTTP
     * @return true si la requête doit être filtrée, false sinon
     */
    public static boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/actuator/") ||
                path.contains("/swagger-ui/") ||
                path.contains("/v3/api-docs") ||
                path.contains("/favicon.ico");
    }
}
