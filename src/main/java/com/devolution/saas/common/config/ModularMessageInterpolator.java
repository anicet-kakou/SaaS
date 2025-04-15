package com.devolution.saas.common.config;

import com.devolution.saas.common.i18n.MessageService;
import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Interpolateur de messages qui utilise notre ModularMessageService pour résoudre les messages de validation.
 */
public class ModularMessageInterpolator implements MessageInterpolator {

    private final ResourceBundleMessageInterpolator defaultInterpolator = new ResourceBundleMessageInterpolator();

    @Autowired
    private MessageService messageService;

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        // Si le messageService est disponible, l'utiliser pour résoudre le message
        if (messageService != null && messageTemplate.startsWith("{") && messageTemplate.endsWith("}")) {
            String code = messageTemplate.substring(1, messageTemplate.length() - 1);
            String message = messageService.getMessage(code, null, locale.toString());

            // Si le message a été résolu, l'utiliser
            if (!message.equals(code)) {
                return message;
            }
        }

        // Sinon, utiliser l'interpolateur par défaut
        return defaultInterpolator.interpolate(messageTemplate, context, locale);
    }
}
