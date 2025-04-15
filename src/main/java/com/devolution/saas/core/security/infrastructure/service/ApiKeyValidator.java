package com.devolution.saas.core.security.infrastructure.service;

import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service pour la validation des clés API.
 * Ce service est séparé de ApiKeyService pour éviter les dépendances circulaires.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyValidator {

    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Valide une clé API.
     *
     * @param apiKeyValue Valeur de la clé API
     * @return Clé API si valide, sinon Optional vide
     */
    @Transactional
    public Optional<ApiKey> validateApiKey(String apiKeyValue) {
        if (apiKeyValue == null || apiKeyValue.length() <= 8) {
            return Optional.empty();
        }

        String prefix = apiKeyValue.substring(0, 8);
        String key = apiKeyValue;

        Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByPrefixAndStatus(prefix, null);
        if (apiKeyOpt.isEmpty()) {
            return Optional.empty();
        }

        ApiKey apiKey = apiKeyOpt.get();

        // Vérification du statut et de l'expiration
        if (!apiKey.isActive()) {
            return Optional.empty();
        }

        // Vérification de la clé
        if (!passwordEncoder.matches(key, apiKey.getKeyHash())) {
            return Optional.empty();
        }

        // Mise à jour de la date de dernière utilisation
        apiKey.updateLastUsed();
        apiKeyRepository.save(apiKey);

        return Optional.of(apiKey);
    }

    /**
     * Vérifie si une adresse IP est autorisée pour une clé API.
     *
     * @param apiKey   Clé API
     * @param clientIp Adresse IP du client
     * @return true si l'adresse IP est autorisée, false sinon
     */
    public boolean isIpAllowed(ApiKey apiKey, String clientIp) {
        // Si aucune restriction d'IP n'est définie, toutes les IPs sont autorisées
        if (apiKey.getAllowedIps() == null || apiKey.getAllowedIps().isEmpty()) {
            return true;
        }

        return apiKey.getAllowedIps().contains(clientIp);
    }
}
