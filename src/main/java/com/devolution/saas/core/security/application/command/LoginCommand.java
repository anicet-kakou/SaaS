package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Commande pour l'authentification d'un utilisateur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginCommand {

    /**
     * Nom d'utilisateur ou adresse email.
     */
    @NotBlank(message = "Le nom d'utilisateur ou l'email est obligatoire")
    private String usernameOrEmail;

    /**
     * Mot de passe.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    /**
     * ID de l'organisation (optionnel).
     * Si fourni, l'utilisateur sera authentifié dans le contexte de cette organisation.
     */
    private UUID organizationId;
}
