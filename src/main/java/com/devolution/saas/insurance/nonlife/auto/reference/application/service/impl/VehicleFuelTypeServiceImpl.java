package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleFuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleFuelTypeMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleFuelTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleFuelType;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleFuelTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des types de carburant de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleFuelTypeServiceImpl implements VehicleFuelTypeService {

    private final VehicleFuelTypeRepository vehicleFuelTypeRepository;
    private final VehicleFuelTypeMapper vehicleFuelTypeMapper;

    @Override
    public VehicleFuelTypeDTO createVehicleFuelType(VehicleFuelType vehicleFuelType, UUID organizationId) {
        vehicleFuelType.setOrganizationId(organizationId);
        VehicleFuelType savedFuelType = vehicleFuelTypeRepository.save(vehicleFuelType);
        return vehicleFuelTypeMapper.toDto(savedFuelType);
    }

    @Override
    public Optional<VehicleFuelTypeDTO> updateVehicleFuelType(UUID id, VehicleFuelType vehicleFuelType, UUID organizationId) {
        return vehicleFuelTypeRepository.findById(id)
                .filter(ft -> ft.getOrganizationId().equals(organizationId))
                .map(existingFuelType -> {
                    vehicleFuelType.setId(id);
                    vehicleFuelType.setOrganizationId(organizationId);
                    VehicleFuelType updatedFuelType = vehicleFuelTypeRepository.save(vehicleFuelType);
                    return vehicleFuelTypeMapper.toDto(updatedFuelType);
                });
    }

    @Override
    public Optional<VehicleFuelTypeDTO> getVehicleFuelTypeById(UUID id, UUID organizationId) {
        return vehicleFuelTypeRepository.findById(id)
                .filter(fuelType -> fuelType.getOrganizationId().equals(organizationId))
                .map(vehicleFuelTypeMapper::toDto);
    }

    @Override
    public Optional<VehicleFuelTypeDTO> getVehicleFuelTypeByCode(String code, UUID organizationId) {
        return vehicleFuelTypeRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleFuelTypeMapper::toDto);
    }

    @Override
    public List<VehicleFuelTypeDTO> getAllVehicleFuelTypes(UUID organizationId) {
        return vehicleFuelTypeRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleFuelTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleFuelTypeDTO> getAllActiveVehicleFuelTypes(UUID organizationId) {
        return vehicleFuelTypeRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleFuelTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleFuelType(UUID id, UUID organizationId) {
        return delete(id, organizationId);
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) {
        return vehicleFuelTypeRepository.findById(id)
                .filter(fuelType -> fuelType.getOrganizationId().equals(organizationId))
                .map(fuelType -> {
                    vehicleFuelTypeRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<VehicleFuelTypeDTO> setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleFuelTypeRepository.findById(id)
                .filter(fuelType -> fuelType.getOrganizationId().equals(organizationId))
                .map(fuelType -> {
                    fuelType.setActive(active);
                    VehicleFuelType updatedFuelType = vehicleFuelTypeRepository.save(fuelType);
                    return vehicleFuelTypeMapper.toDto(updatedFuelType);
                });
    }

    @Override
    public Optional<VehicleFuelTypeDTO> getById(UUID id, UUID organizationId) {
        return getVehicleFuelTypeById(id, organizationId);
    }

    @Override
    public Optional<VehicleFuelTypeDTO> getByCode(String code, UUID organizationId) {
        return getVehicleFuelTypeByCode(code, organizationId);
    }

    @Override
    public VehicleFuelTypeDTO create(VehicleFuelType entity, UUID organizationId) {
        return createVehicleFuelType(entity, organizationId);
    }

    @Override
    public Optional<VehicleFuelTypeDTO> update(UUID id, VehicleFuelType entity, UUID organizationId) {
        return updateVehicleFuelType(id, entity, organizationId);
    }

    @Override
    public List<VehicleFuelTypeDTO> getAll(UUID organizationId) {
        return getAllVehicleFuelTypes(organizationId);
    }

    @Override
    public List<VehicleFuelTypeDTO> getAllActive(UUID organizationId) {
        return getAllActiveVehicleFuelTypes(organizationId);
    }

    @Override
    public String getEntityName() {
        return "type de carburant de véhicule";
    }
}
