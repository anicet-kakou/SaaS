package com.devolution.saas.common.domain.exception;

/**
 * Exception lancée lorsqu'une ressource existe déjà.
 */
public class ResourceAlreadyExistsException extends BusinessException {

    private static final String DEFAULT_CODE = "resource.already.exists";
    private static final String DEFAULT_MESSAGE = "La ressource existe déjà";

    /**
     * Constructeur par défaut.
     */
    public ResourceAlreadyExistsException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message personnalisé.
     *
     * @param message Message d'erreur personnalisé
     */
    public ResourceAlreadyExistsException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public ResourceAlreadyExistsException(String code, String message) {
        super(code, message);
    }

    /**
     * Constructeur avec type de ressource et identifiant.
     *
     * @param resourceType Type de ressource (ex: "Organization", "User")
     * @param id           Identifiant de la ressource
     */
    public ResourceAlreadyExistsException(String resourceType, Object id) {
        super(DEFAULT_CODE, resourceType + " avec l'identifiant " + id + " existe déjà");
    }

    /**
     * Crée une exception pour une ressource existante avec un code.
     *
     * @param resourceType Type de ressource
     * @param code         Code de la ressource
     * @return Exception pour ressource existante avec code
     */
    public static ResourceAlreadyExistsException forCode(String resourceType, String code) {
        return new ResourceAlreadyExistsException(DEFAULT_CODE, resourceType + " avec le code " + code + " existe déjà");
    }

    /**
     * Crée une exception pour une ressource existante avec un identifiant personnalisé.
     *
     * @param resourceType Type de ressource
     * @param identifier   Nom de l'identifiant
     * @param value        Valeur de l'identifiant
     * @return Exception pour ressource existante avec identifiant personnalisé
     */
    public static ResourceAlreadyExistsException forIdentifier(String resourceType, String identifier, String value) {
        return new ResourceAlreadyExistsException(DEFAULT_CODE, resourceType + " avec " + identifier + " " + value + " existe déjà");
    }
}
