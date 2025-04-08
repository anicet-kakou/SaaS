package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.mapper.ApiKeyMapper;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cas d'utilisation pour lister les clés API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ListApiKeys {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;

    /**
     * Exécute le cas d'utilisation pour lister toutes les clés API.
     *
     * @return Liste des DTOs de clés API
     */
    @Transactional(readOnly = true)
    public List<ApiKeyDTO> execute() {
        log.debug("Listage de toutes les clés API");

        List<ApiKey> apiKeys = apiKeyRepository.findAll();
        return apiKeys.stream()
                .map(apiKeyMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Exécute le cas d'utilisation pour lister les clés API par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs de clés API
     */
    @Transactional(readOnly = true)
    public List<ApiKeyDTO> executeByOrganization(UUID organizationId) {
        log.debug("Listage des clés API par organisation: {}", organizationId);

        List<ApiKey> apiKeys = apiKeyRepository.findAllByOrganizationId(organizationId);
        return apiKeys.stream()
                .map(apiKeyMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Exécute le cas d'utilisation pour lister les clés API par statut.
     *
     * @param status Statut des clés API
     * @return Liste des DTOs de clés API
     */
    @Transactional(readOnly = true)
    public List<ApiKeyDTO> executeByStatus(ApiKeyStatus status) {
        log.debug("Listage des clés API par statut: {}", status);

        List<ApiKey> apiKeys = apiKeyRepository.findAllByStatus(status);
        return apiKeys.stream()
                .map(apiKeyMapper::toDTO)
                .collect(Collectors.toList());
    }
}
