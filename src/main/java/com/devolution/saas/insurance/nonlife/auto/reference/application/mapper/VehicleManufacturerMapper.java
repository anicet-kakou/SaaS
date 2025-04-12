package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleManufacturerDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleManufacturer et VehicleManufacturerDTO.
 */
@Component
public class VehicleManufacturerMapper {

    /**
     * Convertit une entité VehicleManufacturer en DTO VehicleManufacturerDTO.
     *
     * @param vehicleManufacturer L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleManufacturerDTO toDto(VehicleManufacturer vehicleManufacturer) {
        if (vehicleManufacturer == null) {
            return null;
        }

        return new VehicleManufacturerDTO(
                vehicleManufacturer.getId(),
                vehicleManufacturer.getCode(),
                vehicleManufacturer.getName(),
                vehicleManufacturer.getDescription(),
                vehicleManufacturer.getCountryOfOrigin(),
                vehicleManufacturer.isActive(),
                vehicleManufacturer.getOrganizationId());
    }

    /**
     * Convertit un DTO VehicleManufacturerDTO en entité VehicleManufacturer.
     *
     * @param vehicleManufacturerDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleManufacturer toEntity(VehicleManufacturerDTO vehicleManufacturerDTO) {
        if (vehicleManufacturerDTO == null) {
            return null;
        }

        return VehicleManufacturer.builder()
                .id(vehicleManufacturerDTO.id())
                .code(vehicleManufacturerDTO.code())
                .name(vehicleManufacturerDTO.name())
                .description(vehicleManufacturerDTO.description())
                .countryOfOrigin(vehicleManufacturerDTO.countryOfOrigin())
                .isActive(vehicleManufacturerDTO.isActive())
                .organizationId(vehicleManufacturerDTO.organizationId())
                .build();
    }
}
