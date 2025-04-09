package com.devolution.saas.core.organization.infrastructure.persistence;

import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les opérations sur les organisations.
 */
@Repository
public interface JpaOrganizationRepository extends JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization>, OrganizationRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Organization> findByCode(String code);

    /**
     * {@inheritDoc}
     */
    @Override
    List<Organization> findAllByStatus(OrganizationStatus status);

    /**
     * {@inheritDoc}
     */
    @Override
    List<Organization> findAllByType(OrganizationType type);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT o FROM Organization o WHERE o.parent.id = :parentId")
    List<Organization> findAllByParentId(@Param("parentId") UUID parentId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT o FROM Organization o WHERE o.parent IS NULL")
    List<Organization> findAllRoots();

    /**
     * {@inheritDoc}
     */
    @Override
    boolean existsByCode(String code);

    @Query("SELECT o FROM Organization o LEFT JOIN FETCH o.parent WHERE o.id = :id")
    Optional<Organization> findByIdWithParent(@Param("id") UUID id);

    @Query("""
                SELECT DISTINCT o FROM Organization o 
                LEFT JOIN FETCH o.parent 
                WHERE o.id IN :ids
            """)
    List<Organization> findAllByIdsWithParent(@Param("ids") Collection<UUID> ids);
}
