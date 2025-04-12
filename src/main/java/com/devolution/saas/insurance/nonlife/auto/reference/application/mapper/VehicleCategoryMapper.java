package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleCategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleCategory et VehicleCategoryDTO.
 */
@Component
public class VehicleCategoryMapper {

    /**
     * Convertit une entité VehicleCategory en DTO VehicleCategoryDTO.
     *
     * @param vehicleCategory L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleCategoryDTO toDto(VehicleCategory vehicleCategory) {
        if (vehicleCategory == null) {
            return null;
        }

        return VehicleCategoryDTO.builder()
                .id(vehicleCategory.getId())
                .code(vehicleCategory.getCode())
                .name(vehicleCategory.getName())
                .description(vehicleCategory.getDescription())
                .tariffCoefficient(vehicleCategory.getTariffCoefficient())
                .isActive(vehicleCategory.isActive())
                .organizationId(vehicleCategory.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO VehicleCategoryDTO en entité VehicleCategory.
     *
     * @param vehicleCategoryDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleCategory toEntity(VehicleCategoryDTO vehicleCategoryDTO) {
        if (vehicleCategoryDTO == null) {
            return null;
        }

        return VehicleCategory.builder()
                .id(vehicleCategoryDTO.id())
                .code(vehicleCategoryDTO.code())
                .name(vehicleCategoryDTO.name())
                .description(vehicleCategoryDTO.description())
                .tariffCoefficient(vehicleCategoryDTO.tariffCoefficient())
                .isActive(vehicleCategoryDTO.isActive())
                .organizationId(vehicleCategoryDTO.organizationId())
                .build();
    }
}
