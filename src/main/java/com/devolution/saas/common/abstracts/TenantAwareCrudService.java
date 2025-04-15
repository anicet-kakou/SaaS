package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.common.domain.model.TenantAwareEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface pour les services CRUD tenant-aware.
 * Définit les méthodes standard pour les opérations CRUD avec gestion des tenants/organisations.
 *
 * @param <T> Type du DTO
 * @param <E> Type de l'entité
 */
public interface TenantAwareCrudService<T, E extends TenantAwareEntity> {

    /**
     * Récupère toutes les entités actives pour une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des entités actives
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "LIST_ACTIVE")
    @TenantRequired
    List<T> getAllActive(UUID organizationId);

    /**
     * Récupère toutes les entités pour une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des entités
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "LIST_ALL")
    @TenantRequired
    List<T> getAll(UUID organizationId);

    /**
     * Récupère une entité par son ID pour une organisation.
     *
     * @param id             ID de l'entité
     * @param organizationId ID de l'organisation
     * @return Entité récupérée
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "GET_BY_ID")
    @TenantRequired
    Optional<T> getById(UUID id, UUID organizationId);

    /**
     * Récupère une entité par son code pour une organisation.
     *
     * @param code           Code de l'entité
     * @param organizationId ID de l'organisation
     * @return Entité récupérée
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "GET_BY_CODE")
    @TenantRequired
    Optional<T> getByCode(String code, UUID organizationId);

    /**
     * Crée une nouvelle entité pour une organisation.
     *
     * @param entity         Entité à créer
     * @param organizationId ID de l'organisation
     * @return Entité créée
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "CREATE")
    @TenantRequired
    T create(E entity, UUID organizationId);

    /**
     * Met à jour une entité existante pour une organisation.
     *
     * @param id             ID de l'entité
     * @param entity         Entité mise à jour
     * @param organizationId ID de l'organisation
     * @return Entité mise à jour
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "UPDATE")
    @TenantRequired
    Optional<T> update(UUID id, E entity, UUID organizationId);

    /**
     * Active ou désactive une entité pour une organisation.
     *
     * @param id             ID de l'entité
     * @param active         Statut d'activation
     * @param organizationId ID de l'organisation
     * @return Entité mise à jour
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "SET_ACTIVE")
    @TenantRequired
    Optional<T> setActive(UUID id, boolean active, UUID organizationId);

    /**
     * Supprime une entité pour une organisation.
     *
     * @param id             ID de l'entité
     * @param organizationId ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "DELETE")
    @TenantRequired
    boolean delete(UUID id, UUID organizationId);

    /**
     * Retourne le nom de l'entité pour les logs.
     *
     * @return Nom de l'entité
     */
    String getEntityName();
}
