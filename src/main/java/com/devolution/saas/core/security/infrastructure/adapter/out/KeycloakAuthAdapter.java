package com.devolution.saas.core.security.infrastructure.adapter.out;

import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.port.out.AuthenticationPort;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * Adapter pour l'authentification Keycloak.
 * Implémente le port d'authentification pour l'authentification via Keycloak.
 * Cet adapter est conditionnel et ne sera créé que si la propriété keycloak.auth-server-url est définie.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "keycloak.auth-server-url", havingValue = "", matchIfMissing = false)
public class KeycloakAuthAdapter implements AuthenticationPort {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> authenticateWithCredentials(String usernameOrEmail, String password) {
        try {
            // Préparer les en-têtes
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Préparer le corps de la requête
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", "password");
            map.add("username", usernameOrEmail);
            map.add("password", password);

            // Créer la requête
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String accessToken = (String) response.getBody().get("access_token");
                return getUserFromToken(accessToken);
            }
        } catch (Exception e) {
            log.error("Échec de l'authentification Keycloak", e);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> authenticateWithToken(String token) {
        if (validateToken(token)) {
            return getUserFromToken(token);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> authenticateWithCode(String code, String redirectUri) {
        try {
            // Préparer les en-têtes
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Préparer le corps de la requête
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", "authorization_code");
            map.add("code", code);
            map.add("redirect_uri", redirectUri);

            // Créer la requête
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String accessToken = (String) response.getBody().get("access_token");
                return getUserFromToken(accessToken);
            }
        } catch (Exception e) {
            log.error("Échec de l'échange de code d'autorisation Keycloak", e);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Map<String, Object>> getUserInfo(String token) {
        try {
            // Préparer les en-têtes
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            // Créer la requête
            HttpEntity<String> request = new HttpEntity<>(headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.exchange(
                    keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo",
                    org.springframework.http.HttpMethod.GET,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            log.error("Échec de la récupération des informations utilisateur Keycloak", e);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateToken(String token) {
        try {
            // Préparer les en-têtes
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Préparer le corps de la requête
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("token", token);

            // Créer la requête
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Boolean.TRUE.equals(response.getBody().get("active"));
            }
        } catch (Exception e) {
            log.error("Échec de la validation du token Keycloak", e);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> refreshToken(String refreshToken) {
        try {
            // Préparer les en-têtes
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Préparer le corps de la requête
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", "refresh_token");
            map.add("refresh_token", refreshToken);

            // Créer la requête
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.ofNullable((String) response.getBody().get("access_token"));
            }
        } catch (Exception e) {
            log.error("Échec du rafraîchissement du token Keycloak", e);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean revokeToken(String token) {
        try {
            // Préparer les en-têtes
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Préparer le corps de la requête
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("token", token);

            // Créer la requête
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            // Envoyer la requête
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout",
                    request,
                    Void.class
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Échec de la révocation du token Keycloak", e);
        }
        return false;
    }

    /**
     * Récupère l'utilisateur à partir d'un token.
     *
     * @param token Token d'authentification
     * @return Utilisateur ou Optional vide si l'utilisateur n'existe pas
     */
    private Optional<User> getUserFromToken(String token) {
        return getUserInfo(token)
                .flatMap(userInfo -> {
                    String email = (String) userInfo.get("email");
                    String username = (String) userInfo.get("preferred_username");

                    // Chercher l'utilisateur par email ou username
                    Optional<User> user = userRepository.findByEmail(email);
                    if (user.isPresent()) {
                        return user;
                    }

                    return userRepository.findByUsername(username);
                });
    }
}
