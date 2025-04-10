package com.devolution.saas.common.api.context;

import com.devolution.saas.common.api.filter.CorrelationIdFilter;
import org.slf4j.MDC;

/**
 * Classe utilitaire pour accéder à l'ID de corrélation depuis n'importe où dans l'application.
 */
public class CorrelationIdContext {

    private CorrelationIdContext() {
        // Classe utilitaire, ne doit pas être instanciée
    }

    /**
     * Récupère l'ID de corrélation actuel.
     *
     * @return L'ID de corrélation ou null s'il n'est pas défini
     */
    public static String getCorrelationId() {
        return MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
    }

    /**
     * Définit l'ID de corrélation dans le contexte MDC.
     * Utile pour les opérations asynchrones ou les threads séparés.
     *
     * @param correlationId L'ID de corrélation à définir
     */
    public static void setCorrelationId(String correlationId) {
        if (correlationId != null) {
            MDC.put(CorrelationIdFilter.CORRELATION_ID_MDC_KEY, correlationId);
        }
    }

    /**
     * Supprime l'ID de corrélation du contexte MDC.
     * Doit être appelé après le traitement dans un thread séparé.
     */
    public static void clearCorrelationId() {
        MDC.remove(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
    }
}
