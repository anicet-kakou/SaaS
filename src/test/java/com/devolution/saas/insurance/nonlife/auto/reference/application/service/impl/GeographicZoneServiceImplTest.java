package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.GeographicZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.GeographicZoneMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.GeographicZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeographicZoneServiceImplTest {

    @Mock
    private GeographicZoneRepository geographicZoneRepository;

    @Mock
    private GeographicZoneMapper geographicZoneMapper;

    @InjectMocks
    private GeographicZoneServiceImpl service;

    private UUID organizationId;
    private UUID zoneId;
    private GeographicZone geographicZone;
    private GeographicZoneDTO geographicZoneDTO;
    private List<GeographicZone> activeZones;
    private List<GeographicZone> allZones;
    private List<GeographicZoneDTO> activeZoneDTOs;
    private List<GeographicZoneDTO> allZoneDTOs;

    @BeforeEach
    void setUp() {
        organizationId = UUID.randomUUID();
        zoneId = UUID.randomUUID();

        geographicZone = new GeographicZone();
        geographicZone.setId(zoneId);
        geographicZone.setCode("ZONE1");
        geographicZone.setName("Zone 1");
        geographicZone.setActive(true);
        geographicZone.setOrganizationId(organizationId);

        geographicZoneDTO = new GeographicZoneDTO();
        geographicZoneDTO.setId(zoneId);
        geographicZoneDTO.setCode("ZONE1");
        geographicZoneDTO.setName("Zone 1");
        geographicZoneDTO.setActive(true);

        GeographicZone zone1 = new GeographicZone();
        zone1.setId(UUID.randomUUID());
        zone1.setCode("ZONE1");
        zone1.setName("Zone 1");
        zone1.setActive(true);
        zone1.setOrganizationId(organizationId);

        GeographicZone zone2 = new GeographicZone();
        zone2.setId(UUID.randomUUID());
        zone2.setCode("ZONE2");
        zone2.setName("Zone 2");
        zone2.setActive(true);
        zone2.setOrganizationId(organizationId);

        GeographicZone zone3 = new GeographicZone();
        zone3.setId(UUID.randomUUID());
        zone3.setCode("ZONE3");
        zone3.setName("Zone 3");
        zone3.setActive(false);
        zone3.setOrganizationId(organizationId);

        activeZones = Arrays.asList(zone1, zone2);
        allZones = Arrays.asList(zone1, zone2, zone3);

        GeographicZoneDTO zone1DTO = new GeographicZoneDTO();
        zone1DTO.setId(zone1.getId());
        zone1DTO.setCode("ZONE1");
        zone1DTO.setName("Zone 1");
        zone1DTO.setActive(true);

        GeographicZoneDTO zone2DTO = new GeographicZoneDTO();
        zone2DTO.setId(zone2.getId());
        zone2DTO.setCode("ZONE2");
        zone2DTO.setName("Zone 2");
        zone2DTO.setActive(true);

        GeographicZoneDTO zone3DTO = new GeographicZoneDTO();
        zone3DTO.setId(zone3.getId());
        zone3DTO.setCode("ZONE3");
        zone3DTO.setName("Zone 3");
        zone3DTO.setActive(false);

        activeZoneDTOs = Arrays.asList(zone1DTO, zone2DTO);
        allZoneDTOs = Arrays.asList(zone1DTO, zone2DTO, zone3DTO);

        // Setup mapper
        when(geographicZoneMapper.toDto(zone1)).thenReturn(zone1DTO);
        when(geographicZoneMapper.toDto(zone2)).thenReturn(zone2DTO);
        when(geographicZoneMapper.toDto(zone3)).thenReturn(zone3DTO);
        when(geographicZoneMapper.toDto(geographicZone)).thenReturn(geographicZoneDTO);
    }

    @Test
    void createGeographicZone_ShouldReturnCreatedZone() {
        // Given
        when(geographicZoneRepository.save(geographicZone)).thenReturn(geographicZone);

        // When
        GeographicZoneDTO result = service.createGeographicZone(geographicZone, organizationId);

        // Then
        assertEquals(geographicZoneDTO, result);
        assertEquals(organizationId, geographicZone.getOrganizationId());
        verify(geographicZoneRepository).save(geographicZone);
        verify(geographicZoneMapper).toDto(geographicZone);
    }

    @Test
    void getAllActiveGeographicZones_ShouldReturnActiveZones() {
        // Given
        when(geographicZoneRepository.findAllByOrganizationIdAndActiveIsTrue(organizationId))
                .thenReturn(activeZones);

        // When
        List<GeographicZoneDTO> result = service.getAllActiveGeographicZones(organizationId);

        // Then
        assertEquals(activeZoneDTOs.size(), result.size());
        verify(geographicZoneRepository).findAllByOrganizationIdAndActiveIsTrue(organizationId);
        verify(geographicZoneMapper, times(activeZones.size())).toDto(any(GeographicZone.class));
    }

    @Test
    void getAllGeographicZones_ShouldReturnAllZones() {
        // Given
        when(geographicZoneRepository.findAllByOrganizationId(organizationId))
                .thenReturn(allZones);

        // When
        List<GeographicZoneDTO> result = service.getAllGeographicZones(organizationId);

        // Then
        assertEquals(allZoneDTOs.size(), result.size());
        verify(geographicZoneRepository).findAllByOrganizationId(organizationId);
        verify(geographicZoneMapper, times(allZones.size())).toDto(any(GeographicZone.class));
    }

    @Test
    void getGeographicZoneById_ShouldReturnZoneById() {
        // Given
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(geographicZone));

        // When
        Optional<GeographicZoneDTO> result = service.getGeographicZoneById(zoneId, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(geographicZoneDTO, result.get());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneMapper).toDto(geographicZone);
    }

    @Test
    void getGeographicZoneById_ShouldReturnEmpty_WhenZoneNotFound() {
        // Given
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        // When
        Optional<GeographicZoneDTO> result = service.getGeographicZoneById(zoneId, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneMapper, never()).toDto(any(GeographicZone.class));
    }

    @Test
    void getGeographicZoneById_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(geographicZone));

        // When
        Optional<GeographicZoneDTO> result = service.getGeographicZoneById(zoneId, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneMapper, never()).toDto(any(GeographicZone.class));
    }

    @Test
    void getByCode_ShouldReturnZoneByCode() {
        // Given
        String code = "ZONE1";
        when(geographicZoneRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.of(geographicZone));

        // When
        Optional<GeographicZoneDTO> result = service.getByCode(code, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(geographicZoneDTO, result.get());
        verify(geographicZoneRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(geographicZoneMapper).toDto(geographicZone);
    }

    @Test
    void getByCode_ShouldReturnEmpty_WhenZoneNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(geographicZoneRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.empty());

        // When
        Optional<GeographicZoneDTO> result = service.getByCode(code, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(geographicZoneRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(geographicZoneMapper, never()).toDto(any(GeographicZone.class));
    }

    @Test
    void updateGeographicZone_ShouldReturnUpdatedZone() {
        // Given
        GeographicZone existingZone = new GeographicZone();
        existingZone.setId(zoneId);
        existingZone.setCode("ZONE1");
        existingZone.setName("Old Name");
        existingZone.setActive(true);
        existingZone.setOrganizationId(organizationId);

        GeographicZone updatedZone = new GeographicZone();
        updatedZone.setId(zoneId);
        updatedZone.setCode("ZONE1");
        updatedZone.setName("New Name");
        updatedZone.setActive(true);
        updatedZone.setOrganizationId(organizationId);

        GeographicZoneDTO updatedZoneDTO = new GeographicZoneDTO();
        updatedZoneDTO.setId(zoneId);
        updatedZoneDTO.setCode("ZONE1");
        updatedZoneDTO.setName("New Name");
        updatedZoneDTO.setActive(true);

        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(existingZone));
        when(geographicZoneRepository.save(any(GeographicZone.class)))
                .thenReturn(updatedZone);
        when(geographicZoneMapper.toDto(updatedZone))
                .thenReturn(updatedZoneDTO);

        // When
        Optional<GeographicZoneDTO> result = service.updateGeographicZone(zoneId, updatedZone, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedZoneDTO, result.get());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository).save(any(GeographicZone.class));
        verify(geographicZoneMapper).toDto(updatedZone);
    }

    @Test
    void updateGeographicZone_ShouldReturnEmpty_WhenZoneNotFound() {
        // Given
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        // When
        Optional<GeographicZoneDTO> result = service.updateGeographicZone(zoneId, geographicZone, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository, never()).save(any(GeographicZone.class));
        verify(geographicZoneMapper, never()).toDto(any(GeographicZone.class));
    }

    @Test
    void updateGeographicZone_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(geographicZone));

        // When
        Optional<GeographicZoneDTO> result = service.updateGeographicZone(zoneId, geographicZone, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository, never()).save(any(GeographicZone.class));
        verify(geographicZoneMapper, never()).toDto(any(GeographicZone.class));
    }

    @Test
    void setActive_ShouldReturnUpdatedZone() {
        // Given
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(geographicZone));

        GeographicZone updatedZone = new GeographicZone();
        updatedZone.setId(zoneId);
        updatedZone.setCode("ZONE1");
        updatedZone.setName("Zone 1");
        updatedZone.setActive(false); // Changed to inactive
        updatedZone.setOrganizationId(organizationId);

        GeographicZoneDTO updatedZoneDTO = new GeographicZoneDTO();
        updatedZoneDTO.setId(zoneId);
        updatedZoneDTO.setCode("ZONE1");
        updatedZoneDTO.setName("Zone 1");
        updatedZoneDTO.setActive(false); // Changed to inactive

        when(geographicZoneRepository.save(any(GeographicZone.class)))
                .thenReturn(updatedZone);
        when(geographicZoneMapper.toDto(updatedZone))
                .thenReturn(updatedZoneDTO);

        // When
        Optional<GeographicZoneDTO> result = service.setActive(zoneId, false, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedZoneDTO, result.get());
        assertFalse(result.get().isActive());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository).save(any(GeographicZone.class));
        verify(geographicZoneMapper).toDto(updatedZone);
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenZoneNotFound() {
        // Given
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        // When
        Optional<GeographicZoneDTO> result = service.setActive(zoneId, false, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository, never()).save(any(GeographicZone.class));
        verify(geographicZoneMapper, never()).toDto(any(GeographicZone.class));
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(geographicZone));

        // When
        Optional<GeographicZoneDTO> result = service.setActive(zoneId, false, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository, never()).save(any(GeographicZone.class));
        verify(geographicZoneMapper, never()).toDto(any(GeographicZone.class));
    }

    @Test
    void delete_ShouldReturnTrue_WhenZoneDeleted() {
        // Given
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(geographicZone));

        // When
        boolean result = service.delete(zoneId, organizationId);

        // Then
        assertTrue(result);
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository).deleteById(zoneId);
    }

    @Test
    void delete_ShouldReturnFalse_WhenZoneNotFound() {
        // Given
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        // When
        boolean result = service.delete(zoneId, organizationId);

        // Then
        assertFalse(result);
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void delete_ShouldReturnFalse_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(geographicZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(geographicZone));

        // When
        boolean result = service.delete(zoneId, differentOrgId);

        // Then
        assertFalse(result);
        verify(geographicZoneRepository).findById(zoneId);
        verify(geographicZoneRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = service.getEntityName();

        // Then
        assertEquals("zone géographique", result);
    }
}
