package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleBodyTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleBodyTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;
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
class VehicleBodyTypeReferenceControllerTest {

    @Mock
    private VehicleBodyTypeService vehicleBodyTypeService;

    @InjectMocks
    private VehicleBodyTypeReferenceController controller;

    private UUID organizationId;
    private UUID bodyTypeId;
    private VehicleBodyType vehicleBodyType;
    private VehicleBodyTypeDTO vehicleBodyTypeDTO;
    private List<VehicleBodyTypeDTO> activeBodyTypes;
    private List<VehicleBodyTypeDTO> allBodyTypes;

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

        VehicleBodyTypeDTO type1 = new VehicleBodyTypeDTO();
        type1.setId(UUID.randomUUID());
        type1.setCode("SEDAN");
        type1.setName("Sedan");
        type1.setActive(true);

        VehicleBodyTypeDTO type2 = new VehicleBodyTypeDTO();
        type2.setId(UUID.randomUUID());
        type2.setCode("SUV");
        type2.setName("SUV");
        type2.setActive(true);

        VehicleBodyTypeDTO type3 = new VehicleBodyTypeDTO();
        type3.setId(UUID.randomUUID());
        type3.setCode("COUPE");
        type3.setName("Coupe");
        type3.setActive(false);

        activeBodyTypes = Arrays.asList(type1, type2);
        allBodyTypes = Arrays.asList(type1, type2, type3);
    }

    @Test
    void listActive_ShouldReturnActiveBodyTypes() {
        // Given
        when(vehicleBodyTypeService.getAllActiveVehicleBodyTypes(organizationId)).thenReturn(activeBodyTypes);

        // When
        List<VehicleBodyTypeDTO> result = controller.listActive(organizationId);

        // Then
        assertEquals(activeBodyTypes, result);
        verify(vehicleBodyTypeService).getAllActiveVehicleBodyTypes(organizationId);
    }

    @Test
    void list_ShouldReturnAllBodyTypes() {
        // Given
        when(vehicleBodyTypeService.getAllVehicleBodyTypes(organizationId)).thenReturn(allBodyTypes);

        // When
        List<VehicleBodyTypeDTO> result = controller.list(organizationId);

        // Then
        assertEquals(allBodyTypes, result);
        verify(vehicleBodyTypeService).getAllVehicleBodyTypes(organizationId);
    }

    @Test
    void get_ShouldReturnBodyTypeById() {
        // Given
        when(vehicleBodyTypeService.getVehicleBodyTypeById(bodyTypeId, organizationId))
                .thenReturn(Optional.of(vehicleBodyTypeDTO));

        // When
        VehicleBodyTypeDTO result = controller.get(bodyTypeId, organizationId);

        // Then
        assertEquals(vehicleBodyTypeDTO, result);
        verify(vehicleBodyTypeService).getVehicleBodyTypeById(bodyTypeId, organizationId);
    }

    @Test
    void get_ShouldThrowResourceNotFoundException_WhenBodyTypeNotFound() {
        // Given
        when(vehicleBodyTypeService.getVehicleBodyTypeById(bodyTypeId, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.get(bodyTypeId, organizationId));
        verify(vehicleBodyTypeService).getVehicleBodyTypeById(bodyTypeId, organizationId);
    }

    @Test
    void getByCode_ShouldReturnBodyTypeByCode() {
        // Given
        String code = "SEDAN";
        when(vehicleBodyTypeService.getVehicleBodyTypeByCode(code, organizationId))
                .thenReturn(Optional.of(vehicleBodyTypeDTO));

        // When
        VehicleBodyTypeDTO result = controller.getByCode(code, organizationId);

        // Then
        assertEquals(vehicleBodyTypeDTO, result);
        verify(vehicleBodyTypeService).getVehicleBodyTypeByCode(code, organizationId);
    }

    @Test
    void getByCode_ShouldThrowResourceNotFoundException_WhenBodyTypeNotFound() {
        // Given
        String code = "NONEXISTENT";
        when(vehicleBodyTypeService.getVehicleBodyTypeByCode(code, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.getByCode(code, organizationId));
        verify(vehicleBodyTypeService).getVehicleBodyTypeByCode(code, organizationId);
    }

    @Test
    void create_ShouldReturnCreatedBodyType() {
        // Given
        when(vehicleBodyTypeService.createVehicleBodyType(vehicleBodyType, organizationId))
                .thenReturn(vehicleBodyTypeDTO);

        // When
        VehicleBodyTypeDTO result = controller.create(vehicleBodyType, organizationId);

        // Then
        assertEquals(vehicleBodyTypeDTO, result);
        verify(vehicleBodyTypeService).createVehicleBodyType(vehicleBodyType, organizationId);
    }

    @Test
    void update_ShouldReturnUpdatedBodyType() {
        // Given
        when(vehicleBodyTypeService.updateVehicleBodyType(bodyTypeId, vehicleBodyType, organizationId))
                .thenReturn(Optional.of(vehicleBodyTypeDTO));

        // When
        VehicleBodyTypeDTO result = controller.update(bodyTypeId, vehicleBodyType, organizationId);

        // Then
        assertEquals(vehicleBodyTypeDTO, result);
        verify(vehicleBodyTypeService).updateVehicleBodyType(bodyTypeId, vehicleBodyType, organizationId);
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenBodyTypeNotFound() {
        // Given
        when(vehicleBodyTypeService.updateVehicleBodyType(bodyTypeId, vehicleBodyType, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.update(bodyTypeId, vehicleBodyType, organizationId));
        verify(vehicleBodyTypeService).updateVehicleBodyType(bodyTypeId, vehicleBodyType, organizationId);
    }

    @Test
    void setActive_ShouldReturnActivatedBodyType() {
        // Given
        when(vehicleBodyTypeService.setActive(bodyTypeId, true, organizationId))
                .thenReturn(Optional.of(vehicleBodyTypeDTO));

        // When
        VehicleBodyTypeDTO result = controller.setActive(bodyTypeId, true, organizationId);

        // Then
        assertEquals(vehicleBodyTypeDTO, result);
        verify(vehicleBodyTypeService).setActive(bodyTypeId, true, organizationId);
    }

    @Test
    void setActive_ShouldThrowResourceNotFoundException_WhenBodyTypeNotFound() {
        // Given
        when(vehicleBodyTypeService.setActive(bodyTypeId, true, organizationId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.setActive(bodyTypeId, true, organizationId));
        verify(vehicleBodyTypeService).setActive(bodyTypeId, true, organizationId);
    }

    @Test
    void delete_ShouldDeleteBodyType() {
        // Given
        when(vehicleBodyTypeService.delete(bodyTypeId, organizationId))
                .thenReturn(true);

        // When
        controller.delete(bodyTypeId, organizationId);

        // Then
        verify(vehicleBodyTypeService).delete(bodyTypeId, organizationId);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenBodyTypeNotFound() {
        // Given
        when(vehicleBodyTypeService.delete(bodyTypeId, organizationId))
                .thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.delete(bodyTypeId, organizationId));
        verify(vehicleBodyTypeService).delete(bodyTypeId, organizationId);
    }

    @Test
    void getEntityName_ShouldReturnCorrectName() {
        // When
        String result = controller.getEntityName();

        // Then
        assertEquals("type de carrosserie", result);
    }

    @Test
    void listActiveEntities_ShouldReturnActiveBodyTypes() {
        // Given
        when(vehicleBodyTypeService.getAllActiveVehicleBodyTypes(organizationId)).thenReturn(activeBodyTypes);

        // When
        ResponseEntity<List<VehicleBodyTypeDTO>> response = controller.listActiveEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeBodyTypes, response.getBody());
        verify(vehicleBodyTypeService).getAllActiveVehicleBodyTypes(organizationId);
    }

    @Test
    void listAllEntities_ShouldReturnAllBodyTypes() {
        // Given
        when(vehicleBodyTypeService.getAllVehicleBodyTypes(organizationId)).thenReturn(allBodyTypes);

        // When
        ResponseEntity<List<VehicleBodyTypeDTO>> response = controller.listAllEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allBodyTypes, response.getBody());
        verify(vehicleBodyTypeService).getAllVehicleBodyTypes(organizationId);
    }
}
