package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.mapper.ApiKeyMapper;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour récupérer une clé API par son ID.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetApiKey {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID de la clé API
     * @return DTO de la clé API
     */
    @Transactional(readOnly = true)
    public ApiKeyDTO execute(UUID id) {
        log.debug("Récupération de la clé API: {}", id);

        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", id));

        return apiKeyMapper.toDTO(apiKey);
    }
}
