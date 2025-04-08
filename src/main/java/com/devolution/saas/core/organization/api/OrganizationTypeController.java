package com.devolution.saas.core.organization.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.organization.application.dto.OrganizationTypeDTO;
import com.devolution.saas.core.organization.application.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des types d'organisations.
 */
@RestController
@RequestMapping("/api/v1/organization-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Organization Types", description = "API pour la gestion des types d'organisations")
public class OrganizationTypeController {

    private final OrganizationService organizationService;

    /**
     * Liste tous les types d'organisations disponibles.
     *
     * @return Liste des DTOs de types d'organisations
     */
    @GetMapping
    @Operation(summary = "Liste tous les types d'organisations disponibles")
    @Auditable(action = "API_LIST_ORGANIZATION_TYPES")
    public ResponseEntity<List<OrganizationTypeDTO>> listOrganizationTypes() {
        log.debug("REST request pour lister les types d'organisations");
        List<OrganizationTypeDTO> result = organizationService.listOrganizationTypes();
        return ResponseEntity.ok(result);
    }
}
