package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleBodyTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleBodyTypeMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleBodyTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleBodyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des types de carrosserie de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleBodyTypeServiceImpl implements VehicleBodyTypeService {

    private final VehicleBodyTypeRepository vehicleBodyTypeRepository;
    private final VehicleBodyTypeMapper vehicleBodyTypeMapper;

    @Override
    public VehicleBodyTypeDTO createVehicleBodyType(VehicleBodyType vehicleBodyType, UUID organizationId) {
        vehicleBodyType.setOrganizationId(organizationId);
        VehicleBodyType savedBodyType = vehicleBodyTypeRepository.save(vehicleBodyType);
        return vehicleBodyTypeMapper.toDto(savedBodyType);
    }

    @Override
    public Optional<VehicleBodyTypeDTO> updateVehicleBodyType(UUID id, VehicleBodyType vehicleBodyType, UUID organizationId) {
        return vehicleBodyTypeRepository.findById(id)
                .filter(bodyType -> bodyType.getOrganizationId().equals(organizationId))
                .map(existingBodyType -> {
                    vehicleBodyType.setId(id);
                    vehicleBodyType.setOrganizationId(organizationId);
                    VehicleBodyType updatedBodyType = vehicleBodyTypeRepository.save(vehicleBodyType);
                    return vehicleBodyTypeMapper.toDto(updatedBodyType);
                });
    }

    @Override
    public Optional<VehicleBodyTypeDTO> getVehicleBodyTypeById(UUID id, UUID organizationId) {
        return vehicleBodyTypeRepository.findById(id)
                .filter(bodyType -> bodyType.getOrganizationId().equals(organizationId))
                .map(vehicleBodyTypeMapper::toDto);
    }

    @Override
    public Optional<VehicleBodyTypeDTO> getByCode(String code, UUID organizationId) {
        return vehicleBodyTypeRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleBodyTypeMapper::toDto);
    }

    @Override
    public Optional<VehicleBodyTypeDTO> getVehicleBodyTypeByCode(String code, UUID organizationId) {
        return getByCode(code, organizationId);
    }

    @Override
    public List<VehicleBodyTypeDTO> getAllVehicleBodyTypes(UUID organizationId) {
        return vehicleBodyTypeRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleBodyTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleBodyTypeDTO> getAllActiveVehicleBodyTypes(UUID organizationId) {
        return vehicleBodyTypeRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleBodyTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleBodyTypeDTO> getAllActive(UUID organizationId) {
        return getAllActiveVehicleBodyTypes(organizationId);
    }

    @Override
    public String getEntityName() {
        return "VehicleBodyType";
    }

    @Override
    public Optional<VehicleBodyTypeDTO> update(UUID id, VehicleBodyType entity, UUID organizationId) {
        return updateVehicleBodyType(id, entity, organizationId);
    }

    @Override
    public VehicleBodyTypeDTO create(VehicleBodyType entity, UUID organizationId) {
        return createVehicleBodyType(entity, organizationId);
    }

    @Override
    public Optional<VehicleBodyTypeDTO> getById(UUID id, UUID organizationId) {
        return getVehicleBodyTypeById(id, organizationId);
    }

    @Override
    public List<VehicleBodyTypeDTO> getAll(UUID organizationId) {
        return getAllVehicleBodyTypes(organizationId);
    }

    @Override
    public boolean deleteVehicleBodyType(UUID id, UUID organizationId) {
        return delete(id, organizationId);
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) {
        return vehicleBodyTypeRepository.findById(id)
                .filter(bodyType -> bodyType.getOrganizationId().equals(organizationId))
                .map(bodyType -> {
                    vehicleBodyTypeRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<VehicleBodyTypeDTO> setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleBodyTypeRepository.findById(id)
                .filter(bodyType -> bodyType.getOrganizationId().equals(organizationId))
                .map(bodyType -> {
                    bodyType.setActive(active);
                    VehicleBodyType updatedBodyType = vehicleBodyTypeRepository.save(bodyType);
                    return vehicleBodyTypeMapper.toDto(updatedBodyType);
                });
    }
}
