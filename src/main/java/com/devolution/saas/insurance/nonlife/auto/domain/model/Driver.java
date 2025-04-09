package com.devolution.saas.insurance.nonlife.auto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entité représentant un conducteur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    private UUID id;
    private UUID customerId;
    private String licenseNumber;
    private UUID licenseTypeId;
    private LocalDate licenseIssueDate;
    private LocalDate licenseExpiryDate;
    private boolean isPrimaryDriver;
    private int yearsOfDrivingExperience;
    private UUID organizationId;
}
