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
        // Créer une nouvelle instance avec l'ID de l'organisation
        VehicleModel modelWithOrg = new VehicleModel();
        modelWithOrg.setCode(vehicleModel.getCode());
        modelWithOrg.setName(vehicleModel.getName());
        modelWithOrg.setDescription(vehicleModel.getDescription());
        modelWithOrg.setManufacturerId(vehicleModel.getManufacturerId());
        modelWithOrg.setActive(vehicleModel.isActive());
        modelWithOrg.setOrganizationId(organizationId); // Définir l'organisation ici

        VehicleModel savedModel = vehicleModelRepository.save(modelWithOrg);
        return vehicleModelMapper.toDto(savedModel);
    }

    @Override
    public Optional<VehicleModelDTO> updateVehicleModel(UUID id, VehicleModel vehicleModel, UUID organizationId) {
        return vehicleModelRepository.findById(id)
                .filter(model -> model.getOrganizationId().equals(organizationId))
                .map(existingModel -> {
                    // Créer une nouvelle instance avec l'ID existant et les nouvelles propriétés
                    VehicleModel updatedModel = new VehicleModel();
                    updatedModel.setId(id); // Conserver l'ID existant
                    updatedModel.setCode(vehicleModel.getCode());
                    updatedModel.setName(vehicleModel.getName());
                    updatedModel.setDescription(vehicleModel.getDescription());
                    updatedModel.setManufacturerId(vehicleModel.getManufacturerId());
                    updatedModel.setActive(vehicleModel.isActive());
                    updatedModel.setOrganizationId(organizationId); // Définir l'organisation

                    VehicleModel savedModel = vehicleModelRepository.save(updatedModel);
                    return vehicleModelMapper.toDto(savedModel);
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
                .toList();
    }

    @Override
    public List<VehicleModelDTO> getAllActiveVehicleModels(UUID organizationId) {
        return vehicleModelRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleModelMapper::toDto)
                .toList();
    }

    @Override
    public List<VehicleModelDTO> getAllVehicleModelsByMake(UUID makeId, UUID organizationId) {
        return vehicleModelRepository.findAllByMakeIdAndOrganizationId(makeId, organizationId)
                .stream()
                .map(vehicleModelMapper::toDto)
                .toList();
    }

    @Override
    public List<VehicleModelDTO> getAllActiveVehicleModelsByMake(UUID makeId, UUID organizationId) {
        return vehicleModelRepository.findAllActiveByMakeIdAndOrganizationId(makeId, organizationId)
                .stream()
                .map(vehicleModelMapper::toDto)
                .toList();
    }

    @Override
    public boolean deleteVehicleModel(UUID id, UUID organizationId) {
        return delete(id, organizationId);
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) {
        return vehicleModelRepository.findById(id)
                .filter(model -> model.getOrganizationId().equals(organizationId))
                .map(model -> {
                    vehicleModelRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<VehicleModelDTO> getByCode(String code, UUID organizationId) {
        // Since we don't have a direct method to get by code without makeId,
        // we'll return the first model with this code regardless of make
        return vehicleModelRepository.findAllByOrganizationId(organizationId).stream()
                .filter(model -> model.getCode().equals(code))
                .findFirst()
                .map(vehicleModelMapper::toDto);
    }

    @Override
    public Optional<VehicleModelDTO> setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleModelRepository.findById(id)
                .filter(model -> model.getOrganizationId().equals(organizationId))
                .map(existingModel -> {
                    // Créer une nouvelle instance avec l'active mis à jour
                    VehicleModel updatedModel = new VehicleModel();
                    updatedModel.setId(existingModel.getId());
                    updatedModel.setCode(existingModel.getCode());
                    updatedModel.setName(existingModel.getName());
                    updatedModel.setDescription(existingModel.getDescription());
                    updatedModel.setManufacturerId(existingModel.getManufacturerId());
                    updatedModel.setActive(active); // Mettre à jour l'active
                    updatedModel.setOrganizationId(existingModel.getOrganizationId());

                    VehicleModel savedModel = vehicleModelRepository.save(updatedModel);
                    return vehicleModelMapper.toDto(savedModel);
                });
    }

    @Override
    public VehicleModelDTO create(VehicleModel entity, UUID organizationId) {
        return createVehicleModel(entity, organizationId);
    }

    @Override
    public Optional<VehicleModelDTO> update(UUID id, VehicleModel entity, UUID organizationId) {
        return updateVehicleModel(id, entity, organizationId);
    }

    @Override
    public Optional<VehicleModelDTO> getById(UUID id, UUID organizationId) {
        return getVehicleModelById(id, organizationId);
    }

    @Override
    public List<VehicleModelDTO> getAll(UUID organizationId) {
        return getAllVehicleModels(organizationId);
    }

    @Override
    public List<VehicleModelDTO> getAllActive(UUID organizationId) {
        return getAllActiveVehicleModels(organizationId);
    }

    @Override
    public String getEntityName() {
        return "VehicleModel";
    }

    public Optional<VehicleModelDTO> getVehicleModelByCode(String code, UUID organizationId) {
        return getByCode(code, organizationId);
    }
}
