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
 * Entité représentant un type de carrosserie de véhicule.
 */
@Entity
@Table(name = "vehicle_body_types")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBodyType extends TenantAwareEntity {
    /**
     * Code unique du type de carrosserie.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom du type de carrosserie.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description du type de carrosserie.
     */
    @Column(name = "description")
    private String description;

    /**
     * Coefficient de risque associé à ce type de carrosserie.
     */
    @Column(name = "risk_coefficient", nullable = false, precision = 5, scale = 2)
    private BigDecimal riskCoefficient;

    /**
     * Indique si le type de carrosserie est actif.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // The organizationId field is inherited from TenantAwareEntity
}
