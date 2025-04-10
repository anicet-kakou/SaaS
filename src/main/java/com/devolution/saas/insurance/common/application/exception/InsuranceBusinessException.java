package com.devolution.saas.insurance.common.application.exception;

import com.devolution.saas.common.domain.exception.BusinessException;

/**
 * Exception levée lorsqu'une règle métier spécifique à l'assurance est violée.
 */
public class InsuranceBusinessException extends BusinessException {

    private static final String DEFAULT_CODE = "insurance.error";
    private static final String DEFAULT_MESSAGE = "Erreur métier d'assurance";

    /**
     * Constructeur par défaut.
     */
    public InsuranceBusinessException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message.
     *
     * @param message Le message d'erreur
     */
    public InsuranceBusinessException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public InsuranceBusinessException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message Le message d'erreur
     * @param cause   La cause de l'erreur
     */
    public InsuranceBusinessException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    /**
     * Constructeur avec code, message et cause.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     * @param cause   Cause de l'exception
     */
    public InsuranceBusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
