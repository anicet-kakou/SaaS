package com.devolution.saas.core.audit.domain.model;

/**
 * Énumération des statuts possibles pour un log d'audit.
 */
public enum AuditStatus {
    /**
     * L'action a réussi.
     */
    SUCCESS,

    /**
     * L'action a échoué.
     */
    FAILURE
}
