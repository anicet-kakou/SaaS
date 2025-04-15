package com.devolution.saas.core.security.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.security.application.command.LoginCommand;
import com.devolution.saas.core.security.application.command.RefreshTokenCommand;
import com.devolution.saas.core.security.application.command.RegisterCommand;
import com.devolution.saas.core.security.application.dto.JwtAuthenticationResponse;
import com.devolution.saas.core.security.application.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour l'authentification des utilisateurs.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "API pour l'authentification des utilisateurs")
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Authentifie un utilisateur et génère un jeton JWT.
     *
     * @param command Commande de connexion
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/login")
    @Operation(summary = "Authentifie un utilisateur et génère un jeton JWT")
    @Auditable(action = "API_LOGIN")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginCommand command) {
        log.debug("REST request pour authentifier un utilisateur: {}", command.usernameOrEmail());
        JwtAuthenticationResponse response = authenticationService.login(command);
        return ResponseEntity.ok(response);
    }

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param command Commande d'enregistrement
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/register")
    @Operation(summary = "Enregistre un nouvel utilisateur")
    @Auditable(action = "API_REGISTER")
    public ResponseEntity<JwtAuthenticationResponse> register(@Valid @RequestBody RegisterCommand command) {
        log.debug("REST request pour enregistrer un nouvel utilisateur: {}", command.getUsername());
        JwtAuthenticationResponse response = authenticationService.register(command);
        return ResponseEntity.ok(response);
    }

    /**
     * Rafraîchit un jeton JWT.
     *
     * @param command Commande de rafraîchissement
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/refresh")
    @Operation(summary = "Rafraîchit un jeton JWT")
    @Auditable(action = "API_REFRESH_TOKEN")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenCommand command) {
        log.debug("REST request pour rafraîchir un jeton");
        JwtAuthenticationResponse response = authenticationService.refreshToken(command);
        return ResponseEntity.ok(response);
    }

    /**
     * Déconnecte un utilisateur en révoquant son jeton de rafraîchissement.
     *
     * @param refreshToken Jeton de rafraîchissement
     * @return Réponse vide avec statut 200 OK
     */
    @PostMapping("/logout")
    @Operation(summary = "Déconnecte un utilisateur en révoquant son jeton de rafraîchissement")
    @Auditable(action = "API_LOGOUT")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        log.debug("REST request pour déconnecter un utilisateur");
        authenticationService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }
}
