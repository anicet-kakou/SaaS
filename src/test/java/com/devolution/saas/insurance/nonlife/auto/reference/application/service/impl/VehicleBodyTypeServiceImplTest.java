package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleBodyTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleBodyTypeMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleBodyTypeRepository;
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
class VehicleBodyTypeServiceImplTest {

    @Mock
    private VehicleBodyTypeRepository vehicleBodyTypeRepository;

    @Mock
    private VehicleBodyTypeMapper vehicleBodyTypeMapper;

    @InjectMocks
    private VehicleBodyTypeServiceImpl service;

    private UUID organizationId;
    private UUID bodyTypeId;
    private VehicleBodyType vehicleBodyType;
    private VehicleBodyTypeDTO vehicleBodyTypeDTO;
    private List<VehicleBodyType> activeBodyTypes;
    private List<VehicleBodyType> allBodyTypes;
    private List<VehicleBodyTypeDTO> activeBodyTypeDTOs;
    private List<VehicleBodyTypeDTO> allBodyTypeDTOs;

    @BeforeEach
    void setUp() {
        organizationId = UUID.randomUUID();
        bodyTypeId = UUID.randomUUID();

        vehicleBodyType = new VehicleBodyType();
        vehicleBodyType.setId(bodyTypeId);
        vehicleBodyType.setCode("SEDAN");
        vehicleBodyType.setName("Sedan");
        vehicleBodyType.setActive(true);
        vehicleBodyType.setOrganizationId(organizationId);

        vehicleBodyTypeDTO = new VehicleBodyTypeDTO();
        vehicleBodyTypeDTO.setId(bodyTypeId);
        vehicleBodyTypeDTO.setCode("SEDAN");
        vehicleBodyTypeDTO.setName("Sedan");
        vehicleBodyTypeDTO.setActive(true);

        VehicleBodyType type1 = new VehicleBodyType();
        type1.setId(UUID.randomUUID());
        type1.setCode("SEDAN");
        type1.setName("Sedan");
        type1.setActive(true);
        type1.setOrganizationId(organizationId);

        VehicleBodyType type2 = new VehicleBodyType();
        type2.setId(UUID.randomUUID());
        type2.setCode("SUV");
        type2.setName("SUV");
        type2.setActive(true);
        type2.setOrganizationId(organizationId);

        VehicleBodyType type3 = new VehicleBodyType();
        type3.setId(UUID.randomUUID());
        type3.setCode("COUPE");
        type3.setName("Coupe");
        type3.setActive(false);
        type3.setOrganizationId(organizationId);

        activeBodyTypes = Arrays.asList(type1, type2);
        allBodyTypes = Arrays.asList(type1, type2, type3);

        VehicleBodyTypeDTO type1DTO = new VehicleBodyTypeDTO();
        type1DTO.setId(type1.getId());
        type1DTO.setCode("SEDAN");
        type1DTO.setName("Sedan");
        type1DTO.setActive(true);

        VehicleBodyTypeDTO type2DTO = new VehicleBodyTypeDTO();
        type2DTO.setId(type2.getId());
        type2DTO.setCode("SUV");
        type2DTO.setName("SUV");
        type2DTO.setActive(true);

        VehicleBodyTypeDTO type3DTO = new VehicleBodyTypeDTO();
        type3DTO.setId(type3.getId());
        type3DTO.setCode("COUPE");
        type3DTO.setName("Coupe");
        type3DTO.setActive(false);

        activeBodyTypeDTOs = Arrays.asList(type1DTO, type2DTO);
        allBodyTypeDTOs = Arrays.asList(type1DTO, type2DTO, type3DTO);

        // Setup mapper
        when(vehicleBodyTypeMapper.toDto(type1)).thenReturn(type1DTO);
        when(vehicleBodyTypeMapper.toDto(type2)).thenReturn(type2DTO);
        when(vehicleBodyTypeMapper.toDto(type3)).thenReturn(type3DTO);
        when(vehicleBodyTypeMapper.toDto(vehicleBodyType)).thenReturn(vehicleBodyTypeDTO);
    }

    @Test
    void createVehicleBodyType_ShouldReturnCreatedBodyType() {
        // Given
        when(vehicleBodyTypeRepository.save(vehicleBodyType)).thenReturn(vehicleBodyType);

        // When
        VehicleBodyTypeDTO result = service.createVehicleBodyType(vehicleBodyType, organizationId);

        // Then
        assertEquals(vehicleBodyTypeDTO, result);
        assertEquals(organizationId, vehicleBodyType.getOrganizationId());
        verify(vehicleBodyTypeRepository).save(vehicleBodyType);
        verify(vehicleBodyTypeMapper).toDto(vehicleBodyType);
    }

