# Mémoire du Projet SaaS Multi-organisation pour le secteur de l'assurance

## 0. Documents de référence et ressources

### Documents clés

- **Roadmap du projet**: [doc/roadmap.md](roadmap.md) - Contient la feuille de route détaillée, les phases de
  développement et le calendrier prévisionnel
- **Cahier de bord du développement**: [doc/devbook.md](devbook.md) - Journal de bord documentant les tâches accomplies,
  les décisions prises et les problèmes rencontrés
- **Standards de code**: [doc/coding-standards.md](coding-standards.md) - Définit les standards de code, les conventions
  de nommage et les principes architecturaux
- **Structure du projet**: [doc/arborescence.md](arborescence.md) - Détaille la structure modulaire et l'organisation
  des packages
- **Documentation API Diotali**: [doc/diotali-api.md](diotali-api.md) - Documentation technique pour l'intégration avec
  l'API d'attestations d'assurance digitale

### Ressources techniques

- **Scripts de migration**: `src/main/resources/db/migration/` - Scripts Flyway pour la création et mise à jour du
  schéma de base de données
- **Configuration**: `src/main/resources/application.yml` - Configuration principale de l'application
- **Tests**: `src/test/` - Tests unitaires et d'intégration

## 1. Vue d'ensemble du projet

### Objectif

