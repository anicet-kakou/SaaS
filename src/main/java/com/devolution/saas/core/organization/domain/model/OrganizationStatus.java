package com.devolution.saas.core.organization.domain.model;

/**
 * Énumération des statuts possibles pour une organisation.
 */
public enum OrganizationStatus {
    /**
     * Organisation active et opérationnelle.
     */
    ACTIVE,

    /**
     * Organisation inactive (désactivée).
     */
    INACTIVE,

    /**
     * Organisation suspendue temporairement.
     */
    SUSPENDED,

    /**
     * Organisation en cours de configuration.
     */
    PENDING,

    /**
     * Organisation archivée (ne peut plus être utilisée).
     */
    ARCHIVED
}
