package com.devolution.saas.core.security.infrastructure.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service pour gérer la liste noire des jetons JWT.
 * Permet de révoquer des jetons avant leur expiration.
 * <p>
 * Note: Cette implémentation utilise une Map en mémoire pour le stockage temporaire.
 * Dans un environnement de production, il est recommandé d'utiliser Redis ou une autre solution distribuée.
 */
@Service
@Slf4j
public class JwtBlacklistService {

    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

    @Value("${jwt.blacklist.cleanup-interval:300}") // 5 minutes par défaut
    private long cleanupIntervalSeconds;

    /**
     * Constructeur.
     */
    public JwtBlacklistService() {
        // Constructeur par défaut
    }

    /**
     * Initialise le nettoyage périodique des jetons expirés.
     * Cette méthode est appelée après l'injection des dépendances.
     */
    @PostConstruct
    public void init() {
        // Planifier le nettoyage périodique des jetons expirés
        cleanupExecutor.scheduleAtFixedRate(
                this::cleanupExpiredTokens,
                cleanupIntervalSeconds,
                cleanupIntervalSeconds,
                TimeUnit.SECONDS
        );

        log.info("Service de liste noire JWT initialisé avec un intervalle de nettoyage de {} secondes", cleanupIntervalSeconds);
    }

    /**
     * Ajoute un jeton à la liste noire.
     *
     * @param token                   Jeton JWT à révoquer
     * @param expirationTimeInSeconds Durée de validité restante du jeton en secondes
     */
    public void blacklistToken(String token, long expirationTimeInSeconds) {
        long expirationTime = System.currentTimeMillis() + (expirationTimeInSeconds * 1000);
        blacklistedTokens.put(token, expirationTime);
        log.debug("Jeton ajouté à la liste noire, expire dans {} secondes", expirationTimeInSeconds);
    }

    /**
     * Vérifie si un jeton est dans la liste noire.
     *
     * @param token Jeton JWT à vérifier
     * @return true si le jeton est dans la liste noire, false sinon
     */
    public boolean isBlacklisted(String token) {
        Long expirationTime = blacklistedTokens.get(token);
        if (expirationTime == null) {
            return false;
        }

        // Vérifier si le jeton est encore valide dans la liste noire
        if (System.currentTimeMillis() > expirationTime) {
            // Le jeton est expiré, le retirer de la liste noire
            blacklistedTokens.remove(token);
            return false;
        }

        return true;
    }

    /**
     * Nettoie les jetons expirés de la liste noire.
     */
    private void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        int initialSize = blacklistedTokens.size();

        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < now);

        int removedCount = initialSize - blacklistedTokens.size();
        if (removedCount > 0) {
            log.debug("Nettoyage de la liste noire JWT: {} jetons expirés supprimés", removedCount);
        }
    }

    /**
     * Ferme le service et libère les ressources.
     * Cette méthode est appelée avant la destruction du bean.
     */
    @PreDestroy
    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
