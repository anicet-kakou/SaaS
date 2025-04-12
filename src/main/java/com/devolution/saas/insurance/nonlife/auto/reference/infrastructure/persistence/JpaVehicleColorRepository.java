package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleColor;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleColorRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les couleurs de véhicule.
 */
@Repository
public interface JpaVehicleColorRepository extends JpaRepository<VehicleColor, UUID>, JpaSpecificationExecutor<VehicleColor>, VehicleColorRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleColor> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleColor save(VehicleColor vehicleColor);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM VehicleColor c WHERE c.code = :code AND c.organizationId = :organizationId")
    Optional<VehicleColor> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM VehicleColor c WHERE c.organizationId = :organizationId")
    List<VehicleColor> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM VehicleColor c WHERE c.organizationId = :organizationId AND c.isActive = true")
    List<VehicleColor> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
