package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.command.UpdateApiKeyCommand;
import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.mapper.ApiKeyMapper;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'utilisation pour la mise à jour d'une clé API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateApiKey {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de mise à jour de clé API
     * @return DTO de la clé API mise à jour
     */
    @Transactional
    public ApiKeyDTO execute(UpdateApiKeyCommand command) {
        log.debug("Mise à jour de la clé API: {}", command.getId());

        // Récupération de la clé API
        ApiKey apiKey = apiKeyRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", command.getId()));

        // Vérification de l'unicité du nom de la clé API dans l'organisation
        if (!apiKey.getName().equals(command.getName()) &&
                apiKeyRepository.existsByNameAndOrganizationId(command.getName(), command.getOrganizationId())) {
            throw new BusinessException("api.key.name.duplicate", "Une clé API avec ce nom existe déjà dans cette organisation");
        }

        // Mise à jour des propriétés
        apiKey.setName(command.getName());
        apiKey.setOrganizationId(command.getOrganizationId());
        apiKey.setExpiresAt(command.getExpiresAt());
        apiKey.setPermissions(command.getPermissions());
        apiKey.setAllowedIps(command.getAllowedIps());
        apiKey.setRateLimit(command.getRateLimit());
        apiKey.setDescription(command.getDescription());

        // Sauvegarde de la clé API
        apiKey = apiKeyRepository.save(apiKey);

        return apiKeyMapper.toDTO(apiKey);
    }
}
