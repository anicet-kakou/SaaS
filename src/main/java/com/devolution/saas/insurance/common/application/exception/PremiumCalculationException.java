package com.devolution.saas.insurance.common.application.exception;

/**
 * Exception levée lorsqu'une erreur survient lors du calcul de la prime.
 */
public class PremiumCalculationException extends InsuranceBusinessException {

    private static final String DEFAULT_CODE = "insurance.premium.calculation.error";
    private static final String DEFAULT_MESSAGE = "Erreur lors du calcul de la prime";

    /**
     * Constructeur par défaut.
     */
    public PremiumCalculationException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message.
     *
     * @param message Le message d'erreur
     */
    public PremiumCalculationException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public PremiumCalculationException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message Le message d'erreur
     * @param cause   La cause de l'erreur
     */
    public PremiumCalculationException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    /**
     * Constructeur avec code, message et cause.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     * @param cause   Cause de l'exception
     */
    public PremiumCalculationException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
