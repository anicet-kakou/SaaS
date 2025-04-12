package com.devolution.saas.insurance.nonlife.auto.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoPolicyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les polices d'assurance automobile.
 */
@Repository
public interface JpaAutoPolicyRepository extends JpaRepository<AutoPolicy, UUID>, JpaSpecificationExecutor<AutoPolicy>, AutoPolicyRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<AutoPolicy> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    AutoPolicy save(AutoPolicy autoPolicy);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoPolicy p WHERE p.policyNumber = :policyNumber AND p.organizationId = :organizationId")
    Optional<AutoPolicy> findByPolicyNumberAndOrganizationId(@Param("policyNumber") String policyNumber, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoPolicy p WHERE p.organizationId = :organizationId")
    List<AutoPolicy> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoPolicy p WHERE p.organizationId = :organizationId AND p.status = 'ACTIVE'")
    List<AutoPolicy> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoPolicy p WHERE p.vehicle.id = :vehicleId AND p.organizationId = :organizationId")
    List<AutoPolicy> findAllByVehicleIdAndOrganizationId(@Param("vehicleId") UUID vehicleId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoPolicy p WHERE p.primaryDriver.id = :driverId AND p.organizationId = :organizationId")
    List<AutoPolicy> findAllByPrimaryDriverIdAndOrganizationId(@Param("driverId") UUID driverId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoPolicy p WHERE p.endDate BETWEEN :startDate AND :endDate AND p.organizationId = :organizationId")
    List<AutoPolicy> findAllByEndDateBetweenAndOrganizationId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
