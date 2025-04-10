# Standards de Code - DEVOLUTION SaaS API

## Objectif

Ce document définit les standards de code pour le projet DEVOLUTION SaaS API. L'objectif est de maintenir un code
cohérent, lisible et facilement maintenable par tous les membres de l'équipe.

## Nomenclature des Classes

### Suffixes et Préfixes

- **Entités de persistance**: Toutes les entités de persistance doivent avoir le suffixe `Entity` (ex: `UserEntity`,
  `OrganizationEntity`)
- **Cas d'utilisation (Use Cases)**: Tous les cas d'utilisation doivent avoir le préfixe `UseCase` (ex:
  `UseCaseCreateUser`, `UseCaseUpdateOrganization`)
- **Repositories**: Toutes les interfaces de repository doivent avoir le suffixe `Repository` (ex: `UserRepository`,
  `OrganizationRepository`)
- **Implémentations de Repository**: Toutes les implémentations de repository doivent avoir le préfixe indiquant la
  technologie utilisée (ex: `JpaUserRepository`, `MongoOrganizationRepository`)
- **Services**: Tous les services doivent avoir le suffixe `Service` (ex: `UserService`, `AuthenticationService`)
- **Contrôleurs**: Tous les contrôleurs doivent avoir le suffixe `Controller` (ex: `UserController`,
  `OrganizationController`)
- **DTOs**: Tous les DTOs doivent avoir le suffixe approprié selon leur usage (`Request`, `Response`, `DTO`) (ex:
  `CreateUserRequest`, `UserResponse`)

### Conventions de Nommage

- Utiliser le CamelCase pour les noms de classes, méthodes et variables
- Les noms de classes doivent être des noms ou des groupes nominaux
- Les noms de méthodes doivent commencer par un verbe
- Les constantes doivent être en majuscules avec des underscores

## Documentation et Commentaires

### En-tête de Fichier

Chaque fichier doit commencer par un en-tête de copyright:

```java
/**
 * Copyright (c) 2025 DEVOLUTION. Tous droits réservés.
 * SaaS - Plateforme API multi-tenant
 */
```

### Documentation de Classe

Chaque classe doit être documentée avec un bloc JavaDoc qui explique son objectif:

```java
/**
 * [Description de la classe]
 *
 * [Informations supplémentaires si nécessaire]
 *
 * @author [Auteur] (optionnel)
 * @since [Version] (optionnel)
 */
```

### Documentation de Méthode

Chaque méthode publique doit être documentée avec un bloc JavaDoc:

```java
/**
 * [Description de ce que fait la méthode]
 *
 * @param paramName [Description du paramètre]
 * @return [Description de la valeur de retour]
 * @throws ExceptionType [Description de quand cette exception est levée]
 */
```

### Commentaires Internes

- Utiliser des commentaires pour expliquer le "pourquoi" plutôt que le "quoi"
- Les sections complexes du code doivent être commentées
- Éviter les commentaires redondants qui répètent simplement ce que le code fait

## Structure des Packages

La structure des packages doit suivre l'architecture hexagonale:

```
com.devolution.saas
├── [module]
│   ├── api (adapters primaires - contrôleurs, DTOs)
│   ├── application (ports primaires - use cases, services)
│   ├── domain (modèle de domaine, exceptions, interfaces de repository)
│   └── infrastructure (adapters secondaires - implémentations de repository, configuration)
├── common (classes communes à tous les modules)
└── infrastructure (configuration globale, aspects, etc.)
```

## Principes SOLID

Tout le code doit respecter les principes SOLID:

### 1. Principe de Responsabilité Unique (Single Responsibility Principle - SRP)

- Chaque classe ne doit avoir qu'une seule raison de changer
- Une classe ne doit avoir qu'une seule responsabilité
- Éviter les classes "God" qui font trop de choses

### 2. Principe Ouvert/Fermé (Open/Closed Principle - OCP)

- Les entités logicielles (classes, modules, fonctions) doivent être ouvertes à l'extension mais fermées à la
  modification
- Utiliser l'héritage et les interfaces pour permettre l'extension sans modifier le code existant
- Favoriser la composition plutôt que l'héritage quand c'est possible

### 3. Principe de Substitution de Liskov (Liskov Substitution Principle - LSP)

- Les objets d'une classe dérivée doivent pouvoir remplacer les objets de la classe de base sans affecter la cohérence
  du programme
