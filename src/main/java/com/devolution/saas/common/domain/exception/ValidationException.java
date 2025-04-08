package com.devolution.saas.common.domain.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception lancée lors d'erreurs de validation.
 */
@Getter
public class ValidationException extends BusinessException {

    private static final String DEFAULT_CODE = "validation.error";
    private static final String DEFAULT_MESSAGE = "Erreur de validation";

    private final List<ValidationError> errors;

    /**
     * Constructeur par défaut.
     */
    public ValidationException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
        this.errors = new ArrayList<>();
    }

    /**
     * Constructeur avec message personnalisé.
     *
     * @param message Message d'erreur personnalisé
     */
    public ValidationException(String message) {
        super(DEFAULT_CODE, message);
        this.errors = new ArrayList<>();
    }

    /**
     * Constructeur avec liste d'erreurs de validation.
     *
     * @param errors Liste d'erreurs de validation
     */
    public ValidationException(List<ValidationError> errors) {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
        this.errors = errors;
    }

    /**
     * Ajoute une erreur de validation.
     *
     * @param field   Champ en erreur
     * @param message Message d'erreur
     * @return Cette instance pour chaînage
     */
    public ValidationException addError(String field, String message) {
        this.errors.add(new ValidationError(field, message));
        return this;
    }

    /**
     * Classe interne représentant une erreur de validation.
     */
    @Getter
    public static class ValidationError {
        private final String field;
        private final String message;

        /**
         * Constructeur.
         *
         * @param field   Champ en erreur
         * @param message Message d'erreur
         */
        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
