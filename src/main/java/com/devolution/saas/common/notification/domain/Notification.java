package com.devolution.saas.common.notification.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entité représentant une notification dans le système.
 */
@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    /**
     * ID de l'utilisateur destinataire de la notification.
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * Code de la notification (ex: "notification.security.login.success").
     */
    @Column(name = "notification_code", nullable = false)
    private String notificationCode;

    /**
     * Message de la notification.
     */
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * Type de la notification (ex: "INFO", "WARNING", "ERROR").
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    /**
     * Données supplémentaires pour la notification.
     */
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;

    /**
     * Indique si la notification a été lue.
     */
    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    /**
     * Date de lecture de la notification.
     */
    @Column(name = "read_at")
    private LocalDateTime readAt;

    /**
     * Date de création de la notification.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Indique si la notification a été envoyée.
     */
    @Column(name = "is_sent", nullable = false)
    private boolean isSent;

    /**
     * Date d'envoi de la notification.
     */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
