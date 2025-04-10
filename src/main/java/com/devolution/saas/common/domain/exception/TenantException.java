package com.devolution.saas.common.domain.exception;

/**
 * Exception lancée lors d'erreurs liées aux tenants.
 */
public class TenantException extends BusinessException {

    private static final String DEFAULT_CODE = "tenant.error";
    private static final String DEFAULT_MESSAGE = "Erreur de tenant";

    /**
     * Constructeur par défaut.
     */
    public TenantException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message personnalisé.
     *
     * @param message Message d'erreur personnalisé
     */
    public TenantException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public TenantException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec tenant ID.
     *
     * @param tenantId ID du tenant
     */
    public TenantException(Object tenantId) {
        super(DEFAULT_CODE, "Tenant avec l'identifiant " + tenantId + " n'a pas été trouvé ou n'est pas accessible");
    }

    /**
     * Constructeur pour tenant requis.
     *
     * @return Exception pour tenant requis
     */
    public static TenantException tenantRequired() {
        return new TenantException("tenant.required", "Un tenant est requis pour cette opération");
    }

    /**
     * Constructeur pour tenant non autorisé.
     *
     * @param tenantId ID du tenant
     * @return Exception pour tenant non autorisé
     */
    public static TenantException tenantNotAuthorized(Object tenantId) {
        return new TenantException("tenant.not.authorized", "Vous n'êtes pas autorisé à accéder au tenant " + tenantId);
    }
}
