package com.devolution.saas.core.organization.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.organization.application.dto.OrganizationHierarchyDTO;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour la récupération de la hiérarchie d'une organisation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetOrganizationHierarchy {

    private final OrganizationRepository organizationRepository;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param organizationId ID de l'organisation racine
     * @return DTO de la hiérarchie
     */
    @Transactional(readOnly = true)
    public OrganizationHierarchyDTO execute(UUID organizationId) {
        log.debug("Récupération de la hiérarchie de l'organisation: {}", organizationId);

        Organization root = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", organizationId));

        return buildHierarchyDTO(root, 0);
    }

    /**
     * Construit récursivement un DTO de hiérarchie.
     *
     * @param organization Organisation
     * @param level        Niveau dans la hiérarchie
     * @return DTO de hiérarchie
     */
    private OrganizationHierarchyDTO buildHierarchyDTO(Organization organization, int level) {
        OrganizationHierarchyDTO dto = OrganizationHierarchyDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .code(organization.getCode())
                .type(organization.getType().name())
                .level(level)
                .build();

        for (Organization child : organization.getChildren()) {
            dto.addChild(buildHierarchyDTO(child, level + 1));
        }

        return dto;
    }
}
