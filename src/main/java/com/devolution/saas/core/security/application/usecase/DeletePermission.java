package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour supprimer une permission.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DeletePermission {

    private final PermissionRepository permissionRepository;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID de la permission à supprimer
     */
    @Transactional
    public void execute(UUID id) {
        log.debug("Suppression de la permission: {}", id);

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", id));

        // Vérification que la permission n'est pas définie par le système
        if (permission.isSystemDefined()) {
            throw new BusinessException("permission.system.nodelete", "Les permissions définies par le système ne peuvent pas être supprimées");
        }

        // Vérification que la permission n'est pas utilisée par des rôles
        if (!permission.getRoles().isEmpty()) {
            throw new BusinessException("permission.in.use", "Cette permission est utilisée par des rôles et ne peut pas être supprimée");
        }

        permissionRepository.delete(permission);
    }
}
