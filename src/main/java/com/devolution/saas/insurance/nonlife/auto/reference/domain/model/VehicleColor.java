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

/**
 * Entité représentant une couleur de véhicule.
 */
@Entity
@Table(name = "vehicle_colors")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleColor extends TenantAwareEntity {
    /**
     * Code unique de la couleur.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom de la couleur.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description de la couleur.
     */
    @Column(name = "description")
    private String description;

    /**
     * Indique si la couleur est active.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // The organizationId field is inherited from TenantAwareEntity
}
