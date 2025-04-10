package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import com.devolution.saas.insurance.common.domain.model.Coverage;
import com.devolution.saas.insurance.common.domain.model.InsuranceProduct;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation de l'interface InsuranceProduct pour l'assurance auto.
 */
@Entity
@Table(name = "auto_insurance_products")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AutoInsuranceProduct extends BaseEntity implements InsuranceProduct {

    /**
     * Code unique du produit.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom du produit.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description du produit.
     */
    @Column(name = "description")
    private String description;

    /**
     * Statut du produit.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    /**
     * Date d'effet du produit.
     */
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    /**
     * Date d'expiration du produit.
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /**
     * ID de l'organisation.
     */
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    /**
     * Calcule la prime d'assurance auto.
     *
     * @param context Le contexte de calcul de la police
     * @return Le montant de la prime
     */
    @Transient
    public BigDecimal calculatePremium(PolicyCalculationContext autoContext) {
        // Logique spécifique au calcul de prime auto
        // Prise en compte du type de véhicule, de l'usage, du bonus-malus, etc.
        return BigDecimal.ZERO; // À implémenter
    }

    @Override
    @Transient
    public BigDecimal calculatePremium(InsuranceProduct.PolicyCalculationContext context) {
        // Adapter le contexte générique au contexte spécifique auto
        return calculatePremium(new PolicyCalculationContext(
                null, // vehicleId
                null, // driverId
                null, // coverageType
                BigDecimal.ONE, // bonusMalusCoefficient
                new ArrayList<>(), // selectedCoverages
                context.getOrganizationId()
        ));
    }

    /**
     * Valide la souscription à l'assurance auto.
     *
     * @param context Le contexte de souscription
     * @return true si la souscription est valide, false sinon
     */
    @Transient
    public boolean validateSubscription(SubscriptionContext autoContext) {
        // Validation spécifique à l'assurance auto
        // Vérification de l'âge du conducteur, du permis, etc.
        return false; // À implémenter
    }

    @Override
    @Transient
    public boolean validateSubscription(InsuranceProduct.SubscriptionContext context) {
        // Adapter le contexte générique au contexte spécifique auto
        return validateSubscription(new SubscriptionContext(
                context.getCustomerId(),
                null, // vehicleId
                null, // driverId
                0, // driverAge
                0, // drivingExperienceYears
                false, // hasPreviousClaims
                context.getOrganizationId()
        ));
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

    @Override
    @Transient
    public List<Coverage> getAvailableCoverages() {
        // Retourner la liste des garanties disponibles pour ce produit
        return new ArrayList<>(); // À implémenter
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
