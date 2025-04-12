package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entité représentant un type de permis de conduire.
 */
@Entity
@Table(
        name = "license_types",
        indexes = {
                @Index(name = "idx_license_types_code", columnList = "code"),
                @Index(name = "idx_license_types_name", columnList = "name")
        }
)
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LicenseType extends TenantAwareEntity {

    /**
     * Code unique du type de permis.
     */
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    /**
     * Nom du type de permis.
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * Description du type de permis.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Indique si le type de permis est actif.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
