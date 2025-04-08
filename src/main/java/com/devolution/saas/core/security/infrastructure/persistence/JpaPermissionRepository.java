package com.devolution.saas.core.security.infrastructure.persistence;

import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les opérations sur les permissions.
 */
@Repository
public interface JpaPermissionRepository extends JpaRepository<Permission, UUID>, PermissionRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Permission> findByName(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Permission> findByResourceTypeAndAction(String resourceType, String action);

    /**
     * {@inheritDoc}
     */
    @Override
    List<Permission> findAllByResourceType(String resourceType);

    /**
     * {@inheritDoc}
     */
    @Override
    List<Permission> findAllBySystemDefined(boolean systemDefined);

    /**
     * {@inheritDoc}
     */
    @Override
    boolean existsByName(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    boolean existsByResourceTypeAndAction(String resourceType, String action);
}
