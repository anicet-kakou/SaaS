package com.devolution.saas.core.security.application.dto;

import com.devolution.saas.core.security.domain.model.AuthenticationProvider;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour l'authentification externe.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalAuthDTO {

    /**
     * Fournisseur d'authentification.
     */
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

    /**
     * État pour la sécurité CSRF (flow OAuth2).
     */
    private String state;
}
