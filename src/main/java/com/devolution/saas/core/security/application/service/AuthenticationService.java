package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import com.devolution.saas.core.security.application.command.LoginCommand;
import com.devolution.saas.core.security.application.command.RefreshTokenCommand;
import com.devolution.saas.core.security.application.command.RegisterCommand;
import com.devolution.saas.core.security.application.dto.JwtAuthenticationResponse;
import com.devolution.saas.core.security.domain.model.*;
import com.devolution.saas.core.security.domain.port.in.AuthenticationUseCase;
import com.devolution.saas.core.security.domain.repository.RefreshTokenRepository;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import com.devolution.saas.core.security.domain.service.AuthenticationDomainService;
import com.devolution.saas.core.security.infrastructure.config.JwtTokenProvider;
import com.devolution.saas.core.security.infrastructure.service.JwtBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service pour l'authentification des utilisateurs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements AuthenticationUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationDomainService authenticationDomainService;
    private final JwtBlacklistService jwtBlacklistService;

    @Value("${jwt.expiration}")
    private long jwtExpirationInSeconds;

    @Value("${devolution.saas.security.jwt.expiration:900}")
    private long refreshTokenExpirationInSeconds;
    /**
     * Construit l'URL d'autorisation pour Keycloak.
     *
     * @param redirectUri URI de redirection
     * @param state État pour la sécurité CSRF
     * @return URL d'autorisation
     */
    @Value("${keycloak.auth-server-url:}")
    private String keycloakServerUrl;
    @Value("${keycloak.realm:}")
    private String keycloakRealm;
    @Value("${keycloak.resource:}")
    private String keycloakClientId;
    /**
     * Construit l'URL d'autorisation pour Auth0.
     *
     * @param redirectUri URI de redirection
     * @param state État pour la sécurité CSRF
     * @return URL d'autorisation
     */
    @Value("${auth0.domain:}")
    private String auth0Domain;
    @Value("${auth0.clientId:}")
    private String auth0ClientId;
    /**
     * Construit l'URL d'autorisation pour Okta.
     *
     * @param redirectUri URI de redirection
     * @param state État pour la sécurité CSRF
     * @return URL d'autorisation
     */
    @Value("${okta.oauth2.issuer:}")
    private String oktaIssuer;
    @Value("${okta.oauth2.client-id:}")
    private String oktaClientId;

    /**
     * Authentifie un utilisateur et génère un jeton JWT.
     *
     * @param command Commande de connexion
     * @return Réponse d'authentification JWT
     */
    @Transactional
    @Auditable(action = "USER_LOGIN")
    public JwtAuthenticationResponse login(LoginCommand command) {
        log.debug("Tentative de connexion pour l'utilisateur: {}", command.usernameOrEmail());

        // Authentification de l'utilisateur
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.usernameOrEmail(),
                        command.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Récupération de l'utilisateur
        User user = (User) authentication.getPrincipal();

        // Vérification du statut de l'utilisateur
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("auth.user.inactive", "Le compte utilisateur n'est pas actif");
        }

        // Vérification de l'organisation si spécifiée
        UUID organizationId = command.organizationId();
        if (organizationId != null) {
            boolean hasOrganization = user.getOrganizations().stream()
                    .anyMatch(org -> org.getOrganizationId().equals(organizationId));

            if (!hasOrganization) {
                throw new BusinessException("auth.user.not.in.organization",
                        "L'utilisateur n'appartient pas à l'organisation spécifiée");
            }

            // Définition de l'organisation principale pour cette session
            user.setOrganizationId(organizationId);
            userRepository.save(user);
        }

        // Enregistrement de la connexion réussie
        user.recordSuccessfulLogin();
        userRepository.save(user);

        // Génération du jeton JWT
        String accessToken = jwtTokenProvider.createToken(user);

        // Génération du jeton de rafraîchissement
        String refreshToken = generateRefreshToken(user.getId());

        // Construction de la réponse
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpirationInSeconds)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .organizationId(user.getOrganizationId())
                .build();
    }

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param command Commande d'enregistrement
     * @return Réponse d'authentification JWT
     */
    @Transactional
    @Auditable(action = "USER_REGISTER")
    public JwtAuthenticationResponse register(RegisterCommand command) {
        log.debug("Enregistrement d'un nouvel utilisateur: {}", command.getUsername());

        // Vérification de l'unicité du nom d'utilisateur
        if (userRepository.existsByUsername(command.getUsername())) {
            throw new ValidationException("Le nom d'utilisateur est déjà utilisé");
        }

        // Vérification de l'unicité de l'email
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new ValidationException("L'adresse email est déjà utilisée");
        }

        // Vérification de l'existence de l'organisation
        Organization organization = null;
        if (command.getOrganizationId() != null) {
            organization = organizationRepository.findById(command.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", command.getOrganizationId()));
        }

        // Création de l'utilisateur
        User user = new User();
        user.setUsername(command.getUsername());
        user.setEmail(command.getEmail());
        user.setPasswordHash(passwordEncoder.encode(command.getPassword()));
        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setPhone(command.getPhone());
        user.setStatus(UserStatus.ACTIVE);
        user.setLastPasswordChangeDate(LocalDateTime.now());

        if (organization != null) {
            user.setOrganizationId(organization.getId());

            // Ajout de l'organisation à l'utilisateur
            UserOrganization userOrganization = new UserOrganization(user.getId(), organization.getId());
            user.addOrganization(userOrganization);
        }

        // Attribution du rôle USER
        Optional<Role> userRole = roleRepository.findByName("USER");
        if (userRole.isPresent()) {
            user.addRole(userRole.get());
        } else {
            log.warn("Rôle USER non trouvé lors de l'enregistrement de l'utilisateur");
        }

        // Sauvegarde de l'utilisateur
        user = userRepository.save(user);

        // Génération du jeton JWT
        String accessToken = jwtTokenProvider.createToken(user);

        // Génération du jeton de rafraîchissement
        String refreshToken = generateRefreshToken(user.getId());

        // Construction de la réponse
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpirationInSeconds)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .organizationId(user.getOrganizationId())
                .build();
    }

    /**
     * Rafraîchit un jeton JWT.
     *
     * @param command Commande de rafraîchissement
     * @return Réponse d'authentification JWT
     */
    @Transactional
    @Auditable(action = "REFRESH_TOKEN")
    public JwtAuthenticationResponse refreshToken(RefreshTokenCommand command) {
        log.debug("Rafraîchissement du jeton");

        // Vérification du jeton de rafraîchissement
        RefreshToken refreshToken = refreshTokenRepository.findByToken(command.refreshToken())
                .orElseThrow(() -> new BusinessException("auth.refresh.token.invalid", "Jeton de rafraîchissement invalide"));

        // Vérification de la validité du jeton
        if (!refreshToken.isValid()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BusinessException("auth.refresh.token.expired", "Jeton de rafraîchissement expiré ou révoqué");
        }

        // Récupération de l'utilisateur
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", refreshToken.getUserId()));

        // Vérification du statut de l'utilisateur
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("auth.user.inactive", "Le compte utilisateur n'est pas actif");
        }

        // Révocation du jeton de rafraîchissement actuel
        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);

        // Génération d'un nouveau jeton JWT
        String accessToken = jwtTokenProvider.createToken(user);

        // Génération d'un nouveau jeton de rafraîchissement
        String newRefreshToken = generateRefreshToken(user.getId());

        // Construction de la réponse
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpirationInSeconds)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .organizationId(user.getOrganizationId())
                .build();
    }

    /**
     * Déconnecte un utilisateur en révoquant son jeton de rafraîchissement.
     *
     * @param refreshToken Jeton de rafraîchissement
     * @return true si la déconnexion a réussi, false sinon
     */
    @Transactional
    @Auditable(action = "USER_LOGOUT")
    public boolean logoutWithRefreshToken(String refreshToken) {
        log.debug("Déconnexion de l'utilisateur avec jeton de rafraîchissement");

        // Vérification du jeton de rafraîchissement
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BusinessException("auth.refresh.token.invalid", "Jeton de rafraîchissement invalide"));

        // Révocation du jeton
        token.revoke();
        refreshTokenRepository.save(token);

        return true;
    }

    /**
     * Génère un jeton de rafraîchissement pour un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return Jeton de rafraîchissement
     */
    private String generateRefreshToken(UUID userId) {
        // Génération d'un jeton aléatoire
        String token = UUID.randomUUID().toString();

        // Calcul de la date d'expiration
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(refreshTokenExpirationInSeconds);

        // Création et sauvegarde du jeton
        RefreshToken refreshToken = new RefreshToken(token, userId, expiryDate);
        refreshTokenRepository.save(refreshToken);

        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JwtAuthenticationResponse authenticateWithCredentials(String usernameOrEmail, String password) {
        log.debug("Authentification avec identifiants: {}", usernameOrEmail);

        // Utiliser la méthode login existante avec une commande
        LoginCommand command = LoginCommand.builder()
                .usernameOrEmail(usernameOrEmail)
                .password(password)
                .build();

        return login(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JwtAuthenticationResponse authenticateWithToken(String token, AuthenticationProvider provider) {
        log.debug("Authentification avec token externe via {}", provider);

        // Authentification avec le token externe
        User user = authenticationDomainService.authenticateWithToken(token, provider)
                .orElseThrow(() -> new BusinessException("auth.token.invalid", "Token d'authentification invalide"));

        // Vérification que l'utilisateur est actif
        if (!user.isActive()) {
            throw new BusinessException("auth.user.inactive", "Le compte utilisateur n'est pas actif");
        }

        // Génération du jeton JWT
        String accessToken = jwtTokenProvider.createToken(user);

        // Génération du jeton de rafraîchissement
        String refreshToken = generateRefreshToken(user.getId());

        // Construction de la réponse
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpirationInSeconds)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JwtAuthenticationResponse authenticateWithCode(String code, String redirectUri, AuthenticationProvider provider) {
        log.debug("Authentification avec code d'autorisation via {}", provider);

        // Authentification avec le code d'autorisation
        User user = authenticationDomainService.authenticateWithCode(code, redirectUri, provider)
                .orElseThrow(() -> new BusinessException("auth.code.invalid", "Code d'autorisation invalide"));

        // Vérification que l'utilisateur est actif
        if (!user.isActive()) {
            throw new BusinessException("auth.user.inactive", "Le compte utilisateur n'est pas actif");
        }

        // Génération du jeton JWT
        String accessToken = jwtTokenProvider.createToken(user);

        // Génération du jeton de rafraîchissement
        String refreshToken = generateRefreshToken(user.getId());

        // Construction de la réponse
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpirationInSeconds)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(AuthenticationProvider provider, String redirectUri, String state) {
        log.debug("Récupération de l'URL d'autorisation pour {}", provider);

        // Construction de l'URL d'autorisation en fonction du fournisseur
        try {
            switch (provider) {
                case KEYCLOAK:
                    return buildKeycloakAuthorizationUrl(redirectUri, state);
                case AUTH0:
                    return buildAuth0AuthorizationUrl(redirectUri, state);
                case OKTA:
                    return buildOktaAuthorizationUrl(redirectUri, state);
                default:
                    throw new BusinessException("auth.provider.unsupported", "Fournisseur d'authentification non supporté");
            }
        } catch (Exception e) {
            log.error("Erreur lors de la construction de l'URL d'autorisation pour {}: {}", provider, e.getMessage());
            throw new BusinessException("auth.provider.configuration.error",
                    "Erreur de configuration du fournisseur d'authentification: " + provider);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Map<String, Object>> getUserInfo(String token, AuthenticationProvider provider) {
        log.debug("Récupération des informations utilisateur via {}", provider);
        return authenticationDomainService.getUserInfo(token, provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        log.debug("Rafraîchissement du jeton avec refreshToken: {}", refreshToken);

        RefreshTokenCommand command = RefreshTokenCommand.builder()
                .refreshToken(refreshToken)
                .build();

        return refreshToken(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logout(String token) {
        // Déterminer si le token est un jeton JWT ou un jeton de rafraîchissement
        if (token.length() > 100) { // Les jetons JWT sont généralement plus longs
            return logoutWithJwt(token);
        } else {
            return logoutWithRefreshToken(token);
        }
    }

    /**
     * Déconnecte un utilisateur en révoquant son jeton JWT et son jeton de rafraîchissement.
     *
     * @param token Jeton JWT à révoquer
     * @return true si la déconnexion a réussi, false sinon
     */
    @Transactional
    @Auditable(action = "LOGOUT")
    public boolean logoutWithJwt(String token) {
        log.debug("Déconnexion de l'utilisateur avec jeton JWT");

        try {
            // Vérifier que le jeton est valide
            if (!jwtTokenProvider.validateToken(token)) {
                log.warn("Tentative de déconnexion avec un jeton invalide");
                return false;
            }

            // Récupérer l'ID de l'utilisateur à partir du jeton
            String username = jwtTokenProvider.getUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BusinessException("auth.user.not.found", "Utilisateur non trouvé"));

            // Révoquer tous les jetons de rafraîchissement de l'utilisateur
            List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUserId(user.getId());
            for (RefreshToken refreshToken : refreshTokens) {
                refreshToken.revoke();
                refreshTokenRepository.save(refreshToken);
            }

            // Ajouter le jeton JWT à la liste noire
            // Calculer la durée de validité restante du jeton
            Date expiration = jwtTokenProvider.getExpiration(token);
            long expirationTimeInSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            if (expirationTimeInSeconds > 0) {
                jwtBlacklistService.blacklistToken(token, expirationTimeInSeconds);
            }

            // Effacer le contexte de sécurité
            SecurityContextHolder.clearContext();

            log.info("Utilisateur {} déconnecté avec succès", username);
            return true;
        } catch (Exception e) {
            log.error("Erreur lors de la déconnexion: {}", e.getMessage());
            return false;
        }
    }

    private String buildKeycloakAuthorizationUrl(String redirectUri, String state) {
        return String.format(
                "%s/realms/%s/protocol/openid-connect/auth?client_id=%s&redirect_uri=%s&response_type=code&state=%s",
                keycloakServerUrl, keycloakRealm, keycloakClientId, redirectUri, state
        );
    }

    private String buildAuth0AuthorizationUrl(String redirectUri, String state) {
        return String.format(
                "https://%s/authorize?client_id=%s&redirect_uri=%s&response_type=code&state=%s",
                auth0Domain, auth0ClientId, redirectUri, state
        );
    }

    private String buildOktaAuthorizationUrl(String redirectUri, String state) {
        return String.format(
                "%s/v1/authorize?client_id=%s&redirect_uri=%s&response_type=code&state=%s",
                oktaIssuer, oktaClientId, redirectUri, state
        );
    }
}
