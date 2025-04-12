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
 * Entité représentant un genre de véhicule.
 */
@Entity
@Table(name = "vehicle_genres")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleGenre extends TenantAwareEntity {
    /**
     * Code unique du genre de véhicule.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom du genre de véhicule.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description du genre de véhicule.
     */
    @Column(name = "description")
    private String description;

    /**
     * Coefficient de risque associé à ce genre de véhicule.
     */
    @Column(name = "risk_coefficient", nullable = false, precision = 5, scale = 2)
    private BigDecimal riskCoefficient;

    /**
     * Indique si le genre de véhicule est actif.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // The organizationId field is inherited from TenantAwareEntity
}
