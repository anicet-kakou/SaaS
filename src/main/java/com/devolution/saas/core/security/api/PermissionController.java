package com.devolution.saas.core.security.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.security.application.command.CreatePermissionCommand;
import com.devolution.saas.core.security.application.command.UpdatePermissionCommand;
import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.service.PermissionService;
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
 * Contrôleur REST pour la gestion des permissions.
 */
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Permissions", description = "API pour la gestion des permissions")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Crée une nouvelle permission.
     *
     * @param command Commande de création de permission
     * @return DTO de la permission créée
     */
    @PostMapping
    @Operation(summary = "Crée une nouvelle permission")
    @Auditable(action = "API_CREATE_PERMISSION")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody CreatePermissionCommand command) {
        log.debug("REST request pour créer une permission: {}", command.getResourceType() + "_" + command.getAction());
        PermissionDTO result = permissionService.createPermission(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Met à jour une permission existante.
     *
     * @param id      ID de la permission à mettre à jour
     * @param command Commande de mise à jour de permission
     * @return DTO de la permission mise à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une permission existante")
    @Auditable(action = "API_UPDATE_PERMISSION")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionDTO> updatePermission(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePermissionCommand command) {
        log.debug("REST request pour mettre à jour la permission {}: {}", id, command);

        if (!id.equals(command.getId())) {
            return ResponseEntity.badRequest().build();
        }

        PermissionDTO result = permissionService.updatePermission(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère une permission par son ID.
     *
     * @param id ID de la permission
     * @return DTO de la permission
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère une permission par son ID")
    @Auditable(action = "API_GET_PERMISSION")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable UUID id) {
        log.debug("REST request pour récupérer la permission: {}", id);
        PermissionDTO result = permissionService.getPermission(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste toutes les permissions.
     *
     * @return Liste des DTOs de permissions
     */
    @GetMapping
    @Operation(summary = "Liste toutes les permissions")
    @Auditable(action = "API_LIST_PERMISSIONS")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionDTO>> listPermissions() {
        log.debug("REST request pour lister toutes les permissions");
        List<PermissionDTO> result = permissionService.listPermissions();
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les permissions par type de ressource.
     *
     * @param resourceType Type de ressource
     * @return Liste des DTOs de permissions
     */
    @GetMapping("/resource-type/{resourceType}")
    @Operation(summary = "Liste les permissions par type de ressource")
    @Auditable(action = "API_LIST_PERMISSIONS_BY_RESOURCE_TYPE")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionDTO>> listPermissionsByResourceType(@PathVariable String resourceType) {
        log.debug("REST request pour lister les permissions par type de ressource: {}", resourceType);
        List<PermissionDTO> result = permissionService.listPermissionsByResourceType(resourceType);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les permissions système.
     *
     * @param systemDefined Indique si les permissions sont définies par le système
     * @return Liste des DTOs de permissions
     */
    @GetMapping("/system")
    @Operation(summary = "Liste les permissions système")
    @Auditable(action = "API_LIST_PERMISSIONS_BY_SYSTEM_DEFINED")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionDTO>> listPermissionsBySystemDefined(
            @RequestParam(defaultValue = "true") boolean systemDefined) {
        log.debug("REST request pour lister les permissions système: {}", systemDefined);
        List<PermissionDTO> result = permissionService.listPermissionsBySystemDefined(systemDefined);
        return ResponseEntity.ok(result);
    }

    /**
     * Supprime une permission.
     *
     * @param id ID de la permission à supprimer
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une permission")
    @Auditable(action = "API_DELETE_PERMISSION")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID id) {
        log.debug("REST request pour supprimer la permission: {}", id);
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
