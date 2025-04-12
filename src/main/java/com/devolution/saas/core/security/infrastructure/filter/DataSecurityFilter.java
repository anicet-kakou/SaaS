package com.devolution.saas.core.security.infrastructure.filter;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.infrastructure.service.SecurityService;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Filtre de sécurité pour les données.
 * Ce composant fournit des méthodes pour filtrer les données en fonction du contexte de sécurité.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSecurityFilter {

    private final SecurityService securityService;
    private final TenantContextHolder tenantContextHolder;

    /**
     * Filtre une liste d'entités pour ne conserver que celles auxquelles l'utilisateur a accès.
     * Cette méthode est annotée avec @PostFilter pour être utilisée dans les expressions SpEL.
     *
     * @param entities Liste d'entités à filtrer
     * @param <T>      Type des entités
     * @return Liste filtrée d'entités
     */
    @PostFilter("hasRole('ADMIN') or filterObject.organizationId == null or filterObject.organizationId == authentication.principal.organizationId")
    public <T extends TenantAwareEntity> List<T> filterByTenant(List<T> entities) {
        return entities;
    }

    /**
     * Filtre une liste d'entités en fonction des permissions de l'utilisateur.
     * Cette méthode vérifie si l'utilisateur a la permission de lire l'entité ou s'il en est le propriétaire.
     *
     * @param entities     Liste d'entités à filtrer
     * @param resourceType Type de ressource
     * @param <T>          Type des entités
     * @return Liste filtrée d'entités
     */
    public <T extends TenantAwareEntity> List<T> filterByPermission(List<T> entities, String resourceType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return List.of();
        }

        // Les administrateurs ont accès à toutes les entités
        if (securityService.hasRole("ADMIN")) {
            return entities;
        }

        // Récupérer l'ID de l'organisation courante
        UUID currentTenant = tenantContextHolder.getCurrentTenant();
        if (currentTenant == null) {
            return List.of();
        }

        // Vérifier si l'utilisateur a la permission de lire ce type de ressource
        boolean hasReadPermission = securityService.hasPermission(resourceType + "_READ");

        // Filtrer les entités
        return entities.stream()
                .filter(entity -> {
                    // Vérifier si l'entité appartient à l'organisation de l'utilisateur
                    if (entity.getOrganizationId() == null || !entity.getOrganizationId().equals(currentTenant)) {
                        return false;
                    }

                    // Si l'utilisateur a la permission de lecture, il peut accéder à l'entité
                    if (hasReadPermission) {
                        return true;
                    }

                    // Sinon, vérifier si l'utilisateur est propriétaire de l'entité
                    return isOwner(entity, authentication);
                })
                .collect(Collectors.toList());
    }

    /**
     * Vérifie si l'utilisateur est propriétaire de l'entité.
     * Cette méthode doit être surchargée pour chaque type d'entité spécifique.
     *
     * @param entity         Entité à vérifier
     * @param authentication Authentification de l'utilisateur
     * @param <T>            Type de l'entité
     * @return true si l'utilisateur est propriétaire, false sinon
     */
    protected <T> boolean isOwner(T entity, Authentication authentication) {
        // Implémentation par défaut
        if (authentication.getPrincipal() instanceof User user) {
            if (entity instanceof User entityUser) {
                return entityUser.getId().equals(user.getId());
            }
        }
        return false;
    }

    /**
     * Filtre une collection d'entités pour ne conserver que celles auxquelles l'utilisateur a accès.
     *
     * @param entities     Collection d'entités à filtrer
     * @param resourceType Type de ressource
     * @param <T>          Type des entités
     * @return Collection filtrée d'entités
     */
    public <T extends TenantAwareEntity> Collection<T> filterCollection(Collection<T> entities, String resourceType) {
        if (entities instanceof List) {
            return filterByPermission((List<T>) entities, resourceType);
        } else {
            return filterByPermission(entities.stream().toList(), resourceType);
        }
    }
}
