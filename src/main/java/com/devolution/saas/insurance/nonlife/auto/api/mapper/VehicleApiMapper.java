package com.devolution.saas.insurance.nonlife.auto.api.mapper;

import com.devolution.saas.common.mapper.EntityApiMapper;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateVehicleRequest;
import com.devolution.saas.insurance.nonlife.auto.api.dto.response.VehicleResponse;
import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper pour convertir entre les DTOs d'API et les entités/DTOs de véhicule.
 */
@Component
public class VehicleApiMapper implements EntityApiMapper<CreateVehicleCommand, CreateVehicleRequest, VehicleResponse> {

    /**
     * Convertit une requête de création de véhicule en commande.
     *
     * @param request La requête à convertir
     * @return La commande correspondante
     */
    @Override
    public CreateVehicleCommand toEntity(CreateVehicleRequest request) {
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
                .build();
    }

    /**
     * Convertit une requête de création de véhicule en commande avec l'ID de l'organisation.
     *
     * @param request        La requête à convertir
     * @param organizationId L'ID de l'organisation
     * @return La commande correspondante
     */
    public CreateVehicleCommand toEntity(CreateVehicleRequest request, UUID organizationId) {
        CreateVehicleCommand command = toEntity(request);
        return CreateVehicleCommand.builder()
                .registrationNumber(command.registrationNumber())
                .makeId(command.makeId())
                .modelId(command.modelId())
                .modelVariant(command.modelVariant())
                .year(command.year())
                .enginePower(command.enginePower())
                .engineSize(command.engineSize())
                .fuelTypeId(command.fuelTypeId())
                .categoryId(command.categoryId())
                .subcategoryId(command.subcategoryId())
                .usageId(command.usageId())
                .geographicZoneId(command.geographicZoneId())
                .purchaseDate(command.purchaseDate())
                .purchaseValue(command.purchaseValue())
                .currentValue(command.currentValue())
                .mileage(command.mileage())
                .vin(command.vin())
                .colorId(command.colorId())
                .ownerId(command.ownerId())
                .organizationId(organizationId)
                .build();
    }

    /**
     * Convertit un DTO de véhicule en réponse.
     *
     * @param dto Le DTO à convertir
     * @return La réponse correspondante
     */
    @Override
    public VehicleResponse toResponse(CreateVehicleCommand dto) {
        throw new UnsupportedOperationException("Cette méthode n'est pas implémentée. Utilisez toResponse(VehicleDTO) à la place.");
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
}
