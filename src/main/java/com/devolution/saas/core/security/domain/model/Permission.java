package com.devolution.saas.core.security.domain.model;

import com.devolution.saas.common.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant une permission dans le système.
 * Une permission définit une action spécifique sur une ressource.
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Nom de la permission.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Description de la permission.
     */
    @Column(name = "description")
    private String description;

    /**
     * Type de ressource concernée par la permission.
     */
    @Column(name = "resource_type", nullable = false)
    private String resourceType;

    /**
     * Action autorisée sur la ressource.
     */
    @Column(name = "action", nullable = false)
    private String action;

    /**
     * Indique si la permission est définie par le système.
     * Les permissions définies par le système ne peuvent pas être modifiées ou supprimées.
     */
    @Column(name = "system_defined", nullable = false)
    private boolean systemDefined = false;

    /**
     * Rôles ayant cette permission.
     */
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructeur par défaut.
     */
    public Permission() {
    }

    /**
     * Constructeur avec tous les champs.
     *
     * @param name          Nom de la permission
     * @param description   Description de la permission
     * @param resourceType  Type de ressource
     * @param action        Action autorisée
     * @param systemDefined Indique si la permission est définie par le système
     */
    public Permission(String name, String description, String resourceType, String action, boolean systemDefined) {
        this.name = name;
        this.description = description;
        this.resourceType = resourceType;
        this.action = action;
        this.systemDefined = systemDefined;
    }

    /**
     * Crée une permission système.
     *
     * @param resourceType Type de ressource
     * @param action       Action autorisée
     * @param description  Description de la permission
     * @return Instance de Permission
     */
    public static Permission createSystemPermission(String resourceType, String action, String description) {
        String name = resourceType.toUpperCase() + "_" + action.toUpperCase();
        return new Permission(name, description, resourceType, action, true);
    }
}
