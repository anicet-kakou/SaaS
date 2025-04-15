package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.GeographicZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.GeographicZoneMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.GeographicZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.GeographicZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des zones géographiques.
 */
@Service
@RequiredArgsConstructor
public class GeographicZoneServiceImpl implements GeographicZoneService {

    private final GeographicZoneRepository geographicZoneRepository;
    private final GeographicZoneMapper geographicZoneMapper;

    @Override
    public GeographicZoneDTO createGeographicZone(GeographicZone geographicZone, UUID organizationId) {
        geographicZone.setOrganizationId(organizationId);
        GeographicZone savedZone = geographicZoneRepository.save(geographicZone);
        return geographicZoneMapper.toDto(savedZone);
    }

    @Override
    public Optional<GeographicZoneDTO> updateGeographicZone(UUID id, GeographicZone geographicZone, UUID organizationId) {
        return geographicZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(existingZone -> {
                    geographicZone.setId(id);
                    geographicZone.setOrganizationId(organizationId);
                    GeographicZone updatedZone = geographicZoneRepository.save(geographicZone);
                    return geographicZoneMapper.toDto(updatedZone);
                });
    }

    @Override
    public Optional<GeographicZoneDTO> getGeographicZoneById(UUID id, UUID organizationId) {
        return geographicZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(geographicZoneMapper::toDto);
    }

    @Override
    public Optional<GeographicZoneDTO> getByCode(String code, UUID organizationId) {
        return geographicZoneRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(geographicZoneMapper::toDto);
    }

    @Override
    public String getEntityName() {
        return "GeographicZone";
    }

    @Override
    public List<GeographicZoneDTO> getAllActive(UUID organizationId) {
        return geographicZoneRepository.findAllByIsActiveAndOrganizationId(true, organizationId).stream()
                .map(geographicZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) {
        return geographicZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(zone -> {
                    geographicZoneRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<GeographicZoneDTO> update(UUID id, GeographicZone entity, UUID organizationId) {
        return updateGeographicZone(id, entity, organizationId);
    }

    @Override
    public GeographicZoneDTO create(GeographicZone entity, UUID organizationId) {
        return createGeographicZone(entity, organizationId);
    }

    @Override
    public Optional<GeographicZoneDTO> getById(UUID id, UUID organizationId) {
        return getGeographicZoneById(id, organizationId);
    }

    @Override
    public List<GeographicZoneDTO> getAll(UUID organizationId) {
        return getAllGeographicZones(organizationId);
    }

    @Override
    public List<GeographicZoneDTO> getAllGeographicZones(UUID organizationId) {
        return geographicZoneRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(geographicZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GeographicZoneDTO> getAllActiveGeographicZones(UUID organizationId) {
        return geographicZoneRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(geographicZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteGeographicZone(UUID id, UUID organizationId) {
        return geographicZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(zone -> {
                    geographicZoneRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }


    @Override
    public Optional<GeographicZoneDTO> setActive(UUID id, boolean active, UUID organizationId) {
        return geographicZoneRepository.findById(id)
                .filter(zone -> zone.getOrganizationId().equals(organizationId))
                .map(zone -> {
                    zone.setActive(active);
                    GeographicZone updatedZone = geographicZoneRepository.save(zone);
                    return geographicZoneMapper.toDto(updatedZone);
                });
    }
}
