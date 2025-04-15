package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleManufacturerDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleManufacturerService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
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
class VehicleManufacturerReferenceControllerTest {

    @Mock
    private VehicleManufacturerService vehicleManufacturerService;

    @InjectMocks
    private VehicleManufacturerReferenceController controller;

    private UUID organizationId;
    private UUID manufacturerId;
    private VehicleManufacturer vehicleManufacturer;
    private VehicleManufacturerDTO vehicleManufacturerDTO;
    private List<VehicleManufacturerDTO> activeManufacturers;
    private List<VehicleManufacturerDTO> allManufacturers;

    @BeforeEach
    void setUp() {
        organizationId = UUID.randomUUID();
        manufacturerId = UUID.randomUUID();

        vehicleManufacturer = new VehicleManufacturer();
        vehicleManufacturer.setId(manufacturerId);
        vehicleManufacturer.setCode("TOYOTA");
        vehicleManufacturer.setName("Toyota");
        vehicleManufacturer.setActive(true);
        vehicleManufacturer.setOrganizationId(organizationId);

        vehicleManufacturerDTO = new VehicleManufacturerDTO();
        vehicleManufacturerDTO.setId(manufacturerId);
        vehicleManufacturerDTO.setCode("TOYOTA");
        vehicleManufacturerDTO.setName("Toyota");
        vehicleManufacturerDTO.setActive(true);

        VehicleManufacturerDTO manufacturer1 = new VehicleManufacturerDTO();
        manufacturer1.setId(UUID.randomUUID());
        manufacturer1.setCode("TOYOTA");
        manufacturer1.setName("Toyota");
        manufacturer1.setActive(true);

        VehicleManufacturerDTO manufacturer2 = new VehicleManufacturerDTO();
        manufacturer2.setId(UUID.randomUUID());
        manufacturer2.setCode("HONDA");
        manufacturer2.setName("Honda");
        manufacturer2.setActive(true);

        VehicleManufacturerDTO manufacturer3 = new VehicleManufacturerDTO();
        manufacturer3.setId(UUID.randomUUID());
        manufacturer3.setCode("BMW");
        manufacturer3.setName("BMW");
        manufacturer3.setActive(false);

        activeManufacturers = Arrays.asList(manufacturer1, manufacturer2);
        allManufacturers = Arrays.asList(manufacturer1, manufacturer2, manufacturer3);
    }

    @Test
    void listActive_ShouldReturnActiveManufacturers() {
        // Given
        when(vehicleManufacturerService.getAllActiveVehicleManufacturers(organizationId)).thenReturn(activeManufacturers);

        // When
        List<VehicleManufacturerDTO> result = controller.listActive(organizationId);

        // Then
        assertEquals(activeManufacturers, result);
        verify(vehicleManufacturerService).getAllActiveVehicleManufacturers(organizationId);
    }

    @Test
    void list_ShouldReturnAllManufacturers() {
        // Given
        when(vehicleManufacturerService.getAllVehicleManufacturers(organizationId)).thenReturn(allManufacturers);

        // When
        List<VehicleManufacturerDTO> result = controller.list(organizationId);

        // Then
        assertEquals(allManufacturers, result);
        verify(vehicleManufacturerService).getAllVehicleManufacturers(organizationId);
    }

    @Test
    void get_ShouldReturnManufacturerById() {
        // Given
        when(vehicleManufacturerService.getVehicleManufacturerById(manufacturerId, organizationId))
                .thenReturn(Optional.of(vehicleManufacturerDTO));

        // When
        VehicleManufacturerDTO result = controller.get(manufacturerId, organizationId);

        // Then
        assertEquals(vehicleManufacturerDTO, result);
        verify(vehicleManufacturerService).getVehicleManufacturerById(manufacturerId, organizationId);
    }

