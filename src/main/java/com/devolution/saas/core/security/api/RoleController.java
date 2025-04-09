package com.devolution.saas.core.security.api;

import com.devolution.saas.common.abstracts.AbstractCrudController;
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
public class RoleController extends AbstractCrudController<RoleDTO, UUID, CreateRoleCommand, UpdateRoleCommand> {

    private final RoleService roleService;

    @Override
    protected RoleDTO create(CreateRoleCommand command) {
        return roleService.createRole(command);
    }

    @Override
    protected RoleDTO update(UUID id, UpdateRoleCommand command) {
        return roleService.updateRole(command);
    }

    @Override
    protected RoleDTO get(UUID id) {
        return roleService.getRole(id);
    }

    @Override
    protected List<RoleDTO> list() {
        return roleService.listRoles();
    }

    @Override
    protected void delete(UUID id) {
        roleService.deleteRole(id);
    }

    @Override
    protected String getEntityName() {
        return "rôle";
    }

    @Override
    protected boolean isValidId(UUID id, UpdateRoleCommand command) {
        return id.equals(command.getId());
    }

    /**
     * Surcharge des méthodes standard pour ajouter les annotations de sécurité
     */
    @Override
    @PostMapping
    @Operation(summary = "Crée un nouveau rôle")
    @Auditable(action = "API_CREATE_ROLE")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> createEntity(@Valid @RequestBody CreateRoleCommand command) {
        return super.createEntity(command);
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un rôle existant")
    @Auditable(action = "API_UPDATE_ROLE")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> updateEntity(@PathVariable UUID id, @Valid @RequestBody UpdateRoleCommand command) {
        return super.updateEntity(id, command);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un rôle par son ID")
    @Auditable(action = "API_GET_ROLE")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> getEntity(@PathVariable UUID id) {
        return super.getEntity(id);
    }

    @Override
    @GetMapping
    @Operation(summary = "Liste tous les rôles")
    @Auditable(action = "API_LIST_ROLES")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> listEntities() {
        return super.listEntities();
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un rôle")
    @Auditable(action = "API_DELETE_ROLE")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEntity(@PathVariable UUID id) {
        return super.deleteEntity(id);
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


}
