package com.devolution.saas.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Configuration pour l'internationalisation des messages.
 */
@Configuration
public class MessageConfig {

    /**
     * Configure le LocaleResolver pour déterminer la locale à partir des en-têtes HTTP.
     *
     * @return LocaleResolver configuré
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.FRENCH); // Locale par défaut
        return localeResolver;
    }

    /**
     * Configure le ValidatorFactoryBean pour utiliser les messages internationalisés dans les validations.
     *
     * @return ValidatorFactoryBean configuré
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        // Utiliser un MessageInterpolator personnalisé qui utilise notre ModularMessageService
        bean.setMessageInterpolator(new ModularMessageInterpolator());
        return bean;
    }
}
