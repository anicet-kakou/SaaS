package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.CirculationZoneService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
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
class CirculationZoneReferenceControllerTest {

    @Mock
    private CirculationZoneService circulationZoneService;

    @InjectMocks
    private CirculationZoneReferenceController controller;

    private UUID organizationId;
    private UUID zoneId;
    private CirculationZone circulationZone;
    private CirculationZoneDTO circulationZoneDTO;
    private List<CirculationZoneDTO> activeZones;
    private List<CirculationZoneDTO> allZones;

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

        CirculationZoneDTO zone1 = new CirculationZoneDTO();
        zone1.setId(UUID.randomUUID());
        zone1.setCode("ZONE1");
        zone1.setName("Zone 1");
        zone1.setActive(true);

        CirculationZoneDTO zone2 = new CirculationZoneDTO();
        zone2.setId(UUID.randomUUID());
        zone2.setCode("ZONE2");
        zone2.setName("Zone 2");
        zone2.setActive(true);

        CirculationZoneDTO zone3 = new CirculationZoneDTO();
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
        when(circulationZoneService.getAllActiveCirculationZones(organizationId)).thenReturn(activeZones);

        // When
        List<CirculationZoneDTO> result = controller.listActive(organizationId);

        // Then
        assertEquals(activeZones, result);
        verify(circulationZoneService).getAllActiveCirculationZones(organizationId);
    }

    @Test
    void list_ShouldReturnAllZones() {
        // Given
        when(circulationZoneService.getAllCirculationZones(organizationId)).thenReturn(allZones);

        // When
        List<CirculationZoneDTO> result = controller.list(organizationId);

        // Then
        assertEquals(allZones, result);
        verify(circulationZoneService).getAllCirculationZones(organizationId);
    }

    @Test
    void get_ShouldReturnZoneById() {
        // Given
        when(circulationZoneService.getCirculationZoneById(zoneId, organizationId))
                .thenReturn(Optional.of(circulationZoneDTO));

        // When
        CirculationZoneDTO result = controller.get(zoneId, organizationId);

        // Then
        assertEquals(circulationZoneDTO, result);
        verify(circulationZoneService).getCirculationZoneById(zoneId, organizationId);
    }

    @Test
    void get_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        when(circulationZoneService.getCirculationZoneById(zoneId, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.get(zoneId, organizationId));
        verify(circulationZoneService).getCirculationZoneById(zoneId, organizationId);
    }

    @Test
    void getByCode_ShouldReturnZoneByCode() {
        // Given
        String code = "ZONE1";
        when(circulationZoneService.getByCode(code, organizationId))
                .thenReturn(Optional.of(circulationZoneDTO));

        // When
        CirculationZoneDTO result = controller.getByCode(code, organizationId);

        // Then
        assertEquals(circulationZoneDTO, result);
        verify(circulationZoneService).getByCode(code, organizationId);
    }

    @Test
    void getByCode_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(circulationZoneService.getByCode(code, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.getByCode(code, organizationId));
        verify(circulationZoneService).getByCode(code, organizationId);
    }

    @Test
    void create_ShouldReturnCreatedZone() {
        // Given
        when(circulationZoneService.createCirculationZone(circulationZone, organizationId))
                .thenReturn(circulationZoneDTO);

        // When
        CirculationZoneDTO result = controller.create(circulationZone, organizationId);

        // Then
        assertEquals(circulationZoneDTO, result);
        verify(circulationZoneService).createCirculationZone(circulationZone, organizationId);
    }

    @Test
    void update_ShouldReturnUpdatedZone() {
        // Given
        when(circulationZoneService.updateCirculationZone(zoneId, circulationZone, organizationId))
                .thenReturn(Optional.of(circulationZoneDTO));

        // When
        CirculationZoneDTO result = controller.update(zoneId, circulationZone, organizationId);

        // Then
        assertEquals(circulationZoneDTO, result);
        verify(circulationZoneService).updateCirculationZone(zoneId, circulationZone, organizationId);
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        when(circulationZoneService.updateCirculationZone(zoneId, circulationZone, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.update(zoneId, circulationZone, organizationId));
        verify(circulationZoneService).updateCirculationZone(zoneId, circulationZone, organizationId);
    }

    @Test
    void setActive_ShouldReturnActivatedZone() {
        // Given
        when(circulationZoneService.setActive(zoneId, true, organizationId))
                .thenReturn(Optional.of(circulationZoneDTO));

        // When
        CirculationZoneDTO result = controller.setActive(zoneId, true, organizationId);

        // Then
        assertEquals(circulationZoneDTO, result);
        verify(circulationZoneService).setActive(zoneId, true, organizationId);
    }

    @Test
    void setActive_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        when(circulationZoneService.setActive(zoneId, true, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.setActive(zoneId, true, organizationId));
        verify(circulationZoneService).setActive(zoneId, true, organizationId);
    }

    @Test
    void delete_ShouldDeleteZone() {
        // Given
        when(circulationZoneService.delete(zoneId, organizationId))
                .thenReturn(true);

        // When
        controller.delete(zoneId, organizationId);

        // Then
        verify(circulationZoneService).delete(zoneId, organizationId);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenZoneNotFound() {
        // Given
        when(circulationZoneService.delete(zoneId, organizationId))
                .thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.delete(zoneId, organizationId));
        verify(circulationZoneService).delete(zoneId, organizationId);
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = controller.getEntityName();

        // Then
        assertEquals("zone de circulation", result);
    }

    @Test
    void listActiveEntities_ShouldReturnActiveZones() {
        // Given
        when(circulationZoneService.getAllActiveCirculationZones(organizationId)).thenReturn(activeZones);

        // When
        ResponseEntity<List<CirculationZoneDTO>> response = controller.listActiveEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeZones, response.getBody());
        verify(circulationZoneService).getAllActiveCirculationZones(organizationId);
    }

    @Test
    void listAllEntities_ShouldReturnAllZones() {
        // Given
        when(circulationZoneService.getAllCirculationZones(organizationId)).thenReturn(allZones);

        // When
        ResponseEntity<List<CirculationZoneDTO>> response = controller.listAllEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allZones, response.getBody());
        verify(circulationZoneService).getAllCirculationZones(organizationId);
    }
}
