package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.FuelType;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.FuelTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les types de carburant.
 */
@Repository
public interface JpaFuelTypeRepository extends JpaRepository<FuelType, UUID>, JpaSpecificationExecutor<FuelType>, FuelTypeRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<FuelType> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    FuelType save(FuelType fuelType);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT f FROM FuelType f WHERE f.code = :code AND f.organizationId = :organizationId")
    Optional<FuelType> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT f FROM FuelType f WHERE f.organizationId = :organizationId")
    List<FuelType> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT f FROM FuelType f WHERE f.organizationId = :organizationId AND f.isActive = true")
    List<FuelType> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
