package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.LicenseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entité représentant un conducteur.
 */
@Entity
@Table(
        name = "drivers",
        indexes = {
                @Index(name = "idx_drivers_customer_id", columnList = "customer_id"),
                @Index(name = "idx_drivers_license_number", columnList = "license_number"),
                @Index(name = "idx_drivers_license_type_id", columnList = "license_type_id")
        }
)
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends TenantAwareEntity {

    /**
     * ID du client associé au conducteur.
     */
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    /**
     * Numéro de permis de conduire.
     */
    @Column(name = "license_number", nullable = false, length = 50)
    private String licenseNumber;

    /**
     * Type de permis.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "license_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_driver_license_type")
    )
    private LicenseType licenseType;

    /**
     * Date de délivrance du permis.
     */
    @Column(name = "license_issue_date", nullable = false)
    private LocalDate licenseIssueDate;

    /**
     * Date d'expiration du permis.
     */
    @Column(name = "license_expiry_date")
    private LocalDate licenseExpiryDate;

    /**
     * Indique si le conducteur est le conducteur principal.
     */
    @Column(name = "is_primary_driver", nullable = false)
    private boolean isPrimaryDriver;

    /**
     * Nombre d'années d'expérience de conduite.
     */
    @Column(name = "years_of_driving_experience", nullable = false)
    private int yearsOfDrivingExperience;

    // Note: Cette entité hérite du champ organizationId de TenantAwareEntity pour le support multi-tenant

    /**
     * Retourne l'ID du type de permis.
     *
     * @return l'ID du type de permis
     */
    public UUID getLicenseTypeId() {
        return licenseType != null ? licenseType.getId() : null;
    }

    /**
     * Définit le type de permis à partir de son ID.
     *
     * @param licenseTypeId ID du type de permis
     */
    public void setLicenseTypeId(UUID licenseTypeId) {
        // Cette méthode est utilisée par le builder et les services
        // Elle ne fait rien car le type de permis doit être défini comme une entité
    }
}
