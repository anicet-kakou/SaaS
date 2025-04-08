package com.devolution.saas.core.organization.api;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.organization.application.dto.OrganizationHierarchyDTO;
import com.devolution.saas.core.organization.application.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Contrôleur REST pour la gestion de la hiérarchie des organisations.
 */
@RestController
@RequestMapping("/api/v1/organization-hierarchies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Organization Hierarchies", description = "API pour la gestion de la hiérarchie des organisations")
public class OrganizationHierarchyController {

    private final OrganizationService organizationService;

    /**
     * Récupère la hiérarchie complète d'une organisation.
     *
     * @param organizationId ID de l'organisation racine
     * @return DTO de la hiérarchie
     */
    @GetMapping("/{organizationId}")
    @Operation(summary = "Récupère la hiérarchie complète d'une organisation")
    @Auditable(action = "API_GET_ORGANIZATION_HIERARCHY")
    public ResponseEntity<OrganizationHierarchyDTO> getOrganizationHierarchy(@PathVariable UUID organizationId) {
        log.debug("REST request pour récupérer la hiérarchie de l'organisation: {}", organizationId);
        OrganizationHierarchyDTO result = organizationService.getOrganizationHierarchy(organizationId);
        return ResponseEntity.ok(result);
    }
}
