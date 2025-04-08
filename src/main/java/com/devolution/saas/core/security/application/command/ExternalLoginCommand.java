package com.devolution.saas.core.security.application.command;

import com.devolution.saas.core.security.domain.model.AuthenticationProvider;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Commande pour l'authentification externe.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalLoginCommand {

    /**
     * Fournisseur d'authentification.
     */
    @NotNull(message = "Le fournisseur d'authentification est obligatoire")
    private AuthenticationProvider provider;

    /**
     * Token d'authentification externe.
     */
    private String token;

    /**
     * Code d'autorisation (flow OAuth2).
     */
    private String code;

    /**
     * URI de redirection (flow OAuth2).
     */
    private String redirectUri;
}
