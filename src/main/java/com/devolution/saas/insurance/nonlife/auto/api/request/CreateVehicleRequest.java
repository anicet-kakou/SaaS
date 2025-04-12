package com.devolution.saas.insurance.nonlife.auto.api.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Requête pour la création d'un véhicule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {

    /**
     * Numéro d'immatriculation du véhicule.
     */
    @NotBlank(message = "Le numéro d'immatriculation est obligatoire")
    @Size(min = 2, max = 20, message = "Le numéro d'immatriculation doit contenir entre 2 et 20 caractères")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Le numéro d'immatriculation ne doit contenir que des lettres majuscules, des chiffres et des tirets")
    private String registrationNumber;

    /**
     * ID de la marque du véhicule.
     */
    @NotNull(message = "L'ID de la marque est obligatoire")
    private UUID makeId;

    /**
     * ID du modèle du véhicule.
     */
    @NotNull(message = "L'ID du modèle est obligatoire")
    private UUID modelId;

    /**
     * Version du véhicule.
     */
    @Size(max = 50, message = "La version ne doit pas dépasser 50 caractères")
    private String version;

    /**
     * Année de fabrication du véhicule.
     */
    @NotNull(message = "L'année est obligatoire")
    @Min(value = 1900, message = "L'année doit être supérieure à 1900")
    private Integer year;

    /**
     * Puissance du moteur en chevaux.
     */
    @Min(value = 1, message = "La puissance du moteur doit être supérieure à 0")
    private Integer enginePower;

    /**
     * Cylindrée du moteur en cm3.
     */
    @Min(value = 1, message = "La cylindrée du moteur doit être supérieure à 0")
    private Integer engineSize;

    /**
     * ID du type de carburant.
     */
    @NotNull(message = "L'ID du type de carburant est obligatoire")
    private UUID fuelTypeId;

    /**
     * ID de la catégorie du véhicule.
     */
    @NotNull(message = "L'ID de la catégorie est obligatoire")
    private UUID categoryId;

    /**
     * ID de la sous-catégorie du véhicule.
     */
    private UUID subcategoryId;

    /**
     * ID du type d'usage du véhicule.
     */
    @NotNull(message = "L'ID du type d'usage est obligatoire")
    private UUID usageId;

    /**
     * ID de la zone géographique.
     */
    @NotNull(message = "L'ID de la zone géographique est obligatoire")
    private UUID geographicZoneId;

    /**
     * Date d'achat du véhicule.
     */
    private LocalDate purchaseDate;

    /**
     * Valeur d'achat du véhicule.
     */
    @Min(value = 0, message = "La valeur d'achat doit être supérieure ou égale à 0")
    private BigDecimal purchaseValue;

    /**
     * Valeur actuelle du véhicule.
     */
    @Min(value = 0, message = "La valeur actuelle doit être supérieure ou égale à 0")
    private BigDecimal currentValue;

    /**
     * Kilométrage du véhicule.
     */
    @Min(value = 0, message = "Le kilométrage doit être supérieur ou égal à 0")
    private Integer mileage;

    /**
     * Numéro d'identification du véhicule (VIN).
     */
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Le VIN doit contenir 17 caractères alphanumériques (sans I, O, Q)")
    private String vin;

    /**
     * ID de la couleur du véhicule.
     */
    @NotNull(message = "L'ID de la couleur est obligatoire")
    private UUID colorId;

    /**
     * ID du propriétaire du véhicule.
     */
    @NotNull(message = "L'ID du propriétaire est obligatoire")
    private UUID ownerId;
}
