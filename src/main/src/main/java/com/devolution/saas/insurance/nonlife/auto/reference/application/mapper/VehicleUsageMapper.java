package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleUsageDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleUsage et VehicleUsageDTO.
 */
@Component
public class VehicleUsageMapper {

    /**
     * Convertit une entité VehicleUsage en DTO VehicleUsageDTO.
     *
     * @param vehicleUsage L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleUsageDTO toDto(VehicleUsage vehicleUsage) {
        if (vehicleUsage == null) {
            return null;
        }

        return VehicleUsageDTO.builder()
                .id(vehicleUsage.getId())
                .code(vehicleUsage.getCode())
                .name(vehicleUsage.getName())
                .description(vehicleUsage.getDescription())
                .tariffCoefficient(vehicleUsage.getTariffCoefficient())
                .isActive(vehicleUsage.isActive())
                .organizationId(vehicleUsage.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO VehicleUsageDTO en entité VehicleUsage.
     *
     * @param vehicleUsageDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleUsage toEntity(VehicleUsageDTO vehicleUsageDTO) {
        if (vehicleUsageDTO == null) {
            return null;
        }

        return VehicleUsage.builder()
                .id(vehicleUsageDTO.getId())
                .code(vehicleUsageDTO.getCode())
                .name(vehicleUsageDTO.getName())
                .description(vehicleUsageDTO.getDescription())
                .tariffCoefficient(vehicleUsageDTO.getTariffCoefficient())
                .isActive(vehicleUsageDTO.isActive())
                .organizationId(vehicleUsageDTO.getOrganizationId())
                .build();
    }
}
