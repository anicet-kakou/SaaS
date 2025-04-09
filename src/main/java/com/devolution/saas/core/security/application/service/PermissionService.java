package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.abstracts.AbstractCrudService;
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
public class PermissionService extends AbstractCrudService<PermissionDTO, UUID, CreatePermissionCommand, UpdatePermissionCommand> {

    private final CreatePermission createPermission;
    private final UpdatePermission updatePermission;
    private final GetPermission getPermission;
    private final ListPermissions listPermissions;
    private final DeletePermission deletePermission;

    @Override
    protected PermissionDTO executeCreate(CreatePermissionCommand command) {
        log.debug("Création d'une nouvelle permission: {}", command.getResourceType() + "_" + command.getAction());
        return createPermission.execute(command);
    }

    @Override
    protected PermissionDTO executeUpdate(UpdatePermissionCommand command) {
        log.debug("Mise à jour de la permission: {}", command.getId());
        return updatePermission.execute(command);
    }

    @Override
    protected PermissionDTO executeGet(UUID id) {
        log.debug("Récupération de la permission: {}", id);
        return getPermission.execute(id);
    }

    @Override
    protected List<PermissionDTO> executeList() {
        log.debug("Listage de toutes les permissions");
        return listPermissions.execute();
    }

    @Override
    protected void executeDelete(UUID id) {
        log.debug("Suppression de la permission: {}", id);
        deletePermission.execute(id);
    }

    @Override
    protected String getEntityName() {
        return "permission";
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
     * Crée une nouvelle permission.
     *
     * @param command Commande de création de permission
     * @return DTO de la permission créée
     */
    public PermissionDTO createPermission(CreatePermissionCommand command) {
        return create(command);
    }

    /**
     * Met à jour une permission existante.
     *
     * @param command Commande de mise à jour de permission
     * @return DTO de la permission mise à jour
     */
    public PermissionDTO updatePermission(UpdatePermissionCommand command) {
        return update(command);
    }

    /**
     * Récupère une permission par son ID.
     *
     * @param id ID de la permission
     * @return DTO de la permission
     */
    public PermissionDTO getPermission(UUID id) {
        return get(id);
    }

    /**
     * Liste toutes les permissions.
     *
     * @return Liste des DTOs de permissions
     */
    public List<PermissionDTO> listPermissions() {
        return list();
    }

    /**
     * Supprime une permission.
     *
     * @param id ID de la permission à supprimer
     */
    public void deletePermission(UUID id) {
        delete(id);
    }
}
