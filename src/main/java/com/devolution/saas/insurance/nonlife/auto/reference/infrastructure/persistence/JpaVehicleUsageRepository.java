package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleUsageRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les types d'usage de véhicule.
 */
@Repository
public interface JpaVehicleUsageRepository extends JpaRepository<VehicleUsage, UUID>, JpaSpecificationExecutor<VehicleUsage>, VehicleUsageRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleUsage> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleUsage save(VehicleUsage vehicleUsage);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT u FROM VehicleUsage u WHERE u.code = :code AND u.organizationId = :organizationId")
    Optional<VehicleUsage> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT u FROM VehicleUsage u WHERE u.organizationId = :organizationId")
    List<VehicleUsage> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT u FROM VehicleUsage u WHERE u.organizationId = :organizationId AND u.isActive = true")
    List<VehicleUsage> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
