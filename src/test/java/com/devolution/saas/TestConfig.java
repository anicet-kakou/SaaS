package com.devolution.saas;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Configuration spécifique pour les tests.
 * Exclut certaines classes du contexte de test pour éviter les erreurs de dépendances non satisfaites.
 */
@Configuration
@ComponentScan(
        basePackages = "com.devolution.saas",
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = {
                                "com\\.devolution\\.saas\\.core\\.audit\\..*",
                                "com\\.devolution\\.saas\\.common\\.filter\\..*",
                                "com\\.devolution\\.saas\\.common\\.aspect\\..*",
                                "com\\.devolution\\.saas\\.core\\.security\\.infrastructure\\.config\\.ApiKeyAuthenticationFilter"
                        }
                )
        }
)
public class TestConfig {
    // Configuration spécifique pour les tests
}
