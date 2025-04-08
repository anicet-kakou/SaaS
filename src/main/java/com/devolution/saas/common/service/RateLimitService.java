package com.devolution.saas.common.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service pour la gestion de la limitation du taux de requêtes (rate limiting).
 */
@Service
@Slf4j
public class RateLimitService {

    // Cache des buckets par clé
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Vérifie si une requête est autorisée pour une clé donnée.
     *
     * @param key   Clé d'identification (IP, API Key, etc.)
     * @param limit Limite de requêtes par minute
     * @return true si la requête est autorisée, false sinon
     */
    public boolean isAllowed(String key, int limit) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(limit));
        return bucket.tryConsume(1);
    }

    /**
     * Vérifie si une requête est autorisée pour une clé donnée avec un coût spécifique.
     *
     * @param key    Clé d'identification (IP, API Key, etc.)
     * @param limit  Limite de requêtes par minute
     * @param tokens Nombre de tokens à consommer (coût de la requête)
     * @return true si la requête est autorisée, false sinon
     */
    public boolean isAllowed(String key, int limit, int tokens) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(limit));
        return bucket.tryConsume(tokens);
    }

    /**
     * Crée un bucket pour la limitation du taux de requêtes.
     *
     * @param limit Limite de requêtes par minute
     * @return Bucket configuré
     */
    private Bucket createBucket(int limit) {
        Bandwidth bandwidth = Bandwidth.classic(limit, Refill.intervally(limit, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(bandwidth).build();
    }

    /**
     * Récupère le nombre de tokens restants pour une clé donnée.
     *
     * @param key   Clé d'identification (IP, API Key, etc.)
     * @param limit Limite de requêtes par minute
     * @return Nombre de tokens restants
     */
    public long getRemainingTokens(String key, int limit) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(limit));
        return bucket.getAvailableTokens();
    }

    /**
     * Réinitialise le bucket pour une clé donnée.
     *
     * @param key   Clé d'identification (IP, API Key, etc.)
     * @param limit Limite de requêtes par minute
     */
    public void resetBucket(String key, int limit) {
        buckets.put(key, createBucket(limit));
    }

    /**
     * Supprime le bucket pour une clé donnée.
     *
     * @param key Clé d'identification (IP, API Key, etc.)
     */
    public void removeBucket(String key) {
        buckets.remove(key);
    }

    /**
     * Nettoie les buckets inutilisés.
     */
    public void cleanupBuckets() {
        // Cette méthode pourrait être appelée périodiquement pour supprimer les buckets inutilisés
        log.debug("Nettoyage des buckets de rate limiting");
        // Implémentation à définir selon les besoins
    }
}
