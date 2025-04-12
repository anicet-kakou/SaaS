package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service abstrait de base pour les opérations CRUD.
 * Fournit des configurations de transaction standardisées pour les opérations CRUD.
 *
 * @param <T> Type de l'entité
 * @param <D> Type du DTO
 * @param <C> Type de la commande de création
 * @param <U> Type de la commande de mise à jour
 */
@Slf4j
public abstract class AbstractCrudServiceBase<T, D, C, U> {

    /**
     * Crée une nouvelle entité.
     *
     * @param command Commande de création
     * @return DTO de l'entité créée
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "CREATE")
    @TenantRequired
    public D create(C command) {
        log.debug("Création d'un nouveau {}: {}", getEntityName(), command);
        return executeCreate(command);
    }

    /**
     * Met à jour une entité existante.
     *
     * @param id      ID de l'entité
     * @param command Commande de mise à jour
     * @return DTO de l'entité mise à jour
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "UPDATE")
    @TenantRequired
    public D update(UUID id, U command) {
        log.debug("Mise à jour du {}: {} avec {}", getEntityName(), id, command);
        return executeUpdate(id, command);
    }

    /**
     * Récupère une entité par son ID.
     *
     * @param id ID de l'entité
     * @return DTO de l'entité
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "GET")
    @TenantRequired
    public Optional<D> getById(UUID id) {
        log.debug("Récupération du {} avec ID: {}", getEntityName(), id);
        return executeGetById(id);
    }

    /**
     * Liste toutes les entités.
     *
     * @return Liste des DTOs des entités
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "LIST")
    @TenantRequired
    public List<D> getAll() {
        log.debug("Listage de tous les {}", getEntityName());
        return executeGetAll();
    }

    /**
     * Supprime une entité.
     *
     * @param id ID de l'entité
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "DELETE")
    @TenantRequired
    public void delete(UUID id) {
        log.debug("Suppression du {} avec ID: {}", getEntityName(), id);
        executeDelete(id);
    }

    /**
     * Exécute la création d'une entité.
     *
     * @param command Commande de création
     * @return DTO de l'entité créée
     */
    protected abstract D executeCreate(C command);

    /**
     * Exécute la mise à jour d'une entité.
     *
     * @param id      ID de l'entité
     * @param command Commande de mise à jour
     * @return DTO de l'entité mise à jour
     */
    protected abstract D executeUpdate(UUID id, U command);

    /**
     * Exécute la récupération d'une entité par son ID.
     *
     * @param id ID de l'entité
     * @return DTO de l'entité
     */
    protected abstract Optional<D> executeGetById(UUID id);

    /**
     * Exécute la liste de toutes les entités.
     *
     * @return Liste des DTOs des entités
     */
    protected abstract List<D> executeGetAll();

    /**
     * Exécute la suppression d'une entité.
     *
     * @param id ID de l'entité
     */
    protected abstract void executeDelete(UUID id);

    /**
     * Retourne le nom de l'entité pour les logs.
     *
     * @return Nom de l'entité
     */
    protected abstract String getEntityName();
}
