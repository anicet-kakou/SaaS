package com.devolution.saas.common.domain.exception;

/**
 * Exception lancée lorsqu'une ressource demandée n'est pas trouvée.
 */
public class ResourceNotFoundException extends BusinessException {

    private static final String DEFAULT_CODE = "resource.not.found";
    private static final String DEFAULT_MESSAGE = "La ressource demandée n'a pas été trouvée";

    /**
     * Constructeur par défaut.
     */
    public ResourceNotFoundException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message personnalisé.
     *
     * @param message Message d'erreur personnalisé
     */
    public ResourceNotFoundException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec type de ressource et identifiant.
     *
     * @param resourceType Type de ressource (ex: "Organization", "User")
     * @param id           Identifiant de la ressource
     */
    public ResourceNotFoundException(String resourceType, Object id) {
        super(DEFAULT_CODE, resourceType + " avec l'identifiant " + id + " n'a pas été trouvé");
    }
}
