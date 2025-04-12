package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleBodyTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleBodyType et VehicleBodyTypeDTO.
 */
@Component
public class VehicleBodyTypeMapper {

    /**
     * Convertit une entité VehicleBodyType en DTO VehicleBodyTypeDTO.
     *
     * @param vehicleBodyType L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleBodyTypeDTO toDto(VehicleBodyType vehicleBodyType) {
        if (vehicleBodyType == null) {
            return null;
        }

        return new VehicleBodyTypeDTO(
                vehicleBodyType.getId(),
                vehicleBodyType.getCode(),
                vehicleBodyType.getName(),
                vehicleBodyType.getDescription(),
                vehicleBodyType.getRiskCoefficient(),
                vehicleBodyType.isActive(),
                vehicleBodyType.getOrganizationId());
    }

    /**
     * Convertit un DTO VehicleBodyTypeDTO en entité VehicleBodyType.
     *
     * @param vehicleBodyTypeDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleBodyType toEntity(VehicleBodyTypeDTO vehicleBodyTypeDTO) {
        if (vehicleBodyTypeDTO == null) {
            return null;
        }

        return VehicleBodyType.builder()
                .id(vehicleBodyTypeDTO.id())
                .code(vehicleBodyTypeDTO.code())
                .name(vehicleBodyTypeDTO.name())
                .description(vehicleBodyTypeDTO.description())
                .riskCoefficient(vehicleBodyTypeDTO.riskCoefficient())
                .isActive(vehicleBodyTypeDTO.isActive())
                .organizationId(vehicleBodyTypeDTO.organizationId())
                .build();
    }
}
