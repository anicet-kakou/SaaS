package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les catégories de véhicule.
 */
@Repository
public interface JpaVehicleCategoryRepository extends JpaRepository<VehicleCategory, UUID>, JpaSpecificationExecutor<VehicleCategory>, VehicleCategoryRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleCategory> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleCategory save(VehicleCategory vehicleCategory);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM VehicleCategory c WHERE c.code = :code AND c.organizationId = :organizationId")
    Optional<VehicleCategory> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM VehicleCategory c WHERE c.organizationId = :organizationId")
    List<VehicleCategory> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM VehicleCategory c WHERE c.organizationId = :organizationId AND c.isActive = true")
    List<VehicleCategory> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
