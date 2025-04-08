package com.devolution.saas.core.security.application.dto;

import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DTO pour les clés API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiKeyDTO {

    /**
     * ID de la clé API.
     */
    private UUID id;

    /**
     * Nom de la clé API.
     */
    private String name;

    /**
     * Préfixe de la clé API (partie visible).
     */
    private String prefix;

    /**
     * Valeur complète de la clé API (uniquement lors de la création).
     */
    private String key;

    /**
     * ID de l'organisation.
     */
    private UUID organizationId;

    /**
     * Statut de la clé API.
     */
    private ApiKeyStatus status;

    /**
     * Date d'expiration de la clé API.
     */
    private LocalDateTime expiresAt;

    /**
     * Date de dernière utilisation de la clé API.
     */
    private LocalDateTime lastUsedAt;

    /**
     * Permissions associées à la clé API.
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
    private Integer rateLimit;

    /**
     * Description de la clé API.
     */
    private String description;

    /**
     * Date de création.
     */
    private LocalDateTime createdAt;

    /**
     * Date de dernière mise à jour.
     */
    private LocalDateTime updatedAt;

    /**
     * Indique si la clé API est active.
     */
    private boolean active;
}
