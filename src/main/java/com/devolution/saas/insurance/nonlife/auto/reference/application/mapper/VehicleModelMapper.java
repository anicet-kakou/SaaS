package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleModelDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleCategoryRepository;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleMakeRepository;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleSubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleModel et VehicleModelDTO.
 */
@Component
@RequiredArgsConstructor
public class VehicleModelMapper {

    private final VehicleMakeRepository vehicleMakeRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final VehicleSubcategoryRepository vehicleSubcategoryRepository;

    /**
     * Convertit une entité VehicleModel en DTO VehicleModelDTO.
     *
     * @param vehicleModel L'entité à convertir
     * @return Le DTO correspondant
     */
    public VehicleModelDTO toDto(VehicleModel vehicleModel) {
        if (vehicleModel == null) {
            return null;
        }

        VehicleModelDTO dto = VehicleModelDTO.builder()
                .id(vehicleModel.getId())
                .makeId(vehicleModel.getMakeId())
                .code(vehicleModel.getCode())
                .name(vehicleModel.getName())
                .description(vehicleModel.getDescription())
                .categoryId(vehicleModel.getCategoryId())
                .subcategoryId(vehicleModel.getSubcategoryId())
                .isActive(vehicleModel.isActive())
                .organizationId(vehicleModel.getOrganizationId())
                .build();

        // Ajouter le nom de la marque si disponible
        vehicleMakeRepository.findById(vehicleModel.getMakeId())
                .ifPresent(make -> dto.setMakeName(make.getName()));

        // Ajouter le nom de la catégorie si disponible
        vehicleCategoryRepository.findById(vehicleModel.getCategoryId())
                .ifPresent(category -> dto.setCategoryName(category.getName()));

        // Ajouter le nom de la sous-catégorie si disponible
        if (vehicleModel.getSubcategoryId() != null) {
            vehicleSubcategoryRepository.findById(vehicleModel.getSubcategoryId())
                    .ifPresent(subcategory -> dto.setSubcategoryName(subcategory.getName()));
        }

        return dto;
    }

    /**
     * Convertit un DTO VehicleModelDTO en entité VehicleModel.
     *
     * @param vehicleModelDTO Le DTO à convertir
     * @return L'entité correspondante
     */
    public VehicleModel toEntity(VehicleModelDTO vehicleModelDTO) {
        if (vehicleModelDTO == null) {
            return null;
        }

        return VehicleModel.builder()
                .id(vehicleModelDTO.getId())
                .makeId(vehicleModelDTO.getMakeId())
                .code(vehicleModelDTO.getCode())
                .name(vehicleModelDTO.getName())
                .description(vehicleModelDTO.getDescription())
                .categoryId(vehicleModelDTO.getCategoryId())
                .subcategoryId(vehicleModelDTO.getSubcategoryId())
                .isActive(vehicleModelDTO.isActive())
                .organizationId(vehicleModelDTO.getOrganizationId())
                .build();
    }
}
