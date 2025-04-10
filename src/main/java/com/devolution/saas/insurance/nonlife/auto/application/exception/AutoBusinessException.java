package com.devolution.saas.insurance.nonlife.auto.application.exception;

import com.devolution.saas.insurance.common.application.exception.InsuranceBusinessException;

/**
 * Exception de base pour les erreurs métier spécifiques au module auto.
 */
public class AutoBusinessException extends InsuranceBusinessException {

    private static final String DEFAULT_CODE = "insurance.auto.error";
    private static final String DEFAULT_MESSAGE = "Erreur métier du module auto";

    /**
     * Constructeur par défaut.
     */
    public AutoBusinessException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message.
     *
     * @param message Le message d'erreur
     */
    public AutoBusinessException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public AutoBusinessException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message Le message d'erreur
     * @param cause   La cause de l'erreur
     */
    public AutoBusinessException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    /**
     * Constructeur avec code, message et cause.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     * @param cause   Cause de l'exception
     */
    public AutoBusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
