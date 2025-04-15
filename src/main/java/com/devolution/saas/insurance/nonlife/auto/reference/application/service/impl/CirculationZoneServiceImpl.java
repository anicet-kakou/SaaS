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
        // Créer une nouvelle instance avec l'ID de l'organisation
        CirculationZone zoneWithOrg = CirculationZone.builder()
                .code(circulationZone.getCode())
                .name(circulationZone.getName())
                .description(circulationZone.getDescription())
                .active(circulationZone.isActive())
                .organizationId(organizationId) // Définir l'organisation ici
                .build();

        CirculationZone savedZone = circulationZoneRepository.save(zoneWithOrg);
        return circulationZoneMapper.toDto(savedZone);
    }

    @Override
    public Optional<CirculationZoneDTO> updateCirculationZone(UUID id, CirculationZone circulationZone, UUID organizationId) {
        return circulationZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(existingZone -> {
                    // Créer une nouvelle instance avec l'ID existant et les nouvelles propriétés
                    CirculationZone updatedZone = CirculationZone.builder()
                            .id(id) // Conserver l'ID existant
                            .code(circulationZone.getCode())
                            .name(circulationZone.getName())
                            .description(circulationZone.getDescription())
                            .active(circulationZone.isActive())
                            .organizationId(organizationId) // Définir l'organisation
                            .build();

                    CirculationZone savedZone = circulationZoneRepository.save(updatedZone);
                    return circulationZoneMapper.toDto(savedZone);
                });
    }

    @Override
    public Optional<CirculationZoneDTO> getCirculationZoneById(UUID id, UUID organizationId) {
        return circulationZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(circulationZoneMapper::toDto);
    }

    @Override
    public Optional<CirculationZoneDTO> getByCode(String code, UUID organizationId) {
        return circulationZoneRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(circulationZoneMapper::toDto);
    }

    @Override
    public String getEntityName() {
        return "CirculationZone";
    }

    @Override
    public List<CirculationZoneDTO> getAllActive(UUID organizationId) {
        return circulationZoneRepository.findAllByIsActiveAndOrganizationId(true, organizationId).stream()
                .map(circulationZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) {
        return circulationZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(zone -> {
                    circulationZoneRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<CirculationZoneDTO> update(UUID id, CirculationZone entity, UUID organizationId) {
        return updateCirculationZone(id, entity, organizationId);
    }

    @Override
    public CirculationZoneDTO create(CirculationZone entity, UUID organizationId) {
        return createCirculationZone(entity, organizationId);
    }

    @Override
    public Optional<CirculationZoneDTO> getById(UUID id, UUID organizationId) {
        return getCirculationZoneById(id, organizationId);
    }

    @Override
    public List<CirculationZoneDTO> getAll(UUID organizationId) {
        return getAllCirculationZones(organizationId);
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
    public Optional<CirculationZoneDTO> setActive(UUID id, boolean active, UUID organizationId) {
        return circulationZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(existingZone -> {
                    // Créer une nouvelle instance avec l'active mis à jour
                    CirculationZone updatedZone = CirculationZone.builder()
                            .id(existingZone.getId())
                            .code(existingZone.getCode())
                            .name(existingZone.getName())
                            .description(existingZone.getDescription())
                            .active(active) // Mettre à jour l'active
                            .organizationId(existingZone.getOrganizationId())
                            .build();

                    CirculationZone savedZone = circulationZoneRepository.save(updatedZone);
                    return circulationZoneMapper.toDto(savedZone);
                });
    }
}
