package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

/**
 * Entité représentant un type de carburant.
 */
@Entity
@Table(name = "fuel_types")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FuelType extends BaseEntity {

    /**
     * Code unique du type de carburant.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom du type de carburant.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description du type de carburant.
     */
    @Column(name = "description")
    private String description;

    /**
     * Indique si le type de carburant est actif.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * ID de l'organisation.
     */
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;
}
