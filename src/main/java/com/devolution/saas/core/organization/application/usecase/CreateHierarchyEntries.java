package com.devolution.saas.core.organization.application.usecase;

import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.model.OrganizationHierarchy;
import com.devolution.saas.core.organization.domain.repository.OrganizationHierarchyRepository;
import com.devolution.saas.core.organization.infrastructure.persistence.JpaOrganizationHierarchyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cas d'utilisation pour la création des entrées de hiérarchie d'une organisation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateHierarchyEntries {

    private final OrganizationHierarchyRepository hierarchyRepository;
    private final JpaOrganizationHierarchyRepository jpaHierarchyRepository;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param organization Organisation
     */
    @Transactional
    public void execute(Organization organization) {
        log.debug("Création des entrées de hiérarchie pour l'organisation: {}", organization.getId());

        // Création de l'entrée self-reference (distance = 0)
        OrganizationHierarchy selfReference = OrganizationHierarchy.createSelfReference(organization.getId());
        hierarchyRepository.save(selfReference);

        // Si l'organisation a un parent, création des entrées pour tous les ancêtres
        if (organization.getParent() != null) {
            UUID parentId = organization.getParent().getId();

            // Relation directe parent-enfant (distance = 1)
            OrganizationHierarchy directParent = new OrganizationHierarchy(parentId, organization.getId(), 1);
            hierarchyRepository.save(directParent);

            // Relations avec tous les ancêtres du parent
            List<OrganizationHierarchy> parentAncestors = hierarchyRepository.findAllByDescendantId(parentId);
            List<OrganizationHierarchy> newEntries = parentAncestors.stream()
                    .filter(h -> h.getAncestorId() != parentId) // Exclure la relation directe déjà créée
                    .map(h -> new OrganizationHierarchy(
                            h.getAncestorId(),
                            organization.getId(),
                            h.getDistance() + 1
                    ))
                    .collect(Collectors.toList());

            if (!newEntries.isEmpty()) {
                jpaHierarchyRepository.saveAll(newEntries);
            }
        }
    }
}
