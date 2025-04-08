package com.devolution.saas.core.organization.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Événement émis lorsqu'une organisation est créée.
 */
@Getter
public class OrganizationCreatedEvent {

    /**
     * ID de l'organisation créée.
     */
    private final UUID organizationId;

    /**
     * Nom de l'organisation créée.
     */
    private final String organizationName;

    /**
     * Code de l'organisation créée.
     */
    private final String organizationCode;

    /**
     * Type de l'organisation créée.
     */
    private final String organizationType;

    /**
     * ID de l'organisation parente, peut être null.
     */
    private final UUID parentId;

    /**
     * Date et heure de création de l'événement.
     */
    private final LocalDateTime timestamp;

    /**
     * Constructeur.
     *
     * @param organizationId   ID de l'organisation
     * @param organizationName Nom de l'organisation
     * @param organizationCode Code de l'organisation
     * @param organizationType Type de l'organisation
     * @param parentId         ID de l'organisation parente
     */
    public OrganizationCreatedEvent(UUID organizationId, String organizationName, String organizationCode,
                                    String organizationType, UUID parentId) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.organizationCode = organizationCode;
        this.organizationType = organizationType;
        this.parentId = parentId;
        this.timestamp = LocalDateTime.now();
    }
}
