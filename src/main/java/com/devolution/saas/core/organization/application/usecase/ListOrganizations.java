package com.devolution.saas.core.organization.application.usecase;

import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.application.mapper.OrganizationMapper;
import com.devolution.saas.core.organization.application.query.ListOrganizationsQuery;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.infrastructure.persistence.JpaOrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cas d'utilisation pour la liste des organisations.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ListOrganizations {

    private final JpaOrganizationRepository jpaOrganizationRepository;
    private final OrganizationMapper organizationMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param query Requête pour lister les organisations
     * @return Liste des DTOs d'organisations
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> execute(ListOrganizationsQuery query) {
        log.debug("Listage des organisations avec les critères: {}", query);

        // Création de la spécification de base
        Specification<Organization> spec = Specification.where(null);

        // Ajout des critères de filtrage
        if (query.type() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), query.type()));
        }

        if (query.status() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), query.status()));
        }

        if (query.parentId() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("parent").get("id"), query.parentId()));
        }

        if (query.rootsOnly()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.isNull(root.get("parent")));
        }

        if (query.searchTerm() != null && !query.searchTerm().trim().isEmpty()) {
            String searchTerm = "%" + query.searchTerm().toLowerCase() + "%";
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchTerm),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), searchTerm)
                    ));
        }

        // Exécution de la requête avec la spécification
        List<Organization> organizations = jpaOrganizationRepository.findAll(spec);

        // Pagination simple (à améliorer avec Spring Data Pageable)
        int start = query.page() * query.size();
        int end = Math.min(start + query.size(), organizations.size());

        if (start < organizations.size()) {
            organizations = organizations.subList(start, end);
        } else {
            organizations = Collections.emptyList();
        }

        return organizations.stream()
                .map(organizationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
