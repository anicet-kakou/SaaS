package com.devolution.saas.common.aspect;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.util.JsonUtils;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

/**
 * Aspect pour l'annotation @Auditable.
 * Enregistre les appels de méthode, les paramètres d'entrée et les résultats dans les journaux d'audit.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditableAspect {

    private final TenantContextHolder tenantContextHolder;

    /**
     * Enregistre les appels de méthode, les paramètres d'entrée et les résultats dans les journaux d'audit.
     *
     * @param joinPoint Point de jonction
     * @param auditable Annotation @Auditable
     * @return Résultat de la méthode
     * @throws Throwable Si une erreur survient
     */
    @Around("@annotation(auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        String action = auditable.action().isEmpty() ? methodName : auditable.action();

        // Récupération des informations de l'utilisateur courant
        String username = "anonymous";
        UUID userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            username = user.getUsername();
            userId = user.getId();
        }

        // Récupération de l'ID de l'organisation
        UUID organizationId = tenantContextHolder.getCurrentTenant();

        // Configuration du MDC pour le logging
        MDC.put("username", username);
        MDC.put("userId", userId != null ? userId.toString() : "");
        MDC.put("organizationId", organizationId != null ? organizationId.toString() : "");
        MDC.put("action", action);

        // Journalisation du début de l'appel
        if (auditable.logParams()) {
            log.info("Début de l'action {} par l'utilisateur {} ({}). Paramètres: {}",
                    action, username, userId, formatParameters(joinPoint.getArgs()));
        } else {
            log.info("Début de l'action {} par l'utilisateur {} ({})", action, username, userId);
        }

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // Exécution de la méthode
            result = joinPoint.proceed();

            // Journalisation du résultat
            long executionTime = System.currentTimeMillis() - startTime;
            if (auditable.logResult()) {
                log.info("Fin de l'action {} par l'utilisateur {} ({}). Temps d'exécution: {} ms. Résultat: {}",
                        action, username, userId, executionTime, formatResult(result));
            } else {
                log.info("Fin de l'action {} par l'utilisateur {} ({}). Temps d'exécution: {} ms",
                        action, username, userId, executionTime);
            }

            return result;
        } catch (Throwable e) {
            // Journalisation de l'erreur
            log.error("Erreur lors de l'action {} par l'utilisateur {} ({}). Erreur: {}",
                    action, username, userId, e.getMessage(), e);
            throw e;
        } finally {
            // Nettoyage du MDC
            MDC.remove("username");
            MDC.remove("userId");
            MDC.remove("organizationId");
            MDC.remove("action");
        }
    }

    /**
     * Formate les paramètres d'entrée pour la journalisation.
     *
     * @param args Paramètres d'entrée
     * @return Chaîne formatée
     */
    private String formatParameters(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        try {
            return JsonUtils.toJson(Arrays.asList(args));
        } catch (Exception e) {
            log.warn("Impossible de formater les paramètres: {}", e.getMessage());
            return Arrays.toString(args);
        }
    }

    /**
     * Formate le résultat pour la journalisation.
     *
     * @param result Résultat
     * @return Chaîne formatée
     */
    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }

        try {
            return JsonUtils.toJson(result);
        } catch (Exception e) {
            log.warn("Impossible de formater le résultat: {}", e.getMessage());
            return result.toString();
        }
    }
}
