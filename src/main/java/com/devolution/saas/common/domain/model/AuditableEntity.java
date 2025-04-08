package com.devolution.saas.common.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité auditable qui étend BaseEntity avec des fonctionnalités d'audit supplémentaires.
 * Utilisée pour les entités qui nécessitent un suivi d'audit complet.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Indique si l'entité est active.
     */
    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * Désactive l'entité (soft delete).
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Active l'entité.
     */
    public void activate() {
        this.active = true;
    }
}
