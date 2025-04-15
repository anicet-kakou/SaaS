package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;

/**
 * Commande pour le rafraîchissement d'un jeton JWT.
 */
public record RefreshTokenCommand(
    /**
     * Jeton de rafraîchissement.
     */
    @NotBlank(message = "Le jeton de rafraîchissement est obligatoire")
    String refreshToken
) {
    /**
     * Builder pour RefreshTokenCommand.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour RefreshTokenCommand.
     */
    public static class Builder {
        private String refreshToken;

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenCommand build() {
            return new RefreshTokenCommand(refreshToken);
        }
    }
}
