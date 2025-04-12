package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'utilisation générique pour la mise à jour d'entités.
 *
 * @param <T>  Type de l'entité DTO
 * @param <U>  Type de la commande de mise à jour
 * @param <E>  Type de l'entité de domaine
 * @param <ID> Type de l'identifiant
 */
@Slf4j
public abstract class AbstractUpdateUseCase<T, U, E, ID> {

    /**
     * Récupère une entité par son ID.
     *
     * @param id ID de l'entité
     * @return Entité récupérée
     * @throws ResourceNotFoundException si l'entité n'existe pas
     */
    protected abstract E getEntity(ID id);

    /**
     * Met à jour une entité à partir d'une commande.
     *
     * @param entity  Entité à mettre à jour
     * @param command Commande de mise à jour
     * @return Entité mise à jour
     */
    protected abstract E updateEntity(E entity, U command);

    /**
     * Convertit une entité de domaine en DTO.
     *
     * @param entity Entité de domaine
     * @return DTO
     */
    protected abstract T toDto(E entity);

    /**
     * Vérifie si l'entité est modifiable.
     *
     * @param entity Entité à vérifier
     * @return true si l'entité est modifiable, false sinon
     */
    protected abstract boolean isEntityModifiable(E entity);

    /**
     * Retourne le nom de l'entité pour les messages d'erreur.
     *
     * @return Nom de l'entité
     */
    protected abstract String getEntityName();

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de mise à jour
     * @return DTO de l'entité mise à jour
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public T execute(U command) {
        log.debug("Exécution du cas d'utilisation de mise à jour de {}", getEntityName());

        // Récupération de l'ID à partir de la commande
        ID id = getIdFromCommand(command);

        // Récupération de l'entité
        E entity = getEntity(id);

        // Vérification que l'entité est modifiable
        if (!isEntityModifiable(entity)) {
            throw new BusinessException(
                    getEntityName() + ".readonly",
                    "Ce " + getEntityName() + " ne peut pas être modifié"
            );
        }

        // Mise à jour de l'entité
        E updatedEntity = updateEntity(entity, command);

        // Conversion en DTO
        return toDto(updatedEntity);
    }

    /**
     * Récupère l'ID à partir de la commande.
     *
     * @param command Commande de mise à jour
     * @return ID de l'entité
     */
    protected abstract ID getIdFromCommand(U command);
}
