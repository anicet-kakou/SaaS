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
 * Entité représentant un type d'usage de véhicule.
 */
@Entity
@Table(name = "vehicle_usages")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleUsage extends TenantAwareEntity {
    /**
     * Code unique du type d'usage.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom du type d'usage.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description du type d'usage.
     */
    @Column(name = "description")
    private String description;

    /**
     * Coefficient tarifaire associé à ce type d'usage.
     */
    @Column(name = "tariff_coefficient", nullable = false, precision = 5, scale = 2)
    private BigDecimal tariffCoefficient;

    /**
     * Indique si le type d'usage est actif.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // The organizationId field is inherited from TenantAwareEntity
}
