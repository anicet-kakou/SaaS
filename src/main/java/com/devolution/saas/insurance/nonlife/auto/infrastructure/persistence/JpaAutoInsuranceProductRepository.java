package com.devolution.saas.insurance.nonlife.auto.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoInsuranceProductRepository;
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
 * Implémentation JPA du repository pour les produits d'assurance auto.
 */
@Repository
public interface JpaAutoInsuranceProductRepository extends JpaRepository<AutoInsuranceProduct, UUID>, JpaSpecificationExecutor<AutoInsuranceProduct>, AutoInsuranceProductRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<AutoInsuranceProduct> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    AutoInsuranceProduct save(AutoInsuranceProduct product);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoInsuranceProduct p WHERE p.code = :code AND p.organizationId = :organizationId")
    Optional<AutoInsuranceProduct> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoInsuranceProduct p WHERE p.organizationId = :organizationId")
    List<AutoInsuranceProduct> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoInsuranceProduct p WHERE p.organizationId = :organizationId AND p.status = 'ACTIVE'")
    List<AutoInsuranceProduct> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM AutoInsuranceProduct p WHERE p.organizationId = :organizationId AND p.status = 'ACTIVE' " +
            "AND p.effectiveDate <= :date AND (p.expiryDate IS NULL OR p.expiryDate >= :date)")
    List<AutoInsuranceProduct> findAllActiveAtDateByOrganizationId(@Param("date") LocalDate date, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
