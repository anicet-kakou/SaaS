package com.devolution.saas.insurance.nonlife.auto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation de l'interface InsuranceProduct pour l'assurance auto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoInsuranceProduct {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private ProductStatus status;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private UUID organizationId;

    /**
     * Calcule la prime d'assurance auto.
     *
     * @param context Le contexte de calcul de la police
     * @return Le montant de la prime
     */
    public BigDecimal calculatePremium(PolicyCalculationContext context) {
        // Logique spécifique au calcul de prime auto
        // Prise en compte du type de véhicule, de l'usage, du bonus-malus, etc.
        return BigDecimal.ZERO; // À implémenter
    }

    /**
     * Valide la souscription à l'assurance auto.
     *
     * @param context Le contexte de souscription
     * @return true si la souscription est valide, false sinon
     */
    public boolean validateSubscription(SubscriptionContext context) {
        // Validation spécifique à l'assurance auto
        // Vérification de l'âge du conducteur, du permis, etc.
        return false; // À implémenter
    }

    /**
     * Statut d'un produit d'assurance.
     */
    public enum ProductStatus {
        DRAFT,
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    /**
     * Contexte de calcul de police.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PolicyCalculationContext {
        private UUID vehicleId;
        private UUID driverId;
        private String coverageType;
        private BigDecimal bonusMalusCoefficient;
        private List<String> selectedCoverages;
        private UUID organizationId;
    }

    /**
     * Contexte de souscription.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionContext {
        private UUID customerId;
        private UUID vehicleId;
        private UUID driverId;
        private int driverAge;
        private int drivingExperienceYears;
        private boolean hasPreviousClaims;
        private UUID organizationId;
    }
}
