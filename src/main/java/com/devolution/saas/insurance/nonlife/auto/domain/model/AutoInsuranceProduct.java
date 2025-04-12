package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.insurance.common.domain.model.Coverage;
import com.devolution.saas.insurance.common.domain.model.InsuranceProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AutoInsuranceProduct extends TenantAwareEntity implements InsuranceProduct {

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
    private InsuranceProduct.ProductStatus status;

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

    // The organizationId field is inherited from TenantAwareEntity

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

    // ProductStatus enum is now used from InsuranceProduct interface

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
    @SuperBuilder
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
    @SuperBuilder
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
