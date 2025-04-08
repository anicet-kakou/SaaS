package com.devolution.saas.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation utilisée pour marquer les méthodes qui doivent être auditées.
 * <p>
 * Cette annotation est utilisée par les aspects pour enregistrer les appels
 * de méthode, les paramètres d'entrée et les résultats dans les journaux d'audit.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    /**
     * Action auditée (obligatoire).
     * Exemple: "CREATE_USER", "UPDATE_ROLE", etc.
     */
    String action();

    /**
     * Description détaillée de l'action auditée.
     */
    String description() default "";

    /**
     * Type d'entité concernée par l'action.
     * Si non spécifié, le nom de la classe sera utilisé.
     */
    String entityType() default "";

    /**
     * ID de l'entité concernée par l'action.
     * Si non spécifié et extractIdFromResult=true, l'ID sera extrait du résultat.
     */
    String entityId() default "";

    /**
     * Indique si l'ID de l'entité doit être extrait du résultat.
     */
    boolean extractIdFromResult() default true;

    /**
     * Indique si les paramètres d'entrée doivent être enregistrés.
     */
    boolean logParams() default true;

    /**
     * Indique si le résultat doit être enregistré.
     */
    boolean logResult() default true;

    /**
     * Indique si l'audit est activé pour cette méthode.
     */
    boolean enabled() default true;
}
