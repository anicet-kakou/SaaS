package com.devolution.saas.insurance.nonlife.auto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO pour le bonus-malus.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonusMalusDTO {
    private UUID id;
    private UUID customerId;
    private String customerName;
    private BigDecimal coefficient;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private BigDecimal previousCoefficient;
    private int yearsWithoutClaim;
}
