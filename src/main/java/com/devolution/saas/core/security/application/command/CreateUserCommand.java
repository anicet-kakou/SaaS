package com.devolution.saas.core.security.application.command;

import com.devolution.saas.core.security.domain.model.UserStatus;
import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Le nom d'utilisateur ne doit contenir que des lettres, des chiffres, des points, des tirets et des underscores")
    private String username;

    /**
     * Adresse email.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Mot de passe.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, max = 100, message = "Le mot de passe doit contenir entre 8 et 100 caractères")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Le mot de passe doit contenir au moins un chiffre, une lettre minuscule, une lettre majuscule et un caractère spécial")
    private String password;

    /**
     * Prénom.
     */
    @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères")
    @Pattern(regexp = "^[\\p{L}\\s'-]+$", message = "Le prénom contient des caractères non autorisés")
    private String firstName;

    /**
     * Nom de famille.
     */
    @Size(max = 50, message = "Le nom de famille ne doit pas dépasser 50 caractères")
    @Pattern(regexp = "^[\\p{L}\\s'-]+$", message = "Le nom de famille contient des caractères non autorisés")
    private String lastName;

    /**
     * Numéro de téléphone.
     */
    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères")
    @Pattern(regexp = "^[+]?[0-9\\s-]+$", message = "Le numéro de téléphone n'est pas valide")
    private String phone;

    /**
     * Statut de l'utilisateur.
     */
    @NotNull(message = "Le statut est obligatoire")
    private UserStatus status;

    /**
     * URL de la photo de profil.
     */
    @Size(max = 255, message = "L'URL de la photo de profil ne doit pas dépasser 255 caractères")
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
