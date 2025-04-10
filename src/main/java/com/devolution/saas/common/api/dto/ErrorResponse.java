package com.devolution.saas.common.api.dto;

import com.devolution.saas.common.domain.exception.ValidationException.ValidationError;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO pour les réponses d'erreur standardisées.
 * Utilisé par le GlobalExceptionHandler pour renvoyer des informations d'erreur cohérentes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Code d'erreur.
     */
    private String code;

    /**
     * Message d'erreur.
     */
    private String message;

    /**
     * Statut HTTP.
     */
    private int status;

    /**
     * Chemin de la requête.
     */
    private String path;

    /**
     * Horodatage de l'erreur.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    /**
     * Liste des erreurs de validation (pour les erreurs de validation).
     */
    @Builder.Default
    private List<FieldError> errors = new ArrayList<>();

    /**
     * Trace de la pile (uniquement en développement).
     */
    private String trace;

    /**
     * Ajoute une erreur de champ.
     *
     * @param field   Nom du champ
     * @param message Message d'erreur
     * @return Cette instance pour chaînage
     */
    public ErrorResponse addFieldError(String field, String message) {
        this.errors.add(new FieldError(field, message));
        return this;
    }

    /**
     * Ajoute une liste d'erreurs de validation.
     *
     * @param validationErrors Liste d'erreurs de validation
     * @return Cette instance pour chaînage
     */
    public ErrorResponse addValidationErrors(List<ValidationError> validationErrors) {
        validationErrors.forEach(error ->
                this.errors.add(new FieldError(error.getField(), error.getMessage()))
        );
        return this;
    }

    /**
     * Classe interne représentant une erreur de champ.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        /**
         * Nom du champ.
         */
        private String field;

        /**
         * Message d'erreur.
         */
        private String message;
    }
}
