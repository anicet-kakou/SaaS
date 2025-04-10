package com.devolution.saas.core.security.infrastructure.service;

import java.util.UUID;

/**
 * Mock implementation of TenantContextHolder for testing.
 * This class provides a simplified implementation that can be used in tests
 * without requiring the full Spring context.
 */
public class MockTenantContextHolder extends TenantContextHolder {

    private UUID currentTenant;

    /**
     * Récupère l'ID de l'organisation courante du contexte.
     *
     * @return ID de l'organisation ou null
     */
    @Override
    public UUID getCurrentTenant() {
        return currentTenant;
    }

    /**
     * Définit l'ID de l'organisation courante dans le contexte.
     *
     * @param tenantId ID de l'organisation
     */
    @Override
    public void setCurrentTenant(UUID tenantId) {
        this.currentTenant = tenantId;
    }

    /**
     * Vérifie si un tenant est défini dans le contexte.
     *
     * @return true si un tenant est défini, false sinon
     */
    @Override
    public boolean hasTenant() {
        return currentTenant != null;
    }

    /**
     * Efface le tenant du contexte.
     */
    @Override
    public void clear() {
        this.currentTenant = null;
    }
}
