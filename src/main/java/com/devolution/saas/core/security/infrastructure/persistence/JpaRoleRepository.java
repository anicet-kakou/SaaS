package com.devolution.saas.core.security.infrastructure.persistence;

import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les opérations sur les rôles.
 */
@Repository
public interface JpaRoleRepository extends JpaRepository<Role, UUID>, RoleRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Role> findByName(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Role> findByNameAndOrganizationId(String name, UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<Role> findAllBySystemDefined(boolean systemDefined);

    /**
     * {@inheritDoc}
     */
    @Override
    List<Role> findAllByOrganizationId(UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    boolean existsByNameAndOrganizationId(String name, UUID organizationId);
}
