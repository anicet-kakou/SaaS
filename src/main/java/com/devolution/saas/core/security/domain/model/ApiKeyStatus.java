package com.devolution.saas.core.security.domain.model;

/**
 * Énumération des statuts possibles pour une clé API.
 */
public enum ApiKeyStatus {
    /**
     * La clé API est active et peut être utilisée.
     */
    ACTIVE,

    /**
     * La clé API est inactive et ne peut pas être utilisée.
     */
    INACTIVE,

    /**
     * La clé API a été révoquée et ne peut plus jamais être utilisée.
     */
    REVOKED
}
