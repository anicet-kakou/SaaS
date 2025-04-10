package com.devolution.saas.insurance.nonlife.auto.infrastructure.persistence;

import com.devolution.saas.insurance.nonlife.auto.domain.model.BonusMalus;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.BonusMalusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour le bonus-malus.
 */
@Repository
public interface JpaBonusMalusRepository extends JpaRepository<BonusMalus, UUID>, JpaSpecificationExecutor<BonusMalus>, BonusMalusRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<BonusMalus> findById(UUID id);

    /**
     * {@inheritDoc}
     */
    @Override
    BonusMalus save(BonusMalus bonusMalus);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT b FROM BonusMalus b WHERE b.customerId = :customerId AND b.organizationId = :organizationId " +
            "AND (b.expiryDate IS NULL OR b.expiryDate >= CURRENT_DATE) ORDER BY b.effectiveDate DESC")
    Optional<BonusMalus> findActiveByCustomerId(@Param("customerId") UUID customerId,
                                                @Param("organizationId") UUID organizationId);

    /**
     * {@inheritDoc}
     */
    @Override
    void deleteById(UUID id);

    /**
     * Trouve le bonus-malus actif d'un client à une date donnée.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @param date           La date à laquelle le bonus-malus doit être actif
     * @return Le bonus-malus trouvé, ou empty si non trouvé
     */
    @Query("SELECT b FROM BonusMalus b WHERE b.customerId = :customerId AND b.organizationId = :organizationId " +
            "AND b.effectiveDate <= :date AND (b.expiryDate IS NULL OR b.expiryDate >= :date) ORDER BY b.effectiveDate DESC")
    Optional<BonusMalus> findActiveByCustomerIdAndDate(@Param("customerId") UUID customerId,
                                                       @Param("organizationId") UUID organizationId,
                                                       @Param("date") LocalDate date);
}
