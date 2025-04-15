package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleManufacturerDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleManufacturerMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleManufacturerRepository;
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
class VehicleManufacturerServiceImplTest {

    @Mock
    private VehicleManufacturerRepository vehicleManufacturerRepository;

    @Mock
    private VehicleManufacturerMapper vehicleManufacturerMapper;

    @InjectMocks
    private VehicleManufacturerServiceImpl service;

    private UUID organizationId;
    private UUID manufacturerId;
    private VehicleManufacturer vehicleManufacturer;
    private VehicleManufacturerDTO vehicleManufacturerDTO;
    private List<VehicleManufacturer> activeManufacturers;
    private List<VehicleManufacturer> allManufacturers;
    private List<VehicleManufacturerDTO> activeManufacturerDTOs;
    private List<VehicleManufacturerDTO> allManufacturerDTOs;

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

        VehicleManufacturer manufacturer1 = new VehicleManufacturer();
        manufacturer1.setId(UUID.randomUUID());
        manufacturer1.setCode("TOYOTA");
        manufacturer1.setName("Toyota");
        manufacturer1.setActive(true);
        manufacturer1.setOrganizationId(organizationId);

        VehicleManufacturer manufacturer2 = new VehicleManufacturer();
        manufacturer2.setId(UUID.randomUUID());
        manufacturer2.setCode("HONDA");
        manufacturer2.setName("Honda");
        manufacturer2.setActive(true);
        manufacturer2.setOrganizationId(organizationId);

        VehicleManufacturer manufacturer3 = new VehicleManufacturer();
        manufacturer3.setId(UUID.randomUUID());
        manufacturer3.setCode("BMW");
        manufacturer3.setName("BMW");
        manufacturer3.setActive(false);
        manufacturer3.setOrganizationId(organizationId);

        activeManufacturers = Arrays.asList(manufacturer1, manufacturer2);
        allManufacturers = Arrays.asList(manufacturer1, manufacturer2, manufacturer3);

        VehicleManufacturerDTO manufacturer1DTO = new VehicleManufacturerDTO();
        manufacturer1DTO.setId(manufacturer1.getId());
        manufacturer1DTO.setCode("TOYOTA");
        manufacturer1DTO.setName("Toyota");
        manufacturer1DTO.setActive(true);

        VehicleManufacturerDTO manufacturer2DTO = new VehicleManufacturerDTO();
        manufacturer2DTO.setId(manufacturer2.getId());
        manufacturer2DTO.setCode("HONDA");
        manufacturer2DTO.setName("Honda");
        manufacturer2DTO.setActive(true);

        VehicleManufacturerDTO manufacturer3DTO = new VehicleManufacturerDTO();
        manufacturer3DTO.setId(manufacturer3.getId());
        manufacturer3DTO.setCode("BMW");
        manufacturer3DTO.setName("BMW");
        manufacturer3DTO.setActive(false);

        activeManufacturerDTOs = Arrays.asList(manufacturer1DTO, manufacturer2DTO);
        allManufacturerDTOs = Arrays.asList(manufacturer1DTO, manufacturer2DTO, manufacturer3DTO);

