package com.devolution.saas.insurance.nonlife.auto.reference.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleSubcategory;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleSubcategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les sous-catégories de véhicule.
 */
@Repository
public interface JpaVehicleSubcategoryRepository extends JpaRepository<VehicleSubcategory, UUID>, JpaSpecificationExecutor<VehicleSubcategory>, VehicleSubcategoryRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<VehicleSubcategory> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    VehicleSubcategory save(VehicleSubcategory vehicleSubcategory);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT s FROM VehicleSubcategory s WHERE s.code = :code AND s.organizationId = :organizationId")
    Optional<VehicleSubcategory> findByCodeAndOrganizationId(@Param("code") String code, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT s FROM VehicleSubcategory s WHERE s.organizationId = :organizationId")
    List<VehicleSubcategory> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT s FROM VehicleSubcategory s WHERE s.organizationId = :organizationId AND s.isActive = true")
    List<VehicleSubcategory> findAllActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT s FROM VehicleSubcategory s WHERE s.categoryId = :categoryId AND s.organizationId = :organizationId")
    List<VehicleSubcategory> findAllByCategoryIdAndOrganizationId(@Param("categoryId") UUID categoryId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT s FROM VehicleSubcategory s WHERE s.categoryId = :categoryId AND s.organizationId = :organizationId AND s.isActive = true")
    List<VehicleSubcategory> findAllActiveByCategoryIdAndOrganizationId(@Param("categoryId") UUID categoryId, @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);
}
