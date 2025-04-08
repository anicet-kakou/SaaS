package com.devolution.saas.core.audit.application.dto;

import com.devolution.saas.core.audit.domain.model.AuditStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour les logs d'audit.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogDTO {

    /**
     * ID du log d'audit.
     */
    private UUID id;

    /**
     * Type d'action effectuée.
     */
    private String action;

    /**
     * Description de l'action.
     */
    private String description;

    /**
     * Type d'entité concernée par l'action.
     */
    private String entityType;

    /**
     * ID de l'entité concernée par l'action.
     */
    private String entityId;

    /**
     * Données avant modification au format JSON.
     */
    private String beforeData;

    /**
     * Données après modification au format JSON.
     */
    private String afterData;

    /**
     * ID de l'utilisateur qui a effectué l'action.
     */
    private UUID userId;

    /**
     * Nom d'utilisateur qui a effectué l'action.
     */
    private String username;

    /**
     * ID de l'organisation concernée par l'action.
     */
    private UUID organizationId;

    /**
     * Adresse IP de l'utilisateur.
     */
    private String ipAddress;

    /**
     * User-Agent du navigateur de l'utilisateur.
     */
    private String userAgent;

    /**
     * URL de la requête.
     */
    private String requestUrl;

    /**
     * Méthode HTTP de la requête.
     */
    private String httpMethod;

    /**
     * Statut de la réponse HTTP.
     */
    private Integer statusCode;

    /**
     * Durée d'exécution de la requête en millisecondes.
     */
    private Long executionTime;

    /**
     * Statut de l'audit (SUCCESS, FAILURE).
     */
    private AuditStatus status;

    /**
     * Message d'erreur en cas d'échec.
     */
    private String errorMessage;

    /**
     * Date et heure de l'action.
     */
    private LocalDateTime actionTime;

    /**
     * Date de création.
     */
    private LocalDateTime createdAt;
}
