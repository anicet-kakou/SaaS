package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.command.UpdatePermissionCommand;
import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.mapper.PermissionMapper;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'utilisation pour la mise à jour d'une permission.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdatePermission {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de mise à jour de permission
     * @return DTO de la permission mise à jour
     */
    @Transactional
    public PermissionDTO execute(UpdatePermissionCommand command) {
        log.debug("Mise à jour de la permission: {}", command.getId());

        // Récupération de la permission
        Permission permission = permissionRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Permission", command.getId()));

        // Vérification que la permission n'est pas définie par le système
        if (permission.isSystemDefined()) {
            throw new BusinessException("permission.system.readonly", "Les permissions définies par le système ne peuvent pas être modifiées");
        }

        // Vérification de l'unicité du nom
        if (!permission.getName().equals(command.getName()) &&
                permissionRepository.existsByName(command.getName())) {
            throw new BusinessException("permission.name.duplicate", "Une permission avec ce nom existe déjà");
        }

        // Vérification de l'unicité de la combinaison resourceType et action
        if ((!permission.getResourceType().equals(command.getResourceType()) ||
                !permission.getAction().equals(command.getAction())) &&
                permissionRepository.existsByResourceTypeAndAction(command.getResourceType(), command.getAction())) {
            throw new BusinessException("permission.resource.action.duplicate", "Une permission avec ce type de ressource et cette action existe déjà");
        }

        // Mise à jour des propriétés
        permission.setName(command.getName());
        permission.setDescription(command.getDescription());
        permission.setResourceType(command.getResourceType());
        permission.setAction(command.getAction());

        // Sauvegarde de la permission
        permission = permissionRepository.save(permission);

        return permissionMapper.toDTO(permission);
    }
}
