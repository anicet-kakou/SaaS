package com.devolution.saas.core.security.domain.repository;

import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour les opérations sur les clés API.
 */
public interface ApiKeyRepository {

    /**
     * Enregistre une clé API.
     *
     * @param apiKey Clé API à enregistrer
     * @return Clé API enregistrée
     */
    ApiKey save(ApiKey apiKey);

    /**
     * Trouve une clé API par son ID.
     *
     * @param id ID de la clé API
     * @return Clé API trouvée ou Optional vide
     */
    Optional<ApiKey> findById(UUID id);

    /**
     * Trouve une clé API par son préfixe et son hash.
     *
     * @param prefix  Préfixe de la clé API
     * @param keyHash Hash de la clé API
     * @return Clé API trouvée ou Optional vide
     */
    Optional<ApiKey> findByPrefixAndKeyHash(String prefix, String keyHash);

    /**
     * Trouve toutes les clés API.
     *
     * @return Liste des clés API
     */
    List<ApiKey> findAll();

    /**
     * Trouve toutes les clés API par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des clés API
     */
    List<ApiKey> findAllByOrganizationId(UUID organizationId);

    /**
     * Trouve toutes les clés API par statut.
     *
     * @param status Statut des clés API
     * @return Liste des clés API
     */
    List<ApiKey> findAllByStatus(ApiKeyStatus status);

    /**
     * Trouve toutes les clés API expirées.
     *
     * @param expiryDate Date d'expiration
     * @return Liste des clés API
     */
    List<ApiKey> findAllByExpiresAtBefore(LocalDateTime expiryDate);

    /**
     * Vérifie si une clé API existe par son nom et l'ID de l'organisation.
     *
     * @param name           Nom de la clé API
     * @param organizationId ID de l'organisation
     * @return true si la clé API existe, false sinon
     */
    boolean existsByNameAndOrganizationId(String name, UUID organizationId);

    /**
     * Supprime une clé API.
     *
     * @param apiKey Clé API à supprimer
     */
    void delete(ApiKey apiKey);

    /**
     * Compte le nombre de clés API.
     *
     * @return Nombre de clés API
     */
    long count();
}
