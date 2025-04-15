package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleModelDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleModelService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleModelReferenceControllerTest {

    @Mock
    private VehicleModelService vehicleModelService;

    @InjectMocks
    private VehicleModelReferenceController controller;

    private UUID organizationId;
    private UUID modelId;
    private UUID makeId;
    private VehicleModel vehicleModel;
    private VehicleModelDTO vehicleModelDTO;
    private List<VehicleModelDTO> activeModels;
    private List<VehicleModelDTO> allModels;
    private List<VehicleModelDTO> activeModelsByMake;
    private List<VehicleModelDTO> allModelsByMake;

    @BeforeEach
    void setUp() {
        organizationId = UUID.randomUUID();
        modelId = UUID.randomUUID();
        makeId = UUID.randomUUID();

        vehicleModel = new VehicleModel();
        vehicleModel.setId(modelId);
        vehicleModel.setCode("MODEL1");
        vehicleModel.setName("Model 1");
        vehicleModel.setActive(true);
        vehicleModel.setOrganizationId(organizationId);
        vehicleModel.setMakeId(makeId);

        vehicleModelDTO = new VehicleModelDTO();
        vehicleModelDTO.setId(modelId);
        vehicleModelDTO.setCode("MODEL1");
        vehicleModelDTO.setName("Model 1");
        vehicleModelDTO.setActive(true);
        vehicleModelDTO.setMakeId(makeId);

        VehicleModelDTO model1 = new VehicleModelDTO();
        model1.setId(UUID.randomUUID());
        model1.setCode("MODEL1");
        model1.setName("Model 1");
        model1.setActive(true);
        model1.setMakeId(makeId);

        VehicleModelDTO model2 = new VehicleModelDTO();
        model2.setId(UUID.randomUUID());
        model2.setCode("MODEL2");
        model2.setName("Model 2");
        model2.setActive(true);
        model2.setMakeId(makeId);

        VehicleModelDTO model3 = new VehicleModelDTO();
        model3.setId(UUID.randomUUID());
        model3.setCode("MODEL3");
        model3.setName("Model 3");
        model3.setActive(false);
        model3.setMakeId(makeId);

        activeModels = Arrays.asList(model1, model2);
        allModels = Arrays.asList(model1, model2, model3);
        activeModelsByMake = Arrays.asList(model1, model2);
        allModelsByMake = Arrays.asList(model1, model2, model3);
    }

    @Test
    void listActive_ShouldReturnActiveModels() {
        // Given
        when(vehicleModelService.getAllActiveVehicleModels(organizationId)).thenReturn(activeModels);

        // When
        List<VehicleModelDTO> result = controller.listActive(organizationId);

        // Then
        assertEquals(activeModels, result);
        verify(vehicleModelService).getAllActiveVehicleModels(organizationId);
    }

    @Test
    void list_ShouldReturnAllModels() {
        // Given
        when(vehicleModelService.getAllVehicleModels(organizationId)).thenReturn(allModels);

        // When
        List<VehicleModelDTO> result = controller.list(organizationId);

        // Then
        assertEquals(allModels, result);
        verify(vehicleModelService).getAllVehicleModels(organizationId);
    }

    @Test
    void get_ShouldReturnModelById() {
        // Given
        when(vehicleModelService.getVehicleModelById(modelId, organizationId))
                .thenReturn(Optional.of(vehicleModelDTO));

        // When
        VehicleModelDTO result = controller.get(modelId, organizationId);

        // Then
        assertEquals(vehicleModelDTO, result);
        verify(vehicleModelService).getVehicleModelById(modelId, organizationId);
    }

    @Test
    void get_ShouldThrowResourceNotFoundException_WhenModelNotFound() {
        // Given
        when(vehicleModelService.getVehicleModelById(modelId, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.get(modelId, organizationId));
        verify(vehicleModelService).getVehicleModelById(modelId, organizationId);
    }

    @Test
    void getByCode_ShouldReturnModelByCode() {
        // Given
        String code = "MODEL1";
        when(vehicleModelService.getByCode(code, organizationId))
                .thenReturn(Optional.of(vehicleModelDTO));

        // When
        VehicleModelDTO result = controller.getByCode(code, organizationId);

        // Then
        assertEquals(vehicleModelDTO, result);
        verify(vehicleModelService).getByCode(code, organizationId);
    }

    @Test
    void getByCode_ShouldThrowResourceNotFoundException_WhenModelNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(vehicleModelService.getByCode(code, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.getByCode(code, organizationId));
        verify(vehicleModelService).getByCode(code, organizationId);
    }

    @Test
    void create_ShouldReturnCreatedModel() {
        // Given
        when(vehicleModelService.createVehicleModel(vehicleModel, organizationId))
                .thenReturn(vehicleModelDTO);

        // When
        VehicleModelDTO result = controller.create(vehicleModel, organizationId);

        // Then
        assertEquals(vehicleModelDTO, result);
        verify(vehicleModelService).createVehicleModel(vehicleModel, organizationId);
    }

    @Test
    void update_ShouldReturnUpdatedModel() {
        // Given
        when(vehicleModelService.updateVehicleModel(modelId, vehicleModel, organizationId))
                .thenReturn(Optional.of(vehicleModelDTO));

        // When
        VehicleModelDTO result = controller.update(modelId, vehicleModel, organizationId);

        // Then
        assertEquals(vehicleModelDTO, result);
        verify(vehicleModelService).updateVehicleModel(modelId, vehicleModel, organizationId);
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenModelNotFound() {
        // Given
        when(vehicleModelService.updateVehicleModel(modelId, vehicleModel, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.update(modelId, vehicleModel, organizationId));
        verify(vehicleModelService).updateVehicleModel(modelId, vehicleModel, organizationId);
    }

    @Test
    void setActive_ShouldReturnActivatedModel() {
        // Given
        when(vehicleModelService.setActive(modelId, true, organizationId))
                .thenReturn(Optional.of(vehicleModelDTO));

        // When
        VehicleModelDTO result = controller.setActive(modelId, true, organizationId);

        // Then
        assertEquals(vehicleModelDTO, result);
        verify(vehicleModelService).setActive(modelId, true, organizationId);
    }

    @Test
    void setActive_ShouldThrowResourceNotFoundException_WhenModelNotFound() {
        // Given
        when(vehicleModelService.setActive(modelId, true, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.setActive(modelId, true, organizationId));
        verify(vehicleModelService).setActive(modelId, true, organizationId);
    }

    @Test
    void delete_ShouldDeleteModel() {
        // Given
        when(vehicleModelService.delete(modelId, organizationId))
                .thenReturn(true);

        // When
        controller.delete(modelId, organizationId);

        // Then
        verify(vehicleModelService).delete(modelId, organizationId);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenModelNotFound() {
        // Given
        when(vehicleModelService.delete(modelId, organizationId))
                .thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.delete(modelId, organizationId));
        verify(vehicleModelService).delete(modelId, organizationId);
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = controller.getEntityName();

        // Then
        assertEquals("modèle de véhicule", result);
    }

    @Test
    void listActiveEntities_ShouldReturnActiveModels() {
        // Given
        when(vehicleModelService.getAllActiveVehicleModels(organizationId)).thenReturn(activeModels);

        // When
        ResponseEntity<List<VehicleModelDTO>> response = controller.listActiveEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeModels, response.getBody());
        verify(vehicleModelService).getAllActiveVehicleModels(organizationId);
    }

    @Test
    void listAllEntities_ShouldReturnAllModels() {
        // Given
        when(vehicleModelService.getAllVehicleModels(organizationId)).thenReturn(allModels);

        // When
        ResponseEntity<List<VehicleModelDTO>> response = controller.listAllEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allModels, response.getBody());
        verify(vehicleModelService).getAllVehicleModels(organizationId);
    }

    @Test
    void getAllActiveVehicleModelsByMake_ShouldReturnActiveModelsByMake() {
        // Given
        when(vehicleModelService.getAllActiveVehicleModelsByMake(makeId, organizationId))
                .thenReturn(activeModelsByMake);

        // When
        ResponseEntity<List<VehicleModelDTO>> response = controller.getAllActiveVehicleModelsByMake(makeId, organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeModelsByMake, response.getBody());
        verify(vehicleModelService).getAllActiveVehicleModelsByMake(makeId, organizationId);
    }

    @Test
    void getAllVehicleModelsByMake_ShouldReturnAllModelsByMake() {
        // Given
        when(vehicleModelService.getAllVehicleModelsByMake(makeId, organizationId))
                .thenReturn(allModelsByMake);

        // When
        ResponseEntity<List<VehicleModelDTO>> response = controller.getAllVehicleModelsByMake(makeId, organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allModelsByMake, response.getBody());
        verify(vehicleModelService).getAllVehicleModelsByMake(makeId, organizationId);
    }

    @Test
    void getVehicleModelByCodeAndMake_ShouldReturnModelByCodeAndMake() {
        // Given
        String code = "MODEL1";
        when(vehicleModelService.getVehicleModelByCodeAndMake(code, makeId, organizationId))
                .thenReturn(Optional.of(vehicleModelDTO));

        // When
        ResponseEntity<VehicleModelDTO> response = controller.getVehicleModelByCodeAndMake(code, makeId, organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vehicleModelDTO, response.getBody());
        verify(vehicleModelService).getVehicleModelByCodeAndMake(code, makeId, organizationId);
    }

    @Test
    void getVehicleModelByCodeAndMake_ShouldReturnNotFound_WhenModelNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(vehicleModelService.getVehicleModelByCodeAndMake(code, makeId, organizationId))
                .thenReturn(Optional.empty());

        // When
        ResponseEntity<VehicleModelDTO> response = controller.getVehicleModelByCodeAndMake(code, makeId, organizationId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(vehicleModelService).getVehicleModelByCodeAndMake(code, makeId, organizationId);
    }
}
