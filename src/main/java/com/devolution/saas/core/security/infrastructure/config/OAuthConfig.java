package com.devolution.saas.core.security.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration pour les fournisseurs d'authentification OAuth2/OIDC.
 */
@Configuration
public class OAuthConfig {

    /**
     * Configuration pour Keycloak.
     */
    @Bean
    @ConfigurationProperties(prefix = "keycloak")
    public KeycloakProperties keycloakProperties() {
        return new KeycloakProperties();
    }

    /**
     * Configuration pour Auth0.
     */
    @Bean
    @ConfigurationProperties(prefix = "auth0")
    public Auth0Properties auth0Properties() {
        return new Auth0Properties();
    }

    /**
     * Configuration pour Okta.
     */
    @Bean
    @ConfigurationProperties(prefix = "okta.oauth2")
    public OktaProperties oktaProperties() {
        return new OktaProperties();
    }

    /**
     * Propriétés de configuration pour Keycloak.
     */
    public static class KeycloakProperties {
        private String authServerUrl;
        private String realm;
        private String resource;
        private Credentials credentials = new Credentials();

        public String getAuthServerUrl() {
            return authServerUrl;
        }

        public void setAuthServerUrl(String authServerUrl) {
            this.authServerUrl = authServerUrl;
        }

        public String getRealm() {
            return realm;
        }

        public void setRealm(String realm) {
            this.realm = realm;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public static class Credentials {
            private String secret;

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }
        }
    }

    /**
     * Propriétés de configuration pour Auth0.
     */
    public static class Auth0Properties {
        private String domain;
        private String clientId;
        private String clientSecret;
        private String audience;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }
    }

    /**
     * Propriétés de configuration pour Okta.
     */
    public static class OktaProperties {
        private String issuer;
        private String clientId;
        private String clientSecret;

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
    }
}
