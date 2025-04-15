package com.devolution.saas.common.i18n;

/**
 * Interface pour le service de messages internationalisés.
 * Permet de récupérer des messages localisés à partir de codes.
 */
public interface MessageService {

    /**
     * Récupère un message localisé à partir d'un code.
     *
     * @param code Code du message
     * @return Message localisé ou le code si le message n'est pas trouvé
     */
    String getMessage(String code);

    /**
     * Récupère un message localisé à partir d'un code et de paramètres.
     *
     * @param code Code du message
     * @param args Paramètres à insérer dans le message
     * @return Message localisé ou le code si le message n'est pas trouvé
     */
    String getMessage(String code, Object[] args);

    /**
     * Récupère un message localisé à partir d'un code, de paramètres et d'une locale.
     *
     * @param code   Code du message
     * @param args   Paramètres à insérer dans le message
     * @param locale Locale à utiliser
     * @return Message localisé ou le code si le message n'est pas trouvé
     */
    String getMessage(String code, Object[] args, String locale);
}
