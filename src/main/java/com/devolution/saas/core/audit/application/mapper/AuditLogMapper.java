package com.devolution.saas.core.audit.application.mapper;

import com.devolution.saas.core.audit.application.dto.AuditLogDTO;
import com.devolution.saas.core.audit.domain.model.AuditLog;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités AuditLog et les DTOs AuditLogDTO.
 */
@Component
public class AuditLogMapper {

    /**
     * Convertit une entité AuditLog en DTO.
     *
     * @param auditLog Entité AuditLog
     * @return DTO AuditLogDTO
     */
    public AuditLogDTO toDTO(AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }

        return AuditLogDTO.builder()
                .id(auditLog.getId())
                .action(auditLog.getAction())
                .description(auditLog.getDescription())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .beforeData(auditLog.getBeforeData())
                .afterData(auditLog.getAfterData())
                .userId(auditLog.getUserId())
                .username(auditLog.getUsername())
                .organizationId(auditLog.getOrganizationId())
                .ipAddress(auditLog.getIpAddress())
                .userAgent(auditLog.getUserAgent())
                .requestUrl(auditLog.getRequestUrl())
                .httpMethod(auditLog.getHttpMethod())
                .statusCode(auditLog.getStatusCode())
                .executionTime(auditLog.getExecutionTime())
                .status(auditLog.getStatus())
                .errorMessage(auditLog.getErrorMessage())
                .actionTime(auditLog.getActionTime())
                .createdAt(auditLog.getCreatedAt())
                .build();
    }
}
