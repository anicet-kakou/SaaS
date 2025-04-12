package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.domain.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'utilisation générique pour la création d'entités.
 *
 * @param <T> Type de l'entité DTO
 * @param <C> Type de la commande de création
 * @param <E> Type de l'entité de domaine
 */
@Slf4j
public abstract class AbstractCreateUseCase<T, C, E> {

    /**
     * Crée une entité à partir d'une commande.
     *
     * @param command Commande de création
     * @return Entité créée
     */
    protected abstract E createEntity(C command);

    /**
     * Convertit une entité de domaine en DTO.
     *
     * @param entity Entité de domaine
     * @return DTO
     */
    protected abstract T toDto(E entity);

    /**
     * Vérifie si une entité existe déjà avec les critères d'unicité.
     *
     * @param command Commande de création
     * @return true si une entité existe déjà, false sinon
     */
    protected abstract boolean existsByUniqueCriteria(C command);

    /**
     * Retourne le nom de l'entité pour les messages d'erreur.
     *
     * @return Nom de l'entité
     */
    protected abstract String getEntityName();

    /**
     * Retourne le nom du champ unique pour les messages d'erreur.
     *
     * @return Nom du champ unique
     */
    protected abstract String getUniqueFieldName();

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de création
     * @return DTO de l'entité créée
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public T execute(C command) {
        log.debug("Exécution du cas d'utilisation de création de {}", getEntityName());

        // Vérification de l'unicité
        if (existsByUniqueCriteria(command)) {
            throw new BusinessException(
                    getEntityName() + "." + getUniqueFieldName() + ".duplicate",
                    "Un " + getEntityName() + " avec ce " + getUniqueFieldName() + " existe déjà"
            );
        }

        // Création de l'entité
        E entity = createEntity(command);

        // Conversion en DTO
        return toDto(entity);
    }
}
