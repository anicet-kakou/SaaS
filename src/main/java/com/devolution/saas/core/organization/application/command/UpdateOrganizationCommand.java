package com.devolution.saas.core.organization.application.command;

import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Commande pour la mise à jour d'une organisation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizationCommand {

    /**
     * ID de l'organisation à mettre à jour.
     */
    @NotNull(message = "L'ID de l'organisation est obligatoire")
    private UUID id;

    /**
     * Nom de l'organisation.
     */
    @NotBlank(message = "Le nom de l'organisation est obligatoire")
    @Size(min = 2, max = 255, message = "Le nom de l'organisation doit contenir entre 2 et 255 caractères")
    private String name;

    /**
     * Type de l'organisation.
     */
    @NotNull(message = "Le type de l'organisation est obligatoire")
    private OrganizationType type;

    /**
     * Statut de l'organisation.
     */
    @NotNull(message = "Le statut de l'organisation est obligatoire")
    private OrganizationStatus status;

    /**
     * ID de l'organisation parente.
     */
    private UUID parentId;

    /**
     * Adresse de l'organisation.
     */
    private String address;

    /**
     * Numéro de téléphone de l'organisation.
     */
    private String phone;

    /**
     * Email de l'organisation.
     */
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Site web de l'organisation.
     */
    private String website;

    /**
     * URL du logo de l'organisation.
     */
    private String logoUrl;

    /**
     * Nom du contact principal de l'organisation.
     */
    private String primaryContactName;

    /**
     * Description de l'organisation.
     */
    private String description;

    /**
     * Paramètres de l'organisation au format JSON.
     */
    private String settings;
}
