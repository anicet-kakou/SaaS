package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleUsageDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleUsageMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleUsageService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des types d'usage de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleUsageServiceImpl implements VehicleUsageService {

    private final VehicleUsageRepository vehicleUsageRepository;
    private final VehicleUsageMapper vehicleUsageMapper;

    @Override
    public VehicleUsageDTO createVehicleUsage(VehicleUsage vehicleUsage, UUID organizationId) {
        vehicleUsage.setOrganizationId(organizationId);
        VehicleUsage savedUsage = vehicleUsageRepository.save(vehicleUsage);
        return vehicleUsageMapper.toDto(savedUsage);
    }

    @Override
    public Optional<VehicleUsageDTO> updateVehicleUsage(UUID id, VehicleUsage vehicleUsage, UUID organizationId) {
        return vehicleUsageRepository.findById(id)
                .filter(usage -> usage.getOrganizationId().equals(organizationId))
                .map(existingUsage -> {
                    vehicleUsage.setId(id);
                    vehicleUsage.setOrganizationId(organizationId);
                    VehicleUsage updatedUsage = vehicleUsageRepository.save(vehicleUsage);
                    return vehicleUsageMapper.toDto(updatedUsage);
                });
    }

    @Override
    public Optional<VehicleUsageDTO> getVehicleUsageById(UUID id, UUID organizationId) {
        return vehicleUsageRepository.findById(id)
                .filter(usage -> usage.getOrganizationId().equals(organizationId))
                .map(vehicleUsageMapper::toDto);
    }

    @Override
    public Optional<VehicleUsageDTO> getVehicleUsageByCode(String code, UUID organizationId) {
        return vehicleUsageRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleUsageMapper::toDto);
    }

    @Override
    public List<VehicleUsageDTO> getAllVehicleUsages(UUID organizationId) {
        return vehicleUsageRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleUsageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleUsageDTO> getAllActiveVehicleUsages(UUID organizationId) {
        return vehicleUsageRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleUsageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleUsage(UUID id, UUID organizationId) {
        return vehicleUsageRepository.findById(id)
                .filter(usage -> usage.getOrganizationId().equals(organizationId))
                .map(usage -> {
                    vehicleUsageRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
