package com.devolution.saas.core.security.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Classe qui gère le contexte du tenant (organisation) pour la requête courante.
 * Utilise un ThreadLocal pour stocker l'ID de l'organisation.
 */
@Component
@Slf4j
public class TenantContextHolder {

    private static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * Récupère l'ID de l'organisation courante du contexte.
     *
     * @return ID de l'organisation ou null
     */
    public UUID getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Définit l'ID de l'organisation courante dans le contexte.
     *
     * @param tenantId ID de l'organisation
     */
    public void setCurrentTenant(UUID tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Vérifie si un tenant est défini dans le contexte.
     *
     * @return true si un tenant est défini, false sinon
     */
    public boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }

    /**
     * Efface le tenant du contexte.
     */
    public void clear() {
        CURRENT_TENANT.remove();
    }
}
