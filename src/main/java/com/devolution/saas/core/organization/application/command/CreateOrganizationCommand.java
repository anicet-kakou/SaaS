package com.devolution.saas.core.organization.application.command;

import com.devolution.saas.core.organization.domain.model.OrganizationType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

/**
 * Commande pour la création d'une organisation.
 */
@Data
public class CreateOrganizationCommand {

    /**
     * Nom de l'organisation.
     */
    @NotBlank(message = "Le nom de l'organisation est obligatoire")
    @Size(min = 2, max = 255, message = "Le nom de l'organisation doit contenir entre 2 et 255 caractères")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\-_&'.()]+$", message = "Le nom de l'organisation contient des caractères non autorisés")
    private String name;

    /**
     * Code de l'organisation.
     */
    @NotBlank(message = "Le code de l'organisation est obligatoire")
    @Size(min = 2, max = 50, message = "Le code de l'organisation doit contenir entre 2 et 50 caractères")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Le code de l'organisation ne doit contenir que des lettres majuscules, des chiffres, des tirets et des underscores")
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
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String address;

    /**
     * Numéro de téléphone de l'organisation.
     */
    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères")
    @Pattern(regexp = "^[+]?[0-9\\s-]+$", message = "Le numéro de téléphone n'est pas valide")
    private String phone;

    /**
     * Email de l'organisation.
     */
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Site web de l'organisation.
     */
    @Size(max = 255, message = "Le site web ne doit pas dépasser 255 caractères")
    @Pattern(regexp = "^(https?://)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/.*)?$", message = "Le site web n'est pas valide")
    private String website;

    /**
     * URL du logo de l'organisation.
     */
    @Size(max = 255, message = "L'URL du logo ne doit pas dépasser 255 caractères")
    private String logoUrl;

    /**
     * Nom du contact principal de l'organisation.
     */
    @Size(max = 100, message = "Le nom du contact principal ne doit pas dépasser 100 caractères")
    private String primaryContactName;

    /**
     * Description de l'organisation.
     */
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    /**
     * Paramètres de l'organisation au format JSON.
     */
    @Size(max = 1000, message = "Les paramètres ne doivent pas dépasser 1000 caractères")
    private String settings;
}
