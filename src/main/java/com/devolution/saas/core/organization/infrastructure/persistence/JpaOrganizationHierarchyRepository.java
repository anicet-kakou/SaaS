package com.devolution.saas.core.organization.infrastructure.persistence;

import com.devolution.saas.core.organization.domain.model.OrganizationHierarchy;
import com.devolution.saas.core.organization.domain.repository.OrganizationHierarchyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les opérations sur la hiérarchie des organisations.
 */
@Repository
public interface JpaOrganizationHierarchyRepository extends JpaRepository<OrganizationHierarchy, UUID>, OrganizationHierarchyRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    List<OrganizationHierarchy> findAllByAncestorId(UUID ancestorId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<OrganizationHierarchy> findAllByDescendantId(UUID descendantId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<OrganizationHierarchy> findAllByAncestorIdAndDistance(UUID ancestorId, int distance);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT h.descendantId FROM OrganizationHierarchy h WHERE h.ancestorId = :organizationId AND h.distance > 0")
    List<UUID> findAllDescendantIds(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT h.ancestorId FROM OrganizationHierarchy h WHERE h.descendantId = :organizationId AND h.distance > 0")
    List<UUID> findAllAncestorIds(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Modifying
    @Query("DELETE FROM OrganizationHierarchy h WHERE h.ancestorId = :organizationId OR h.descendantId = :organizationId")
    void deleteAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM OrganizationHierarchy h " +
            "WHERE h.ancestorId = :ancestorId AND h.descendantId = :descendantId AND h.distance > 0")
    boolean isAncestorOf(@Param("ancestorId") UUID ancestorId, @Param("descendantId") UUID descendantId);
}
