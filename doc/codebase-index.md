# Indexation de la Codebase

## Introduction

Ce document fournit une indexation complète de la codebase du projet SaaS multi-tenant pour l'assurance. Il est conçu
pour aider les développeurs à naviguer efficacement dans le code source, comprendre les relations entre les différents
composants et identifier rapidement les modules pertinents pour leurs tâches.

## Structure Globale du Projet

Le projet suit une architecture hexagonale (Ports & Adapters) avec une organisation modulaire par domaine métier. Chaque
module respecte les principes DDD (Domain-Driven Design) avec une séparation claire entre l'API, l'application, le
domaine et l'infrastructure.

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── devolution/
│   │           └── saas/
│   │               ├── SaasApplication.java       # Point d'entrée de l'application
│   │               ├── common/                    # Composants communs
│   │               ├── core/                      # Modules techniques
│   │               └── insurance/                 # Modules métier assurance
│   └── resources/                                # Ressources et configuration
└── test/                                        # Tests unitaires et d'intégration
```

## Modules Principaux

### 1. Module Common

Contient les composants partagés et utilitaires utilisés dans toute l'application.

#### 1.1 Abstractions

| Classe                   | Description                                                |
|--------------------------|------------------------------------------------------------|
| `AbstractCreateUseCase`  | Classe abstraite pour les cas d'utilisation de création    |
| `AbstractCrudController` | Classe abstraite pour les contrôleurs CRUD                 |
| `AbstractCrudService`    | Classe abstraite pour les services CRUD                    |
| `AbstractUpdateUseCase`  | Classe abstraite pour les cas d'utilisation de mise à jour |

#### 1.2 Annotations

| Annotation       | Description                     |
|------------------|---------------------------------|
| `Auditable`      | Marque une méthode pour l'audit |
| `TenantFilter`   | Applique un filtre de tenant    |
| `TenantRequired` | Indique qu'un tenant est requis |

#### 1.3 API

| Classe                   | Description                                 |
|--------------------------|---------------------------------------------|
| `GlobalExceptionHandler` | Gestionnaire global d'exceptions pour l'API |
| `ErrorResponse`          | DTO pour les réponses d'erreur              |

#### 1.4 Aspects

| Classe                 | Description                           |
|------------------------|---------------------------------------|
| `AuditAspect`          | Aspect pour l'audit des opérations    |
| `AuditableAspect`      | Aspect pour les éléments auditables   |
| `TenantFilterAspect`   | Aspect pour le filtrage par tenant    |
| `TenantRequiredAspect` | Aspect pour la vérification de tenant |

#### 1.5 Configuration

| Classe            | Description                                      |
|-------------------|--------------------------------------------------|
| `JacksonConfig`   | Configuration Jackson pour la sérialisation JSON |
| `RateLimitConfig` | Configuration pour la limitation de débit        |

#### 1.6 Domaine

##### 1.6.1 Exceptions

| Classe                      | Description                          |
|-----------------------------|--------------------------------------|
| `BusinessException`         | Exception métier générique           |
| `ConcurrencyException`      | Exception de concurrence             |
| `ResourceNotFoundException` | Exception pour ressource non trouvée |
| `SecurityException`         | Exception de sécurité                |
| `TenantException`           | Exception liée au tenant             |
| `ValidationException`       | Exception de validation              |

##### 1.6.2 Modèles

| Classe                | Description                      |
|-----------------------|----------------------------------|
| `AuditableEntity`     | Entité avec support d'audit      |
| `BaseEntity`          | Entité de base avec ID           |
| `SystemDefinedEntity` | Entité définie par le système    |
| `TenantAwareEntity`   | Entité avec support multi-tenant |

#### 1.7 Filtres

| Classe                 | Description                                |
|------------------------|--------------------------------------------|
| `RateLimitFilter`      | Filtre pour la limitation de débit         |
| `RequestLoggingFilter` | Filtre pour la journalisation des requêtes |

#### 1.8 Infrastructure

| Classe                | Description                                   |
|-----------------------|-----------------------------------------------|
| `TenantSpecification` | Spécification JPA pour le filtrage par tenant |

#### 1.9 Services

| Classe             | Description                                       |
|--------------------|---------------------------------------------------|
| `RateLimitService` | Service pour la gestion de la limitation de débit |

#### 1.10 Utilitaires

| Classe             | Description                                |
|--------------------|--------------------------------------------|
| `DateUtils`        | Utilitaires pour la manipulation des dates |
| `HttpRequestUtils` | Utilitaires pour les requêtes HTTP         |
| `JsonUtils`        | Utilitaires pour la manipulation JSON      |
| `StringUtils`      | Utilitaires pour les chaînes de caractères |
| `ValidationUtils`  | Utilitaires pour la validation             |

### 2. Module Core

Contient les fonctionnalités centrales du système.

#### 2.1 Audit

##### 2.1.1 API

| Classe               | Description                      |
|----------------------|----------------------------------|
| `AuditLogController` | Contrôleur pour les logs d'audit |

##### 2.1.2 Application

| Classe           | Description                        |
|------------------|------------------------------------|
| `AuditLogDTO`    | DTO pour les logs d'audit          |
| `AuditLogMapper` | Mapper pour les logs d'audit       |
| `AuditService`   | Service pour la gestion de l'audit |

##### 2.1.3 Domaine

| Classe               | Description                                   |
|----------------------|-----------------------------------------------|
| `AuditLog`           | Entité représentant un log d'audit            |
| `AuditStatus`        | Énumération des statuts d'audit               |
| `AuditLogRepository` | Interface du repository pour les logs d'audit |

##### 2.1.4 Infrastructure

| Classe                  | Description                              |
|-------------------------|------------------------------------------|
| `JpaAuditLogRepository` | Implémentation JPA du repository d'audit |

#### 2.2 Organisation

##### 2.2.1 API

| Classe                            | Description                                     |
|-----------------------------------|-------------------------------------------------|
| `OrganizationController`          | Contrôleur pour les organisations               |
| `OrganizationHierarchyController` | Contrôleur pour les hiérarchies d'organisations |
| `OrganizationTypeController`      | Contrôleur pour les types d'organisations       |

##### 2.2.2 Application

| Classe                         | Description                                             |
|--------------------------------|---------------------------------------------------------|
| `CreateOrganizationCommand`    | Commande pour créer une organisation                    |
| `UpdateOrganizationCommand`    | Commande pour mettre à jour une organisation            |
| `OrganizationDTO`              | DTO pour les organisations                              |
| `OrganizationHierarchyDTO`     | DTO pour les hiérarchies d'organisations                |
| `OrganizationTypeDTO`          | DTO pour les types d'organisations                      |
| `OrganizationMapper`           | Mapper pour les organisations                           |
| `OrganizationHierarchyService` | Service pour la gestion des hiérarchies d'organisations |
| `OrganizationService`          | Service pour la gestion des organisations               |

##### 2.2.3 Domaine

| Classe                            | Description                                                  |
|-----------------------------------|--------------------------------------------------------------|
| `OrganizationCreatedEvent`        | Événement de création d'organisation                         |
| `OrganizationUpdatedEvent`        | Événement de mise à jour d'organisation                      |
| `Organization`                    | Entité représentant une organisation                         |
| `OrganizationHierarchy`           | Entité représentant une hiérarchie d'organisations           |
| `OrganizationStatus`              | Énumération des statuts d'organisation                       |
| `OrganizationType`                | Énumération des types d'organisation                         |
| `OrganizationRepository`          | Interface du repository pour les organisations               |
| `OrganizationHierarchyRepository` | Interface du repository pour les hiérarchies d'organisations |

##### 2.2.4 Infrastructure

| Classe                               | Description                                                     |
|--------------------------------------|-----------------------------------------------------------------|
| `OrganizationEventPublisher`         | Publieur d'événements pour les organisations                    |
| `JpaOrganizationRepository`          | Implémentation JPA du repository d'organisations                |
| `JpaOrganizationHierarchyRepository` | Implémentation JPA du repository de hiérarchies d'organisations |

#### 2.3 Sécurité

##### 2.3.1 API

| Classe                 | Description                              |
|------------------------|------------------------------------------|
| `ApiKeyController`     | Contrôleur pour les clés API             |
| `AuthController`       | Contrôleur pour l'authentification       |
| `OAuthController`      | Contrôleur pour l'authentification OAuth |
| `PermissionController` | Contrôleur pour les permissions          |
| `RoleController`       | Contrôleur pour les rôles                |
| `UserController`       | Contrôleur pour les utilisateurs         |

##### 2.3.2 Application

| Classe                    | Description                              |
|---------------------------|------------------------------------------|
| `ChangePasswordCommand`   | Commande pour changer de mot de passe    |
| `CreateApiKeyCommand`     | Commande pour créer une clé API          |
| `CreatePermissionCommand` | Commande pour créer une permission       |
| `CreateRoleCommand`       | Commande pour créer un rôle              |
| `CreateUserCommand`       | Commande pour créer un utilisateur       |
| `ApiKeyDTO`               | DTO pour les clés API                    |
| `PermissionDTO`           | DTO pour les permissions                 |
| `RoleDTO`                 | DTO pour les rôles                       |
| `UserDTO`                 | DTO pour les utilisateurs                |
| `ApiKeyService`           | Service pour la gestion des clés API     |
| `AuthenticationService`   | Service pour l'authentification          |
| `PermissionService`       | Service pour la gestion des permissions  |
| `RoleService`             | Service pour la gestion des rôles        |
| `UserService`             | Service pour la gestion des utilisateurs |

##### 2.3.3 Domaine

| Classe                   | Description                                                 |
|--------------------------|-------------------------------------------------------------|
| `ApiKey`                 | Entité représentant une clé API                             |
| `ApiKeyStatus`           | Énumération des statuts de clé API                          |
| `Permission`             | Entité représentant une permission                          |
| `RefreshToken`           | Entité représentant un token de rafraîchissement            |
| `Role`                   | Entité représentant un rôle                                 |
| `User`                   | Entité représentant un utilisateur                          |
| `UserOrganization`       | Entité représentant l'association utilisateur-organisation  |
| `UserStatus`             | Énumération des statuts utilisateur                         |
| `ApiKeyRepository`       | Interface du repository pour les clés API                   |
| `PermissionRepository`   | Interface du repository pour les permissions                |
| `RefreshTokenRepository` | Interface du repository pour les tokens de rafraîchissement |
| `RoleRepository`         | Interface du repository pour les rôles                      |
| `UserRepository`         | Interface du repository pour les utilisateurs               |

##### 2.3.4 Infrastructure

| Classe                        | Description                                                    |
|-------------------------------|----------------------------------------------------------------|
| `Auth0AuthAdapter`            | Adaptateur pour Auth0                                          |
| `KeycloakAuthAdapter`         | Adaptateur pour Keycloak                                       |
| `LocalAuthAdapter`            | Adaptateur pour l'authentification locale                      |
| `JwtAuthenticationEntryPoint` | Point d'entrée pour l'authentification JWT                     |
| `JwtTokenProvider`            | Fournisseur de tokens JWT                                      |
| `SecurityConfig`              | Configuration de sécurité                                      |
| `ApiKeyAuthenticationFilter`  | Filtre d'authentification par clé API                          |
| `JwtAuthenticationFilter`     | Filtre d'authentification JWT                                  |
| `JpaApiKeyRepository`         | Implémentation JPA du repository de clés API                   |
| `JpaPermissionRepository`     | Implémentation JPA du repository de permissions                |
| `JpaRefreshTokenRepository`   | Implémentation JPA du repository de tokens de rafraîchissement |
| `JpaRoleRepository`           | Implémentation JPA du repository de rôles                      |
| `JpaUserRepository`           | Implémentation JPA du repository d'utilisateurs                |
| `CustomUserDetailsService`    | Service de détails utilisateur personnalisé                    |
| `SecurityService`             | Service de sécurité                                            |
| `TenantContextHolder`         | Gestionnaire de contexte tenant                                |

### 3. Module Insurance

Contient les modules métier spécifiques à l'assurance.

#### 3.1 Auto (Assurance Automobile)

##### 3.1.1 API

| Classe                 | Description                        |
|------------------------|------------------------------------|
| `AutoPolicyController` | Contrôleur pour les polices auto   |
| `BonusMalusController` | Contrôleur pour les bonus/malus    |
| `VehicleController`    | Contrôleur pour les véhicules      |
| `ApiVersions`          | Constantes pour les versions d'API |

##### 3.1.2 Application

| Classe                    | Description                                     |
|---------------------------|-------------------------------------------------|
| `CreateVehicleCommand`    | Commande pour créer un véhicule                 |
| `UpdateBonusMalusCommand` | Commande pour mettre à jour un bonus/malus      |
| `BonusMalusDTO`           | DTO pour les bonus/malus                        |
| `VehicleDTO`              | DTO pour les véhicules                          |
| `AutoPricingService`      | Service pour le calcul des prix auto            |
| `VehicleService`          | Service pour la gestion des véhicules           |
| `ApplyBonusMalus`         | Cas d'utilisation pour appliquer un bonus/malus |
| `CalculateAutoPremium`    | Cas d'utilisation pour calculer une prime auto  |

##### 3.1.3 Domaine

| Classe                 | Description                                     |
|------------------------|-------------------------------------------------|
| `AutoInsuranceProduct` | Entité représentant un produit d'assurance auto |
| `AutoPolicy`           | Entité représentant une police auto             |
| `BonusMalus`           | Entité représentant un bonus/malus              |
| `Driver`               | Entité représentant un conducteur               |
| `Vehicle`              | Entité représentant un véhicule                 |
| `VehicleUsage`         | Énumération des usages de véhicule              |
| `BonusMalusRepository` | Interface du repository pour les bonus/malus    |
| `VehicleRepository`    | Interface du repository pour les véhicules      |
| `BonusMalusCalculator` | Service de calcul de bonus/malus                |

##### 3.1.4 Infrastructure

| Classe                    | Description                                     |
|---------------------------|-------------------------------------------------|
| `JpaBonusMalusRepository` | Implémentation JPA du repository de bonus/malus |
| `JpaVehicleRepository`    | Implémentation JPA du repository de véhicules   |

## Relations entre les Modules

### Relations Principales

1. **Multi-tenant**
    - Le module `core/organization` fournit la gestion des organisations
    - Les entités métier héritent de `TenantAwareEntity` pour le support multi-tenant
    - Le `TenantContextHolder` maintient le contexte du tenant actuel

2. **Sécurité et Authentification**
    - Le module `core/security` gère l'authentification et l'autorisation
    - Les filtres JWT et API Key interceptent les requêtes pour l'authentification
    - Les contrôleurs utilisent les annotations de sécurité pour l'autorisation

3. **Audit**
    - Le module `core/audit` fournit les fonctionnalités d'audit
    - L'aspect `AuditAspect` intercepte les méthodes annotées avec `@Auditable`
    - Les entités auditables héritent de `AuditableEntity`

4. **Modules Métier**
    - Les modules métier dans `insurance/` utilisent les services techniques du `core/`
    - Chaque module métier suit l'architecture hexagonale avec ses propres API, domaine et infrastructure

## Métriques de Code

### Complexité et Couverture

| Module            | Nombre de Classes | Complexité Cyclomatique Moyenne | Couverture de Tests |
|-------------------|-------------------|---------------------------------|---------------------|
| common            | ~25               | Faible                          | À déterminer        |
| core/audit        | ~10               | Faible                          | À déterminer        |
| core/organization | ~20               | Moyenne                         | À déterminer        |
| core/security     | ~50               | Élevée                          | À déterminer        |
| insurance/auto    | ~25               | Moyenne                         | À déterminer        |

## Points d'Extension

1. **Nouveaux Produits d'Assurance**
    - Ajouter de nouveaux modules dans `insurance/`
    - Suivre le même modèle d'architecture hexagonale

2. **Intégrations Externes**
    - Ajouter des adaptateurs dans l'infrastructure de chaque module
    - Implémenter les interfaces de port définies dans le domaine

3. **Personnalisation Multi-tenant**
    - Étendre le module `core/organization` pour la gestion des configurations par tenant
    - Utiliser le contexte tenant pour charger les configurations spécifiques

## Conclusion

Cette indexation fournit une vue d'ensemble de la structure et des composants de la codebase. Elle sert de point de
départ pour comprendre l'organisation du code et les relations entre les différents modules. Pour des informations plus
détaillées sur des aspects spécifiques, consultez les autres documents de documentation technique dans le dossier
`doc/`.
