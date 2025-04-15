package com.devolution.saas.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitaires pour la gestion des exceptions.
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
        // Classe utilitaire, ne doit pas être instanciée
    }

    /**
     * Récupère la trace de la pile d'une exception.
     *
     * @param ex Exception
     * @return Trace de la pile
     */
    public static String getStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Récupère l'adresse IP du client.
     *
     * @param request Requête HTTP
     * @return Adresse IP du client
     */
    public static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Récupère le nom d'utilisateur de l'utilisateur authentifié.
     *
     * @return Nom d'utilisateur ou "anonymous" si non authentifié
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }
        return authentication.getName();
    }

    /**
     * Récupère les informations de contexte de la requête.
     *
     * @param request Requête HTTP
     * @return Map des informations de contexte
     */
    public static Map<String, String> getContextInfo(HttpServletRequest request) {
        Map<String, String> contextInfo = new HashMap<>();
        contextInfo.put("method", request.getMethod());
        contextInfo.put("uri", request.getRequestURI());
        contextInfo.put("clientIp", getClientIp(request));
        contextInfo.put("userAgent", request.getHeader("User-Agent"));
        contextInfo.put("username", getCurrentUsername());
        return contextInfo;
    }

    /**
     * Récupère la requête HTTP courante.
     *
     * @return Requête HTTP ou null si non disponible
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
