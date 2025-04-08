package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.core.security.application.dto.PermissionDTO;
import com.devolution.saas.core.security.application.mapper.PermissionMapper;
import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Cas d'utilisation pour lister les permissions.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ListPermissions {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    /**
     * Exécute le cas d'utilisation pour lister toutes les permissions.
     *
     * @return Liste des DTOs de permissions
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> execute() {
        log.debug("Listage de toutes les permissions");

        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Exécute le cas d'utilisation pour lister les permissions par type de ressource.
     *
     * @param resourceType Type de ressource
     * @return Liste des DTOs de permissions
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> executeByResourceType(String resourceType) {
        log.debug("Listage des permissions par type de ressource: {}", resourceType);

        List<Permission> permissions = permissionRepository.findAllByResourceType(resourceType);
        return permissions.stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Exécute le cas d'utilisation pour lister les permissions système.
     *
     * @param systemDefined Indique si les permissions sont définies par le système
     * @return Liste des DTOs de permissions
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> executeBySystemDefined(boolean systemDefined) {
        log.debug("Listage des permissions système: {}", systemDefined);

        List<Permission> permissions = permissionRepository.findAllBySystemDefined(systemDefined);
        return permissions.stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
