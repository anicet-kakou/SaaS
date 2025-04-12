package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entité représentant le bonus-malus d'un client.
 */
@Entity
@Table(name = "bonus_malus")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BonusMalus extends TenantAwareEntity {

    /**
     * ID du client.
     */
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    /**
     * Coefficient de bonus-malus.
     */
    @Column(name = "coefficient", nullable = false, precision = 5, scale = 2)
    private BigDecimal coefficient;

    /**
     * Date d'effet du bonus-malus.
     */
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    /**
     * Date d'expiration du bonus-malus.
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /**
     * Coefficient précédent de bonus-malus.
     */
    @Column(name = "previous_coefficient", precision = 5, scale = 2)
    private BigDecimal previousCoefficient;

    /**
     * Nombre d'années sans sinistre.
     */
    @Column(name = "years_without_claim", nullable = false)
    private int yearsWithoutClaim;

    // The organizationId field is inherited from TenantAwareEntity
}
