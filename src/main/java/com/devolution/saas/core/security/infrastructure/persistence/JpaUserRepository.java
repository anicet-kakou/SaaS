package com.devolution.saas.core.security.infrastructure.persistence;

import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.model.UserStatus;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les opérations sur les utilisateurs.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User>, UserRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<User> findByUsername(String username);

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<User> findByEmail(String email);

    /**
     * {@inheritDoc}
     */
    @Override
    List<User> findAllByStatus(UserStatus status);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT u FROM User u JOIN u.organizations o WHERE o.organizationId = :organizationId")
    List<User> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    boolean existsByUsername(String username);

    /**
     * {@inheritDoc}
     */
    @Override
    boolean existsByEmail(String email);
}
