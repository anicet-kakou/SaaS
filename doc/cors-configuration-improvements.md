# Améliorations de la Configuration CORS

Ce document détaille les problèmes identifiés dans la configuration CORS actuelle et propose des améliorations pour
renforcer la sécurité tout en maintenant la fonctionnalité requise.

## Problèmes Identifiés

### 1. Configuration CORS Trop Permissive

- **Problème** : La configuration actuelle utilise `"*"` comme origine autorisée, ce qui permet à n'importe quel domaine
  d'effectuer des requêtes CORS.
- **Risque** : Vulnérabilité aux attaques de type Cross-Site Request Forgery (CSRF) et aux fuites d'informations
  sensibles.

```java
configuration.setAllowedOrigins(List.of("*"));
```

### 2. En-têtes Exposés Limités

- **Problème** : Seul l'en-tête `x-auth-token` est exposé, ce qui peut limiter certaines fonctionnalités légitimes.
- **Risque** : Impossibilité pour les clients légitimes d'accéder à certains en-têtes de réponse nécessaires.

```java
configuration.setExposedHeaders(List.of("x-auth-token"));
```

### 3. Absence de Configuration pour les Cookies

- **Problème** : La configuration actuelle ne spécifie pas si les cookies doivent être inclus dans les requêtes CORS.
- **Risque** : Comportement incohérent entre les navigateurs et potentiellement des problèmes d'authentification.

### 4. Absence de Durée de Mise en Cache

- **Problème** : La durée de mise en cache des résultats de pré-vérification CORS n'est pas spécifiée.
- **Risque** : Augmentation inutile du nombre de requêtes de pré-vérification, impactant les performances.

### 5. Configuration CORS Statique

- **Problème** : La configuration CORS est codée en dur dans la classe `SecurityConfig`.
- **Risque** : Difficulté à adapter la configuration à différents environnements (développement, test, production).

## Améliorations Proposées

### 1. Configuration des Origines Spécifiques

Remplacer l'utilisation de `"*"` par une liste d'origines spécifiques autorisées :

```java
@Value("${cors.allowed-origins}")
private List<String> allowedOrigins;

// Dans la méthode corsConfigurationSource()
configuration.setAllowedOrigins(allowedOrigins);
```

### 2. Exposition d'En-têtes Supplémentaires

Ajouter des en-têtes supplémentaires utiles pour les clients :

```java
configuration.setExposedHeaders(Arrays.asList(
    "x-auth-token",
    "X-Correlation-ID",
    "Content-Disposition",
    "X-Rate-Limit-Remaining",
    "X-Rate-Limit-Reset"
));
```

### 3. Configuration des Cookies

Configurer explicitement la gestion des cookies pour les requêtes CORS :

```java
// Pour les environnements de production
configuration.setAllowCredentials(true);
```

### 4. Mise en Cache des Résultats de Pré-vérification

Définir une durée de mise en cache pour réduire le nombre de requêtes de pré-vérification :

```java
configuration.setMaxAge(3600L); // 1 heure en secondes
```

### 5. Configuration Basée sur l'Environnement

Externaliser la configuration CORS dans les fichiers de propriétés :

```properties
# application.properties
cors.allowed-origins=https://app.example.com,https://admin.example.com
cors.allowed-methods=GET,POST,PUT,PATCH,DELETE,OPTIONS
cors.allowed-headers=authorization,content-type,x-auth-token,x-requested-with,x-correlation-id
cors.exposed-headers=x-auth-token,x-correlation-id,content-disposition,x-rate-limit-remaining,x-rate-limit-reset
cors.allow-credentials=true
cors.max-age=3600
```

### 6. Implémentation d'un Filtre CORS Personnalisé

Créer un filtre CORS personnalisé pour un contrôle plus précis :

```java
@Component
public class CustomCorsFilter implements Filter {

    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${cors.allowed-methods}")
    private List<String> allowedMethods;

    @Value("${cors.allowed-headers}")
    private List<String> allowedHeaders;

    @Value("${cors.exposed-headers}")
    private List<String> exposedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${cors.max-age}")
    private long maxAge;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        if (origin != null && isAllowedOrigin(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", String.join(",", allowedMethods));
            response.setHeader("Access-Control-Allow-Headers", String.join(",", allowedHeaders));
            response.setHeader("Access-Control-Expose-Headers", String.join(",", exposedHeaders));
            response.setHeader("Access-Control-Max-Age", String.valueOf(maxAge));
            
            if (allowCredentials) {
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    private boolean isAllowedOrigin(String origin) {
        return allowedOrigins.contains(origin) || allowedOrigins.contains("*");
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
```

## Plan d'Implémentation

### Phase 1 : Configuration de Base

1. Externaliser la configuration CORS dans les fichiers de propriétés
2. Mettre à jour la méthode `corsConfigurationSource()` pour utiliser les propriétés configurées
3. Définir des configurations spécifiques pour chaque environnement (dev, test, prod)

### Phase 2 : Améliorations Avancées

1. Implémenter un filtre CORS personnalisé pour un contrôle plus précis
2. Ajouter une journalisation des requêtes CORS rejetées pour la détection des problèmes
3. Mettre en place un mécanisme de liste blanche dynamique pour les origines autorisées

### Phase 3 : Intégration avec la Sécurité Globale

1. Intégrer la configuration CORS avec la stratégie CSRF
2. Mettre en place des tests automatisés pour vérifier la configuration CORS
3. Documenter les bonnes pratiques pour les développeurs frontend

## Exemple de Configuration Complète

### SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${cors.allowed-methods}")
    private List<String> allowedMethods;

    @Value("${cors.allowed-headers}")
    private List<String> allowedHeaders;

    @Value("${cors.exposed-headers}")
    private List<String> exposedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${cors.max-age}")
    private long maxAge;

    // Autres champs et méthodes...

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setExposedHeaders(exposedHeaders);
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### application.properties

```properties
# Configuration CORS commune
cors.allowed-methods=GET,POST,PUT,PATCH,DELETE,OPTIONS
cors.allowed-headers=authorization,content-type,x-auth-token,x-requested-with,x-correlation-id
cors.exposed-headers=x-auth-token,x-correlation-id,content-disposition,x-rate-limit-remaining,x-rate-limit-reset
cors.max-age=3600
```

### application-dev.properties

```properties
# Configuration CORS pour le développement
cors.allowed-origins=http://localhost:3000,http://localhost:4200
cors.allow-credentials=true
```

### application-prod.properties

```properties
# Configuration CORS pour la production
cors.allowed-origins=https://app.example.com,https://admin.example.com
cors.allow-credentials=true
```

## Conclusion

L'implémentation de ces améliorations renforcera considérablement la sécurité de la configuration CORS tout en
maintenant la flexibilité nécessaire pour les différents environnements. La configuration proposée suit les meilleures
pratiques de l'industrie et répond aux vulnérabilités identifiées dans l'implémentation actuelle.
