package com.devolution.saas.core.security.api;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.security.application.command.CreateApiKeyCommand;
import com.devolution.saas.core.security.application.command.UpdateApiKeyCommand;
import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.service.ApiKeyService;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiKeyController.class)
class ApiKeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApiKeyService apiKeyService;

    @MockBean
    private TenantContextHolder tenantContextHolder;

    private UUID apiKeyId;
    private ApiKeyDTO apiKeyDTO;
    private CreateApiKeyCommand createCommand;
    private UpdateApiKeyCommand updateCommand;

    @BeforeEach
    void setUp() {
        apiKeyId = UUID.randomUUID();

        // Set up tenant context
        when(tenantContextHolder.getCurrentTenant()).thenReturn(UUID.randomUUID());
        when(tenantContextHolder.hasTenant()).thenReturn(true);

        // Set up API key DTO
        apiKeyDTO = new ApiKeyDTO();
        apiKeyDTO.setId(apiKeyId);
        apiKeyDTO.setName("Test API Key");
        apiKeyDTO.setPrefix("12345678");
        apiKeyDTO.setKey("12345678abcdefgh"); // Only returned on creation
        apiKeyDTO.setDescription("Test API key description");
        apiKeyDTO.setStatus(ApiKeyStatus.ACTIVE);
        apiKeyDTO.setExpiresAt(LocalDateTime.now().plusDays(30));
        apiKeyDTO.setPermissions("{}");
        apiKeyDTO.setAllowedIps(new HashSet<>(Arrays.asList("127.0.0.1")));
        apiKeyDTO.setRateLimit(100);
        apiKeyDTO.setActive(true);

        // Set up create command
        createCommand = new CreateApiKeyCommand();
        createCommand.setName("Test API Key");
        createCommand.setDescription("Test API key description");
        createCommand.setExpiresAt(LocalDateTime.now().plusDays(30));
        createCommand.setPermissions("{}");
        createCommand.setAllowedIps(new HashSet<>(Arrays.asList("127.0.0.1")));
        createCommand.setRateLimit(100);

        // Set up update command
        updateCommand = new UpdateApiKeyCommand();
        updateCommand.setId(apiKeyId);
        updateCommand.setName("Updated API Key");
        updateCommand.setDescription("Updated API key description");
        updateCommand.setExpiresAt(LocalDateTime.now().plusDays(60));
        updateCommand.setPermissions("{}");
        updateCommand.setAllowedIps(new HashSet<>(Arrays.asList("127.0.0.1", "192.168.1.1")));
        updateCommand.setRateLimit(200);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createApiKey_Success() throws Exception {
        when(apiKeyService.createApiKey(any(CreateApiKeyCommand.class)))
                .thenReturn(apiKeyDTO);

        mockMvc.perform(post("/api/v1/api-keys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(apiKeyId.toString())))
                .andExpect(jsonPath("$.name", is("Test API Key")))
                .andExpect(jsonPath("$.prefix", is("12345678")))
                .andExpect(jsonPath("$.key", is("12345678abcdefgh")))
                .andExpect(jsonPath("$.description", is("Test API key description")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createApiKey_ValidationError() throws Exception {
        when(apiKeyService.createApiKey(any(CreateApiKeyCommand.class)))
                .thenThrow(new ValidationException("Le nom de la clé API est déjà utilisé"));

        mockMvc.perform(post("/api/v1/api-keys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getApiKey_Success() throws Exception {
        when(apiKeyService.getApiKey(any(UUID.class)))
                .thenReturn(apiKeyDTO);

        mockMvc.perform(get("/api/v1/api-keys/{id}", apiKeyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(apiKeyId.toString())))
                .andExpect(jsonPath("$.name", is("Test API Key")))
                .andExpect(jsonPath("$.prefix", is("12345678")))
                .andExpect(jsonPath("$.description", is("Test API key description")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getApiKey_NotFound() throws Exception {
        when(apiKeyService.getApiKey(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("ApiKey", apiKeyId));

        mockMvc.perform(get("/api/v1/api-keys/{id}", apiKeyId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateApiKey_Success() throws Exception {
        ApiKeyDTO updatedDTO = new ApiKeyDTO();
        updatedDTO.setId(apiKeyId);
        updatedDTO.setName("Updated API Key");
        updatedDTO.setPrefix("12345678");
        updatedDTO.setDescription("Updated API key description");
        updatedDTO.setStatus(ApiKeyStatus.ACTIVE);

        when(apiKeyService.updateApiKey(any(UpdateApiKeyCommand.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/api-keys/{id}", apiKeyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(apiKeyId.toString())))
                .andExpect(jsonPath("$.name", is("Updated API Key")))
                .andExpect(jsonPath("$.description", is("Updated API key description")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listApiKeys_Success() throws Exception {
        List<ApiKeyDTO> apiKeys = Arrays.asList(
                apiKeyDTO,
                new ApiKeyDTO()
        );

        when(apiKeyService.listApiKeys())
                .thenReturn(apiKeys);

        mockMvc.perform(get("/api/v1/api-keys"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(apiKeyId.toString())))
                .andExpect(jsonPath("$[0].name", is("Test API Key")))
                .andExpect(jsonPath("$[0].prefix", is("12345678")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void revokeApiKey_Success() throws Exception {
        ApiKeyDTO revokedDTO = new ApiKeyDTO();
        revokedDTO.setId(apiKeyId);
        revokedDTO.setName("Test API Key");
        revokedDTO.setPrefix("12345678");
        revokedDTO.setStatus(ApiKeyStatus.REVOKED);

        when(apiKeyService.revokeApiKey(any(UUID.class)))
                .thenReturn(revokedDTO);

        mockMvc.perform(put("/api/v1/api-keys/{id}/revoke", apiKeyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(apiKeyId.toString())))
                .andExpect(jsonPath("$.name", is("Test API Key")))
                .andExpect(jsonPath("$.status", is("REVOKED")));

        verify(apiKeyService).revokeApiKey(apiKeyId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteApiKey_Success() throws Exception {
        doNothing().when(apiKeyService).deleteApiKey(any(UUID.class));

        mockMvc.perform(delete("/api/v1/api-keys/{id}", apiKeyId))
                .andExpect(status().isNoContent());

        verify(apiKeyService).deleteApiKey(apiKeyId);
    }
}
