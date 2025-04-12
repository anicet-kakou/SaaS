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
import java.util.stream.Collectors;

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
        vehicleManufacturer.setOrganizationId(organizationId);
        VehicleManufacturer savedManufacturer = vehicleManufacturerRepository.save(vehicleManufacturer);
        return vehicleManufacturerMapper.toDto(savedManufacturer);
    }

    @Override
    public Optional<VehicleManufacturerDTO> updateVehicleManufacturer(UUID id, VehicleManufacturer vehicleManufacturer, UUID organizationId) {
        return vehicleManufacturerRepository.findById(id)
                .filter(manufacturer -> manufacturer.getOrganizationId().equals(organizationId))
                .map(existingManufacturer -> {
                    vehicleManufacturer.setId(id);
                    vehicleManufacturer.setOrganizationId(organizationId);
                    VehicleManufacturer updatedManufacturer = vehicleManufacturerRepository.save(vehicleManufacturer);
                    return vehicleManufacturerMapper.toDto(updatedManufacturer);
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
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleManufacturerDTO> getAllActiveVehicleManufacturers(UUID organizationId) {
        return vehicleManufacturerRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleManufacturerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleManufacturer(UUID id, UUID organizationId) {
        return vehicleManufacturerRepository.findById(id)
                .filter(manufacturer -> manufacturer.getOrganizationId().equals(organizationId))
                .map(manufacturer -> {
                    vehicleManufacturerRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
