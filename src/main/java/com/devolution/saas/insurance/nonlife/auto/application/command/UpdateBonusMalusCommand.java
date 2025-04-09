package com.devolution.saas.insurance.nonlife.auto.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Commande pour la mise à jour du bonus-malus.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBonusMalusCommand {
    private UUID id;
    private UUID customerId;
    private BigDecimal coefficient;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private BigDecimal previousCoefficient;
    private int yearsWithoutClaim;
    private UUID organizationId;
}
