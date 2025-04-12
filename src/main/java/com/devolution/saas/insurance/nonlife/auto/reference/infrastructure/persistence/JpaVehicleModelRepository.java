package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleModelRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les modèles de véhicule.
 */
@Repository
public interface JpaVehicleModelRepository extends JpaRepository<VehicleModel, UUID>, JpaSpecificationExecutor<VehicleModel>, VehicleModelRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleModel> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleModel save(VehicleModel vehicleModel);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT m FROM VehicleModel m WHERE m.code = :code AND m.manufacturerId = :makeId AND m.organizationId = :organizationId")
    Optional<VehicleModel> findByCodeAndMakeIdAndOrganizationId(@Param("code") String code, @Param("makeId") UUID makeId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT m FROM VehicleModel m WHERE m.organizationId = :organizationId")
    List<VehicleModel> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT m FROM VehicleModel m WHERE m.organizationId = :organizationId AND m.isActive = true")
    List<VehicleModel> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT m FROM VehicleModel m WHERE m.manufacturerId = :makeId AND m.organizationId = :organizationId")
    List<VehicleModel> findAllByMakeIdAndOrganizationId(@Param("makeId") UUID makeId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT m FROM VehicleModel m WHERE m.manufacturerId = :makeId AND m.organizationId = :organizationId AND m.isActive = true")
    List<VehicleModel> findAllActiveByMakeIdAndOrganizationId(@Param("makeId") UUID makeId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
