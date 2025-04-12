package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleSubcategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
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

        // Get category name if available
        String categoryName = null;
        if (vehicleSubcategory.getCategoryId() != null) {
            categoryName = vehicleCategoryRepository.findById(vehicleSubcategory.getCategoryId())
                    .map(VehicleCategory::getName)
                    .orElse(null);
        }

        // Create the DTO with all values
        return new VehicleSubcategoryDTO(
                vehicleSubcategory.getId(),
                vehicleSubcategory.getCategoryId(),
                categoryName,
                vehicleSubcategory.getCode(),
                vehicleSubcategory.getName(),
                vehicleSubcategory.getDescription(),
                vehicleSubcategory.getTariffCoefficient(),
                vehicleSubcategory.isActive(),
                vehicleSubcategory.getOrganizationId());
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
                .id(vehicleSubcategoryDTO.id())
                .categoryId(vehicleSubcategoryDTO.categoryId())
                .code(vehicleSubcategoryDTO.code())
                .name(vehicleSubcategoryDTO.name())
                .description(vehicleSubcategoryDTO.description())
                .tariffCoefficient(vehicleSubcategoryDTO.tariffCoefficient())
                .isActive(vehicleSubcategoryDTO.isActive())
                .organizationId(vehicleSubcategoryDTO.organizationId())
                .build();
    }
}
