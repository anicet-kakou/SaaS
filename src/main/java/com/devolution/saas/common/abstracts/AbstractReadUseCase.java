package com.devolution.saas.common.abstracts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Cas d'utilisation générique pour la lecture d'entités.
 *
 * @param <T> Type de l'entité DTO
 * @param <E> Type de l'entité de domaine
 */
@Slf4j
public abstract class AbstractReadUseCase<T, E> {

    /**
     * Convertit une entité de domaine en DTO.
     *
     * @param entity Entité de domaine
     * @return DTO
     */
    protected abstract T toDto(E entity);

    /**
     * Récupère une entité par son ID.
     *
     * @param id ID de l'entité
     * @return Entité récupérée
     */
    protected abstract Optional<E> findById(UUID id);

    /**
     * Récupère toutes les entités.
     *
     * @return Liste des entités
     */
    protected abstract List<E> findAll();

    /**
     * Retourne le nom de l'entité pour les logs.
     *
     * @return Nom de l'entité
     */
    protected abstract String getEntityName();

    /**
     * Exécute le cas d'utilisation pour récupérer une entité par son ID.
     *
     * @param id ID de l'entité
     * @return DTO de l'entité récupérée
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<T> execute(UUID id) {
        log.debug("Exécution du cas d'utilisation de récupération de {} avec ID: {}", getEntityName(), id);

        return findById(id).map(this::toDto);
    }

    /**
     * Exécute le cas d'utilisation pour récupérer toutes les entités.
     *
     * @return Liste des DTOs des entités récupérées
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<T> execute() {
        log.debug("Exécution du cas d'utilisation de récupération de tous les {}", getEntityName());

        return findAll().stream()
                .map(this::toDto)
                .toList();
    }
}
