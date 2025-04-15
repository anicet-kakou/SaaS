package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.CirculationZoneMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.CirculationZoneRepository;
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
class CirculationZoneServiceImplTest {

    @Mock
    private CirculationZoneRepository circulationZoneRepository;

    @Mock
    private CirculationZoneMapper circulationZoneMapper;

    @InjectMocks
    private CirculationZoneServiceImpl service;

    private UUID organizationId;
    private UUID zoneId;
    private CirculationZone circulationZone;
    private CirculationZoneDTO circulationZoneDTO;
    private List<CirculationZone> activeZones;
    private List<CirculationZone> allZones;
    private List<CirculationZoneDTO> activeZoneDTOs;
    private List<CirculationZoneDTO> allZoneDTOs;

    @BeforeEach
    void setUp() {
        organizationId = UUID.randomUUID();
        zoneId = UUID.randomUUID();

        circulationZone = new CirculationZone();
        circulationZone.setId(zoneId);
        circulationZone.setCode("ZONE1");
        circulationZone.setName("Zone 1");
        circulationZone.setActive(true);
        circulationZone.setOrganizationId(organizationId);

        circulationZoneDTO = new CirculationZoneDTO();
        circulationZoneDTO.setId(zoneId);
        circulationZoneDTO.setCode("ZONE1");
        circulationZoneDTO.setName("Zone 1");
        circulationZoneDTO.setActive(true);

        CirculationZone zone1 = new CirculationZone();
        zone1.setId(UUID.randomUUID());
        zone1.setCode("ZONE1");
        zone1.setName("Zone 1");
        zone1.setActive(true);
        zone1.setOrganizationId(organizationId);

        CirculationZone zone2 = new CirculationZone();
        zone2.setId(UUID.randomUUID());
        zone2.setCode("ZONE2");
        zone2.setName("Zone 2");
        zone2.setActive(true);
        zone2.setOrganizationId(organizationId);

        CirculationZone zone3 = new CirculationZone();
        zone3.setId(UUID.randomUUID());
        zone3.setCode("ZONE3");
        zone3.setName("Zone 3");
        zone3.setActive(false);
        zone3.setOrganizationId(organizationId);

        activeZones = Arrays.asList(zone1, zone2);
        allZones = Arrays.asList(zone1, zone2, zone3);

        CirculationZoneDTO zone1DTO = new CirculationZoneDTO();
        zone1DTO.setId(zone1.getId());
        zone1DTO.setCode("ZONE1");
        zone1DTO.setName("Zone 1");
        zone1DTO.setActive(true);

        CirculationZoneDTO zone2DTO = new CirculationZoneDTO();
        zone2DTO.setId(zone2.getId());
        zone2DTO.setCode("ZONE2");
        zone2DTO.setName("Zone 2");
        zone2DTO.setActive(true);

        CirculationZoneDTO zone3DTO = new CirculationZoneDTO();
        zone3DTO.setId(zone3.getId());
        zone3DTO.setCode("ZONE3");
        zone3DTO.setName("Zone 3");
        zone3DTO.setActive(false);

        activeZoneDTOs = Arrays.asList(zone1DTO, zone2DTO);
        allZoneDTOs = Arrays.asList(zone1DTO, zone2DTO, zone3DTO);

        // Setup mapper
        when(circulationZoneMapper.toDto(zone1)).thenReturn(zone1DTO);
        when(circulationZoneMapper.toDto(zone2)).thenReturn(zone2DTO);
        when(circulationZoneMapper.toDto(zone3)).thenReturn(zone3DTO);
        when(circulationZoneMapper.toDto(circulationZone)).thenReturn(circulationZoneDTO);
    }

    @Test
    void createCirculationZone_ShouldReturnCreatedZone() {
        // Given
        when(circulationZoneRepository.save(circulationZone)).thenReturn(circulationZone);

        // When
        CirculationZoneDTO result = service.createCirculationZone(circulationZone, organizationId);

        // Then
        assertEquals(circulationZoneDTO, result);
        assertEquals(organizationId, circulationZone.getOrganizationId());
        verify(circulationZoneRepository).save(circulationZone);
        verify(circulationZoneMapper).toDto(circulationZone);
    }

    @Test
    void getAllActiveCirculationZones_ShouldReturnActiveZones() {
        // Given
        when(circulationZoneRepository.findAllByOrganizationIdAndActiveIsTrue(organizationId))
                .thenReturn(activeZones);

        // When
        List<CirculationZoneDTO> result = service.getAllActiveCirculationZones(organizationId);

        // Then
        assertEquals(activeZoneDTOs.size(), result.size());
        verify(circulationZoneRepository).findAllByOrganizationIdAndActiveIsTrue(organizationId);
        verify(circulationZoneMapper, times(activeZones.size())).toDto(any(CirculationZone.class));
    }

    @Test
    void getAllCirculationZones_ShouldReturnAllZones() {
        // Given
        when(circulationZoneRepository.findAllByOrganizationId(organizationId))
                .thenReturn(allZones);

        // When
        List<CirculationZoneDTO> result = service.getAllCirculationZones(organizationId);

        // Then
        assertEquals(allZoneDTOs.size(), result.size());
        verify(circulationZoneRepository).findAllByOrganizationId(organizationId);
        verify(circulationZoneMapper, times(allZones.size())).toDto(any(CirculationZone.class));
    }

    @Test
    void getCirculationZoneById_ShouldReturnZoneById() {
        // Given
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(circulationZone));

