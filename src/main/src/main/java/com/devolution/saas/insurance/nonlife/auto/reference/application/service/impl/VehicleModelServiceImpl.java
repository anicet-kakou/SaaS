package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleModelDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleModelMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleModelService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des modèles de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleModelMapper vehicleModelMapper;

    @Override
    public VehicleModelDTO createVehicleModel(VehicleModel vehicleModel, UUID organizationId) {
        vehicleModel.setOrganizationId(organizationId);
        VehicleModel savedModel = vehicleModelRepository.save(vehicleModel);
        return vehicleModelMapper.toDto(savedModel);
    }

    @Override
    public Optional<VehicleModelDTO> updateVehicleModel(UUID id, VehicleModel vehicleModel, UUID organizationId) {
        return vehicleModelRepository.findById(id)
                .filter(model -> model.getOrganizationId().equals(organizationId))
                .map(existingModel -> {
                    vehicleModel.setId(id);
                    vehicleModel.setOrganizationId(organizationId);
                    VehicleModel updatedModel = vehicleModelRepository.save(vehicleModel);
                    return vehicleModelMapper.toDto(updatedModel);
                });
    }

    @Override
    public Optional<VehicleModelDTO> getVehicleModelById(UUID id, UUID organizationId) {
        return vehicleModelRepository.findById(id)
                .filter(model -> model.getOrganizationId().equals(organizationId))
                .map(vehicleModelMapper::toDto);
    }

    @Override
    public Optional<VehicleModelDTO> getVehicleModelByCodeAndMake(String code, UUID makeId, UUID organizationId) {
        return vehicleModelRepository.findByCodeAndMakeIdAndOrganizationId(code, makeId, organizationId)
                .map(vehicleModelMapper::toDto);
    }

    @Override
    public List<VehicleModelDTO> getAllVehicleModels(UUID organizationId) {
        return vehicleModelRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleModelDTO> getAllActiveVehicleModels(UUID organizationId) {
        return vehicleModelRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleModelDTO> getAllVehicleModelsByMake(UUID makeId, UUID organizationId) {
        return vehicleModelRepository.findAllByMakeIdAndOrganizationId(makeId, organizationId)
                .stream()
                .map(vehicleModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleModelDTO> getAllActiveVehicleModelsByMake(UUID makeId, UUID organizationId) {
        return vehicleModelRepository.findAllActiveByMakeIdAndOrganizationId(makeId, organizationId)
                .stream()
                .map(vehicleModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleModel(UUID id, UUID organizationId) {
        return vehicleModelRepository.findById(id)
                .filter(model -> model.getOrganizationId().equals(organizationId))
                .map(model -> {
                    vehicleModelRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
