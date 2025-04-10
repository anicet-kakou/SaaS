package com.devolution.saas.insurance.common.application.exception;

/**
 * Exception levée lorsqu'une police d'assurance est invalide.
 */
public class InvalidPolicyException extends InsuranceBusinessException {

    private static final String DEFAULT_CODE = "insurance.policy.invalid";
    private static final String DEFAULT_MESSAGE = "Police d'assurance invalide";

    /**
     * Constructeur par défaut.
     */
    public InvalidPolicyException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message.
     *
     * @param message Le message d'erreur
     */
    public InvalidPolicyException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public InvalidPolicyException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message Le message d'erreur
     * @param cause   La cause de l'erreur
     */
    public InvalidPolicyException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    /**
     * Crée une exception pour une police invalide avec des erreurs de validation.
     *
     * @param errors La liste des erreurs de validation
     * @return L'exception créée
     */
    public static InvalidPolicyException withValidationErrors(java.util.List<String> errors) {
        return new InvalidPolicyException(DEFAULT_CODE, "La police est invalide: " + String.join(", ", errors));
    }
}
