package com.devolution.saas.core.security.infrastructure.service;

import com.devolution.saas.core.security.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
     * Vérifie si l'utilisateur courant a la permission spécifiée.
     *
     * @param permissionName Nom de la permission à vérifier
     * @return true si l'utilisateur courant a la permission spécifiée, false sinon
     */
    public boolean hasPermission(String permissionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(permissionName));
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
