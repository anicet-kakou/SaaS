package com.devolution.saas.insurance.nonlife.auto.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités Vehicle et les DTOs VehicleDTO.
 */
@Component
public class VehicleMapper {

    /**
     * Convertit une entité Vehicle en DTO VehicleDTO.
     *
     * @param vehicle L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleDTO toDto(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        return VehicleDTO.builder()
                .id(vehicle.getId())
                .registrationNumber(vehicle.getRegistrationNumber())
                .manufacturerId(vehicle.getManufacturerId())
                .modelId(vehicle.getModelId())
                .modelVariant(vehicle.getModelVariant())
                .version(vehicle.getVersion())
                .year(vehicle.getYear())
                .enginePower(vehicle.getEnginePower())
                .engineSize(vehicle.getEngineSize())
                .fuelTypeId(vehicle.getFuelTypeId())
                .categoryId(vehicle.getCategoryId())
                .subcategoryId(vehicle.getSubcategoryId())
                .usageId(vehicle.getUsageId())
                .geographicZoneId(vehicle.getGeographicZoneId())
                .purchaseDate(vehicle.getPurchaseDate())
                .purchaseValue(vehicle.getPurchaseValue())
                .currentValue(vehicle.getCurrentValue())
                .mileage(vehicle.getMileage())
                .vin(vehicle.getVin())
                .colorId(vehicle.getColorId())
                .ownerId(vehicle.getOwnerId())
                .organizationId(vehicle.getOrganizationId())
                .autoPolicyId(vehicle.getAutoPolicyId())
                .hasAntiTheftDevice(vehicle.isHasAntiTheftDevice())
                .parkingType(vehicle.getParkingType() != null ? vehicle.getParkingType().name() : null)
                .build();
    }

    /**
     * Convertit un DTO VehicleDTO en entité Vehicle.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public Vehicle toEntity(VehicleDTO dto) {
        if (dto == null) {
            return null;
        }

        Vehicle vehicle = Vehicle.builder()
                .id(dto.id())
                .registrationNumber(dto.registrationNumber())
                .modelVariant(dto.modelVariant())
                .version(dto.version())
                .year(dto.year())
                .enginePower(dto.enginePower())
                .engineSize(dto.engineSize())
                .purchaseDate(dto.purchaseDate())
                .purchaseValue(dto.purchaseValue())
                .currentValue(dto.currentValue())
                .mileage(dto.mileage())
                .vin(dto.vin())
                .ownerId(dto.ownerId())
                .organizationId(dto.organizationId())
                .hasAntiTheftDevice(dto.hasAntiTheftDevice())
                .parkingType(dto.parkingType() != null ? AutoPolicy.ParkingType.valueOf(dto.parkingType()) : null)
                .build();

        // Utiliser les setters pour définir les IDs
        vehicle.setManufacturerId(dto.manufacturerId());
        vehicle.setModelId(dto.modelId());
        vehicle.setFuelTypeId(dto.fuelTypeId());
        vehicle.setCategoryId(dto.categoryId());
        vehicle.setSubcategoryId(dto.subcategoryId());
        vehicle.setUsageId(dto.usageId());
        vehicle.setGeographicZoneId(dto.geographicZoneId());
        vehicle.setColorId(dto.colorId());
        vehicle.setAutoPolicyId(dto.autoPolicyId());

        return vehicle;
    }
}
