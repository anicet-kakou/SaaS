package com.devolution.saas.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration personnalisée pour WebMvc.
 * Cette classe est utilisée pour résoudre les problèmes d'ambiguïté de mapping.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configure le PathMatcher pour être plus strict sur les correspondances de chemin.
     * Cela aide à résoudre les problèmes d'ambiguïté de mapping.
     *
     * @param configurer Le configurateur de correspondance de chemin
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Désactive la correspondance de suffixe pour éviter les ambiguïtés
        configurer.setUseSuffixPatternMatch(false);

        // Désactive la correspondance de point final pour éviter les ambiguïtés
        configurer.setUseTrailingSlashMatch(false);
    }
}
