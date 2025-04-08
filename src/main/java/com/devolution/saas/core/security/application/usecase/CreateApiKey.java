package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.core.security.application.command.CreateApiKeyCommand;
import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.mapper.ApiKeyMapper;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Cas d'utilisation pour la création d'une clé API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateApiKey {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de création de clé API
     * @return DTO de la clé API créée
     */
    @Transactional
    public ApiKeyDTO execute(CreateApiKeyCommand command) {
        log.debug("Création d'une nouvelle clé API: {}", command.getName());

        // Vérification de l'unicité du nom de la clé API dans l'organisation
        if (apiKeyRepository.existsByNameAndOrganizationId(command.getName(), command.getOrganizationId())) {
            throw new BusinessException("api.key.name.duplicate", "Une clé API avec ce nom existe déjà dans cette organisation");
        }

        // Génération de la clé API
        String fullKey = generateApiKey();
        String prefix = fullKey.substring(0, 8);
        String keyHash = passwordEncoder.encode(fullKey);

        // Création de la clé API
        ApiKey apiKey = new ApiKey();
        apiKey.setName(command.getName());
        apiKey.setPrefix(prefix);
        apiKey.setKeyHash(keyHash);
        apiKey.setOrganizationId(command.getOrganizationId());
        apiKey.setStatus(ApiKeyStatus.ACTIVE);
        apiKey.setExpiresAt(command.getExpiresAt());
        apiKey.setPermissions(command.getPermissions());
        apiKey.setAllowedIps(command.getAllowedIps());
        apiKey.setRateLimit(command.getRateLimit());
        apiKey.setDescription(command.getDescription());

        // Sauvegarde de la clé API
        apiKey = apiKeyRepository.save(apiKey);

        return apiKeyMapper.toDTOWithKey(apiKey, fullKey);
    }

    /**
     * Génère une clé API aléatoire.
     *
     * @return Clé API générée
     */
    private String generateApiKey() {
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
