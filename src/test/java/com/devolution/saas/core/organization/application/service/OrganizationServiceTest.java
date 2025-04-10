package com.devolution.saas.core.organization.application.service;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.organization.application.command.CreateOrganizationCommand;
import com.devolution.saas.core.organization.application.command.UpdateOrganizationCommand;
import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.application.dto.OrganizationHierarchyDTO;
import com.devolution.saas.core.organization.application.dto.OrganizationTypeDTO;
import com.devolution.saas.core.organization.application.mapper.OrganizationMapper;
import com.devolution.saas.core.organization.application.query.GetOrganizationQuery;
import com.devolution.saas.core.organization.application.query.ListOrganizationsQuery;
import com.devolution.saas.core.organization.application.usecase.*;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.model.OrganizationType;
import com.devolution.saas.core.organization.domain.repository.OrganizationHierarchyRepository;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import com.devolution.saas.core.organization.infrastructure.messaging.OrganizationEventPublisher;
import com.devolution.saas.core.security.infrastructure.service.MockTenantContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationHierarchyRepository hierarchyRepository;

    @Mock
    private OrganizationEventPublisher eventPublisher;

    @Mock
    private CreateOrganization createOrganization;

    @Mock
    private UpdateOrganization updateOrganization;

    @Mock
    private GetOrganization getOrganization;

    @Mock
    private ListOrganizations listOrganizations;

    @Mock
    private GetOrganizationHierarchy getOrganizationHierarchy;

    @Mock
    private DeleteOrganization deleteOrganization;

    @Mock
    private OrganizationMapper organizationMapper;

    @Spy
    private MockTenantContextHolder tenantContextHolder = new MockTenantContextHolder();

    @InjectMocks
    private OrganizationService organizationService;

    private CreateOrganizationCommand createCommand;
    private Organization organization;
    private UUID organizationId;

    @BeforeEach
    void setUp() {
        organizationId = UUID.randomUUID();

        // Set up tenant context
        tenantContextHolder.setCurrentTenant(UUID.randomUUID());

        // Création d'une commande de test
        createCommand = CreateOrganizationCommand.builder()
                .name("Test Organization")
                .code("TEST-ORG")
                .type(OrganizationType.INSURANCE_COMPANY)
                .address("123 Test Street")
                .phone("+1234567890")
                .email("test@example.com")
                .build();

        // Création d'une organisation de test
        organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Test Organization");
        organization.setCode("TEST-ORG");
        organization.setType(OrganizationType.INSURANCE_COMPANY);
        organization.setAddress("123 Test Street");
        organization.setPhone("+1234567890");
        organization.setEmail("test@example.com");
    }

    @Test
    void createOrganization_Success() {
        // Given
        OrganizationDTO expectedDto = new OrganizationDTO();
        expectedDto.setId(organizationId);
        expectedDto.setName(createCommand.getName());
        expectedDto.setCode(createCommand.getCode());
        expectedDto.setType(createCommand.getType());

        when(createOrganization.execute(any(CreateOrganizationCommand.class))).thenReturn(expectedDto);

        // When
        OrganizationDTO result = organizationService.createOrganization(createCommand);

        // Then
        assertNotNull(result);
        assertEquals(organizationId, result.getId());
        assertEquals(createCommand.getName(), result.getName());
        assertEquals(createCommand.getCode(), result.getCode());
        assertEquals(createCommand.getType(), result.getType());

        verify(createOrganization).execute(createCommand);
    }

    @Test
    void createOrganization_DuplicateCode() {
        // Given
        when(createOrganization.execute(any(CreateOrganizationCommand.class)))
                .thenThrow(new ValidationException("Le code de l'organisation est déjà utilisé"));

        // When & Then
        assertThrows(ValidationException.class, () -> {
            organizationService.createOrganization(createCommand);
        });

        verify(createOrganization).execute(createCommand);
    }

    @Test
    void getOrganization_ById_Success() {
        // Given
        GetOrganizationQuery query = new GetOrganizationQuery(organizationId, null);
        OrganizationDTO expectedDto = new OrganizationDTO();
        expectedDto.setId(organizationId);
        expectedDto.setName(organization.getName());
        expectedDto.setCode(organization.getCode());

        when(getOrganization.execute(any(GetOrganizationQuery.class))).thenReturn(expectedDto);

        // When
        OrganizationDTO result = organizationService.getOrganization(query);

        // Then
        assertNotNull(result);
        assertEquals(organizationId, result.getId());
        assertEquals(organization.getName(), result.getName());
        assertEquals(organization.getCode(), result.getCode());

        verify(getOrganization).execute(query);
    }

    @Test
    void getOrganization_ByCode_Success() {
        // Given
        String code = "TEST-ORG";
        GetOrganizationQuery query = new GetOrganizationQuery(null, code);
        OrganizationDTO expectedDto = new OrganizationDTO();
        expectedDto.setId(organizationId);
        expectedDto.setName(organization.getName());
        expectedDto.setCode(organization.getCode());

        when(getOrganization.execute(any(GetOrganizationQuery.class))).thenReturn(expectedDto);

        // When
        OrganizationDTO result = organizationService.getOrganization(query);

        // Then
        assertNotNull(result);
        assertEquals(organizationId, result.getId());
        assertEquals(organization.getName(), result.getName());
        assertEquals(organization.getCode(), result.getCode());

        verify(getOrganization).execute(query);
    }

    @Test
    void getOrganization_NotFound() {
        // Given
        GetOrganizationQuery query = new GetOrganizationQuery(organizationId, null);
        when(getOrganization.execute(any(GetOrganizationQuery.class)))
                .thenThrow(new ResourceNotFoundException("Organization", organizationId));

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            organizationService.getOrganization(query);
        });

        verify(getOrganization).execute(query);
    }

    @Test
    void getOrganization_InvalidQuery() {
        // Given
        GetOrganizationQuery query = new GetOrganizationQuery(null, null);
        when(getOrganization.execute(any(GetOrganizationQuery.class)))
                .thenThrow(new ValidationException("L'ID ou le code de l'organisation est obligatoire"));

        // When & Then
        assertThrows(ValidationException.class, () -> {
            organizationService.getOrganization(query);
        });

        verify(getOrganization).execute(query);
    }

    @Test
    void listOrganizations_Success() {
        // Given
        ListOrganizationsQuery query = ListOrganizationsQuery.builder()
                .page(0)
                .size(10)
                .build();

        List<OrganizationDTO> expectedDtos = Arrays.asList(
                new OrganizationDTO(),
                new OrganizationDTO()
        );

        when(listOrganizations.execute(any(ListOrganizationsQuery.class))).thenReturn(expectedDtos);

        // When
        List<OrganizationDTO> result = organizationService.listOrganizations(query);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listOrganizations).execute(query);
    }

    @Test
    void getOrganizationHierarchy_Success() {
        // Given
        UUID organizationId = UUID.randomUUID();
        OrganizationHierarchyDTO expectedDto = new OrganizationHierarchyDTO();
        expectedDto.setId(organizationId);
        expectedDto.setName("Test Organization");

        when(getOrganizationHierarchy.execute(any(UUID.class))).thenReturn(expectedDto);

        // When
        OrganizationHierarchyDTO result = organizationService.getOrganizationHierarchy(organizationId);

        // Then
        assertNotNull(result);
        assertEquals(organizationId, result.getId());
        assertEquals("Test Organization", result.getName());
        verify(getOrganizationHierarchy).execute(organizationId);
    }

    @Test
    void listOrganizationTypes_Success() {
        // Given
        // No setup needed as this method doesn't use any dependencies

        // When
        List<OrganizationTypeDTO> result = organizationService.listOrganizationTypes();

        // Then
        assertNotNull(result);
        assertEquals(OrganizationType.values().length, result.size());
        // Verify each enum value is represented in the result
        for (OrganizationType type : OrganizationType.values()) {
            assertTrue(result.stream().anyMatch(dto -> dto.getCode().equals(type.name())));
        }
    }

    @Test
    void updateOrganization_Success() {
        // Given
        UUID organizationId = UUID.randomUUID();
        UpdateOrganizationCommand command = new UpdateOrganizationCommand();
        command.setId(organizationId);
        command.setName("Updated Organization");

        OrganizationDTO expectedDto = new OrganizationDTO();
        expectedDto.setId(organizationId);
        expectedDto.setName("Updated Organization");

        when(updateOrganization.execute(any(UpdateOrganizationCommand.class))).thenReturn(expectedDto);

        // When
        OrganizationDTO result = organizationService.updateOrganization(command);

        // Then
        assertNotNull(result);
        assertEquals(organizationId, result.getId());
        assertEquals("Updated Organization", result.getName());
        verify(updateOrganization).execute(command);
    }

    @Test
    void deleteOrganization_Success() {
        // Given
        UUID organizationId = UUID.randomUUID();
        doNothing().when(deleteOrganization).execute(any(UUID.class));

        // When
        organizationService.deleteOrganization(organizationId);

        // Then
        verify(deleteOrganization).execute(organizationId);
    }

    @Test
    void executeList_Success() {
        // Given
        List<OrganizationDTO> expectedDtos = Arrays.asList(
                new OrganizationDTO(),
                new OrganizationDTO()
        );

        when(listOrganizations.execute(any(ListOrganizationsQuery.class))).thenReturn(expectedDtos);

        // When
        List<OrganizationDTO> result = organizationService.executeList();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listOrganizations).execute(any(ListOrganizationsQuery.class));
    }
}