        // Setup mapper
        when(vehicleManufacturerMapper.toDto(manufacturer1)).thenReturn(manufacturer1DTO);
        when(vehicleManufacturerMapper.toDto(manufacturer2)).thenReturn(manufacturer2DTO);
        when(vehicleManufacturerMapper.toDto(manufacturer3)).thenReturn(manufacturer3DTO);
        when(vehicleManufacturerMapper.toDto(vehicleManufacturer)).thenReturn(vehicleManufacturerDTO);
    }

    @Test
    void createVehicleManufacturer_ShouldReturnCreatedManufacturer() {
        // Given
        when(vehicleManufacturerRepository.save(vehicleManufacturer)).thenReturn(vehicleManufacturer);

        // When
        VehicleManufacturerDTO result = service.createVehicleManufacturer(vehicleManufacturer, organizationId);

        // Then
        assertEquals(vehicleManufacturerDTO, result);
        assertEquals(organizationId, vehicleManufacturer.getOrganizationId());
        verify(vehicleManufacturerRepository).save(vehicleManufacturer);
        verify(vehicleManufacturerMapper).toDto(vehicleManufacturer);
    }

    @Test
    void getAllActiveVehicleManufacturers_ShouldReturnActiveManufacturers() {
        // Given
        when(vehicleManufacturerRepository.findAllByOrganizationIdAndActiveIsTrue(organizationId))
                .thenReturn(activeManufacturers);

        // When
        List<VehicleManufacturerDTO> result = service.getAllActiveVehicleManufacturers(organizationId);

        // Then
        assertEquals(activeManufacturerDTOs.size(), result.size());
        verify(vehicleManufacturerRepository).findAllByOrganizationIdAndActiveIsTrue(organizationId);
        verify(vehicleManufacturerMapper, times(activeManufacturers.size())).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void getAllVehicleManufacturers_ShouldReturnAllManufacturers() {
        // Given
        when(vehicleManufacturerRepository.findAllByOrganizationId(organizationId))
                .thenReturn(allManufacturers);

        // When
        List<VehicleManufacturerDTO> result = service.getAllVehicleManufacturers(organizationId);

        // Then
        assertEquals(allManufacturerDTOs.size(), result.size());
        verify(vehicleManufacturerRepository).findAllByOrganizationId(organizationId);
        verify(vehicleManufacturerMapper, times(allManufacturers.size())).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void getVehicleManufacturerById_ShouldReturnManufacturerById() {
        // Given
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.of(vehicleManufacturer));

        // When
        Optional<VehicleManufacturerDTO> result = service.getVehicleManufacturerById(manufacturerId, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleManufacturerDTO, result.get());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerMapper).toDto(vehicleManufacturer);
    }

    @Test
    void getVehicleManufacturerById_ShouldReturnEmpty_WhenManufacturerNotFound() {
        // Given
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleManufacturerDTO> result = service.getVehicleManufacturerById(manufacturerId, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerMapper, never()).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void getVehicleManufacturerById_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.of(vehicleManufacturer));

        // When
        Optional<VehicleManufacturerDTO> result = service.getVehicleManufacturerById(manufacturerId, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerMapper, never()).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void getVehicleManufacturerByCode_ShouldReturnManufacturerByCode() {
        // Given
        String code = "TOYOTA";
        when(vehicleManufacturerRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.of(vehicleManufacturer));

        // When
        Optional<VehicleManufacturerDTO> result = service.getVehicleManufacturerByCode(code, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleManufacturerDTO, result.get());
        verify(vehicleManufacturerRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(vehicleManufacturerMapper).toDto(vehicleManufacturer);
    }

    @Test
    void getVehicleManufacturerByCode_ShouldReturnEmpty_WhenManufacturerNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(vehicleManufacturerRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleManufacturerDTO> result = service.getVehicleManufacturerByCode(code, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleManufacturerRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(vehicleManufacturerMapper, never()).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void getByCode_ShouldReturnManufacturerByCode() {
        // Given
        String code = "TOYOTA";
        when(vehicleManufacturerRepository.findByCodeAndOrganizationId(code, organizationId))
                .thenReturn(Optional.of(vehicleManufacturer));

        // When
        Optional<VehicleManufacturerDTO> result = service.getByCode(code, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehicleManufacturerDTO, result.get());
        verify(vehicleManufacturerRepository).findByCodeAndOrganizationId(code, organizationId);
        verify(vehicleManufacturerMapper).toDto(vehicleManufacturer);
    }

    @Test
    void updateVehicleManufacturer_ShouldReturnUpdatedManufacturer() {
        // Given
        VehicleManufacturer existingManufacturer = new VehicleManufacturer();
        existingManufacturer.setId(manufacturerId);
        existingManufacturer.setCode("TOYOTA");
        existingManufacturer.setName("Old Name");
        existingManufacturer.setActive(true);
        existingManufacturer.setOrganizationId(organizationId);

        VehicleManufacturer updatedManufacturer = new VehicleManufacturer();
        updatedManufacturer.setId(manufacturerId);
        updatedManufacturer.setCode("TOYOTA");
        updatedManufacturer.setName("New Name");
        updatedManufacturer.setActive(true);
        updatedManufacturer.setOrganizationId(organizationId);

        VehicleManufacturerDTO updatedManufacturerDTO = new VehicleManufacturerDTO();
        updatedManufacturerDTO.setId(manufacturerId);
        updatedManufacturerDTO.setCode("TOYOTA");
        updatedManufacturerDTO.setName("New Name");
        updatedManufacturerDTO.setActive(true);

        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.of(existingManufacturer));
        when(vehicleManufacturerRepository.save(any(VehicleManufacturer.class)))
                .thenReturn(updatedManufacturer);
        when(vehicleManufacturerMapper.toDto(updatedManufacturer))
                .thenReturn(updatedManufacturerDTO);

        // When
        Optional<VehicleManufacturerDTO> result = service.updateVehicleManufacturer(manufacturerId, updatedManufacturer, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedManufacturerDTO, result.get());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository).save(any(VehicleManufacturer.class));
        verify(vehicleManufacturerMapper).toDto(updatedManufacturer);
    }

    @Test
    void updateVehicleManufacturer_ShouldReturnEmpty_WhenManufacturerNotFound() {
        // Given
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleManufacturerDTO> result = service.updateVehicleManufacturer(manufacturerId, vehicleManufacturer, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository, never()).save(any(VehicleManufacturer.class));
        verify(vehicleManufacturerMapper, never()).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void updateVehicleManufacturer_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.of(vehicleManufacturer));

        // When
        Optional<VehicleManufacturerDTO> result = service.updateVehicleManufacturer(manufacturerId, vehicleManufacturer, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository, never()).save(any(VehicleManufacturer.class));
        verify(vehicleManufacturerMapper, never()).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void setActive_ShouldReturnUpdatedManufacturer() {
        // Given
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.of(vehicleManufacturer));

        VehicleManufacturer updatedManufacturer = new VehicleManufacturer();
        updatedManufacturer.setId(manufacturerId);
        updatedManufacturer.setCode("TOYOTA");
        updatedManufacturer.setName("Toyota");
        updatedManufacturer.setActive(false); // Changed to inactive
        updatedManufacturer.setOrganizationId(organizationId);

        VehicleManufacturerDTO updatedManufacturerDTO = new VehicleManufacturerDTO();
        updatedManufacturerDTO.setId(manufacturerId);
        updatedManufacturerDTO.setCode("TOYOTA");
        updatedManufacturerDTO.setName("Toyota");
        updatedManufacturerDTO.setActive(false); // Changed to inactive

        when(vehicleManufacturerRepository.save(any(VehicleManufacturer.class)))
                .thenReturn(updatedManufacturer);
        when(vehicleManufacturerMapper.toDto(updatedManufacturer))
                .thenReturn(updatedManufacturerDTO);

        // When
        Optional<VehicleManufacturerDTO> result = service.setActive(manufacturerId, false, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedManufacturerDTO, result.get());
        assertFalse(result.get().isActive());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository).save(any(VehicleManufacturer.class));
        verify(vehicleManufacturerMapper).toDto(updatedManufacturer);
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenManufacturerNotFound() {
        // Given
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.empty());

        // When
        Optional<VehicleManufacturerDTO> result = service.setActive(manufacturerId, false, organizationId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository, never()).save(any(VehicleManufacturer.class));
        verify(vehicleManufacturerMapper, never()).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void setActive_ShouldReturnEmpty_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.of(vehicleManufacturer));

        // When
        Optional<VehicleManufacturerDTO> result = service.setActive(manufacturerId, false, differentOrgId);

        // Then
        assertFalse(result.isPresent());
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository, never()).save(any(VehicleManufacturer.class));
        verify(vehicleManufacturerMapper, never()).toDto(any(VehicleManufacturer.class));
    }

    @Test
    void delete_ShouldReturnTrue_WhenManufacturerDeleted() {
        // Given
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.of(vehicleManufacturer));

        // When
        boolean result = service.delete(manufacturerId, organizationId);

        // Then
        assertTrue(result);
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository).deleteById(manufacturerId);
    }

    @Test
    void delete_ShouldReturnFalse_WhenManufacturerNotFound() {
        // Given
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.empty());

        // When
        boolean result = service.delete(manufacturerId, organizationId);

        // Then
        assertFalse(result);
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void delete_ShouldReturnFalse_WhenOrganizationIdDoesNotMatch() {
        // Given
        UUID differentOrgId = UUID.randomUUID();
        when(vehicleManufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.of(vehicleManufacturer));

        // When
        boolean result = service.delete(manufacturerId, differentOrgId);

        // Then
        assertFalse(result);
        verify(vehicleManufacturerRepository).findById(manufacturerId);
        verify(vehicleManufacturerRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = service.getEntityName();

        // Then
        assertEquals("fabricant de véhicule", result);
    }
}