Développer une plateforme SaaS multi-tenant pour la gestion des solutions d'assurance, permettant aux différents acteurs
du secteur (assureurs, courtiers, agents généraux, coassureurs, apporteurs d'affaires, etc.) de gérer leurs
utilisateurs, rôles, permissions et données métier de manière sécurisée et isolée.

La plateforme respecte la hiérarchie naturelle du secteur assurantiel en offrant à chaque entité un espace cloisonné
avec une visibilité adaptée à sa position hiérarchique. Les organisations de niveau supérieur (comme les assureurs)
bénéficient d'une visibilité complète sur les ressources des organisations qui leur sont subordonnées, tandis que les
organisations de niveau inférieur n'ont accès qu'à leurs propres données.

### Architecture globale

#### Principes architecturaux

- Architecture hexagonale (Ports & Adapters) avec organisation modulaire par domaine métier
- Séparation claire des préoccupations (API, application, domaine, infrastructure)
- Approche Domain-Driven Design (DDD)
- Monolithe modulaire avec possibilité d'évolution vers des microservices

#### Choix technologiques

- **Langage et framework** : Java 17 avec Spring Boot 3.x
- **Base de données** : PostgreSQL 17 avec Flyway pour les migrations
- **Interface utilisateur** : Application web responsive avec React et Material-UI
- **Approche multi-tenant** : Isolation par base de données partagée avec discrimination par ID d'organisation

#### Sécurité

- **Authentification** : JWT Bearer token avec refresh tokens, API Keys pour les systèmes externes
- **Autorisation** : Contrôle d'accès basé sur les rôles (RBAC), filtrage contextuel des données
- **Protection des données** : Chiffrement des données sensibles, journalisation complète, conformité RGPD

## 2. Structure du projet

### Organisation des packages

Le projet suit une architecture hexagonale avec une organisation modulaire par domaine métier :

```
com.devolution.saas
├── SaasApplication.java                # Point d'entrée de l'application
│
├── common                             # Composants communs et utilitaires
│   ├── annotation                     # Annotations personnalisées
│   ├── config                         # Configurations globales
│   ├── domain                         # Modèles de domaine communs
│   ├── exception                      # Gestion des exceptions
│   ├── logging                        # Configuration et utilitaires de logging
│   ├── security                       # Configuration de sécurité commune
│   └── util                           # Classes utilitaires
│
├── organization                       # Module de gestion des organisations
│   ├── api                            # Contrôleurs REST
│   ├── application                    # Services d'application
│   ├── domain                         # Modèles et repositories de domaine
│   └── infrastructure                 # Implémentations d'infrastructure
│
├── security                           # Module de gestion de la sécurité
│   ├── api                            # Contrôleurs REST
│   ├── application                    # Services d'application
│   ├── domain                         # Modèles et repositories de domaine
│   └── infrastructure                 # Implémentations d'infrastructure
│
├── insurance                          # Module de gestion des produits d'assurance
│   ├── api                            # Contrôleurs REST
│   ├── application                    # Services d'application
│   ├── domain                         # Modèles et repositories de domaine
│   └── infrastructure                 # Implémentations d'infrastructure
│
├── policy                             # Module de gestion des polices d'assurance
│   ├── api                            # Contrôleurs REST
│   ├── application                    # Services d'application
│   ├── domain                         # Modèles et repositories de domaine
│   └── infrastructure                 # Implémentations d'infrastructure
│
├── nonelife                           # Module spécifique aux produits nonelife
│   ├── auto                           # Module d'assurance automobile
│   ├── home                           # Module d'assurance habitation (MRH)
│   ├── travel                         # Module d'assurance voyage
│   └── misc                           # Module risques divers
│
├── claim                              # Module de gestion des sinistres
│   ├── api                            # Contrôleurs REST
│   ├── application                    # Services d'application
│   ├── domain                         # Modèles et repositories de domaine
│   └── infrastructure                 # Implémentations d'infrastructure
│
└── reporting                          # Module de reporting et d'analytics
    ├── api                            # Contrôleurs REST
    ├── application                    # Services d'application
    ├── domain                         # Modèles et repositories de domaine
    └── infrastructure                 # Implémentations d'infrastructure
```

### Modèle de données

Le modèle de données est structuré autour des entités principales suivantes :

#### Entités de base

1. **Organization** : Représente une organisation cliente (assureur, courtier, agent général, etc.)
2. **OrganizationType** : Définit le type d'organisation et son niveau dans la hiérarchie assurantielle
3. **User** : Représente un utilisateur du système
4. **Role** : Représente un rôle dans le système
5. **Permission** : Représente une permission spécifique
6. **RefreshToken** : Stocke les tokens de rafraîchissement
7. **Policy** : Représente une police d'assurance
8. **Claim** : Représente un sinistre
9. **Product** : Représente un produit d'assurance
10. **ProductCategory** : Représente une catégorie de produits d'assurance (Auto, MRH, Voyage, etc.)
11. **Coverage** : Représente une garantie associée à un produit d'assurance
12. **AuditLog** : Enregistre toutes les actions effectuées dans le système

#### Relations

- Une **Organization** peut avoir plusieurs **Organizations** enfants (hiérarchie)
- Chaque **Organization** a un **OrganizationType** qui définit son niveau hiérarchique
- Un **User** peut appartenir à plusieurs **Organizations** avec des rôles différents
- Un **User** peut avoir plusieurs **Roles** dans une même organisation
- Un **Role** peut avoir plusieurs **Permissions**
- Une **Organization** de niveau supérieur peut voir les données des **Organizations** de niveau inférieur
- Une **Policy** est liée à une ou plusieurs **Organizations** selon leur rôle (assureur, courtier, etc.)
- Un **Claim** est lié à une **Policy** et peut être géré par différentes **Organizations**

## 3. Fonctionnalités par module

### Module Common

- Configuration de base de l'application
- Gestion des exceptions globales
- Utilitaires communs
- Filtres de sécurité et intercepteurs
- Mécanisme de filtrage automatique des données basé sur la hiérarchie des organisations
- Annotations personnalisées pour la gestion des permissions hiérarchiques

### Module Organization

- Gestion des organisations (CRUD)
- Gestion des types d'organisations spécifiques au secteur assurantiel
- Gestion de la hiérarchie des organisations avec visibilité descendante
- Gestion des contacts d'organisation avec types de contacts
- Gestion des paramètres d'organisation avec valeurs personnalisées
- Gestion des relations entre organisations (parent-enfant, coassurance, courtage, etc.)
- Association des utilisateurs aux organisations avec gestion des rôles spécifiques

### Module Security

- Authentification utilisateur par JWT Bearer token (login, logout, refresh token)
- Authentification des systèmes externes par API Keys avec gestion des droits granulaires
- Gestion des utilisateurs (CRUD)
- Gestion des rôles et permissions (CRUD)
- Contrôle d'accès basé sur les rôles (RBAC) avec contexte organisationnel
- Permissions spécifiques pour la visibilité hiérarchique (descendante et latérale)
- Système de cloisonnement des données par organisation avec filtrage automatique
- Système d'audit avancé

### Modules métier assurantiels

- **Gestion des polices d'assurance**
    - Création et gestion des contrats d'assurance
    - Suivi des échéances et renouvellements
    - Gestion des avenants et modifications de contrat
    - Visibilité des contrats selon le niveau hiérarchique

- **Gestion des sinistres**
    - Déclaration et suivi des sinistres
    - Workflow de traitement adapté au type d'organisation
    - Gestion des indemnisations et recours
    - Partage d'informations entre les différents intervenants

- **Gestion des produits d'assurance**
    - Configuration des produits et garanties
    - Tarification et règles de souscription
    - Gestion des formules et options
    - Personnalisation des produits par courtier/agent
    - Gestion des produits IARD spécifiques :
        - **Assurance Auto** : Gestion des véhicules, formules tous risques/tiers, bonus-malus
        - **Assurance Habitation (MRH)** : Gestion des biens, risques locatifs, valeur à neuf
        - **Assurance Voyage** : Couvertures temporaires, assistance internationale, annulation
        - **Risques Divers** : Protection juridique, responsabilité civile, assurances affinitaires

- **Reporting et analytics**
    - Tableaux de bord spécifiques par type d'organisation
    - Rapports de production et de sinistralité
    - Analyses prédictives et aide à la décision
    - Consolidation des données à différents niveaux hiérarchiques

## 4. État actuel du projet et prochaines étapes

### État actuel (référence: [doc/devbook.md](devbook.md))

Le projet dispose d'une structure de base avec :

- Une application Spring Boot 3.2.3 avec Java 21
- Une configuration de base pour PostgreSQL et Flyway
- Un fichier de migration initial (V1__init_schema.sql) avec quelques tables de base
- Une structure de projet à développer selon l'architecture hexagonale

### Réalisations actuelles (au 04/04/2025)

- Structure des packages selon l'architecture hexagonale
- Entités de base (BaseEntity, AuditableEntity, TenantAwareEntity)
- Module d'organisation:
    - Modèles: Organization, OrganizationType, OrganizationStatus, OrganizationHierarchy
    - Repositories: OrganizationRepository, OrganizationHierarchyRepository
    - Events: OrganizationCreatedEvent, OrganizationUpdatedEvent
    - Services: OrganizationService
    - Controllers: OrganizationController, OrganizationTypeController, OrganizationHierarchyController
- Module de sécurité:
    - Modèles: User, UserStatus, Role, Permission, UserOrganization, RefreshToken
    - Repositories: UserRepository, RoleRepository, PermissionRepository, RefreshTokenRepository
    - Configuration JWT: JwtTokenProvider, JwtAuthenticationFilter, JwtAuthenticationEntryPoint
    - Services: AuthenticationService, UserService
    - Controllers: AuthController, UserController
- Aspects pour les annotations (TenantRequired, Auditable)
- Service de sécurité pour les vérifications d'autorisation personnalisées
- Données initiales pour les rôles et permissions système
- Filtrage contextuel des données basé sur la hiérarchie des organisations
- Refactoring de l'architecture pour améliorer la cohérence et la maintenabilité du code

### Prochaines étapes (référence: [doc/roadmap.md](roadmap.md))

#### Phase 1: Fondation (en cours)

- Finaliser les tests unitaires pour les cas d'utilisation
- Implémenter les tests d'intégration pour les repositories
- Optimiser les performances avec une stratégie de cache

#### Phase 2: Sécurité et authentification (Sprint 3-4)

- Finaliser le système d'audit avancé
- Implémenter le système d'API Keys

#### Phase 3: Gestion des organisations assurantielles (Sprint 5-7)

- Développement complet du module organization
- Implémentation des fonctionnalités avancées
- Implémentation des relations spécifiques au secteur assurantiel

#### Phase 4: Modules métier assurantiels (Sprint 7-10)

- Implémenter le module de gestion des polices d'assurance
- Implémenter le module de gestion des sinistres
- Implémenter le module de gestion des produits d'assurance
- Implémenter les modules IARD spécifiques
- Implémenter l'intégration avec l'API Diotali pour les attestations d'assurance

## 5. Problèmes connus et améliorations à apporter

### Problèmes identifiés (référence: [doc/devbook.md](devbook.md))

- Besoin de tests unitaires et d'intégration plus complets
- Problème de requêtes N+1 à optimiser
- Absence de stratégie de cache
- Documentation et messages d'erreur à améliorer
- Validation des entrées à renforcer

### Améliorations techniques à apporter

- Optimisation des performances:
    - Implémenter une stratégie de cache avec Spring Cache et Redis
    - Optimiser les requêtes JPA avec des projections et des fetch joins
    - Mettre en place des index pour les requêtes fréquentes
- Amélioration de la qualité du code:
    - Augmenter la couverture de tests
    - Implémenter des tests de performance
    - Mettre en place des outils d'analyse statique du code
- Sécurité:
    - Renforcer la validation des entrées avec des validateurs personnalisés
    - Implémenter une protection contre les attaques CSRF et XSS
    - Mettre en place une journalisation complète des événements de sécurité

### Améliorations fonctionnelles à considérer

- Implémenter un système de notifications pour les événements importants
- Ajouter des fonctionnalités de reporting avancées
- Développer une API pour l'intégration avec des systèmes externes
- Mettre en place un système de workflow configurable pour les processus métier

## 6. Standards de code

### Nomenclature des Classes

- **Entités de persistance**: Suffixe `Entity` (ex: `UserEntity`, `OrganizationEntity`)
- **Cas d'utilisation**: Préfixe `UseCase` (ex: `UseCaseCreateUser`, `UseCaseUpdateOrganization`)
- **Repositories**: Suffixe `Repository` (ex: `UserRepository`, `OrganizationRepository`)
- **Implémentations de Repository**: Préfixe indiquant la technologie (ex: `JpaUserRepository`)
- **Services**: Suffixe `Service` (ex: `UserService`, `AuthenticationService`)
- **Contrôleurs**: Suffixe `Controller` (ex: `UserController`, `OrganizationController`)
- **DTOs**: Suffixe approprié selon usage (`Request`, `Response`, `DTO`)

### Conventions de Nommage

- CamelCase pour les noms de classes, méthodes et variables
- Noms de classes: noms ou groupes nominaux
- Noms de méthodes: commencent par un verbe
- Constantes: majuscules avec underscores

### Documentation

- En-tête de fichier avec copyright
- Documentation JavaDoc pour chaque classe et méthode publique
- Commentaires pour expliquer le "pourquoi" plutôt que le "quoi"

### Principes SOLID

- **Responsabilité Unique (SRP)**: Une classe ne doit avoir qu'une seule responsabilité
- **Ouvert/Fermé (OCP)**: Ouvert à l'extension, fermé à la modification
- **Substitution de Liskov (LSP)**: Les objets d'une classe dérivée doivent pouvoir remplacer les objets de la classe de
  base
- **Ségrégation des Interfaces (ISP)**: Interfaces spécifiques plutôt qu'une seule interface générale
- **Inversion des Dépendances (DIP)**: Dépendre des abstractions, pas des implémentations

## 6. Intégration avec l'API Diotali

Le projet prévoit une intégration avec l'API Diotali pour la gestion des attestations d'assurance automobile digitales.
Cette API permet :

- L'authentification via OAuth2
- La gestion des commandes de QR codes virtuels
- Le calcul de la RC (Responsabilité Civile) pour différents types de véhicules
- La génération d'attestations digitales avec QR codes
- L'invalidation d'attestations
- La vérification du statut des attestations

L'intégration avec cette API permettra d'offrir des fonctionnalités de digitalisation des attestations d'assurance
automobile conformes aux standards de l'Association des Assureurs.

## 7. Points d'attention et recommandations

### Architecture

- Maintenir une séparation claire des couches (hexagonale)
- Respecter les principes DDD dans chaque module
- Éviter les dépendances cycliques entre modules
- Utiliser les événements de domaine pour la communication entre modules

### Performance

- Optimiser les requêtes N+1 avec des stratégies de chargement appropriées
- Mettre en place une stratégie de cache pour les données fréquemment accédées
- Indexer correctement les tables pour les requêtes hiérarchiques

### Sécurité

- Valider toutes les entrées utilisateur
- Appliquer le principe du moindre privilège
- Journaliser toutes les actions sensibles
- Chiffrer les données sensibles au repos et en transit

### Tests

- Maintenir une couverture de tests élevée
- Tester les cas limites et les scénarios d'erreur
- Implémenter des tests d'intégration pour les repositories
- Mettre en place des tests de performance pour les opérations critiques

### Évolutivité

- Concevoir pour permettre l'évolution vers des microservices si nécessaire
- Utiliser des interfaces et des abstractions pour faciliter les changements
- Documenter les décisions d'architecture et les compromis

## 8. Environnement de développement et outils

### Prérequis techniques

- Java 17 ou supérieur
- Maven 3.8 ou supérieur
- PostgreSQL 17
- Git
- IDE recommandé: IntelliJ IDEA ou Eclipse avec plugins Spring

### Configuration de l'environnement

- Base de données: PostgreSQL avec schéma `saas`
- Variables d'environnement:
    - `SPRING_DATASOURCE_URL`: URL de connexion à la base de données
    - `SPRING_DATASOURCE_USERNAME`: Nom d'utilisateur de la base de données
    - `SPRING_DATASOURCE_PASSWORD`: Mot de passe de la base de données
    - `JWT_SECRET`: Clé secrète pour la génération des tokens JWT

### Commandes utiles

- Lancement de l'application: `mvn spring-boot:run`
- Exécution des tests: `mvn test`
- Construction du package: `mvn clean package`
- Lancement avec profil spécifique: `mvn spring-boot:run -Dspring-boot.run.profiles=dev`

### Workflow de développement

1. Créer une branche de fonctionnalité à partir de `develop`
2. Développer et tester la fonctionnalité
3. Soumettre une pull request vers `develop`
4. Revue de code et validation
5. Fusion dans `develop`
6. Intégration continue et déploiement sur l'environnement de test

### Ressources additionnelles

- Documentation Spring Boot: https://docs.spring.io/spring-boot/docs/current/reference/html/
- Documentation Spring Security: https://docs.spring.io/spring-security/reference/
- Documentation JPA: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- Documentation Flyway: https://flywaydb.org/documentation/

## 9. Conclusion

Ce document de mémoire sert de point d'entrée unique pour la compréhension et le développement du projet SaaS
Multi-organisation pour le secteur de l'assurance. Il compile toutes les informations essentielles et référence les
documents sources pour une exploration plus approfondie.

En consultant ce document, vous devriez être en mesure de :

1. Comprendre l'objectif global et l'architecture du projet
2. Identifier ce qui a déjà été réalisé et ce qui reste à faire
3. Connaître les problèmes connus et les améliorations à apporter
4. Accéder rapidement aux documents sources pour des informations détaillées
5. Configurer l'environnement de développement et commencer à contribuer au projet

Ce document sera mis à jour régulièrement pour refléter l'état actuel du projet et servir de référence fiable pour tous
les membres de l'équipe de développement.
