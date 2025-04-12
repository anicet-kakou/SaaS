package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleManufacturerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les fabricants de véhicule.
 */
@Repository
public interface JpaVehicleManufacturerRepository extends JpaRepository<VehicleManufacturer, UUID>, JpaSpecificationExecutor<VehicleManufacturer>, VehicleManufacturerRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleManufacturer> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleManufacturer save(VehicleManufacturer vehicleManufacturer);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT m FROM VehicleManufacturer m WHERE m.code = :code AND m.organizationId = :organizationId")
    Optional<VehicleManufacturer> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT m FROM VehicleManufacturer m WHERE m.organizationId = :organizationId")
    List<VehicleManufacturer> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT m FROM VehicleManufacturer m WHERE m.organizationId = :organizationId AND m.isActive = true")
    List<VehicleManufacturer> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
