package com.devolution.saas.core.organization.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Entité représentant la hiérarchie des organisations.
 * Cette entité permet de stocker les relations hiérarchiques entre les organisations
 * de manière optimisée pour les requêtes de type "tous les descendants" ou "tous les ancêtres".
 */
@Entity
@Table(name = "organization_hierarchies")
@Getter
@Setter
public class OrganizationHierarchy extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Organisation ancêtre.
     */
    @Column(name = "ancestor_id", nullable = false)
    private UUID ancestorId;

    /**
     * Organisation descendante.
     */
    @Column(name = "descendant_id", nullable = false)
    private UUID descendantId;

    /**
     * Distance entre l'ancêtre et le descendant dans l'arbre hiérarchique.
     * 0 = même organisation, 1 = parent direct, etc.
     */
    @Column(name = "distance", nullable = false)
    private int distance;

    /**
     * Constructeur par défaut.
     */
    public OrganizationHierarchy() {
    }

    /**
     * Constructeur avec tous les champs.
     *
     * @param ancestorId   ID de l'organisation ancêtre
     * @param descendantId ID de l'organisation descendante
     * @param distance     Distance entre l'ancêtre et le descendant
     */
    public OrganizationHierarchy(UUID ancestorId, UUID descendantId, int distance) {
        this.ancestorId = ancestorId;
        this.descendantId = descendantId;
        this.distance = distance;
    }

    /**
     * Crée une entrée de hiérarchie pour une organisation qui est son propre ancêtre (distance = 0).
     *
     * @param organizationId ID de l'organisation
     * @return Instance de OrganizationHierarchy
     */
    public static OrganizationHierarchy createSelfReference(UUID organizationId) {
        return new OrganizationHierarchy(organizationId, organizationId, 0);
    }
}
