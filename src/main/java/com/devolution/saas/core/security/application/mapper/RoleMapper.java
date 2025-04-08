package com.devolution.saas.core.security.application.mapper;

import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre les entités Role et les DTOs RoleDTO.
 */
@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final PermissionMapper permissionMapper;

    /**
     * Convertit une entité Role en DTO.
     *
     * @param role Entité Role
     * @return DTO RoleDTO
     */
    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }

        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .organizationId(role.getOrganizationId())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .systemDefined(role.isSystemDefined())
                .permissions(mapPermissionsToDTOs(role.getPermissions()))
                .build();
    }

    /**
     * Convertit un ensemble de permissions en ensemble de DTOs.
     *
     * @param permissions Ensemble de permissions
     * @return Ensemble de DTOs PermissionDTO
     */
    private Set<PermissionDTO> mapPermissionsToDTOs(Set<Permission> permissions) {
        if (permissions == null) {
            return Set.of();
        }

        return permissions.stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toSet());
    }
}
