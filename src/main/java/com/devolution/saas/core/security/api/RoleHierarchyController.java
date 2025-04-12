package com.devolution.saas.core.security.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.service.RoleHierarchyService;
import com.devolution.saas.core.security.application.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion de la hiérarchie des rôles.
 */
@RestController
@RequestMapping("/api/v1/roles/hierarchy")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Role Hierarchy", description = "API pour la gestion de la hiérarchie des rôles")
public class RoleHierarchyController {

    private final RoleService roleService;
    private final RoleHierarchyService roleHierarchyService;

    /**
     * Définit le rôle parent d'un rôle.
     *
     * @param roleId   ID du rôle
     * @param parentId ID du rôle parent
     * @return DTO du rôle mis à jour
     */
    @PutMapping("/{roleId}/parent/{parentId}")
    @Operation(summary = "Définit le rôle parent d'un rôle")
    @Auditable(action = "API_SET_ROLE_PARENT")
    @PreAuthorize("hasPermission('ROLE_UPDATE') and @securityService.canManageRole(#roleId)")
    public ResponseEntity<RoleDTO> setParent(@PathVariable UUID roleId, @PathVariable UUID parentId) {
        log.debug("REST request pour définir le parent du rôle {} : {}", roleId, parentId);

        // Vérifier que le rôle parent existe
        roleService.getRole(parentId); // Lance une exception si le rôle n'existe pas

        // Définir le parent
        roleHierarchyService.setParent(roleId, parentId);

        // Récupérer le rôle mis à jour
        RoleDTO updatedRole = roleService.getRole(roleId);

        return ResponseEntity.ok(updatedRole);
    }

    /**
     * Supprime le rôle parent d'un rôle.
     *
     * @param roleId ID du rôle
     * @return DTO du rôle mis à jour
     */
    @DeleteMapping("/{roleId}/parent")
    @Operation(summary = "Supprime le rôle parent d'un rôle")
    @Auditable(action = "API_REMOVE_ROLE_PARENT")
    @PreAuthorize("hasPermission('ROLE_UPDATE') and @securityService.canManageRole(#roleId)")
    public ResponseEntity<RoleDTO> removeParent(@PathVariable UUID roleId) {
        log.debug("REST request pour supprimer le parent du rôle {}", roleId);

        // Définir le parent à null
        roleHierarchyService.setParent(roleId, null);

        // Récupérer le rôle mis à jour
        RoleDTO updatedRole = roleService.getRole(roleId);

        return ResponseEntity.ok(updatedRole);
    }

    /**
     * Récupère tous les rôles enfants d'un rôle.
     *
     * @param roleId ID du rôle
     * @return Liste des DTOs des rôles enfants
     */
    @GetMapping("/{roleId}/children")
    @Operation(summary = "Récupère tous les rôles enfants d'un rôle")
    @Auditable(action = "API_GET_ROLE_CHILDREN")
    @PreAuthorize("hasPermission('ROLE_READ')")
    public ResponseEntity<List<RoleDTO>> getChildren(@PathVariable UUID roleId) {
        log.debug("REST request pour récupérer les enfants du rôle {}", roleId);

        // Vérifier que le rôle existe
        roleService.getRole(roleId); // Lance une exception si le rôle n'existe pas

        // Récupérer les enfants
        List<RoleDTO> children = roleHierarchyService.getAllChildren(roleId).stream()
                .map(roleService::mapToDTO)
                .toList();

        return ResponseEntity.ok(children);
    }

    /**
     * Récupère la représentation textuelle de la hiérarchie des rôles.
     *
     * @return Représentation textuelle de la hiérarchie des rôles
     */
    @GetMapping("/tree")
    @Operation(summary = "Récupère la représentation textuelle de la hiérarchie des rôles")
    @Auditable(action = "API_GET_ROLE_HIERARCHY_TREE")
    @PreAuthorize("hasPermission('ROLE_READ')")
    public ResponseEntity<Map<String, String>> getHierarchyTree() {
        log.debug("REST request pour récupérer l'arbre de hiérarchie des rôles");

        String hierarchyTree = roleHierarchyService.buildRoleHierarchyString();

        return ResponseEntity.ok(Map.of("hierarchyTree", hierarchyTree));
    }
}
