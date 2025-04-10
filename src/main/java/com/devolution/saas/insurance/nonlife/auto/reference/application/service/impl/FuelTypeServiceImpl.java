package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.FuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.FuelTypeMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.FuelTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.FuelType;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.FuelTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des types de carburant.
 */
@Service
@RequiredArgsConstructor
public class FuelTypeServiceImpl implements FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;
    private final FuelTypeMapper fuelTypeMapper;

    @Override
    public FuelTypeDTO createFuelType(FuelType fuelType, UUID organizationId) {
        fuelType.setOrganizationId(organizationId);
        FuelType savedFuelType = fuelTypeRepository.save(fuelType);
        return fuelTypeMapper.toDto(savedFuelType);
    }

    @Override
    public Optional<FuelTypeDTO> updateFuelType(UUID id, FuelType fuelType, UUID organizationId) {
        return fuelTypeRepository.findById(id)
                .filter(ft -> ft.getOrganizationId().equals(organizationId))
                .map(existingFuelType -> {
                    fuelType.setId(id);
                    fuelType.setOrganizationId(organizationId);
                    FuelType updatedFuelType = fuelTypeRepository.save(fuelType);
                    return fuelTypeMapper.toDto(updatedFuelType);
                });
    }

    @Override
    public Optional<FuelTypeDTO> getFuelTypeById(UUID id, UUID organizationId) {
        return fuelTypeRepository.findById(id)
                .filter(fuelType -> fuelType.getOrganizationId().equals(organizationId))
                .map(fuelTypeMapper::toDto);
    }

    @Override
    public Optional<FuelTypeDTO> getFuelTypeByCode(String code, UUID organizationId) {
        return fuelTypeRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(fuelTypeMapper::toDto);
    }

    @Override
    public List<FuelTypeDTO> getAllFuelTypes(UUID organizationId) {
        return fuelTypeRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(fuelTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuelTypeDTO> getAllActiveFuelTypes(UUID organizationId) {
        return fuelTypeRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(fuelTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteFuelType(UUID id, UUID organizationId) {
        return fuelTypeRepository.findById(id)
                .filter(fuelType -> fuelType.getOrganizationId().equals(organizationId))
                .map(fuelType -> {
                    fuelTypeRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
