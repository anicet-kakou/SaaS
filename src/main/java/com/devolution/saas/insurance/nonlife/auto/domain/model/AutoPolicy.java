package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import com.devolution.saas.insurance.common.domain.model.Coverage;
import com.devolution.saas.insurance.common.domain.model.Policy;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant une police d'assurance automobile.
 */
@Entity
@Table(name = "auto_policies")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AutoPolicy extends BaseEntity implements Policy {

    /**
     * Numéro de police.
     */
    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    /**
     * Statut de la police.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;

    /**
     * Date de début de la police.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Date de fin de la police.
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * Montant de la prime.
     */
    @Column(name = "premium_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal premiumAmount;

    /**
     * ID du véhicule assuré.
     */
    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    /**
     * ID du conducteur principal.
     */
    @Column(name = "primary_driver_id", nullable = false)
    private UUID primaryDriverId;

    /**
     * Type de couverture.
     */
    @Column(name = "coverage_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CoverageType coverageType;

    /**
     * Coefficient de bonus-malus.
     */
    @Column(name = "bonus_malus_coefficient", nullable = false, precision = 5, scale = 2)
    private BigDecimal bonusMalusCoefficient;

    /**
     * Kilométrage annuel.
     */
    @Column(name = "annual_mileage")
    private Integer annualMileage;

    /**
     * Type de stationnement.
     */
    @Column(name = "parking_type")
    @Enumerated(EnumType.STRING)
    private ParkingType parkingType;

    /**
     * Indique si le véhicule est équipé d'un dispositif antivol.
     */
    @Column(name = "has_anti_theft_device", nullable = false)
    private boolean hasAntiTheftDevice;

    /**
     * ID de la catégorie d'historique de sinistres.
     */
    @Column(name = "claim_history_category_id", nullable = false)
    private UUID claimHistoryCategoryId;

    /**
     * ID de l'organisation.
     */
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    /**
     * ID du produit d'assurance.
     */
    @Column(name = "product_id")
    private UUID productId;

    /**
     * ID du client.
     */
    @Column(name = "customer_id")
    private UUID customerId;

    /**
     * Types de couverture pour une police auto.
     */
    public enum CoverageType {
        THIRD_PARTY,
        COMPREHENSIVE
    }

    /**
     * Types de stationnement pour un véhicule.
     */
    public enum ParkingType {
        GARAGE,
        STREET,
        PARKING_LOT
    }

    @Override
    @Transient
    public List<Coverage> getSelectedCoverages() {
        // Retourner la liste des garanties sélectionnées pour cette police
        return new ArrayList<>(); // À implémenter
    }

    @Override
    @Transient
    public BigDecimal calculatePremium() {
        // Calcul de la prime pour cette police
        return this.premiumAmount; // Pour l'instant, on retourne simplement la prime stockée
    }

    @Override
    @Transient
    public Policy renew(LocalDate newStartDate, LocalDate newEndDate) {
        // Création d'une nouvelle police basée sur celle-ci
        return AutoPolicy.builder()
                .policyNumber(this.policyNumber + "-R") // Suffixe pour indiquer un renouvellement
                .status(PolicyStatus.DRAFT)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .premiumAmount(this.premiumAmount) // À recalculer en pratique
                .vehicleId(this.vehicleId)
                .primaryDriverId(this.primaryDriverId)
                .coverageType(this.coverageType)
                .bonusMalusCoefficient(this.bonusMalusCoefficient)
                .annualMileage(this.annualMileage)
                .parkingType(this.parkingType)
                .hasAntiTheftDevice(this.hasAntiTheftDevice)
                .claimHistoryCategoryId(this.claimHistoryCategoryId)
                .productId(this.productId)
                .customerId(this.customerId)
                .organizationId(this.organizationId)
                .build();
    }

    @Override
    @Transient
    public Policy cancel(LocalDate cancellationDate, String reason) {
        // Annulation de la police
        this.status = PolicyStatus.CANCELLED;
        // En pratique, on enregistrerait aussi la date d'annulation et la raison
        return this;
    }

    /**
     * Statuts possibles d'une police.
     */
    public enum PolicyStatus {
        DRAFT,
        ACTIVE,
        EXPIRED,
        CANCELLED,
        SUSPENDED
    }
}
