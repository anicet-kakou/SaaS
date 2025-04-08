package com.devolution.saas.core.organization.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.organization.application.command.UpdateOrganizationCommand;
import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.application.mapper.OrganizationMapper;
import com.devolution.saas.core.organization.domain.event.OrganizationUpdatedEvent;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.repository.OrganizationHierarchyRepository;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import com.devolution.saas.core.organization.infrastructure.messaging.OrganizationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'utilisation pour la mise à jour d'une organisation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateOrganization {

    private final OrganizationRepository organizationRepository;
    private final OrganizationHierarchyRepository hierarchyRepository;
    private final OrganizationEventPublisher eventPublisher;
    private final OrganizationMapper organizationMapper;
    private final CreateHierarchyEntries createHierarchyEntries;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de mise à jour d'organisation
     * @return DTO de l'organisation mise à jour
     */
    @Transactional
    public OrganizationDTO execute(UpdateOrganizationCommand command) {
        log.debug("Mise à jour de l'organisation: {}", command.getId());

        // Récupération de l'organisation
        Organization organization = organizationRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", command.getId()));

        // Mise à jour des champs
        organization.setName(command.getName());
        organization.setType(command.getType());
        organization.setStatus(command.getStatus());
        organization.setAddress(command.getAddress());
        organization.setPhone(command.getPhone());
        organization.setEmail(command.getEmail());
        organization.setWebsite(command.getWebsite());
        organization.setLogoUrl(command.getLogoUrl());
        organization.setPrimaryContactName(command.getPrimaryContactName());
        organization.setDescription(command.getDescription());
        organization.setSettings(command.getSettings());

        // Gestion de l'organisation parente
        if (command.getParentId() != null && !command.getParentId().equals(organization.getParent() != null ? organization.getParent().getId() : null)) {
            // Vérification que la nouvelle organisation parente existe
            Organization parent = organizationRepository.findById(command.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", command.getParentId()));

            // Vérification que la nouvelle organisation parente n'est pas un descendant
            if (hierarchyRepository.isAncestorOf(organization.getId(), parent.getId())) {
                throw new ValidationException("Une organisation ne peut pas être parente de l'un de ses ancêtres");
            }

            // Mise à jour de la hiérarchie
            hierarchyRepository.deleteAllByOrganizationId(organization.getId());
            organization.setParent(parent);
            organization = organizationRepository.save(organization);
            createHierarchyEntries.execute(organization);
        } else if (command.getParentId() == null && organization.getParent() != null) {
            // Suppression de la relation parente
            hierarchyRepository.deleteAllByOrganizationId(organization.getId());
            organization.setParent(null);
            organization = organizationRepository.save(organization);
            createHierarchyEntries.execute(organization);
        } else {
            // Pas de changement de parent
            organization = organizationRepository.save(organization);
        }

        // Publication de l'événement
        eventPublisher.publishOrganizationUpdatedEvent(
                new OrganizationUpdatedEvent(
                        organization.getId(),
                        organization.getName(),
                        organization.getCode(),
                        organization.getType().name(),
                        organization.getStatus().name(),
                        organization.getParent() != null ? organization.getParent().getId() : null
                )
        );

        return organizationMapper.toDTO(organization);
    }
}
