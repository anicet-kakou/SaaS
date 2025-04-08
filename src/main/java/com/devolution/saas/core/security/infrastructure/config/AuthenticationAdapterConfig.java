package com.devolution.saas.core.security.infrastructure.config;

import com.devolution.saas.core.security.domain.model.AuthenticationProvider;
import com.devolution.saas.core.security.domain.port.out.AuthenticationPort;
import com.devolution.saas.core.security.infrastructure.adapter.out.Auth0AuthAdapter;
import com.devolution.saas.core.security.infrastructure.adapter.out.KeycloakAuthAdapter;
import com.devolution.saas.core.security.infrastructure.adapter.out.LocalAuthAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.EnumMap;
import java.util.Map;

/**
 * Configuration des adapters d'authentification.
 */
@Configuration
public class AuthenticationAdapterConfig {

    /**
     * Crée un RestTemplate pour les requêtes HTTP.
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Crée une map des adapters d'authentification par fournisseur.
     *
     * @param localAuthAdapter    Adapter d'authentification locale
     * @param keycloakAuthAdapter Adapter d'authentification Keycloak (peut être null)
     * @param auth0AuthAdapter    Adapter d'authentification Auth0 (peut être null)
     * @return Map des adapters d'authentification
     */
    @Bean
    public Map<AuthenticationProvider, AuthenticationPort> authenticationAdapters(
            LocalAuthAdapter localAuthAdapter,
            @Autowired(required = false) KeycloakAuthAdapter keycloakAuthAdapter,
            @Autowired(required = false) Auth0AuthAdapter auth0AuthAdapter) {
        Map<AuthenticationProvider, AuthenticationPort> adapters = new EnumMap<>(AuthenticationProvider.class);
        adapters.put(AuthenticationProvider.LOCAL, localAuthAdapter);

        // Ajouter les adapters externes seulement s'ils sont disponibles
        if (keycloakAuthAdapter != null) {
            adapters.put(AuthenticationProvider.KEYCLOAK, keycloakAuthAdapter);
        }

        if (auth0AuthAdapter != null) {
            adapters.put(AuthenticationProvider.AUTH0, auth0AuthAdapter);
        }

        return adapters;
    }
}
