package com.devolution.saas.common.domain.exception;

/**
 * Exception lancée lors d'erreurs de concurrence.
 */
public class ConcurrencyException extends BusinessException {

    private static final String DEFAULT_CODE = "concurrency.error";
    private static final String DEFAULT_MESSAGE = "Erreur de concurrence";

    /**
     * Constructeur par défaut.
     */
    public ConcurrencyException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message personnalisé.
     *
     * @param message Message d'erreur personnalisé
     */
    public ConcurrencyException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public ConcurrencyException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur pour conflit d'optimistic locking.
     *
     * @param entityName Nom de l'entité
     * @param entityId   ID de l'entité
     * @return Exception pour conflit d'optimistic locking
     */
    public static ConcurrencyException optimisticLockingConflict(String entityName, Object entityId) {
        return new ConcurrencyException(
                "concurrency.optimistic.locking",
                "L'entité " + entityName + " avec l'identifiant " + entityId + " a été modifiée par un autre utilisateur"
        );
    }

    /**
     * Constructeur pour conflit de version.
     *
     * @param entityName    Nom de l'entité
     * @param entityId      ID de l'entité
     * @param version       Version attendue
     * @param actualVersion Version actuelle
     * @return Exception pour conflit de version
     */
    public static ConcurrencyException versionConflict(String entityName, Object entityId, long version, long actualVersion) {
        return new ConcurrencyException(
                "concurrency.version.conflict",
                "L'entité " + entityName + " avec l'identifiant " + entityId + " a une version différente de celle attendue. " +
                        "Version attendue: " + version + ", version actuelle: " + actualVersion
        );
    }
}
