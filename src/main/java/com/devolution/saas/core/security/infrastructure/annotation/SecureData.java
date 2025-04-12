package com.devolution.saas.core.security.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour sécuriser les données retournées par une méthode.
 * Cette annotation indique que les données retournées par la méthode doivent être filtrées
 * en fonction des autorisations de l'utilisateur courant.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureData {

    /**
     * Type de ressource concernée par la méthode.
     * Si non spécifié, le type de ressource sera déduit du nom de la méthode.
     *
     * @return Type de ressource
     */
    String resourceType() default "";
}
