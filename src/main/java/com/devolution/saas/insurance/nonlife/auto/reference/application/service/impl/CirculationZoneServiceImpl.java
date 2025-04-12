package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.CirculationZoneMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.CirculationZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.CirculationZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des zones de circulation.
 */
@Service
@RequiredArgsConstructor
public class CirculationZoneServiceImpl implements CirculationZoneService {

    private final CirculationZoneRepository circulationZoneRepository;
    private final CirculationZoneMapper circulationZoneMapper;

    @Override
    public CirculationZoneDTO createCirculationZone(CirculationZone circulationZone, UUID organizationId) {
        circulationZone.setOrganizationId(organizationId);
        CirculationZone savedZone = circulationZoneRepository.save(circulationZone);
        return circulationZoneMapper.toDto(savedZone);
    }

    @Override
    public Optional<CirculationZoneDTO> updateCirculationZone(UUID id, CirculationZone circulationZone, UUID organizationId) {
        return circulationZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(existingZone -> {
                    circulationZone.setId(id);
                    circulationZone.setOrganizationId(organizationId);
                    CirculationZone updatedZone = circulationZoneRepository.save(circulationZone);
                    return circulationZoneMapper.toDto(updatedZone);
                });
    }

    @Override
    public Optional<CirculationZoneDTO> getCirculationZoneById(UUID id, UUID organizationId) {
        return circulationZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(circulationZoneMapper::toDto);
    }

    @Override
    public Optional<CirculationZoneDTO> getCirculationZoneByCode(String code, UUID organizationId) {
        return circulationZoneRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(circulationZoneMapper::toDto);
    }

    @Override
    public List<CirculationZoneDTO> getAllCirculationZones(UUID organizationId) {
        return circulationZoneRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(circulationZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CirculationZoneDTO> getAllActiveCirculationZones(UUID organizationId) {
        return circulationZoneRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(circulationZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteCirculationZone(UUID id, UUID organizationId) {
        return circulationZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(zone -> {
                    circulationZoneRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<CirculationZoneDTO> setCirculationZoneActive(UUID id, boolean active, UUID organizationId) {
        return circulationZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(zone -> {
                    zone.setActive(active);
                    CirculationZone updatedZone = circulationZoneRepository.save(zone);
                    return circulationZoneMapper.toDto(updatedZone);
                });
    }
}
