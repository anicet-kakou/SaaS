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

        return VehicleBodyTypeDTO.builder()
                .id(vehicleBodyType.getId())
                .code(vehicleBodyType.getCode())
                .name(vehicleBodyType.getName())
                .description(vehicleBodyType.getDescription())
                .riskCoefficient(vehicleBodyType.getRiskCoefficient())
                .isActive(vehicleBodyType.isActive())
                .organizationId(vehicleBodyType.getOrganizationId())
                .build();
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
                .id(vehicleBodyTypeDTO.getId())
                .code(vehicleBodyTypeDTO.getCode())
                .name(vehicleBodyTypeDTO.getName())
                .description(vehicleBodyTypeDTO.getDescription())
                .riskCoefficient(vehicleBodyTypeDTO.getRiskCoefficient())
                .isActive(vehicleBodyTypeDTO.isActive())
                .organizationId(vehicleBodyTypeDTO.getOrganizationId())
                .build();
    }
}
