package com.devolution.saas.common.notification.repository;

import com.devolution.saas.common.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour l'entité Notification.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    /**
     * Recherche les notifications d'un utilisateur.
     *
     * @param userId   ID de l'utilisateur
     * @param pageable Pagination
     * @return Page de notifications
     */
    Page<Notification> findByUserId(String userId, Pageable pageable);

    /**
     * Recherche les notifications non lues d'un utilisateur.
     *
     * @param userId   ID de l'utilisateur
     * @param pageable Pagination
     * @return Page de notifications non lues
     */
    Page<Notification> findByUserIdAndIsReadFalse(String userId, Pageable pageable);

    /**
     * Recherche les notifications lues d'un utilisateur.
     *
     * @param userId   ID de l'utilisateur
     * @param pageable Pagination
     * @return Page de notifications lues
     */
    Page<Notification> findByUserIdAndIsReadTrue(String userId, Pageable pageable);

    /**
     * Recherche les notifications d'un utilisateur créées après une date donnée.
     *
     * @param userId    ID de l'utilisateur
     * @param createdAt Date de création
     * @param pageable  Pagination
     * @return Page de notifications
     */
    Page<Notification> findByUserIdAndCreatedAtAfter(String userId, LocalDateTime createdAt, Pageable pageable);

    /**
     * Compte le nombre de notifications non lues d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return Nombre de notifications non lues
     */
    long countByUserIdAndIsReadFalse(String userId);

    /**
     * Recherche les notifications non envoyées.
     *
     * @return Liste des notifications non envoyées
     */
    List<Notification> findByIsSentFalse();

    /**
     * Marque toutes les notifications d'un utilisateur comme lues.
     *
     * @param isRead Indique si les notifications sont lues
     * @param readAt Date de lecture
     * @param userId ID de l'utilisateur
     * @return Nombre de notifications mises à jour
     */
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = :isRead, n.readAt = :readAt WHERE n.userId = :userId")
    int updateIsReadAndReadAtByUserId(boolean isRead, LocalDateTime readAt, String userId);
}