    @Test
    void getAllActiveVehicleBodyTypes_ShouldReturnActiveBodyTypes() {
        // Given
        when(vehicleBodyTypeRepository.findAllByOrganizationIdAndActiveIsTrue(organizationId))
                .thenReturn(activeBodyTypes);

        // When
        List<VehicleBodyTypeDTO> result = service.getAllActiveVehicleBodyTypes(organizationId);

        // Then
        assertEquals(activeBodyTypeDTOs.size(), result.size());
        verify(vehicleBodyTypeRepository).findAllByOrganizationIdAndActiveIsTrue(organizationId);
        verify(vehicleBodyTypeMapper, times(activeBodyTypes.size())).toDto(any(VehicleBodyType.class));
    }

    @Test
    void getAllVehicleBodyTypes_ShouldReturnAllBodyTypes() {
        // Given
        when(vehicleBodyTypeRepository.findAllByOrganizationId(organizationId))
                .thenReturn(allBodyTypes);

        // When
        List<VehicleBodyTypeDTO> result = service.getAllVehicleBodyTypes(organizationId);

        // Then
        assertEquals(allBodyTypeDTOs.size(), result.size());
        verify(vehicleBodyTypeRepository).findAllByOrganizationId(organizationId);
        verify(vehicleBodyTypeMapper, times(allBodyTypes.size())).toDto(any(VehicleBodyType.class));
    }

    @Test
    void getVehicleBodyTypeById_ShouldReturnBodyTypeById() {
        // Given
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.of(vehicleBodyType));

