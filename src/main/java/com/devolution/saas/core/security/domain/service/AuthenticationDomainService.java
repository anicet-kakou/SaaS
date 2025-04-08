package com.devolution.saas.core.security.domain.service;

import com.devolution.saas.core.security.domain.model.AuthenticationProvider;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.port.out.AuthenticationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Service de domaine pour l'authentification.
 * Implémente la logique métier liée à l'authentification.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationDomainService {

    private final Map<AuthenticationProvider, AuthenticationPort> authenticationAdapters;

    /**
     * Authentifie un utilisateur avec un nom d'utilisateur et un mot de passe.
     *
     * @param usernameOrEmail Nom d'utilisateur ou email
     * @param password        Mot de passe
     * @return Utilisateur authentifié ou Optional vide si l'authentification échoue
     */
    public Optional<User> authenticateWithCredentials(String usernameOrEmail, String password) {
        log.debug("Authentification avec identifiants: {}", usernameOrEmail);
        return authenticationAdapters.get(AuthenticationProvider.LOCAL)
                .authenticateWithCredentials(usernameOrEmail, password);
    }

    /**
     * Authentifie un utilisateur avec un token externe.
     *
     * @param token    Token d'authentification externe
     * @param provider Fournisseur d'authentification
     * @return Utilisateur authentifié ou Optional vide si l'authentification échoue
     */
    public Optional<User> authenticateWithToken(String token, AuthenticationProvider provider) {
        log.debug("Authentification avec token externe via {}", provider);
        AuthenticationPort adapter = authenticationAdapters.get(provider);
        if (adapter == null) {
            log.warn("Fournisseur d'authentification non disponible: {}", provider);
            return Optional.empty();
        }
        return adapter.authenticateWithToken(token);
    }

    /**
     * Authentifie un utilisateur avec un code d'autorisation.
     *
     * @param code        Code d'autorisation
     * @param redirectUri URI de redirection
     * @param provider    Fournisseur d'authentification
     * @return Utilisateur authentifié ou Optional vide si l'authentification échoue
     */
    public Optional<User> authenticateWithCode(String code, String redirectUri, AuthenticationProvider provider) {
        log.debug("Authentification avec code d'autorisation via {}", provider);
        AuthenticationPort adapter = authenticationAdapters.get(provider);
        if (adapter == null) {
            log.warn("Fournisseur d'authentification non disponible: {}", provider);
            return Optional.empty();
        }
        return adapter.authenticateWithCode(code, redirectUri);
    }

    /**
     * Récupère les informations utilisateur à partir d'un token.
     *
     * @param token    Token d'authentification
     * @param provider Fournisseur d'authentification
     * @return Map des informations utilisateur ou Optional vide si le token est invalide
     */
    public Optional<Map<String, Object>> getUserInfo(String token, AuthenticationProvider provider) {
        log.debug("Récupération des informations utilisateur via {}", provider);
        AuthenticationPort adapter = authenticationAdapters.get(provider);
        if (adapter == null) {
            log.warn("Fournisseur d'authentification non disponible: {}", provider);
            return Optional.empty();
        }
        return adapter.getUserInfo(token);
    }

    /**
     * Vérifie si un token est valide.
     *
     * @param token    Token à vérifier
     * @param provider Fournisseur d'authentification
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token, AuthenticationProvider provider) {
        log.debug("Validation du token via {}", provider);
        AuthenticationPort adapter = authenticationAdapters.get(provider);
        if (adapter == null) {
            log.warn("Fournisseur d'authentification non disponible: {}", provider);
            return false;
        }
        return adapter.validateToken(token);
    }

    /**
     * Rafraîchit un token d'accès.
     *
     * @param refreshToken Token de rafraîchissement
     * @param provider     Fournisseur d'authentification
     * @return Nouveau token d'accès ou Optional vide si le rafraîchissement échoue
     */
    public Optional<String> refreshToken(String refreshToken, AuthenticationProvider provider) {
        log.debug("Rafraîchissement du token via {}", provider);
        return authenticationAdapters.get(provider)
                .refreshToken(refreshToken);
    }

    /**
     * Révoque un token.
     *
     * @param token    Token à révoquer
     * @param provider Fournisseur d'authentification
     * @return true si le token a été révoqué avec succès, false sinon
     */
    public boolean revokeToken(String token, AuthenticationProvider provider) {
        log.debug("Révocation du token via {}", provider);
        return authenticationAdapters.get(provider)
                .revokeToken(token);
    }
}
