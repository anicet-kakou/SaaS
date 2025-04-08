package com.devolution.saas.core.security.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.security.application.command.ChangePasswordCommand;
import com.devolution.saas.core.security.application.command.CreateUserCommand;
import com.devolution.saas.core.security.application.command.UpdateUserCommand;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.service.UserService;
import com.devolution.saas.core.security.domain.model.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "API pour la gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    /**
     * Crée un nouvel utilisateur.
     *
     * @param command Commande de création d'utilisateur
     * @return DTO de l'utilisateur créé
     */
    @PostMapping
    @Operation(summary = "Crée un nouvel utilisateur")
    @Auditable(action = "API_CREATE_USER")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserCommand command) {
        log.debug("REST request pour créer un utilisateur: {}", command.getUsername());
        UserDTO result = userService.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 3
     * Met à jour un utilisateur existant.
     *
     * @param id      ID de l'utilisateur à mettre à jour
     * @param command Commande de mise à jour d'utilisateur
     * @return DTO de l'utilisateur mis à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un utilisateur existant")
    @Auditable(action = "API_UPDATE_USER")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserCommand command) {
        log.debug("REST request pour mettre à jour l'utilisateur {}: {}", id, command);

        if (!id.equals(command.getId())) {
            return ResponseEntity.badRequest().build();
        }

        UserDTO result = userService.updateUser(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un utilisateur par son ID")
    @Auditable(action = "API_GET_USER")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        log.debug("REST request pour récupérer l'utilisateur: {}", id);
        UserDTO result = userService.getUser(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur.
     *
     * @param username Nom d'utilisateur
     * @return DTO de l'utilisateur
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Récupère un utilisateur par son nom d'utilisateur")
    @Auditable(action = "API_GET_USER_BY_USERNAME")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUsername(#username)")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        log.debug("REST request pour récupérer l'utilisateur par nom d'utilisateur: {}", username);
        UserDTO result = userService.getUserByUsername(username);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère un utilisateur par son adresse email.
     *
     * @param email Adresse email
     * @return DTO de l'utilisateur
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Récupère un utilisateur par son adresse email")
    @Auditable(action = "API_GET_USER_BY_EMAIL")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserEmail(#email)")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        log.debug("REST request pour récupérer l'utilisateur par email: {}", email);
        UserDTO result = userService.getUserByEmail(email);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste tous les utilisateurs.
     *
     * @return Liste des DTOs d'utilisateurs
     */
    @GetMapping
    @Operation(summary = "Liste tous les utilisateurs")
    @Auditable(action = "API_LIST_USERS")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listUsers() {
        log.debug("REST request pour lister les utilisateurs");
        List<UserDTO> result = userService.listUsers();
        return ResponseEntity.ok(result);
    }

    /**
     * Liste tous les utilisateurs par statut.
     *
     * @param status Statut des utilisateurs
     * @return Liste des DTOs d'utilisateurs
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Liste tous les utilisateurs par statut")
    @Auditable(action = "API_LIST_USERS_BY_STATUS")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listUsersByStatus(@PathVariable UserStatus status) {
        log.debug("REST request pour lister les utilisateurs par statut: {}", status);
        List<UserDTO> result = userService.listUsersByStatus(status);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste tous les utilisateurs appartenant à une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs d'utilisateurs
     */
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Liste tous les utilisateurs appartenant à une organisation")
    @Auditable(action = "API_LIST_USERS_BY_ORGANIZATION")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listUsersByOrganization(@PathVariable UUID organizationId) {
        log.debug("REST request pour lister les utilisateurs par organisation: {}", organizationId);
        List<UserDTO> result = userService.listUsersByOrganization(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Change le mot de passe d'un utilisateur.
     *
     * @param command Commande de changement de mot de passe
     * @return Réponse vide avec statut 200 OK
     */
    @PostMapping("/change-password")
    @Operation(summary = "Change le mot de passe d'un utilisateur")
    @Auditable(action = "API_CHANGE_PASSWORD")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#command.userId)")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordCommand command) {
        log.debug("REST request pour changer le mot de passe de l'utilisateur: {}", command.getUserId());
        userService.changePassword(command);
        return ResponseEntity.ok().build();
    }

    /**
     * Active un utilisateur.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur activé
     */
    @PutMapping("/{id}/activate")
    @Operation(summary = "Active un utilisateur")
    @Auditable(action = "API_ACTIVATE_USER")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> activateUser(@PathVariable UUID id) {
        log.debug("REST request pour activer l'utilisateur: {}", id);
        UserDTO result = userService.activateUser(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Désactive un utilisateur.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur désactivé
     */
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactive un utilisateur")
    @Auditable(action = "API_DEACTIVATE_USER")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable UUID id) {
        log.debug("REST request pour désactiver l'utilisateur: {}", id);
        UserDTO result = userService.deactivateUser(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Verrouille un utilisateur.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur verrouillé
     */
    @PutMapping("/{id}/lock")
    @Operation(summary = "Verrouille un utilisateur")
    @Auditable(action = "API_LOCK_USER")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> lockUser(@PathVariable UUID id) {
        log.debug("REST request pour verrouiller l'utilisateur: {}", id);
        UserDTO result = userService.lockUser(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Déverrouille un utilisateur.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur déverrouillé
     */
    @PutMapping("/{id}/unlock")
    @Operation(summary = "Déverrouille un utilisateur")
    @Auditable(action = "API_UNLOCK_USER")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> unlockUser(@PathVariable UUID id) {
        log.debug("REST request pour déverrouiller l'utilisateur: {}", id);
        UserDTO result = userService.unlockUser(id);
        return ResponseEntity.ok(result);
    }
}
