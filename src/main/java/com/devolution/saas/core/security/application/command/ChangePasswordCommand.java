package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Commande pour le changement de mot de passe d'un utilisateur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordCommand {

    /**
     * ID de l'utilisateur.
     */
    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private UUID userId;

    /**
     * Ancien mot de passe.
     */
    @NotBlank(message = "L'ancien mot de passe est obligatoire")
    private String oldPassword;

    /**
     * Nouveau mot de passe.
     */
    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caractères")
    private String newPassword;
}
