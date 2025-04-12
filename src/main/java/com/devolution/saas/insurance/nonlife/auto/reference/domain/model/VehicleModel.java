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

import java.util.UUID;

/**
 * Entité représentant un modèle de véhicule.
 */
@Entity
@Table(name = "vehicle_models")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModel extends TenantAwareEntity {

    /**
     * ID du fabricant du véhicule.
     */
    @Column(name = "manufacturer_id", nullable = false)
    private UUID manufacturerId;

    /**
     * Code unique du modèle.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom du modèle.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description du modèle.
     */
    @Column(name = "description")
    private String description;

    /**
     * ID de la catégorie du véhicule.
     */
    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    /**
     * ID de la sous-catégorie du véhicule.
     */
    @Column(name = "subcategory_id")
    private UUID subcategoryId;

    /**
     * Indique si le modèle est actif.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // The organizationId field is inherited from TenantAwareEntity
}
