package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * Commande pour l'authentification d'un utilisateur.
 */
public record LoginCommand(
    /**
     * Nom d'utilisateur ou adresse email.
     */
    @NotBlank(message = "Le nom d'utilisateur ou l'email est obligatoire")
    String usernameOrEmail,

    /**
     * Mot de passe.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    String password,

    /**
     * ID de l'organisation (optionnel).
     * Si fourni, l'utilisateur sera authentifié dans le contexte de cette organisation.
     */
    UUID organizationId
) {
    /**
     * Builder pour LoginCommand.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour LoginCommand.
     */
    public static class Builder {
        private String usernameOrEmail;
        private String password;
        private UUID organizationId;

        public Builder usernameOrEmail(String usernameOrEmail) {
            this.usernameOrEmail = usernameOrEmail;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public LoginCommand build() {
            return new LoginCommand(usernameOrEmail, password, organizationId);
        }
    }
}
