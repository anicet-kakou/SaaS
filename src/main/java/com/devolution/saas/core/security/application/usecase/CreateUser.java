package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.abstracts.AbstractCreateUseCase;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import com.devolution.saas.core.security.application.command.CreateUserCommand;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.mapper.UserMapper;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.model.UserOrganization;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Cas d'utilisation pour la création d'un utilisateur.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateUser extends AbstractCreateUseCase<UserDTO, CreateUserCommand, User> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Implémentation des méthodes abstraites de AbstractCreateUseCase
     */
    @Override
    protected User createEntity(CreateUserCommand command) {
        // Vérification de l'existence de l'organisation principale
        organizationRepository.findById(command.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", command.getOrganizationId()));

        // Création de l'utilisateur
        User user = new User();
        user.setUsername(command.getUsername());
        user.setEmail(command.getEmail());
        user.setPasswordHash(passwordEncoder.encode(command.getPassword()));
        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setPhone(command.getPhone());
        user.setStatus(command.getStatus());
        user.setProfilePictureUrl(command.getProfilePictureUrl());
        user.setOrganizationId(command.getOrganizationId());
        user.setLastPasswordChangeDate(LocalDateTime.now());

        // Ajout des organisations
        Set<UUID> organizationIds = command.getOrganizationIds();
        if (organizationIds.isEmpty()) {
            // Si aucune organisation n'est spécifiée, ajouter l'organisation principale
            organizationIds = Set.of(command.getOrganizationId());
        }

        for (UUID orgId : organizationIds) {
            // Vérification de l'existence de l'organisation
            organizationRepository.findById(orgId)
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", orgId));

            UserOrganization userOrganization = new UserOrganization(user.getId(), orgId);
            user.addOrganization(userOrganization);
        }

        // Ajout des rôles
        for (UUID roleId : command.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
            user.addRole(role);
        }

        // Sauvegarde de l'utilisateur
        return userRepository.save(user);
    }

    @Override
    protected UserDTO toDto(User entity) {
        return userMapper.toDTO(entity);
    }

    @Override
    protected boolean existsByUniqueCriteria(CreateUserCommand command) {
        return userRepository.existsByUsername(command.getUsername()) ||
                userRepository.existsByEmail(command.getEmail());
    }

    @Override
    protected String getEntityName() {
        return "utilisateur";
    }

    @Override
    protected String getUniqueFieldName() {
        return "identifiant";
    }
}
