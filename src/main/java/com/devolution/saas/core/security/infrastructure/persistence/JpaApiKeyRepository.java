package com.devolution.saas.core.security.infrastructure.persistence;

import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les opérations sur les clés API.
 */
@Repository
public interface JpaApiKeyRepository extends JpaRepository<ApiKey, UUID>, ApiKeyRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<ApiKey> findByPrefixAndKeyHash(String prefix, String keyHash);

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<ApiKey> findByPrefixAndStatus(String prefix, ApiKeyStatus status);

    /**
     * {@inheritDoc}
     */
    @Override
    List<ApiKey> findAllByOrganizationId(UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<ApiKey> findAllByStatus(ApiKeyStatus status);

    /**
     * {@inheritDoc}
     */
    @Override
    List<ApiKey> findAllByExpiresAtBefore(LocalDateTime expiryDate);

    /**
     * {@inheritDoc}
     */
    @Override
    boolean existsByNameAndOrganizationId(String name, UUID organizationId);
}
