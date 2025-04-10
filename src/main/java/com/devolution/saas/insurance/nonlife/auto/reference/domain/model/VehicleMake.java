package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

/**
 * Entité représentant une marque de véhicule.
 */
@Entity
@Table(name = "vehicle_makes")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMake extends BaseEntity {

    /**
     * Code unique de la marque.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Nom de la marque.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description de la marque.
     */
    @Column(name = "description")
    private String description;

    /**
     * Pays d'origine de la marque.
     */
    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    /**
     * Indique si la marque est active.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * ID de l'organisation.
     */
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;
}
