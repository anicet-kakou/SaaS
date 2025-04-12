# Améliorations de l'Implémentation JWT

Ce document détaille les problèmes identifiés dans l'implémentation JWT actuelle et propose des améliorations pour
renforcer la sécurité et la robustesse du système d'authentification.

## Problèmes Identifiés

### 1. Sécurité des Clés

- **Problème** : La clé secrète JWT est stockée en clair dans les fichiers de propriétés et potentiellement dans le
  contrôle de version.
- **Risque** : Exposition de la clé secrète, permettant la création de jetons JWT frauduleux.

### 2. Algorithme de Signature

- **Problème** : Utilisation de l'algorithme HS256 (HMAC avec SHA-256) qui utilise une clé symétrique.
- **Risque** : La même clé est utilisée pour signer et vérifier les jetons, ce qui augmente le risque si la clé est
  compromise.

### 3. Gestion des Jetons

- **Problème** : Absence de mécanisme de révocation pour les jetons JWT actifs.
- **Risque** : Impossibilité de révoquer un jeton JWT avant son expiration, même en cas de compromission.

### 4. Durée de Validité des Jetons

- **Problème** : Durée de validité potentiellement trop longue (86400 secondes / 24 heures).
- **Risque** : Fenêtre d'opportunité étendue pour l'exploitation d'un jeton volé.

### 5. Validation des Claims

- **Problème** : Validation limitée des claims JWT (uniquement expiration).
- **Risque** : Acceptation de jetons avec des claims invalides ou malveillants.

### 6. Gestion des Jetons de Rafraîchissement

- **Problème** : Les jetons de rafraîchissement sont stockés en base de données mais sans rotation.
- **Risque** : Un jeton de rafraîchissement compromis peut être utilisé indéfiniment jusqu'à son expiration.

### 7. Sécurité des Endpoints

- **Problème** : Absence de protection contre les attaques par force brute sur les endpoints d'authentification.
- **Risque** : Vulnérabilité aux tentatives d'authentification répétées.

## Améliorations Proposées

### 1. Sécurisation des Clés

#### 1.1 Utilisation de Clés Asymétriques (RS256)

Remplacer l'algorithme HS256 par RS256 (RSA avec SHA-256) qui utilise une paire de clés publique/privée :

```java
@Configuration
public class JwtConfig {
    @Value("${jwt.private-key-location}")
    private String privateKeyLocation;
    
    @Value("${jwt.public-key-location}")
    private String publicKeyLocation;
    
    @Bean
    public RSAPrivateKey jwtSigningKey() {
        try (FileInputStream fis = new FileInputStream(privateKeyLocation)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(fis, keyStorePassword.toCharArray());
            Key key = keyStore.getKey(keyAlias, keyPassword.toCharArray());
            return (RSAPrivateKey) key;
        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger la clé privée JWT", e);
        }
    }
    
    @Bean
    public RSAPublicKey jwtValidationKey() {
        try (FileInputStream fis = new FileInputStream(publicKeyLocation)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(fis, keyStorePassword.toCharArray());
            Certificate cert = keyStore.getCertificate(keyAlias);
            return (RSAPublicKey) cert.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger la clé publique JWT", e);
        }
    }
}
```

#### 1.2 Stockage Sécurisé des Clés

- Stocker les clés dans un keystore protégé par mot de passe
- Exclure les fichiers de clés du contrôle de version
- Utiliser des variables d'environnement ou un service de gestion des secrets (comme HashiCorp Vault ou AWS Secrets
  Manager) pour les mots de passe

### 2. Amélioration de la Gestion des Jetons

#### 2.1 Implémentation d'une Liste Noire de Jetons

Créer un mécanisme pour révoquer les jetons JWT avant leur expiration :

```java
@Service
public class JwtBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    
    public JwtBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    public void blacklistToken(String token, long expirationTimeInSeconds) {
        redisTemplate.opsForValue().set(token, "blacklisted", expirationTimeInSeconds, TimeUnit.SECONDS);
    }
    
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
```

#### 2.2 Réduction de la Durée de Validité des Jetons

