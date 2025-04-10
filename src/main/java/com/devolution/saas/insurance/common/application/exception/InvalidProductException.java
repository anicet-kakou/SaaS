package com.devolution.saas.insurance.common.application.exception;

/**
 * Exception levée lorsqu'un produit d'assurance est invalide.
 */
public class InvalidProductException extends InsuranceBusinessException {

    private static final String DEFAULT_CODE = "insurance.product.invalid";
    private static final String DEFAULT_MESSAGE = "Produit d'assurance invalide";

    /**
     * Constructeur par défaut.
     */
    public InvalidProductException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message.
     *
     * @param message Le message d'erreur
     */
    public InvalidProductException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public InvalidProductException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message Le message d'erreur
     * @param cause   La cause de l'erreur
     */
    public InvalidProductException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    /**
     * Crée une exception pour un produit invalide avec des erreurs de validation.
     *
     * @param errors La liste des erreurs de validation
     * @return L'exception créée
     */
    public static InvalidProductException withValidationErrors(java.util.List<String> errors) {
        return new InvalidProductException(DEFAULT_CODE, "Le produit est invalide: " + String.join(", ", errors));
    }
}
