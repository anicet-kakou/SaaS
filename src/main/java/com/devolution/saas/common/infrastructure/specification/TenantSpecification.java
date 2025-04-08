package com.devolution.saas.common.infrastructure.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;
import java.util.UUID;

/**
 * Spécification JPA pour filtrer les entités par tenant (organisation).
 *
 * @param <T> Type de l'entité
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1L;

    /**
     * ID de l'organisation courante.
     */
    private UUID organizationId;

    /**
     * Ensemble des IDs des organisations visibles.
     * Si non null, les entités appartenant à ces organisations seront incluses.
     */
    private Set<UUID> visibleOrganizationIds;

    /**
     * Nom de l'attribut d'entité qui contient l'ID de l'organisation.
     */
    private String organizationIdField;

    /**
     * Crée une spécification pour filtrer par l'organisation courante.
     *
     * @param organizationId      ID de l'organisation
     * @param organizationIdField Nom de l'attribut d'entité qui contient l'ID de l'organisation
     * @param <T>                 Type de l'entité
     * @return Spécification
     */
    public static <T> TenantSpecification<T> byOrganization(UUID organizationId, String organizationIdField) {
        return TenantSpecification.<T>builder()
                .organizationId(organizationId)
                .organizationIdField(organizationIdField)
                .build();
    }

    /**
     * Crée une spécification pour filtrer par les organisations visibles.
     *
     * @param visibleOrganizationIds Ensemble des IDs des organisations visibles
     * @param organizationIdField    Nom de l'attribut d'entité qui contient l'ID de l'organisation
     * @param <T>                    Type de l'entité
     * @return Spécification
     */
    public static <T> TenantSpecification<T> byVisibleOrganizations(Set<UUID> visibleOrganizationIds, String organizationIdField) {
        return TenantSpecification.<T>builder()
                .visibleOrganizationIds(visibleOrganizationIds)
                .organizationIdField(organizationIdField)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (visibleOrganizationIds != null && !visibleOrganizationIds.isEmpty()) {
            // Filtrer par les organisations visibles
            return root.get(organizationIdField).in(visibleOrganizationIds);
        } else if (organizationId != null) {
            // Filtrer par l'organisation courante uniquement
            return criteriaBuilder.equal(root.get(organizationIdField), organizationId);
        } else {
            // Pas de filtrage
            return criteriaBuilder.conjunction();
        }
    }
}
