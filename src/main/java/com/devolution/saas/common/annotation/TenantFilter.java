package com.devolution.saas.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation utilisée pour marquer les méthodes qui doivent être filtrées par tenant.
 * <p>
 * Cette annotation est utilisée par l'aspect de filtrage pour ajouter automatiquement
 * des critères de filtrage basés sur le contexte du tenant.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantFilter {

    /**
     * Indique si le filtrage doit inclure les descendants de l'organisation courante.
     * Si true, les données des organisations descendantes seront également incluses.
     * Si false, seules les données de l'organisation courante seront incluses.
     */
    boolean includeDescendants() default false;

    /**
     * Nom de l'attribut d'entité qui contient l'ID de l'organisation.
     * Par défaut, "organizationId".
     */
    String organizationIdField() default "organizationId";
}
