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
import java.time.Instant;
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

    @Value("${devolution.saas.security.jwt.audience:https://api.devolution-saas.com}")
    private String audience;

    @Value("${devolution.saas.security.jwt.clock-skew-seconds:30}")
    private int clockSkewSeconds;

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

        // Limiter les informations sensibles dans le token
        // Ne pas inclure l'email complet pour réduire les risques en cas de compromission du token
        // claims.put("email", user.getEmail());

        // Inclure uniquement les rôles, pas les permissions détaillées
        claims.put("roles", user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName())
                .collect(Collectors.toList()));

        if (user.getOrganizationId() != null) {
            claims.put("organizationId", user.getOrganizationId().toString());
        }

        // Générer un identifiant unique pour le token
        String tokenId = UUID.randomUUID().toString();

        Instant now = Instant.now();
        Instant validity = now.plusSeconds(validityInSeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setId(tokenId)  // jti - JWT ID
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(validity))
                .setNotBefore(Date.from(now))  // nbf - Not Before
                .setIssuer(issuer)  // iss - Issuer
                .setAudience(audience)  // aud - Audience
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
            // Utiliser un parser avec des validations plus strictes
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer(issuer)  // Vérifier l'émetteur
                    .requireAudience(audience)  // Vérifier l'audience
                    .setAllowedClockSkewSeconds(clockSkewSeconds)  // Tolérance pour les décalages d'horloge
                    .build();

            // Analyser et valider le token
            Jws<Claims> claims = parser.parseClaimsJws(token);

            // Vérifications supplémentaires
            Date now = new Date();
            Date expiration = claims.getBody().getExpiration();
            Date notBefore = claims.getBody().getNotBefore();

            // Vérifier que le token n'est pas expiré et qu'il est déjà valide (not before)
            return !expiration.before(now) && (notBefore == null || !notBefore.after(now));
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
     * Extrait la date d'expiration d'un jeton JWT.
     *
     * @param token Jeton JWT
     * @return Date d'expiration
     */
    public Date getExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
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
