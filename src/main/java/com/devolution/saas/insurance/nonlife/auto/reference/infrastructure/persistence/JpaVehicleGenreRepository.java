package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleGenre;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleGenreRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les genres de véhicule.
 */
@Repository
public interface JpaVehicleGenreRepository extends JpaRepository<VehicleGenre, UUID>, JpaSpecificationExecutor<VehicleGenre>, VehicleGenreRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleGenre> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleGenre save(VehicleGenre vehicleGenre);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT g FROM VehicleGenre g WHERE g.code = :code AND g.organizationId = :organizationId")
    Optional<VehicleGenre> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT g FROM VehicleGenre g WHERE g.organizationId = :organizationId")
    List<VehicleGenre> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT g FROM VehicleGenre g WHERE g.organizationId = :organizationId AND g.isActive = true")
    List<VehicleGenre> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
