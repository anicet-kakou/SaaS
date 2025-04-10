package com.devolution.saas.insurance.nonlife.auto.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les véhicules.
 */
@Repository
public interface JpaVehicleRepository extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle>, VehicleRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Vehicle> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    Vehicle save(Vehicle vehicle);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM Vehicle v WHERE v.registrationNumber = :registrationNumber AND v.organizationId = :organizationId")
    Optional<Vehicle> findByRegistrationNumber(@Param("registrationNumber") String registrationNumber, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM Vehicle v WHERE v.organizationId = :organizationId")
    List<Vehicle> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM Vehicle v WHERE v.ownerId = :ownerId AND v.organizationId = :organizationId")
    List<Vehicle> findAllByOwnerIdAndOrganizationId(@Param("ownerId") UUID ownerId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
