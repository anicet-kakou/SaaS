package com.devolution.saas.insurance.nonlife.auto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entité représentant le bonus-malus d'un client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonusMalus {
    private UUID id;
    private UUID customerId;
    private BigDecimal coefficient;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private BigDecimal previousCoefficient;
    private int yearsWithoutClaim;
    private UUID organizationId;
}
