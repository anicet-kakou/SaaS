package com.devolution.saas.common.i18n;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implémentation de base pour les fournisseurs de messages spécifiques à un module.
 */
@Slf4j
public abstract class AbstractModuleMessageProvider implements ModuleMessageProvider {

    private final Map<String, Properties> propertiesCache = new ConcurrentHashMap<>();

    /**
     * Retourne le chemin de base pour les fichiers de messages.
     *
     * @return Chemin de base
     */
    protected abstract String getBasePath();

    @Override
    public Properties getMessages(String messageType, Locale locale) {
        String cacheKey = getModuleName() + "." + messageType + "." + locale;

        return propertiesCache.computeIfAbsent(cacheKey, k -> {
            Properties properties = new Properties();
            String path = getBasePath() + "/" + messageType + "/" + messageType + "_" + locale + ".properties";

            try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
                if (is != null) {
                    properties.load(is);
                } else {
                    log.debug("Fichier de messages non trouvé: {}", path);
                }
            } catch (IOException e) {
                log.error("Erreur lors du chargement du fichier de messages: {}", path, e);
            }

            return properties;
        });
    }

    @Override
    public boolean canHandle(String code) {
        // Par défaut, un module peut gérer un code s'il commence par le nom du module
        return code.startsWith(getModuleName() + ".");
    }

    @Override
    public String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) {
        // Déterminer le type de message à partir du code
        String messageType = determineMessageType(code);

        if (messageType != null) {
            Properties properties = getMessages(messageType, locale);
            String message = properties.getProperty(code);

            if (message != null) {
                return formatMessage(message, args, locale);
            }
        }

        return null;
    }

    /**
     * Détermine le type de message à partir du code.
     *
     * @param code Code du message
     * @return Type de message ou null si non déterminé
     */
    protected String determineMessageType(String code) {
        for (String type : getMessageTypes()) {
            if (code.contains("." + type + ".") || code.endsWith("." + type)) {
                return type;
            }
        }

        // Par défaut, on essaie avec "errors"
        return "errors";
    }

    /**
     * Formate un message avec des arguments.
     *
     * @param message Message à formater
     * @param args    Arguments pour le message
     * @param locale  Locale
     * @return Message formaté
     */
    protected String formatMessage(String message, Object[] args, Locale locale) {
        if (args == null || args.length == 0) {
            return message;
        }

        MessageFormat messageFormat = new MessageFormat(message, locale);
        return messageFormat.format(args);
    }
}
