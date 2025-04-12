package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.CirculationZoneRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les zones de circulation.
 */
@Repository
public interface JpaCirculationZoneRepository extends JpaRepository<CirculationZone, UUID>, JpaSpecificationExecutor<CirculationZone>, CirculationZoneRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<CirculationZone> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    CirculationZone save(CirculationZone circulationZone);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CirculationZone c WHERE c.code = :code AND c.organizationId = :organizationId")
    Optional<CirculationZone> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CirculationZone c WHERE c.organizationId = :organizationId")
    List<CirculationZone> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CirculationZone c WHERE c.organizationId = :organizationId AND c.isActive = true")
    List<CirculationZone> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
