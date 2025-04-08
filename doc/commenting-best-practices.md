# Bonnes Pratiques pour les Commentaires

## Introduction

Ce document présente les bonnes pratiques pour écrire des commentaires de qualité dans le code du projet DEVOLUTION SaaS
API. Des commentaires bien écrits facilitent la compréhension, la maintenance et l'évolution du code.

## Principes Généraux

1. **Commentez le "pourquoi", pas le "quoi"**
    - Le code lui-même devrait expliquer ce qu'il fait
    - Les commentaires devraient expliquer pourquoi une décision a été prise

2. **Soyez concis et précis**
    - Évitez les commentaires verbeux
    - Allez droit au but

3. **Maintenez les commentaires à jour**
    - Des commentaires obsolètes sont pires que pas de commentaires du tout
    - Mettez à jour les commentaires lorsque vous modifiez le code

4. **Utilisez un langage professionnel**
    - Évitez l'argot, les abréviations obscures et l'humour
    - Utilisez un français correct et professionnel

## Types de Commentaires

### 1. En-tête de Fichier

Chaque fichier doit commencer par un en-tête de copyright:

```java
/**
 * Copyright (c) 2025 DEVOLUTION. Tous droits réservés.
 * SaaS - Plateforme API multi-tenant
 */
```

### 2. Documentation de Classe (JavaDoc)

```java
/**
 * [Description concise de la classe]
 *
 * [Description détaillée si nécessaire, expliquant le rôle de la classe
 * dans le système et ses responsabilités principales]
 *
 * @author [Auteur] (optionnel)
 * @since [Version] (optionnel)
 */
```

Exemple:

```java
/**
 * Service de gestion des utilisateurs.
 *
 * Ce service fournit des fonctionnalités pour créer, rechercher, mettre à jour
 * et supprimer des utilisateurs. Il gère également l'authentification et
 * l'autorisation des utilisateurs.
 *
 * @author Équipe DEVOLUTION
 * @since 1.0.0
 */
```

### 3. Documentation de Méthode (JavaDoc)

```java
/**
 * [Description concise de ce que fait la méthode]
 *
 * [Description détaillée si nécessaire, expliquant le comportement de la méthode,
 * les algorithmes utilisés, etc.]
 *
 * @param paramName [Description du paramètre]
 * @return [Description de la valeur de retour]
 * @throws ExceptionType [Description de quand cette exception est levée]
 */
```

Exemple:

```java
/**
 * Authentifie un utilisateur avec son nom d'utilisateur et son mot de passe.
 *
 * Cette méthode vérifie les identifiants de l'utilisateur, génère un token JWT
 * si l'authentification réussit, et crée un token de rafraîchissement.
 *
 * @param username Le nom d'utilisateur
 * @param password Le mot de passe en clair
 * @return Un objet AuthenticationResult contenant les tokens et les informations de l'utilisateur
 * @throws AuthenticationException Si les identifiants sont invalides
 */
```

### 4. Commentaires de Bloc

Utilisez des commentaires de bloc pour expliquer des sections complexes de code:

```java
/*
 * Cette section implémente l'algorithme de tri fusion.
 * Nous avons choisi cet algorithme car il offre de bonnes performances
 * pour les grandes collections (O(n log n)) et est stable.
 */
```

### 5. Commentaires de Ligne

Utilisez des commentaires de ligne pour des explications brèves:

```java
int result = calculateTotal(); // Le total inclut les taxes
```

## Cas Particuliers

### 1. TODO, FIXME et autres annotations

Utilisez des annotations standardisées pour marquer le code qui nécessite une attention future:

```java
// TODO: Implémenter la validation des données d'entrée
// FIXME: Cette méthode a une fuite de mémoire
// NOTE: Cette implémentation est temporaire et sera remplacée dans la version 2.0
```

### 2. Code Complexe ou Non Intuitif

Expliquez toujours le code qui n'est pas immédiatement compréhensible:

```java
// Utilisation d'une expression régulière pour valider le format de l'email
// Le pattern vérifie: nom@domaine.extension
if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
        throw new

ValidationException("Format d'email invalide");
}
```

### 3. Optimisations

Documentez les optimisations et expliquez pourquoi elles sont nécessaires:

```java
// Utilisation d'un cache pour éviter des requêtes répétées à la base de données
// Cela améliore les performances de 30% selon nos benchmarks
```

## Exemples Complets

### Exemple de Classe Bien Commentée

```java
/**
 * Copyright (c) 2025 DEVOLUTION. Tous droits réservés.
 * SaaS - Plateforme API multi-tenant
 */
package com.devolution.saas.security.application.service;

/**
 * Service d'authentification des utilisateurs.
 *
 * Ce service gère l'authentification des utilisateurs, la génération et la validation
 * des tokens JWT, ainsi que la gestion des tokens de rafraîchissement.
 *
 * @author Équipe DEVOLUTION
 * @since 1.0.0
 */
public class AuthenticationService {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    /**
     * Constructeur pour l'injection de dépendances.
     *
     * @param jwtProvider Le fournisseur de JWT utilisé pour générer et valider les tokens
     * @param userService Le service utilisateur utilisé pour charger les informations des utilisateurs
     */
    public AuthenticationService(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    /**
     * Authentifie un utilisateur avec son nom d'utilisateur et son mot de passe.
     *
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe en clair
     * @return Un objet AuthenticationResult contenant les tokens et les informations de l'utilisateur
     * @throws AuthenticationException Si les identifiants sont invalides
     */
    public AuthenticationResult authenticate(String username, String password) {
        // Vérification des identifiants
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));

        // Vérification du mot de passe
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Mot de passe incorrect");
        }

        // Génération des tokens
        String accessToken = jwtProvider.generateToken(user);
        String refreshToken = refreshTokenService.createToken(user.getId());

        return new AuthenticationResult(accessToken, refreshToken, user);
    }
}
```

## Conclusion

Des commentaires de qualité sont essentiels pour maintenir un code lisible et maintenable. En suivant ces bonnes
pratiques, nous assurons que notre code reste compréhensible pour tous les membres de l'équipe, actuels et futurs.
