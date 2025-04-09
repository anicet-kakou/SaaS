package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.FuelTypeDTO;
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

    @Override
    public FuelTypeDTO createFuelType(FuelType fuelType, UUID organizationId) {
        fuelType.setOrganizationId(organizationId);
        FuelType savedFuelType = fuelTypeRepository.save(fuelType);
        return mapToDTO(savedFuelType);
    }

    @Override
    public Optional<FuelTypeDTO> getFuelTypeById(UUID id, UUID organizationId) {
        return fuelTypeRepository.findById(id)
                .filter(fuelType -> fuelType.getOrganizationId().equals(organizationId))
                .map(this::mapToDTO);
    }

    @Override
    public Optional<FuelTypeDTO> getFuelTypeByCode(String code, UUID organizationId) {
        return fuelTypeRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(this::mapToDTO);
    }

    @Override
    public List<FuelTypeDTO> getAllFuelTypes(UUID organizationId) {
        return fuelTypeRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuelTypeDTO> getAllActiveFuelTypes(UUID organizationId) {
        return fuelTypeRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FuelTypeDTO> updateFuelType(UUID id, FuelType fuelType, UUID organizationId) {
        return fuelTypeRepository.findById(id)
                .filter(existingFuelType -> existingFuelType.getOrganizationId().equals(organizationId))
                .map(existingFuelType -> {
                    fuelType.setId(id);
                    fuelType.setOrganizationId(organizationId);
                    return fuelTypeRepository.save(fuelType);
                })
                .map(this::mapToDTO);
    }

    @Override
    public boolean deleteFuelType(UUID id, UUID organizationId) {
        Optional<FuelType> fuelType = fuelTypeRepository.findById(id);
        if (fuelType.isPresent() && fuelType.get().getOrganizationId().equals(organizationId)) {
            fuelTypeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Convertit une entité FuelType en DTO.
     *
     * @param fuelType L'entité à convertir
     * @return Le DTO correspondant
     */
    private FuelTypeDTO mapToDTO(FuelType fuelType) {
        return FuelTypeDTO.builder()
                .id(fuelType.getId())
                .code(fuelType.getCode())
                .name(fuelType.getName())
                .description(fuelType.getDescription())
                .isActive(fuelType.isActive())
                .build();
    }
}
