package com.devolution.saas.core.security.application.mapper;

import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.dto.UserOrganizationDTO;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.model.UserOrganization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir les entités User en DTOs et vice-versa.
 *
 * @author Cyr Leonce Anicet KAKOU <cyrkakou@gmail.com>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserMapper {

    private final OrganizationRepository organizationRepository;

    /**
     * Convertit une entité User en DTO.
     *
     * @param user Entité User
     * @return DTO UserDTO
     */
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                // Suppression de l'appel à fullName car cette propriété est calculée dans le record
                .phone(user.getPhone())
                .status(user.getStatus())
                .profilePictureUrl(user.getProfilePictureUrl())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .organizationId(user.getOrganizationId())
                .active(user.isActive())
                .locked(user.isLocked())
                .roles(mapRolesToDTOs(user.getRoles()))
                .organizations(mapUserOrganizationsToDTOs(user.getOrganizations()))
                .build();
    }

    /**
     * Convertit un ensemble de rôles en ensemble de DTOs.
     *
     * @param roles Ensemble de rôles
     * @return Ensemble de DTOs RoleDTO
     */
    private Set<RoleDTO> mapRolesToDTOs(Set<Role> roles) {
        return roles.stream()
                .map(role -> RoleDTO.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .organizationId(role.getOrganizationId())
                        .createdAt(role.getCreatedAt())
                        .updatedAt(role.getUpdatedAt())
                        .systemDefined(role.isSystemDefined())
                        .permissions(mapPermissionsToDTOs(role.getPermissions()))
                        .build())
                .collect(Collectors.toSet());
    }

    /**
     * Convertit un ensemble de permissions en ensemble de DTOs.
     *
     * @param permissions Ensemble de permissions
     * @return Ensemble de DTOs PermissionDTO
     */
    private Set<PermissionDTO> mapPermissionsToDTOs(Set<Permission> permissions) {
        return permissions.stream()
                .map(permission -> PermissionDTO.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .description(permission.getDescription())
                        .resourceType(permission.getResourceType())
                        .action(permission.getAction())
                        .createdAt(permission.getCreatedAt())
                        .updatedAt(permission.getUpdatedAt())
                        .systemDefined(permission.isSystemDefined())
                        .build())
                .collect(Collectors.toSet());
    }

    /**
     * Convertit un ensemble de relations utilisateur-organisation en ensemble de DTOs.
     *
     * @param userOrganizations Ensemble de relations utilisateur-organisation
     * @return Ensemble de DTOs UserOrganizationDTO
     */
    private Set<UserOrganizationDTO> mapUserOrganizationsToDTOs(Set<UserOrganization> userOrganizations) {
        return userOrganizations.stream()
                .map(userOrg -> {
                    String orgName = "";
                    String orgCode = "";

                    // Récupération du nom et du code de l'organisation
                    try {
                        var org = organizationRepository.findById(userOrg.getOrganizationId());
                        if (org.isPresent()) {
                            orgName = org.get().getName();
                            orgCode = org.get().getCode();
                        }
                    } catch (Exception e) {
                        log.warn("Impossible de récupérer l'organisation: {}", userOrg.getOrganizationId());
                    }

                    return UserOrganizationDTO.builder()
                            .id(UUID.randomUUID()) // Générer un UUID pour le DTO
                            .userId(userOrg.getUserId())
                            .organizationId(userOrg.getOrganizationId())
                            .organizationName(orgName)
                            .organizationCode(orgCode)
                            .createdAt(userOrg.getCreatedAt())
                            .updatedAt(userOrg.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toSet());
    }
}
