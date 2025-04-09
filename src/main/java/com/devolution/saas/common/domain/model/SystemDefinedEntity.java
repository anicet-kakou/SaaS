package com.devolution.saas.common.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité qui peut être définie par le système.
 * Étend TenantAwareEntity et ajoute un indicateur pour les entités définies par le système.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class SystemDefinedEntity extends TenantAwareEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Indique si l'entité est définie par le système.
     * Les entités définies par le système ne peuvent généralement pas être modifiées ou supprimées.
     */
    @Column(name = "system_defined", nullable = false)
    private boolean systemDefined = false;
}
