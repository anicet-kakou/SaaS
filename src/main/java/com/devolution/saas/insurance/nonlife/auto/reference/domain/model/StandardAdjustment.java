package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité représentant une réduction/majoration standard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardAdjustment {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private AdjustmentType adjustmentType;
    private BigDecimal adjustmentPercent;
    private boolean isActive;
    private UUID organizationId;

    /**
     * Types de réduction/majoration.
     */
    public enum AdjustmentType {
        DISCOUNT,
        SURCHARGE
    }
}
