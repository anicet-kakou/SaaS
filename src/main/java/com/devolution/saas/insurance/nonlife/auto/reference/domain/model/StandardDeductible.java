package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité représentant une franchise standard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardDeductible {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private BigDecimal amount;
    private BigDecimal premiumAdjustmentPercent;
    private boolean isActive;
    private UUID organizationId;
}
