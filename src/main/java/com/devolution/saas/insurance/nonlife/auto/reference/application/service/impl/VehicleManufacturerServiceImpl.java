package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleManufacturerDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleManufacturerMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleManufacturerService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation du service de gestion des fabricants de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleManufacturerServiceImpl implements VehicleManufacturerService {

    private final VehicleManufacturerRepository vehicleManufacturerRepository;
    private final VehicleManufacturerMapper vehicleManufacturerMapper;

    @Override
    public VehicleManufacturerDTO createVehicleManufacturer(VehicleManufacturer vehicleManufacturer, UUID organizationId) {
        // Créer une nouvelle instance avec l'ID de l'organisation
        VehicleManufacturer manufacturerWithOrg = VehicleManufacturer.builder()
                .code(vehicleManufacturer.getCode())
                .name(vehicleManufacturer.getName())
                .description(vehicleManufacturer.getDescription())
                .active(vehicleManufacturer.isActive())
                .organizationId(organizationId) // Définir l'organisation ici
                .build();

        VehicleManufacturer savedManufacturer = vehicleManufacturerRepository.save(manufacturerWithOrg);
        return vehicleManufacturerMapper.toDto(savedManufacturer);
    }

    @Override
    public Optional<VehicleManufacturerDTO> updateVehicleManufacturer(UUID id, VehicleManufacturer vehicleManufacturer, UUID organizationId) {
        return vehicleManufacturerRepository.findById(id)
                .filter(manufacturer -> manufacturer.getOrganizationId().equals(organizationId))
                .map(existingManufacturer -> {
                    // Créer une nouvelle instance avec l'ID existant et les nouvelles propriétés
                    VehicleManufacturer updatedManufacturer = VehicleManufacturer.builder()
                            .id(id) // Conserver l'ID existant
                            .code(vehicleManufacturer.getCode())
                            .name(vehicleManufacturer.getName())
                            .description(vehicleManufacturer.getDescription())
                            .active(vehicleManufacturer.isActive())
                            .organizationId(organizationId) // Définir l'organisation
                            .build();

                    VehicleManufacturer savedManufacturer = vehicleManufacturerRepository.save(updatedManufacturer);
                    return vehicleManufacturerMapper.toDto(savedManufacturer);
                });
    }

    @Override
    public Optional<VehicleManufacturerDTO> getVehicleManufacturerById(UUID id, UUID organizationId) {
        return vehicleManufacturerRepository.findById(id)
                .filter(manufacturer -> manufacturer.getOrganizationId().equals(organizationId))
                .map(vehicleManufacturerMapper::toDto);
    }

    @Override
    public Optional<VehicleManufacturerDTO> getVehicleManufacturerByCode(String code, UUID organizationId) {
        return vehicleManufacturerRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleManufacturerMapper::toDto);
    }

    @Override
    public List<VehicleManufacturerDTO> getAllVehicleManufacturers(UUID organizationId) {
        return vehicleManufacturerRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleManufacturerMapper::toDto)
                .toList();
    }

    @Override
    public List<VehicleManufacturerDTO> getAllActiveVehicleManufacturers(UUID organizationId) {
        return vehicleManufacturerRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleManufacturerMapper::toDto)
                .toList();
    }

    @Override
    public boolean deleteVehicleManufacturer(UUID id, UUID organizationId) {
        return delete(id, organizationId);
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) {
        return vehicleManufacturerRepository.findById(id)
                .filter(manufacturer -> manufacturer.getOrganizationId().equals(organizationId))
                .map(manufacturer -> {
                    vehicleManufacturerRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<VehicleManufacturerDTO> setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleManufacturerRepository.findById(id)
                .filter(manufacturer -> manufacturer.getOrganizationId().equals(organizationId))
                .map(existingManufacturer -> {
                    // Créer une nouvelle instance avec l'active mis à jour
                    VehicleManufacturer updatedManufacturer = VehicleManufacturer.builder()
                            .id(existingManufacturer.getId())
                            .code(existingManufacturer.getCode())
                            .name(existingManufacturer.getName())
                            .description(existingManufacturer.getDescription())
                            .active(active) // Mettre à jour l'active
                            .organizationId(existingManufacturer.getOrganizationId())
                            .build();

                    VehicleManufacturer savedManufacturer = vehicleManufacturerRepository.save(updatedManufacturer);
                    return vehicleManufacturerMapper.toDto(savedManufacturer);
                });
    }

    @Override
    public Optional<VehicleManufacturerDTO> getByCode(String code, UUID organizationId) {
        return getVehicleManufacturerByCode(code, organizationId);
    }

    @Override
    public VehicleManufacturerDTO create(VehicleManufacturer entity, UUID organizationId) {
        return createVehicleManufacturer(entity, organizationId);
    }

    @Override
    public Optional<VehicleManufacturerDTO> update(UUID id, VehicleManufacturer entity, UUID organizationId) {
        return updateVehicleManufacturer(id, entity, organizationId);
    }

    @Override
    public Optional<VehicleManufacturerDTO> getById(UUID id, UUID organizationId) {
        return getVehicleManufacturerById(id, organizationId);
    }

    @Override
    public List<VehicleManufacturerDTO> getAll(UUID organizationId) {
        return getAllVehicleManufacturers(organizationId);
    }

    @Override
    public List<VehicleManufacturerDTO> getAllActive(UUID organizationId) {
        return getAllActiveVehicleManufacturers(organizationId);
    }

    @Override
    public String getEntityName() {
        return "fabricant de véhicule";
    }
}
