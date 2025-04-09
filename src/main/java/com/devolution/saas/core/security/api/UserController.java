package com.devolution.saas.core.security.api;

import com.devolution.saas.common.abstracts.AbstractCrudController;
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
public class UserController extends AbstractCrudController<UserDTO, UUID, CreateUserCommand, UpdateUserCommand> {

    private final UserService userService;

    @Override
    protected UserDTO create(CreateUserCommand command) {
        return userService.createUser(command);
    }

    @Override
    protected UserDTO update(UUID id, UpdateUserCommand command) {
        return userService.updateUser(command);
    }

    @Override
    protected UserDTO get(UUID id) {
        return userService.getUser(id);
    }

    @Override
    protected List<UserDTO> list() {
        return userService.listUsers();
    }

    @Override
    protected void delete(UUID id) {
        // Non implémenté pour le moment
        throw new UnsupportedOperationException("La suppression d'utilisateurs n'est pas prise en charge");
    }

    @Override
    protected String getEntityName() {
        return "utilisateur";
    }

    @Override
    protected boolean isValidId(UUID id, UpdateUserCommand command) {
        return id.equals(command.getId());
    }

    /**
     * Surcharge des méthodes standard pour ajouter les annotations de sécurité
     */
    @Override
    @PostMapping
    @Operation(summary = "Crée un nouvel utilisateur")
    @Auditable(action = "API_CREATE_USER")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createEntity(@Valid @RequestBody CreateUserCommand command) {
        return super.createEntity(command);
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un utilisateur existant")
    @Auditable(action = "API_UPDATE_USER")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> updateEntity(@PathVariable UUID id, @Valid @RequestBody UpdateUserCommand command) {
        return super.updateEntity(id, command);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un utilisateur par son ID")
    @Auditable(action = "API_GET_USER")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> getEntity(@PathVariable UUID id) {
        return super.getEntity(id);
    }

    @Override
    @GetMapping
    @Operation(summary = "Liste tous les utilisateurs")
    @Auditable(action = "API_LIST_USERS")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listEntities() {
        return super.listEntities();
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
