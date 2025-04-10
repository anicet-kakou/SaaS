package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

/**
 * Entité représentant un modèle de véhicule.
 */
@Entity
@Table(name = "vehicle_models")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModel extends BaseEntity {

    /**
     * ID de la marque du véhicule.
     */
    @Column(name = "make_id", nullable = false)
    private UUID makeId;

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

    /**
     * ID de l'organisation.
     */
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;
}
