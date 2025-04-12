package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.GeographicZoneRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les zones géographiques.
 */
@Repository
public interface JpaGeographicZoneRepository extends JpaRepository<GeographicZone, UUID>, JpaSpecificationExecutor<GeographicZone>, GeographicZoneRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<GeographicZone> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    GeographicZone save(GeographicZone geographicZone);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT g FROM GeographicZone g WHERE g.code = :code AND g.organizationId = :organizationId")
    Optional<GeographicZone> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT g FROM GeographicZone g WHERE g.organizationId = :organizationId")
    List<GeographicZone> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT g FROM GeographicZone g WHERE g.organizationId = :organizationId AND g.isActive = true")
    List<GeographicZone> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
