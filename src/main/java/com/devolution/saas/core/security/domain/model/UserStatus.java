package com.devolution.saas.core.security.domain.model;

/**
 * Énumération des statuts possibles pour un utilisateur.
 */
public enum UserStatus {
    /**
     * Utilisateur actif et opérationnel.
     */
    ACTIVE,

    /**
     * Utilisateur inactif (désactivé).
     */
    INACTIVE,

    /**
     * Utilisateur suspendu temporairement.
     */
    SUSPENDED,

    /**
     * Utilisateur en attente de confirmation (par exemple, après inscription).
     */
    PENDING,

    /**
     * Utilisateur archivé (ne peut plus être utilisé).
     */
    ARCHIVED
}
