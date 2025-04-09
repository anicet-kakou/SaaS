package com.devolution.saas.core.organization.api;

import com.devolution.saas.common.abstracts.AbstractCrudController;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class OrganizationController extends AbstractCrudController<OrganizationDTO, UUID, CreateOrganizationCommand, UpdateOrganizationCommand> {

    private final OrganizationService organizationService;

    @Override
    @TenantRequired
    protected OrganizationDTO create(CreateOrganizationCommand command) {
        log.debug("REST request pour créer une organisation: {}", command);
        return organizationService.createOrganization(command);
    }

    @Override
    @TenantRequired
    protected OrganizationDTO update(UUID id, UpdateOrganizationCommand command) {
        log.debug("REST request pour mettre à jour l'organisation {}: {}", id, command);
        return organizationService.updateOrganization(command);
    }

    @Override
    protected OrganizationDTO get(UUID id) {
        log.debug("REST request pour récupérer l'organisation: {}", id);
        GetOrganizationQuery query = new GetOrganizationQuery(id, null);
        return organizationService.getOrganization(query);
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

    @Override
    protected List<OrganizationDTO> list() {
        log.debug("REST request pour lister toutes les organisations");
        ListOrganizationsQuery query = ListOrganizationsQuery.builder()
                .page(0)
                .size(100)
                .build();
        return organizationService.listOrganizations(query);
    }

    @Override
    protected void delete(UUID id) {
        log.debug("REST request pour supprimer l'organisation: {}", id);
        organizationService.deleteOrganization(id);
    }

    @Override
    protected String getEntityName() {
        return "organisation";
    }

    @Override
    protected boolean isValidId(UUID id, UpdateOrganizationCommand command) {
        return id.equals(command.getId());
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
    @GetMapping("/search")
    @Operation(summary = "Liste les organisations selon les critères de filtrage")
    @Auditable(action = "API_LIST_ORGANIZATIONS")
    public ResponseEntity<List<OrganizationDTO>> searchOrganizations(
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


}
