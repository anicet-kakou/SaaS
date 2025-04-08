package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.core.security.application.dto.RoleDTO;
import com.devolution.saas.core.security.application.mapper.RoleMapper;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cas d'utilisation pour lister les rôles.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ListRoles {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Exécute le cas d'utilisation pour lister tous les rôles.
     *
     * @return Liste des DTOs de rôles
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> execute() {
        log.debug("Listage de tous les rôles");

        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Exécute le cas d'utilisation pour lister les rôles par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs de rôles
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> executeByOrganization(UUID organizationId) {
        log.debug("Listage des rôles par organisation: {}", organizationId);

        List<Role> roles = roleRepository.findAllByOrganizationId(organizationId);
        return roles.stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Exécute le cas d'utilisation pour lister les rôles système.
     *
     * @param systemDefined Indique si les rôles sont définis par le système
     * @return Liste des DTOs de rôles
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> executeBySystemDefined(boolean systemDefined) {
        log.debug("Listage des rôles système: {}", systemDefined);

        List<Role> roles = roleRepository.findAllBySystemDefined(systemDefined);
        return roles.stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }
}
