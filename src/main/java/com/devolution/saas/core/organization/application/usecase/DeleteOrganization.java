package com.devolution.saas.core.organization.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.repository.OrganizationHierarchyRepository;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour la suppression d'une organisation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteOrganization {

    private final OrganizationRepository organizationRepository;
    private final OrganizationHierarchyRepository hierarchyRepository;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param organizationId ID de l'organisation à supprimer
     */
    @Transactional
    public void execute(UUID organizationId) {
        log.debug("Suppression de l'organisation: {}", organizationId);

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", organizationId));

        // Vérification que l'organisation n'a pas d'enfants
        if (organization.hasChildren()) {
            throw new ValidationException("Impossible de supprimer une organisation qui a des organisations enfants");
        }

        // Suppression des entrées de hiérarchie
        hierarchyRepository.deleteAllByOrganizationId(organizationId);

        // Suppression de l'organisation
        organizationRepository.delete(organization);
    }
}
