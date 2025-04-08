package com.devolution.saas.core.audit.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.audit.application.dto.AuditLogDTO;
import com.devolution.saas.core.audit.application.service.AuditService;
import com.devolution.saas.core.audit.domain.model.AuditStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des logs d'audit.
 */
@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Audit Logs", description = "API pour la gestion des logs d'audit")
public class AuditLogController {

    private final AuditService auditService;

    /**
     * Récupère un log d'audit par son ID.
     *
     * @param id ID du log d'audit
     * @return DTO du log d'audit
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un log d'audit par son ID")
    @Auditable(action = "API_GET_AUDIT_LOG")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuditLogDTO> getAuditLog(@PathVariable UUID id) {
        log.debug("REST request pour récupérer le log d'audit: {}", id);
        AuditLogDTO result = auditService.getAuditLog(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste tous les logs d'audit.
     *
     * @return Liste des DTOs de logs d'audit
     */
    @GetMapping
    @Operation(summary = "Liste tous les logs d'audit")
    @Auditable(action = "API_LIST_AUDIT_LOGS")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> listAuditLogs() {
        log.debug("REST request pour lister tous les logs d'audit");
        List<AuditLogDTO> result = auditService.listAuditLogs();
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les logs d'audit par action.
     *
     * @param action Action
     * @return Liste des DTOs de logs d'audit
     */
    @GetMapping("/action/{action}")
    @Operation(summary = "Liste les logs d'audit par action")
    @Auditable(action = "API_LIST_AUDIT_LOGS_BY_ACTION")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> listAuditLogsByAction(@PathVariable String action) {
        log.debug("REST request pour lister les logs d'audit par action: {}", action);
        List<AuditLogDTO> result = auditService.listAuditLogsByAction(action);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les logs d'audit par utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return Liste des DTOs de logs d'audit
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Liste les logs d'audit par utilisateur")
    @Auditable(action = "API_LIST_AUDIT_LOGS_BY_USER")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> listAuditLogsByUser(@PathVariable UUID userId) {
        log.debug("REST request pour lister les logs d'audit par utilisateur: {}", userId);
        List<AuditLogDTO> result = auditService.listAuditLogsByUser(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les logs d'audit par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs de logs d'audit
     */
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Liste les logs d'audit par organisation")
    @Auditable(action = "API_LIST_AUDIT_LOGS_BY_ORGANIZATION")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> listAuditLogsByOrganization(@PathVariable UUID organizationId) {
        log.debug("REST request pour lister les logs d'audit par organisation: {}", organizationId);
        List<AuditLogDTO> result = auditService.listAuditLogsByOrganization(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les logs d'audit par entité.
     *
     * @param entityType Type d'entité
     * @param entityId   ID de l'entité
     * @return Liste des DTOs de logs d'audit
     */
    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Liste les logs d'audit par entité")
    @Auditable(action = "API_LIST_AUDIT_LOGS_BY_ENTITY")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> listAuditLogsByEntity(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        log.debug("REST request pour lister les logs d'audit par entité: {}/{}", entityType, entityId);
        List<AuditLogDTO> result = auditService.listAuditLogsByEntity(entityType, entityId);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les logs d'audit par statut.
     *
     * @param status Statut
     * @return Liste des DTOs de logs d'audit
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Liste les logs d'audit par statut")
    @Auditable(action = "API_LIST_AUDIT_LOGS_BY_STATUS")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> listAuditLogsByStatus(@PathVariable AuditStatus status) {
        log.debug("REST request pour lister les logs d'audit par statut: {}", status);
        List<AuditLogDTO> result = auditService.listAuditLogsByStatus(status);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les logs d'audit entre deux dates.
     *
     * @param startDate Date de début
     * @param endDate   Date de fin
     * @return Liste des DTOs de logs d'audit
     */
    @GetMapping("/date-range")
    @Operation(summary = "Liste les logs d'audit entre deux dates")
    @Auditable(action = "API_LIST_AUDIT_LOGS_BY_DATE_RANGE")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> listAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.debug("REST request pour lister les logs d'audit entre deux dates: {} - {}", startDate, endDate);
        List<AuditLogDTO> result = auditService.listAuditLogsByDateRange(startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
