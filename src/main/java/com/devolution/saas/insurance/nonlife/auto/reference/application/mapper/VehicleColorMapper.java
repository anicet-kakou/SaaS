package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleColorDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleColor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleColor et VehicleColorDTO.
 */
@Component
public class VehicleColorMapper {

    /**
     * Convertit une entité VehicleColor en DTO VehicleColorDTO.
     *
     * @param vehicleColor L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleColorDTO toDto(VehicleColor vehicleColor) {
        if (vehicleColor == null) {
            return null;
        }

        return VehicleColorDTO.builder()
                .id(vehicleColor.getId())
                .code(vehicleColor.getCode())
                .name(vehicleColor.getName())
                .description(vehicleColor.getDescription())
                .isActive(vehicleColor.isActive())
                .organizationId(vehicleColor.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO VehicleColorDTO en entité VehicleColor.
     *
     * @param vehicleColorDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleColor toEntity(VehicleColorDTO vehicleColorDTO) {
        if (vehicleColorDTO == null) {
            return null;
        }

        return VehicleColor.builder()
                .id(vehicleColorDTO.getId())
                .code(vehicleColorDTO.getCode())
                .name(vehicleColorDTO.getName())
                .description(vehicleColorDTO.getDescription())
                .isActive(vehicleColorDTO.isActive())
                .organizationId(vehicleColorDTO.getOrganizationId())
                .build();
    }
}
