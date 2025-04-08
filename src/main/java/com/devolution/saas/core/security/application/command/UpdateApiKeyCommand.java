package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Commande pour la mise à jour d'une clé API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApiKeyCommand {

    /**
     * ID de la clé API à mettre à jour.
     */
    @NotNull(message = "L'ID de la clé API est obligatoire")
    private UUID id;

    /**
     * Nom de la clé API.
     */
    @NotBlank(message = "Le nom de la clé API est obligatoire")
    @Size(min = 3, max = 100, message = "Le nom de la clé API doit contenir entre 3 et 100 caractères")
    private String name;

    /**
     * ID de l'organisation.
     */
    @NotNull(message = "L'ID de l'organisation est obligatoire")
    private UUID organizationId;

    /**
     * Date d'expiration de la clé API.
     */
    private LocalDateTime expiresAt;

    /**
     * Permissions associées à la clé API au format JSON.
     */
    private String permissions;

    /**
     * Adresses IP autorisées à utiliser cette clé API.
     */
    @Builder.Default
    private Set<String> allowedIps = new HashSet<>();

    /**
     * Limite de taux de requêtes par minute.
     */
    @Min(value = 1, message = "La limite de taux doit être supérieure à 0")
    private Integer rateLimit;

    /**
     * Description de la clé API.
     */
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;
}
