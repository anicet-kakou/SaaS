package com.devolution.saas.common.config;

import com.devolution.saas.common.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuration pour la limitation du taux de requêtes (rate limiting).
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class RateLimitConfig {

    private final RateLimitService rateLimitService;

    /**
     * Tâche planifiée pour nettoyer les buckets inutilisés.
     * S'exécute toutes les heures.
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanupBuckets() {
        log.debug("Exécution de la tâche planifiée de nettoyage des buckets de rate limiting");
        rateLimitService.cleanupBuckets();
    }
}
