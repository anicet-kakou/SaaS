package com.devolution.saas.insurance.nonlife.auto.application.dto;

import com.devolution.saas.insurance.common.domain.model.InsuranceProduct.ProductStatus;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO pour les produits d'assurance auto.
 */
public record AutoInsuranceProductDTO(
        UUID id,
        String code,
        String name,
        String description,
        ProductStatus status,
        LocalDate effectiveDate,
        LocalDate expiryDate,
        UUID organizationId
) {
    /**
     * Builder for AutoInsuranceProductDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for AutoInsuranceProductDTO.
     */
    public static class Builder {
        private UUID id;
        private String code;
        private String name;
        private String description;
        private ProductStatus status;
        private LocalDate effectiveDate;
        private LocalDate expiryDate;
        private UUID organizationId;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

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

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public AutoInsuranceProductDTO build() {
            return new AutoInsuranceProductDTO(
                    id, code, name, description, status, effectiveDate, expiryDate, organizationId
            );
        }
    }
}
