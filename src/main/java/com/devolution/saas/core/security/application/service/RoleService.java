package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.abstracts.AbstractCrudService;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.security.application.command.CreateRoleCommand;
import com.devolution.saas.core.security.application.command.UpdateRoleCommand;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.mapper.RoleMapper;
import com.devolution.saas.core.security.application.usecase.*;
import com.devolution.saas.core.security.domain.model.Role;
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
public class RoleService extends AbstractCrudService<RoleDTO, UUID, CreateRoleCommand, UpdateRoleCommand> {

    private final CreateRole createRole;
    private final UpdateRole updateRole;
    private final GetRole getRole;
    private final ListRoles listRoles;
    private final DeleteRole deleteRole;
    private final RoleMapper roleMapper;

    /**
     * Implémentation des méthodes abstraites de AbstractCrudService
     */
    @Override
    protected RoleDTO executeCreate(CreateRoleCommand command) {
        return createRole.execute(command);
    }

    @Override
    protected RoleDTO executeUpdate(UpdateRoleCommand command) {
        return updateRole.execute(command);
    }

    @Override
    protected RoleDTO executeGet(UUID id) {
        return getRole.execute(id);
    }

    @Override
    protected List<RoleDTO> executeList() {
        return listRoles.execute();
    }

    @Override
    protected void executeDelete(UUID id) {
        deleteRole.execute(id);
    }

    @Override
    protected String getEntityName() {
        return "rôle";
    }

    /**
     * Méthodes de façade pour les opérations CRUD standard
     */

    /**
     * Crée un nouveau rôle.
     *
     * @param command Commande de création de rôle
     * @return DTO du rôle créé
     */
    @Auditable(action = "CREATE_ROLE")
    public RoleDTO createRole(CreateRoleCommand command) {
        return create(command);
    }

    /**
     * Met à jour un rôle existant.
     *
     * @param command Commande de mise à jour de rôle
     * @return DTO du rôle mis à jour
     */
    @Auditable(action = "UPDATE_ROLE")
    public RoleDTO updateRole(UpdateRoleCommand command) {
        return update(command);
    }

    /**
     * Récupère un rôle par son ID.
     *
     * @param id ID du rôle
     * @return DTO du rôle
     */
    @Auditable(action = "GET_ROLE")
    public RoleDTO getRole(UUID id) {
        return get(id);
    }

    /**
     * Liste tous les rôles.
     *
     * @return Liste des DTOs de rôles
     */
    @Auditable(action = "LIST_ROLES")
    public List<RoleDTO> listRoles() {
        return list();
    }

    /**
     * Supprime un rôle.
     *
     * @param id ID du rôle à supprimer
     */
    @Auditable(action = "DELETE_ROLE")
    public void deleteRole(UUID id) {
        delete(id);
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
     * Convertit une entité Role en DTO.
     * Cette méthode est utilisée par les services qui manipulent directement les entités Role.
     *
     * @param role Entité Role à convertir
     * @return DTO du rôle
     */
    @Transactional(readOnly = true)
    public RoleDTO mapToDTO(Role role) {
        return roleMapper.toDTO(role);
    }


}
