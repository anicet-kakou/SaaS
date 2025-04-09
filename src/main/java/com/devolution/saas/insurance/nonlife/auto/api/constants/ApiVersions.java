package com.devolution.saas.insurance.nonlife.auto.api.constants;

/**
 * Constantes pour les versions d'API.
 * Cette classe centralise les constantes de versionnement d'API pour assurer la cohérence.
 */
public final class ApiVersions {

    /**
     * Préfixe de base pour toutes les API.
     */
    public static final String API_PREFIX = "/api";

    /**
     * Version 1 de l'API.
     */
    public static final String V1 = "/v1";

    /**
     * Préfixe complet pour la version 1 de l'API.
     */
    public static final String API_V1 = API_PREFIX + V1;

    /**
     * Préfixe pour les API auto en version 1.
     */
    public static final String API_V1_AUTO = API_V1 + "/auto";

    /**
     * Préfixe pour les API de référence auto en version 1.
     */
    public static final String API_V1_AUTO_REFERENCE = API_V1_AUTO + "/reference";

    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private ApiVersions() {
        throw new AssertionError("Cette classe ne doit pas être instanciée");
    }
}
