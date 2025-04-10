package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleMakeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleMake;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleMake et VehicleMakeDTO.
 */
@Component
public class VehicleMakeMapper {

    /**
     * Convertit une entité VehicleMake en DTO VehicleMakeDTO.
     *
     * @param vehicleMake L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleMakeDTO toDto(VehicleMake vehicleMake) {
        if (vehicleMake == null) {
            return null;
        }

        return new VehicleMakeDTO(
                vehicleMake.getId(),
                vehicleMake.getCode(),
                vehicleMake.getName(),
                vehicleMake.getDescription(),
                vehicleMake.getCountryOfOrigin(),
                vehicleMake.isActive(),
                vehicleMake.getOrganizationId()
        );
    }

    /**
     * Convertit un DTO VehicleMakeDTO en entité VehicleMake.
     *
     * @param vehicleMakeDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleMake toEntity(VehicleMakeDTO vehicleMakeDTO) {
        if (vehicleMakeDTO == null) {
            return null;
        }

        return VehicleMake.builder()
                .id(vehicleMakeDTO.id())
                .code(vehicleMakeDTO.code())
                .name(vehicleMakeDTO.name())
                .description(vehicleMakeDTO.description())
                .countryOfOrigin(vehicleMakeDTO.countryOfOrigin())
                .isActive(vehicleMakeDTO.isActive())
                .organizationId(vehicleMakeDTO.organizationId())
                .build();
    }
}
