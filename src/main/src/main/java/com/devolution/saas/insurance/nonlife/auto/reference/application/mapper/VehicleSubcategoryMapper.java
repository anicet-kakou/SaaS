package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleSubcategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleSubcategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleSubcategory et VehicleSubcategoryDTO.
 */
@Component
@RequiredArgsConstructor
public class VehicleSubcategoryMapper {

    private final VehicleCategoryRepository vehicleCategoryRepository;

    /**
     * Convertit une entité VehicleSubcategory en DTO VehicleSubcategoryDTO.
     *
     * @param vehicleSubcategory L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleSubcategoryDTO toDto(VehicleSubcategory vehicleSubcategory) {
        if (vehicleSubcategory == null) {
            return null;
        }

        VehicleSubcategoryDTO dto = VehicleSubcategoryDTO.builder()
                .id(vehicleSubcategory.getId())
                .categoryId(vehicleSubcategory.getCategoryId())
                .code(vehicleSubcategory.getCode())
                .name(vehicleSubcategory.getName())
                .description(vehicleSubcategory.getDescription())
                .tariffCoefficient(vehicleSubcategory.getTariffCoefficient())
                .isActive(vehicleSubcategory.isActive())
                .organizationId(vehicleSubcategory.getOrganizationId())
                .build();

        // Ajouter le nom de la catégorie si disponible
        vehicleCategoryRepository.findById(vehicleSubcategory.getCategoryId())
                .ifPresent(category -> dto.setCategoryName(category.getName()));

        return dto;
    }

    /**
     * Convertit un DTO VehicleSubcategoryDTO en entité VehicleSubcategory.
     *
     * @param vehicleSubcategoryDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleSubcategory toEntity(VehicleSubcategoryDTO vehicleSubcategoryDTO) {
        if (vehicleSubcategoryDTO == null) {
            return null;
        }

        return VehicleSubcategory.builder()
                .id(vehicleSubcategoryDTO.getId())
                .categoryId(vehicleSubcategoryDTO.getCategoryId())
                .code(vehicleSubcategoryDTO.getCode())
                .name(vehicleSubcategoryDTO.getName())
                .description(vehicleSubcategoryDTO.getDescription())
                .tariffCoefficient(vehicleSubcategoryDTO.getTariffCoefficient())
                .isActive(vehicleSubcategoryDTO.isActive())
                .organizationId(vehicleSubcategoryDTO.getOrganizationId())
                .build();
    }
}
