package com.devolution.saas.core.security.infrastructure.filter;

import com.devolution.saas.common.util.HttpRequestUtils;
import com.devolution.saas.core.security.domain.model.ApiKey;
import com.devolution.saas.core.security.infrastructure.service.ApiKeyValidator;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * Filtre d'authentification par clé API.
 * Vérifie la présence et la validité d'une clé API dans les requêtes HTTP.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyValidator apiKeyValidator;
    private final TenantContextHolder tenantContextHolder;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String apiKey = resolveApiKey(request);
            if (StringUtils.hasText(apiKey)) {
                Optional<ApiKey> apiKeyOpt = apiKeyValidator.validateApiKey(apiKey);
                if (apiKeyOpt.isPresent()) {
                    ApiKey validApiKey = apiKeyOpt.get();

                    // Vérification de l'adresse IP si des restrictions sont définies
                    String clientIp = getClientIp(request);
                    if (!apiKeyValidator.isIpAllowed(validApiKey, clientIp)) {
                        log.warn("Tentative d'utilisation d'une clé API depuis une adresse IP non autorisée: {}", clientIp);
                        filterChain.doFilter(request, response);
                        return;
                    }

                    // Création de l'authentification
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            "api-key-" + validApiKey.getId(),
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_API"))
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // Définir le contexte du tenant
                    tenantContextHolder.setCurrentTenant(validApiKey.getOrganizationId());
                    log.debug("Authentification par clé API réussie pour l'organisation: {}", validApiKey.getOrganizationId());
                }
            }
        } catch (Exception e) {
            log.error("Impossible de définir l'authentification par clé API dans le contexte de sécurité", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrait la clé API de la requête HTTP.
     *
     * @param request Requête HTTP
     * @return Clé API ou null
     */
    private String resolveApiKey(HttpServletRequest request) {
        return HttpRequestUtils.resolveApiKey(request);
    }

    /**
     * Récupère l'adresse IP du client.
     *
     * @param request Requête HTTP
     * @return Adresse IP du client
     */
    private String getClientIp(HttpServletRequest request) {
        return HttpRequestUtils.getClientIp(request);
    }
}
