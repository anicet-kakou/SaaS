package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.mapper.PermissionMapper;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour récupérer une permission par son ID.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetPermission {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID de la permission
     * @return DTO de la permission
     */
    @Transactional(readOnly = true)
    public PermissionDTO execute(UUID id) {
        log.debug("Récupération de la permission: {}", id);

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", id));

        return permissionMapper.toDTO(permission);
    }
}
