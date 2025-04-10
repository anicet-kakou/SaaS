package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.GeographicZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre GeographicZone et GeographicZoneDTO.
 */
@Component
public class GeographicZoneMapper {

    /**
     * Convertit une entité GeographicZone en DTO GeographicZoneDTO.
     *
     * @param geographicZone L'entité à convertir
     * @return Le DTO correspondant
     */
    public GeographicZoneDTO toDto(GeographicZone geographicZone) {
        if (geographicZone == null) {
            return null;
        }

        return GeographicZoneDTO.builder()
                .id(geographicZone.getId())
                .code(geographicZone.getCode())
                .name(geographicZone.getName())
                .description(geographicZone.getDescription())
                .tariffCoefficient(geographicZone.getTariffCoefficient())
                .isActive(geographicZone.isActive())
                .organizationId(geographicZone.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO GeographicZoneDTO en entité GeographicZone.
     *
     * @param geographicZoneDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public GeographicZone toEntity(GeographicZoneDTO geographicZoneDTO) {
        if (geographicZoneDTO == null) {
            return null;
        }

        return GeographicZone.builder()
                .id(geographicZoneDTO.getId())
                .code(geographicZoneDTO.getCode())
                .name(geographicZoneDTO.getName())
                .description(geographicZoneDTO.getDescription())
                .tariffCoefficient(geographicZoneDTO.getTariffCoefficient())
                .isActive(geographicZoneDTO.isActive())
                .organizationId(geographicZoneDTO.getOrganizationId())
                .build();
    }
}
