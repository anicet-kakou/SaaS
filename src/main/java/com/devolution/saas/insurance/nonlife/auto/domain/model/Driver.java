package com.devolution.saas.insurance.nonlife.auto.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entité représentant un conducteur.
 */
@Entity
@Table(name = "drivers")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends BaseEntity {

    /**
     * ID du client associé au conducteur.
     */
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    /**
     * Numéro de permis de conduire.
     */
    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    /**
     * ID du type de permis.
     */
    @Column(name = "license_type_id", nullable = false)
    private UUID licenseTypeId;

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

    /**
     * ID de l'organisation.
     */
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;
}
