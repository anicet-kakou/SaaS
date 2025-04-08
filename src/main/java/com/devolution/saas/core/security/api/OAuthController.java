package com.devolution.saas.core.security.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.security.application.command.ExternalLoginCommand;
import com.devolution.saas.core.security.application.dto.JwtAuthenticationResponse;
import com.devolution.saas.core.security.domain.model.AuthenticationProvider;
import com.devolution.saas.core.security.domain.port.in.AuthenticationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur REST pour l'authentification OAuth2/OIDC.
 */
@RestController
@RequestMapping("/api/v1/auth/oauth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "OAuth Authentication", description = "API pour l'authentification OAuth2/OIDC")
public class OAuthController {

    private final AuthenticationUseCase authenticationUseCase;

    /**
     * Authentifie un utilisateur avec un token externe.
     *
     * @param command Commande d'authentification externe
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/token")
    @Operation(summary = "Authentifie un utilisateur avec un token externe")
    @Auditable(action = "API_OAUTH_TOKEN_LOGIN")
    public ResponseEntity<JwtAuthenticationResponse> authenticateWithToken(@Valid @RequestBody ExternalLoginCommand command) {
        log.debug("REST request pour authentifier un utilisateur avec un token externe via {}", command.getProvider());
        JwtAuthenticationResponse response = authenticationUseCase.authenticateWithToken(command.getToken(), command.getProvider());
        return ResponseEntity.ok(response);
    }

    /**
     * Authentifie un utilisateur avec un code d'autorisation.
     *
     * @param command Commande d'authentification externe
     * @return Réponse d'authentification JWT
     */
    @PostMapping("/code")
    @Operation(summary = "Authentifie un utilisateur avec un code d'autorisation")
    @Auditable(action = "API_OAUTH_CODE_LOGIN")
    public ResponseEntity<JwtAuthenticationResponse> authenticateWithCode(@Valid @RequestBody ExternalLoginCommand command) {
        log.debug("REST request pour authentifier un utilisateur avec un code d'autorisation via {}", command.getProvider());
        JwtAuthenticationResponse response = authenticationUseCase.authenticateWithCode(command.getCode(), command.getRedirectUri(), command.getProvider());
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère l'URL d'autorisation pour un fournisseur d'authentification externe.
     *
     * @param provider    Fournisseur d'authentification
     * @param redirectUri URI de redirection
     * @param state       État pour la sécurité CSRF
     * @return URL d'autorisation
     */
    @GetMapping("/authorize")
    @Operation(summary = "Récupère l'URL d'autorisation pour un fournisseur d'authentification externe")
    @Auditable(action = "API_OAUTH_AUTHORIZE_URL")
    public ResponseEntity<Map<String, String>> getAuthorizationUrl(
            @RequestParam AuthenticationProvider provider,
            @RequestParam String redirectUri,
            @RequestParam(required = false) String state) {
        log.debug("REST request pour récupérer l'URL d'autorisation pour {}", provider);
        String url = authenticationUseCase.getAuthorizationUrl(provider, redirectUri, state);
        return ResponseEntity.ok(Map.of("url", url));
    }

    /**
     * Récupère les informations utilisateur à partir d'un token.
     *
     * @param token    Token d'authentification
     * @param provider Fournisseur d'authentification
     * @return Informations utilisateur
     */
    @GetMapping("/userinfo")
    @Operation(summary = "Récupère les informations utilisateur à partir d'un token")
    @Auditable(action = "API_OAUTH_USER_INFO")
    public ResponseEntity<Map<String, Object>> getUserInfo(
            @RequestParam String token,
            @RequestParam AuthenticationProvider provider) {
        log.debug("REST request pour récupérer les informations utilisateur via {}", provider);
        Optional<Map<String, Object>> userInfo = authenticationUseCase.getUserInfo(token, provider);
        return userInfo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
