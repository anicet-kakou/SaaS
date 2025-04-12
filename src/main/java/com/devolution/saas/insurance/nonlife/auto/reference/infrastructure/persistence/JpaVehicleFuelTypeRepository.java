package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleFuelType;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleFuelTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les types de carburant de véhicule.
 */
@Repository
public interface JpaVehicleFuelTypeRepository extends JpaRepository<VehicleFuelType, UUID>, JpaSpecificationExecutor<VehicleFuelType>, VehicleFuelTypeRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleFuelType> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleFuelType save(VehicleFuelType vehicleFuelType);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT f FROM VehicleFuelType f WHERE f.code = :code AND f.organizationId = :organizationId")
    Optional<VehicleFuelType> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT f FROM VehicleFuelType f WHERE f.organizationId = :organizationId")
    List<VehicleFuelType> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT f FROM VehicleFuelType f WHERE f.organizationId = :organizationId AND f.isActive = true")
    List<VehicleFuelType> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
