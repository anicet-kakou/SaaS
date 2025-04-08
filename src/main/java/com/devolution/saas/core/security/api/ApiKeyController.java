package com.devolution.saas.core.security.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.security.application.command.CreateApiKeyCommand;
import com.devolution.saas.core.security.application.command.UpdateApiKeyCommand;
import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.service.ApiKeyService;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des clés API.
 */
@RestController
@RequestMapping("/api/v1/api-keys")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "API Keys", description = "API pour la gestion des clés API")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    /**
     * Crée une nouvelle clé API.
     *
     * @param command Commande de création de clé API
     * @return DTO de la clé API créée
     */
    @PostMapping
    @Operation(summary = "Crée une nouvelle clé API")
    @Auditable(action = "API_CREATE_API_KEY")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiKeyDTO> createApiKey(@Valid @RequestBody CreateApiKeyCommand command) {
        log.debug("REST request pour créer une clé API: {}", command.getName());
        ApiKeyDTO result = apiKeyService.createApiKey(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Met à jour une clé API existante.
     *
     * @param id      ID de la clé API à mettre à jour
     * @param command Commande de mise à jour de clé API
     * @return DTO de la clé API mise à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une clé API existante")
    @Auditable(action = "API_UPDATE_API_KEY")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiKeyDTO> updateApiKey(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateApiKeyCommand command) {
        log.debug("REST request pour mettre à jour la clé API {}: {}", id, command);

        if (!id.equals(command.getId())) {
            return ResponseEntity.badRequest().build();
        }

        ApiKeyDTO result = apiKeyService.updateApiKey(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère une clé API par son ID.
     *
     * @param id ID de la clé API
     * @return DTO de la clé API
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère une clé API par son ID")
    @Auditable(action = "API_GET_API_KEY")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiKeyDTO> getApiKey(@PathVariable UUID id) {
        log.debug("REST request pour récupérer la clé API: {}", id);
        ApiKeyDTO result = apiKeyService.getApiKey(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste toutes les clés API.
     *
     * @return Liste des DTOs de clés API
     */
    @GetMapping
    @Operation(summary = "Liste toutes les clés API")
    @Auditable(action = "API_LIST_API_KEYS")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApiKeyDTO>> listApiKeys() {
        log.debug("REST request pour lister toutes les clés API");
        List<ApiKeyDTO> result = apiKeyService.listApiKeys();
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les clés API par organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs de clés API
     */
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Liste les clés API par organisation")
    @Auditable(action = "API_LIST_API_KEYS_BY_ORGANIZATION")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApiKeyDTO>> listApiKeysByOrganization(@PathVariable UUID organizationId) {
        log.debug("REST request pour lister les clés API par organisation: {}", organizationId);
        List<ApiKeyDTO> result = apiKeyService.listApiKeysByOrganization(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les clés API par statut.
     *
     * @param status Statut des clés API
     * @return Liste des DTOs de clés API
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Liste les clés API par statut")
    @Auditable(action = "API_LIST_API_KEYS_BY_STATUS")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApiKeyDTO>> listApiKeysByStatus(@PathVariable ApiKeyStatus status) {
        log.debug("REST request pour lister les clés API par statut: {}", status);
        List<ApiKeyDTO> result = apiKeyService.listApiKeysByStatus(status);
        return ResponseEntity.ok(result);
    }

    /**
     * Révoque une clé API.
     *
     * @param id ID de la clé API à révoquer
     * @return DTO de la clé API révoquée
     */
    @PutMapping("/{id}/revoke")
    @Operation(summary = "Révoque une clé API")
    @Auditable(action = "API_REVOKE_API_KEY")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiKeyDTO> revokeApiKey(@PathVariable UUID id) {
        log.debug("REST request pour révoquer la clé API: {}", id);
        ApiKeyDTO result = apiKeyService.revokeApiKey(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Supprime une clé API.
     *
     * @param id ID de la clé API à supprimer
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une clé API")
    @Auditable(action = "API_DELETE_API_KEY")
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteApiKey(@PathVariable UUID id) {
        log.debug("REST request pour supprimer la clé API: {}", id);
        apiKeyService.deleteApiKey(id);
        return ResponseEntity.noContent().build();
    }
}
