package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.abstracts.AbstractUpdateUseCase;
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

import java.util.UUID;

/**
 * Cas d'utilisation pour la mise à jour d'une permission.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdatePermission extends AbstractUpdateUseCase<PermissionDTO, UpdatePermissionCommand, Permission, UUID> {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    protected Permission getEntity(UUID id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", id));
    }

    @Override
    protected Permission updateEntity(Permission entity, UpdatePermissionCommand command) {
        log.debug("Mise à jour de la permission: {}", command.getId());

        // Vérification de l'unicité du nom
        if (!entity.getName().equals(command.getName()) &&
                permissionRepository.existsByName(command.getName())) {
            throw new BusinessException("permission.name.duplicate", "Une permission avec ce nom existe déjà");
        }

        // Vérification de l'unicité de la combinaison resourceType et action
        if ((!entity.getResourceType().equals(command.getResourceType()) ||
                !entity.getAction().equals(command.getAction())) &&
                permissionRepository.existsByResourceTypeAndAction(command.getResourceType(), command.getAction())) {
            throw new BusinessException("permission.resource.action.duplicate", "Une permission avec ce type de ressource et cette action existe déjà");
        }

        // Mise à jour des propriétés
        entity.setName(command.getName());
        entity.setDescription(command.getDescription());
        entity.setResourceType(command.getResourceType());
        entity.setAction(command.getAction());

        // Sauvegarde de la permission
        return permissionRepository.save(entity);
    }

    @Override
    protected PermissionDTO toDto(Permission entity) {
        return permissionMapper.toDTO(entity);
    }

    @Override
    protected boolean isEntityModifiable(Permission entity) {
        return !entity.isSystemDefined();
    }

    @Override
    protected String getEntityName() {
        return "permission";
    }

    @Override
    protected UUID getIdFromCommand(UpdatePermissionCommand command) {
        return command.getId();
    }
}
