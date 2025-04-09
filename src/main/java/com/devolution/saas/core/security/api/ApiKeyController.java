package com.devolution.saas.core.security.api;

import com.devolution.saas.common.abstracts.AbstractCrudController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.security.application.command.CreateApiKeyCommand;
import com.devolution.saas.core.security.application.command.UpdateApiKeyCommand;
import com.devolution.saas.core.security.application.dto.ApiKeyDTO;
import com.devolution.saas.core.security.application.service.ApiKeyService;
import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ApiKeyController extends AbstractCrudController<ApiKeyDTO, UUID, CreateApiKeyCommand, UpdateApiKeyCommand> {

    private final ApiKeyService apiKeyService;

    @Override
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    protected ApiKeyDTO create(CreateApiKeyCommand command) {
        log.debug("REST request pour créer une clé API: {}", command.getName());
        return apiKeyService.createApiKey(command);
    }

    @Override
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    protected ApiKeyDTO update(UUID id, UpdateApiKeyCommand command) {
        log.debug("REST request pour mettre à jour la clé API {}: {}", id, command);
        return apiKeyService.updateApiKey(command);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    protected ApiKeyDTO get(UUID id) {
        log.debug("REST request pour récupérer la clé API: {}", id);
        return apiKeyService.getApiKey(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    protected List<ApiKeyDTO> list() {
        log.debug("REST request pour lister toutes les clés API");
        return apiKeyService.listApiKeys();
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

    @Override
    @TenantRequired
    @PreAuthorize("hasRole('ADMIN')")
    protected void delete(UUID id) {
        log.debug("REST request pour supprimer la clé API: {}", id);
        apiKeyService.deleteApiKey(id);
    }

    @Override
    protected String getEntityName() {
        return "clé API";
    }

    @Override
    protected boolean isValidId(UUID id, UpdateApiKeyCommand command) {
        return id.equals(command.getId());
    }
}
