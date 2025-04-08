package com.devolution.saas.core.security.infrastructure.filter;

import com.devolution.saas.core.security.infrastructure.config.JwtTokenProvider;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtre d'authentification JWT.
 * Vérifie la présence et la validité d'un jeton JWT dans les requêtes HTTP.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TenantContextHolder tenantContextHolder;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = resolveToken(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Authentication auth = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Définir le contexte du tenant
                UUID organizationId = jwtTokenProvider.getOrganizationId(jwt);
                if (organizationId != null) {
                    tenantContextHolder.setCurrentTenant(organizationId);
                    log.debug("Tenant défini dans le contexte: {}", organizationId);
                }
            }
        } catch (Exception e) {
            log.error("Impossible de définir l'authentification utilisateur dans le contexte de sécurité", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le jeton JWT de la requête HTTP.
     *
     * @param request Requête HTTP
     * @return Jeton JWT ou null
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
