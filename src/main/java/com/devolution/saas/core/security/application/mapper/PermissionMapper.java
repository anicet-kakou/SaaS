package com.devolution.saas.core.security.application.mapper;

import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.domain.model.Permission;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités Permission et les DTOs PermissionDTO.
 */
@Component
public class PermissionMapper {

    /**
     * Convertit une entité Permission en DTO.
     *
     * @param permission Entité Permission
     * @return DTO PermissionDTO
     */
    public PermissionDTO toDTO(Permission permission) {
        if (permission == null) {
            return null;
        }

        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .resourceType(permission.getResourceType())
                .action(permission.getAction())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .systemDefined(permission.isSystemDefined())
                .build();
    }
}
