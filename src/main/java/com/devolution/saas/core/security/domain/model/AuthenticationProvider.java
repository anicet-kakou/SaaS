package com.devolution.saas.core.security.domain.model;

/**
 * Énumération des fournisseurs d'authentification supportés.
 */
public enum AuthenticationProvider {
    /**
     * Authentification locale (base de données).
     */
    LOCAL,

    /**
     * Authentification via Keycloak.
     */
    KEYCLOAK,

    /**
     * Authentification via Auth0.
     */
    AUTH0,

    /**
     * Authentification via Okta.
     */
    OKTA,

    /**
     * Authentification via Microsoft Azure AD.
     */
    AZURE_AD,

    /**
     * Authentification via Google.
     */
    GOOGLE
}
