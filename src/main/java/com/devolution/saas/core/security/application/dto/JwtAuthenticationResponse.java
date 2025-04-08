package com.devolution.saas.core.security.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Réponse d'authentification JWT.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {

    /**
     * Jeton d'accès JWT.
     */
    private String accessToken;

    /**
     * Jeton de rafraîchissement.
     */
    private String refreshToken;

    /**
     * Type de jeton.
     */
    private String tokenType;

    /**
     * Durée de validité du jeton en secondes.
     */
    private long expiresIn;

    /**
     * ID de l'utilisateur.
     */
    private UUID userId;

    /**
     * Nom d'utilisateur.
     */
    private String username;

    /**
     * Adresse email.
     */
    private String email;

    /**
     * Nom complet de l'utilisateur.
     */
    private String fullName;

    /**
     * ID de l'organisation principale.
     */
    private UUID organizationId;
}
