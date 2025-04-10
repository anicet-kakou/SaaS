package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.FuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.FuelType;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre FuelType et FuelTypeDTO.
 */
@Component
public class FuelTypeMapper {

    /**
     * Convertit une entité FuelType en DTO FuelTypeDTO.
     *
     * @param fuelType L'entité à convertir
     * @return Le DTO correspondant
     */
    public FuelTypeDTO toDto(FuelType fuelType) {
        if (fuelType == null) {
            return null;
        }

        return FuelTypeDTO.builder()
                .id(fuelType.getId())
                .code(fuelType.getCode())
                .name(fuelType.getName())
                .description(fuelType.getDescription())
                .isActive(fuelType.isActive())
                .organizationId(fuelType.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO FuelTypeDTO en entité FuelType.
     *
     * @param fuelTypeDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public FuelType toEntity(FuelTypeDTO fuelTypeDTO) {
        if (fuelTypeDTO == null) {
            return null;
        }

        return FuelType.builder()
                .id(fuelTypeDTO.getId())
                .code(fuelTypeDTO.getCode())
                .name(fuelTypeDTO.getName())
                .description(fuelTypeDTO.getDescription())
                .isActive(fuelTypeDTO.isActive())
                .organizationId(fuelTypeDTO.getOrganizationId())
                .build();
    }
}
