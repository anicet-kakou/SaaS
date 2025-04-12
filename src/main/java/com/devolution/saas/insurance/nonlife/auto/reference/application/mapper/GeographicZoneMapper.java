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

        return new GeographicZoneDTO(
                geographicZone.getId(),
                geographicZone.getCode(),
                geographicZone.getName(),
                geographicZone.getDescription(),
                geographicZone.getTariffCoefficient(),
                geographicZone.isActive(),
                geographicZone.getOrganizationId());
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
                .id(geographicZoneDTO.id())
                .code(geographicZoneDTO.code())
                .name(geographicZoneDTO.name())
                .description(geographicZoneDTO.description())
                .tariffCoefficient(geographicZoneDTO.tariffCoefficient())
                .isActive(geographicZoneDTO.isActive())
                .organizationId(geographicZoneDTO.organizationId())
                .build();
    }
}
