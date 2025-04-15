package com.devolution.saas.common.i18n.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO pour les traductions.
 */
public record TranslationDTO(
        Long id,

        @NotBlank(message = "module.required")
        @Size(max = 50, message = "module.too.long")
        String module,

        @NotBlank(message = "message.type.required")
        @Size(max = 50, message = "message.type.too.long")
        String messageType,

        @NotBlank(message = "message.key.required")
        @Size(max = 255, message = "message.key.too.long")
        String messageKey,

        @NotBlank(message = "locale.required")
        @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$", message = "locale.invalid")
        String locale,

        @NotBlank(message = "message.text.required")
        String messageText,

        boolean isDefault,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        String createdBy,

        String updatedBy
) {
    /**
     * Builder pour TranslationDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour TranslationDTO.
     */
    public static class Builder {
        private Long id;
        private String module;
        private String messageType;
        private String messageKey;
        private String locale;
        private String messageText;
        private boolean isDefault;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder module(String module) {
            this.module = module;
            return this;
        }

        public Builder messageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder messageKey(String messageKey) {
            this.messageKey = messageKey;
            return this;
        }

        public Builder locale(String locale) {
            this.locale = locale;
            return this;
        }

        public Builder messageText(String messageText) {
            this.messageText = messageText;
            return this;
        }

        public Builder isDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public TranslationDTO build() {
            return new TranslationDTO(
                    id, module, messageType, messageKey, locale, messageText,
                    isDefault, createdAt, updatedAt, createdBy, updatedBy
            );
        }
    }
}
