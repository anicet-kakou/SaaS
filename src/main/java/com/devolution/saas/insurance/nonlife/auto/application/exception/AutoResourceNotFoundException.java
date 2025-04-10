package com.devolution.saas.insurance.nonlife.auto.application.exception;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;

/**
 * Exception levée lorsqu'une ressource du module auto n'est pas trouvée.
 * Cette classe étend la ResourceNotFoundException commune et ajoute des fonctionnalités spécifiques au module auto.
 */
public class AutoResourceNotFoundException extends ResourceNotFoundException {

    private static final String DEFAULT_CODE = "insurance.auto.resource.not.found";
    private static final String DEFAULT_MESSAGE = "La ressource du module auto n'a pas été trouvée";

    /**
     * Constructeur par défaut.
     */
    public AutoResourceNotFoundException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message.
     *
     * @param message Le message d'erreur
     */
    public AutoResourceNotFoundException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public AutoResourceNotFoundException(String code, String message) {
        super(code, message);
    }

    /**
     * Crée une exception pour une ressource auto non trouvée par ID.
     *
     * @param resourceName Le nom de la ressource
     * @param id           L'ID de la ressource
     * @return L'exception créée
     */
    public static AutoResourceNotFoundException forId(String resourceName, Object id) {
        return new AutoResourceNotFoundException(DEFAULT_CODE, resourceName + " non trouvé avec l'ID: " + id);
    }

    /**
     * Crée une exception pour une ressource auto non trouvée par code.
     *
     * @param resourceName Le nom de la ressource
     * @param code         Le code de la ressource
     * @return L'exception créée
     */
    public static AutoResourceNotFoundException forCode(String resourceName, String code) {
        return new AutoResourceNotFoundException(DEFAULT_CODE, resourceName + " non trouvé avec le code: " + code);
    }
}
