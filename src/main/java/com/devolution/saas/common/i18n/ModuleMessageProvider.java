package com.devolution.saas.common.i18n;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Interface pour les fournisseurs de messages spécifiques à un module.
 * Chaque module peut implémenter cette interface pour fournir ses propres messages.
 */
public interface ModuleMessageProvider {

    /**
     * Retourne le nom du module.
     *
     * @return Nom du module
     */
    String getModuleName();

    /**
     * Retourne les types de messages fournis par ce module (ex: "errors", "validation").
     *
     * @return Liste des types de messages
     */
    List<String> getMessageTypes();

    /**
     * Retourne les propriétés pour un type de message et une locale donnés.
     *
     * @param messageType Type de message (ex: "errors", "validation")
     * @param locale      Locale
     * @return Propriétés contenant les messages
     */
    Properties getMessages(String messageType, Locale locale);

    /**
     * Vérifie si ce fournisseur peut gérer un code de message donné.
     *
     * @param code Code du message
     * @return true si ce fournisseur peut gérer ce code, false sinon
     */
    boolean canHandle(String code);

    /**
     * Retourne le message pour un code donné.
     *
     * @param code   Code du message
     * @param locale Locale
     * @return Message ou null si non trouvé
     */
    String getMessage(String code, Locale locale);

    /**
     * Retourne le message pour un code et des arguments donnés.
     *
     * @param code   Code du message
     * @param args   Arguments pour le message
     * @param locale Locale
     * @return Message formaté ou null si non trouvé
     */
    String getMessage(String code, Object[] args, Locale locale);
}
