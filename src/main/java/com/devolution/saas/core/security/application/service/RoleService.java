package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.security.application.command.CreateRoleCommand;
import com.devolution.saas.core.security.application.command.UpdateRoleCommand;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.usecase.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service pour la gestion des rôles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final CreateRole createRole;
    private final UpdateRole updateRole;
    private final GetRole getRole;
    private final ListRoles listRoles;
    private final DeleteRole deleteRole;

    /**
     * Crée un nouveau rôle.
     *
     * @param command Commande de création de rôle
     * @return DTO du rôle créé
     */
    @Transactional
    @Auditable(action = "CREATE_ROLE")
    @TenantRequired
    public RoleDTO createRole(CreateRoleCommand command) {
        log.debug("Création d'un nouveau rôle: {}", command.getName());
        return createRole.execute(command);
    }

    /**
     * Met à jour un rôle existant.
     *
     * @param command Commande de mise à jour de rôle
     * @return DTO du rôle mis à jour
     */
    @Transactional
    @Auditable(action = "UPDATE_ROLE")
    @TenantRequired
    public RoleDTO updateRole(UpdateRoleCommand command) {
        log.debug("Mise à jour du rôle: {}", command.getId());
        return updateRole.execute(command);
    }

    /**
     * Récupère un rôle par son ID.
     *
     * @param id ID du rôle
     * @return DTO du rôle
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_ROLE")
    public RoleDTO getRole(UUID id) {
        log.debug("Récupération du rôle: {}", id);
        return getRole.execute(id);
    }

    /**
     * Liste tous les rôles.
     *
     * @return Liste des DTOs de rôles
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_ROLES")
    public List<RoleDTO> listRoles() {
        log.debug("Listage de tous les rôles");
        return listRoles.execute();
    }

    /**
     * Liste les rôles par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs de rôles
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_ROLES_BY_ORGANIZATION")
    @TenantRequired
    public List<RoleDTO> listRolesByOrganization(UUID organizationId) {
        log.debug("Listage des rôles par organisation: {}", organizationId);
        return listRoles.executeByOrganization(organizationId);
    }

    /**
     * Liste les rôles système.
     *
     * @param systemDefined Indique si les rôles sont définis par le système
     * @return Liste des DTOs de rôles
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_ROLES_BY_SYSTEM_DEFINED")
    public List<RoleDTO> listRolesBySystemDefined(boolean systemDefined) {
        log.debug("Listage des rôles système: {}", systemDefined);
        return listRoles.executeBySystemDefined(systemDefined);
    }

    /**
     * Supprime un rôle.
     *
     * @param id ID du rôle à supprimer
     */
    @Transactional
    @Auditable(action = "DELETE_ROLE")
    @TenantRequired
    public void deleteRole(UUID id) {
        log.debug("Suppression du rôle: {}", id);
        deleteRole.execute(id);
    }
}
