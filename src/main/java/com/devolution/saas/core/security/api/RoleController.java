package com.devolution.saas.core.security.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.security.application.command.CreateRoleCommand;
import com.devolution.saas.core.security.application.command.UpdateRoleCommand;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.service.RoleService;
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
 * Contrôleur REST pour la gestion des rôles.
 */
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Roles", description = "API pour la gestion des rôles")
public class RoleController {

    private final RoleService roleService;

    /**
     * Crée un nouveau rôle.
     *
     * @param command Commande de création de rôle
     * @return DTO du rôle créé
     */
    @PostMapping
    @Operation(summary = "Crée un nouveau rôle")
    @Auditable(action = "API_CREATE_ROLE")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody CreateRoleCommand command) {
        log.debug("REST request pour créer un rôle: {}", command.getName());
        RoleDTO result = roleService.createRole(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Met à jour un rôle existant.
     *
     * @param id      ID du rôle à mettre à jour
     * @param command Commande de mise à jour de rôle
     * @return DTO du rôle mis à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un rôle existant")
    @Auditable(action = "API_UPDATE_ROLE")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRoleCommand command) {
        log.debug("REST request pour mettre à jour le rôle {}: {}", id, command);

        if (!id.equals(command.getId())) {
            return ResponseEntity.badRequest().build();
        }

        RoleDTO result = roleService.updateRole(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère un rôle par son ID.
     *
     * @param id ID du rôle
     * @return DTO du rôle
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un rôle par son ID")
    @Auditable(action = "API_GET_ROLE")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> getRole(@PathVariable UUID id) {
        log.debug("REST request pour récupérer le rôle: {}", id);
        RoleDTO result = roleService.getRole(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste tous les rôles.
     *
     * @return Liste des DTOs de rôles
     */
    @GetMapping
    @Operation(summary = "Liste tous les rôles")
    @Auditable(action = "API_LIST_ROLES")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> listRoles() {
        log.debug("REST request pour lister tous les rôles");
        List<RoleDTO> result = roleService.listRoles();
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les rôles par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs de rôles
     */
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Liste les rôles par organisation")
    @Auditable(action = "API_LIST_ROLES_BY_ORGANIZATION")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> listRolesByOrganization(@PathVariable UUID organizationId) {
        log.debug("REST request pour lister les rôles par organisation: {}", organizationId);
        List<RoleDTO> result = roleService.listRolesByOrganization(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les rôles système.
     *
     * @param systemDefined Indique si les rôles sont définis par le système
     * @return Liste des DTOs de rôles
     */
    @GetMapping("/system")
    @Operation(summary = "Liste les rôles système")
    @Auditable(action = "API_LIST_ROLES_BY_SYSTEM_DEFINED")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> listRolesBySystemDefined(
            @RequestParam(defaultValue = "true") boolean systemDefined) {
        log.debug("REST request pour lister les rôles système: {}", systemDefined);
        List<RoleDTO> result = roleService.listRolesBySystemDefined(systemDefined);
        return ResponseEntity.ok(result);
    }

    /**
     * Supprime un rôle.
     *
     * @param id ID du rôle à supprimer
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un rôle")
    @Auditable(action = "API_DELETE_ROLE")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        log.debug("REST request pour supprimer le rôle: {}", id);
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
