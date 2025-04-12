package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation générique pour la suppression d'entités.
 *
 * @param <E> Type de l'entité de domaine
 */
@Slf4j
public abstract class AbstractDeleteUseCase<E> {

    /**
     * Récupère une entité par son ID.
     *
     * @param id ID de l'entité
     * @return Entité récupérée
     */
    protected abstract E findById(UUID id);

    /**
     * Supprime une entité.
     *
     * @param entity Entité à supprimer
     */
    protected abstract void deleteEntity(E entity);

    /**
     * Vérifie si l'entité est supprimable.
     *
     * @param entity Entité à vérifier
     * @return true si l'entité est supprimable, false sinon
     */
    protected abstract boolean isEntityDeletable(E entity);

    /**
     * Retourne le nom de l'entité pour les logs et les messages d'erreur.
     *
     * @return Nom de l'entité
     */
    protected abstract String getEntityName();

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID de l'entité à supprimer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void execute(UUID id) {
        log.debug("Exécution du cas d'utilisation de suppression de {} avec ID: {}", getEntityName(), id);

        // Récupération de l'entité
        E entity;
        try {
            entity = findById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException(getEntityName(), id);
        }

        // Vérification que l'entité est supprimable
        if (!isEntityDeletable(entity)) {
            throw new BusinessException(
                    getEntityName() + ".not.deletable",
                    "Ce " + getEntityName() + " ne peut pas être supprimé"
            );
        }

        // Suppression de l'entité
        deleteEntity(entity);
    }
}
