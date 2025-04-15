package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.common.util.Validation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    // Constants for bonus-malus calculation
    private static final BigDecimal MIN_COEFFICIENT = new BigDecimal("0.50");
    private static final BigDecimal MAX_COEFFICIENT = new BigDecimal("3.50");
    private static final BigDecimal INITIAL_COEFFICIENT = new BigDecimal("1.00");
    private static final BigDecimal ANNUAL_REDUCTION_RATE = new BigDecimal("0.05");
    private static final BigDecimal CLAIM_SURCHARGE_RATE = new BigDecimal("0.25");

    // Constante pour l'échelle décimale
    private static final int DECIMAL_SCALE = 2;

    /**
     * Crée un bonus-malus initial pour un nouveau client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return Le bonus-malus initial
     */
    @Transient
    public static BonusMalus createInitial(UUID customerId, UUID organizationId) {
        // Validate input parameters
        Validation.validateNotNull(customerId, "ID du client");
        Validation.validateNotNull(organizationId, "ID de l'organisation");
        return BonusMalus.builder()
                .customerId(customerId)
                .coefficient(INITIAL_COEFFICIENT)
                .previousCoefficient(INITIAL_COEFFICIENT)
                .effectiveDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(1))
                .yearsWithoutClaim(0)
                .organizationId(organizationId)
                .build();
    }

    /**
     * Calcule le nouveau coefficient de bonus-malus en fonction du nombre de sinistres.
     *
     * @param claimCount Le nombre de sinistres responsables
     * @return Le nouveau coefficient de bonus-malus
     */
    @Transient
    public BigDecimal calculateNewCoefficient(int claimCount) {
        // Validate claimCount to prevent negative values
        Validation.validateNotNegative(claimCount, "nombre de sinistres");

        if (claimCount == 0) {
            return applyAnnualReduction();
        } else {
            BigDecimal newCoefficient = this.coefficient;
            for (int i = 0; i < claimCount; i++) {
                newCoefficient = applySurchargeForClaim(newCoefficient);
            }
            return newCoefficient;
        }
    }

    /**
     * Applique la réduction annuelle pour absence de sinistre.
     *
     * @return Le nouveau coefficient après réduction
     */
    @Transient
    public BigDecimal applyAnnualReduction() {
        // Check if coefficient is null to avoid NPE
        if (this.coefficient == null) {
            return MIN_COEFFICIENT;
        }

        BigDecimal reduction = this.coefficient.multiply(ANNUAL_REDUCTION_RATE);
        BigDecimal newCoefficient = this.coefficient.subtract(reduction);

        // Ne pas descendre en dessous du coefficient minimum
        return newCoefficient.compareTo(MIN_COEFFICIENT) < 0 ?
                MIN_COEFFICIENT :
                newCoefficient.setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Applique une majoration pour un sinistre responsable.
     *
     * @param currentCoefficient Le coefficient actuel
     * @return Le nouveau coefficient après majoration
     */
    @Transient
    private BigDecimal applySurchargeForClaim(BigDecimal currentCoefficient) {
        BigDecimal surcharge = currentCoefficient.multiply(CLAIM_SURCHARGE_RATE);
        BigDecimal newCoefficient = currentCoefficient.add(surcharge);

        // Ne pas dépasser le coefficient maximum
        return newCoefficient.compareTo(MAX_COEFFICIENT) > 0 ?
                MAX_COEFFICIENT :
                newCoefficient.setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Met à jour le bonus-malus en fonction du nombre de sinistres.
     *
     * @param claimCount Le nombre de sinistres responsables
     * @return Le bonus-malus mis à jour
     */
    @Transient
    public BonusMalus update(int claimCount) {
        // Validate claimCount to prevent negative values
        Validation.validateNotNegative(claimCount, "nombre de sinistres");

        // Sauvegarder le coefficient actuel comme précédent
        // Check if coefficient is null to avoid NPE
        this.previousCoefficient = this.coefficient != null ? this.coefficient : INITIAL_COEFFICIENT;

        // Calculer le nouveau coefficient
        this.coefficient = calculateNewCoefficient(claimCount);

        // Mettre à jour les dates
        this.effectiveDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(1);

        // Mettre à jour les années sans sinistre

        if (claimCount == 0) {
            this.yearsWithoutClaim++;
        } else {
            this.yearsWithoutClaim = 0;
        }

        return this;
    }
}
