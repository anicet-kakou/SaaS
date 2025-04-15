package com.devolution.saas.insurance.nonlife.auto.api.dto.request;

import com.devolution.saas.insurance.common.domain.model.InsuranceProduct.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Requête pour la création d'un produit d'assurance auto.
 */
public record CreateAutoInsuranceProductRequest(
        /**
         * Code unique du produit.
         */
        @NotBlank(message = "Le code est obligatoire")
        @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
        String code,

        /**
         * Nom du produit.
         */
        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
        String name,

        /**
         * Description du produit.
         */
        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
        String description,

        /**
         * Statut du produit.
         */
        @NotNull(message = "Le statut est obligatoire")
        ProductStatus status,

        /**
         * Date d'effet du produit.
         */
        @NotNull(message = "La date d'effet est obligatoire")
        LocalDate effectiveDate,

        /**
         * Date d'expiration du produit.
         */
        LocalDate expiryDate
) {
    /**
     * Builder pour CreateAutoInsuranceProductRequest.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour CreateAutoInsuranceProductRequest.
     */
    public static class Builder {
        private String code;
        private String name;
        private String description;
        private ProductStatus status;
        private LocalDate effectiveDate;
        private LocalDate expiryDate;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(ProductStatus status) {
            this.status = status;
            return this;
        }

        public Builder effectiveDate(LocalDate effectiveDate) {
            this.effectiveDate = effectiveDate;
            return this;
        }

        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public CreateAutoInsuranceProductRequest build() {
            return new CreateAutoInsuranceProductRequest(
                    code, name, description, status, effectiveDate, expiryDate
            );
        }
    }
}
