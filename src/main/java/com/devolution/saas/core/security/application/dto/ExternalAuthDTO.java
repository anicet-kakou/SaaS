package com.devolution.saas.core.security.application.dto;

import com.devolution.saas.core.security.domain.model.AuthenticationProvider;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO pour l'authentification externe.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExternalAuthDTO(
    /**
     * Fournisseur d'authentification.
     */
    AuthenticationProvider provider,

    /**
     * Token d'authentification externe.
     */
    String token,

    /**
     * Code d'autorisation (flow OAuth2).
     */
    String code,

    /**
     * URI de redirection (flow OAuth2).
     */
    String redirectUri,

    /**
     * État pour la sécurité CSRF (flow OAuth2).
     */
    String state
) {
    /**
     * Builder pour ExternalAuthDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour ExternalAuthDTO.
     */
    public static class Builder {
        private AuthenticationProvider provider;
        private String token;
        private String code;
        private String redirectUri;
        private String state;

        public Builder provider(AuthenticationProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public ExternalAuthDTO build() {
            return new ExternalAuthDTO(provider, token, code, redirectUri, state);
        }
    }
}
