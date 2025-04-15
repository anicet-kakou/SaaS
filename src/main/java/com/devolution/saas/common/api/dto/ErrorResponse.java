package com.devolution.saas.common.api.dto;

import com.devolution.saas.common.domain.exception.ValidationException.ValidationError;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO pour les réponses d'erreur standardisées.
 * Utilisé par le GlobalExceptionHandler pour renvoyer des informations d'erreur cohérentes.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    /**
     * Code d'erreur.
     */
    String code,

    /**
     * Message d'erreur.
     */
    String message,

    /**
     * Statut HTTP.
     */
    int status,

    /**
     * Chemin de la requête.
     */
    String path,

    /**
     * Horodatage de l'erreur.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    LocalDateTime timestamp,

    /**
     * Liste des erreurs de validation (pour les erreurs de validation).
     */
    List<FieldError> errors,

    /**
     * ID de corrélation pour le suivi des erreurs.
     */
    String correlationId,

    /**
     * Trace de la pile (uniquement en développement).
     */
    String trace
) {
    /**
     * Constructeur compact avec valeurs par défaut pour les collections.
     */
    public ErrorResponse {
        errors = errors != null ? errors : new ArrayList<>();
    }

    /**
     * Builder pour ErrorResponse.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Ajoute une erreur de champ.
     *
     * @param field   Nom du champ
     * @param message Message d'erreur
     * @return Une nouvelle instance d'ErrorResponse avec l'erreur ajoutée
     */
    public ErrorResponse addFieldError(String field, String message) {
        List<FieldError> newErrors = new ArrayList<>(this.errors);
        newErrors.add(new FieldError(field, message));
        return new ErrorResponse(code, this.message, status, path, timestamp, newErrors, correlationId, trace);
    }

    /**
     * Ajoute une liste d'erreurs de validation.
     *
     * @param validationErrors Liste d'erreurs de validation
     * @return Une nouvelle instance d'ErrorResponse avec les erreurs ajoutées
     */
    public ErrorResponse addValidationErrors(List<ValidationError> validationErrors) {
        List<FieldError> newErrors = new ArrayList<>(this.errors);
        validationErrors.forEach(error ->
                newErrors.add(new FieldError(error.getField(), error.getMessage()))
        );
        return new ErrorResponse(code, this.message, status, path, timestamp, newErrors, correlationId, trace);
    }

    /**
     * Classe Builder pour ErrorResponse.
     */
    public static class Builder {
        private String code;
        private String message;
        private int status;
        private String path;
        private LocalDateTime timestamp;
        private List<FieldError> errors = new ArrayList<>();
        private String correlationId;
        private String trace;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Définit la liste des erreurs de validation.
         *
         * @param errors Liste des erreurs de validation
         * @return Cette instance du Builder pour chaînage
         */
        public Builder errors(List<FieldError> errors) {
            if (errors != null) {
                this.errors = new ArrayList<>(errors);
            }
            return this;
        }

        /**
         * Ajoute une erreur de validation.
         *
         * @param field   Nom du champ en erreur
         * @param message Message d'erreur
         * @return Cette instance du Builder pour chaînage
         */
        public Builder addError(String field, String message) {
            if (field != null) {
                this.errors.add(new FieldError(field, message));
            }
            return this;
        }

        /**
         * Ajoute une erreur de validation.
         *
         * @param error Erreur de validation à ajouter
         * @return Cette instance du Builder pour chaînage
         */
        public Builder addError(FieldError error) {
            if (error != null) {
                this.errors.add(error);
            }
            return this;
        }

        /**
         * Ajoute plusieurs erreurs de validation.
         *
         * @param errors Liste des erreurs de validation à ajouter
         * @return Cette instance du Builder pour chaînage
         */
        public Builder addErrors(List<FieldError> errors) {
            if (errors != null) {
                this.errors.addAll(errors);
            }
            return this;
        }

        /**
         * Ajoute plusieurs erreurs de validation depuis des ValidationError.
         *
         * @param validationErrors Liste des erreurs de validation à ajouter
         * @return Cette instance du Builder pour chaînage
         */
        public Builder addValidationErrors(List<ValidationError> validationErrors) {
            if (validationErrors != null) {
                validationErrors.forEach(error ->
                        this.errors.add(new FieldError(error.getField(), error.getMessage()))
                );
            }
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder trace(String trace) {
            this.trace = trace;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(
                    code, message, status, path, timestamp, errors, correlationId, trace
            );
        }
    }

    /**
     * Classe interne représentant une erreur de champ.
     */
    public record FieldError(
        /**
         * Nom du champ.
         */
        String field,

        /**
         * Message d'erreur.
         */
        String message
    ) {
    }
}
