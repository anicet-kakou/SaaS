package com.devolution.saas.core.audit.application.service;

import com.devolution.saas.core.audit.application.dto.AuditLogDTO;
import com.devolution.saas.core.audit.application.mapper.AuditLogMapper;
import com.devolution.saas.core.audit.domain.model.AuditLog;
import com.devolution.saas.core.audit.domain.model.AuditStatus;
import com.devolution.saas.core.audit.domain.repository.AuditLogRepository;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des logs d'audit.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final ObjectMapper objectMapper;
    private final TenantContextHolder tenantContextHolder;

    /**
     * Crée un log d'audit.
     *
     * @param action      Type d'action effectuée
     * @param description Description de l'action
     * @param entityType  Type d'entité concernée par l'action
     * @param entityId    ID de l'entité concernée par l'action
     * @param beforeData  Données avant modification
     * @param afterData   Données après modification
     * @return DTO du log d'audit créé
     */
    @Transactional
    public AuditLogDTO createAuditLog(String action, String description, String entityType, String entityId,
                                      Object beforeData, Object afterData) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setAction(action);
            auditLog.setDescription(description);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);

            if (beforeData != null) {
                auditLog.setBeforeData(objectMapper.writeValueAsString(beforeData));
            }

            if (afterData != null) {
                auditLog.setAfterData(objectMapper.writeValueAsString(afterData));
            }

            // Récupération des informations de l'utilisateur
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    auditLog.setUsername(((UserDetails) principal).getUsername());
                } else {
                    auditLog.setUsername(principal.toString());
                }
            }

            // Récupération des informations de la requête HTTP
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                auditLog.setIpAddress(getClientIp(request));
                auditLog.setUserAgent(request.getHeader("User-Agent"));
                auditLog.setRequestUrl(request.getRequestURL().toString());
                auditLog.setHttpMethod(request.getMethod());
            }

            // Récupération de l'ID de l'organisation
            UUID organizationId = tenantContextHolder.getCurrentTenant();
            if (organizationId != null) {
                auditLog.setOrganizationId(organizationId);
            }

            auditLog.setStatus(AuditStatus.SUCCESS);
            auditLog.setActionTime(LocalDateTime.now());

            auditLog = auditLogRepository.save(auditLog);

            return auditLogMapper.toDTO(auditLog);
        } catch (Exception e) {
            log.error("Erreur lors de la création du log d'audit", e);
            return null;
        }
    }

    /**
     * Crée un log d'audit d'erreur.
     *
     * @param action       Type d'action effectuée
     * @param description  Description de l'action
     * @param entityType   Type d'entité concernée par l'action
     * @param entityId     ID de l'entité concernée par l'action
     * @param errorMessage Message d'erreur
     * @param exception    Exception
     * @return DTO du log d'audit créé
     */
    @Transactional
    public AuditLogDTO createErrorAuditLog(String action, String description, String entityType, String entityId,
                                           String errorMessage, Exception exception) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setAction(action);
            auditLog.setDescription(description);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setErrorMessage(errorMessage);

            if (exception != null) {
                StringBuilder stackTrace = new StringBuilder();
                for (StackTraceElement element : exception.getStackTrace()) {
                    stackTrace.append(element.toString()).append("\n");
                }
                auditLog.setStackTrace(stackTrace.toString());
            }

            // Récupération des informations de l'utilisateur
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    auditLog.setUsername(((UserDetails) principal).getUsername());
                } else {
                    auditLog.setUsername(principal.toString());
                }
            }

            // Récupération des informations de la requête HTTP
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                auditLog.setIpAddress(getClientIp(request));
                auditLog.setUserAgent(request.getHeader("User-Agent"));
                auditLog.setRequestUrl(request.getRequestURL().toString());
                auditLog.setHttpMethod(request.getMethod());
            }

            // Récupération de l'ID de l'organisation
            UUID organizationId = tenantContextHolder.getCurrentTenant();
            if (organizationId != null) {
                auditLog.setOrganizationId(organizationId);
            }

            auditLog.setStatus(AuditStatus.FAILURE);
            auditLog.setActionTime(LocalDateTime.now());

            auditLog = auditLogRepository.save(auditLog);

            return auditLogMapper.toDTO(auditLog);
        } catch (Exception e) {
            log.error("Erreur lors de la création du log d'audit d'erreur", e);
            return null;
        }
    }

    /**
     * Récupère un log d'audit par son ID.
     *
     * @param id ID du log d'audit
     * @return DTO du log d'audit
     */
    @Transactional(readOnly = true)
    public AuditLogDTO getAuditLog(UUID id) {
        return auditLogRepository.findById(id)
                .map(auditLogMapper::toDTO)
                .orElse(null);
    }

    /**
     * Liste tous les logs d'audit.
     *
     * @return Liste des DTOs de logs d'audit
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> listAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Liste les logs d'audit par action.
     *
     * @param action Action
     * @return Liste des DTOs de logs d'audit
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> listAuditLogsByAction(String action) {
        return auditLogRepository.findAllByAction(action).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Liste les logs d'audit par utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return Liste des DTOs de logs d'audit
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> listAuditLogsByUser(UUID userId) {
        return auditLogRepository.findAllByUserId(userId).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Liste les logs d'audit par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs de logs d'audit
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> listAuditLogsByOrganization(UUID organizationId) {
        return auditLogRepository.findAllByOrganizationId(organizationId).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Liste les logs d'audit par entité.
     *
     * @param entityType Type d'entité
     * @param entityId   ID de l'entité
     * @return Liste des DTOs de logs d'audit
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> listAuditLogsByEntity(String entityType, String entityId) {
        return auditLogRepository.findAllByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Liste les logs d'audit par statut.
     *
     * @param status Statut
     * @return Liste des DTOs de logs d'audit
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> listAuditLogsByStatus(AuditStatus status) {
        return auditLogRepository.findAllByStatus(status).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Liste les logs d'audit entre deux dates.
     *
     * @param startDate Date de début
     * @param endDate   Date de fin
     * @return Liste des DTOs de logs d'audit
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> listAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findAllByActionTimeBetween(startDate, endDate).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère l'adresse IP du client.
     *
     * @param request Requête HTTP
     * @return Adresse IP du client
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
