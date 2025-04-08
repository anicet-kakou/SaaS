package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantFilter;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.security.application.command.ChangePasswordCommand;
import com.devolution.saas.core.security.application.command.CreateUserCommand;
import com.devolution.saas.core.security.application.command.UpdateUserCommand;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.usecase.*;
import com.devolution.saas.core.security.domain.model.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service pour la gestion des utilisateurs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final CreateUser createUser;
    private final UpdateUser updateUser;
    private final GetUser getUser;
    private final GetUserByUsername getUserByUsername;
    private final GetUserByEmail getUserByEmail;
    private final ListUsers listUsers;
    private final ListUsersByStatus listUsersByStatus;
    private final ListUsersByOrganization listUsersByOrganization;
    private final ChangePassword changePassword;
    private final ActivateUser activateUser;
    private final DeactivateUser deactivateUser;
    private final LockUser lockUser;
    private final UnlockUser unlockUser;

    /**
     * Crée un nouvel utilisateur.
     *
     * @param command Commande de création d'utilisateur
     * @return DTO de l'utilisateur créé
     */
    @Transactional
    @Auditable(action = "CREATE_USER")
    @TenantRequired
    public UserDTO createUser(CreateUserCommand command) {
        log.debug("Création d'un nouvel utilisateur: {}", command.getUsername());
        return createUser.execute(command);
    }

    /**
     * Met à jour un utilisateur existant.
     *
     * @param command Commande de mise à jour d'utilisateur
     * @return DTO de l'utilisateur mis à jour
     */
    @Transactional
    @Auditable(action = "UPDATE_USER")
    @TenantRequired
    public UserDTO updateUser(UpdateUserCommand command) {
        log.debug("Mise à jour de l'utilisateur: {}", command.getId());
        return updateUser.execute(command);
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_USER")
    public UserDTO getUser(UUID id) {
        log.debug("Récupération de l'utilisateur: {}", id);
        return getUser.execute(id);
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur.
     *
     * @param username Nom d'utilisateur
     * @return DTO de l'utilisateur
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_USER_BY_USERNAME")
    public UserDTO getUserByUsername(String username) {
        log.debug("Récupération de l'utilisateur par nom d'utilisateur: {}", username);
        return getUserByUsername.execute(username);
    }

    /**
     * Récupère un utilisateur par son adresse email.
     *
     * @param email Adresse email
     * @return DTO de l'utilisateur
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_USER_BY_EMAIL")
    public UserDTO getUserByEmail(String email) {
        log.debug("Récupération de l'utilisateur par email: {}", email);
        return getUserByEmail.execute(email);
    }

    /**
     * Liste tous les utilisateurs.
     *
     * @return Liste des DTOs d'utilisateurs
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_USERS")
    @TenantFilter(includeDescendants = true)
    public List<UserDTO> listUsers() {
        log.debug("Listage des utilisateurs");
        return listUsers.execute();
    }

    /**
     * Liste tous les utilisateurs par statut.
     *
     * @param status Statut des utilisateurs
     * @return Liste des DTOs d'utilisateurs
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_USERS_BY_STATUS")
    @TenantFilter(includeDescendants = true)
    public List<UserDTO> listUsersByStatus(UserStatus status) {
        log.debug("Listage des utilisateurs par statut: {}", status);
        return listUsersByStatus.execute(status);
    }

    /**
     * Liste tous les utilisateurs appartenant à une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs d'utilisateurs
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_USERS_BY_ORGANIZATION")
    @TenantRequired
    @TenantFilter(includeDescendants = false)
    public List<UserDTO> listUsersByOrganization(UUID organizationId) {
        log.debug("Listage des utilisateurs par organisation: {}", organizationId);
        return listUsersByOrganization.execute(organizationId);
    }

    /**
     * Change le mot de passe d'un utilisateur.
     *
     * @param command Commande de changement de mot de passe
     */
    @Transactional
    @Auditable(action = "CHANGE_PASSWORD")
    public void changePassword(ChangePasswordCommand command) {
        log.debug("Changement de mot de passe pour l'utilisateur: {}", command.getUserId());
        changePassword.execute(command);
    }

    /**
     * Active un utilisateur.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur activé
     */
    @Transactional
    @Auditable(action = "ACTIVATE_USER")
    @TenantRequired
    public UserDTO activateUser(UUID id) {
        log.debug("Activation de l'utilisateur: {}", id);
        return activateUser.execute(id);
    }

    /**
     * Désactive un utilisateur.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur désactivé
     */
    @Transactional
    @Auditable(action = "DEACTIVATE_USER")
    @TenantRequired
    public UserDTO deactivateUser(UUID id) {
        log.debug("Désactivation de l'utilisateur: {}", id);
        return deactivateUser.execute(id);
    }

    /**
     * Verrouille un utilisateur.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur verrouillé
     */
    @Transactional
    @Auditable(action = "LOCK_USER")
    @TenantRequired
    public UserDTO lockUser(UUID id) {
        log.debug("Verrouillage de l'utilisateur: {}", id);
        return lockUser.execute(id);
    }

    /**
     * Déverrouille un utilisateur.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur déverrouillé
     */
    @Transactional
    @Auditable(action = "UNLOCK_USER")
    @TenantRequired
    public UserDTO unlockUser(UUID id) {
        log.debug("Déverrouillage de l'utilisateur: {}", id);
        return unlockUser.execute(id);
    }

    // Ces méthodes ont été déplacées vers UserMapper
}
