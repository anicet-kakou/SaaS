package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.abstracts.AbstractCreateUseCase;
import com.devolution.saas.core.security.application.command.CreatePermissionCommand;
import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.mapper.PermissionMapper;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Cas d'utilisation pour la création d'une permission.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreatePermission extends AbstractCreateUseCase<PermissionDTO, CreatePermissionCommand, Permission> {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    protected Permission createEntity(CreatePermissionCommand command) {
        log.debug("Création d'une nouvelle permission: {}", command.resourceType() + "_" + command.action());

        // Génération du nom si non spécifié
        String name = command.name();
        if (!StringUtils.hasText(name)) {
            name = command.resourceType().toUpperCase() + "_" + command.action().toUpperCase();
        }

        // Création de la permission
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(command.description());
        permission.setResourceType(command.resourceType());
        permission.setAction(command.action());
        permission.setSystemDefined(command.systemDefined());

        // Sauvegarde de la permission
        return permissionRepository.save(permission);
    }

    @Override
    protected PermissionDTO toDto(Permission entity) {
        return permissionMapper.toDTO(entity);
    }

    @Override
    protected boolean existsByUniqueCriteria(CreatePermissionCommand command) {
        // Génération du nom si non spécifié
        String name = command.name();
        if (!StringUtils.hasText(name)) {
            name = command.resourceType().toUpperCase() + "_" + command.action().toUpperCase();
        }

        return permissionRepository.existsByName(name) ||
                permissionRepository.existsByResourceTypeAndAction(command.resourceType(), command.action());
    }

    @Override
    protected String getEntityName() {
        return "permission";
    }

    @Override
    protected String getUniqueFieldName() {
        return "identifiant";
    }
}
