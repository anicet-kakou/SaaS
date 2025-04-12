package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entité représentant un type de carburant de véhicule.
 */
@Entity
@Table(name = "vehicle_fuel_types")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleFuelType extends TenantAwareEntity {

    /**
     * Code unique du type de carburant.
     */
    @Column(name = "code", nullable = false)
    @NotBlank(message = "Le code du type de carburant est obligatoire")
    @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Le code ne doit contenir que des lettres majuscules, des chiffres et des underscores")
    private String code;

    /**
     * Nom du type de carburant.
     */
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Le nom du type de carburant est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String name;

    /**
     * Description du type de carburant.
     */
    @Column(name = "description")
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    /**
     * Indique si le type de carburant est actif.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
