package com.devolution.saas.insurance.nonlife.auto.application.exception;

import com.devolution.saas.common.domain.exception.ResourceAlreadyExistsException;

/**
 * Exception levée lorsqu'une ressource du module auto existe déjà.
 * Cette classe étend la ResourceAlreadyExistsException commune et ajoute des fonctionnalités spécifiques au module auto.
 */
public class AutoResourceAlreadyExistsException extends ResourceAlreadyExistsException {

    private static final String DEFAULT_CODE = "insurance.auto.resource.already.exists";
    private static final String DEFAULT_MESSAGE = "La ressource du module auto existe déjà";

    /**
     * Constructeur par défaut.
     */
    public AutoResourceAlreadyExistsException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * Constructeur avec message.
     *
     * @param message Le message d'erreur
     */
    public AutoResourceAlreadyExistsException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * Constructeur avec code et message personnalisés.
     *
     * @param code    Code d'erreur personnalisé
     * @param message Message d'erreur personnalisé
     */
    public AutoResourceAlreadyExistsException(String code, String message) {
        super(code, message);
    }

    /**
     * Crée une exception pour une ressource auto qui existe déjà avec un code.
     *
     * @param resourceName Le nom de la ressource
     * @param code         Le code de la ressource
     * @return L'exception créée
     */
    public static AutoResourceAlreadyExistsException forCode(String resourceName, String code) {
        return new AutoResourceAlreadyExistsException(DEFAULT_CODE, resourceName + " existe déjà avec le code: " + code);
    }

    /**
     * Crée une exception pour une ressource auto qui existe déjà avec un identifiant.
     *
     * @param resourceName Le nom de la ressource
     * @param identifier   L'identifiant de la ressource (numéro de police, numéro d'immatriculation, etc.)
     * @param value        La valeur de l'identifiant
     * @return L'exception créée
     */
    public static AutoResourceAlreadyExistsException forIdentifier(String resourceName, String identifier, String value) {
        return new AutoResourceAlreadyExistsException(DEFAULT_CODE, resourceName + " existe déjà avec " + identifier + ": " + value);
    }
}
