package com.devolution.saas.core.security.infrastructure.service;

import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service pour les vérifications d'autorisation personnalisées.
 * Utilisé dans les expressions SpEL des annotations @PreAuthorize.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final TenantContextHolder tenantContextHolder;
    private final RoleRepository roleRepository;

    /**
     * Vérifie si l'utilisateur courant est l'utilisateur spécifié.
     *
     * @param userId ID de l'utilisateur à vérifier
     * @return true si l'utilisateur courant est l'utilisateur spécifié, false sinon
     */
    public boolean isCurrentUser(UUID userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return false;
        }

        User currentUser = (User) principal;
        return currentUser.getId().equals(userId);
    }

    /**
     * Vérifie si l'utilisateur courant a le nom d'utilisateur spécifié.
     *
     * @param username Nom d'utilisateur à vérifier
     * @return true si l'utilisateur courant a le nom d'utilisateur spécifié, false sinon
     */
    public boolean isCurrentUsername(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return false;
        }

        User currentUser = (User) principal;
        return currentUser.getUsername().equals(username);
    }

    /**
     * Vérifie si l'utilisateur courant a l'adresse email spécifiée.
     *
     * @param email Adresse email à vérifier
     * @return true si l'utilisateur courant a l'adresse email spécifiée, false sinon
     */
    public boolean isCurrentUserEmail(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return false;
        }

        User currentUser = (User) principal;
        return currentUser.getEmail().equals(email);
    }

    /**
     * Vérifie si l'utilisateur courant appartient à l'organisation spécifiée.
     *
     * @param organizationId ID de l'organisation à vérifier
     * @return true si l'utilisateur courant appartient à l'organisation spécifiée, false sinon
     */
    public boolean isUserInOrganization(UUID organizationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return false;
        }

        User currentUser = (User) principal;
        return currentUser.getOrganizations().stream()
                .anyMatch(org -> org.getOrganizationId().equals(organizationId));
    }

    /**
     * Vérifie si l'utilisateur courant a le rôle spécifié.
     *
     * @param roleName Nom du rôle à vérifier
     * @return true si l'utilisateur courant a le rôle spécifié, false sinon
     */
    public boolean hasRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + roleName));
    }

    /**
     * Vérifie si l'utilisateur courant a au moins un des rôles spécifiés.
     *
     * @param roleNames Noms des rôles à vérifier
     * @return true si l'utilisateur courant a au moins un des rôles spécifiés, false sinon
     */
    public boolean hasAnyRole(String... roleNames) {
        for (String roleName : roleNames) {
            if (hasRole(roleName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si l'utilisateur courant a tous les rôles spécifiés.
     *
     * @param roleNames Noms des rôles à vérifier
     * @return true si l'utilisateur courant a tous les rôles spécifiés, false sinon
     */
    public boolean hasAllRoles(String... roleNames) {
        for (String roleName : roleNames) {
            if (!hasRole(roleName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si l'utilisateur courant a la permission spécifiée.
     * Cette méthode tient compte de la hiérarchie des rôles.
     *
     * @param permissionName Nom de la permission à vérifier
     * @return true si l'utilisateur courant a la permission spécifiée, false sinon
     */
    public boolean hasPermission(String permissionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Vérifier d'abord si l'utilisateur a directement la permission
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(permissionName))) {
            return true;
        }

        // Si l'utilisateur n'a pas directement la permission, vérifier la hiérarchie des rôles
        // Extraire les rôles de l'utilisateur (format: ROLE_XXX)
        return authentication.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(authority -> authority.getAuthority().substring(5)) // Enlever le préfixe "ROLE_"
                .anyMatch(roleName -> {
                    Optional<Role> roleOpt = roleRepository.findByName(roleName);
                    return roleOpt.isPresent() && roleOpt.get().hasPermissionWithHierarchy(permissionName);
                });
    }

    /**
     * Vérifie si l'utilisateur courant a au moins une des permissions spécifiées.
     *
     * @param permissionNames Noms des permissions à vérifier
     * @return true si l'utilisateur courant a au moins une des permissions spécifiées, false sinon
     */
    public boolean hasAnyPermission(String... permissionNames) {
        for (String permissionName : permissionNames) {
            if (hasPermission(permissionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si l'utilisateur courant a toutes les permissions spécifiées.
     *
     * @param permissionNames Noms des permissions à vérifier
     * @return true si l'utilisateur courant a toutes les permissions spécifiées, false sinon
     */
    public boolean hasAllPermissions(String... permissionNames) {
        for (String permissionName : permissionNames) {
            if (!hasPermission(permissionName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Récupère l'ID de l'organisation courante du contexte.
     *
     * @return ID de l'organisation courante ou null
     */
    public UUID getCurrentOrganizationId() {
        return tenantContextHolder.getCurrentTenant();
    }

    /**
     * Vérifie si l'utilisateur courant a la permission spécifiée pour une ressource spécifique.
     * Cette méthode combine la vérification de permission avec la vérification de propriété.
     *
     * @param resourceId   ID de la ressource
     * @param resourceType Type de la ressource
     * @param action       Action à effectuer sur la ressource
     * @return true si l'utilisateur a la permission ou est propriétaire, false sinon
     */
    public boolean hasPermission(UUID resourceId, String resourceType, String action) {
        // Vérifier si l'utilisateur a la permission spécifiée
        String permissionName = resourceType.toUpperCase() + "_" + action.toUpperCase();
        if (hasPermission(permissionName)) {
            return true;
        }

        // Vérifier si l'utilisateur est propriétaire de la ressource
        return isOwner(resourceId, resourceType);
    }

    /**
     * Vérifie si l'utilisateur courant est propriétaire de la ressource spécifiée.
     * Cette méthode doit être surchargée pour chaque type de ressource.
     *
     * @param resourceId   ID de la ressource
     * @param resourceType Type de la ressource
     * @return true si l'utilisateur est propriétaire, false sinon
     */
    public boolean isOwner(UUID resourceId, String resourceType) {
        // Implémentation par défaut pour les types de ressources courants
        switch (resourceType.toUpperCase()) {
            case "USER":
                return isCurrentUser(resourceId);
            // Ajouter d'autres types de ressources au besoin
            default:
                return false;
        }
    }

    /**
     * Vérifie si l'utilisateur courant peut accéder à l'organisation spécifiée.
     *
     * @param organizationId ID de l'organisation
     * @return true si l'utilisateur peut accéder à l'organisation, false sinon
     */
    public boolean canAccessOrganization(UUID organizationId) {
        // Les administrateurs peuvent accéder à toutes les organisations
        if (hasRole("ADMIN")) {
            return true;
        }

        // Les autres utilisateurs ne peuvent accéder qu'à leur propre organisation
        UUID currentTenant = tenantContextHolder.getCurrentTenant();
        return currentTenant != null && currentTenant.equals(organizationId);
    }

    /**
     * Vérifie si l'utilisateur courant peut gérer le rôle spécifié.
     *
     * @param roleId ID du rôle
     * @return true si l'utilisateur peut gérer le rôle, false sinon
     */
    public boolean canManageRole(UUID roleId) {
        // Seuls les administrateurs peuvent gérer les rôles
        return hasRole("ADMIN");
    }

    /**
     * Vérifie si l'utilisateur courant peut gérer les utilisateurs de l'organisation spécifiée.
     *
     * @param organizationId ID de l'organisation
     * @return true si l'utilisateur peut gérer les utilisateurs, false sinon
     */
    public boolean canManageOrganizationUsers(UUID organizationId) {
        // Vérifier si l'utilisateur peut accéder à l'organisation
        if (!canAccessOrganization(organizationId)) {
            return false;
        }

        // Vérifier si l'utilisateur a la permission de gérer les utilisateurs
        return hasAnyPermission("USER_CREATE", "USER_UPDATE", "USER_DELETE", "USER_ACTIVATE", "USER_DEACTIVATE");
    }

    /**
     * Vérifie si l'organisation courante est l'organisation spécifiée.
     *
     * @param organizationId ID de l'organisation à vérifier
     * @return true si l'organisation courante est l'organisation spécifiée, false sinon
     */
    public boolean isCurrentOrganization(UUID organizationId) {
        UUID currentOrganizationId = tenantContextHolder.getCurrentTenant();
        return currentOrganizationId != null && currentOrganizationId.equals(organizationId);
    }
}
