package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.insurance.common.domain.model.Coverage;
import com.devolution.saas.insurance.common.domain.model.Policy;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Entité représentant une police d'assurance automobile.
 */
@Entity
@Table(
        name = "auto_policies",
        indexes = {
                @Index(name = "idx_auto_policies_policy_number", columnList = "policy_number"),
                @Index(name = "idx_auto_policies_vehicle_id", columnList = "vehicle_id"),
                @Index(name = "idx_auto_policies_primary_driver_id", columnList = "primary_driver_id"),
                @Index(name = "idx_auto_policies_customer_id", columnList = "customer_id"),
                @Index(name = "idx_auto_policies_product_id", columnList = "product_id"),
                @Index(name = "idx_auto_policies_geographic_zone_id", columnList = "geographic_zone_id"),
                @Index(name = "idx_auto_policies_circulation_zone_id", columnList = "circulation_zone_id")
        }
)
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AutoPolicy extends TenantAwareEntity implements Policy {

    /**
     * Numéro de police.
     */
    @Column(name = "policy_number", nullable = false, length = 50)
    private String policyNumber;

    /**
     * Statut de la police.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Policy.PolicyStatus status;

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
     * Véhicule assuré.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vehicle_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_auto_policy_vehicle")
    )
    private Vehicle vehicle;

    /**
     * Conducteur principal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "primary_driver_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_auto_policy_primary_driver")
    )
    private Driver primaryDriver;

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
     * Note: Nous gardons l'ID car cette entité pourrait être dans un autre module.
     */
    @Column(name = "claim_history_category_id", nullable = false)
    private UUID claimHistoryCategoryId;

    // Note: Cette entité hérite du champ organizationId de TenantAwareEntity pour le support multi-tenant

    /**
     * ID du produit d'assurance.
     * Note: Nous gardons l'ID car cette entité pourrait être dans un autre module.
     */
    @Column(name = "product_id")
    private UUID productId;

    /**
     * ID du client.
     * Note: Nous gardons l'ID car cette entité est dans un autre module (Customer).
     */
    @Column(name = "customer_id")
    private UUID customerId;

    /**
     * Zone géographique de résidence du propriétaire.
     * Selon le Code CIMA, cette information est un critère de tarification.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "geographic_zone_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_auto_policy_geographic_zone")
    )
    private GeographicZone geographicZone;

    /**
     * Zone de circulation principale du véhicule.
     * Selon le Code CIMA, cette information est un critère de tarification.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "circulation_zone_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_auto_policy_circulation_zone")
    )
    private CirculationZone circulationZone;

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
        if (vehicle == null || primaryDriver == null) {
            // Si les données nécessaires ne sont pas disponibles, retourner la prime stockée
            return this.premiumAmount;
        }

        // Calcul de la prime de base en fonction du véhicule
        BigDecimal basePremium = calculateBasePremium();

        // Calcul des facteurs d'ajustement
        Map<String, BigDecimal> factors = calculateRiskFactors();

        // Calcul de la prime ajustée
        BigDecimal adjustedPremium = applyFactors(basePremium, factors);

        // Application du coefficient bonus-malus
        return applyBonusMalus(adjustedPremium);
    }

    @Override
    @Transient
    public Policy renew(LocalDate newStartDate, LocalDate newEndDate) {
        // Création d'une nouvelle police basée sur celle-ci
        AutoPolicy newPolicy = AutoPolicy.builder()
                .policyNumber(this.policyNumber + "-R") // Suffixe pour indiquer un renouvellement
                .status(Policy.PolicyStatus.DRAFT)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .premiumAmount(this.premiumAmount) // À recalculer en pratique
                .coverageType(this.coverageType)
                .bonusMalusCoefficient(this.bonusMalusCoefficient)
                .annualMileage(this.annualMileage)
                .parkingType(this.parkingType)
                .hasAntiTheftDevice(this.hasAntiTheftDevice)
                .claimHistoryCategoryId(this.claimHistoryCategoryId)
                .productId(this.productId)
                .customerId(this.customerId)
                .organizationId(this.getOrganizationId())
                .geographicZone(this.geographicZone)
                .circulationZone(this.circulationZone)
                .build();

        // Définir les IDs avec les setters
        newPolicy.setVehicleId(this.getVehicleId());
        newPolicy.setPrimaryDriverId(this.getPrimaryDriverId());

        return newPolicy;
    }

    @Override
    @Transient
    public Policy cancel(LocalDate cancellationDate, String reason) {
        // Annulation de la police
        this.status = Policy.PolicyStatus.CANCELLED;
        // En pratique, on enregistrerait aussi la date d'annulation et la raison
        return this;
    }

    // PolicyStatus enum is now used from Policy interface

    /**
     * Retourne l'ID du véhicule assuré.
     *
     * @return l'ID du véhicule
     */
    public UUID getVehicleId() {
        return vehicle != null ? vehicle.getId() : null;
    }

    /**
     * Définit le véhicule assuré à partir de son ID.
     *
     * @param vehicleId ID du véhicule
     */
    public void setVehicleId(UUID vehicleId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car le véhicule doit être défini comme une entité
    }


    /**
     * Retourne l'ID du conducteur principal.
     *
     * @return l'ID du conducteur principal
     */
    public UUID getPrimaryDriverId() {
        return primaryDriver != null ? primaryDriver.getId() : null;
    }

    /**
     * Définit le conducteur principal à partir de son ID.
     *
     * @param primaryDriverId ID du conducteur principal
     */
    public void setPrimaryDriverId(UUID primaryDriverId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car le conducteur doit être défini comme une entité
    }

    /**
     * Retourne l'ID de la zone géographique.
     *
     * @return l'ID de la zone géographique
     */
    public UUID getGeographicZoneId() {
        return geographicZone != null ? geographicZone.getId() : null;
    }

    /**
     * Définit la zone géographique à partir de son ID.
     *
     * @param geographicZoneId ID de la zone géographique
     */
    public void setGeographicZoneId(UUID geographicZoneId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car la zone géographique doit être définie comme une entité
    }

    /**
     * Retourne l'ID de la zone de circulation.
     *
     * @return l'ID de la zone de circulation
     */
    public UUID getCirculationZoneId() {
        return circulationZone != null ? circulationZone.getId() : null;
    }

    /**
     * Définit la zone de circulation à partir de son ID.
     *
     * @param circulationZoneId ID de la zone de circulation
     */
    public void setCirculationZoneId(UUID circulationZoneId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car la zone de circulation doit être définie comme une entité
    }

    /**
     * Calcule la prime de base en fonction du véhicule et du type de couverture.
     *
     * @return La prime de base
     */
    @Transient
    public BigDecimal calculateBasePremium() {
        // Valeurs par défaut pour la démonstration
        BigDecimal basePremium = new BigDecimal("500.00");

        // Ajustement selon la catégorie du véhicule
        if (vehicle.getCategoryId() != null) {
            // Ici, on pourrait récupérer le coefficient de la catégorie depuis une référence
            // Pour l'exemple, on utilise des valeurs codées en dur
            basePremium = basePremium.multiply(getCategoryFactor(vehicle.getCategoryId()));
        }

        // Ajustement selon l'usage du véhicule
        if (vehicle.getUsageId() != null) {
            basePremium = basePremium.multiply(getUsageFactor(vehicle.getUsageId()));
        }

        // Ajustement selon le type de couverture
        if (coverageType == CoverageType.COMPREHENSIVE) {
            basePremium = basePremium.multiply(new BigDecimal("1.5"));
        }

        return basePremium.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Calcule les facteurs de risque pour cette police.
     *
     * @return Une carte des facteurs de risque
     */
    @Transient
    public Map<String, BigDecimal> calculateRiskFactors() {
        Map<String, BigDecimal> factors = new HashMap<>();

        // Facteur lié à l'âge du véhicule
        int vehicleAge = java.time.LocalDate.now().getYear() - vehicle.getYear();
        if (vehicleAge < 3) {
            factors.put("vehicleAge", new BigDecimal("1.2"));
        } else if (vehicleAge > 10) {
            factors.put("vehicleAge", new BigDecimal("0.8"));
        } else {
            factors.put("vehicleAge", BigDecimal.ONE);
        }

        // Facteur lié à la puissance du moteur
        if (vehicle.getEnginePower() != null && vehicle.getEnginePower() > 100) {
            factors.put("enginePower", new BigDecimal("1.3"));
        } else {
            factors.put("enginePower", BigDecimal.ONE);
        }

        // Facteur lié à l'expérience du conducteur
        if (primaryDriver.getYearsOfDrivingExperience() < 2) {
            factors.put("driverExperience", new BigDecimal("1.5"));
        } else if (primaryDriver.getYearsOfDrivingExperience() > 10) {
            factors.put("driverExperience", new BigDecimal("0.9"));
        } else {
            factors.put("driverExperience", BigDecimal.ONE);
        }

        // Facteur lié au dispositif antivol
        if (hasAntiTheftDevice) {
            factors.put("antiTheftDevice", new BigDecimal("0.9"));
        } else {
            factors.put("antiTheftDevice", BigDecimal.ONE);
        }

        // Facteur lié au type de stationnement
        if (parkingType == ParkingType.GARAGE) {
            factors.put("parkingType", new BigDecimal("0.9"));
        } else if (parkingType == ParkingType.STREET) {
            factors.put("parkingType", new BigDecimal("1.1"));
        } else {
            factors.put("parkingType", BigDecimal.ONE);
        }

        // Facteur lié aux zones géographiques
        factors.put("geographicZone", getGeographicZoneFactor());
        factors.put("circulationZone", getCirculationZoneFactor());

        return factors;
    }

    /**
     * Applique les facteurs de risque à la prime de base.
     *
     * @param basePremium La prime de base
     * @param factors     Les facteurs de risque
     * @return La prime ajustée
     */
    @Transient
    private BigDecimal applyFactors(BigDecimal basePremium, Map<String, BigDecimal> factors) {
        BigDecimal adjustedPremium = basePremium;

        // Application de chaque facteur
        for (BigDecimal factor : factors.values()) {
            adjustedPremium = adjustedPremium.multiply(factor);
        }

        return adjustedPremium.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Applique le coefficient bonus-malus à la prime ajustée.
     *
     * @param adjustedPremium La prime ajustée
     * @return La prime finale
     */
    @Transient
    private BigDecimal applyBonusMalus(BigDecimal adjustedPremium) {
        return adjustedPremium.multiply(bonusMalusCoefficient)
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Calcule la répartition de la prime par garantie.
     *
     * @return Une carte des primes par garantie
     */
    @Transient
    public Map<String, BigDecimal> calculateCoveragePremiums() {
        BigDecimal totalPremium = calculatePremium();
        Map<String, BigDecimal> coveragePremiums = new HashMap<>();

        // Répartition selon le type de couverture
        if (coverageType == CoverageType.COMPREHENSIVE) {
            // Exemple de répartition pour une couverture tous risques
            coveragePremiums.put("responsabiliteCivile", totalPremium.multiply(new BigDecimal("0.4")));
            coveragePremiums.put("dommagesCollision", totalPremium.multiply(new BigDecimal("0.3")));
            coveragePremiums.put("volIncendie", totalPremium.multiply(new BigDecimal("0.2")));
            coveragePremiums.put("brisDeGlace", totalPremium.multiply(new BigDecimal("0.1")));
        } else {
            // Exemple de répartition pour une couverture au tiers
            coveragePremiums.put("responsabiliteCivile", totalPremium.multiply(new BigDecimal("0.8")));
            coveragePremiums.put("defenseRecours", totalPremium.multiply(new BigDecimal("0.2")));
        }

        return coveragePremiums;
    }

    /**
     * Obtient le facteur de risque pour une catégorie de véhicule.
     * Dans une implémentation réelle, ces valeurs seraient récupérées depuis une référence.
     *
     * @param categoryId L'ID de la catégorie
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal getCategoryFactor(UUID categoryId) {
        // Exemple simplifié - dans une implémentation réelle, ces valeurs seraient récupérées depuis une référence
        return new BigDecimal("1.2");
    }

    /**
     * Obtient le facteur de risque pour un usage de véhicule.
     * Dans une implémentation réelle, ces valeurs seraient récupérées depuis une référence.
     *
     * @param usageId L'ID de l'usage
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal getUsageFactor(UUID usageId) {
        // Exemple simplifié - dans une implémentation réelle, ces valeurs seraient récupérées depuis une référence
        return new BigDecimal("1.1");
    }

    /**
     * Obtient le facteur de risque pour la zone géographique.
     * Dans une implémentation réelle, cette valeur serait récupérée depuis l'entité GeographicZone.
     *
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal getGeographicZoneFactor() {
        // Exemple simplifié - dans une implémentation réelle, cette valeur serait récupérée depuis l'entité GeographicZone
        return new BigDecimal("1.1");
    }

    /**
     * Obtient le facteur de risque pour la zone de circulation.
     * Dans une implémentation réelle, cette valeur serait récupérée depuis l'entité CirculationZone.
     *
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal getCirculationZoneFactor() {
        // Exemple simplifié - dans une implémentation réelle, cette valeur serait récupérée depuis l'entité CirculationZone
        return new BigDecimal("1.2");
    }
}
