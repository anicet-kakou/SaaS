package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service générique pour les opérations CRUD.
 * Implémente les standards de transaction pour les opérations CRUD.
 *
 * @param <T>  Type de l'entité DTO
 * @param <ID> Type de l'identifiant
 * @param <C>  Type de la commande de création
 * @param <U>  Type de la commande de mise à jour
 */
public abstract class AbstractCrudService<T, ID, C, U> {
    // Logger protégé pour être accessible aux classes filles
    protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AbstractCrudService.class);

    /**
     * Exécute la création d'une entité.
     *
     * @param command Commande de création
     * @return Entité créée
     */
    protected abstract T executeCreate(C command);

    /**
     * Exécute la mise à jour d'une entité.
     *
     * @param command Commande de mise à jour
     * @return Entité mise à jour
     */
    protected abstract T executeUpdate(U command);

    /**
     * Exécute la récupération d'une entité.
     *
     * @param id ID de l'entité
     * @return Entité récupérée
     */
    protected abstract T executeGet(ID id);

    /**
     * Exécute la liste des entités.
     *
     * @return Liste des entités
     */
    protected abstract List<T> executeList();

    /**
     * Exécute la suppression d'une entité.
     *
     * @param id ID de l'entité
     */
    protected abstract void executeDelete(ID id);

    /**
     * Retourne le nom de l'entité pour les logs.
     *
     * @return Nom de l'entité
     */
    protected abstract String getEntityName();

    /**
     * Crée une nouvelle entité.
     *
     * @param command Commande de création
     * @return Entité créée
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "CREATE")
    @TenantRequired
    public T create(C command) {
        log.debug("Création d'un nouveau {}: {}", getEntityName(), command);
        return executeCreate(command);
    }

    /**
     * Met à jour une entité existante.
     *
     * @param command Commande de mise à jour
     * @return Entité mise à jour
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "UPDATE")
    @TenantRequired
    public T update(U command) {
        log.debug("Mise à jour du {}: {}", getEntityName(), command);
        return executeUpdate(command);
    }

    /**
     * Récupère une entité par son ID.
     *
     * @param id ID de l'entité
     * @return Entité récupérée
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "GET")
    public T get(ID id) {
        log.debug("Récupération du {}: {}", getEntityName(), id);
        return executeGet(id);
    }

    /**
     * Récupère une entité par son ID, retournant un Optional.
     *
     * @param id ID de l'entité
     * @return Optional contenant l'entité récupérée, ou vide si non trouvée
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "GET")
    public Optional<T> getOptional(ID id) {
        log.debug("Récupération optionnelle du {}: {}", getEntityName(), id);
        return executeGetOptional(id);
    }

    /**
     * Exécute la récupération d'une entité, retournant un Optional.
     *
     * @param id ID de l'entité
     * @return Optional contenant l'entité récupérée, ou vide si non trouvée
     */
    protected Optional<T> executeGetOptional(ID id) {
        try {
            return Optional.ofNullable(executeGet(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Liste toutes les entités.
     *
     * @return Liste des entités
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Auditable(action = "LIST")
    public List<T> list() {
        log.debug("Listage des {}", getEntityName());
        return executeList();
    }

    /**
     * Supprime une entité.
     *
     * @param id ID de l'entité
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Auditable(action = "DELETE")
    @TenantRequired
    public void delete(ID id) {
        log.debug("Suppression du {}: {}", getEntityName(), id);
        executeDelete(id);
    }
}
