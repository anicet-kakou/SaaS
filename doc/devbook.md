# Cahier de Bord du Développement

## Introduction

Ce document sert de journal de bord pour suivre l'évolution du développement de la plateforme SaaS multi-tenant pour le
secteur de l'assurance. Il documente les tâches accomplies, les décisions prises, les problèmes rencontrés et leurs
solutions.

## Informations Générales

- **Projet** : API SaaS Multi-organisation pour le secteur de l'assurance
- **Architecte** : Senior Software Architect
- **Date de début** : 03/04/2025
- **Référence Roadmap** : [doc/roadmap.md](roadmap.md)

## Analyse Initiale - 03/04/2025

### État actuel du projet

Le projet dispose d'une structure de base avec :

- Une application Spring Boot 3.2.3 avec Java 21
- Une configuration de base pour PostgreSQL et Flyway
- Un fichier de migration initial (V1__init_schema.sql) avec quelques tables de base
- Une structure de projet à développer selon l'architecture hexagonale

### Analyse de la documentation

La documentation du projet comprend :

- Une roadmap détaillée (roadmap.md) qui définit les phases de développement
- Des standards de codage (coding-standards.md) qui spécifient l'architecture hexagonale
- Une arborescence prévue (arborescence.md) qui détaille la structure modulaire
- Des plans spécifiques pour les modules d'assurance auto et risques divers
- Une documentation sur l'API Diotali pour l'intégration avec les systèmes d'attestation d'assurance

### Prochaines étapes

Selon la roadmap, la Phase 1 (Fondation) doit être implémentée en priorité :

1. Mise en place de la structure des packages selon l'architecture hexagonale
2. Implémentation des entités de base
3. Configuration de la base de données et migrations Flyway
4. Implémentation des repositories de base
5. Mise en place du système d'authentification et de sécurité

## Implémentation - Phase 1 (Fondation) - 03/04/2025

### Structure des packages et entités de base

J'ai mis en place la structure des packages selon l'architecture hexagonale et implémenté les entités de base :

1. **Package common** :
    - Création des classes de base : `BaseEntity`, `AuditableEntity`, `TenantAwareEntity`
    - Création des annotations : `TenantRequired`, `Auditable`
    - Création des exceptions : `BusinessException`, `ResourceNotFoundException`, `ValidationException`
    - Création des utilitaires : `DateUtils`, `StringUtils`, `ValidationUtils`, `JsonUtils`

2. **Module organization** :
    - **Domain** :
        - Modèles : `Organization`, `OrganizationType`, `OrganizationStatus`, `OrganizationHierarchy`
        - Repositories : `OrganizationRepository`, `OrganizationHierarchyRepository`
        - Events : `OrganizationCreatedEvent`, `OrganizationUpdatedEvent`
    - **Infrastructure** :
        - Persistence : `JpaOrganizationRepository`, `JpaOrganizationHierarchyRepository`
        - Messaging : `OrganizationEventPublisher`
    - **Application** :
        - DTOs : `OrganizationDTO`, `OrganizationTypeDTO`, `OrganizationHierarchyDTO`
        - Commands : `CreateOrganizationCommand`, `UpdateOrganizationCommand`
        - Queries : `GetOrganizationQuery`, `ListOrganizationsQuery`
        - Services : `OrganizationService`
    - **API** :
        - Controllers : `OrganizationController`, `OrganizationTypeController`, `OrganizationHierarchyController`

Les migrations Flyway étaient déjà configurées dans le projet initial avec les tables nécessaires pour les organisations
et la hiérarchie des organisations.

### Tests unitaires

J'ai implémenté des tests unitaires pour le service d'organisation :

- `OrganizationServiceTest` : Tests pour les méthodes de création et de récupération d'organisations
    - Test de création réussie d'une organisation
    - Test de validation du code unique lors de la création
    - Test de récupération d'une organisation par ID
    - Test de récupération d'une organisation par code
    - Test de gestion des erreurs (organisation non trouvée, requête invalide)

## Implémentation - Module de Sécurité - 03/04/2025

J'ai implémenté le module de sécurité pour gérer l'authentification, les utilisateurs, les rôles et les permissions :

1. **Entités du domaine de sécurité** :
    - Modèles : `User`, `UserStatus`, `Role`, `Permission`, `UserOrganization`, `RefreshToken`
    - Repositories : `UserRepository`, `RoleRepository`, `PermissionRepository`, `RefreshTokenRepository`

