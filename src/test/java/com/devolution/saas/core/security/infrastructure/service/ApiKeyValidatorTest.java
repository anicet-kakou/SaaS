package com.devolution.saas.core.security.infrastructure.service;

import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyValidatorTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ApiKeyValidator apiKeyValidator;

    private ApiKey apiKey;
    private String apiKeyValue;

    @BeforeEach
    void setUp() {
        apiKeyValue = "abcdefgh12345678";

        apiKey = new ApiKey();
        apiKey.setId(UUID.randomUUID());
        apiKey.setName("Test API Key");
        apiKey.setPrefix("abcdefgh");
        apiKey.setKeyHash("hashedKey");
        apiKey.setStatus(ApiKeyStatus.ACTIVE);
        apiKey.setOrganizationId(UUID.randomUUID());
        apiKey.setAllowedIps(new HashSet<>());
    }

    @Test
    void validateApiKey_ShouldReturnApiKey_WhenValid() {
        // Given
        when(apiKeyRepository.findByPrefixAndStatus("abcdefgh", null)).thenReturn(Optional.of(apiKey));
        when(passwordEncoder.matches(apiKeyValue, "hashedKey")).thenReturn(true);

        // When
        Optional<ApiKey> result = apiKeyValidator.validateApiKey(apiKeyValue);

        // Then
        assertTrue(result.isPresent());
        assertEquals(apiKey.getId(), result.get().getId());
        verify(apiKeyRepository).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_ShouldReturnEmpty_WhenApiKeyTooShort() {
        // Given
        String shortKey = "abc";

        // When
        Optional<ApiKey> result = apiKeyValidator.validateApiKey(shortKey);

        // Then
        assertTrue(result.isEmpty());
        verify(apiKeyRepository, never()).findByPrefixAndStatus(anyString(), any());
    }

    @Test
    void validateApiKey_ShouldReturnEmpty_WhenApiKeyNotFound() {
        // Given
        when(apiKeyRepository.findByPrefixAndStatus("abcdefgh", null)).thenReturn(Optional.empty());

        // When
        Optional<ApiKey> result = apiKeyValidator.validateApiKey(apiKeyValue);

        // Then
        assertTrue(result.isEmpty());
        verify(apiKeyRepository, never()).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_ShouldReturnEmpty_WhenApiKeyInactive() {
        // Given
        apiKey.setStatus(ApiKeyStatus.INACTIVE);
        when(apiKeyRepository.findByPrefixAndStatus("abcdefgh", null)).thenReturn(Optional.of(apiKey));

        // When
        Optional<ApiKey> result = apiKeyValidator.validateApiKey(apiKeyValue);

        // Then
        assertTrue(result.isEmpty());
        verify(apiKeyRepository, never()).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_ShouldReturnEmpty_WhenApiKeyExpired() {
        // Given
        apiKey.setExpiresAt(LocalDateTime.now().minusDays(1));
        when(apiKeyRepository.findByPrefixAndStatus("abcdefgh", null)).thenReturn(Optional.of(apiKey));

        // When
        Optional<ApiKey> result = apiKeyValidator.validateApiKey(apiKeyValue);

        // Then
        assertTrue(result.isEmpty());
        verify(apiKeyRepository, never()).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_ShouldReturnEmpty_WhenKeyHashDoesNotMatch() {
        // Given
        when(apiKeyRepository.findByPrefixAndStatus("abcdefgh", null)).thenReturn(Optional.of(apiKey));
        when(passwordEncoder.matches(apiKeyValue, "hashedKey")).thenReturn(false);

        // When
        Optional<ApiKey> result = apiKeyValidator.validateApiKey(apiKeyValue);

        // Then
        assertTrue(result.isEmpty());
        verify(apiKeyRepository, never()).save(any(ApiKey.class));
    }

    @Test
    void isIpAllowed_ShouldReturnTrue_WhenNoIpRestrictions() {
        // Given
        apiKey.setAllowedIps(new HashSet<>());

        // When
        boolean result = apiKeyValidator.isIpAllowed(apiKey, "192.168.1.1");

        // Then
        assertTrue(result);
    }

    @Test
    void isIpAllowed_ShouldReturnTrue_WhenIpInAllowedList() {
        // Given
        HashSet<String> allowedIps = new HashSet<>();
        allowedIps.add("192.168.1.1");
        apiKey.setAllowedIps(allowedIps);

        // When
        boolean result = apiKeyValidator.isIpAllowed(apiKey, "192.168.1.1");

        // Then
        assertTrue(result);
    }

    @Test
    void isIpAllowed_ShouldReturnFalse_WhenIpNotInAllowedList() {
        // Given
        HashSet<String> allowedIps = new HashSet<>();
        allowedIps.add("192.168.1.1");
        apiKey.setAllowedIps(allowedIps);

        // When
        boolean result = apiKeyValidator.isIpAllowed(apiKey, "192.168.1.2");

        // Then
        assertFalse(result);
    }
}
