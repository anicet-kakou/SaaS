package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleMakeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleMakeMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleMakeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleMake;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleMakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des marques de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleMakeServiceImpl implements VehicleMakeService {

    private final VehicleMakeRepository vehicleMakeRepository;
    private final VehicleMakeMapper vehicleMakeMapper;

    @Override
    public VehicleMakeDTO createVehicleMake(VehicleMake vehicleMake, UUID organizationId) {
        vehicleMake.setOrganizationId(organizationId);
        VehicleMake savedMake = vehicleMakeRepository.save(vehicleMake);
        return vehicleMakeMapper.toDto(savedMake);
    }

    @Override
    public Optional<VehicleMakeDTO> updateVehicleMake(UUID id, VehicleMake vehicleMake, UUID organizationId) {
        return vehicleMakeRepository.findById(id)
                .filter(make -> make.getOrganizationId().equals(organizationId))
                .map(existingMake -> {
                    vehicleMake.setId(id);
                    vehicleMake.setOrganizationId(organizationId);
                    VehicleMake updatedMake = vehicleMakeRepository.save(vehicleMake);
                    return vehicleMakeMapper.toDto(updatedMake);
                });
    }

    @Override
    public Optional<VehicleMakeDTO> getVehicleMakeById(UUID id, UUID organizationId) {
        return vehicleMakeRepository.findById(id)
                .filter(make -> make.getOrganizationId().equals(organizationId))
                .map(vehicleMakeMapper::toDto);
    }

    @Override
    public Optional<VehicleMakeDTO> getVehicleMakeByCode(String code, UUID organizationId) {
        return vehicleMakeRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleMakeMapper::toDto);
    }

    @Override
    public List<VehicleMakeDTO> getAllVehicleMakes(UUID organizationId) {
        return vehicleMakeRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleMakeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleMakeDTO> getAllActiveVehicleMakes(UUID organizationId) {
        return vehicleMakeRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleMakeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleMake(UUID id, UUID organizationId) {
        return vehicleMakeRepository.findById(id)
                .filter(make -> make.getOrganizationId().equals(organizationId))
                .map(make -> {
                    vehicleMakeRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