2. **Infrastructure de sécurité** :
    - Persistence : `JpaUserRepository`, `JpaRoleRepository`, `JpaPermissionRepository`, `JpaRefreshTokenRepository`
    - Configuration JWT : `JwtTokenProvider`, `JwtAuthenticationFilter`, `JwtAuthenticationEntryPoint`
    - Configuration de sécurité : `SecurityConfig`
    - Service de contexte tenant : `TenantContextHolder`

3. **Application de sécurité** :
    - DTOs : `UserDTO`, `RoleDTO`, `PermissionDTO`, `UserOrganizationDTO`, `JwtAuthenticationResponse`
    - Commands : `LoginCommand`, `RegisterCommand`, `RefreshTokenCommand`, `CreateUserCommand`, `UpdateUserCommand`,
      `ChangePasswordCommand`
    - Services : `AuthenticationService`, `UserService`

4. **API de sécurité** :
    - Controllers : `AuthController`, `UserController`

Le module de sécurité implémente les fonctionnalités suivantes :

- Authentification JWT avec jetons d'accès et de rafraîchissement
- Enregistrement et connexion des utilisateurs
- Gestion des utilisateurs (création, mise à jour, activation/désactivation, verrouillage/déverrouillage)
- Gestion des rôles et permissions
- Support multi-tenant avec filtrage des données par organisation

### Implémentation des aspects et du service de sécurité

J'ai implémenté les aspects pour les annotations `TenantRequired` et `Auditable` ainsi que le service de sécurité pour
les vérifications d'autorisation personnalisées :

1. **Aspects** :
    - `TenantRequiredAspect` : Vérifie qu'un tenant (organisation) est défini dans le contexte avant d'exécuter la
      méthode annotée
    - `AuditableAspect` : Enregistre les appels de méthode, les paramètres d'entrée et les résultats dans les journaux
      d'audit

2. **Service de sécurité** :
    - `SecurityService` : Fournit des méthodes pour les vérifications d'autorisation personnalisées utilisées dans les
      expressions SpEL des annotations `@PreAuthorize`
    - Méthodes implémentées : `isCurrentUser`, `isCurrentUsername`, `isCurrentUserEmail`, `isUserInOrganization`,
      `hasRole`, `hasPermission`, `getCurrentOrganizationId`, `isCurrentOrganization`

Ces implémentations permettent :

- De sécuriser les API en vérifiant que l'utilisateur a les autorisations nécessaires
- D'assurer que les opérations nécessitant un contexte d'organisation sont exécutées dans ce contexte
- De journaliser les actions des utilisateurs pour l'audit et le débogage

### Implémentation des données initiales

J'ai implémenté le chargement des données initiales pour les rôles et permissions système :

- `InitialDataLoader` : Classe de configuration qui initialise les rôles et permissions système au démarrage de
  l'application

1. **Permissions système** :
    - Permissions pour les organisations (CREATE, READ, UPDATE, DELETE, LIST)
    - Permissions pour les utilisateurs (CREATE, READ, UPDATE, DELETE, LIST, CHANGE_PASSWORD, ACTIVATE, DEACTIVATE,
      LOCK, UNLOCK)
    - Permissions pour les rôles (CREATE, READ, UPDATE, DELETE, LIST, ASSIGN, REVOKE)
    - Permissions pour les permissions (READ, LIST, ASSIGN, REVOKE)
    - Permissions pour les polices d'assurance (CREATE, READ, UPDATE, DELETE, LIST, RENEW, CANCEL, SUSPEND, REACTIVATE)
    - Permissions pour les sinistres (CREATE, READ, UPDATE, DELETE, LIST, APPROVE, REJECT, CLOSE, REOPEN)

2. **Rôles système** :
    - ADMIN : Administrateur avec tous les droits (toutes les permissions)
    - USER : Utilisateur standard (permissions de lecture et de création de sinistres)
    - MANAGER : Gestionnaire avec droits étendus (permissions de gestion des utilisateurs, polices et sinistres)

Ces données initiales permettent de définir les autorisations de base pour les différents types d'utilisateurs de la
plateforme.

### Résumé des réalisations

J'ai implémenté les éléments suivants de la Phase 1 (Fondation) de la roadmap :

