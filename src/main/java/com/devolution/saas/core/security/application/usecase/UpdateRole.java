package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.command.UpdateRoleCommand;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.mapper.RoleMapper;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour la mise à jour d'un rôle.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateRole {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de mise à jour de rôle
     * @return DTO du rôle mis à jour
     */
    @Transactional
    public RoleDTO execute(UpdateRoleCommand command) {
        log.debug("Mise à jour du rôle: {}", command.getId());

        // Récupération du rôle
        Role role = roleRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", command.getId()));

        // Vérification que le rôle n'est pas défini par le système
        if (role.isSystemDefined()) {
            throw new BusinessException("role.system.readonly", "Les rôles définis par le système ne peuvent pas être modifiés");
        }

        // Vérification de l'unicité du nom du rôle dans l'organisation
        if (!role.getName().equals(command.getName()) &&
                roleRepository.existsByNameAndOrganizationId(command.getName(), command.getOrganizationId())) {
            throw new BusinessException("role.name.duplicate", "Un rôle avec ce nom existe déjà dans cette organisation");
        }

        // Mise à jour des propriétés
        role.setName(command.getName());
        role.setDescription(command.getDescription());
        role.setOrganizationId(command.getOrganizationId());

        // Mise à jour des permissions
        role.getPermissions().clear();
        for (UUID permissionId : command.getPermissionIds()) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission", permissionId));
            role.addPermission(permission);
        }

        // Sauvegarde du rôle
        role = roleRepository.save(role);

        return roleMapper.toDTO(role);
    }
}
