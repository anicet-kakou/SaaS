package com.devolution.saas.core.organization.application.dto;

import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour les organisations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {

    /**
     * ID de l'organisation.
     */
    private UUID id;

    /**
     * Nom de l'organisation.
     */
    private String name;

    /**
     * Code de l'organisation.
     */
    private String code;

    /**
     * Type de l'organisation.
     */
    private OrganizationType type;

    /**
     * Statut de l'organisation.
     */
    private OrganizationStatus status;

    /**
     * ID de l'organisation parente.
     */
    private UUID parentId;

    /**
     * Nom de l'organisation parente.
     */
    private String parentName;

    /**
     * Adresse de l'organisation.
     */
    private String address;

    /**
     * Numéro de téléphone de l'organisation.
     */
    private String phone;

    /**
     * Email de l'organisation.
     */
    private String email;

    /**
     * Site web de l'organisation.
     */
    private String website;

    /**
     * URL du logo de l'organisation.
     */
    private String logoUrl;

    /**
     * Nom du contact principal de l'organisation.
     */
    private String primaryContactName;

    /**
     * Description de l'organisation.
     */
    private String description;

    /**
     * Paramètres de l'organisation au format JSON.
     */
    private String settings;

    /**
     * Date de création de l'organisation.
     */
    private LocalDateTime createdAt;

    /**
     * Date de dernière mise à jour de l'organisation.
     */
    private LocalDateTime updatedAt;

    /**
     * Indique si l'organisation est active.
     */
    private boolean active;
}
