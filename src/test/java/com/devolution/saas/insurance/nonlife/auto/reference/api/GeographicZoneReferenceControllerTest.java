package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.GeographicZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.GeographicZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeographicZoneReferenceControllerTest {

    @Mock
    private GeographicZoneService geographicZoneService;

    @InjectMocks
    private GeographicZoneReferenceController controller;

    private UUID organizationId;
    private UUID zoneId;
    private GeographicZone geographicZone;
    private GeographicZoneDTO geographicZoneDTO;
    private List<GeographicZoneDTO> activeZones;
    private List<GeographicZoneDTO> allZones;

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

        GeographicZoneDTO zone1 = new GeographicZoneDTO();
        zone1.setId(UUID.randomUUID());
        zone1.setCode("ZONE1");
        zone1.setName("Zone 1");
        zone1.setActive(true);

        GeographicZoneDTO zone2 = new GeographicZoneDTO();
        zone2.setId(UUID.randomUUID());
        zone2.setCode("ZONE2");
        zone2.setName("Zone 2");
        zone2.setActive(true);

        GeographicZoneDTO zone3 = new GeographicZoneDTO();
        zone3.setId(UUID.randomUUID());
        zone3.setCode("ZONE3");
        zone3.setName("Zone 3");
        zone3.setActive(false);

        activeZones = Arrays.asList(zone1, zone2);
        allZones = Arrays.asList(zone1, zone2, zone3);
    }

    @Test
    void listActive_ShouldReturnActiveZones() {
        // Given
        when(geographicZoneService.getAllActiveGeographicZones(organizationId)).thenReturn(activeZones);

        // When
        List<GeographicZoneDTO> result = controller.listActive(organizationId);

        // Then
        assertEquals(activeZones, result);
        verify(geographicZoneService).getAllActiveGeographicZones(organizationId);
    }

    @Test
    void list_ShouldReturnAllZones() {
        // Given
        when(geographicZoneService.getAllGeographicZones(organizationId)).thenReturn(allZones);

        // When
        List<GeographicZoneDTO> result = controller.list(organizationId);

        // Then
        assertEquals(allZones, result);
        verify(geographicZoneService).getAllGeographicZones(organizationId);
    }

    @Test
    void get_ShouldReturnZoneById() {
        // Given
        when(geographicZoneService.getGeographicZoneById(zoneId, organizationId))
                .thenReturn(Optional.of(geographicZoneDTO));

        // When
        GeographicZoneDTO result = controller.get(zoneId, organizationId);

        // Then
        assertEquals(geographicZoneDTO, result);
        verify(geographicZoneService).getGeographicZoneById(zoneId, organizationId);
    }

    @Test
    void get_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        when(geographicZoneService.getGeographicZoneById(zoneId, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.get(zoneId, organizationId));
        verify(geographicZoneService).getGeographicZoneById(zoneId, organizationId);
    }

    @Test
    void getByCode_ShouldReturnZoneByCode() {
        // Given
        String code = "ZONE1";
        when(geographicZoneService.getByCode(code, organizationId))
                .thenReturn(Optional.of(geographicZoneDTO));

        // When
        GeographicZoneDTO result = controller.getByCode(code, organizationId);

        // Then
        assertEquals(geographicZoneDTO, result);
        verify(geographicZoneService).getByCode(code, organizationId);
    }

    @Test
    void getByCode_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(geographicZoneService.getByCode(code, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.getByCode(code, organizationId));
        verify(geographicZoneService).getByCode(code, organizationId);
    }

    @Test
    void create_ShouldReturnCreatedZone() {
        // Given
        when(geographicZoneService.createGeographicZone(geographicZone, organizationId))
                .thenReturn(geographicZoneDTO);

        // When
        GeographicZoneDTO result = controller.create(geographicZone, organizationId);

        // Then
        assertEquals(geographicZoneDTO, result);
        verify(geographicZoneService).createGeographicZone(geographicZone, organizationId);
    }

    @Test
    void update_ShouldReturnUpdatedZone() {
        // Given
        when(geographicZoneService.updateGeographicZone(zoneId, geographicZone, organizationId))
                .thenReturn(Optional.of(geographicZoneDTO));

        // When
        GeographicZoneDTO result = controller.update(zoneId, geographicZone, organizationId);

        // Then
        assertEquals(geographicZoneDTO, result);
        verify(geographicZoneService).updateGeographicZone(zoneId, geographicZone, organizationId);
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        when(geographicZoneService.updateGeographicZone(zoneId, geographicZone, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.update(zoneId, geographicZone, organizationId));
        verify(geographicZoneService).updateGeographicZone(zoneId, geographicZone, organizationId);
    }

    @Test
    void setActive_ShouldReturnActivatedZone() {
        // Given
        when(geographicZoneService.setActive(zoneId, true, organizationId))
                .thenReturn(Optional.of(geographicZoneDTO));

        // When
        GeographicZoneDTO result = controller.setActive(zoneId, true, organizationId);

        // Then
        assertEquals(geographicZoneDTO, result);
        verify(geographicZoneService).setActive(zoneId, true, organizationId);
    }

    @Test
    void setActive_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        when(geographicZoneService.setActive(zoneId, true, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.setActive(zoneId, true, organizationId));
        verify(geographicZoneService).setActive(zoneId, true, organizationId);
    }

    @Test
    void delete_ShouldDeleteZone() {
        // Given
        when(geographicZoneService.delete(zoneId, organizationId))
                .thenReturn(true);

        // When
        controller.delete(zoneId, organizationId);

        // Then
        verify(geographicZoneService).delete(zoneId, organizationId);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        when(geographicZoneService.delete(zoneId, organizationId))
                .thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.delete(zoneId, organizationId));
        verify(geographicZoneService).delete(zoneId, organizationId);
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = controller.getEntityName();

        // Then
        assertEquals("zone géographique", result);
    }

    @Test
    void listActiveEntities_ShouldReturnActiveZones() {
        // Given
        when(geographicZoneService.getAllActiveGeographicZones(organizationId)).thenReturn(activeZones);

        // When
        ResponseEntity<List<GeographicZoneDTO>> response = controller.listActiveEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeZones, response.getBody());
        verify(geographicZoneService).getAllActiveGeographicZones(organizationId);
    }

    @Test
    void listAllEntities_ShouldReturnAllZones() {
        // Given
        when(geographicZoneService.getAllGeographicZones(organizationId)).thenReturn(allZones);

        // When
        ResponseEntity<List<GeographicZoneDTO>> response = controller.listAllEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allZones, response.getBody());
        verify(geographicZoneService).getAllGeographicZones(organizationId);
    }
}
