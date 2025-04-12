package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleBodyTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les types de carrosserie de véhicule.
 */
@Repository
public interface JpaVehicleBodyTypeRepository extends JpaRepository<VehicleBodyType, UUID>, JpaSpecificationExecutor<VehicleBodyType>, VehicleBodyTypeRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleBodyType> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleBodyType save(VehicleBodyType vehicleBodyType);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT b FROM VehicleBodyType b WHERE b.code = :code AND b.organizationId = :organizationId")
    Optional<VehicleBodyType> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT b FROM VehicleBodyType b WHERE b.organizationId = :organizationId")
    List<VehicleBodyType> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT b FROM VehicleBodyType b WHERE b.organizationId = :organizationId AND b.isActive = true")
    List<VehicleBodyType> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
