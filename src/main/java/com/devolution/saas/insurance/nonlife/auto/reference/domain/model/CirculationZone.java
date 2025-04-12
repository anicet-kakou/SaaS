package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

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

/**
 * Entité représentant une zone de circulation pour les véhicules.
 */
@Entity
@Table(name = "circulation_zones")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CirculationZone extends TenantAwareEntity {
    /**
     * Code unique de la zone de circulation.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom de la zone de circulation.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description de la zone de circulation.
     */
    @Column(name = "description")
    private String description;

    /**
     * Coefficient de risque associé à cette zone de circulation.
     */
    @Column(name = "risk_coefficient", nullable = false, precision = 5, scale = 2)
    private BigDecimal riskCoefficient;

    /**
     * Indique si la zone de circulation est active.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // The organizationId field is inherited from TenantAwareEntity
}
