package com.devolution.saas.core.security.api;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.security.application.command.CreateRoleCommand;
import com.devolution.saas.core.security.application.command.UpdateRoleCommand;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.service.RoleService;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService roleService;

    @MockBean
    private TenantContextHolder tenantContextHolder;

    private UUID roleId;
    private RoleDTO roleDTO;
    private CreateRoleCommand createCommand;
    private UpdateRoleCommand updateCommand;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();

        // Set up tenant context
        when(tenantContextHolder.getCurrentTenant()).thenReturn(UUID.randomUUID());
        when(tenantContextHolder.hasTenant()).thenReturn(true);

        // Set up role DTO
        roleDTO = RoleDTO.builder()
                .id(roleId)
                .name("Test Role")
                .description("Test role description")
                .systemDefined(false)
                .build();

        // Set up create command
        createCommand = CreateRoleCommand.builder()
                .name("Test Role")
                .description("Test role description")
                .organizationId(UUID.randomUUID())
                .permissionIds(new HashSet<>(Arrays.asList(UUID.randomUUID())))
                .build();

        // Set up update command
        updateCommand = new UpdateRoleCommand();
        updateCommand.setId(roleId);
        updateCommand.setName("Updated Role");
        updateCommand.setDescription("Updated role description");
        updateCommand.setPermissionIds(Arrays.asList(UUID.randomUUID()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createRole_Success() throws Exception {
        when(roleService.createRole(any(CreateRoleCommand.class)))
                .thenReturn(roleDTO);

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(roleId.toString())))
                .andExpect(jsonPath("$.name", is("Test Role")))
                .andExpect(jsonPath("$.code", is("TEST_ROLE")))
                .andExpect(jsonPath("$.description", is("Test role description")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createRole_ValidationError() throws Exception {
        when(roleService.createRole(any(CreateRoleCommand.class)))
                .thenThrow(new ValidationException("Le code du rôle est déjà utilisé"));

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRole_Success() throws Exception {
        when(roleService.getRole(any(UUID.class)))
                .thenReturn(roleDTO);

        mockMvc.perform(get("/api/v1/roles/{id}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roleId.toString())))
                .andExpect(jsonPath("$.name", is("Test Role")))
                .andExpect(jsonPath("$.code", is("TEST_ROLE")))
                .andExpect(jsonPath("$.description", is("Test role description")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRole_NotFound() throws Exception {
        when(roleService.getRole(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("Role", roleId));

        mockMvc.perform(get("/api/v1/roles/{id}", roleId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateRole_Success() throws Exception {
        RoleDTO updatedDTO = RoleDTO.builder()
                .id(roleId)
                .name("Updated Role")
                .description("Updated role description")
                .build();

        when(roleService.updateRole(any(UpdateRoleCommand.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/roles/{id}", roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roleId.toString())))
                .andExpect(jsonPath("$.name", is("Updated Role")))
                .andExpect(jsonPath("$.description", is("Updated role description")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listRoles_Success() throws Exception {
        List<RoleDTO> roles = Arrays.asList(
                roleDTO,
                RoleDTO.builder().build()
        );

        when(roleService.listRoles())
                .thenReturn(roles);

        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(roleId.toString())))
                .andExpect(jsonPath("$[0].name", is("Test Role")))
                .andExpect(jsonPath("$[0].code", is("TEST_ROLE")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listRolesByOrganization_Success() throws Exception {
        UUID organizationId = UUID.randomUUID();
        List<RoleDTO> roles = Arrays.asList(
                roleDTO,
                RoleDTO.builder().build()
        );

        when(roleService.listRolesByOrganization(any(UUID.class)))
                .thenReturn(roles);

        mockMvc.perform(get("/api/v1/roles/organization/{organizationId}", organizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(roleId.toString())))
                .andExpect(jsonPath("$[0].name", is("Test Role")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listRolesBySystemDefined_Success() throws Exception {
        List<RoleDTO> roles = Arrays.asList(
                roleDTO,
                RoleDTO.builder().build()
        );

        when(roleService.listRolesBySystemDefined(anyBoolean()))
                .thenReturn(roles);

        mockMvc.perform(get("/api/v1/roles/system")
                        .param("systemDefined", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(roleId.toString())))
                .andExpect(jsonPath("$[0].name", is("Test Role")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRole_Success() throws Exception {
        doNothing().when(roleService).deleteRole(any(UUID.class));

        mockMvc.perform(delete("/api/v1/roles/{id}", roleId))
                .andExpect(status().isNoContent());

        verify(roleService).deleteRole(roleId);
    }
}
