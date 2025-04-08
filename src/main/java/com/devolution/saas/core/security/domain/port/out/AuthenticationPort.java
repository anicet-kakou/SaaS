package com.devolution.saas.core.security.domain.port.out;

import com.devolution.saas.core.security.domain.model.User;

import java.util.Map;
import java.util.Optional;

/**
 * Port de sortie pour l'authentification.
 * Interface qui définit les opérations d'authentification qui peuvent être implémentées
 * par différents adapters (local, Keycloak, Auth0, Okta, etc.).
 */
public interface AuthenticationPort {

    /**
     * Authentifie un utilisateur avec un nom d'utilisateur et un mot de passe.
     *
     * @param usernameOrEmail Nom d'utilisateur ou email
     * @param password        Mot de passe
     * @return Utilisateur authentifié ou Optional vide si l'authentification échoue
     */
    Optional<User> authenticateWithCredentials(String usernameOrEmail, String password);

    /**
     * Authentifie un utilisateur avec un token externe (OAuth2, OIDC, etc.).
     *
     * @param token Token d'authentification externe
     * @return Utilisateur authentifié ou Optional vide si l'authentification échoue
     */
    Optional<User> authenticateWithToken(String token);

    /**
     * Authentifie un utilisateur avec un code d'autorisation (flow OAuth2).
     *
     * @param code        Code d'autorisation
     * @param redirectUri URI de redirection
     * @return Utilisateur authentifié ou Optional vide si l'authentification échoue
     */
    Optional<User> authenticateWithCode(String code, String redirectUri);

    /**
     * Récupère les informations utilisateur à partir d'un token.
     *
     * @param token Token d'authentification
     * @return Map des informations utilisateur ou Optional vide si le token est invalide
     */
    Optional<Map<String, Object>> getUserInfo(String token);

    /**
     * Vérifie si un token est valide.
     *
     * @param token Token à vérifier
     * @return true si le token est valide, false sinon
     */
    boolean validateToken(String token);

    /**
     * Rafraîchit un token d'accès.
     *
     * @param refreshToken Token de rafraîchissement
     * @return Nouveau token d'accès ou Optional vide si le rafraîchissement échoue
     */
    Optional<String> refreshToken(String refreshToken);

    /**
     * Révoque un token.
     *
     * @param token Token à révoquer
     * @return true si le token a été révoqué avec succès, false sinon
     */
    boolean revokeToken(String token);
}
