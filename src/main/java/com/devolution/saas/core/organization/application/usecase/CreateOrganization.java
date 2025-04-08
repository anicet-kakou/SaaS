package com.devolution.saas.core.organization.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.organization.application.command.CreateOrganizationCommand;
import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.application.mapper.OrganizationMapper;
import com.devolution.saas.core.organization.domain.event.OrganizationCreatedEvent;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import com.devolution.saas.core.organization.infrastructure.messaging.OrganizationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'utilisation pour la création d'une organisation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateOrganization {

    private final OrganizationRepository organizationRepository;
    private final OrganizationEventPublisher eventPublisher;
    private final OrganizationMapper organizationMapper;
    private final CreateHierarchyEntries createHierarchyEntries;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de création d'organisation
     * @return DTO de l'organisation créée
     */
    @Transactional
    public OrganizationDTO execute(CreateOrganizationCommand command) {
        log.debug("Création d'une nouvelle organisation: {}", command.getName());

        // Validation du code unique
        if (organizationRepository.existsByCode(command.getCode())) {
            throw new ValidationException("Le code de l'organisation est déjà utilisé");
        }

        // Création de l'organisation
        Organization organization = new Organization();
        organization.setName(command.getName());
        organization.setCode(command.getCode());
        organization.setType(command.getType());
        organization.setAddress(command.getAddress());
        organization.setPhone(command.getPhone());
        organization.setEmail(command.getEmail());
        organization.setWebsite(command.getWebsite());
        organization.setLogoUrl(command.getLogoUrl());
        organization.setPrimaryContactName(command.getPrimaryContactName());
        organization.setDescription(command.getDescription());
        organization.setSettings(command.getSettings());

        // Gestion de l'organisation parente
        if (command.getParentId() != null) {
            Organization parent = organizationRepository.findById(command.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", command.getParentId()));
            organization.setParent(parent);
        }

        // Sauvegarde de l'organisation
        organization = organizationRepository.save(organization);

        // Création des entrées de hiérarchie
        createHierarchyEntries.execute(organization);

        // Publication de l'événement
        eventPublisher.publishOrganizationCreatedEvent(
                new OrganizationCreatedEvent(
                        organization.getId(),
                        organization.getName(),
                        organization.getCode(),
                        organization.getType().name(),
                        organization.getParent() != null ? organization.getParent().getId() : null
                )
        );

        return organizationMapper.toDTO(organization);
    }
}
