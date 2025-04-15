package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.insurance.common.domain.model.Coverage;
import com.devolution.saas.insurance.common.domain.model.InsuranceProduct;
import com.devolution.saas.insurance.nonlife.auto.application.dto.PolicyCalculationContextDTO;
import com.devolution.saas.insurance.nonlife.auto.application.dto.SubscriptionContextDTO;
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
     * @param autoContext Le contexte de calcul de la police
     * @return Le montant de la prime
     */
    @Transient
    public BigDecimal calculatePremium(PolicyCalculationContextDTO autoContext) {
        // Logique spécifique au calcul de prime auto
        // Prise en compte du type de véhicule, de l'usage, du bonus-malus, etc.
        return BigDecimal.ZERO; // À implémenter
    }

    @Override
    @Transient
    public BigDecimal calculatePremium(InsuranceProduct.PolicyCalculationContext context) {
        // Adapter le contexte générique au contexte spécifique auto
        return calculatePremium(PolicyCalculationContextDTO.builder()
                .vehicleId(null) // vehicleId
                .driverId(null) // driverId
                .coverageType(null) // coverageType
                .bonusMalusCoefficient(BigDecimal.ONE) // bonusMalusCoefficient
                .selectedCoverages(new ArrayList<>()) // selectedCoverages
                .organizationId(context.getOrganizationId())
                .build());
    }

    /**
     * Valide la souscription à l'assurance auto.
     *
     * @param context Le contexte de souscription
     * @return true si la souscription est valide, false sinon
     */
    @Transient
    public boolean validateSubscription(SubscriptionContextDTO autoContext) {
        // Validation spécifique à l'assurance auto
        // Vérification de l'âge du conducteur, du permis, etc.
        return false; // À implémenter
    }

    @Override
    @Transient
    public boolean validateSubscription(InsuranceProduct.SubscriptionContext context) {
        // Adapter le contexte générique au contexte spécifique auto
        return validateSubscription(SubscriptionContextDTO.builder()
                .customerId(context.getCustomerId())
                .vehicleId(null) // vehicleId
                .driverId(null) // driverId
                .driverAge(0) // driverAge
                .drivingExperienceYears(0) // drivingExperienceYears
                .hasPreviousClaims(false) // hasPreviousClaims
                .organizationId(context.getOrganizationId())
                .build());
    }

    // ProductStatus enum is now used from InsuranceProduct interface

    @Override
    @Transient
    public List<Coverage> getAvailableCoverages() {
        // Retourner la liste des garanties disponibles pour ce produit
        return new ArrayList<>(); // À implémenter
    }

    // Les classes PolicyCalculationContext et SubscriptionContext ont été déplacées
    // vers des classes DTO dédiées: PolicyCalculationContextDTO et SubscriptionContextDTO
}
