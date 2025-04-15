package com.devolution.saas.insurance.nonlife.auto.api.mapper;

import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateAutoInsuranceProductRequest;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateAutoPolicyRequest;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateDriverRequest;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateVehicleRequest;
import com.devolution.saas.insurance.nonlife.auto.api.dto.response.DriverResponse;
import com.devolution.saas.insurance.nonlife.auto.api.dto.response.VehicleResponse;
import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.DriverDTO;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper pour convertir entre les DTOs d'application et les DTOs d'API.
 */
@Component
public class ApiMapper {

    /**
     * Convertit une requête de création de véhicule en commande.
     *
     * @param request        La requête à convertir
     * @param organizationId L'ID de l'organisation
     * @return La commande correspondante
     */
    public CreateVehicleCommand toCommand(CreateVehicleRequest request, UUID organizationId) {
        return CreateVehicleCommand.builder()
                .registrationNumber(request.registrationNumber())
                .makeId(request.makeId())
                .modelId(request.modelId())
                .modelVariant(request.version())
                .year(request.year())
                .enginePower(request.enginePower())
                .engineSize(request.engineSize())
                .fuelTypeId(request.fuelTypeId())
                .categoryId(request.categoryId())
                .subcategoryId(request.subcategoryId())
                .usageId(request.usageId())
                .geographicZoneId(request.geographicZoneId())
                .purchaseDate(request.purchaseDate())
                .purchaseValue(request.purchaseValue())
                .currentValue(request.currentValue())
                .mileage(request.mileage())
                .vin(request.vin())
                .colorId(request.colorId())
                .ownerId(request.ownerId())
                .organizationId(organizationId)
                .build();
    }

    /**
     * Convertit un DTO de véhicule en réponse.
     *
     * @param dto Le DTO à convertir
     * @return La réponse correspondante
     */
    public VehicleResponse toResponse(VehicleDTO dto) {
        return VehicleResponse.builder()
                .id(dto.id())
                .registrationNumber(dto.registrationNumber())
                .manufacturerId(dto.manufacturerId())
                .manufacturerName(dto.manufacturerName())
                .modelId(dto.modelId())
                .modelName(dto.modelName())
                .modelVariant(dto.modelVariant())
                .year(dto.year())
                .enginePower(dto.enginePower())
                .engineSize(dto.engineSize())
                .fuelTypeId(dto.fuelTypeId())
                .fuelTypeName(dto.fuelTypeName())
                .categoryId(dto.categoryId())
                .categoryName(dto.categoryName())
                .subcategoryId(dto.subcategoryId())
                .subcategoryName(dto.subcategoryName())
                .usageId(dto.usageId())
                .usageName(dto.usageName())
                .geographicZoneId(dto.geographicZoneId())
                .geographicZoneName(dto.geographicZoneName())
                .purchaseDate(dto.purchaseDate())
                .purchaseValue(dto.purchaseValue())
                .currentValue(dto.currentValue())
                .mileage(dto.mileage())
                .vin(dto.vin())
                .colorId(dto.colorId())
                .colorName(dto.colorName())
                .ownerId(dto.ownerId())
                .ownerName(dto.ownerName())
                .organizationId(dto.organizationId())
                .autoPolicyId(dto.autoPolicyId())
                .hasAntiTheftDevice(dto.hasAntiTheftDevice())
                .parkingType(dto.parkingType() != null ? AutoPolicy.ParkingType.valueOf(dto.parkingType()) : null)
                .build();
    }

    /**
     * Convertit une requête de création de conducteur en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    public Driver toEntity(CreateDriverRequest request) {
        Driver driver = Driver.builder()
                .customerId(request.customerId())
                .licenseNumber(request.licenseNumber())
                .licenseIssueDate(request.licenseIssueDate())
                .licenseExpiryDate(request.licenseExpiryDate())
                .isPrimaryDriver(request.isPrimaryDriver())
                .yearsOfDrivingExperience(request.yearsOfDrivingExperience())
                .build();

        // Utiliser les setters pour définir les IDs
        driver.setLicenseTypeId(request.licenseTypeId());

        return driver;
    }

    /**
     * Convertit un DTO de conducteur en réponse.
     *
     * @param dto Le DTO à convertir
     * @return La réponse correspondante
     */
    public DriverResponse toResponse(DriverDTO dto) {
        return DriverResponse.builder()
                .id(dto.id())
                .customerId(dto.customerId())
                .customerName(dto.customerName())
                .licenseNumber(dto.licenseNumber())
                .licenseTypeId(dto.licenseTypeId())
                .licenseTypeName(dto.licenseTypeName())
                .licenseIssueDate(dto.licenseIssueDate())
                .licenseExpiryDate(dto.licenseExpiryDate())
                .isPrimaryDriver(dto.isPrimaryDriver())
                .yearsOfDrivingExperience(dto.yearsOfDrivingExperience())
                .organizationId(dto.organizationId())
                .build();
    }

    /**
     * Convertit une requête de création de produit d'assurance auto en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    public AutoInsuranceProduct toEntity(CreateAutoInsuranceProductRequest request) {
        return AutoInsuranceProduct.builder()
                .code(request.code())
                .name(request.name())
                .description(request.description())
                .status(request.status())
                .effectiveDate(request.effectiveDate())
                .expiryDate(request.expiryDate())
                .build();
    }

    /**
     * Convertit une requête de création de police d'assurance auto en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    public AutoPolicy toEntity(CreateAutoPolicyRequest request) {
        AutoPolicy policy = AutoPolicy.builder()
                .policyNumber(request.policyNumber())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .premiumAmount(request.premiumAmount())
                .coverageType(request.coverageType())
                .bonusMalusCoefficient(request.bonusMalusCoefficient())
                .annualMileage(request.annualMileage())
                .parkingType(request.parkingType())
                .hasAntiTheftDevice(request.hasAntiTheftDevice())
                .build();

        // Utiliser les setters pour définir les IDs
        policy.setVehicleId(request.vehicleId());
        policy.setPrimaryDriverId(request.primaryDriverId());
        policy.setClaimHistoryCategoryId(request.claimHistoryCategoryId());

        return policy;
    }
}
