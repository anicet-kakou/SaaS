package com.devolution.saas.core.organization.application.command;

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
 * Commande pour la création d'une organisation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationCommand {

    /**
     * Nom de l'organisation.
     */
    @NotBlank(message = "Le nom de l'organisation est obligatoire")
    @Size(min = 2, max = 255, message = "Le nom de l'organisation doit contenir entre 2 et 255 caractères")
    private String name;

    /**
     * Code de l'organisation.
     */
    @NotBlank(message = "Le code de l'organisation est obligatoire")
    @Size(min = 2, max = 50, message = "Le code de l'organisation doit contenir entre 2 et 50 caractères")
    private String code;

    /**
     * Type de l'organisation.
     */
    @NotNull(message = "Le type de l'organisation est obligatoire")
    private OrganizationType type;

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
