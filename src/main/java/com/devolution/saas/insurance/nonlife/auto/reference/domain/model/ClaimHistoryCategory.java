package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité représentant une catégorie d'antécédents de sinistres.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimHistoryCategory {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private int minClaims;
    private Integer maxClaims;
    private int periodYears;
    private BigDecimal riskCoefficient;
    private boolean isActive;
    private UUID organizationId;
}
