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

        return new VehicleUsageDTO(
                vehicleUsage.getId(),
                vehicleUsage.getCode(),
                vehicleUsage.getName(),
                vehicleUsage.getDescription(),
                vehicleUsage.getTariffCoefficient(),
                vehicleUsage.isActive(),
                vehicleUsage.getOrganizationId());
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
                .id(vehicleUsageDTO.id())
                .code(vehicleUsageDTO.code())
                .name(vehicleUsageDTO.name())
                .description(vehicleUsageDTO.description())
                .tariffCoefficient(vehicleUsageDTO.tariffCoefficient())
                .isActive(vehicleUsageDTO.isActive())
                .organizationId(vehicleUsageDTO.organizationId())
                .build();
    }
}
