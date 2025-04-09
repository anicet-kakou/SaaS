package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.abstracts.AbstractCrudService;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.security.application.command.CreateApiKeyCommand;
import com.devolution.saas.core.security.application.command.UpdateApiKeyCommand;
import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.usecase.*;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des clés API.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyService extends AbstractCrudService<ApiKeyDTO, UUID, CreateApiKeyCommand, UpdateApiKeyCommand> {

    private final CreateApiKey createApiKey;
    private final UpdateApiKey updateApiKey;
    private final GetApiKey getApiKey;
    private final ListApiKeys listApiKeys;
    private final RevokeApiKey revokeApiKey;
    private final DeleteApiKey deleteApiKey;
    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @TenantRequired
    protected ApiKeyDTO executeCreate(CreateApiKeyCommand command) {
        log.debug("Création d'une nouvelle clé API: {}", command.getName());
        return createApiKey.execute(command);
    }

    @Override
    @TenantRequired
    protected ApiKeyDTO executeUpdate(UpdateApiKeyCommand command) {
        log.debug("Mise à jour de la clé API: {}", command.getId());
        return updateApiKey.execute(command);
    }

    @Override
    protected ApiKeyDTO executeGet(UUID id) {
        log.debug("Récupération de la clé API: {}", id);
        return getApiKey.execute(id);
    }

    @Override
    protected List<ApiKeyDTO> executeList() {
        log.debug("Listage de toutes les clés API");
        return listApiKeys.execute();
    }

    @Override
    @TenantRequired
    protected void executeDelete(UUID id) {
        log.debug("Suppression de la clé API: {}", id);
        deleteApiKey.execute(id);
    }

    @Override
    protected String getEntityName() {
        return "clé API";
    }

    /**
     * Liste les clés API par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs de clés API
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_API_KEYS_BY_ORGANIZATION")
    @TenantRequired
    public List<ApiKeyDTO> listApiKeysByOrganization(UUID organizationId) {
        log.debug("Listage des clés API par organisation: {}", organizationId);
        return listApiKeys.executeByOrganization(organizationId);
    }

    /**
     * Liste les clés API par statut.
     *
     * @param status Statut des clés API
     * @return Liste des DTOs de clés API
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_API_KEYS_BY_STATUS")
    public List<ApiKeyDTO> listApiKeysByStatus(ApiKeyStatus status) {
        log.debug("Listage des clés API par statut: {}", status);
        return listApiKeys.executeByStatus(status);
    }

    /**
     * Révoque une clé API.
     *
     * @param id ID de la clé API à révoquer
     * @return DTO de la clé API révoquée
     */
    @Transactional
    @Auditable(action = "REVOKE_API_KEY")
    @TenantRequired
    public ApiKeyDTO revokeApiKey(UUID id) {
        log.debug("Révocation de la clé API: {}", id);
        return revokeApiKey.execute(id);
    }

    /**
     * Crée une nouvelle clé API.
     *
     * @param command Commande de création de clé API
     * @return DTO de la clé API créée
     */
    public ApiKeyDTO createApiKey(CreateApiKeyCommand command) {
        return create(command);
    }

    /**
     * Met à jour une clé API existante.
     *
     * @param command Commande de mise à jour de clé API
     * @return DTO de la clé API mise à jour
     */
    public ApiKeyDTO updateApiKey(UpdateApiKeyCommand command) {
        return update(command);
    }

    /**
     * Récupère une clé API par son ID.
     *
     * @param id ID de la clé API
     * @return DTO de la clé API
     */
    public ApiKeyDTO getApiKey(UUID id) {
        return get(id);
    }

    /**
     * Liste toutes les clés API.
     *
     * @return Liste des DTOs de clés API
     */
    public List<ApiKeyDTO> listApiKeys() {
        return list();
    }

    /**
     * Supprime une clé API.
     *
     * @param id ID de la clé API à supprimer
     */
    public void deleteApiKey(UUID id) {
        delete(id);
    }

    /**
     * Valide une clé API.
     *
     * @param apiKeyValue Valeur de la clé API
     * @return Clé API si valide, sinon Optional vide
     */
    @Transactional
    public Optional<ApiKey> validateApiKey(String apiKeyValue) {
        if (apiKeyValue == null || apiKeyValue.length() <= 8) {
            return Optional.empty();
        }

        String prefix = apiKeyValue.substring(0, 8);
        String key = apiKeyValue;

        Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByPrefixAndKeyHash(prefix, null);
        if (apiKeyOpt.isEmpty()) {
            return Optional.empty();
        }

        ApiKey apiKey = apiKeyOpt.get();

        // Vérification du statut et de l'expiration
        if (!apiKey.isActive()) {
            return Optional.empty();
        }

        // Vérification de la clé
        if (!passwordEncoder.matches(key, apiKey.getKeyHash())) {
            return Optional.empty();
        }

        // Mise à jour de la date de dernière utilisation
        apiKey.updateLastUsed();
        apiKeyRepository.save(apiKey);

        return Optional.of(apiKey);
    }

    /**
     * Nettoie les clés API expirées.
     */
    @Transactional
    @Auditable(action = "CLEAN_EXPIRED_API_KEYS")
    public void cleanExpiredApiKeys() {
        log.debug("Nettoyage des clés API expirées");

        LocalDateTime now = LocalDateTime.now();
        List<ApiKey> expiredKeys = apiKeyRepository.findAllByExpiresAtBefore(now);

        for (ApiKey apiKey : expiredKeys) {
            apiKey.setStatus(ApiKeyStatus.INACTIVE);
            apiKeyRepository.save(apiKey);
        }

        log.debug("{} clés API expirées ont été désactivées", expiredKeys.size());
    }
}
