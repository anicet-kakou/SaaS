package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleCategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleCategoryMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleCategoryService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des catégories de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final VehicleCategoryMapper vehicleCategoryMapper;

    @Override
    public VehicleCategoryDTO createVehicleCategory(VehicleCategory vehicleCategory, UUID organizationId) {
        vehicleCategory.setOrganizationId(organizationId);
        VehicleCategory savedCategory = vehicleCategoryRepository.save(vehicleCategory);
        return vehicleCategoryMapper.toDto(savedCategory);
    }

    @Override
    public Optional<VehicleCategoryDTO> updateVehicleCategory(UUID id, VehicleCategory vehicleCategory, UUID organizationId) {
        return vehicleCategoryRepository.findById(id)
                .filter(category -> category.getOrganizationId().equals(organizationId))
                .map(existingCategory -> {
                    vehicleCategory.setId(id);
                    vehicleCategory.setOrganizationId(organizationId);
                    VehicleCategory updatedCategory = vehicleCategoryRepository.save(vehicleCategory);
                    return vehicleCategoryMapper.toDto(updatedCategory);
                });
    }

    @Override
    public Optional<VehicleCategoryDTO> getVehicleCategoryById(UUID id, UUID organizationId) {
        return vehicleCategoryRepository.findById(id)
                .filter(category -> category.getOrganizationId().equals(organizationId))
                .map(vehicleCategoryMapper::toDto);
    }

    @Override
    public Optional<VehicleCategoryDTO> getVehicleCategoryByCode(String code, UUID organizationId) {
        return vehicleCategoryRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleCategoryMapper::toDto);
    }

    @Override
    public List<VehicleCategoryDTO> getAllVehicleCategories(UUID organizationId) {
        return vehicleCategoryRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleCategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleCategoryDTO> getAllActiveVehicleCategories(UUID organizationId) {
        return vehicleCategoryRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleCategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleCategory(UUID id, UUID organizationId) {
        return vehicleCategoryRepository.findById(id)
                .filter(category -> category.getOrganizationId().equals(organizationId))
                .map(category -> {
                    vehicleCategoryRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
