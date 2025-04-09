package com.devolution.saas.insurance.nonlife.auto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO pour les véhicules.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private UUID id;
    private String registrationNumber;
    private UUID makeId;
    private String makeName;
    private UUID modelId;
    private String modelName;
    private String version;
    private int year;
    private Integer enginePower;
    private Integer engineSize;
    private UUID fuelTypeId;
    private String fuelTypeName;
    private UUID categoryId;
    private String categoryName;
    private UUID subcategoryId;
    private String subcategoryName;
    private UUID usageId;
    private String usageName;
    private UUID geographicZoneId;
    private String geographicZoneName;
    private LocalDate purchaseDate;
    private BigDecimal purchaseValue;
    private BigDecimal currentValue;
    private Integer mileage;
    private String vin;
    private UUID colorId;
    private String colorName;
    private UUID ownerId;
    private String ownerName;
}
