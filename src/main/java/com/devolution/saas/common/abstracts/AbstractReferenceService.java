package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service abstrait pour les données de référence.
 * Fournit des méthodes communes pour la gestion des données de référence.
 *
 * @param <T> Type du DTO
 * @param <E> Type de l'entité
 */
@Slf4j
public abstract class AbstractReferenceService<T, E extends TenantAwareEntity> {

    /**
     * Récupère toutes les données de référence actives pour une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des données de référence actives
     */
    public abstract List<T> getAllActive(UUID organizationId);

    /**
     * Récupère toutes les données de référence pour une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des données de référence
     */
    public abstract List<T> getAll(UUID organizationId);

    /**
     * Récupère une donnée de référence par son ID.
     *
     * @param id             ID de la donnée de référence
     * @param organizationId ID de l'organisation
     * @return Donnée de référence
     */
    public abstract Optional<T> getById(UUID id, UUID organizationId);

    /**
     * Crée une nouvelle donnée de référence.
     *
     * @param entity         Entité à créer
     * @param organizationId ID de l'organisation
     * @return Donnée de référence créée
     */
    public abstract T create(E entity, UUID organizationId);

    /**
     * Met à jour une donnée de référence existante.
     *
     * @param id             ID de la donnée de référence
     * @param entity         Entité mise à jour
     * @param organizationId ID de l'organisation
     * @return Donnée de référence mise à jour
     */
    public abstract Optional<T> update(UUID id, E entity, UUID organizationId);

    /**
     * Active ou désactive une donnée de référence.
     *
     * @param id             ID de la donnée de référence
     * @param active         Statut d'activation
     * @param organizationId ID de l'organisation
     * @return Donnée de référence mise à jour
     */
    public abstract Optional<T> setActive(UUID id, boolean active, UUID organizationId);

    /**
     * Supprime une donnée de référence.
     *
     * @param id             ID de la donnée de référence
     * @param organizationId ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    public abstract boolean delete(UUID id, UUID organizationId);
}