        // When
        Optional<CirculationZoneDTO> result = service.getCirculationZoneById(zoneId, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(circulationZoneDTO, result.get());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneMapper).toDto(circulationZone);
    }

    @Test
    void getCirculationZoneById_ShouldReturnEmpty_WhenZoneNotFound() {
        // Given
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        // When
        Optional<CirculationZoneDTO> result = service.getCirculationZoneById(zoneId, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneMapper, never()).toDto(any(CirculationZone.class));
    }

    @Test
    void getCirculationZoneById_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(circulationZone));

        // When
        Optional<CirculationZoneDTO> result = service.getCirculationZoneById(zoneId, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneMapper, never()).toDto(any(CirculationZone.class));
    }

    @Test
    void getByCode_ShouldReturnZoneByCode() {
        // Given
        String code = "ZONE1";
        when(circulationZoneRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.of(circulationZone));

        // When
        Optional<CirculationZoneDTO> result = service.getByCode(code, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(circulationZoneDTO, result.get());
        verify(circulationZoneRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(circulationZoneMapper).toDto(circulationZone);
    }

    @Test
    void getByCode_ShouldReturnEmpty_WhenZoneNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(circulationZoneRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.empty());

        // When
        Optional<CirculationZoneDTO> result = service.getByCode(code, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(circulationZoneRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(circulationZoneMapper, never()).toDto(any(CirculationZone.class));
    }

    @Test
    void updateCirculationZone_ShouldReturnUpdatedZone() {
        // Given
        CirculationZone existingZone = new CirculationZone();
        existingZone.setId(zoneId);
        existingZone.setCode("ZONE1");
        existingZone.setName("Old Name");
        existingZone.setActive(true);
        existingZone.setOrganizationId(organizationId);

        CirculationZone updatedZone = new CirculationZone();
        updatedZone.setId(zoneId);
        updatedZone.setCode("ZONE1");
        updatedZone.setName("New Name");
        updatedZone.setActive(true);
        updatedZone.setOrganizationId(organizationId);

        CirculationZoneDTO updatedZoneDTO = new CirculationZoneDTO();
        updatedZoneDTO.setId(zoneId);
        updatedZoneDTO.setCode("ZONE1");
        updatedZoneDTO.setName("New Name");
        updatedZoneDTO.setActive(true);

        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(existingZone));
        when(circulationZoneRepository.save(any(CirculationZone.class)))
                .thenReturn(updatedZone);
        when(circulationZoneMapper.toDto(updatedZone))
                .thenReturn(updatedZoneDTO);

        // When
        Optional<CirculationZoneDTO> result = service.updateCirculationZone(zoneId, updatedZone, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedZoneDTO, result.get());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository).save(any(CirculationZone.class));
        verify(circulationZoneMapper).toDto(updatedZone);
    }

    @Test
    void updateCirculationZone_ShouldReturnEmpty_WhenZoneNotFound() {
        // Given
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        // When
        Optional<CirculationZoneDTO> result = service.updateCirculationZone(zoneId, circulationZone, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository, never()).save(any(CirculationZone.class));
        verify(circulationZoneMapper, never()).toDto(any(CirculationZone.class));
    }

    @Test
    void updateCirculationZone_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(circulationZone));

        // When
        Optional<CirculationZoneDTO> result = service.updateCirculationZone(zoneId, circulationZone, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository, never()).save(any(CirculationZone.class));
        verify(circulationZoneMapper, never()).toDto(any(CirculationZone.class));
    }

    @Test
    void setActive_ShouldReturnUpdatedZone() {
        // Given
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(circulationZone));

        CirculationZone updatedZone = new CirculationZone();
        updatedZone.setId(zoneId);
        updatedZone.setCode("ZONE1");
        updatedZone.setName("Zone 1");
        updatedZone.setActive(false); // Changed to inactive
        updatedZone.setOrganizationId(organizationId);

        CirculationZoneDTO updatedZoneDTO = new CirculationZoneDTO();
        updatedZoneDTO.setId(zoneId);
        updatedZoneDTO.setCode("ZONE1");
        updatedZoneDTO.setName("Zone 1");
        updatedZoneDTO.setActive(false); // Changed to inactive

        when(circulationZoneRepository.save(any(CirculationZone.class)))
                .thenReturn(updatedZone);
        when(circulationZoneMapper.toDto(updatedZone))
                .thenReturn(updatedZoneDTO);

        // When
        Optional<CirculationZoneDTO> result = service.setActive(zoneId, false, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedZoneDTO, result.get());
        assertFalse(result.get().isActive());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository).save(any(CirculationZone.class));
        verify(circulationZoneMapper).toDto(updatedZone);
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenZoneNotFound() {
        // Given
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        // When
        Optional<CirculationZoneDTO> result = service.setActive(zoneId, false, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository, never()).save(any(CirculationZone.class));
        verify(circulationZoneMapper, never()).toDto(any(CirculationZone.class));
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(circulationZone));

        // When
        Optional<CirculationZoneDTO> result = service.setActive(zoneId, false, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository, never()).save(any(CirculationZone.class));
        verify(circulationZoneMapper, never()).toDto(any(CirculationZone.class));
    }

    @Test
    void delete_ShouldReturnTrue_WhenZoneDeleted() {
        // Given
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(circulationZone));

        // When
        boolean result = service.delete(zoneId, organizationId);

        // Then
        assertTrue(result);
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository).deleteById(zoneId);
    }

    @Test
    void delete_ShouldReturnFalse_WhenZoneNotFound() {
        // Given
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        // When
        boolean result = service.delete(zoneId, organizationId);

        // Then
        assertFalse(result);
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void delete_ShouldReturnFalse_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(circulationZoneRepository.findById(zoneId))
                .thenReturn(Optional.of(circulationZone));

        // When
        boolean result = service.delete(zoneId, differentOrgId);

        // Then
        assertFalse(result);
        verify(circulationZoneRepository).findById(zoneId);
        verify(circulationZoneRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = service.getEntityName();

        // Then
        assertEquals("zone de circulation", result);
    }
}
