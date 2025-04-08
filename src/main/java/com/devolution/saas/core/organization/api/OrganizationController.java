package com.devolution.saas.core.organization.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.organization.application.command.CreateOrganizationCommand;
import com.devolution.saas.core.organization.application.command.UpdateOrganizationCommand;
import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.application.dto.OrganizationHierarchyDTO;
import com.devolution.saas.core.organization.application.query.GetOrganizationQuery;
import com.devolution.saas.core.organization.application.query.ListOrganizationsQuery;
import com.devolution.saas.core.organization.application.service.OrganizationService;
import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des organisations.
 */
@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Organizations", description = "API pour la gestion des organisations")
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * Crée une nouvelle organisation.
     *
     * @param command Commande de création d'organisation
     * @return DTO de l'organisation créée
     */
    @PostMapping
    @Operation(summary = "Crée une nouvelle organisation")
    @Auditable(action = "API_CREATE_ORGANIZATION")
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody CreateOrganizationCommand command) {
        log.debug("REST request pour créer une organisation: {}", command);
        OrganizationDTO result = organizationService.createOrganization(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Met à jour une organisation existante.
     *
     * @param id      ID de l'organisation à mettre à jour
     * @param command Commande de mise à jour d'organisation
     * @return DTO de l'organisation mise à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une organisation existante")
    @Auditable(action = "API_UPDATE_ORGANIZATION")
    @TenantRequired
    public ResponseEntity<OrganizationDTO> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrganizationCommand command) {
        log.debug("REST request pour mettre à jour l'organisation {}: {}", id, command);

        if (!id.equals(command.getId())) {
            return ResponseEntity.badRequest().build();
        }

        OrganizationDTO result = organizationService.updateOrganization(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère une organisation par son ID.
     *
     * @param id ID de l'organisation
     * @return DTO de l'organisation
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère une organisation par son ID")
    @Auditable(action = "API_GET_ORGANIZATION")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable UUID id) {
        log.debug("REST request pour récupérer l'organisation: {}", id);
        GetOrganizationQuery query = new GetOrganizationQuery(id, null);
        OrganizationDTO result = organizationService.getOrganization(query);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère une organisation par son code.
     *
     * @param code Code de l'organisation
     * @return DTO de l'organisation
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère une organisation par son code")
    @Auditable(action = "API_GET_ORGANIZATION_BY_CODE")
    public ResponseEntity<OrganizationDTO> getOrganizationByCode(@PathVariable String code) {
        log.debug("REST request pour récupérer l'organisation par code: {}", code);
        GetOrganizationQuery query = new GetOrganizationQuery(null, code);
        OrganizationDTO result = organizationService.getOrganization(query);
        return ResponseEntity.ok(result);
    }

    /**
     * Liste les organisations selon les critères de filtrage.
     *
     * @param type       Type d'organisation à filtrer
     * @param status     Statut d'organisation à filtrer
     * @param parentId   ID de l'organisation parente à filtrer
     * @param rootsOnly  Indique si seules les organisations racines doivent être retournées
     * @param searchTerm Terme de recherche pour le nom ou le code
     * @param page       Numéro de page pour la pagination
     * @param size       Taille de page pour la pagination
     * @return Liste des DTOs d'organisations
     */
    @GetMapping
    @Operation(summary = "Liste les organisations selon les critères de filtrage")
    @Auditable(action = "API_LIST_ORGANIZATIONS")
    public ResponseEntity<List<OrganizationDTO>> listOrganizations(
            @Parameter(description = "Type d'organisation à filtrer")
            @RequestParam(required = false) OrganizationType type,

            @Parameter(description = "Statut d'organisation à filtrer")
            @RequestParam(required = false) OrganizationStatus status,

            @Parameter(description = "ID de l'organisation parente à filtrer")
            @RequestParam(required = false) UUID parentId,

            @Parameter(description = "Indique si seules les organisations racines doivent être retournées")
            @RequestParam(defaultValue = "false") boolean rootsOnly,

            @Parameter(description = "Terme de recherche pour le nom ou le code")
            @RequestParam(required = false) String searchTerm,

            @Parameter(description = "Numéro de page pour la pagination")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Taille de page pour la pagination")
            @RequestParam(defaultValue = "20") int size) {

        log.debug("REST request pour lister les organisations avec les critères: type={}, status={}, parentId={}, rootsOnly={}, searchTerm={}, page={}, size={}",
                type, status, parentId, rootsOnly, searchTerm, page, size);

        ListOrganizationsQuery query = ListOrganizationsQuery.builder()
                .type(type)
                .status(status)
                .parentId(parentId)
                .rootsOnly(rootsOnly)
                .searchTerm(searchTerm)
                .page(page)
                .size(size)
                .build();

        List<OrganizationDTO> result = organizationService.listOrganizations(query);
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère la hiérarchie complète d'une organisation.
     *
     * @param id ID de l'organisation racine
     * @return DTO de la hiérarchie
     */
    @GetMapping("/{id}/hierarchy")
    @Operation(summary = "Récupère la hiérarchie complète d'une organisation")
    @Auditable(action = "API_GET_ORGANIZATION_HIERARCHY")
    public ResponseEntity<OrganizationHierarchyDTO> getOrganizationHierarchy(@PathVariable UUID id) {
        log.debug("REST request pour récupérer la hiérarchie de l'organisation: {}", id);
        OrganizationHierarchyDTO result = organizationService.getOrganizationHierarchy(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Supprime une organisation.
     *
     * @param id ID de l'organisation à supprimer
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprime une organisation")
    @Auditable(action = "API_DELETE_ORGANIZATION")
    @TenantRequired
    public ResponseEntity<Void> deleteOrganization(@PathVariable UUID id) {
        log.debug("REST request pour supprimer l'organisation: {}", id);
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}
