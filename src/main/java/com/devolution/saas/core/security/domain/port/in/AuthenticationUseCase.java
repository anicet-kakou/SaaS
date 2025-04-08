package com.devolution.saas.core.security.domain.port.in;

import com.devolution.saas.core.security.application.dto.JwtAuthenticationResponse;
import com.devolution.saas.core.security.domain.model.AuthenticationProvider;

import java.util.Map;
import java.util.Optional;

/**
 * Port d'entrée pour l'authentification.
 * Interface qui définit les cas d'utilisation liés à l'authentification.
 */
public interface AuthenticationUseCase {

    /**
     * Authentifie un utilisateur avec un nom d'utilisateur et un mot de passe.
     *
     * @param usernameOrEmail Nom d'utilisateur ou email
     * @param password        Mot de passe
     * @return Réponse d'authentification JWT
     */
    JwtAuthenticationResponse authenticateWithCredentials(String usernameOrEmail, String password);

    /**
     * Authentifie un utilisateur avec un token externe (OAuth2, OIDC, etc.).
     *
     * @param token    Token d'authentification externe
     * @param provider Fournisseur d'authentification
     * @return Réponse d'authentification JWT
     */
    JwtAuthenticationResponse authenticateWithToken(String token, AuthenticationProvider provider);

    /**
     * Authentifie un utilisateur avec un code d'autorisation (flow OAuth2).
     *
     * @param code        Code d'autorisation
     * @param redirectUri URI de redirection
     * @param provider    Fournisseur d'authentification
     * @return Réponse d'authentification JWT
     */
    JwtAuthenticationResponse authenticateWithCode(String code, String redirectUri, AuthenticationProvider provider);

    /**
     * Rafraîchit un token d'accès.
     *
     * @param refreshToken Token de rafraîchissement
     * @return Réponse d'authentification JWT
     */
    JwtAuthenticationResponse refreshToken(String refreshToken);

    /**
     * Déconnecte un utilisateur en révoquant son token.
     *
     * @param refreshToken Token de rafraîchissement
     * @return true si la déconnexion a réussi, false sinon
     */
    boolean logout(String refreshToken);

    /**
     * Récupère l'URL d'autorisation pour un fournisseur d'authentification externe.
     *
     * @param provider    Fournisseur d'authentification
     * @param redirectUri URI de redirection
     * @param state       État pour la sécurité CSRF
     * @return URL d'autorisation
     */
    String getAuthorizationUrl(AuthenticationProvider provider, String redirectUri, String state);

    /**
     * Récupère les informations utilisateur à partir d'un token.
     *
     * @param token    Token d'authentification
     * @param provider Fournisseur d'authentification
     * @return Map des informations utilisateur ou Optional vide si le token est invalide
     */
    Optional<Map<String, Object>> getUserInfo(String token, AuthenticationProvider provider);
}
