package com.devolution.saas.common.domain.exception;

/**
 * Exception lancée lors d'erreurs de sécurité.
 */
public class SecurityException extends BusinessException {

    private static final String DEFAULT_CODE = "security.error";
    private static final String DEFAULT_MESSAGE = "Erreur de sécurité";

    /**
     * Constructeur par défaut.
     */
    public SecurityException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message personnalisé.
     *
     * @param message Message d'erreur personnalisé
     */
    public SecurityException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public SecurityException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec code, message et cause.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     * @param cause   Cause de l'exception
     */
    public SecurityException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
