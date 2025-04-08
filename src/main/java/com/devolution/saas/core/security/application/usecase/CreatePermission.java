package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.core.security.application.command.CreatePermissionCommand;
import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.mapper.PermissionMapper;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Cas d'utilisation pour la création d'une permission.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreatePermission {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de création de permission
     * @return DTO de la permission créée
     */
    @Transactional
    public PermissionDTO execute(CreatePermissionCommand command) {
        log.debug("Création d'une nouvelle permission: {}", command.getResourceType() + "_" + command.getAction());

        // Génération du nom si non spécifié
        String name = command.getName();
        if (!StringUtils.hasText(name)) {
            name = command.getResourceType().toUpperCase() + "_" + command.getAction().toUpperCase();
        }

        // Vérification de l'unicité du nom
        if (permissionRepository.existsByName(name)) {
            throw new BusinessException("permission.name.duplicate", "Une permission avec ce nom existe déjà");
        }

        // Vérification de l'unicité de la combinaison resourceType et action
        if (permissionRepository.existsByResourceTypeAndAction(command.getResourceType(), command.getAction())) {
            throw new BusinessException("permission.resource.action.duplicate", "Une permission avec ce type de ressource et cette action existe déjà");
        }

        // Création de la permission
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(command.getDescription());
        permission.setResourceType(command.getResourceType());
        permission.setAction(command.getAction());
        permission.setSystemDefined(command.isSystemDefined());

        // Sauvegarde de la permission
        permission = permissionRepository.save(permission);

        return permissionMapper.toDTO(permission);
    }
}
