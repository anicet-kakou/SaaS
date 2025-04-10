package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleSubcategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleSubcategoryMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleSubcategoryService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleSubcategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleSubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des sous-catégories de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleSubcategoryServiceImpl implements VehicleSubcategoryService {

    private final VehicleSubcategoryRepository vehicleSubcategoryRepository;
    private final VehicleSubcategoryMapper vehicleSubcategoryMapper;

    @Override
    public VehicleSubcategoryDTO createVehicleSubcategory(VehicleSubcategory vehicleSubcategory, UUID organizationId) {
        vehicleSubcategory.setOrganizationId(organizationId);
        VehicleSubcategory savedSubcategory = vehicleSubcategoryRepository.save(vehicleSubcategory);
        return vehicleSubcategoryMapper.toDto(savedSubcategory);
    }

    @Override
    public Optional<VehicleSubcategoryDTO> updateVehicleSubcategory(UUID id, VehicleSubcategory vehicleSubcategory, UUID organizationId) {
        return vehicleSubcategoryRepository.findById(id)
                .filter(subcategory -> subcategory.getOrganizationId().equals(organizationId))
                .map(existingSubcategory -> {
                    vehicleSubcategory.setId(id);
                    vehicleSubcategory.setOrganizationId(organizationId);
                    VehicleSubcategory updatedSubcategory = vehicleSubcategoryRepository.save(vehicleSubcategory);
                    return vehicleSubcategoryMapper.toDto(updatedSubcategory);
                });
    }

    @Override
    public Optional<VehicleSubcategoryDTO> getVehicleSubcategoryById(UUID id, UUID organizationId) {
        return vehicleSubcategoryRepository.findById(id)
                .filter(subcategory -> subcategory.getOrganizationId().equals(organizationId))
                .map(vehicleSubcategoryMapper::toDto);
    }

    @Override
    public Optional<VehicleSubcategoryDTO> getVehicleSubcategoryByCode(String code, UUID organizationId) {
        return vehicleSubcategoryRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleSubcategoryMapper::toDto);
    }

    @Override
    public List<VehicleSubcategoryDTO> getAllVehicleSubcategories(UUID organizationId) {
        return vehicleSubcategoryRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleSubcategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleSubcategoryDTO> getAllActiveVehicleSubcategories(UUID organizationId) {
        return vehicleSubcategoryRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleSubcategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleSubcategoryDTO> getAllVehicleSubcategoriesByCategory(UUID categoryId, UUID organizationId) {
        return vehicleSubcategoryRepository.findAllByCategoryIdAndOrganizationId(categoryId, organizationId)
                .stream()
                .map(vehicleSubcategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleSubcategoryDTO> getAllActiveVehicleSubcategoriesByCategory(UUID categoryId, UUID organizationId) {
        return vehicleSubcategoryRepository.findAllActiveByCategoryIdAndOrganizationId(categoryId, organizationId)
                .stream()
                .map(vehicleSubcategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleSubcategory(UUID id, UUID organizationId) {
        return vehicleSubcategoryRepository.findById(id)
                .filter(subcategory -> subcategory.getOrganizationId().equals(organizationId))
                .map(subcategory -> {
                    vehicleSubcategoryRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
