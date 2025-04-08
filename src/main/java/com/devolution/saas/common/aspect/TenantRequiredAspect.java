package com.devolution.saas.common.aspect;

import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aspect pour l'annotation @TenantRequired.
 * Vérifie qu'un tenant (organisation) est défini dans le contexte avant d'exécuter la méthode annotée.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantRequiredAspect {

    private final TenantContextHolder tenantContextHolder;

    /**
     * Vérifie qu'un tenant est défini dans le contexte avant d'exécuter la méthode annotée.
     *
     * @param joinPoint      Point de jonction
     * @param tenantRequired Annotation @TenantRequired
     */
    @Before("@annotation(tenantRequired) || @within(tenantRequired)")
    public void checkTenantContext(JoinPoint joinPoint, TenantRequired tenantRequired) {
        log.debug("Vérification du contexte tenant pour la méthode: {}", joinPoint.getSignature().toShortString());

        if (!tenantContextHolder.hasTenant()) {
            throw new BusinessException("tenant.required", "Un contexte d'organisation est requis pour cette opération");
        }
    }
}
