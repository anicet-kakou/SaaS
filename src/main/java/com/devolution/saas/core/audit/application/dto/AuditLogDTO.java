package com.devolution.saas.core.audit.application.dto;

import com.devolution.saas.core.audit.domain.model.AuditStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour les logs d'audit.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuditLogDTO(
    /**
     * ID du log d'audit.
     */
    UUID id,

    /**
     * Type d'action effectuée.
     */
    String action,

    /**
     * Description de l'action.
     */
    String description,

    /**
     * Type d'entité concernée par l'action.
     */
    String entityType,

    /**
     * ID de l'entité concernée par l'action.
     */
    String entityId,

    /**
     * Données avant modification au format JSON.
     */
    String beforeData,

    /**
     * Données après modification au format JSON.
     */
    String afterData,

    /**
     * ID de l'utilisateur qui a effectué l'action.
     */
    UUID userId,

    /**
     * Nom d'utilisateur qui a effectué l'action.
     */
    String username,

    /**
     * ID de l'organisation concernée par l'action.
     */
    UUID organizationId,

    /**
     * Adresse IP de l'utilisateur.
     */
    String ipAddress,

    /**
     * User-Agent du navigateur de l'utilisateur.
     */
    String userAgent,

    /**
     * URL de la requête.
     */
    String requestUrl,

    /**
     * Méthode HTTP de la requête.
     */
    String httpMethod,

    /**
     * Statut de la réponse HTTP.
     */
    Integer statusCode,

    /**
     * Durée d'exécution de la requête en millisecondes.
     */
    Long executionTime,

    /**
     * Statut de l'audit (SUCCESS, FAILURE).
     */
    AuditStatus status,

    /**
     * Message d'erreur en cas d'échec.
     */
    String errorMessage,

    /**
     * Date et heure de l'action.
     */
    LocalDateTime actionTime,

    /**
     * Date de création.
     */
    LocalDateTime createdAt
) {
    /**
     * Builder pour AuditLogDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour AuditLogDTO.
     */
    public static class Builder {
        private UUID id;
        private String action;
        private String description;
        private String entityType;
        private String entityId;
        private String beforeData;
        private String afterData;
        private UUID userId;
        private String username;
        private UUID organizationId;
        private String ipAddress;
        private String userAgent;
        private String requestUrl;
        private String httpMethod;
        private Integer statusCode;
        private Long executionTime;
        private AuditStatus status;
        private String errorMessage;
        private LocalDateTime actionTime;
        private LocalDateTime createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder entityType(String entityType) {
            this.entityType = entityType;
            return this;
        }

        public Builder entityId(String entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder beforeData(String beforeData) {
            this.beforeData = beforeData;
            return this;
        }

        public Builder afterData(String afterData) {
            this.afterData = afterData;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public Builder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder executionTime(Long executionTime) {
            this.executionTime = executionTime;
            return this;
        }

        public Builder status(AuditStatus status) {
            this.status = status;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder actionTime(LocalDateTime actionTime) {
            this.actionTime = actionTime;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AuditLogDTO build() {
            return new AuditLogDTO(
                    id, action, description, entityType, entityId, beforeData, afterData,
                    userId, username, organizationId, ipAddress, userAgent, requestUrl,
                    httpMethod, statusCode, executionTime, status, errorMessage,
                    actionTime, createdAt
            );
        }
    }
}
