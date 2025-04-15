package com.devolution.saas.core.security.application.mapper;

import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.domain.model.ApiKey;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités ApiKey et les DTOs ApiKeyDTO.
 */
@Component
public class ApiKeyMapper {

    /**
     * Convertit une entité ApiKey en DTO.
     *
     * @param apiKey Entité ApiKey
     * @return DTO ApiKeyDTO
     */
    public ApiKeyDTO toDTO(ApiKey apiKey) {
        if (apiKey == null) {
            return null;
        }

        return ApiKeyDTO.builder()
                .id(apiKey.getId())
                .name(apiKey.getName())
                .prefix(apiKey.getPrefix())
                .organizationId(apiKey.getOrganizationId())
                .status(apiKey.getStatus())
                .expiresAt(apiKey.getExpiresAt())
                .lastUsedAt(apiKey.getLastUsedAt())
                .permissions(apiKey.getPermissions())
                .allowedIps(apiKey.getAllowedIps())
                .rateLimit(apiKey.getRateLimit())
                .description(apiKey.getDescription())
                .createdAt(apiKey.getCreatedAt())
                .updatedAt(apiKey.getUpdatedAt())
                .active(apiKey.isActive())
                .build();
    }

    /**
     * Convertit une entité ApiKey en DTO avec la clé complète.
     *
     * @param apiKey  Entité ApiKey
     * @param fullKey Clé complète
     * @return DTO ApiKeyDTO
     */
    public ApiKeyDTO toDTOWithKey(ApiKey apiKey, String fullKey) {
        if (apiKey == null) {
            return null;
        }

        return ApiKeyDTO.builder()
                .id(apiKey.getId())
                .name(apiKey.getName())
                .prefix(apiKey.getPrefix())
                .key(fullKey) // Set the full key here
                .organizationId(apiKey.getOrganizationId())
                .status(apiKey.getStatus())
                .expiresAt(apiKey.getExpiresAt())
                .lastUsedAt(apiKey.getLastUsedAt())
                .permissions(apiKey.getPermissions())
                .allowedIps(apiKey.getAllowedIps())
                .rateLimit(apiKey.getRateLimit())
                .description(apiKey.getDescription())
                .createdAt(apiKey.getCreatedAt())
                .updatedAt(apiKey.getUpdatedAt())
                .active(apiKey.isActive())
                .build();
    }
}
