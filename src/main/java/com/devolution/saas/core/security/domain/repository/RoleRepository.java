package com.devolution.saas.core.security.domain.repository;

import com.devolution.saas.core.security.domain.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour les opérations sur les rôles.
 */
public interface RoleRepository {

    /**
     * Enregistre un rôle.
     *
     * @param role Rôle à enregistrer
     * @return Rôle enregistré
     */
    Role save(Role role);

    /**
     * Trouve un rôle par son ID.
     *
     * @param id ID du rôle
     * @return Rôle trouvé ou Optional vide
     */
    Optional<Role> findById(UUID id);

    /**
     * Trouve un rôle par son nom.
     *
     * @param name Nom du rôle
     * @return Rôle trouvé ou Optional vide
     */
    Optional<Role> findByName(String name);

    /**
     * Trouve un rôle par son nom et l'ID de l'organisation.
     *
     * @param name           Nom du rôle
     * @param organizationId ID de l'organisation
     * @return Rôle trouvé ou Optional vide
     */
    Optional<Role> findByNameAndOrganizationId(String name, UUID organizationId);

    /**
     * Trouve tous les rôles.
     *
     * @return Liste des rôles
     */
    List<Role> findAll();

    /**
     * Trouve tous les rôles définis par le système.
     *
     * @return Liste des rôles système
     */
    List<Role> findAllBySystemDefined(boolean systemDefined);

    /**
     * Trouve tous les rôles appartenant à une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des rôles
     */
    List<Role> findAllByOrganizationId(UUID organizationId);

    /**
     * Vérifie si un rôle existe par son nom et l'ID de l'organisation.
     *
     * @param name           Nom du rôle
     * @param organizationId ID de l'organisation
     * @return true si le rôle existe, false sinon
     */
    boolean existsByNameAndOrganizationId(String name, UUID organizationId);

    /**
     * Supprime un rôle.
     *
     * @param role Rôle à supprimer
     */
    void delete(Role role);

    /**
     * Compte le nombre de rôles.
     *
     * @return Nombre de rôles
     */
    long count();
}
