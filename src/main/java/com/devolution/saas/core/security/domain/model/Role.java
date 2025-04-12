package com.devolution.saas.core.security.domain.model;

import com.devolution.saas.common.domain.model.SystemDefinedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entité représentant un rôle dans le système.
 * Un rôle est un ensemble de permissions qui peut être attribué à un utilisateur.
 */
@Entity
@Table(
        name = "roles",
        indexes = {
                @Index(name = "idx_roles_name", columnList = "name"),
                @Index(name = "idx_roles_system_defined", columnList = "system_defined")
        }
)
@Getter
@Setter
public class Role extends SystemDefinedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Nom du rôle.
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * Description du rôle.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Note: Le champ systemDefined est hérité de SystemDefinedEntity.
     * Les rôles définis par le système ne peuvent pas être modifiés ou supprimés.
     */

    /**
     * Permissions associées au rôle.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(
                    name = "role_id",
                    foreignKey = @ForeignKey(name = "fk_role_permissions_role")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id",
                    foreignKey = @ForeignKey(name = "fk_role_permissions_permission")
            )
    )
    private Set<Permission> permissions = new HashSet<>();

    /**
     * Utilisateurs ayant ce rôle.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    /**
     * Rôle parent dans la hiérarchie.
     * Si non null, ce rôle hérite des permissions du rôle parent.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "parent_id",
            foreignKey = @ForeignKey(name = "fk_role_parent")
    )
    private Role parent;

    /**
     * Rôles enfants dans la hiérarchie.
     * Ces rôles héritent des permissions de ce rôle.
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Role> children = new HashSet<>();

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
     * Ajoute un rôle enfant à ce rôle.
     *
     * @param child Rôle enfant à ajouter
     */
    public void addChild(Role child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Supprime un rôle enfant de ce rôle.
     *
     * @param child Rôle enfant à supprimer
     */
    public void removeChild(Role child) {
        children.remove(child);
        child.setParent(null);
    }

    /**
     * Vérifie si le rôle a une permission spécifique.
     * Cette méthode ne vérifie que les permissions directement attribuées à ce rôle,
     * sans tenir compte de la hiérarchie des rôles.
     *
     * @param permissionName Nom de la permission
     * @return true si le rôle a la permission, false sinon
     */
    public boolean hasPermission(String permissionName) {
        return permissions.stream()
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }

    /**
     * Vérifie si le rôle a une permission spécifique, en tenant compte de la hiérarchie des rôles.
     * Cette méthode vérifie les permissions directement attribuées à ce rôle,
     * ainsi que celles héritées des rôles parents.
     *
     * @param permissionName Nom de la permission
     * @param checkedRoles   Ensemble des rôles déjà vérifiés (pour éviter les boucles infinies)
     * @return true si le rôle a la permission, false sinon
     */
    public boolean hasPermissionWithHierarchy(String permissionName, Set<UUID> checkedRoles) {
        // Éviter les boucles infinies
        if (checkedRoles.contains(getId())) {
            return false;
        }
        checkedRoles.add(getId());

        // Vérifier les permissions directes
        if (hasPermission(permissionName)) {
            return true;
        }

        // Vérifier les permissions héritées du rôle parent
        return parent != null && parent.hasPermissionWithHierarchy(permissionName, checkedRoles);
    }

    /**
     * Vérifie si le rôle a une permission spécifique, en tenant compte de la hiérarchie des rôles.
     * Cette méthode est une surcharge qui initialise l'ensemble des rôles vérifiés.
     *
     * @param permissionName Nom de la permission
     * @return true si le rôle a la permission, false sinon
     */
    public boolean hasPermissionWithHierarchy(String permissionName) {
        return hasPermissionWithHierarchy(permissionName, new HashSet<>());
    }
}
