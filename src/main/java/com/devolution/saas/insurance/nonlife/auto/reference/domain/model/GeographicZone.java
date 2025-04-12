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
 * Entité représentant une zone géographique pour les véhicules.
 */
@Entity
@Table(name = "geographic_zones")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GeographicZone extends TenantAwareEntity {
    /**
     * Code unique de la zone géographique.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom de la zone géographique.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description de la zone géographique.
     */
    @Column(name = "description")
    private String description;

    /**
     * Coefficient tarifaire associé à cette zone géographique.
     */
    @Column(name = "tariff_coefficient", nullable = false, precision = 5, scale = 2)
    private BigDecimal tariffCoefficient;

    /**
     * Indique si la zone géographique est active.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // The organizationId field is inherited from TenantAwareEntity
}
