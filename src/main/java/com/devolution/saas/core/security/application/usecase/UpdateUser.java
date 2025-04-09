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

        // Mise à jour des champs
        if (command.getEmail() != null) {
            user.setEmail(command.getEmail());
        }
        if (command.getFirstName() != null) {
            user.setFirstName(command.getFirstName());
        }
        if (command.getLastName() != null) {
            user.setLastName(command.getLastName());
        }
        if (command.getPhone() != null) {
            user.setPhone(command.getPhone());
        }
        if (command.getStatus() != null) {
            user.setStatus(command.getStatus());
        }
        if (command.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(command.getProfilePictureUrl());
        }

        // Mise à jour de l'organisation principale si spécifiée
        if (command.getOrganizationId() != null && !command.getOrganizationId().equals(user.getOrganizationId())) {
            organizationRepository.findById(command.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", command.getOrganizationId()));
            user.setOrganizationId(command.getOrganizationId());
        }

        // Mise à jour des organisations si spécifiées
        if (!command.getOrganizationIds().isEmpty()) {
            // Suppression des organisations existantes
            user.getOrganizations().clear();

            // Ajout des nouvelles organisations
            for (UUID orgId : command.getOrganizationIds()) {
                organizationRepository.findById(orgId)
                        .orElseThrow(() -> new ResourceNotFoundException("Organization", orgId));

                UserOrganization userOrganization = new UserOrganization(user.getId(), orgId);
                user.addOrganization(userOrganization);
            }
        }

        // Mise à jour des rôles si spécifiés
        if (!command.getRoleIds().isEmpty()) {
            // Suppression des rôles existants
            user.getRoles().clear();

            // Ajout des nouveaux rôles
            for (UUID roleId : command.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
                user.addRole(role);
            }
        }

        // Sauvegarde de l'utilisateur
        return userRepository.save(user);
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
