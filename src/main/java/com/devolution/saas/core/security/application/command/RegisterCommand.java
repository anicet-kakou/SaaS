package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Commande pour l'enregistrement d'un nouvel utilisateur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCommand {

    /**
     * Nom d'utilisateur.
     */
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    /**
     * Adresse email.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Mot de passe.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    /**
     * Prénom.
     */
    private String firstName;

    /**
     * Nom de famille.
     */
    private String lastName;

    /**
     * Numéro de téléphone.
     */
    private String phone;

    /**
     * ID de l'organisation.
     */
    private UUID organizationId;
}