        // When
        Optional<VehicleBodyTypeDTO> result = service.getVehicleBodyTypeById(bodyTypeId, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleBodyTypeDTO, result.get());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeMapper).toDto(vehicleBodyType);
    }

    @Test
    void getVehicleBodyTypeById_ShouldReturnEmpty_WhenBodyTypeNotFound() {
        // Given
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleBodyTypeDTO> result = service.getVehicleBodyTypeById(bodyTypeId, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeMapper, never()).toDto(any(VehicleBodyType.class));
    }

    @Test
    void getVehicleBodyTypeById_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.of(vehicleBodyType));

        // When
        Optional<VehicleBodyTypeDTO> result = service.getVehicleBodyTypeById(bodyTypeId, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeMapper, never()).toDto(any(VehicleBodyType.class));
    }

    @Test
    void getVehicleBodyTypeByCode_ShouldReturnBodyTypeByCode() {
        // Given
        String code = "SEDAN";
        when(vehicleBodyTypeRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.of(vehicleBodyType));

        // When
        Optional<VehicleBodyTypeDTO> result = service.getVehicleBodyTypeByCode(code, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleBodyTypeDTO, result.get());
        verify(vehicleBodyTypeRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(vehicleBodyTypeMapper).toDto(vehicleBodyType);
    }

    @Test
    void getVehicleBodyTypeByCode_ShouldReturnEmpty_WhenBodyTypeNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(vehicleBodyTypeRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleBodyTypeDTO> result = service.getVehicleBodyTypeByCode(code, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleBodyTypeRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(vehicleBodyTypeMapper, never()).toDto(any(VehicleBodyType.class));
    }

    @Test
    void updateVehicleBodyType_ShouldReturnUpdatedBodyType() {
        // Given
        VehicleBodyType existingBodyType = new VehicleBodyType();
        existingBodyType.setId(bodyTypeId);
        existingBodyType.setCode("SEDAN");
        existingBodyType.setName("Old Name");
        existingBodyType.setActive(true);
        existingBodyType.setOrganizationId(organizationId);

        VehicleBodyType updatedBodyType = new VehicleBodyType();
        updatedBodyType.setId(bodyTypeId);
        updatedBodyType.setCode("SEDAN");
        updatedBodyType.setName("New Name");
        updatedBodyType.setActive(true);
        updatedBodyType.setOrganizationId(organizationId);

        VehicleBodyTypeDTO updatedBodyTypeDTO = new VehicleBodyTypeDTO();
        updatedBodyTypeDTO.setId(bodyTypeId);
        updatedBodyTypeDTO.setCode("SEDAN");
        updatedBodyTypeDTO.setName("New Name");
        updatedBodyTypeDTO.setActive(true);

        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.of(existingBodyType));
        when(vehicleBodyTypeRepository.save(any(VehicleBodyType.class)))
                .thenReturn(updatedBodyType);
        when(vehicleBodyTypeMapper.toDto(updatedBodyType))
                .thenReturn(updatedBodyTypeDTO);

        // When
        Optional<VehicleBodyTypeDTO> result = service.updateVehicleBodyType(bodyTypeId, updatedBodyType, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedBodyTypeDTO, result.get());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository).save(any(VehicleBodyType.class));
        verify(vehicleBodyTypeMapper).toDto(updatedBodyType);
    }

    @Test
    void updateVehicleBodyType_ShouldReturnEmpty_WhenBodyTypeNotFound() {
        // Given
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleBodyTypeDTO> result = service.updateVehicleBodyType(bodyTypeId, vehicleBodyType, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository, never()).save(any(VehicleBodyType.class));
        verify(vehicleBodyTypeMapper, never()).toDto(any(VehicleBodyType.class));
    }

    @Test
    void updateVehicleBodyType_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.of(vehicleBodyType));

        // When
        Optional<VehicleBodyTypeDTO> result = service.updateVehicleBodyType(bodyTypeId, vehicleBodyType, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository, never()).save(any(VehicleBodyType.class));
        verify(vehicleBodyTypeMapper, never()).toDto(any(VehicleBodyType.class));
    }

    @Test
    void setActive_ShouldReturnUpdatedBodyType() {
        // Given
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.of(vehicleBodyType));

        VehicleBodyType updatedBodyType = new VehicleBodyType();
        updatedBodyType.setId(bodyTypeId);
        updatedBodyType.setCode("SEDAN");
        updatedBodyType.setName("Sedan");
        updatedBodyType.setActive(false); // Changed to inactive
        updatedBodyType.setOrganizationId(organizationId);

        VehicleBodyTypeDTO updatedBodyTypeDTO = new VehicleBodyTypeDTO();
        updatedBodyTypeDTO.setId(bodyTypeId);
        updatedBodyTypeDTO.setCode("SEDAN");
        updatedBodyTypeDTO.setName("Sedan");
        updatedBodyTypeDTO.setActive(false); // Changed to inactive

        when(vehicleBodyTypeRepository.save(any(VehicleBodyType.class)))
                .thenReturn(updatedBodyType);
        when(vehicleBodyTypeMapper.toDto(updatedBodyType))
                .thenReturn(updatedBodyTypeDTO);

        // When
        Optional<VehicleBodyTypeDTO> result = service.setActive(bodyTypeId, false, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedBodyTypeDTO, result.get());
        assertFalse(result.get().isActive());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository).save(any(VehicleBodyType.class));
        verify(vehicleBodyTypeMapper).toDto(updatedBodyType);
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenBodyTypeNotFound() {
        // Given
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleBodyTypeDTO> result = service.setActive(bodyTypeId, false, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository, never()).save(any(VehicleBodyType.class));
        verify(vehicleBodyTypeMapper, never()).toDto(any(VehicleBodyType.class));
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.of(vehicleBodyType));

        // When
        Optional<VehicleBodyTypeDTO> result = service.setActive(bodyTypeId, false, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository, never()).save(any(VehicleBodyType.class));
        verify(vehicleBodyTypeMapper, never()).toDto(any(VehicleBodyType.class));
    }

    @Test
    void delete_ShouldReturnTrue_WhenBodyTypeDeleted() {
        // Given
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.of(vehicleBodyType));

        // When
        boolean result = service.delete(bodyTypeId, organizationId);

        // Then
        assertTrue(result);
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository).deleteById(bodyTypeId);
    }

    @Test
    void delete_ShouldReturnFalse_WhenBodyTypeNotFound() {
        // Given
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.empty());

        // When
        boolean result = service.delete(bodyTypeId, organizationId);

        // Then
        assertFalse(result);
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void delete_ShouldReturnFalse_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleBodyTypeRepository.findById(bodyTypeId))
                .thenReturn(Optional.of(vehicleBodyType));

        // When
        boolean result = service.delete(bodyTypeId, differentOrgId);

        // Then
        assertFalse(result);
        verify(vehicleBodyTypeRepository).findById(bodyTypeId);
        verify(vehicleBodyTypeRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getByCode_ShouldReturnBodyTypeByCode() {
        // Given
        String code = "SEDAN";
        when(vehicleBodyTypeRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.of(vehicleBodyType));

        // When
        Optional<VehicleBodyTypeDTO> result = service.getByCode(code, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleBodyTypeDTO, result.get());
        verify(vehicleBodyTypeRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(vehicleBodyTypeMapper).toDto(vehicleBodyType);
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = service.getEntityName();

        // Then
        assertEquals("type de carrosserie", result);
    }
}
