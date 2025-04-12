package com.devolution.saas.insurance.nonlife.auto.reference.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleModelDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleSubcategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleCategoryRepository;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleManufacturerRepository;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleSubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre VehicleModel et VehicleModelDTO.
 */
@Component
@RequiredArgsConstructor
public class VehicleModelMapper {

    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final VehicleSubcategoryRepository vehicleSubcategoryRepository;
    private final VehicleManufacturerRepository vehicleManufacturerRepository;

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

        // Get related entity names
        String makeName = null;
        if (vehicleModel.getManufacturerId() != null) {
            makeName = vehicleManufacturerRepository.findById(vehicleModel.getManufacturerId())
                    .map(VehicleManufacturer::getName)
                    .orElse(null);
        }

        String categoryName = null;
        if (vehicleModel.getCategoryId() != null) {
            categoryName = vehicleCategoryRepository.findById(vehicleModel.getCategoryId())
                    .map(VehicleCategory::getName)
                    .orElse(null);
        }

        String subcategoryName = null;
        if (vehicleModel.getSubcategoryId() != null) {
            subcategoryName = vehicleSubcategoryRepository.findById(vehicleModel.getSubcategoryId())
                    .map(VehicleSubcategory::getName)
                    .orElse(null);
        }

        // Create the DTO with all values
        return new VehicleModelDTO(
                vehicleModel.getId(),
                vehicleModel.getManufacturerId(),
                makeName,
                vehicleModel.getCode(),
                vehicleModel.getName(),
                vehicleModel.getDescription(),
                vehicleModel.getCategoryId(),
                categoryName,
                vehicleModel.getSubcategoryId(),
                subcategoryName,
                vehicleModel.isActive(),
                vehicleModel.getOrganizationId());
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
                .id(vehicleModelDTO.id())
                .manufacturerId(vehicleModelDTO.makeId())
                .code(vehicleModelDTO.code())
                .name(vehicleModelDTO.name())
                .description(vehicleModelDTO.description())
                .categoryId(vehicleModelDTO.categoryId())
                .subcategoryId(vehicleModelDTO.subcategoryId())
                .isActive(vehicleModelDTO.isActive())
                .organizationId(vehicleModelDTO.organizationId())
                .build();
    }
}
