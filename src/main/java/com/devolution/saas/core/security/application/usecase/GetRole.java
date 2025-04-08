package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.mapper.RoleMapper;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour récupérer un rôle par son ID.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetRole {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID du rôle
     * @return DTO du rôle
     */
    @Transactional(readOnly = true)
    public RoleDTO execute(UUID id) {
        log.debug("Récupération du rôle: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

        return roleMapper.toDTO(role);
    }
}
