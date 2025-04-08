package com.devolution.saas.core.audit.domain.repository;

import com.devolution.saas.core.audit.domain.model.AuditLog;
import com.devolution.saas.core.audit.domain.model.AuditStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour les opérations sur les logs d'audit.
 */
public interface AuditLogRepository {

    /**
     * Enregistre un log d'audit.
     *
     * @param auditLog Log d'audit à enregistrer
     * @return Log d'audit enregistré
     */
    AuditLog save(AuditLog auditLog);

    /**
     * Trouve un log d'audit par son ID.
     *
     * @param id ID du log d'audit
     * @return Log d'audit trouvé ou Optional vide
     */
    Optional<AuditLog> findById(UUID id);

    /**
     * Trouve tous les logs d'audit.
     *
     * @return Liste des logs d'audit
     */
    List<AuditLog> findAll();

    /**
     * Trouve tous les logs d'audit par action.
     *
     * @param action Action
     * @return Liste des logs d'audit
     */
    List<AuditLog> findAllByAction(String action);

    /**
     * Trouve tous les logs d'audit par utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return Liste des logs d'audit
     */
    List<AuditLog> findAllByUserId(UUID userId);

    /**
     * Trouve tous les logs d'audit par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des logs d'audit
     */
    List<AuditLog> findAllByOrganizationId(UUID organizationId);

    /**
     * Trouve tous les logs d'audit par type d'entité et ID d'entité.
     *
     * @param entityType Type d'entité
     * @param entityId   ID de l'entité
     * @return Liste des logs d'audit
     */
    List<AuditLog> findAllByEntityTypeAndEntityId(String entityType, String entityId);

    /**
     * Trouve tous les logs d'audit par statut.
     *
     * @param status Statut
     * @return Liste des logs d'audit
     */
    List<AuditLog> findAllByStatus(AuditStatus status);

    /**
     * Trouve tous les logs d'audit entre deux dates.
     *
     * @param startDate Date de début
     * @param endDate   Date de fin
     * @return Liste des logs d'audit
     */
    List<AuditLog> findAllByActionTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Trouve tous les logs d'audit par adresse IP.
     *
     * @param ipAddress Adresse IP
     * @return Liste des logs d'audit
     */
    List<AuditLog> findAllByIpAddress(String ipAddress);

    /**
     * Supprime un log d'audit.
     *
     * @param auditLog Log d'audit à supprimer
     */
    void delete(AuditLog auditLog);

    /**
     * Compte le nombre de logs d'audit.
     *
     * @return Nombre de logs d'audit
     */
    long count();
}
