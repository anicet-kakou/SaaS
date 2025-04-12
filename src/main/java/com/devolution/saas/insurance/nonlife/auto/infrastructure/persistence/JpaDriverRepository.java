package com.devolution.saas.insurance.nonlife.auto.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.DriverRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les conducteurs.
 */
@Repository
public interface JpaDriverRepository extends JpaRepository<Driver, UUID>, JpaSpecificationExecutor<Driver>, DriverRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<Driver> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    Driver save(Driver driver);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT d FROM Driver d WHERE d.licenseNumber = :licenseNumber AND d.organizationId = :organizationId")
    Optional<Driver> findByLicenseNumberAndOrganizationId(@Param("licenseNumber") String licenseNumber, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT d FROM Driver d WHERE d.organizationId = :organizationId")
    List<Driver> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT d FROM Driver d WHERE d.customerId = :customerId AND d.organizationId = :organizationId")
    List<Driver> findAllByCustomerIdAndOrganizationId(@Param("customerId") UUID customerId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT d FROM Driver d WHERE d.customerId = :customerId AND d.isPrimaryDriver = true AND d.organizationId = :organizationId")
    Optional<Driver> findByCustomerIdAndIsPrimaryDriverTrueAndOrganizationId(@Param("customerId") UUID customerId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
