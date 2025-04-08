package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Commande pour le rafraîchissement d'un jeton JWT.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenCommand {

    /**
     * Jeton de rafraîchissement.
     */
    @NotBlank(message = "Le jeton de rafraîchissement est obligatoire")
    private String refreshToken;
}
