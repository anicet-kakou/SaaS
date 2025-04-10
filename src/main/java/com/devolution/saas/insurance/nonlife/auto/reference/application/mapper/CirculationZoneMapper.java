package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre CirculationZone et CirculationZoneDTO.
 */
@Component
public class CirculationZoneMapper {

    /**
     * Convertit une entité CirculationZone en DTO CirculationZoneDTO.
     *
     * @param circulationZone L'entité à convertir
     * @return Le DTO correspondant
     */
    public CirculationZoneDTO toDto(CirculationZone circulationZone) {
        if (circulationZone == null) {
            return null;
        }

        return CirculationZoneDTO.builder()
                .id(circulationZone.getId())
                .code(circulationZone.getCode())
                .name(circulationZone.getName())
                .description(circulationZone.getDescription())
                .riskCoefficient(circulationZone.getRiskCoefficient())
                .isActive(circulationZone.isActive())
                .organizationId(circulationZone.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO CirculationZoneDTO en entité CirculationZone.
     *
     * @param circulationZoneDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public CirculationZone toEntity(CirculationZoneDTO circulationZoneDTO) {
        if (circulationZoneDTO == null) {
            return null;
        }

        return CirculationZone.builder()
                .id(circulationZoneDTO.getId())
                .code(circulationZoneDTO.getCode())
                .name(circulationZoneDTO.getName())
                .description(circulationZoneDTO.getDescription())
                .riskCoefficient(circulationZoneDTO.getRiskCoefficient())
                .isActive(circulationZoneDTO.isActive())
                .organizationId(circulationZoneDTO.getOrganizationId())
                .build();
    }
}
