package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entité représentant un véhicule assuré.
 */
@Entity
@Table(name = "vehicles")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseEntity {

    /**
     * Numéro d'immatriculation du véhicule.
     */
    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    /**
     * ID de la marque du véhicule.
     */
    @Column(name = "make_id", nullable = false)
    private UUID makeId;

    /**
     * ID du modèle du véhicule.
     */
    @Column(name = "model_id", nullable = false)
    private UUID modelId;

    /**
     * Version du véhicule.
     */
    @Column(name = "version")
    private String version;

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
     * ID du type de carburant.
     */
    @Column(name = "fuel_type_id", nullable = false)
    private UUID fuelTypeId;

    /**
     * ID de la catégorie du véhicule.
     */
    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    /**
     * ID de la sous-catégorie du véhicule.
     */
    @Column(name = "subcategory_id")
    private UUID subcategoryId;

    /**
     * ID du type d'usage du véhicule.
     */
    @Column(name = "usage_id", nullable = false)
    private UUID usageId;

    /**
     * ID de la zone géographique.
     */
    @Column(name = "geographic_zone_id", nullable = false)
    private UUID geographicZoneId;

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
     * Numéro d'identification du véhicule (VIN).
     */
    @Column(name = "vin")
    private String vin;

    /**
     * ID de la couleur du véhicule.
     */
    @Column(name = "color_id", nullable = false)
    private UUID colorId;

    /**
     * ID du propriétaire du véhicule.
     */
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    /**
     * ID de l'organisation.
     */
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    /**
     * ID de la police auto associée.
     */
    @Column(name = "auto_policy_id")
    private UUID autoPolicyId;
}
