package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleFuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleFuelType;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleFuelType et VehicleFuelTypeDTO.
 */
@Component
public class VehicleFuelTypeMapper {

    /**
     * Convertit une entité VehicleFuelType en DTO VehicleFuelTypeDTO.
     *
     * @param vehicleFuelType L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleFuelTypeDTO toDto(VehicleFuelType vehicleFuelType) {
        if (vehicleFuelType == null) {
            return null;
        }

        return new VehicleFuelTypeDTO(
                vehicleFuelType.getId(),
                vehicleFuelType.getCode(),
                vehicleFuelType.getName(),
                vehicleFuelType.getDescription(),
                vehicleFuelType.isActive(),
                vehicleFuelType.getOrganizationId());
    }

    /**
     * Convertit un DTO VehicleFuelTypeDTO en entité VehicleFuelType.
     *
     * @param vehicleFuelTypeDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleFuelType toEntity(VehicleFuelTypeDTO vehicleFuelTypeDTO) {
        if (vehicleFuelTypeDTO == null) {
            return null;
        }

        return VehicleFuelType.builder()
                .id(vehicleFuelTypeDTO.id())
                .code(vehicleFuelTypeDTO.code())
                .name(vehicleFuelTypeDTO.name())
                .description(vehicleFuelTypeDTO.description())
                .isActive(vehicleFuelTypeDTO.isActive())
                .organizationId(vehicleFuelTypeDTO.organizationId())
                .build();
    }
}
