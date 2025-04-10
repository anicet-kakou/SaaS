package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.security.application.command.CreateRoleCommand;
import com.devolution.saas.core.security.application.command.UpdateRoleCommand;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.usecase.*;
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
class RoleServiceTest {

    @Mock
    private CreateRole createRole;

    @Mock
    private UpdateRole updateRole;

    @Mock
    private GetRole getRole;

    @Mock
    private ListRoles listRoles;

    @Mock
    private DeleteRole deleteRole;

    @Spy
    private MockTenantContextHolder tenantContextHolder = new MockTenantContextHolder();

    @InjectMocks
    private RoleService roleService;

    private UUID roleId;
    private CreateRoleCommand createCommand;
    private UpdateRoleCommand updateCommand;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();

        // Set up tenant context
        tenantContextHolder.setCurrentTenant(UUID.randomUUID());

        // Set up create command
        createCommand = new CreateRoleCommand();
        createCommand.setName("Test Role");
        createCommand.setCode("TEST_ROLE");
        createCommand.setDescription("Test role description");
        createCommand.setSystemDefined(false);
        createCommand.setPermissionIds(Arrays.asList(UUID.randomUUID()));

        // Set up update command
        updateCommand = new UpdateRoleCommand();
        updateCommand.setId(roleId);
        updateCommand.setName("Updated Role");
        updateCommand.setDescription("Updated role description");
        updateCommand.setPermissionIds(Arrays.asList(UUID.randomUUID()));

        // Set up role DTO
        roleDTO = new RoleDTO();
        roleDTO.setId(roleId);
        roleDTO.setName("Test Role");
        roleDTO.setCode("TEST_ROLE");
        roleDTO.setDescription("Test role description");
        roleDTO.setSystemDefined(false);
    }

    @Test
    void createRole_Success() {
        // Given
        when(createRole.execute(any(CreateRoleCommand.class))).thenReturn(roleDTO);

        // When
        RoleDTO result = roleService.createRole(createCommand);

        // Then
        assertNotNull(result);
        assertEquals(roleId, result.getId());
        assertEquals("Test Role", result.getName());
        assertEquals("TEST_ROLE", result.getCode());
        verify(createRole).execute(createCommand);
    }

    @Test
    void createRole_ValidationError() {
        // Given
        when(createRole.execute(any(CreateRoleCommand.class)))
                .thenThrow(new ValidationException("Le code du rôle est déjà utilisé"));

        // When & Then
        assertThrows(ValidationException.class, () -> {
            roleService.createRole(createCommand);
        });

        verify(createRole).execute(createCommand);
    }

    @Test
    void updateRole_Success() {
        // Given
        RoleDTO updatedDTO = new RoleDTO();
        updatedDTO.setId(roleId);
        updatedDTO.setName("Updated Role");
        updatedDTO.setCode("TEST_ROLE");
        updatedDTO.setDescription("Updated role description");

        when(updateRole.execute(any(UpdateRoleCommand.class))).thenReturn(updatedDTO);

        // When
        RoleDTO result = roleService.updateRole(updateCommand);

        // Then
        assertNotNull(result);
        assertEquals(roleId, result.getId());
        assertEquals("Updated Role", result.getName());
        assertEquals("Updated role description", result.getDescription());
        verify(updateRole).execute(updateCommand);
    }

    @Test
    void getRole_Success() {
        // Given
        when(getRole.execute(any(UUID.class))).thenReturn(roleDTO);

        // When
        RoleDTO result = roleService.getRole(roleId);

        // Then
        assertNotNull(result);
        assertEquals(roleId, result.getId());
        assertEquals("Test Role", result.getName());
        assertEquals("TEST_ROLE", result.getCode());
        verify(getRole).execute(roleId);
    }

    @Test
    void getRole_NotFound() {
        // Given
        when(getRole.execute(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("Role", roleId));

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            roleService.getRole(roleId);
        });

        verify(getRole).execute(roleId);
    }

    @Test
    void listRoles_Success() {
        // Given
        List<RoleDTO> expectedRoles = Arrays.asList(roleDTO, new RoleDTO());
        when(listRoles.execute()).thenReturn(expectedRoles);

        // When
        List<RoleDTO> result = roleService.listRoles();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listRoles).execute();
    }

    @Test
    void listRolesByOrganization_Success() {
        // Given
        UUID organizationId = UUID.randomUUID();
        List<RoleDTO> expectedRoles = Arrays.asList(roleDTO, new RoleDTO());
        when(listRoles.executeByOrganization(any(UUID.class))).thenReturn(expectedRoles);

        // When
        List<RoleDTO> result = roleService.listRolesByOrganization(organizationId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listRoles).executeByOrganization(organizationId);
    }

    @Test
    void listRolesBySystemDefined_Success() {
        // Given
        boolean systemDefined = true;
        List<RoleDTO> expectedRoles = Arrays.asList(roleDTO, new RoleDTO());
        when(listRoles.executeBySystemDefined(anyBoolean())).thenReturn(expectedRoles);

        // When
        List<RoleDTO> result = roleService.listRolesBySystemDefined(systemDefined);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listRoles).executeBySystemDefined(systemDefined);
    }

    @Test
    void deleteRole_Success() {
        // Given
        doNothing().when(deleteRole).execute(any(UUID.class));

        // When
        roleService.deleteRole(roleId);

        // Then
        verify(deleteRole).execute(roleId);
    }

    @Test
    void executeList_Success() {
        // Given
        List<RoleDTO> expectedRoles = Arrays.asList(roleDTO, new RoleDTO());
        when(listRoles.execute()).thenReturn(expectedRoles);

        // When
        List<RoleDTO> result = roleService.executeList();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listRoles).execute();
    }
}
