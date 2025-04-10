package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.security.application.command.CreateApiKeyCommand;
import com.devolution.saas.core.security.application.command.UpdateApiKeyCommand;
import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.usecase.*;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import com.devolution.saas.core.security.infrastructure.service.MockTenantContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    @Mock
    private CreateApiKey createApiKey;

    @Mock
    private UpdateApiKey updateApiKey;

    @Mock
    private GetApiKey getApiKey;

    @Mock
    private ListApiKeys listApiKeys;

    @Mock
    private RevokeApiKey revokeApiKey;

    @Mock
    private DeleteApiKey deleteApiKey;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private MockTenantContextHolder tenantContextHolder = new MockTenantContextHolder();

    @InjectMocks
    private ApiKeyService apiKeyService;

    private UUID apiKeyId;
    private CreateApiKeyCommand createCommand;
    private UpdateApiKeyCommand updateCommand;
    private ApiKeyDTO apiKeyDTO;
    private ApiKey apiKey;

    @BeforeEach
    void setUp() {
        apiKeyId = UUID.randomUUID();

        // Set up tenant context
        tenantContextHolder.setCurrentTenant(UUID.randomUUID());

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

        // Set up API key DTO
        apiKeyDTO = new ApiKeyDTO();
        apiKeyDTO.setId(apiKeyId);
        apiKeyDTO.setName("Test API Key");
        apiKeyDTO.setPrefix("12345678");
        apiKeyDTO.setDescription("Test API key description");
        apiKeyDTO.setStatus(ApiKeyStatus.ACTIVE);
        apiKeyDTO.setExpiresAt(LocalDateTime.now().plusDays(30));
        apiKeyDTO.setPermissions("{}");
        apiKeyDTO.setAllowedIps(new HashSet<>(Arrays.asList("127.0.0.1")));
        apiKeyDTO.setRateLimit(100);
        apiKeyDTO.setActive(true);

        // Set up API key entity
        apiKey = new ApiKey();
        apiKey.setId(apiKeyId);
        apiKey.setName("Test API Key");
        apiKey.setPrefix("12345678");
        apiKey.setKeyHash("hashedKey");
        apiKey.setStatus(ApiKeyStatus.ACTIVE);
        apiKey.setExpiresAt(LocalDateTime.now().plusDays(30));
        apiKey.setPermissions("{}");
        apiKey.setAllowedIps(new HashSet<>(Arrays.asList("127.0.0.1")));
        apiKey.setRateLimit(100);
    }

    @Test
    void createApiKey_Success() {
        // Given
        when(createApiKey.execute(any(CreateApiKeyCommand.class))).thenReturn(apiKeyDTO);

        // When
        ApiKeyDTO result = apiKeyService.createApiKey(createCommand);

        // Then
        assertNotNull(result);
        assertEquals(apiKeyId, result.getId());
        assertEquals("Test API Key", result.getName());
        assertEquals("12345678", result.getPrefix());
        verify(createApiKey).execute(createCommand);
    }

    @Test
    void createApiKey_ValidationError() {
        // Given
        when(createApiKey.execute(any(CreateApiKeyCommand.class)))
                .thenThrow(new ValidationException("Le nom de la clé API est déjà utilisé"));

        // When & Then
        assertThrows(ValidationException.class, () -> {
            apiKeyService.createApiKey(createCommand);
        });

        verify(createApiKey).execute(createCommand);
    }

    @Test
    void updateApiKey_Success() {
        // Given
        ApiKeyDTO updatedDTO = new ApiKeyDTO();
        updatedDTO.setId(apiKeyId);
        updatedDTO.setName("Updated API Key");
        updatedDTO.setPrefix("12345678");
        updatedDTO.setDescription("Updated API key description");

        when(updateApiKey.execute(any(UpdateApiKeyCommand.class))).thenReturn(updatedDTO);

        // When
        ApiKeyDTO result = apiKeyService.updateApiKey(updateCommand);

        // Then
        assertNotNull(result);
        assertEquals(apiKeyId, result.getId());
        assertEquals("Updated API Key", result.getName());
        assertEquals("Updated API key description", result.getDescription());
        verify(updateApiKey).execute(updateCommand);
    }

    @Test
    void getApiKey_Success() {
        // Given
        when(getApiKey.execute(any(UUID.class))).thenReturn(apiKeyDTO);

        // When
        ApiKeyDTO result = apiKeyService.getApiKey(apiKeyId);

        // Then
        assertNotNull(result);
        assertEquals(apiKeyId, result.getId());
        assertEquals("Test API Key", result.getName());
        assertEquals("12345678", result.getPrefix());
        verify(getApiKey).execute(apiKeyId);
    }

    @Test
    void getApiKey_NotFound() {
        // Given
        when(getApiKey.execute(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("ApiKey", apiKeyId));

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            apiKeyService.getApiKey(apiKeyId);
        });

        verify(getApiKey).execute(apiKeyId);
    }

    @Test
    void listApiKeys_Success() {
        // Given
        List<ApiKeyDTO> expectedApiKeys = Arrays.asList(apiKeyDTO, new ApiKeyDTO());
        when(listApiKeys.execute()).thenReturn(expectedApiKeys);

        // When
        List<ApiKeyDTO> result = apiKeyService.listApiKeys();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listApiKeys).execute();
    }

    @Test
    void listApiKeysByOrganization_Success() {
        // Given
        UUID organizationId = UUID.randomUUID();
        List<ApiKeyDTO> expectedApiKeys = Arrays.asList(apiKeyDTO, new ApiKeyDTO());
        when(listApiKeys.executeByOrganization(any(UUID.class))).thenReturn(expectedApiKeys);

        // When
        List<ApiKeyDTO> result = apiKeyService.listApiKeysByOrganization(organizationId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listApiKeys).executeByOrganization(organizationId);
    }

    @Test
    void listApiKeysByStatus_Success() {
        // Given
        ApiKeyStatus status = ApiKeyStatus.ACTIVE;
        List<ApiKeyDTO> expectedApiKeys = Arrays.asList(apiKeyDTO, new ApiKeyDTO());
        when(listApiKeys.executeByStatus(any(ApiKeyStatus.class))).thenReturn(expectedApiKeys);

        // When
        List<ApiKeyDTO> result = apiKeyService.listApiKeysByStatus(status);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listApiKeys).executeByStatus(status);
    }

    @Test
    void revokeApiKey_Success() {
        // Given
        ApiKeyDTO revokedDTO = new ApiKeyDTO();
        revokedDTO.setId(apiKeyId);
        revokedDTO.setName("Test API Key");
        revokedDTO.setStatus(ApiKeyStatus.REVOKED);

        when(revokeApiKey.execute(any(UUID.class))).thenReturn(revokedDTO);

        // When
        ApiKeyDTO result = apiKeyService.revokeApiKey(apiKeyId);

        // Then
        assertNotNull(result);
        assertEquals(apiKeyId, result.getId());
        assertEquals(ApiKeyStatus.REVOKED, result.getStatus());
        verify(revokeApiKey).execute(apiKeyId);
    }

    @Test
    void deleteApiKey_Success() {
        // Given
        doNothing().when(deleteApiKey).execute(any(UUID.class));

        // When
        apiKeyService.deleteApiKey(apiKeyId);

        // Then
        verify(deleteApiKey).execute(apiKeyId);
    }

    @Test
    void validateApiKey_Success() {
        // Given
        String apiKeyValue = "12345678abcdefgh";
        when(apiKeyRepository.findByPrefixAndKeyHash(anyString(), any())).thenReturn(Optional.of(apiKey));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // When
        Optional<ApiKey> result = apiKeyService.validateApiKey(apiKeyValue);

        // Then
        assertTrue(result.isPresent());
        assertEquals(apiKeyId, result.get().getId());
        verify(apiKeyRepository).findByPrefixAndKeyHash(eq("12345678"), eq(null));
        verify(passwordEncoder).matches(eq(apiKeyValue), eq("hashedKey"));
        verify(apiKeyRepository).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_InvalidKey() {
        // Given
        String apiKeyValue = "12345678abcdefgh";
        when(apiKeyRepository.findByPrefixAndKeyHash(anyString(), any())).thenReturn(Optional.of(apiKey));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When
        Optional<ApiKey> result = apiKeyService.validateApiKey(apiKeyValue);

        // Then
        assertFalse(result.isPresent());
        verify(apiKeyRepository).findByPrefixAndKeyHash(eq("12345678"), eq(null));
        verify(passwordEncoder).matches(eq(apiKeyValue), eq("hashedKey"));
        verify(apiKeyRepository, never()).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_KeyNotFound() {
        // Given
        String apiKeyValue = "12345678abcdefgh";
        when(apiKeyRepository.findByPrefixAndKeyHash(anyString(), any())).thenReturn(Optional.empty());

        // When
        Optional<ApiKey> result = apiKeyService.validateApiKey(apiKeyValue);

        // Then
        assertFalse(result.isPresent());
        verify(apiKeyRepository).findByPrefixAndKeyHash(eq("12345678"), eq(null));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(apiKeyRepository, never()).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_InactiveKey() {
        // Given
        String apiKeyValue = "12345678abcdefgh";
        apiKey.setStatus(ApiKeyStatus.INACTIVE);
        when(apiKeyRepository.findByPrefixAndKeyHash(anyString(), any())).thenReturn(Optional.of(apiKey));

        // When
        Optional<ApiKey> result = apiKeyService.validateApiKey(apiKeyValue);

        // Then
        assertFalse(result.isPresent());
        verify(apiKeyRepository).findByPrefixAndKeyHash(eq("12345678"), eq(null));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(apiKeyRepository, never()).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_ExpiredKey() {
        // Given
        String apiKeyValue = "12345678abcdefgh";
        apiKey.setExpiresAt(LocalDateTime.now().minusDays(1));
        when(apiKeyRepository.findByPrefixAndKeyHash(anyString(), any())).thenReturn(Optional.of(apiKey));

        // When
        Optional<ApiKey> result = apiKeyService.validateApiKey(apiKeyValue);

        // Then
        assertFalse(result.isPresent());
        verify(apiKeyRepository).findByPrefixAndKeyHash(eq("12345678"), eq(null));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(apiKeyRepository, never()).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_NullKey() {
        // When
        Optional<ApiKey> result = apiKeyService.validateApiKey(null);

        // Then
        assertFalse(result.isPresent());
        verify(apiKeyRepository, never()).findByPrefixAndKeyHash(anyString(), any());
    }

    @Test
    void validateApiKey_ShortKey() {
        // When
        Optional<ApiKey> result = apiKeyService.validateApiKey("short");

        // Then
        assertFalse(result.isPresent());
        verify(apiKeyRepository, never()).findByPrefixAndKeyHash(anyString(), any());
    }

    @Test
    void cleanExpiredApiKeys_Success() {
        // Given
        List<ApiKey> expiredKeys = Arrays.asList(apiKey, new ApiKey());
        when(apiKeyRepository.findAllByExpiresAtBefore(any(LocalDateTime.class))).thenReturn(expiredKeys);

        // When
        apiKeyService.cleanExpiredApiKeys();

        // Then
        verify(apiKeyRepository).findAllByExpiresAtBefore(any(LocalDateTime.class));
        verify(apiKeyRepository, times(2)).save(any(ApiKey.class));
        assertEquals(ApiKeyStatus.INACTIVE, apiKey.getStatus());
    }
}
