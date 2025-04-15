package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.abstracts.AbstractUpdateUseCase;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import com.devolution.saas.core.security.application.command.UpdateUserCommand;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.mapper.UserMapper;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.model.UserOrganization;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Cas d'utilisation pour la mise à jour d'un utilisateur.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateUser extends AbstractUpdateUseCase<UserDTO, UpdateUserCommand, User, UUID> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final UserMapper userMapper;

    /**
     * Implémentation des méthodes abstraites de AbstractUpdateUseCase
     */
    @Override
    protected User getEntity(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Override
    protected User updateEntity(User user, UpdateUserCommand command) {
        // Vérification de l'unicité de l'email si modifié
        if (command.getEmail() != null && !command.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(command.getEmail())) {
            throw new ValidationException("L'adresse email est déjà utilisée");
        }

        // Créer un nouvel utilisateur avec les valeurs existantes
        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPasswordHash(user.getPassword());
        updatedUser.setEnabled(user.isEnabled());
        updatedUser.setLocked(user.isLocked());
        updatedUser.setAccountNonExpired(!user.isAccountNonExpired());
        updatedUser.setCredentialsNonExpired(!user.isCredentialsNonExpired());
        updatedUser.setLastLoginAt(user.getLastLoginAt());
        updatedUser.setCreatedAt(user.getCreatedAt());
        updatedUser.setUpdatedAt(user.getUpdatedAt());

        // Mise à jour des champs avec les valeurs de la commande ou les valeurs existantes
        updatedUser.setEmail(command.getEmail() != null ? command.getEmail() : user.getEmail());
        updatedUser.setFirstName(command.getFirstName() != null ? command.getFirstName() : user.getFirstName());
        updatedUser.setLastName(command.getLastName() != null ? command.getLastName() : user.getLastName());
        updatedUser.setPhone(command.getPhone() != null ? command.getPhone() : user.getPhone());
        updatedUser.setStatus(command.getStatus() != null ? command.getStatus() : user.getStatus());
        updatedUser.setProfilePictureUrl(command.getProfilePictureUrl() != null ? command.getProfilePictureUrl() : user.getProfilePictureUrl());

        // Mise à jour de l'organisation principale si spécifiée
        UUID organizationId = user.getOrganizationId();
        if (command.getOrganizationId() != null && !command.getOrganizationId().equals(user.getOrganizationId())) {
            organizationRepository.findById(command.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", command.getOrganizationId()));
            organizationId = command.getOrganizationId();
        }
        updatedUser.setOrganizationId(organizationId);

        // Créer un nouvel ensemble d'organisations si spécifié
        java.util.Set<UserOrganization> organizations = new java.util.HashSet<>(user.getOrganizations());
        if (!command.getOrganizationIds().isEmpty()) {
            organizations = new java.util.HashSet<>();
            for (UUID orgId : command.getOrganizationIds()) {
                organizationRepository.findById(orgId)
                        .orElseThrow(() -> new ResourceNotFoundException("Organization", orgId));
                organizations.add(new UserOrganization(user.getId(), orgId));
            }
        }
        updatedUser.setOrganizations(organizations);

        // Créer un nouvel ensemble de rôles si spécifié
        java.util.Set<Role> roles = new java.util.HashSet<>(user.getRoles());
        if (!command.getRoleIds().isEmpty()) {
            roles = new java.util.HashSet<>();
            for (UUID roleId : command.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
                roles.add(role);
            }
        }
        updatedUser.setRoles(roles);

        // Sauvegarder le nouvel utilisateur
        return userRepository.save(updatedUser);
    }

    @Override
    protected UserDTO toDto(User entity) {
        return userMapper.toDTO(entity);
    }

    @Override
    protected boolean isEntityModifiable(User entity) {
        // Tous les utilisateurs sont modifiables pour le moment
        return true;
    }

    @Override
    protected String getEntityName() {
        return "utilisateur";
    }

    @Override
    protected UUID getIdFromCommand(UpdateUserCommand command) {
        return command.getId();
    }
}
