package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.common.util.Validation;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Entité représentant un véhicule assuré.
 */
@Entity
@Table(
        name = "vehicles",
        indexes = {
                @Index(name = "idx_vehicles_registration_number", columnList = "registration_number"),
                @Index(name = "idx_vehicles_make_id", columnList = "make_id"),
                @Index(name = "idx_vehicles_model_id", columnList = "model_id"),
                @Index(name = "idx_vehicles_owner_id", columnList = "owner_id"),
                @Index(name = "idx_vehicles_auto_policy_id", columnList = "auto_policy_id")
        }
)
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends TenantAwareEntity {

    /**
     * Numéro d'immatriculation du véhicule.
     */
    @Column(name = "registration_number", nullable = false, length = 20)
    private String registrationNumber;

    /**
     * Fabricant du véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "manufacturer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_manufacturer")
    )
    private VehicleManufacturer manufacturer;

    /**
     * Modèle du véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "model_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_model")
    )
    private VehicleModel model;

    /**
     * Nom de la variante du modèle du véhicule.
     * Note: Ce champ est différent du champ 'version' hérité de BaseEntity qui est utilisé pour le verrouillage optimiste.
     */
    @Column(name = "model_variant", length = 50)
    private String modelVariant;
    /**
     * Type de carburant du véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fuel_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_fuel_type")
    )
    private VehicleFuelType fuelType;

    /**
     * Année de fabrication du véhicule.
     */
    @Column(name = "year", nullable = false)
    private int year;

    /**
     * Puissance du moteur en chevaux.
     */
    @Column(name = "engine_power")
    private Integer enginePower;

    /**
     * Cylindrée du moteur en cm3.
     */
    @Column(name = "engine_size")
    private Integer engineSize;
    /**
     * Catégorie du véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_category")
    )
    private VehicleCategory category;
    /**
     * Sous-catégorie du véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "subcategory_id",
            foreignKey = @ForeignKey(name = "fk_vehicle_subcategory")
    )
    private VehicleSubcategory subcategory;
    /**
     * Type d'usage du véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usage_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_usage")
    )
    private com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage usage;
    /**
     * Zone géographique du véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "geographic_zone_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_geographic_zone")
    )
    private GeographicZone geographicZone;
    /**
     * Numéro d'identification du véhicule (VIN).
     */
    @Column(name = "vin", length = 17)
    private String vin;

    /**
     * Date d'achat du véhicule.
     */
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    /**
     * Valeur d'achat du véhicule.
     */
    @Column(name = "purchase_value")
    private BigDecimal purchaseValue;

    /**
     * Valeur actuelle du véhicule.
     */
    @Column(name = "current_value")
    private BigDecimal currentValue;

    /**
     * Kilométrage du véhicule.
     */
    @Column(name = "mileage")
    private Integer mileage;
    /**
     * Couleur du véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "color_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_color")
    )
    private VehicleColor color;
    /**
     * ID du propriétaire du véhicule.
     * Note: Nous gardons l'ID car le propriétaire est dans un autre module (Customer).
     */
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;
    /**
     * Police auto associée au véhicule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "auto_policy_id",
            foreignKey = @ForeignKey(name = "fk_vehicle_auto_policy")
    )
    private AutoPolicy autoPolicy;

    // Note: Cette entité hérite du champ organizationId de TenantAwareEntity pour le support multi-tenant
    /**
     * Indique si le véhicule est équipé d'un dispositif antivol.
     */
    @Column(name = "has_anti_theft_device", nullable = false)
    private boolean hasAntiTheftDevice;
    /**
     * Type de stationnement du véhicule.
     */
    @Column(name = "parking_type")
    @Enumerated(EnumType.STRING)
    private AutoPolicy.ParkingType parkingType;

    /**
     * Retourne le nom de la variante du modèle du véhicule.
     *
     * @return le nom de la variante du modèle
     */
    public String getModelVariant() {
        return modelVariant;
    }

    /**
     * Vérifie si le véhicule est équipé d'un dispositif antivol.
     *
     * @return true si le véhicule est équipé d'un dispositif antivol, false sinon
     */
    public boolean isHasAntiTheftDevice() {
        return hasAntiTheftDevice;
    }

    /**
     * Retourne le type de stationnement du véhicule.
     *
     * @return le type de stationnement
     */
    public AutoPolicy.ParkingType getParkingType() {
        return parkingType;
    }

    // Méthodes de compatibilité pour maintenir l'API existante

    /**
     * Retourne l'ID du fabricant du véhicule.
     *
     * @return l'ID du fabricant
     */
    public UUID getManufacturerId() {
        return manufacturer != null ? manufacturer.getId() : null;
    }

    /**
     * Définit le fabricant du véhicule à partir de son ID.
     *
     * @param manufacturerId ID du fabricant
     */
    public void setManufacturerId(UUID manufacturerId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car le fabricant doit être défini comme une entité
    }

    /**
     * Retourne l'ID du modèle du véhicule.
     *
     * @return l'ID du modèle
     */
    public UUID getModelId() {
        return model != null ? model.getId() : null;
    }

    /**
     * Définit le modèle du véhicule à partir de son ID.
     *
     * @param modelId ID du modèle
     */
    public void setModelId(UUID modelId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car le modèle doit être défini comme une entité
    }

    /**
     * Retourne l'ID du type de carburant du véhicule.
     *
     * @return l'ID du type de carburant
     */
    public UUID getFuelTypeId() {
        return fuelType != null ? fuelType.getId() : null;
    }

    /**
     * Définit le type de carburant du véhicule à partir de son ID.
     *
     * @param fuelTypeId ID du type de carburant
     */
    public void setFuelTypeId(UUID fuelTypeId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car le type de carburant doit être défini comme une entité
    }

    /**
     * Retourne l'ID de la catégorie du véhicule.
     *
     * @return l'ID de la catégorie
     */
    public UUID getCategoryId() {
        return category != null ? category.getId() : null;
    }

    /**
     * Définit la catégorie du véhicule à partir de son ID.
     *
     * @param categoryId ID de la catégorie
     */
    public void setCategoryId(UUID categoryId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car la catégorie doit être définie comme une entité
    }

    /**
     * Retourne l'ID de la sous-catégorie du véhicule.
     *
     * @return l'ID de la sous-catégorie
     */
    public UUID getSubcategoryId() {
        return subcategory != null ? subcategory.getId() : null;
    }

    /**
     * Définit la sous-catégorie du véhicule à partir de son ID.
     *
     * @param subcategoryId ID de la sous-catégorie
     */
    public void setSubcategoryId(UUID subcategoryId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car la sous-catégorie doit être définie comme une entité
    }

    /**
     * Retourne l'ID du type d'usage du véhicule.
     *
     * @return l'ID du type d'usage
     */
    public UUID getUsageId() {
        return usage != null ? usage.getId() : null;
    }

    /**
     * Définit le type d'usage du véhicule à partir de son ID.
     *
     * @param usageId ID du type d'usage
     */
    public void setUsageId(UUID usageId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car le type d'usage doit être défini comme une entité
    }

    /**
     * Retourne l'ID de la zone géographique du véhicule.
     *
     * @return l'ID de la zone géographique
     */
    public UUID getGeographicZoneId() {
        return geographicZone != null ? geographicZone.getId() : null;
    }

    /**
     * Définit la zone géographique du véhicule à partir de son ID.
     *
     * @param geographicZoneId ID de la zone géographique
     */
    public void setGeographicZoneId(UUID geographicZoneId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car la zone géographique doit être définie comme une entité
    }

    /**
     * Retourne l'ID de la couleur du véhicule.
     *
     * @return l'ID de la couleur
     */
    public UUID getColorId() {
        return color != null ? color.getId() : null;
    }

    /**
     * Définit la couleur du véhicule à partir de son ID.
     *
     * @param colorId ID de la couleur
     */
    public void setColorId(UUID colorId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car la couleur doit être définie comme une entité
    }

    /**
     * Retourne l'ID de la police auto associée au véhicule.
     *
     * @return l'ID de la police auto
     */
    public UUID getAutoPolicyId() {
        return autoPolicy != null ? autoPolicy.getId() : null;
    }

    /**
     * Définit la police auto associée au véhicule à partir de son ID.
     *
     * @param autoPolicyId ID de la police auto
     */
    public void setAutoPolicyId(UUID autoPolicyId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car la police auto doit être définie comme une entité
    }

    // Constante pour le multiplicateur de couverture tous risques
    private static final BigDecimal COMPREHENSIVE_MULTIPLIER = new BigDecimal("1.5");
    // Constante pour l'échelle décimale
    private static final int DECIMAL_SCALE = 2;

    /**
     * Calcule l'âge du véhicule en années.
     *
     * @return L'âge du véhicule en années
     */
    @Transient
    public int calculateAge() {
        if (this.year <= 0) {
            // Default to current year if year is not set or invalid
            return 0;
        }
        return LocalDate.now().getYear() - this.year;
    }

    /**
     * Calcule les facteurs de risque spécifiques au véhicule.
     *
     * @return Une carte des facteurs de risque
     */
    @Transient
    public Map<String, BigDecimal> calculateRiskFactors() {
        Map<String, BigDecimal> factors = new HashMap<>();

        // Facteur lié à l'âge du véhicule
        factors.put("vehicleAge", calculateAgeFactor());

        // Facteur lié à la puissance du moteur
        factors.put("enginePower", calculateEnginePowerFactor());

        // Facteur lié à la catégorie du véhicule
        factors.put("category", calculateCategoryFactor());

        // Facteur lié à l'usage du véhicule
        factors.put("usage", calculateUsageFactor());

        // Facteur lié au dispositif antivol
        // Use Boolean object's equals to avoid NPE if hasAntiTheftDevice is null
        factors.put("antiTheftDevice", Boolean.TRUE.equals(hasAntiTheftDevice) ?
                new BigDecimal("0.9") : BigDecimal.ONE);

        // Facteur lié au type de stationnement
        factors.put("parkingType", calculateParkingTypeFactor());

        return factors;
    }

    /**
     * Calcule le facteur de risque lié à l'âge du véhicule.
     *
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal calculateAgeFactor() {
        int age = calculateAge();

        if (age < 3) {
            return new BigDecimal("1.2"); // Véhicule neuf - plus cher à remplacer
        } else if (age < 5) {
            return new BigDecimal("1.1");
        } else if (age < 10) {
            return BigDecimal.ONE; // Facteur neutre
        } else if (age < 15) {
            return new BigDecimal("0.9");
        } else {
            return new BigDecimal("0.8"); // Véhicule ancien - moins cher à remplacer mais plus risqué
        }
    }

    /**
     * Calcule le facteur de risque lié à la puissance du moteur.
     *
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal calculateEnginePowerFactor() {
        if (enginePower == null) {
            return BigDecimal.ONE; // Facteur neutre si la puissance n'est pas spécifiée
        }

        // Validate enginePower to prevent negative values
        Validation.validateNotNegative(enginePower, "puissance du moteur");

        if (enginePower < 70) {
            return new BigDecimal("0.9"); // Faible puissance - moins risqué
        } else if (enginePower < 100) {
            return new BigDecimal("1.0"); // Puissance moyenne - facteur neutre
        } else if (enginePower < 150) {
            return new BigDecimal("1.2"); // Puissance élevée - plus risqué
        } else if (enginePower < 200) {
            return new BigDecimal("1.4"); // Puissance très élevée - très risqué
        } else {
            return new BigDecimal("1.6"); // Puissance extrême - extrêmement risqué
        }
    }

    /**
     * Calcule le facteur de risque lié à la catégorie du véhicule.
     * Dans une implémentation réelle, cette valeur serait récupérée depuis l'entité VehicleCategory.
     *
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal calculateCategoryFactor() {
        if (category != null && category.getTariffCoefficient() != null) {
            return category.getTariffCoefficient();
        }
        return BigDecimal.ONE; // Facteur neutre par défaut
    }

    /**
     * Calcule le facteur de risque lié à l'usage du véhicule.
     * Dans une implémentation réelle, cette valeur serait récupérée depuis l'entité VehicleUsage.
     *
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal calculateUsageFactor() {
        if (usage != null && usage.getTariffCoefficient() != null) {
            return usage.getTariffCoefficient();
        }
        return BigDecimal.ONE; // Facteur neutre par défaut
    }

    /**
     * Calcule le facteur de risque lié au type de stationnement.
     *
     * @return Le facteur de risque
     */
    @Transient
    private BigDecimal calculateParkingTypeFactor() {
        if (parkingType == null) {
            return BigDecimal.ONE; // Facteur neutre si le type de stationnement n'est pas spécifié
        }

        switch (parkingType) {
            case GARAGE:
                return new BigDecimal("0.9"); // Garage privé - moins risqué
            case PARKING_LOT:
                return new BigDecimal("1.0"); // Parking - facteur neutre
            case STREET:
                return new BigDecimal("1.1"); // Rue - plus risqué
            default:
                return BigDecimal.ONE;
        }
    }

    /**
     * Calcule la valeur de base pour l'assurance du véhicule.
     * Cette valeur est utilisée comme point de départ pour le calcul de la prime.
     *
     * @return La valeur de base
     */
    @Transient
    public BigDecimal calculateBaseValue() {
        // Utiliser la valeur actuelle si disponible, sinon la valeur d'achat
        BigDecimal value = currentValue != null ? currentValue :
                (purchaseValue != null ? purchaseValue : new BigDecimal("10000"));

        // Validate value to prevent negative values
        Validation.validateNotNegative(value, "valeur du véhicule");

        // Ajuster en fonction de l'âge et de la catégorie
        BigDecimal baseValue = value.multiply(calculateAgeFactor())
                .multiply(calculateCategoryFactor())
                .setScale(2, RoundingMode.HALF_UP);

        return baseValue;
    }

    /**
     * Calcule la prime de base pour le véhicule en fonction du type de couverture.
     * Cette méthode suppose que le type de couverture a déjà été validé par la couche service.
     *
     * @param coverageType Le type de couverture (au tiers ou tous risques)
     * @return La prime de base
     */
    @Transient
    public BigDecimal calculateBasePremium(CoverageType coverageType) {
        // Calcul de la valeur de base
        BigDecimal basePremium = calculateBaseValue();

        // Ajustement selon le type de couverture
        if (coverageType == CoverageType.COMPREHENSIVE) {
            basePremium = basePremium.multiply(COMPREHENSIVE_MULTIPLIER);
        }

        return basePremium.setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calcule la prime ajustée en appliquant les facteurs de risque.
     * Cette méthode suppose que la prime de base a déjà été validée par la couche service.
     *
     * @param basePremium La prime de base
     * @return La prime ajustée
     */
    @Transient
    public BigDecimal calculateAdjustedPremium(BigDecimal basePremium) {
        // Application des facteurs de risque
        Map<String, BigDecimal> factors = calculateRiskFactors();
        BigDecimal adjustedPremium = basePremium;

        for (BigDecimal factor : factors.values()) {
            adjustedPremium = adjustedPremium.multiply(factor);
        }

        return adjustedPremium.setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calcule la prime finale en appliquant le coefficient bonus-malus et les options additionnelles.
     * Cette méthode suppose que les paramètres ont déjà été validés par la couche service.
     *
     * @param coverageType          Le type de couverture (au tiers ou tous risques)
     * @param bonusMalusCoefficient Le coefficient de bonus-malus
     * @param additionalOptions     Les options additionnelles
     * @return La prime finale
     */
    @Transient
    public BigDecimal calculateFinalPremium(CoverageType coverageType, BigDecimal bonusMalusCoefficient,
                                            Map<String, Boolean> additionalOptions) {
        // Calcul de la prime de base
        BigDecimal basePremium = calculateBasePremium(coverageType);

        // Calcul de la prime ajustée
        BigDecimal adjustedPremium = calculateAdjustedPremium(basePremium);

        // Application du coefficient bonus-malus
        BigDecimal finalPremium = adjustedPremium.multiply(bonusMalusCoefficient);

        // Application des options additionnelles
        Map<String, Boolean> options = additionalOptions != null ? additionalOptions : new HashMap<>();

        for (CoverageOption option : CoverageOption.values()) {
            if (Boolean.TRUE.equals(options.get(option.getKey()))) {
                finalPremium = finalPremium.add(option.getPremium());
            }
        }

        return finalPremium.setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Simule les différentes options de tarification pour le véhicule.
     *
     * @return Une carte des options de tarification
     */
    @Transient
    public Map<String, BigDecimal> simulatePricingOptions() {
        Map<String, BigDecimal> options = new HashMap<>();

        // Calcul de la prime au tiers
        BigDecimal thirdPartyPremium = calculateBasePremium(CoverageType.THIRD_PARTY);
        options.put("thirdPartyPremium", thirdPartyPremium);

        // Calcul de la prime tous risques
        BigDecimal comprehensivePremium = calculateBasePremium(CoverageType.COMPREHENSIVE);
        options.put("comprehensivePremium", comprehensivePremium);

        // Ajout des options additionnelles
        for (CoverageOption option : CoverageOption.values()) {
            options.put(option.getKey(), option.getPremium());
        }

        return options;
    }
}
