package com.devolution.saas.core.organization.application.service;

import com.devolution.saas.common.abstracts.AbstractCrudService;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantFilter;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.core.organization.application.command.CreateOrganizationCommand;
import com.devolution.saas.core.organization.application.command.UpdateOrganizationCommand;
import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.application.dto.OrganizationHierarchyDTO;
import com.devolution.saas.core.organization.application.dto.OrganizationTypeDTO;
import com.devolution.saas.core.organization.application.query.GetOrganizationQuery;
import com.devolution.saas.core.organization.application.query.ListOrganizationsQuery;
import com.devolution.saas.core.organization.application.usecase.*;
import com.devolution.saas.core.organization.domain.model.OrganizationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des organisations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService extends AbstractCrudService<OrganizationDTO, UUID, CreateOrganizationCommand, UpdateOrganizationCommand> {

    private final CreateOrganization createOrganization;
    private final UpdateOrganization updateOrganization;
    private final GetOrganization getOrganization;
    private final ListOrganizations listOrganizations;
    private final GetOrganizationHierarchy getOrganizationHierarchy;
    private final DeleteOrganization deleteOrganization;

    @Override
    @TenantRequired
    protected OrganizationDTO executeCreate(CreateOrganizationCommand command) {
        log.debug("Création d'une nouvelle organisation: {}", command.getName());
        return createOrganization.execute(command);
    }

    @Override
    @TenantRequired
    protected OrganizationDTO executeUpdate(UpdateOrganizationCommand command) {
        log.debug("Mise à jour de l'organisation: {}", command.getId());
        return updateOrganization.execute(command);
    }

    @Override
    protected OrganizationDTO executeGet(UUID id) {
        log.debug("Récupération de l'organisation: {}", id);
        GetOrganizationQuery query = new GetOrganizationQuery(id, null);
        return getOrganization.execute(query);
    }

    @Override
    @TenantFilter(includeDescendants = true)
    protected List<OrganizationDTO> executeList() {
        log.debug("Listage de toutes les organisations");
        ListOrganizationsQuery query = ListOrganizationsQuery.builder()
                .page(0)
                .size(100)
                .build();
        return listOrganizations.execute(query);
    }

    @Override
    @TenantRequired
    protected void executeDelete(UUID id) {
        log.debug("Suppression de l'organisation: {}", id);
        deleteOrganization.execute(id);
    }

    @Override
    protected String getEntityName() {
        return "organisation";
    }

    /**
     * Récupère une organisation par ID ou code.
     *
     * @param query Requête pour obtenir une organisation
     * @return DTO de l'organisation
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_ORGANIZATION")
    public OrganizationDTO getOrganization(GetOrganizationQuery query) {
        log.debug("Récupération de l'organisation: {}", query);
        return getOrganization.execute(query);
    }

    /**
     * Liste les organisations selon les critères de filtrage.
     *
     * @param query Requête pour lister les organisations
     * @return Liste des DTOs d'organisations
     */
    @Transactional(readOnly = true)
    @Auditable(action = "LIST_ORGANIZATIONS")
    @TenantFilter(includeDescendants = true)
    public List<OrganizationDTO> listOrganizations(ListOrganizationsQuery query) {
        log.debug("Listage des organisations avec les critères: {}", query);
        return listOrganizations.execute(query);
    }

    /**
     * Récupère la hiérarchie complète d'une organisation.
     *
     * @param organizationId ID de l'organisation racine
     * @return DTO de la hiérarchie
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_ORGANIZATION_HIERARCHY")
    public OrganizationHierarchyDTO getOrganizationHierarchy(UUID organizationId) {
        log.debug("Récupération de la hiérarchie de l'organisation: {}", organizationId);
        return getOrganizationHierarchy.execute(organizationId);
    }

    /**
     * Liste tous les types d'organisations disponibles.
     *
     * @return Liste des DTOs de types d'organisations
     */
    @Transactional(readOnly = true)
    public List<OrganizationTypeDTO> listOrganizationTypes() {
        log.debug("Listage des types d'organisations");

        return Arrays.stream(OrganizationType.values())
                .map(OrganizationTypeDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Crée une nouvelle organisation.
     *
     * @param command Commande de création d'organisation
     * @return DTO de l'organisation créée
     */
    public OrganizationDTO createOrganization(CreateOrganizationCommand command) {
        return create(command);
    }

    /**
     * Met à jour une organisation existante.
     *
     * @param command Commande de mise à jour d'organisation
     * @return DTO de l'organisation mise à jour
     */
    public OrganizationDTO updateOrganization(UpdateOrganizationCommand command) {
        return update(command);
    }

    /**
     * Supprime une organisation.
     *
     * @param organizationId ID de l'organisation à supprimer
     */
    public void deleteOrganization(UUID organizationId) {
        delete(organizationId);
    }

    // Cette méthode a été déplacée vers CreateHierarchyEntriesUseCase

    // Cette méthode a été déplacée vers GetOrganizationHierarchyUseCase

    // Cette méthode a été déplacée vers OrganizationMapper
}
