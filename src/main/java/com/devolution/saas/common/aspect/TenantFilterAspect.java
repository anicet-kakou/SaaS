package com.devolution.saas.common.aspect;

import com.devolution.saas.common.annotation.TenantFilter;
import com.devolution.saas.common.infrastructure.specification.TenantSpecification;
import com.devolution.saas.core.organization.application.service.OrganizationHierarchyService;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

/**
 * Aspect pour l'annotation @TenantFilter.
 * Ajoute automatiquement des critères de filtrage basés sur le contexte du tenant.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantFilterAspect {

    private final TenantContextHolder tenantContextHolder;
    private final OrganizationHierarchyService organizationHierarchyService;

    /**
     * Ajoute des critères de filtrage basés sur le contexte du tenant.
     *
     * @param joinPoint    Point de jonction
     * @param tenantFilter Annotation @TenantFilter
     * @return Résultat de la méthode
     * @throws Throwable Si une erreur survient
     */
    @Around("@annotation(tenantFilter)")
    public Object filterByTenant(ProceedingJoinPoint joinPoint, TenantFilter tenantFilter) throws Throwable {
        UUID currentTenant = tenantContextHolder.getCurrentTenant();

        // Si aucun tenant n'est défini dans le contexte, exécuter la méthode sans filtrage
        if (currentTenant == null) {
            log.debug("Aucun tenant défini dans le contexte, exécution sans filtrage");
            return joinPoint.proceed();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        log.debug("Filtrage des données par tenant pour la méthode: {}", methodName);

        // Récupérer les arguments de la méthode
        Object[] args = joinPoint.getArgs();

        // Vérifier si la méthode a un paramètre de type Specification
        int specificationIndex = findSpecificationParameterIndex(signature.getParameterTypes());

        if (specificationIndex >= 0 && args.length > specificationIndex) {
            // Ajouter le critère de filtrage à la spécification existante
            args[specificationIndex] = addTenantFilter(
                    (Specification<?>) args[specificationIndex],
                    currentTenant,
                    tenantFilter.includeDescendants(),
                    tenantFilter.organizationIdField()
            );

            log.debug("Critère de filtrage par tenant ajouté à la spécification existante");
        } else {
            // Créer une nouvelle spécification avec le critère de filtrage
            // Note: Ceci est une simplification, une implémentation plus complète
            // nécessiterait de gérer différents types de méthodes de repository
            log.debug("Aucun paramètre de type Specification trouvé, impossible d'ajouter le critère de filtrage");
        }

        // Exécuter la méthode avec les arguments modifiés
        return joinPoint.proceed(args);
    }

    /**
     * Trouve l'index du paramètre de type Specification dans un tableau de types de paramètres.
     *
     * @param parameterTypes Types de paramètres
     * @return Index du paramètre de type Specification ou -1 si aucun n'est trouvé
     */
    private int findSpecificationParameterIndex(Class<?>[] parameterTypes) {
        for (int i = 0; i < parameterTypes.length; i++) {
            if (Specification.class.isAssignableFrom(parameterTypes[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Ajoute un critère de filtrage par tenant à une spécification existante.
     *
     * @param specification       Spécification existante
     * @param organizationId      ID de l'organisation
     * @param includeDescendants  Indique si les descendants doivent être inclus
     * @param organizationIdField Nom de l'attribut d'entité qui contient l'ID de l'organisation
     * @param <T>                 Type de l'entité
     * @return Spécification avec le critère de filtrage ajouté
     */
    @SuppressWarnings("unchecked")
    private <T> Specification<T> addTenantFilter(
            Specification<T> specification,
            UUID organizationId,
            boolean includeDescendants,
            String organizationIdField) {

        Specification<T> tenantSpec;

        if (includeDescendants) {
            // Récupérer tous les descendants de l'organisation courante
            Set<UUID> visibleOrganizationIds = organizationHierarchyService.getVisibleOrganizationIds(organizationId);
            tenantSpec = (Specification<T>) TenantSpecification.byVisibleOrganizations(visibleOrganizationIds, organizationIdField);
        } else {
            // Filtrer uniquement par l'organisation courante
            tenantSpec = (Specification<T>) TenantSpecification.byOrganization(organizationId, organizationIdField);
        }

        // Combiner avec la spécification existante
        return specification != null ? specification.and(tenantSpec) : tenantSpec;
    }
}