    @Test
    void get_ShouldThrowResourceNotFoundException_WhenManufacturerNotFound() {
        // Given
        when(vehicleManufacturerService.getVehicleManufacturerById(manufacturerId, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.get(manufacturerId, organizationId));
        verify(vehicleManufacturerService).getVehicleManufacturerById(manufacturerId, organizationId);
    }

    @Test
    void getByCode_ShouldReturnManufacturerByCode() {
        // Given
        String code = "TOYOTA";
        when(vehicleManufacturerService.getVehicleManufacturerByCode(code, organizationId))
                .thenReturn(Optional.of(vehicleManufacturerDTO));

        // When
        VehicleManufacturerDTO result = controller.getByCode(code, organizationId);

        // Then
        assertEquals(vehicleManufacturerDTO, result);
        verify(vehicleManufacturerService).getVehicleManufacturerByCode(code, organizationId);
    }

    @Test
    void getByCode_ShouldThrowResourceNotFoundException_WhenManufacturerNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(vehicleManufacturerService.getVehicleManufacturerByCode(code, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.getByCode(code, organizationId));
        verify(vehicleManufacturerService).getVehicleManufacturerByCode(code, organizationId);
    }

    @Test
    void create_ShouldReturnCreatedManufacturer() {
        // Given
        when(vehicleManufacturerService.createVehicleManufacturer(vehicleManufacturer, organizationId))
                .thenReturn(vehicleManufacturerDTO);

        // When
        VehicleManufacturerDTO result = controller.create(vehicleManufacturer, organizationId);

        // Then
        assertEquals(vehicleManufacturerDTO, result);
        verify(vehicleManufacturerService).createVehicleManufacturer(vehicleManufacturer, organizationId);
    }

    @Test
    void update_ShouldReturnUpdatedManufacturer() {
        // Given
        when(vehicleManufacturerService.updateVehicleManufacturer(manufacturerId, vehicleManufacturer, organizationId))
                .thenReturn(Optional.of(vehicleManufacturerDTO));

        // When
        VehicleManufacturerDTO result = controller.update(manufacturerId, vehicleManufacturer, organizationId);

        // Then
        assertEquals(vehicleManufacturerDTO, result);
        verify(vehicleManufacturerService).updateVehicleManufacturer(manufacturerId, vehicleManufacturer, organizationId);
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenManufacturerNotFound() {
        // Given
        when(vehicleManufacturerService.updateVehicleManufacturer(manufacturerId, vehicleManufacturer, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.update(manufacturerId, vehicleManufacturer, organizationId));
        verify(vehicleManufacturerService).updateVehicleManufacturer(manufacturerId, vehicleManufacturer, organizationId);
    }

    @Test
    void setActive_ShouldReturnActivatedManufacturer() {
        // Given
        when(vehicleManufacturerService.setActive(manufacturerId, true, organizationId))
                .thenReturn(Optional.of(vehicleManufacturerDTO));

        // When
        VehicleManufacturerDTO result = controller.setActive(manufacturerId, true, organizationId);

        // Then
        assertEquals(vehicleManufacturerDTO, result);
        verify(vehicleManufacturerService).setActive(manufacturerId, true, organizationId);
    }

    @Test
    void setActive_ShouldThrowResourceNotFoundException_WhenManufacturerNotFound() {
        // Given
        when(vehicleManufacturerService.setActive(manufacturerId, true, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.setActive(manufacturerId, true, organizationId));
        verify(vehicleManufacturerService).setActive(manufacturerId, true, organizationId);
    }

    @Test
    void delete_ShouldDeleteManufacturer() {
        // Given
        when(vehicleManufacturerService.delete(manufacturerId, organizationId))
                .thenReturn(true);

        // When
        controller.delete(manufacturerId, organizationId);

        // Then
        verify(vehicleManufacturerService).delete(manufacturerId, organizationId);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenManufacturerNotFound() {
        // Given
        when(vehicleManufacturerService.delete(manufacturerId, organizationId))
                .thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.delete(manufacturerId, organizationId));
        verify(vehicleManufacturerService).delete(manufacturerId, organizationId);
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = controller.getEntityName();

        // Then
        assertEquals("fabricant de véhicule", result);
    }

    @Test
    void listActiveEntities_ShouldReturnActiveManufacturers() {
        // Given
        when(vehicleManufacturerService.getAllActiveVehicleManufacturers(organizationId)).thenReturn(activeManufacturers);

        // When
        ResponseEntity<List<VehicleManufacturerDTO>> response = controller.listActiveEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeManufacturers, response.getBody());
        verify(vehicleManufacturerService).getAllActiveVehicleManufacturers(organizationId);
    }

    @Test
    void listAllEntities_ShouldReturnAllManufacturers() {
        // Given
        when(vehicleManufacturerService.getAllVehicleManufacturers(organizationId)).thenReturn(allManufacturers);

        // When
        ResponseEntity<List<VehicleManufacturerDTO>> response = controller.listAllEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allManufacturers, response.getBody());
        verify(vehicleManufacturerService).getAllVehicleManufacturers(organizationId);
    }
}
