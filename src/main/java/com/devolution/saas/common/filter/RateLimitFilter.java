package com.devolution.saas.common.filter;

import com.devolution.saas.common.util.HttpRequestUtils;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.infrastructure.service.ApiKeyValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filtre pour la limitation du taux de requêtes (rate limiting).
 * Ce filtre limite le nombre de requêtes par minute pour chaque client.
 */
@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    // Limite par défaut pour les requêtes par IP (60 requêtes par minute)
    private static final int DEFAULT_IP_LIMIT = 60;
    // Limite par défaut pour les requêtes par API Key (300 requêtes par minute)
    private static final int DEFAULT_API_KEY_LIMIT = 300;
    private final ApiKeyValidator apiKeyValidator;
    private final ObjectMapper objectMapper;
    // Cache des buckets par adresse IP
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    // Cache des buckets par API Key
    private final Map<String, Bucket> apiKeyBuckets = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Vérification si la requête est une requête API
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Récupération de l'API Key
        String apiKeyValue = HttpRequestUtils.resolveApiKey(request);

        // Si une API Key est présente, utiliser la limite de l'API Key
        if (StringUtils.hasText(apiKeyValue)) {
            Optional<ApiKey> apiKeyOpt = apiKeyValidator.validateApiKey(apiKeyValue);
            if (apiKeyOpt.isPresent()) {
                ApiKey apiKey = apiKeyOpt.get();

                // Utilisation de la limite spécifique de l'API Key ou de la limite par défaut
                int rateLimit = apiKey.getRateLimit() != null ? apiKey.getRateLimit() : DEFAULT_API_KEY_LIMIT;

                // Récupération ou création du bucket pour cette API Key
                Bucket bucket = apiKeyBuckets.computeIfAbsent(apiKeyValue, k -> createBucket(rateLimit));

                if (bucket.tryConsume(1)) {
                    // La requête est autorisée
                    filterChain.doFilter(request, response);
                } else {
                    // La requête est rejetée (trop de requêtes)
                    sendRateLimitExceededResponse(response, "API Key rate limit exceeded");
                }

                return;
            }
        }

        // Si aucune API Key n'est présente ou si elle est invalide, utiliser la limite par IP
        String clientIp = HttpRequestUtils.getClientIp(request);

        // Récupération ou création du bucket pour cette adresse IP
        Bucket bucket = ipBuckets.computeIfAbsent(clientIp, k -> createBucket(DEFAULT_IP_LIMIT));

        if (bucket.tryConsume(1)) {
            // La requête est autorisée
            filterChain.doFilter(request, response);
        } else {
            // La requête est rejetée (trop de requêtes)
            sendRateLimitExceededResponse(response, "IP rate limit exceeded");
        }
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
     * Envoie une réponse d'erreur lorsque la limite de taux est dépassée.
     *
     * @param response Réponse HTTP
     * @param message  Message d'erreur
     * @throws IOException Si une erreur d'E/S se produit
     */
    private void sendRateLimitExceededResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorResponse = Map.of(
                "status", HttpStatus.TOO_MANY_REQUESTS.value(),
                "error", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),
                "message", message
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return HttpRequestUtils.shouldNotFilter(request);
    }
}
