package com.devolution.saas.core.security.infrastructure.specification;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.infrastructure.service.SecurityService;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Spécifications JPA sécurisées pour filtrer les données au niveau de la requête.
 * Ce composant fournit des méthodes pour créer des spécifications JPA qui intègrent les contraintes de sécurité.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SecureSpecifications {

    private final SecurityService securityService;
    private final TenantContextHolder tenantContextHolder;

    /**
     * Crée une spécification qui limite les résultats aux entités accessibles par l'utilisateur courant.
     * Cette méthode tient compte du contexte de sécurité et du contexte de tenant.
     *
     * @param entityClass Classe de l'entité
     * @param <T>         Type de l'entité
     * @return Spécification JPA
     */
    public <T extends TenantAwareEntity> Specification<T> forCurrentUser(Class<T> entityClass) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return (root, query, cb) -> cb.equal(cb.literal(1), 0); // Toujours faux
        }

        // Les administrateurs ont accès à toutes les entités
        if (securityService.hasRole("ADMIN")) {
            return null; // Pas de restriction
        }

        // Récupérer l'ID de l'organisation courante
        UUID currentTenant = tenantContextHolder.getCurrentTenant();
        if (currentTenant == null) {
            return (root, query, cb) -> cb.equal(cb.literal(1), 0); // Toujours faux
        }

        // Créer une spécification qui limite les résultats aux entités de l'organisation courante
        return (root, query, cb) -> cb.equal(root.get("organizationId"), currentTenant);
    }

    /**
     * Crée une spécification qui limite les résultats aux entités accessibles par l'utilisateur courant,
     * en tenant compte des permissions spécifiques.
     *
     * @param entityClass  Classe de l'entité
     * @param resourceType Type de ressource
     * @param <T>          Type de l'entité
     * @return Spécification JPA
     */
    public <T extends TenantAwareEntity> Specification<T> withPermission(Class<T> entityClass, String resourceType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return (root, query, cb) -> cb.equal(cb.literal(1), 0); // Toujours faux
        }

        // Les administrateurs ont accès à toutes les entités
        if (securityService.hasRole("ADMIN")) {
            return null; // Pas de restriction
        }

        // Récupérer l'ID de l'organisation courante
        UUID currentTenant = tenantContextHolder.getCurrentTenant();
        if (currentTenant == null) {
            return (root, query, cb) -> cb.equal(cb.literal(1), 0); // Toujours faux
        }

        // Vérifier si l'utilisateur a la permission de lire ce type de ressource
        boolean hasReadPermission = securityService.hasPermission(resourceType + "_READ");

        // Si l'utilisateur n'a pas la permission de lecture, limiter aux entités dont il est propriétaire
        if (!hasReadPermission) {
            if (authentication.getPrincipal() instanceof User user) {
                return (root, query, cb) -> cb.and(
                        cb.equal(root.get("organizationId"), currentTenant),
                        getOwnershipPredicate(root, query, cb, entityClass, user)
                );
            } else {
                return (root, query, cb) -> cb.equal(cb.literal(1), 0); // Toujours faux
            }
        }

        // Sinon, limiter aux entités de l'organisation courante
        return (root, query, cb) -> cb.equal(root.get("organizationId"), currentTenant);
    }

    /**
     * Crée un prédicat qui vérifie si l'utilisateur est propriétaire de l'entité.
     * Cette méthode doit être surchargée pour chaque type d'entité spécifique.
     *
     * @param root        Racine de la requête
     * @param query       Requête
     * @param cb          Constructeur de critères
     * @param entityClass Classe de l'entité
     * @param user        Utilisateur courant
     * @param <T>         Type de l'entité
     * @return Prédicat
     */
    protected <T> Predicate getOwnershipPredicate(
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            Class<T> entityClass,
            User user) {
        // Implémentation par défaut pour User
        if (entityClass.equals(User.class)) {
            return cb.equal(root.get("id"), user.getId());
        }

        // Pour les autres entités, vérifier si elles ont un champ createdBy ou userId
        try {
            if (root.get("createdBy") != null) {
                return cb.equal(root.get("createdBy"), user.getId());
            } else if (root.get("userId") != null) {
                return cb.equal(root.get("userId"), user.getId());
            }
        } catch (IllegalArgumentException e) {
            // Le champ n'existe pas
        }

        // Par défaut, retourner un prédicat toujours faux
        return cb.equal(cb.literal(1), 0);
    }
}
