package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleColorDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleColorMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleColorService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleColor;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des couleurs de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleColorServiceImpl implements VehicleColorService {

    private final VehicleColorRepository vehicleColorRepository;
    private final VehicleColorMapper vehicleColorMapper;

    @Override
    public VehicleColorDTO createVehicleColor(VehicleColor vehicleColor, UUID organizationId) {
        vehicleColor.setOrganizationId(organizationId);
        VehicleColor savedColor = vehicleColorRepository.save(vehicleColor);
        return vehicleColorMapper.toDto(savedColor);
    }

    @Override
    public Optional<VehicleColorDTO> updateVehicleColor(UUID id, VehicleColor vehicleColor, UUID organizationId) {
        return vehicleColorRepository.findById(id)
                .filter(color -> color.getOrganizationId().equals(organizationId))
                .map(existingColor -> {
                    vehicleColor.setId(id);
                    vehicleColor.setOrganizationId(organizationId);
                    VehicleColor updatedColor = vehicleColorRepository.save(vehicleColor);
                    return vehicleColorMapper.toDto(updatedColor);
                });
    }

    @Override
    public Optional<VehicleColorDTO> getVehicleColorById(UUID id, UUID organizationId) {
        return vehicleColorRepository.findById(id)
                .filter(color -> color.getOrganizationId().equals(organizationId))
                .map(vehicleColorMapper::toDto);
    }

    @Override
    public Optional<VehicleColorDTO> getVehicleColorByCode(String code, UUID organizationId) {
        return vehicleColorRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleColorMapper::toDto);
    }

    @Override
    public List<VehicleColorDTO> getAllVehicleColors(UUID organizationId) {
        return vehicleColorRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleColorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleColorDTO> getAllActiveVehicleColors(UUID organizationId) {
        return vehicleColorRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleColorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleColor(UUID id, UUID organizationId) {
        return vehicleColorRepository.findById(id)
                .filter(color -> color.getOrganizationId().equals(organizationId))
                .map(color -> {
                    vehicleColorRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
