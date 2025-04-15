package com.devolution.saas.common.notification.dto;

import com.devolution.saas.common.notification.domain.NotificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO pour les notifications.
 */
public record NotificationDTO(
        /**
         * ID de la notification.
         */
        String id,

        /**
         * ID de l'utilisateur destinataire de la notification.
         */
        @NotBlank(message = "L'ID de l'utilisateur est obligatoire")
        String userId,

        /**
         * Code de la notification (identifiant du type de notification).
         */
        @NotBlank(message = "Le code de notification est obligatoire")
        @Size(max = 50, message = "Le code de notification ne doit pas dépasser 50 caractères")
        String notificationCode,

        /**
         * Message de la notification.
         */
        @NotBlank(message = "Le message est obligatoire")
        String message,

        /**
         * Type de notification (EMAIL, SMS, PUSH, INTERNAL).
         */
        @NotNull(message = "Le type de notification est obligatoire")
        NotificationType type,

        /**
         * Données supplémentaires associées à la notification au format clé-valeur.
         */
        Map<String, Object> data,

        /**
         * Indique si la notification a été lue par l'utilisateur.
         */
        boolean isRead,

        /**
         * Date et heure à laquelle la notification a été lue.
         */
        LocalDateTime readAt,

        /**
         * Date et heure de création de la notification.
         */
        LocalDateTime createdAt,

        /**
         * Indique si la notification a été envoyée.
         */
        boolean isSent,

        /**
         * Date et heure d'envoi de la notification.
         */
        LocalDateTime sentAt
) {
    /**
     * Constructeur compact avec valeurs par défaut pour les collections.
     */
    public NotificationDTO {
        data = data != null ? data : new HashMap<>();
    }

    /**
     * Builder pour NotificationDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour NotificationDTO.
     */
    public static class Builder {
        private String id;
        private String userId;
        private String notificationCode;
        private String message;
        private NotificationType type;
        private Map<String, Object> data = new HashMap<>();
        private boolean isRead;
        private LocalDateTime readAt;
        private LocalDateTime createdAt;
        private boolean isSent;
        private LocalDateTime sentAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder notificationCode(String notificationCode) {
            this.notificationCode = notificationCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder type(NotificationType type) {
            this.type = type;
            return this;
        }

        /**
         * Définit les données supplémentaires de la notification.
         *
         * @param data Données au format clé-valeur
         * @return Cette instance du Builder pour chaînage
         */
        public Builder data(Map<String, Object> data) {
            if (data != null) {
                this.data = new HashMap<>(data);
            }
            return this;
        }

        /**
         * Ajoute une donnée supplémentaire à la notification.
         *
         * @param key   Clé de la donnée
         * @param value Valeur de la donnée
         * @return Cette instance du Builder pour chaînage
         */
        public Builder addData(String key, Object value) {
            if (key != null) {
                this.data.put(key, value);
            }
            return this;
        }

        /**
         * Ajoute plusieurs données supplémentaires à la notification.
         *
         * @param data Données au format clé-valeur
         * @return Cette instance du Builder pour chaînage
         */
        public Builder addAllData(Map<String, Object> data) {
            if (data != null) {
                this.data.putAll(data);
            }
            return this;
        }

        public Builder isRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public Builder readAt(LocalDateTime readAt) {
            this.readAt = readAt;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder isSent(boolean isSent) {
            this.isSent = isSent;
            return this;
        }

        public Builder sentAt(LocalDateTime sentAt) {
            this.sentAt = sentAt;
            return this;
        }

        public NotificationDTO build() {
            return new NotificationDTO(
                    id, userId, notificationCode, message, type, data,
                    isRead, readAt, createdAt, isSent, sentAt
            );
        }
    }
}
