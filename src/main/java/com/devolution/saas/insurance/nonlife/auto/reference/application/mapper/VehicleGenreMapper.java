package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleGenreDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleGenre;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleGenre et VehicleGenreDTO.
 */
@Component
public class VehicleGenreMapper {

    /**
     * Convertit une entité VehicleGenre en DTO VehicleGenreDTO.
     *
     * @param vehicleGenre L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleGenreDTO toDto(VehicleGenre vehicleGenre) {
        if (vehicleGenre == null) {
            return null;
        }

        return VehicleGenreDTO.builder()
                .id(vehicleGenre.getId())
                .code(vehicleGenre.getCode())
                .name(vehicleGenre.getName())
                .description(vehicleGenre.getDescription())
                .riskCoefficient(vehicleGenre.getRiskCoefficient())
                .isActive(vehicleGenre.isActive())
                .organizationId(vehicleGenre.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO VehicleGenreDTO en entité VehicleGenre.
     *
     * @param vehicleGenreDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleGenre toEntity(VehicleGenreDTO vehicleGenreDTO) {
        if (vehicleGenreDTO == null) {
            return null;
        }

        return VehicleGenre.builder()
                .id(vehicleGenreDTO.getId())
                .code(vehicleGenreDTO.getCode())
                .name(vehicleGenreDTO.getName())
                .description(vehicleGenreDTO.getDescription())
                .riskCoefficient(vehicleGenreDTO.getRiskCoefficient())
                .isActive(vehicleGenreDTO.isActive())
                .organizationId(vehicleGenreDTO.getOrganizationId())
                .build();
    }
}