1. **Structure des packages selon l'architecture hexagonale**
2. **Entités de base** (BaseEntity, AuditableEntity, TenantAwareEntity)
3. **Module d'organisation** (domain, infrastructure, application, api)
4. **Module de sécurité** (domain, infrastructure, application, api)
5. **Aspects pour les annotations** (TenantRequired, Auditable)
6. **Service de sécurité** pour les vérifications d'autorisation personnalisées
7. **Données initiales** pour les rôles et permissions système

## Implémentation - Filtrage contextuel des données - 03/04/2025

J'ai implémenté le filtrage contextuel des données basé sur la hiérarchie des organisations :

1. **Annotation et aspect pour le filtrage** :
    - `@TenantFilter` : Annotation pour marquer les méthodes qui doivent être filtrées par tenant
    - `TenantFilterAspect` : Aspect qui intercepte les appels de méthodes annotées et ajoute des critères de filtrage

2. **Service de hiérarchie d'organisations** :
    - `OrganizationHierarchyService` : Service qui gère les relations hiérarchiques entre organisations
    - Méthodes implémentées : `getAllDescendantIds`, `getAllAncestorIds`, `isAncestorOf`, `getVisibleOrganizationIds`,
      `getOrganizationPath`

3. **Spécification JPA pour le filtrage** :
    - `TenantSpecification` : Spécification JPA pour filtrer les données par organisation
    - Support pour le filtrage par organisation unique ou par ensemble d'organisations visibles

4. **Modification des repositories et services** :
    - Ajout de l'interface `JpaSpecificationExecutor` aux repositories JPA
    - Utilisation de l'annotation `@TenantFilter` sur les méthodes de service appropriées
    - Conversion des requêtes existantes en spécifications JPA pour supporter le filtrage

Cette implémentation permet de filtrer automatiquement les données en fonction du contexte de l'organisation courante,
en prenant en compte la hiérarchie des organisations. Les utilisateurs ne peuvent voir que les données de leur propre
organisation et, si configuré, des organisations descendantes.

## Implémentation - Refactoring de l'architecture - 04/04/2025

J'ai effectué un refactoring important de l'architecture pour améliorer la cohérence et la maintenabilité du code :

1. **Implémentation des cas d'utilisation (use cases)** :
    - Création de packages `usecase` dans les modules d'organisation et de sécurité
    - Extraction de la logique métier des services vers des classes de cas d'utilisation dédiées
    - Transformation des services en orchestrateurs qui délèguent aux cas d'utilisation

2. **Création de packages `mapper` dédiés** :
    - Déplacement des mappers depuis les packages `usecase` vers des packages `mapper` dédiés
    - Séparation claire des responsabilités entre mapping et logique métier

3. **Simplification des noms de classes** :
    - Suppression du suffixe redondant `UseCase` des classes de cas d'utilisation
    - Noms plus concis et expressifs (ex: `CreateOrganization` au lieu de `CreateOrganizationUseCase`)

4. **Correction des erreurs de compilation et d'exécution** :
    - Résolution des problèmes de méthodes manquantes dans les interfaces
    - Correction des conflits de versions dans les scripts de migration Flyway
    - Résolution des problèmes de clés étrangères et de colonnes manquantes

Ces améliorations ont permis d'obtenir une architecture plus cohérente, plus maintenable et plus conforme aux principes
de l'architecture hexagonale. Le code est maintenant plus modulaire, plus testable et plus facile à étendre.

### Analyse de cohérence et de performance

J'ai également réalisé une analyse approfondie de la cohérence architecturale et des performances potentielles du
projet. Les principaux points forts identifiés sont :

- Architecture hexagonale bien implémentée avec séparation claire des couches
- Respect des principes SOLID
- Utilisation efficace de JPA avec spécifications et filtrage contextuel
- Sécurité robuste avec authentification JWT et autorisation fine

Les principaux points d'amélioration identifiés sont :

- Besoin de tests unitaires et d'intégration
- Optimisation des requêtes N+1 et mise en place d'une stratégie de cache
- Amélioration de la documentation et des messages d'erreur
- Renforcement de la validation des entrées

### Prochaines étapes

- [ ] Implémenter les tests unitaires pour les cas d'utilisation
- [ ] Implémenter les tests d'intégration pour les repositories
- [ ] Optimiser les performances avec une stratégie de cache
- [ ] Implémenter le module de gestion des polices d'assurance
- [ ] Implémenter le module de gestion des sinistres
- [ ] Implémenter l'intégration avec l'API Diotali pour les attestations d'assurance