- Les sous-classes ne doivent pas renforcer les préconditions ni affaiblir les postconditions
- Respecter le contrat défini par la classe de base

### 4. Principe de Ségrégation des Interfaces (Interface Segregation Principle - ISP)

- Les clients ne doivent pas être forcés de dépendre d'interfaces qu'ils n'utilisent pas
- Préférer plusieurs interfaces spécifiques plutôt qu'une seule interface générale
- Concevoir des interfaces fines et cohésives

### 5. Principe d'Inversion des Dépendances (Dependency Inversion Principle - DIP)

- Les modules de haut niveau ne doivent pas dépendre des modules de bas niveau. Les deux doivent dépendre d'abstractions
- Les abstractions ne doivent pas dépendre des détails. Les détails doivent dépendre des abstractions
- Utiliser l'injection de dépendances pour découpler les composants

## Application des Principes SOLID

### Exemples Concrets

#### SRP (Responsabilité Unique)

```java
// Mauvais exemple - Trop de responsabilités
public class User {
    public void saveToDatabase() { /* ... */ }

    public void sendEmail() { /* ... */ }
}

// Bon exemple - Responsabilités séparées
public class User { /* Données et comportements de l'utilisateur */
}

public class UserRepository {
    public void save(User user) { /* ... */ }
}

public class EmailService {
    public void sendEmail(User user) { /* ... */ }
}
```

#### OCP (Ouvert/Fermé)

```java
// Bon exemple - Extension sans modification
public interface PaymentProcessor {
    void processPayment(Payment payment);
}

public class CreditCardProcessor implements PaymentProcessor { /* ... */
}

public class PayPalProcessor implements PaymentProcessor { /* ... */
}
```

#### LSP (Substitution de Liskov)

```java
// Bon exemple - Substitution possible
public interface Repository<T> {
    Optional<T> findById(UUID id);

    T save(T entity);
}

public class UserRepository implements Repository<User> { /* ... */
}

public class OrganizationRepository implements Repository<Organization> { /* ... */
}
```

#### ISP (Ségrégation des Interfaces)

```java
// Mauvais exemple - Interface trop large
public interface Worker {
    void work();

    void eat();

    void sleep();
}

// Bon exemple - Interfaces séparées
public interface Workable {
    void work();
}

public interface Eatable {
    void eat();
}

public interface Sleepable {
    void sleep();
}
```

#### DIP (Inversion des Dépendances)

```java
// Mauvais exemple - Dépendance directe
public class UserService {
    private PostgresUserRepository repository;
}

// Bon exemple - Dépendance sur une abstraction
public class UserService {
    private UserRepository repository; // Interface
}
```

## Gestion des Exceptions

- Créer des exceptions spécifiques au domaine
- Documenter toutes les exceptions avec JavaDoc
- Gérer les exceptions au niveau approprié
- Éviter de supprimer les exceptions

## Tests

- Chaque classe doit avoir des tests unitaires correspondants
- Les noms des classes de test doivent suivre le format `ClassNameTest`
- Chaque méthode de test doit tester un seul aspect du comportement
- Les noms des méthodes de test doivent être descriptifs

## Exemple Complet

```java
/**
 * Copyright (c) 2025 DEVOLUTION. Tous droits réservés.
 * SaaS - Plateforme API multi-tenant
 */
package com.devolution.saas.user.domain.model;

/**
 * Représente un utilisateur dans le système.
 *
 * Un utilisateur appartient à une organisation et peut avoir plusieurs rôles
 * qui déterminent ses permissions dans le système.
 */
public class User {

    private UUID id;
    private String username;
    private String email;

    /**
     * Crée un nouvel utilisateur avec les informations de base.
     *
     * @param username Le nom d'utilisateur unique
     * @param email L'adresse email de l'utilisateur
     * @param organizationId L'ID de l'organisation à laquelle l'utilisateur appartient
     * @return Une nouvelle instance d'utilisateur
     */
    public static User create(String username, String email, UUID organizationId) {
        // Implémentation
    }

    /**
     * Vérifie si l'utilisateur a une permission spécifique.
     *
     * @param permissionName Le nom de la permission à vérifier
     * @return true si l'utilisateur a la permission, false sinon
     */
    public boolean hasPermission(String permissionName) {
        // Implémentation
    }
}
```
