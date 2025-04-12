package com.devolution.saas.insurance.nonlife.auto.application.exception;

/**
 * Exception levée lorsqu'une règle métier du module auto est violée.
 */
public class BusinessRuleViolationException extends AutoBusinessException {

    private static final String DEFAULT_CODE = "insurance.auto.business.rule.violation";
    private static final String DEFAULT_MESSAGE = "Violation d'une règle métier du module auto";

    /**
     * Constructeur par défaut.
     */
    public BusinessRuleViolationException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message.
     *
     * @param message Le message d'erreur
     */
    public BusinessRuleViolationException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public BusinessRuleViolationException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message Le message d'erreur
     * @param cause   La cause de l'erreur
     */
    public BusinessRuleViolationException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }
}
