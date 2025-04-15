package com.devolution.saas.core.security.api;

import com.devolution.saas.common.abstracts.AbstractCrudController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.security.application.command.CreatePermissionCommand;
import com.devolution.saas.core.security.application.command.UpdatePermissionCommand;
import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class PermissionController extends AbstractCrudController<PermissionDTO, UUID, CreatePermissionCommand, UpdatePermissionCommand> {

    private final PermissionService permissionService;

    @Override
    @PreAuthorize("hasPermission('PERMISSION_CREATE')")
    protected PermissionDTO create(CreatePermissionCommand command) {
        log.debug("REST request pour créer une permission: {}", command.resourceType() + "_" + command.action());
        return permissionService.createPermission(command);
    }

    @Override
    @PreAuthorize("hasPermission('PERMISSION_UPDATE')")
    protected PermissionDTO update(UUID id, UpdatePermissionCommand command) {
        log.debug("REST request pour mettre à jour la permission {}: {}", id, command);
        return permissionService.updatePermission(command);
    }

    @Override
    @PreAuthorize("hasPermission('PERMISSION_READ')")
    protected PermissionDTO get(UUID id) {
        log.debug("REST request pour récupérer la permission: {}", id);
        return permissionService.getPermission(id);
    }

    @Override
    @PreAuthorize("hasPermission('PERMISSION_LIST')")
    protected List<PermissionDTO> list() {
        log.debug("REST request pour lister toutes les permissions");
        return permissionService.listPermissions();
    }

    @Override
    @PreAuthorize("hasPermission('PERMISSION_DELETE')")
    protected void delete(UUID id) {
        log.debug("REST request pour supprimer la permission: {}", id);
        permissionService.deletePermission(id);
    }

    @Override
    protected String getEntityName() {
        return "permission";
    }

    @Override
    protected boolean isValidId(UUID id, UpdatePermissionCommand command) {
        return id.equals(command.getId());
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
    @PreAuthorize("hasPermission('PERMISSION_LIST')")
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
    @PreAuthorize("hasPermission('PERMISSION_LIST')")
    public ResponseEntity<List<PermissionDTO>> listPermissionsBySystemDefined(
            @RequestParam(defaultValue = "true") boolean systemDefined) {
        log.debug("REST request pour lister les permissions système: {}", systemDefined);
        List<PermissionDTO> result = permissionService.listPermissionsBySystemDefined(systemDefined);
        return ResponseEntity.ok(result);
    }
}
