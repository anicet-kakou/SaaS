package com.devolution.saas.core.audit.infrastructure.persistence;

import com.devolution.saas.core.audit.domain.model.AuditLog;
import com.devolution.saas.core.audit.domain.model.AuditStatus;
import com.devolution.saas.core.audit.domain.repository.AuditLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les opérations sur les logs d'audit.
 */
@Repository
public interface JpaAuditLogRepository extends JpaRepository<AuditLog, UUID>, JpaSpecificationExecutor<AuditLog>, AuditLogRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    List<AuditLog> findAllByAction(String action);

    /**
     * {@inheritDoc}
     */
    @Override
    List<AuditLog> findAllByUserId(UUID userId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<AuditLog> findAllByOrganizationId(UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<AuditLog> findAllByEntityTypeAndEntityId(String entityType, String entityId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<AuditLog> findAllByStatus(AuditStatus status);

    /**
     * {@inheritDoc}
     */
    @Override
    List<AuditLog> findAllByActionTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * {@inheritDoc}
     */
    @Override
    List<AuditLog> findAllByIpAddress(String ipAddress);
}
