package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO pour les catégories de véhicule.
 */
public record VehicleCategoryDTO(
        UUID id,
        String code,
        String name,
        String description,
        BigDecimal tariffCoefficient,
        boolean isActive,
        UUID organizationId
) {
    /**
     * Builder for VehicleCategoryDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for VehicleCategoryDTO.
     */
    public static class Builder {
        private UUID id;
        private String code;
        private String name;
        private String description;
        private BigDecimal tariffCoefficient;
        private boolean isActive;
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

        public Builder tariffCoefficient(BigDecimal tariffCoefficient) {
            this.tariffCoefficient = tariffCoefficient;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public VehicleCategoryDTO build() {
            return new VehicleCategoryDTO(
                    id, code, name, description, tariffCoefficient, isActive, organizationId
            );
        }
    }
}
