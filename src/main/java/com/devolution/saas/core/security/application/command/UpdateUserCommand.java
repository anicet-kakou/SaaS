package com.devolution.saas.core.security.application.command;

import com.devolution.saas.core.security.domain.model.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Commande pour la mise à jour d'un utilisateur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCommand {

    /**
     * ID de l'utilisateur à mettre à jour.
     */
    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private UUID id;

    /**
     * Adresse email.
     */
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    @Email(message = "L'email doit être valide")
    private String email;

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
    private UserStatus status;

    /**
     * URL de la photo de profil.
     */
    @Size(max = 255, message = "L'URL de la photo de profil ne doit pas dépasser 255 caractères")
    private String profilePictureUrl;

    /**
     * ID de l'organisation principale.
     */
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
