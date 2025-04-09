package com.devolution.saas.insurance.nonlife.auto.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Commande pour la création d'un véhicule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleCommand {
    private String registrationNumber;
    private UUID makeId;
    private UUID modelId;
    private String version;
    private int year;
    private Integer enginePower;
    private Integer engineSize;
    private UUID fuelTypeId;
    private UUID categoryId;
    private UUID subcategoryId;
    private UUID usageId;
    private UUID geographicZoneId;
    private LocalDate purchaseDate;
    private BigDecimal purchaseValue;
    private BigDecimal currentValue;
    private Integer mileage;
    private String vin;
    private UUID colorId;
    private UUID ownerId;
    private UUID organizationId;
}
