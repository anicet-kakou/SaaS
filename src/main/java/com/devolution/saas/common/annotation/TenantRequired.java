package com.devolution.saas.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation utilisée pour marquer les méthodes ou classes qui nécessitent
 * un contexte d'organisation (tenant) pour fonctionner.
 * <p>
 * Cette annotation est utilisée par les aspects pour vérifier la présence
 * d'un identifiant d'organisation dans le contexte de sécurité avant
 * d'exécuter la méthode annotée.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantRequired {
}
