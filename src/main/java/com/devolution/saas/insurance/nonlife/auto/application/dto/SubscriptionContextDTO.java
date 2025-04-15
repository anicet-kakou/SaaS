package com.devolution.saas.insurance.nonlife.auto.application.dto;

import java.util.UUID;

/**
 * DTO pour le contexte de souscription d'assurance auto.
 */
public record SubscriptionContextDTO(
        UUID customerId,
        UUID vehicleId,
        UUID driverId,
        int driverAge,
        int drivingExperienceYears,
        boolean hasPreviousClaims,
        UUID organizationId
) {
    /**
     * Builder pour SubscriptionContextDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour SubscriptionContextDTO.
     */
    public static class Builder {
        private UUID customerId;
        private UUID vehicleId;
        private UUID driverId;
        private int driverAge;
        private int drivingExperienceYears;
        private boolean hasPreviousClaims;
        private UUID organizationId;

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder vehicleId(UUID vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder driverId(UUID driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder driverAge(int driverAge) {
            this.driverAge = driverAge;
            return this;
        }

        public Builder drivingExperienceYears(int drivingExperienceYears) {
            this.drivingExperienceYears = drivingExperienceYears;
            return this;
        }

        public Builder hasPreviousClaims(boolean hasPreviousClaims) {
            this.hasPreviousClaims = hasPreviousClaims;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public SubscriptionContextDTO build() {
            return new SubscriptionContextDTO(
                    customerId, vehicleId, driverId, driverAge, drivingExperienceYears, hasPreviousClaims, organizationId
            );
        }
    }
}
