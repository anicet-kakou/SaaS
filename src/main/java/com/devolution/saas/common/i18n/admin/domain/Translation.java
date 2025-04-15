package com.devolution.saas.common.i18n.admin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité représentant une traduction dans le système.
 */
@Entity
@Table(name = "translations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"module", "message_type", "message_key", "locale"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Module auquel appartient cette traduction (ex: "common", "security", "insurance").
     */
    @Column(name = "module", nullable = false)
    private String module;

    /**
     * Type de message (ex: "errors", "validation", "notifications").
     */
    @Column(name = "message_type", nullable = false)
    private String messageType;

    /**
     * Clé du message (ex: "user.not.found").
     */
    @Column(name = "message_key", nullable = false)
    private String messageKey;

    /**
     * Locale de la traduction (ex: "fr", "en").
     */
    @Column(name = "locale", nullable = false, length = 10)
    private String locale;

    /**
     * Texte de la traduction.
     */
    @Column(name = "message_text", nullable = false, columnDefinition = "TEXT")
    private String messageText;

    /**
     * Indique si cette traduction est la version par défaut.
     */
    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    /**
     * Date de création de la traduction.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification de la traduction.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Utilisateur ayant créé la traduction.
     */
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    /**
     * Utilisateur ayant modifié la traduction en dernier.
     */
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
