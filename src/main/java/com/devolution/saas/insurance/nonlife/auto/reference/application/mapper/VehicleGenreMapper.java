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

        return new VehicleGenreDTO(
                vehicleGenre.getId(),
                vehicleGenre.getCode(),
                vehicleGenre.getName(),
                vehicleGenre.getDescription(),
                vehicleGenre.getRiskCoefficient(),
                vehicleGenre.isActive(),
                vehicleGenre.getOrganizationId());
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
                .id(vehicleGenreDTO.id())
                .code(vehicleGenreDTO.code())
                .name(vehicleGenreDTO.name())
                .description(vehicleGenreDTO.description())
                .riskCoefficient(vehicleGenreDTO.riskCoefficient())
                .isActive(vehicleGenreDTO.isActive())
                .organizationId(vehicleGenreDTO.organizationId())
                .build();
    }
}
