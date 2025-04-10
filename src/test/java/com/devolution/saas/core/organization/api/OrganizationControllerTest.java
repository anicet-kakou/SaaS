package com.devolution.saas.core.organization.api;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.organization.application.command.CreateOrganizationCommand;
import com.devolution.saas.core.organization.application.command.UpdateOrganizationCommand;
import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.application.dto.OrganizationHierarchyDTO;
import com.devolution.saas.core.organization.application.query.GetOrganizationQuery;
import com.devolution.saas.core.organization.application.query.ListOrganizationsQuery;
import com.devolution.saas.core.organization.application.service.OrganizationService;
import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrganizationService organizationService;

    @MockBean
    private TenantContextHolder tenantContextHolder;

    private UUID organizationId;
    private OrganizationDTO organizationDTO;
    private CreateOrganizationCommand createCommand;
    private UpdateOrganizationCommand updateCommand;

    @BeforeEach
    void setUp() {
        organizationId = UUID.randomUUID();

        // Set up tenant context
        when(tenantContextHolder.getCurrentTenant()).thenReturn(UUID.randomUUID());
        when(tenantContextHolder.hasTenant()).thenReturn(true);

        // Set up organization DTO
        organizationDTO = new OrganizationDTO();
        organizationDTO.setId(organizationId);
        organizationDTO.setName("Test Organization");
        organizationDTO.setCode("TEST-ORG");
        organizationDTO.setType(OrganizationType.COMPANY);
        organizationDTO.setStatus(OrganizationStatus.ACTIVE);

        // Set up create command
        createCommand = new CreateOrganizationCommand();
        createCommand.setName("Test Organization");
        createCommand.setCode("TEST-ORG");
        createCommand.setType(OrganizationType.COMPANY);

        // Set up update command
        updateCommand = new UpdateOrganizationCommand();
        updateCommand.setId(organizationId);
        updateCommand.setName("Updated Organization");
        updateCommand.setCode("TEST-ORG");
        updateCommand.setType(OrganizationType.COMPANY);
    }

    @Test
    void createOrganization_Success() throws Exception {
        when(organizationService.createOrganization(any(CreateOrganizationCommand.class)))
                .thenReturn(organizationDTO);

        mockMvc.perform(post("/api/v1/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(organizationId.toString())))
                .andExpect(jsonPath("$.name", is("Test Organization")))
                .andExpect(jsonPath("$.code", is("TEST-ORG")))
                .andExpect(jsonPath("$.type", is(OrganizationType.COMPANY.name())));
    }

    @Test
    void createOrganization_ValidationError() throws Exception {
        when(organizationService.createOrganization(any(CreateOrganizationCommand.class)))
                .thenThrow(new ValidationException("Le code de l'organisation est déjà utilisé"));

        mockMvc.perform(post("/api/v1/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrganization_Success() throws Exception {
        when(organizationService.getOrganization(any(GetOrganizationQuery.class)))
                .thenReturn(organizationDTO);

        mockMvc.perform(get("/api/v1/organizations/{id}", organizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(organizationId.toString())))
                .andExpect(jsonPath("$.name", is("Test Organization")))
                .andExpect(jsonPath("$.code", is("TEST-ORG")))
                .andExpect(jsonPath("$.type", is(OrganizationType.COMPANY.name())));
    }

    @Test
    void getOrganization_NotFound() throws Exception {
        when(organizationService.getOrganization(any(GetOrganizationQuery.class)))
                .thenThrow(new ResourceNotFoundException("Organization", organizationId));

        mockMvc.perform(get("/api/v1/organizations/{id}", organizationId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOrganizationByCode_Success() throws Exception {
        when(organizationService.getOrganization(any(GetOrganizationQuery.class)))
                .thenReturn(organizationDTO);

        mockMvc.perform(get("/api/v1/organizations/code/{code}", "TEST-ORG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(organizationId.toString())))
                .andExpect(jsonPath("$.name", is("Test Organization")))
                .andExpect(jsonPath("$.code", is("TEST-ORG")))
                .andExpect(jsonPath("$.type", is(OrganizationType.COMPANY.name())));
    }

    @Test
    void updateOrganization_Success() throws Exception {
        OrganizationDTO updatedDTO = new OrganizationDTO();
        updatedDTO.setId(organizationId);
        updatedDTO.setName("Updated Organization");
        updatedDTO.setCode("TEST-ORG");
        updatedDTO.setType(OrganizationType.COMPANY);

        when(organizationService.updateOrganization(any(UpdateOrganizationCommand.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/organizations/{id}", organizationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(organizationId.toString())))
                .andExpect(jsonPath("$.name", is("Updated Organization")))
                .andExpect(jsonPath("$.code", is("TEST-ORG")))
                .andExpect(jsonPath("$.type", is(OrganizationType.COMPANY.name())));
    }

    @Test
    void listOrganizations_Success() throws Exception {
        List<OrganizationDTO> organizations = Arrays.asList(
                organizationDTO,
                new OrganizationDTO()
        );

        when(organizationService.listOrganizations(any(ListOrganizationsQuery.class)))
                .thenReturn(organizations);

        mockMvc.perform(get("/api/v1/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(organizationId.toString())))
                .andExpect(jsonPath("$[0].name", is("Test Organization")))
                .andExpect(jsonPath("$[0].code", is("TEST-ORG")));
    }

    @Test
    void searchOrganizations_Success() throws Exception {
        List<OrganizationDTO> organizations = Arrays.asList(
                organizationDTO,
                new OrganizationDTO()
        );

        when(organizationService.listOrganizations(any(ListOrganizationsQuery.class)))
                .thenReturn(organizations);

        mockMvc.perform(get("/api/v1/organizations/search")
                        .param("type", "COMPANY")
                        .param("status", "ACTIVE")
                        .param("searchTerm", "Test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(organizationId.toString())))
                .andExpect(jsonPath("$[0].name", is("Test Organization")))
                .andExpect(jsonPath("$[0].code", is("TEST-ORG")));
    }

    @Test
    void getOrganizationHierarchy_Success() throws Exception {
        OrganizationHierarchyDTO hierarchyDTO = new OrganizationHierarchyDTO();
        hierarchyDTO.setId(organizationId);
        hierarchyDTO.setName("Test Organization");
        hierarchyDTO.setCode("TEST-ORG");
        hierarchyDTO.setChildren(Arrays.asList(new OrganizationHierarchyDTO()));

        when(organizationService.getOrganizationHierarchy(eq(organizationId)))
                .thenReturn(hierarchyDTO);

        mockMvc.perform(get("/api/v1/organizations/{id}/hierarchy", organizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(organizationId.toString())))
                .andExpect(jsonPath("$.name", is("Test Organization")))
                .andExpect(jsonPath("$.code", is("TEST-ORG")))
                .andExpect(jsonPath("$.children", hasSize(1)));
    }

    @Test
    void deleteOrganization_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/organizations/{id}", organizationId))
                .andExpect(status().isNoContent());
    }
}
