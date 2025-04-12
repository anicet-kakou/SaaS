package com.devolution.saas.core.security.infrastructure.aspect;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import com.devolution.saas.core.security.infrastructure.annotation.SecureData;
import com.devolution.saas.core.security.infrastructure.filter.DataSecurityFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Aspect pour appliquer automatiquement les filtres de sécurité aux données.
 * Cet aspect intercepte les méthodes annotées avec @SecureData et applique les filtres de sécurité appropriés.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSecurityAspect {

    private final DataSecurityFilter dataSecurityFilter;

    /**
     * Intercepte les méthodes annotées avec @SecureData et applique les filtres de sécurité appropriés.
     *
     * @param joinPoint  Point de jonction
     * @param secureData Annotation @SecureData
     * @return Résultat filtré
     * @throws Throwable Si une erreur survient
     */
    @Around("@annotation(secureData)")
    public Object secureData(ProceedingJoinPoint joinPoint, SecureData secureData) throws Throwable {
        // Exécuter la méthode
        Object result = joinPoint.proceed();

        // Si le résultat est null, retourner null
        if (result == null) {
            return null;
        }

        // Récupérer le type de ressource
        String resourceType = secureData.resourceType();
        if (resourceType.isEmpty()) {
            // Si le type de ressource n'est pas spécifié, essayer de le déduire du nom de la méthode
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            resourceType = deduceResourceTypeFromMethod(method);
        }

        // Appliquer les filtres de sécurité en fonction du type de résultat
        if (result instanceof List<?> list) {
            return filterList(list, resourceType);
        } else if (result instanceof Collection<?> collection) {
            return filterCollection(collection, resourceType);
        } else if (result instanceof TenantAwareEntity entity) {
            return filterEntity(entity, resourceType);
        }

        // Si le type de résultat n'est pas pris en charge, retourner le résultat tel quel
        return result;
    }

    /**
     * Déduit le type de ressource à partir du nom de la méthode.
     *
     * @param method Méthode
     * @return Type de ressource
     */
    private String deduceResourceTypeFromMethod(Method method) {
        String methodName = method.getName();

        // Essayer de déduire le type de ressource à partir du nom de la méthode
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return methodName.substring(3).toUpperCase();
        } else if (methodName.startsWith("list") && methodName.length() > 4) {
            String resourceName = methodName.substring(4);
            // Enlever le "s" final si présent
            if (resourceName.endsWith("s") && resourceName.length() > 1) {
                resourceName = resourceName.substring(0, resourceName.length() - 1);
            }
            return resourceName.toUpperCase();
        } else if (methodName.startsWith("find") && methodName.length() > 4) {
            return methodName.substring(4).toUpperCase();
        }

        // Si impossible de déduire, utiliser un type générique
        return "ENTITY";
    }

    /**
     * Filtre une liste d'entités.
     *
     * @param list         Liste à filtrer
     * @param resourceType Type de ressource
     * @param <T>          Type des entités
     * @return Liste filtrée
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> filterList(List<?> list, String resourceType) {
        if (list.isEmpty()) {
            return (List<T>) list;
        }

        Object first = list.get(0);
        if (first instanceof TenantAwareEntity) {
            return (List<T>) dataSecurityFilter.filterByPermission((List<TenantAwareEntity>) list, resourceType);
        }

        return (List<T>) list;
    }

    /**
     * Filtre une collection d'entités.
     *
     * @param collection   Collection à filtrer
     * @param resourceType Type de ressource
     * @param <T>          Type des entités
     * @return Collection filtrée
     */
    @SuppressWarnings("unchecked")
    private <T> Collection<T> filterCollection(Collection<?> collection, String resourceType) {
        if (collection.isEmpty()) {
            return (Collection<T>) collection;
        }

        Object first = collection.iterator().next();
        if (first instanceof TenantAwareEntity) {
            return (Collection<T>) dataSecurityFilter.filterCollection((Collection<TenantAwareEntity>) collection, resourceType);
        }

        return (Collection<T>) collection;
    }

    /**
     * Filtre une entité.
     *
     * @param entity       Entité à filtrer
     * @param resourceType Type de ressource
     * @param <T>          Type de l'entité
     * @return Entité filtrée ou null si l'accès est refusé
     */
    @SuppressWarnings("unchecked")
    private <T> T filterEntity(TenantAwareEntity entity, String resourceType) {
        List<TenantAwareEntity> filtered = dataSecurityFilter.filterByPermission(List.of(entity), resourceType);
        if (filtered.isEmpty()) {
            return null;
        }
        return (T) filtered.get(0);
    }
}
