package com.devolution.saas.core.security.infrastructure.config;

import com.devolution.saas.core.security.domain.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Fournisseur de jetons JWT pour l'authentification.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInSeconds;

    @Value("${devolution.saas.security.jwt.issuer:DEVOLUTION SaaS}")
    private String issuer;

    private Key key;

    /**
     * Initialise la clé de signature JWT.
     */
    @PostConstruct
    protected void init() {
        // Utiliser une clé sécurisée pour HMAC-SHA256
        // La clé doit avoir au moins 256 bits (32 octets) selon la spécification JWT
        if (secretKey.length() < 32) {
            // Padding de la clé si elle est trop courte
            secretKey = String.format("%-32s", secretKey).replace(' ', 'x');
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Crée un jeton JWT pour un utilisateur.
     *
     * @param user Utilisateur
     * @return Jeton JWT
     */
    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName())
                .collect(Collectors.toList()));

        if (user.getOrganizationId() != null) {
            claims.put("organizationId", user.getOrganizationId().toString());
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .setIssuer(issuer)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valide un jeton JWT.
     *
     * @param token Jeton JWT
     * @return true si le jeton est valide, false sinon
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token invalide: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrait le nom d'utilisateur d'un jeton JWT.
     *
     * @param token Jeton JWT
     * @return Nom d'utilisateur
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extrait l'ID de l'organisation d'un jeton JWT.
     *
     * @param token Jeton JWT
     * @return ID de l'organisation ou null
     */
    public UUID getOrganizationId(String token) {
        String orgId = (String) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("organizationId");

        return orgId != null ? UUID.fromString(orgId) : null;
    }

    /**
     * Extrait les rôles d'un jeton JWT.
     *
     * @param token Jeton JWT
     * @return Liste des rôles
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", List.class);
    }

    /**
     * Crée un objet Authentication à partir d'un jeton JWT.
     *
     * @param token Jeton JWT
     * @return Objet Authentication
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Crée un objet Authentication à partir d'un jeton JWT avec des autorités personnalisées.
     *
     * @param token Jeton JWT
     * @return Objet Authentication
     */
    public Authentication getAuthenticationWithCustomAuthorities(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));

        List<GrantedAuthority> authorities = getRoles(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
