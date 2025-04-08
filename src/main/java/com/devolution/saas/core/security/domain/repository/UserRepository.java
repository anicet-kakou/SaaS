package com.devolution.saas.core.security.domain.repository;

import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.model.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour les opérations sur les utilisateurs.
 */
public interface UserRepository {

    /**
     * Enregistre un utilisateur.
     *
     * @param user Utilisateur à enregistrer
     * @return Utilisateur enregistré
     */
    User save(User user);

    /**
     * Trouve un utilisateur par son ID.
     *
     * @param id ID de l'utilisateur
     * @return Utilisateur trouvé ou Optional vide
     */
    Optional<User> findById(UUID id);

    /**
     * Trouve un utilisateur par son nom d'utilisateur.
     *
     * @param username Nom d'utilisateur
     * @return Utilisateur trouvé ou Optional vide
     */
    Optional<User> findByUsername(String username);

    /**
     * Trouve un utilisateur par son adresse email.
     *
     * @param email Adresse email
     * @return Utilisateur trouvé ou Optional vide
     */
    Optional<User> findByEmail(String email);

    /**
     * Trouve tous les utilisateurs.
     *
     * @return Liste des utilisateurs
     */
    List<User> findAll();

    /**
     * Trouve tous les utilisateurs par statut.
     *
     * @param status Statut des utilisateurs
     * @return Liste des utilisateurs
     */
    List<User> findAllByStatus(UserStatus status);

    /**
     * Trouve tous les utilisateurs appartenant à une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des utilisateurs
     */
    List<User> findAllByOrganizationId(UUID organizationId);

    /**
     * Vérifie si un utilisateur existe par son nom d'utilisateur.
     *
     * @param username Nom d'utilisateur
     * @return true si l'utilisateur existe, false sinon
     */
    boolean existsByUsername(String username);

    /**
     * Vérifie si un utilisateur existe par son adresse email.
     *
     * @param email Adresse email
     * @return true si l'utilisateur existe, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Supprime un utilisateur.
     *
     * @param user Utilisateur à supprimer
     */
    void delete(User user);

    /**
     * Compte le nombre d'utilisateurs.
     *
     * @return Nombre d'utilisateurs
     */
    long count();
}
