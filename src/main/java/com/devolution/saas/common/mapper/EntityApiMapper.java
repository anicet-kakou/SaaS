package com.devolution.saas.common.mapper;

/**
 * Interface générique pour les mappers entre les objets d'API et les entités/DTOs.
 *
 * @param <E> Type de l'entité ou du DTO d'application
 * @param <R> Type de la requête API
 * @param <T> Type de la réponse API
 */
public interface EntityApiMapper<E, R, T> {

    /**
     * Convertit une requête API en entité ou DTO d'application.
     *
     * @param request La requête à convertir
     * @return L'entité ou DTO correspondant
     */
    E toEntity(R request);

    /**
     * Convertit une entité ou DTO d'application en réponse API.
     *
     * @param entity L'entité ou DTO à convertir
     * @return La réponse correspondante
     */
    T toResponse(E entity);
}
