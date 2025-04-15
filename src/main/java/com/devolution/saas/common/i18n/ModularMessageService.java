package com.devolution.saas.common.i18n;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * Service de messages qui délègue la résolution des messages aux fournisseurs de messages des modules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ModularMessageService implements MessageService {

    private final List<ModuleMessageProvider> messageProviders;

    @Override
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    @Override
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, null);
    }

    @Override
    public String getMessage(String code, Object[] args, String localeStr) {
        Locale locale = localeStr != null ? Locale.forLanguageTag(localeStr) : LocaleContextHolder.getLocale();

        // Chercher le fournisseur de messages qui peut gérer ce code
        for (ModuleMessageProvider provider : messageProviders) {
            if (provider.canHandle(code)) {
                String message = provider.getMessage(code, args, locale);
                if (message != null) {
                    return message;
                }
            }
        }

        // Si aucun fournisseur ne peut gérer ce code, retourner le code lui-même
        log.debug("Message non trouvé pour le code: {}, locale: {}", code, locale);
        return code;
    }
}
