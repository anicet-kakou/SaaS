package com.devolution.saas.core.security.application.controller;

import com.devolution.saas.common.util.HttpRequestUtils;
import com.devolution.saas.core.security.application.command.LoginCommand;
import com.devolution.saas.core.security.application.command.RefreshTokenCommand;
import com.devolution.saas.core.security.application.command.RegisterCommand;
import com.devolution.saas.core.security.application.dto.JwtAuthenticationResponse;
import com.devolution.saas.core.security.application.service.AuthenticationService;
import com.devolution.saas.core.security.domain.model.AuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur pour les opérations d'authentification.
 */
// This controller is deprecated and should be removed in favor of com.devolution.saas.core.security.api.AuthController
// @RestController
// @RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class DeprecatedAuthController {

    private final AuthenticationService authenticationService;

    /**
     * Authentifie un utilisateur.
     *
     * @param command Commande de connexion
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginCommand command) {
        log.debug("Demande de connexion pour l'utilisateur: {}", command.getUsernameOrEmail());
        return ResponseEntity.ok(authenticationService.login(command));
    }

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param command Commande d'enregistrement
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@Valid @RequestBody RegisterCommand command) {
        log.debug("Demande d'enregistrement pour l'utilisateur: {}", command.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(command));
    }

    /**
     * Rafraîchit un jeton JWT.
     *
     * @param command Commande de rafraîchissement
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@Valid @RequestBody RefreshTokenCommand command) {
        log.debug("Demande de rafraîchissement de jeton");
        return ResponseEntity.ok(authenticationService.refreshToken(command));
    }

    /**
     * Déconnecte un utilisateur.
     *
     * @param request Requête HTTP
     * @return Message de succès
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        log.debug("Demande de déconnexion");

        // Récupérer le jeton JWT de la requête
        String token = HttpRequestUtils.resolveJwtToken(request);

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Aucun jeton fourni"));
        }

        boolean success = authenticationService.logout(token);

        if (success) {
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Échec de la déconnexion"));
        }
    }

    /**
     * Déconnecte un utilisateur avec un jeton de rafraîchissement.
     *
     * @param command Commande contenant le jeton de rafraîchissement
     * @return Message de succès
     */
    @PostMapping("/logout/refresh")
    public ResponseEntity<Map<String, String>> logoutWithRefreshToken(@Valid @RequestBody RefreshTokenCommand command) {
        log.debug("Demande de déconnexion avec jeton de rafraîchissement");

        boolean success = authenticationService.logoutWithRefreshToken(command.getRefreshToken());

        if (success) {
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Échec de la déconnexion"));
        }
    }

    /**
     * Récupère l'URL d'autorisation pour un fournisseur d'authentification externe.
     *
     * @param provider    Fournisseur d'authentification
     * @param redirectUri URI de redirection
     * @param state       État pour la vérification CSRF
     * @return URL d'autorisation
     */
    @GetMapping("/oauth2/authorize/{provider}")
    public ResponseEntity<Map<String, String>> getAuthorizationUrl(
            @PathVariable AuthenticationProvider provider,
            @RequestParam String redirectUri,
            @RequestParam(required = false, defaultValue = "state") String state) {
        log.debug("Demande d'URL d'autorisation pour le fournisseur: {}", provider);

        String authorizationUrl = authenticationService.getAuthorizationUrl(provider, redirectUri, state);

        return ResponseEntity.ok(Map.of("authorizationUrl", authorizationUrl));
    }

    /**
     * Authentifie un utilisateur avec un code d'autorisation OAuth2.
     *
     * @param provider    Fournisseur d'authentification
     * @param code        Code d'autorisation
     * @param redirectUri URI de redirection
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/oauth2/callback/{provider}")
    public ResponseEntity<JwtAuthenticationResponse> oauth2Callback(
            @PathVariable AuthenticationProvider provider,
            @RequestParam String code,
            @RequestParam String redirectUri) {
        log.debug("Callback OAuth2 pour le fournisseur: {}", provider);

        JwtAuthenticationResponse response = authenticationService.authenticateWithCode(code, redirectUri, provider);

        return ResponseEntity.ok(response);
    }
}
