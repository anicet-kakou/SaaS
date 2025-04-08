package com.devolution.saas.core.security.domain.repository;

import com.devolution.saas.core.security.domain.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour les opérations sur les permissions.
 */
public interface PermissionRepository {

    /**
     * Enregistre une permission.
     *
     * @param permission Permission à enregistrer
     * @return Permission enregistrée
     */
    Permission save(Permission permission);

    // La méthode saveAll est héritée de JpaRepository, pas besoin de la déclarer ici

    /**
     * Trouve une permission par son ID.
     *
     * @param id ID de la permission
     * @return Permission trouvée ou Optional vide
     */
    Optional<Permission> findById(UUID id);

    /**
     * Trouve une permission par son nom.
     *
     * @param name Nom de la permission
     * @return Permission trouvée ou Optional vide
     */
    Optional<Permission> findByName(String name);

    /**
     * Trouve une permission par le type de ressource et l'action.
     *
     * @param resourceType Type de ressource
     * @param action       Action
     * @return Permission trouvée ou Optional vide
     */
    Optional<Permission> findByResourceTypeAndAction(String resourceType, String action);

    /**
     * Trouve toutes les permissions.
     *
     * @return Liste des permissions
     */
    List<Permission> findAll();

    /**
     * Trouve toutes les permissions par type de ressource.
     *
     * @param resourceType Type de ressource
     * @return Liste des permissions
     */
    List<Permission> findAllByResourceType(String resourceType);

    /**
     * Trouve toutes les permissions définies par le système.
     *
     * @return Liste des permissions système
     */
    List<Permission> findAllBySystemDefined(boolean systemDefined);

    /**
     * Vérifie si une permission existe par son nom.
     *
     * @param name Nom de la permission
     * @return true si la permission existe, false sinon
     */
    boolean existsByName(String name);

    /**
     * Vérifie si une permission existe par le type de ressource et l'action.
     *
     * @param resourceType Type de ressource
     * @param action       Action
     * @return true si la permission existe, false sinon
     */
    boolean existsByResourceTypeAndAction(String resourceType, String action);

    /**
     * Supprime une permission.
     *
     * @param permission Permission à supprimer
     */
    void delete(Permission permission);

    /**
     * Compte le nombre de permissions.
     *
     * @return Nombre de permissions
     */
    long count();
}
