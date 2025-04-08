package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.command.CreateRoleCommand;
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
 * Cas d'utilisation pour la création d'un rôle.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateRole {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de création de rôle
     * @return DTO du rôle créé
     */
    @Transactional
    public RoleDTO execute(CreateRoleCommand command) {
        log.debug("Création d'un nouveau rôle: {}", command.getName());

        // Vérification de l'unicité du nom du rôle dans l'organisation
        if (roleRepository.existsByNameAndOrganizationId(command.getName(), command.getOrganizationId())) {
            throw new BusinessException("role.name.duplicate", "Un rôle avec ce nom existe déjà dans cette organisation");
        }

        // Création du rôle
        Role role = new Role();
        role.setName(command.getName());
        role.setDescription(command.getDescription());
        role.setOrganizationId(command.getOrganizationId());
        role.setSystemDefined(false);

        // Ajout des permissions
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
