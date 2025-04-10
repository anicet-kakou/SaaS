package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité représentant une catégorie de véhicule.
 */
@Entity
@Table(name = "vehicle_categories")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCategory extends BaseEntity {

    /**
     * Code unique de la catégorie.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom de la catégorie.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description de la catégorie.
     */
    @Column(name = "description")
    private String description;

    /**
     * Coefficient tarifaire associé à cette catégorie.
     */
    @Column(name = "tariff_coefficient", nullable = false, precision = 5, scale = 2)
    private BigDecimal tariffCoefficient;

    /**
     * Indique si la catégorie est active.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * ID de l'organisation.
     */
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;
}
