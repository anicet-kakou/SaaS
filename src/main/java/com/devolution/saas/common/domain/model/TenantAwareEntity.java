package com.devolution.saas.common.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Entité qui prend en charge le multi-tenant.
 * Étend AuditableEntity et ajoute un identifiant d'organisation pour la séparation des données.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class TenantAwareEntity extends AuditableEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant de l'organisation à laquelle appartient cette entité.
     * Utilisé pour la séparation des données dans un environnement multi-tenant.
     */
    @Column(name = "organization_id")
    private UUID organizationId;
}
