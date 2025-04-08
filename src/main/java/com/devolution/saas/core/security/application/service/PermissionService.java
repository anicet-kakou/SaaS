package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.security.application.command.CreatePermissionCommand;
import com.devolution.saas.core.security.application.command.UpdatePermissionCommand;
import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.usecase.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service pour la gestion des permissions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final CreatePermission createPermission;
    private final UpdatePermission updatePermission;
    private final GetPermission getPermission;
    private final ListPermissions listPermissions;
    private final DeletePermission deletePermission;

    /**
     * Crée une nouvelle permission.
     *
     * @param command Commande de création de permission
     * @return DTO de la permission créée
     */
    @Transactional
    @Auditable(action = "CREATE_PERMISSION")
    public PermissionDTO createPermission(CreatePermissionCommand command) {
        log.debug("Création d'une nouvelle permission: {}", command.getResourceType() + "_" + command.getAction());
        return createPermission.execute(command);
    }

    /**
     * Met à jour une permission existante.
     *
     * @param command Commande de mise à jour de permission
     * @return DTO de la permission mise à jour
     */
    @Transactional
    @Auditable(action = "UPDATE_PERMISSION")
    public PermissionDTO updatePermission(UpdatePermissionCommand command) {
        log.debug("Mise à jour de la permission: {}", command.getId());
        return updatePermission.execute(command);
    }

    /**
     * Récupère une permission par son ID.
     *
     * @param id ID de la permission
     * @return DTO de la permission
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_PERMISSION")
    public PermissionDTO getPermission(UUID id) {
        log.debug("Récupération de la permission: {}", id);
        return getPermission.execute(id);
    }

    /**
     * Liste toutes les permissions.
     *
     * @return Liste des DTOs de permissions
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_PERMISSIONS")
    public List<PermissionDTO> listPermissions() {
        log.debug("Listage de toutes les permissions");
        return listPermissions.execute();
    }

    /**
     * Liste les permissions par type de ressource.
     *
     * @param resourceType Type de ressource
     * @return Liste des DTOs de permissions
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_PERMISSIONS_BY_RESOURCE_TYPE")
    public List<PermissionDTO> listPermissionsByResourceType(String resourceType) {
        log.debug("Listage des permissions par type de ressource: {}", resourceType);
        return listPermissions.executeByResourceType(resourceType);
    }

    /**
     * Liste les permissions système.
     *
     * @param systemDefined Indique si les permissions sont définies par le système
     * @return Liste des DTOs de permissions
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_PERMISSIONS_BY_SYSTEM_DEFINED")
    public List<PermissionDTO> listPermissionsBySystemDefined(boolean systemDefined) {
        log.debug("Listage des permissions système: {}", systemDefined);
        return listPermissions.executeBySystemDefined(systemDefined);
    }

    /**
     * Supprime une permission.
     *
     * @param id ID de la permission à supprimer
     */
    @Transactional
    @Auditable(action = "DELETE_PERMISSION")
    public void deletePermission(UUID id) {
        log.debug("Suppression de la permission: {}", id);
        deletePermission.execute(id);
    }
}
