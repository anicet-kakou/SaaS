package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour supprimer un rôle.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteRole {

    private final RoleRepository roleRepository;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID du rôle à supprimer
     */
    @Transactional
    public void execute(UUID id) {
        log.debug("Suppression du rôle: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

        // Vérification que le rôle n'est pas défini par le système
        if (role.isSystemDefined()) {
            throw new BusinessException("role.system.nodelete", "Les rôles définis par le système ne peuvent pas être supprimés");
        }

        // Vérification que le rôle n'est pas utilisé par des utilisateurs
        if (!role.getUsers().isEmpty()) {
            throw new BusinessException("role.in.use", "Ce rôle est attribué à des utilisateurs et ne peut pas être supprimé");
        }

        roleRepository.delete(role);
    }
}