- Réduire la durée de validité des jetons JWT à 15-30 minutes
- Implémenter un mécanisme de rafraîchissement automatique côté client

```properties
# JWT Configuration
jwt.expiration=900  # 15 minutes en secondes
jwt.refresh-expiration=86400  # 24 heures en secondes
```

#### 2.3 Amélioration de la Validation des Claims

Renforcer la validation des claims JWT :

```java
public boolean validateToken(String token) {
    try {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer)
                .requireAudience(audience)
                .setAllowedClockSkewSeconds(30)  // Tolérance de 30 secondes pour les décalages d'horloge
                .build();
        
        Claims claims = parser.parseClaimsJws(token).getBody();
        
        // Vérification supplémentaire de l'expiration
        Date now = new Date();
        return !claims.getExpiration().before(now);
    } catch (JwtException | IllegalArgumentException e) {
        log.error("JWT token invalide: {}", e.getMessage());
        return false;
    }
}
```

### 3. Amélioration de la Gestion des Jetons de Rafraîchissement

#### 3.1 Rotation des Jetons de Rafraîchissement

Implémenter la rotation des jetons de rafraîchissement pour limiter la durée d'utilisation d'un jeton compromis :

```java
@Transactional
public JwtAuthenticationResponse refreshToken(RefreshTokenCommand command) {
    // Vérification du jeton de rafraîchissement
    RefreshToken refreshToken = refreshTokenRepository.findByToken(command.getRefreshToken())
            .orElseThrow(() -> new BusinessException("auth.refresh.token.invalid", "Jeton de rafraîchissement invalide"));

    // Vérification de la validité du jeton
    if (!refreshToken.isValid()) {
        refreshTokenRepository.delete(refreshToken);
        throw new BusinessException("auth.refresh.token.expired", "Jeton de rafraîchissement expiré ou révoqué");
    }

    // Récupération de l'utilisateur
    User user = userRepository.findById(refreshToken.getUserId())
            .orElseThrow(() -> new BusinessException("auth.user.not.found", "Utilisateur non trouvé"));

    // Révocation du jeton de rafraîchissement actuel
    refreshToken.revoke();
    refreshTokenRepository.save(refreshToken);

    // Génération d'un nouveau jeton JWT
    String accessToken = jwtTokenProvider.createToken(user);

    // Génération d'un nouveau jeton de rafraîchissement
    String newRefreshToken = generateRefreshToken(user.getId());

    // Construction de la réponse
    return JwtAuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .expiresIn(jwtExpirationInSeconds)
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .organizationId(user.getOrganizationId())
            .build();
}
```

#### 3.2 Limitation du Nombre de Jetons de Rafraîchissement Actifs

Limiter le nombre de jetons de rafraîchissement actifs par utilisateur :

```java
@Transactional
public String generateRefreshToken(UUID userId) {
    // Génération d'un jeton aléatoire
    String token = UUID.randomUUID().toString();

    // Calcul de la date d'expiration
    LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(refreshTokenExpirationInSeconds);

    // Création du nouveau jeton
    RefreshToken refreshToken = new RefreshToken(token, userId, expiryDate);
    
    // Limiter le nombre de jetons actifs par utilisateur
    List<RefreshToken> activeTokens = refreshTokenRepository.findAllActiveByUserId(userId);
    if (activeTokens.size() >= maxActiveRefreshTokensPerUser) {
        // Révoquer le jeton le plus ancien
        activeTokens.stream()
                .min(Comparator.comparing(RefreshToken::getCreatedAt))
                .ifPresent(oldestToken -> {
                    oldestToken.revoke();
                    refreshTokenRepository.save(oldestToken);
                });
    }
    
    // Sauvegarde du nouveau jeton
    refreshTokenRepository.save(refreshToken);

    return token;
}
```

### 4. Sécurisation des Endpoints d'Authentification

#### 4.1 Limitation du Taux de Requêtes

Implémenter une limitation du taux de requêtes pour les endpoints d'authentification :

