package com.devolution.saas.core.security.application.command;

import com.devolution.saas.core.security.domain.model.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
    @Email(message = "L'email doit être valide")
    private String email;

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
    private UserStatus status;

    /**
     * URL de la photo de profil.
     */
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
