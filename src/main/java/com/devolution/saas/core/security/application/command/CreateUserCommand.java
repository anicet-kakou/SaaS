package com.devolution.saas.core.security.application.command;

import com.devolution.saas.core.security.domain.model.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Commande pour la création d'un utilisateur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCommand {

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
     * Statut de l'utilisateur.
     */
    @NotNull(message = "Le statut est obligatoire")
    private UserStatus status;

    /**
     * URL de la photo de profil.
     */
    private String profilePictureUrl;

    /**
     * ID de l'organisation principale.
     */
    @NotNull(message = "L'ID de l'organisation est obligatoire")
    private UUID organizationId;

    /**
     * IDs des rôles à attribuer à l'utilisateur.
     */
    @Builder.Default
    private Set<UUID> roleIds = new HashSet<>();

    /**
     * IDs des organisations auxquelles l'utilisateur appartient.
     */
    @Builder.Default
    private Set<UUID> organizationIds = new HashSet<>();
}