```java
@Component
public class AuthenticationRateLimiter {
    private final LoadingCache<String, Integer> attemptsCache;
    
    public AuthenticationRateLimiter() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }
    
    public void incrementAttempts(String key) {
        int attempts = attemptsCache.getUnchecked(key);
        attemptsCache.put(key, attempts + 1);
    }
    
    public boolean isBlocked(String key) {
        return attemptsCache.getUnchecked(key) >= 5; // Bloquer après 5 tentatives
    }
}
```

#### 4.2 Délai Progressif

Implémenter un délai progressif pour les tentatives d'authentification échouées :

```java
@Service
public class AuthenticationService implements AuthenticationUseCase {
    // ...
    
    private final AuthenticationRateLimiter rateLimiter;
    
    @Override
    public JwtAuthenticationResponse authenticate(LoginCommand command) {
        String key = command.getUsername() + "|" + getClientIp();
        
        if (rateLimiter.isBlocked(key)) {
            throw new BusinessException("auth.too.many.attempts", "Trop de tentatives d'authentification. Veuillez réessayer plus tard.");
        }
        
        try {
            // Tentative d'authentification
            // ...
            
            // Réinitialiser les tentatives en cas de succès
            rateLimiter.resetAttempts(key);
            
            return response;
        } catch (AuthenticationException e) {
            // Incrémenter les tentatives en cas d'échec
            rateLimiter.incrementAttempts(key);
            
            // Ajouter un délai progressif
            int attempts = rateLimiter.getAttempts(key);
            try {
                Thread.sleep(1000L * attempts);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            
            throw new BusinessException("auth.invalid.credentials", "Identifiants invalides");
        }
    }
    
    // ...
}
```

### 5. Amélioration de la Structure des Jetons JWT

#### 5.1 Ajout de Claims Standards

Ajouter des claims standards pour améliorer la sécurité et la traçabilité :

```java
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
    String tokenId = UUID.randomUUID().toString();

    return Jwts.builder()
            .setClaims(claims)
            .setId(tokenId)  // Identifiant unique du jeton (jti)
            .setIssuedAt(now)
            .setExpiration(validity)
            .setIssuer(issuer)
            .setAudience(audience)
            .setNotBefore(now)  // Le jeton n'est pas valide avant maintenant
            .signWith(key, SignatureAlgorithm.RS256)
            .compact();
}
```

#### 5.2 Limitation des Informations Sensibles

Limiter les informations sensibles dans les jetons JWT :

```java
public String createToken(User user) {
    Claims claims = Jwts.claims().setSubject(user.getUsername());
    claims.put("id", user.getId().toString());
    
    // Inclure uniquement les rôles, pas les permissions détaillées
    claims.put("roles", user.getRoles().stream()
            .map(role -> "ROLE_" + role.getName())
            .collect(Collectors.toList()));

    if (user.getOrganizationId() != null) {
        claims.put("organizationId", user.getOrganizationId().toString());
    }

    // Ne pas inclure d'informations sensibles comme l'adresse email complète
    // claims.put("email", user.getEmail());
    
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInSeconds * 1000);

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .setIssuer(issuer)
            .signWith(key, SignatureAlgorithm.RS256)
            .compact();
}
```

## Plan d'Implémentation

### Phase 1 : Améliorations Immédiates

1. Réduire la durée de validité des jetons JWT
2. Améliorer la validation des claims
3. Implémenter la rotation des jetons de rafraîchissement
4. Limiter les informations sensibles dans les jetons JWT

### Phase 2 : Améliorations Structurelles

1. Migrer vers l'algorithme RS256 avec des clés asymétriques
2. Implémenter le stockage sécurisé des clés
3. Mettre en place une liste noire de jetons
4. Limiter le nombre de jetons de rafraîchissement actifs par utilisateur

### Phase 3 : Améliorations de Sécurité Avancées

1. Implémenter la limitation du taux de requêtes pour les endpoints d'authentification
2. Mettre en place un délai progressif pour les tentatives d'authentification échouées
3. Ajouter des mécanismes de détection des anomalies
4. Implémenter la journalisation avancée des événements d'authentification

## Conclusion

L'implémentation de ces améliorations renforcera considérablement la sécurité du système d'authentification JWT. Les
modifications proposées suivent les meilleures pratiques de l'industrie et répondent aux vulnérabilités identifiées dans
l'implémentation actuelle.
