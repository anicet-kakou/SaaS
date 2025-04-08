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
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * Adapter pour l'authentification Auth0.
 * Implémente le port d'authentification pour l'authentification via Auth0.
 * Cet adapter est conditionnel et ne sera créé que si la propriété auth0.domain est définie.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "auth0.domain", havingValue = "", matchIfMissing = false)
public class Auth0AuthAdapter implements AuthenticationPort {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> authenticateWithCredentials(String usernameOrEmail, String password) {
        try {
            // Préparer les en-têtes
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Préparer le corps de la requête
            String requestBody = String.format(
                    "{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"audience\":\"%s\",\"grant_type\":\"password\",\"username\":\"%s\",\"password\":\"%s\"}",
                    clientId, clientSecret, audience, usernameOrEmail, password
            );

            // Créer la requête
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://" + domain + "/oauth/token",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String accessToken = (String) response.getBody().get("access_token");
                return getUserFromToken(accessToken);
            }
        } catch (Exception e) {
            log.error("Échec de l'authentification Auth0", e);
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
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Préparer le corps de la requête
            String requestBody = String.format(
                    "{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"code\":\"%s\",\"grant_type\":\"authorization_code\",\"redirect_uri\":\"%s\"}",
                    clientId, clientSecret, code, redirectUri
            );

            // Créer la requête
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://" + domain + "/oauth/token",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String accessToken = (String) response.getBody().get("access_token");
                return getUserFromToken(accessToken);
            }
        } catch (Exception e) {
            log.error("Échec de l'échange de code d'autorisation Auth0", e);
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
                    "https://" + domain + "/userinfo",
                    org.springframework.http.HttpMethod.GET,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            log.error("Échec de la récupération des informations utilisateur Auth0", e);
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
            headers.setBearerAuth(token);

            // Créer la requête
            HttpEntity<String> request = new HttpEntity<>(headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://" + domain + "/userinfo",
                    org.springframework.http.HttpMethod.GET,
                    request,
                    Map.class
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Échec de la validation du token Auth0", e);
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
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Préparer le corps de la requête
            String requestBody = String.format(
                    "{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"refresh_token\":\"%s\",\"grant_type\":\"refresh_token\"}",
                    clientId, clientSecret, refreshToken
            );

            // Créer la requête
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // Envoyer la requête
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://" + domain + "/oauth/token",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.ofNullable((String) response.getBody().get("access_token"));
            }
        } catch (Exception e) {
            log.error("Échec du rafraîchissement du token Auth0", e);
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
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Préparer le corps de la requête
            String requestBody = String.format(
                    "{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"token\":\"%s\"}",
                    clientId, clientSecret, token
            );

            // Créer la requête
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // Envoyer la requête
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    "https://" + domain + "/oauth/revoke",
                    request,
                    Void.class
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Échec de la révocation du token Auth0", e);
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
                    String username = (String) userInfo.get("nickname");

                    // Chercher l'utilisateur par email ou username
                    Optional<User> user = userRepository.findByEmail(email);
                    if (user.isPresent()) {
                        return user;
                    }

                    return userRepository.findByUsername(username);
                });
    }
}
