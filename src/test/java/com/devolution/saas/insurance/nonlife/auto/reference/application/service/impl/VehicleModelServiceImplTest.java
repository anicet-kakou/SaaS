package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleModelDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleModelMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleModelRepository;
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
class VehicleModelServiceImplTest {

    @Mock
    private VehicleModelRepository vehicleModelRepository;

    @Mock
    private VehicleModelMapper vehicleModelMapper;

    @InjectMocks
    private VehicleModelServiceImpl service;

    private UUID organizationId;
    private UUID modelId;
    private UUID makeId;
    private VehicleModel vehicleModel;
    private VehicleModelDTO vehicleModelDTO;
    private List<VehicleModel> activeModels;
    private List<VehicleModel> allModels;
    private List<VehicleModel> activeModelsByMake;
    private List<VehicleModel> allModelsByMake;
    private List<VehicleModelDTO> activeModelDTOs;
    private List<VehicleModelDTO> allModelDTOs;
    private List<VehicleModelDTO> activeModelDTOsByMake;
    private List<VehicleModelDTO> allModelDTOsByMake;

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

        VehicleModel model1 = new VehicleModel();
        model1.setId(UUID.randomUUID());
        model1.setCode("MODEL1");
        model1.setName("Model 1");
        model1.setActive(true);
        model1.setOrganizationId(organizationId);
        model1.setMakeId(makeId);

        VehicleModel model2 = new VehicleModel();
        model2.setId(UUID.randomUUID());
        model2.setCode("MODEL2");
        model2.setName("Model 2");
        model2.setActive(true);
        model2.setOrganizationId(organizationId);
        model2.setMakeId(makeId);

        VehicleModel model3 = new VehicleModel();
        model3.setId(UUID.randomUUID());
        model3.setCode("MODEL3");
        model3.setName("Model 3");
        model3.setActive(false);
        model3.setOrganizationId(organizationId);
        model3.setMakeId(makeId);

        activeModels = Arrays.asList(model1, model2);
        allModels = Arrays.asList(model1, model2, model3);
        activeModelsByMake = Arrays.asList(model1, model2);
        allModelsByMake = Arrays.asList(model1, model2, model3);

        VehicleModelDTO model1DTO = new VehicleModelDTO();
        model1DTO.setId(model1.getId());
        model1DTO.setCode("MODEL1");
        model1DTO.setName("Model 1");
        model1DTO.setActive(true);
        model1DTO.setMakeId(makeId);

        VehicleModelDTO model2DTO = new VehicleModelDTO();
        model2DTO.setId(model2.getId());
        model2DTO.setCode("MODEL2");
        model2DTO.setName("Model 2");
        model2DTO.setActive(true);
        model2DTO.setMakeId(makeId);

        VehicleModelDTO model3DTO = new VehicleModelDTO();
        model3DTO.setId(model3.getId());
        model3DTO.setCode("MODEL3");
        model3DTO.setName("Model 3");
        model3DTO.setActive(false);
        model3DTO.setMakeId(makeId);

        activeModelDTOs = Arrays.asList(model1DTO, model2DTO);
        allModelDTOs = Arrays.asList(model1DTO, model2DTO, model3DTO);
        activeModelDTOsByMake = Arrays.asList(model1DTO, model2DTO);
        allModelDTOsByMake = Arrays.asList(model1DTO, model2DTO, model3DTO);

