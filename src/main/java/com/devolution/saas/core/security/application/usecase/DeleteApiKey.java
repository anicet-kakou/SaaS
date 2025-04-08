package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour supprimer une clé API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteApiKey {

    private final ApiKeyRepository apiKeyRepository;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID de la clé API à supprimer
     */
    @Transactional
    public void execute(UUID id) {
        log.debug("Suppression de la clé API: {}", id);

        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", id));

        apiKeyRepository.delete(apiKey);
    }
}
