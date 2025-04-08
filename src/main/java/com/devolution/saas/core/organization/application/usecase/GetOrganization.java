package com.devolution.saas.core.organization.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.application.mapper.OrganizationMapper;
import com.devolution.saas.core.organization.application.query.GetOrganizationQuery;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'utilisation pour la récupération d'une organisation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetOrganization {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param query Requête pour obtenir une organisation
     * @return DTO de l'organisation
     */
    @Transactional(readOnly = true)
    public OrganizationDTO execute(GetOrganizationQuery query) {
        log.debug("Récupération de l'organisation: {}", query);

        if (!query.isValid()) {
            throw new ValidationException("L'ID ou le code de l'organisation est obligatoire");
        }

        Organization organization;
        if (query.getId() != null) {
            organization = organizationRepository.findById(query.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", query.getId()));
        } else {
            organization = organizationRepository.findByCode(query.getCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", query.getCode()));
        }

        return organizationMapper.toDTO(organization);
    }
}
