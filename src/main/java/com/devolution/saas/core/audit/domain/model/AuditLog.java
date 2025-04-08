package com.devolution.saas.core.audit.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité représentant un log d'audit dans le système.
 * Enregistre les actions effectuées par les utilisateurs et les systèmes.
 */
@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Type d'action effectuée.
     */
    @Column(name = "action", nullable = false)
    private String action;

    /**
     * Description de l'action.
     */
    @Column(name = "description")
    private String description;

    /**
     * Type d'entité concernée par l'action.
     */
    @Column(name = "entity_type")
    private String entityType;

    /**
     * ID de l'entité concernée par l'action.
     */
    @Column(name = "entity_id")
    private String entityId;

    /**
     * Données avant modification au format JSON.
     */
    @Column(name = "before_data", columnDefinition = "jsonb")
    private String beforeData;

    /**
     * Données après modification au format JSON.
     */
    @Column(name = "after_data", columnDefinition = "jsonb")
    private String afterData;

    /**
     * ID de l'utilisateur qui a effectué l'action.
     */
    @Column(name = "user_id")
    private UUID userId;

    /**
     * Nom d'utilisateur qui a effectué l'action.
     */
    @Column(name = "username")
    private String username;

    /**
     * ID de l'organisation concernée par l'action.
     */
    @Column(name = "organization_id")
    private UUID organizationId;

    /**
     * Adresse IP de l'utilisateur.
     */
    @Column(name = "ip_address")
    private String ipAddress;

    /**
     * User-Agent du navigateur de l'utilisateur.
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * URL de la requête.
     */
    @Column(name = "request_url")
    private String requestUrl;

    /**
     * Méthode HTTP de la requête.
     */
    @Column(name = "http_method")
    private String httpMethod;

    /**
     * Statut de la réponse HTTP.
     */
    @Column(name = "status_code")
    private Integer statusCode;

    /**
     * Durée d'exécution de la requête en millisecondes.
     */
    @Column(name = "execution_time")
    private Long executionTime;

    /**
     * Statut de l'audit (SUCCESS, FAILURE).
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditStatus status = AuditStatus.SUCCESS;

    /**
     * Message d'erreur en cas d'échec.
     */
    @Column(name = "error_message")
    private String errorMessage;

    /**
     * Trace de la pile en cas d'erreur.
     */
    @Column(name = "stack_trace", columnDefinition = "text")
    private String stackTrace;

    /**
     * Date et heure de l'action.
     */
    @Column(name = "action_time", nullable = false)
    private LocalDateTime actionTime = LocalDateTime.now();
}
