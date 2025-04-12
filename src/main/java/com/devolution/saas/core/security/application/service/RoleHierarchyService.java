package com.devolution.saas.core.security.application.service;

import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service pour gérer la hiérarchie des rôles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleHierarchyService {

    private final RoleRepository roleRepository;

    /**
     * Récupère toutes les permissions d'un rôle, y compris celles héritées de ses rôles parents.
     *
     * @param role Rôle pour lequel récupérer les permissions
     * @return Ensemble des permissions du rôle
     */
    @Transactional(readOnly = true)
    public Set<Permission> getAllPermissions(Role role) {
        Set<Permission> allPermissions = new HashSet<>(role.getPermissions());
        Set<UUID> checkedRoles = new HashSet<>();

        // Ajouter les permissions des rôles parents
        addParentPermissions(role, allPermissions, checkedRoles);

        return allPermissions;
    }

    /**
     * Ajoute récursivement les permissions des rôles parents.
     *
     * @param role           Rôle courant
     * @param allPermissions Ensemble des permissions à compléter
     * @param checkedRoles   Ensemble des rôles déjà vérifiés (pour éviter les boucles infinies)
     */
    private void addParentPermissions(Role role, Set<Permission> allPermissions, Set<UUID> checkedRoles) {
        // Éviter les boucles infinies
        if (role == null || checkedRoles.contains(role.getId())) {
            return;
        }
        checkedRoles.add(role.getId());

        // Ajouter les permissions du rôle parent
        Role parent = role.getParent();
        if (parent != null) {
            allPermissions.addAll(parent.getPermissions());
            addParentPermissions(parent, allPermissions, checkedRoles);
        }
    }

    /**
     * Vérifie si un rôle a une permission spécifique, en tenant compte de la hiérarchie des rôles.
     *
     * @param role           Rôle à vérifier
     * @param permissionName Nom de la permission
     * @return true si le rôle a la permission, false sinon
     */
    @Transactional(readOnly = true)
    public boolean hasPermission(Role role, String permissionName) {
        return role.hasPermissionWithHierarchy(permissionName);
    }

    /**
     * Définit le rôle parent d'un rôle.
     *
     * @param roleId   ID du rôle
     * @param parentId ID du rôle parent
     * @return Rôle mis à jour
     */
    @Transactional
    public Role setParent(UUID roleId, UUID parentId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rôle non trouvé: " + roleId));

        // Si parentId est null, supprimer le parent
        if (parentId == null) {
            role.setParent(null);
            return roleRepository.save(role);
        }

        // Vérifier que le parent existe
        Role parent = roleRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Rôle parent non trouvé: " + parentId));

        // Vérifier qu'il n'y a pas de cycle dans la hiérarchie
        if (wouldCreateCycle(role, parent)) {
            throw new IllegalArgumentException("Cette relation créerait un cycle dans la hiérarchie des rôles");
        }

        // Définir le parent
        role.setParent(parent);
        return roleRepository.save(role);
    }

    /**
     * Vérifie si l'ajout d'un parent créerait un cycle dans la hiérarchie des rôles.
     *
     * @param role   Rôle à modifier
     * @param parent Rôle parent potentiel
     * @return true si un cycle serait créé, false sinon
     */
    private boolean wouldCreateCycle(Role role, Role parent) {
        // Si le parent est null, pas de cycle possible
        if (parent == null) {
            return false;
        }

        // Si le parent est le même que le rôle, c'est un cycle
        if (role.getId().equals(parent.getId())) {
            return true;
        }

        // Vérifier récursivement les parents du parent
        Set<UUID> checkedRoles = new HashSet<>();
        checkedRoles.add(role.getId());

        Role currentParent = parent;
        while (currentParent != null) {
            // Si on a déjà vérifié ce rôle, c'est un cycle
            if (checkedRoles.contains(currentParent.getId())) {
                return true;
            }

            checkedRoles.add(currentParent.getId());
            currentParent = currentParent.getParent();
        }

        return false;
    }

    /**
     * Récupère tous les rôles enfants d'un rôle, de manière récursive.
     *
     * @param roleId ID du rôle
     * @return Liste des rôles enfants
     */
    @Transactional(readOnly = true)
    public List<Role> getAllChildren(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rôle non trouvé: " + roleId));

        List<Role> allChildren = new ArrayList<>();
        Set<UUID> checkedRoles = new HashSet<>();

        addChildrenRecursively(role, allChildren, checkedRoles);

        return allChildren;
    }

    /**
     * Ajoute récursivement les rôles enfants.
     *
     * @param role         Rôle courant
     * @param allChildren  Liste des rôles enfants à compléter
     * @param checkedRoles Ensemble des rôles déjà vérifiés (pour éviter les boucles infinies)
     */
    private void addChildrenRecursively(Role role, List<Role> allChildren, Set<UUID> checkedRoles) {
        // Éviter les boucles infinies
        if (role == null || checkedRoles.contains(role.getId())) {
            return;
        }
        checkedRoles.add(role.getId());

        // Ajouter les enfants directs
        for (Role child : role.getChildren()) {
            allChildren.add(child);
            addChildrenRecursively(child, allChildren, checkedRoles);
        }
    }

    /**
     * Construit une représentation de la hiérarchie des rôles sous forme de chaîne de caractères.
     *
     * @return Chaîne de caractères représentant la hiérarchie des rôles
     */
    @Transactional(readOnly = true)
    public String buildRoleHierarchyString() {
        StringBuilder hierarchyBuilder = new StringBuilder();

        // Récupérer tous les rôles racines (sans parent)
        List<Role> rootRoles = roleRepository.findByParentIsNull();

        // Construire la hiérarchie pour chaque rôle racine
        for (Role rootRole : rootRoles) {
            buildRoleHierarchyStringRecursively(rootRole, hierarchyBuilder, 0);
        }

        return hierarchyBuilder.toString();
    }

    /**
     * Construit récursivement une représentation de la hiérarchie des rôles sous forme de chaîne de caractères.
     *
     * @param role             Rôle courant
     * @param hierarchyBuilder Constructeur de chaîne de caractères
     * @param level            Niveau d'indentation
     */
    private void buildRoleHierarchyStringRecursively(Role role, StringBuilder hierarchyBuilder, int level) {
        // Ajouter l'indentation
        for (int i = 0; i < level; i++) {
            hierarchyBuilder.append("  ");
        }

        // Ajouter le rôle
        hierarchyBuilder.append(role.getName());

        // Ajouter les permissions directes
        hierarchyBuilder.append(" [");
        role.getPermissions().stream()
                .map(Permission::getName)
                .sorted()
                .forEach(name -> hierarchyBuilder.append(name).append(", "));
        if (!role.getPermissions().isEmpty()) {
            hierarchyBuilder.delete(hierarchyBuilder.length() - 2, hierarchyBuilder.length());
        }
        hierarchyBuilder.append("]");

        hierarchyBuilder.append("\n");

        // Ajouter les enfants
        for (Role child : role.getChildren()) {
            buildRoleHierarchyStringRecursively(child, hierarchyBuilder, level + 1);
        }
    }
}
