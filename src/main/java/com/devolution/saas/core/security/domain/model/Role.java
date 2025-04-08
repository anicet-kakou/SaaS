package com.devolution.saas.core.security.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un rôle dans le système.
 * Un rôle est un ensemble de permissions qui peut être attribué à un utilisateur.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends TenantAwareEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Nom du rôle.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description du rôle.
     */
    @Column(name = "description")
    private String description;

    /**
     * Indique si le rôle est défini par le système.
     * Les rôles définis par le système ne peuvent pas être modifiés ou supprimés.
     */
    @Column(name = "system_defined", nullable = false)
    private boolean systemDefined = false;

    /**
     * Permissions associées au rôle.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    /**
     * Utilisateurs ayant ce rôle.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    /**
     * Ajoute une permission au rôle.
     *
     * @param permission Permission à ajouter
     */
    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    /**
     * Supprime une permission du rôle.
     *
     * @param permission Permission à supprimer
     */
    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }

    /**
     * Vérifie si le rôle a une permission spécifique.
     *
     * @param permissionName Nom de la permission
     * @return true si le rôle a la permission, false sinon
     */
    public boolean hasPermission(String permissionName) {
        return permissions.stream()
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }
}
