package com.devolution.saas.common.aspect;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.core.audit.application.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Aspect pour l'audit des méthodes annotées avec @Auditable.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    /**
     * Intercepte les méthodes annotées avec @Auditable et crée un log d'audit.
     *
     * @param joinPoint Point de jonction
     * @param auditable Annotation Auditable
     * @return Résultat de la méthode
     * @throws Throwable Si une erreur se produit
     */
    @Around("@annotation(auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String action = auditable.action();

        // Récupération des paramètres de la méthode
        Map<String, Object> params = getMethodParams(joinPoint);

        try {
            // Exécution de la méthode
            result = joinPoint.proceed();

            // Création du log d'audit si l'audit est activé
            if (auditable.enabled()) {
                String description = auditable.description().isEmpty()
                        ? "Exécution de la méthode " + methodName + " dans la classe " + className
                        : auditable.description();

                String entityType = auditable.entityType().isEmpty() ? className : auditable.entityType();
                String entityId = auditable.entityId().isEmpty() ? null : auditable.entityId();

                // Extraction de l'ID de l'entité à partir du résultat si nécessaire
                if (entityId == null && result != null && auditable.extractIdFromResult()) {
                    try {
                        Method getIdMethod = result.getClass().getMethod("getId");
                        Object id = getIdMethod.invoke(result);
                        if (id != null) {
                            entityId = id.toString();
                        }
                    } catch (Exception e) {
                        log.debug("Impossible d'extraire l'ID du résultat", e);
                    }
                }

                auditService.createAuditLog(
                        action,
                        description,
                        entityType,
                        entityId,
                        auditable.logParams() ? params : null,
                        auditable.logResult() ? result : null
                );
            }

            return result;
        } catch (Exception e) {
            // Création du log d'audit d'erreur si l'audit est activé
            if (auditable.enabled()) {
                String description = auditable.description().isEmpty()
                        ? "Erreur lors de l'exécution de la méthode " + methodName + " dans la classe " + className
                        : auditable.description() + " (erreur)";

                String entityType = auditable.entityType().isEmpty() ? className : auditable.entityType();
                String entityId = auditable.entityId().isEmpty() ? null : auditable.entityId();

                auditService.createErrorAuditLog(
                        action,
                        description,
                        entityType,
                        entityId,
                        e.getMessage(),
                        e
                );
            }

            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            log.debug("Méthode {} exécutée en {} ms", methodName, executionTime);
        }
    }

    /**
     * Intercepte les exceptions lancées par les méthodes annotées avec @Auditable.
     *
     * @param joinPoint Point de jonction
     * @param auditable Annotation Auditable
     * @param exception Exception lancée
     */
    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "exception")
    public void auditException(JoinPoint joinPoint, Auditable auditable, Exception exception) {
        if (!auditable.enabled()) {
            return;
        }

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String action = auditable.action() + "_ERROR";

        String description = auditable.description().isEmpty()
                ? "Exception lors de l'exécution de la méthode " + methodName + " dans la classe " + className
                : auditable.description() + " (exception)";

        String entityType = auditable.entityType().isEmpty() ? className : auditable.entityType();
        String entityId = auditable.entityId().isEmpty() ? null : auditable.entityId();

        // Récupération des paramètres de la méthode
        Map<String, Object> params = getMethodParams(joinPoint);

        auditService.createErrorAuditLog(
                action,
                description,
                entityType,
                entityId,
                exception.getMessage(),
                exception
        );
    }

    /**
     * Récupère les paramètres de la méthode.
     *
     * @param joinPoint Point de jonction
     * @return Map des paramètres
     */
    private Map<String, Object> getMethodParams(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            for (int i = 0; i < parameterNames.length; i++) {
                if (i < args.length) {
                    params.put(parameterNames[i], args[i]);
                }
            }
        } catch (Exception e) {
            log.debug("Impossible de récupérer les paramètres de la méthode", e);

            // Fallback: utilisation des indices comme noms de paramètres
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                params.put("arg" + i, args[i]);
            }
        }

        return params;
    }
}