        // Setup mapper
        when(vehicleModelMapper.toDto(model1)).thenReturn(model1DTO);
        when(vehicleModelMapper.toDto(model2)).thenReturn(model2DTO);
        when(vehicleModelMapper.toDto(model3)).thenReturn(model3DTO);
        when(vehicleModelMapper.toDto(vehicleModel)).thenReturn(vehicleModelDTO);
    }

    @Test
    void createVehicleModel_ShouldReturnCreatedModel() {
        // Given
        when(vehicleModelRepository.save(vehicleModel)).thenReturn(vehicleModel);

        // When
        VehicleModelDTO result = service.createVehicleModel(vehicleModel, organizationId);

        // Then
        assertEquals(vehicleModelDTO, result);
        assertEquals(organizationId, vehicleModel.getOrganizationId());
        verify(vehicleModelRepository).save(vehicleModel);
        verify(vehicleModelMapper).toDto(vehicleModel);
    }

    @Test
    void getAllActiveVehicleModels_ShouldReturnActiveModels() {
        // Given
        when(vehicleModelRepository.findAllByOrganizationIdAndActiveIsTrue(organizationId))
                .thenReturn(activeModels);

        // When
        List<VehicleModelDTO> result = service.getAllActiveVehicleModels(organizationId);

        // Then
        assertEquals(activeModelDTOs.size(), result.size());
        verify(vehicleModelRepository).findAllByOrganizationIdAndActiveIsTrue(organizationId);
        verify(vehicleModelMapper, times(activeModels.size())).toDto(any(VehicleModel.class));
    }

    @Test
    void getAllVehicleModels_ShouldReturnAllModels() {
        // Given
        when(vehicleModelRepository.findAllByOrganizationId(organizationId))
                .thenReturn(allModels);

        // When
        List<VehicleModelDTO> result = service.getAllVehicleModels(organizationId);

        // Then
        assertEquals(allModelDTOs.size(), result.size());
        verify(vehicleModelRepository).findAllByOrganizationId(organizationId);
        verify(vehicleModelMapper, times(allModels.size())).toDto(any(VehicleModel.class));
    }

    @Test
    void getAllActiveVehicleModelsByMake_ShouldReturnActiveModelsByMake() {
        // Given
        when(vehicleModelRepository.findAllByMakeIdAndOrganizationIdAndActiveIsTrue(makeId, organizationId))
                .thenReturn(activeModelsByMake);

        // When
        List<VehicleModelDTO> result = service.getAllActiveVehicleModelsByMake(makeId, organizationId);

        // Then
        assertEquals(activeModelDTOsByMake.size(), result.size());
        verify(vehicleModelRepository).findAllByMakeIdAndOrganizationIdAndActiveIsTrue(makeId, organizationId);
        verify(vehicleModelMapper, times(activeModelsByMake.size())).toDto(any(VehicleModel.class));
    }

    @Test
    void getAllVehicleModelsByMake_ShouldReturnAllModelsByMake() {
        // Given
        when(vehicleModelRepository.findAllByMakeIdAndOrganizationId(makeId, organizationId))
                .thenReturn(allModelsByMake);

        // When
        List<VehicleModelDTO> result = service.getAllVehicleModelsByMake(makeId, organizationId);

        // Then
        assertEquals(allModelDTOsByMake.size(), result.size());
        verify(vehicleModelRepository).findAllByMakeIdAndOrganizationId(makeId, organizationId);
        verify(vehicleModelMapper, times(allModelsByMake.size())).toDto(any(VehicleModel.class));
    }

    @Test
    void getVehicleModelById_ShouldReturnModelById() {
        // Given
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.of(vehicleModel));

        // When
        Optional<VehicleModelDTO> result = service.getVehicleModelById(modelId, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleModelDTO, result.get());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelMapper).toDto(vehicleModel);
    }

    @Test
    void getVehicleModelById_ShouldReturnEmpty_WhenModelNotFound() {
        // Given
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleModelDTO> result = service.getVehicleModelById(modelId, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelMapper, never()).toDto(any(VehicleModel.class));
    }

    @Test
    void getVehicleModelById_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.of(vehicleModel));

        // When
        Optional<VehicleModelDTO> result = service.getVehicleModelById(modelId, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelMapper, never()).toDto(any(VehicleModel.class));
    }

    @Test
    void getVehicleModelByCodeAndMake_ShouldReturnModelByCodeAndMake() {
        // Given
        String code = "MODEL1";
        when(vehicleModelRepository.findByCodeAndMakeIdAndOrganizationId(code, makeId, organizationId))
                .thenReturn(Optional.of(vehicleModel));

        // When
        Optional<VehicleModelDTO> result = service.getVehicleModelByCodeAndMake(code, makeId, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleModelDTO, result.get());
        verify(vehicleModelRepository).findByCodeAndMakeIdAndOrganizationId(code, makeId, organizationId);
        verify(vehicleModelMapper).toDto(vehicleModel);
    }

    @Test
    void getVehicleModelByCodeAndMake_ShouldReturnEmpty_WhenModelNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(vehicleModelRepository.findByCodeAndMakeIdAndOrganizationId(code, makeId, organizationId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleModelDTO> result = service.getVehicleModelByCodeAndMake(code, makeId, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleModelRepository).findByCodeAndMakeIdAndOrganizationId(code, makeId, organizationId);
        verify(vehicleModelMapper, never()).toDto(any(VehicleModel.class));
    }

    @Test
    void getByCode_ShouldReturnModelByCode() {
        // Given
        String code = "MODEL1";
        when(vehicleModelRepository.findAllByOrganizationId(organizationId))
                .thenReturn(Arrays.asList(vehicleModel));

        // When
        Optional<VehicleModelDTO> result = service.getByCode(code, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleModelDTO, result.get());
        verify(vehicleModelRepository).findAllByOrganizationId(organizationId);
        verify(vehicleModelMapper).toDto(vehicleModel);
    }

    @Test
    void getByCode_ShouldReturnEmpty_WhenModelNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(vehicleModelRepository.findAllByOrganizationId(organizationId))
                .thenReturn(Arrays.asList(vehicleModel));

        // When
        Optional<VehicleModelDTO> result = service.getByCode(code + "NONEXISTENT", organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleModelRepository).findAllByOrganizationId(organizationId);
        verify(vehicleModelMapper, never()).toDto(any(VehicleModel.class));
    }

    @Test
    void updateVehicleModel_ShouldReturnUpdatedModel() {
        // Given
        VehicleModel existingModel = new VehicleModel();
        existingModel.setId(modelId);
        existingModel.setCode("MODEL1");
        existingModel.setName("Old Name");
        existingModel.setActive(true);
        existingModel.setOrganizationId(organizationId);
        existingModel.setMakeId(makeId);

        VehicleModel updatedModel = new VehicleModel();
        updatedModel.setId(modelId);
        updatedModel.setCode("MODEL1");
        updatedModel.setName("New Name");
        updatedModel.setActive(true);
        updatedModel.setOrganizationId(organizationId);
        updatedModel.setMakeId(makeId);

        VehicleModelDTO updatedModelDTO = new VehicleModelDTO();
        updatedModelDTO.setId(modelId);
        updatedModelDTO.setCode("MODEL1");
        updatedModelDTO.setName("New Name");
        updatedModelDTO.setActive(true);
        updatedModelDTO.setMakeId(makeId);

        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.of(existingModel));
        when(vehicleModelRepository.save(any(VehicleModel.class)))
                .thenReturn(updatedModel);
        when(vehicleModelMapper.toDto(updatedModel))
                .thenReturn(updatedModelDTO);

        // When
        Optional<VehicleModelDTO> result = service.updateVehicleModel(modelId, updatedModel, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedModelDTO, result.get());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository).save(any(VehicleModel.class));
        verify(vehicleModelMapper).toDto(updatedModel);
    }

    @Test
    void updateVehicleModel_ShouldReturnEmpty_WhenModelNotFound() {
        // Given
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleModelDTO> result = service.updateVehicleModel(modelId, vehicleModel, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository, never()).save(any(VehicleModel.class));
        verify(vehicleModelMapper, never()).toDto(any(VehicleModel.class));
    }

    @Test
    void updateVehicleModel_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.of(vehicleModel));

        // When
        Optional<VehicleModelDTO> result = service.updateVehicleModel(modelId, vehicleModel, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository, never()).save(any(VehicleModel.class));
        verify(vehicleModelMapper, never()).toDto(any(VehicleModel.class));
    }

    @Test
    void setActive_ShouldReturnUpdatedModel() {
        // Given
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.of(vehicleModel));

        VehicleModel updatedModel = new VehicleModel();
        updatedModel.setId(modelId);
        updatedModel.setCode("MODEL1");
        updatedModel.setName("Model 1");
        updatedModel.setActive(false); // Changed to inactive
        updatedModel.setOrganizationId(organizationId);
        updatedModel.setMakeId(makeId);

        VehicleModelDTO updatedModelDTO = new VehicleModelDTO();
        updatedModelDTO.setId(modelId);
        updatedModelDTO.setCode("MODEL1");
        updatedModelDTO.setName("Model 1");
        updatedModelDTO.setActive(false); // Changed to inactive
        updatedModelDTO.setMakeId(makeId);

        when(vehicleModelRepository.save(any(VehicleModel.class)))
                .thenReturn(updatedModel);
        when(vehicleModelMapper.toDto(updatedModel))
                .thenReturn(updatedModelDTO);

        // When
        Optional<VehicleModelDTO> result = service.setActive(modelId, false, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedModelDTO, result.get());
        assertFalse(result.get().isActive());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository).save(any(VehicleModel.class));
        verify(vehicleModelMapper).toDto(updatedModel);
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenModelNotFound() {
        // Given
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleModelDTO> result = service.setActive(modelId, false, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository, never()).save(any(VehicleModel.class));
        verify(vehicleModelMapper, never()).toDto(any(VehicleModel.class));
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.of(vehicleModel));

        // When
        Optional<VehicleModelDTO> result = service.setActive(modelId, false, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository, never()).save(any(VehicleModel.class));
        verify(vehicleModelMapper, never()).toDto(any(VehicleModel.class));
    }

    @Test
    void delete_ShouldReturnTrue_WhenModelDeleted() {
        // Given
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.of(vehicleModel));

        // When
        boolean result = service.delete(modelId, organizationId);

        // Then
        assertTrue(result);
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository).deleteById(modelId);
    }

    @Test
    void delete_ShouldReturnFalse_WhenModelNotFound() {
        // Given
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.empty());

        // When
        boolean result = service.delete(modelId, organizationId);

        // Then
        assertFalse(result);
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void delete_ShouldReturnFalse_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleModelRepository.findById(modelId))
                .thenReturn(Optional.of(vehicleModel));

        // When
        boolean result = service.delete(modelId, differentOrgId);

        // Then
        assertFalse(result);
        verify(vehicleModelRepository).findById(modelId);
        verify(vehicleModelRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = service.getEntityName();

        // Then
        assertEquals("modèle de véhicule", result);
    }
}
