# Guide Développeur - Plateforme SaaS

## Table des matières

1. [Introduction](#introduction)
2. [Architecture](#architecture)
    - [Architecture Hexagonale](#architecture-hexagonale)
    - [Structure du Projet](#structure-du-projet)
    - [Diagrammes](#diagrammes)
3. [Processus de Développement](#processus-de-développement)
    - [Workflow Git](#workflow-git)
    - [Intégration Continue](#intégration-continue)
    - [Déploiement Continu](#déploiement-continu)
4. [Environnement de Développement](#environnement-de-développement)
    - [Prérequis](#prérequis)
    - [Configuration](#configuration)
    - [Compilation et Exécution](#compilation-et-exécution)
    - [Tests](#tests)
5. [Modules et Composants](#modules-et-composants)
    - [Module Common](#module-common)
    - [Module Core](#module-core)
    - [Module Insurance](#module-insurance)
6. [Base de Données](#base-de-données)
    - [Modèle de Données](#modèle-de-données)
    - [Migrations](#migrations)
    - [Accès aux Données](#accès-aux-données)
7. [API REST](#api-rest)
    - [Contrôleurs](#contrôleurs)
    - [DTOs](#dtos)
    - [Validation](#validation)
    - [Documentation (Swagger/OpenAPI)](#documentation-swaggeropenapi)
8. [Sécurité](#sécurité)
    - [Authentification](#authentification)
    - [Autorisation](#autorisation)
    - [Gestion des Utilisateurs](#gestion-des-utilisateurs)
    - [Pratiques de Sécurité](#pratiques-de-sécurité)
9. [Internationalisation (i18n)](#internationalisation-i18n)
    - [Architecture Modulaire](#architecture-modulaire)
    - [Fournisseurs de Messages](#fournisseurs-de-messages)
    - [Service de Messages](#service-de-messages)
    - [Administration des Traductions](#administration-des-traductions)
10. [Notifications](#notifications)
    - [Architecture](#architecture-des-notifications)
    - [Service de Notification](#service-de-notification)
    - [Types de Notifications](#types-de-notifications)
11. [Qualité du Code](#qualité-du-code)
    - [Standards de Qualité](#standards-de-qualité)
    - [Revue de Code](#revue-de-code)
    - [Analyse Statique](#analyse-statique)
12. [Performance](#performance)
    - [Optimisation](#optimisation)
    - [Benchmarks](#benchmarks)
    - [Profiling](#profiling)
13. [Déploiement](#déploiement)
    - [Environnements](#environnements)
    - [Stratégies de Déploiement](#stratégies-de-déploiement)
    - [Configuration](#configuration-des-environnements)
14. [Monitoring et Observabilité](#monitoring-et-observabilité)
    - [Instrumentation](#instrumentation)
    - [Outils de Monitoring](#outils-de-monitoring)
    - [Gestion des Logs](#gestion-des-logs)
15. [Gestion des Dépendances](#gestion-des-dépendances)
    - [Stratégie de Gestion](#stratégie-de-gestion)
    - [Mises à Jour de Sécurité](#mises-à-jour-de-sécurité)
    - [Vérification des Vulnérabilités](#vérification-des-vulnérabilités)
16. [Documentation du Code](#documentation-du-code)
    - [Standards](#standards-de-documentation)
    - [Génération de Documentation](#génération-de-documentation)
17. [Contribution](#contribution)
    - [Guide pour les Nouveaux Contributeurs](#guide-pour-les-nouveaux-contributeurs)
    - [Processus de Contribution](#processus-de-contribution)
18. [Scalabilité](#scalabilité)
    - [Scalabilité Horizontale et Verticale](#scalabilité-horizontale-et-verticale)
    - [Patterns de Résilience](#patterns-de-résilience)
19. [Bonnes Pratiques](#bonnes-pratiques)
    - [Conventions de Nommage](#conventions-de-nommage)
    - [Gestion des Exceptions](#gestion-des-exceptions)
    - [Logging](#logging)
    - [Tests](#tests-1)
20. [Annexes](#annexes)
    - [Glossaire](#glossaire)
    - [Références](#références)

## Introduction

Ce guide développeur est destiné aux développeurs qui travaillent sur la plateforme SaaS. Il fournit des informations
détaillées sur l'architecture, les composants, les bonnes pratiques et les conventions utilisées dans le projet.

La plateforme SaaS est une solution complète pour la gestion des produits d'assurance, conçue selon une architecture
hexagonale (ou architecture en ports et adaptateurs). Cette architecture permet une séparation claire entre la logique
métier et les détails techniques, facilitant ainsi la maintenance, l'évolution et l'extension de la plateforme.

## Architecture

### Architecture Hexagonale

La plateforme SaaS est construite selon une architecture hexagonale (ou architecture en ports et adaptateurs), qui
sépare clairement la logique métier des détails techniques. Cette architecture est organisée en trois couches
principales :

1. **Couche Domain (Domaine)** :
    - Contient la logique métier pure, indépendante de toute technologie ou framework
    - Définit les entités métier, les règles métier et les ports (interfaces) que les adaptateurs doivent implémenter
    - Ne dépend d'aucune autre couche

2. **Couche Application** :
    - Orchestre les cas d'utilisation en coordonnant les entités du domaine
    - Utilise les ports pour communiquer avec le monde extérieur
    - Contient les services d'application, les DTOs (Data Transfer Objects) et les mappers
    - Dépend uniquement de la couche Domain

3. **Couche Infrastructure** :
    - Implémente les adaptateurs qui permettent à l'application de communiquer avec le monde extérieur
    - Contient les contrôleurs REST, les repositories JPA, les implémentations de services externes, etc.
    - Dépend des couches Domain et Application

Cette architecture présente plusieurs avantages :

- **Testabilité** : La logique métier peut être testée indépendamment des détails techniques
- **Flexibilité** : Les composants techniques peuvent être remplacés sans affecter la logique métier
- **Maintenabilité** : Les responsabilités sont clairement séparées, facilitant la maintenance
- **Évolutivité** : De nouvelles fonctionnalités peuvent être ajoutées sans perturber l'existant

### Structure du Projet

La structure du projet suit l'architecture hexagonale et est organisée en modules fonctionnels :

```
src/main/java/com/devolution/saas/
├── SaasApplication.java
├── common/
│   ├── api/
│   ├── config/
│   ├── domain/
│   ├── exception/
│   ├── i18n/
│   ├── notification/
│   └── util/
├── core/
│   ├── audit/
│   ├── organization/
│   └── security/
└── insurance/
    ├── common/
    ├── life/
    └── nonlife/
        └── auto/
            ├── api/
            ├── application/
            ├── domain/
            └── infrastructure/
```

Chaque module fonctionnel suit la même structure interne, reflétant l'architecture hexagonale :

```
module/
├── api/                  # Contrôleurs REST et DTOs d'API
├── application/          # Services d'application, DTOs, mappers
│   ├── command/          # Commandes (modifications d'état)
│   ├── dto/              # DTOs (Data Transfer Objects)
│   ├── mapper/           # Mappers entre entités et DTOs
│   ├── query/            # Requêtes (lecture d'état)
│   ├── service/          # Services d'application
│   └── usecase/          # Cas d'utilisation
├── domain/               # Logique métier pure
│   ├── event/            # Événements de domaine
│   ├── model/            # Entités et objets de valeur
│   ├── port/             # Interfaces (ports)
│   │   ├── in/           # Ports d'entrée (utilisés par l'application)
│   │   └── out/          # Ports de sortie (implémentés par l'infrastructure)
│   ├── repository/       # Interfaces des repositories
│   └── service/          # Services de domaine
└── infrastructure/       # Détails techniques
    ├── adapter/          # Adaptateurs
    │   ├── in/           # Adaptateurs d'entrée
    │   └── out/          # Adaptateurs de sortie
    ├── config/           # Configuration spécifique au module
    └── persistence/      # Implémentation des repositories
```

### Diagrammes

#### Diagramme de l'Architecture Hexagonale

```
+---------------------+       +---------------------+       +---------------------+
|                     |       |                     |       |                     |
|    Presentation     |       |     Application     |       |      Domain         |
|                     |       |                     |       |                     |
|  +---------------+  |       |  +---------------+  |       |  +---------------+  |
|  |               |  |       |  |               |  |       |  |               |  |
|  | Controllers   |  |       |  | Services      |  |       |  | Entities      |  |
|  |               |  |       |  |               |  |       |  |               |  |
|  +-------+-------+  |       |  +-------+-------+  |       |  +-------+-------+  |
|          |          |       |          |          |       |          |          |
|          v          |       |          v          |       |          v          |
|  +---------------+  |       |  +---------------+  |       |  +---------------+  |
|  |               |  |       |  |               |  |       |  |               |  |
|  | DTOs          |  |       |  | Use Cases     |  |       |  | Value Objects |  |
|  |               |  |       |  |               |  |       |  |               |  |
|  +-------+-------+  |       |  +-------+-------+  |       |  +-------+-------+  |
|          |          |       |          |          |       |          |          |
|          v          |       |          v          |       |          v          |
|  +---------------+  |       |  +---------------+  |       |  +---------------+  |
|  |               |  |       |  |               |  |       |  |               |  |
|  | Mappers       +------------>+ Ports (In)    +------------>+ Domain        |  |
|  |               |  |       |  |               |  |       |  | Services      |  |
|  +---------------+  |       |  +---------------+  |       |  +---------------+  |
|                     |       |                     |       |                     |
+----------+----------+       +----------+----------+       +----------+----------+
           ^                             ^                             |
           |                             |                             |
           |                             |                             v
+----------+-----------------------------+-----------------------------+----------+
|                                                                                 |
|                                 Infrastructure                                  |
|                                                                                 |
|  +------------------+    +------------------+    +------------------+           |
|  |                  |    |                  |    |                  |           |
|  | Repositories     |    | Adapters (Out)   |    | External         |           |
|  | (JPA)            |    | (Ports Impl)     |    | Services         |           |
|  |                  |    |                  |    |                  |           |
|  +------------------+    +------------------+    +------------------+           |
|                                                                                 |
+----------------------------------------------------------------------------------+
```

## Processus de Développement

Cette section décrit le processus de développement utilisé pour la plateforme SaaS, y compris le workflow Git,
l'intégration continue et le déploiement continu.

### Workflow Git

Le projet utilise le modèle de branchement Git Flow, qui définit une structure de branches stricte conçue pour la
gestion des versions et des releases.

#### Branches principales

- **main** : Branche principale contenant le code en production. Seules les releases stables y sont fusionnées.
- **dev** : Branche de développement contenant les dernières fonctionnalités terminées. C'est à partir de cette branche
  que sont créées les branches de fonctionnalités.

#### Branches temporaires

- **feature/*** : Branches de fonctionnalités (ex: `feature/add-notification-system`). Créées à partir de `dev` et
  fusionnées dans `dev`.
- **bugfix/*** : Branches de correction de bugs (ex: `bugfix/fix-login-issue`). Créées à partir de `dev` et fusionnées
  dans `dev`.
- **release/*** : Branches de préparation de release (ex: `release/1.0.0`). Créées à partir de `dev` et fusionnées dans
  `main` et `dev`.
- **hotfix/*** : Branches de correction urgente en production (ex: `hotfix/critical-security-fix`). Créées à partir de
  `main` et fusionnées dans `main` et `dev`.

#### Processus de Pull Request

1. **Création** : Créer une Pull Request (PR) depuis votre branche vers la branche cible (généralement `dev`).
2. **Description** : Inclure une description détaillée des changements, des liens vers les tickets associés et des
   instructions de test.
3. **Revue de code** : Au moins un autre développeur doit approuver la PR avant qu'elle puisse être fusionnée.
4. **CI/CD** : Tous les tests automatiques doivent passer avant la fusion.
5. **Fusion** : Une fois approuvée et les tests passés, la PR peut être fusionnée.

#### Conventions de commit

Les messages de commit doivent suivre le format suivant :

```
<type>(<scope>): <description>

[corps]

[pied]
```

Où :

- **type** : feat (fonctionnalité), fix (correction), docs (documentation), style, refactor, test, chore (tâches
  diverses)
- **scope** : module ou composant concerné (ex: auth, notification)
- **description** : description concise du changement
- **corps** : description détaillée (optionnel)
- **pied** : références aux tickets, breaking changes (optionnel)

Exemple :

```
feat(notification): ajouter le système de notification par email

Implémentation du service d'envoi d'emails pour les notifications.
- Ajout du service EmailNotificationSender
- Intégration avec le service de notification existant
- Configuration pour différents environnements

Resolves: #123
```

### Intégration Continue

Le projet utilise GitHub Actions pour l'intégration continue (CI). Chaque push et pull request déclenche automatiquement
un pipeline CI qui effectue les opérations suivantes :

1. **Compilation** : Vérifie que le code compile correctement.
2. **Tests unitaires** : Exécute tous les tests unitaires.
3. **Tests d'intégration** : Exécute les tests d'intégration.
4. **Analyse statique** : Exécute SonarQube pour analyser la qualité du code.
5. **Vérification des dépendances** : Vérifie les vulnérabilités dans les dépendances.

Le fichier de configuration CI se trouve à `.github/workflows/ci.yml`.

### Déploiement Continu

Le déploiement continu (CD) est configuré pour déployer automatiquement le code dans différents environnements en
fonction de la branche :

1. **Développement** : Déploiement automatique à chaque push sur la branche `dev`.
2. **Test** : Déploiement manuel déclenché depuis la console CI/CD pour les branches `release/*`.
3. **Production** : Déploiement manuel déclenché depuis la console CI/CD pour la branche `main`.

Le processus de déploiement comprend les étapes suivantes :

1. **Construction** : Construction de l'application (JAR).
2. **Tests de fumée** : Vérification rapide que l'application démarre correctement.
3. **Déploiement** : Déploiement de l'application dans l'environnement cible.
4. **Tests post-déploiement** : Vérification que l'application fonctionne correctement après le déploiement.

Le fichier de configuration CD se trouve à `.github/workflows/cd.yml`.

## Environnement de Développement

### Prérequis

Pour développer sur la plateforme SaaS, vous aurez besoin des éléments suivants :

- **Java Development Kit (JDK)** : Version 21 ou supérieure
- **Gradle** : Version 8.0 ou supérieure (inclus dans le wrapper du projet)
- **PostgreSQL** : Version 14 ou supérieure
- **IDE** : IntelliJ IDEA, Eclipse ou tout autre IDE Java avec support pour Spring Boot
- **Git** : Pour la gestion de version

### Configuration

1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/anicet-kakou/SaaS.git
   cd SaaS
   ```

2. **Configuration de la base de données** :
    - Créer une base de données PostgreSQL
    - Configurer les informations de connexion dans le fichier `.env` à la racine du projet :
      ```
      DB_HOST=localhost
      DB_PORT=5432
      DB_NAME=saas_db
      DB_USERNAME=postgres
      DB_PASSWORD=postgres
      ```

3. **Configuration de l'application** :
    - Créer ou modifier le fichier `.env` à la racine du projet avec les variables d'environnement nécessaires :
      ```
      APP_NAME=SaaS
      SERVER_PORT=8080
      SPRING_PROFILES_ACTIVE=dev,jwt,oauth
      JWT_SECRET=your_jwt_secret_key
      JWT_EXPIRATION=900
      JWT_REFRESH_EXPIRATION=86400
      ```

### Compilation et Exécution

**Compilation** :

```bash
./gradlew clean build
```

**Exécution** :

```bash
./gradlew bootRun
```

**Création d'un JAR exécutable** :

```bash
./gradlew bootJar
java -jar build/libs/saas-0.0.1-SNAPSHOT.jar
```

### Tests

**Exécution des tests** :

```bash
./gradlew test
```

**Exécution des tests d'intégration** :

```bash
./gradlew integrationTest
```

**Rapport de couverture de code** :

```bash
./gradlew jacocoTestReport
```

Le rapport sera généré dans `build/reports/jacoco/test/html/index.html`.

## Modules et Composants

### Module Common

Le module Common contient les composants partagés par tous les autres modules. Il fournit les fonctionnalités de base
qui sont utilisées dans toute l'application.

#### Structure

```
common/
├── api/                  # Composants API communs
│   ├── advice/           # Gestionnaires d'exceptions globaux
│   ├── context/          # Contexte d'exécution (ex: CorrelationId)
│   ├── dto/              # DTOs communs
│   └── filter/           # Filtres HTTP
├── config/               # Configuration Spring
├── domain/               # Composants de domaine communs
│   ├── exception/        # Exceptions métier
│   └── model/            # Entités et objets de valeur communs
├── i18n/                 # Internationalisation
│   ├── admin/            # Administration des traductions
│   ├── errors/           # Messages d'erreur
│   ├── messages/         # Messages généraux
│   ├── notifications/    # Messages de notification
│   └── validation/       # Messages de validation
├── notification/         # Système de notification
│   ├── api/              # API de notification
│   ├── domain/           # Modèle de domaine des notifications
│   ├── dto/              # DTOs de notification
│   └── repository/       # Repositories de notification
└── util/                 # Classes utilitaires
```

#### Composants Principaux

**Gestion des Exceptions** :

Le module Common définit une hiérarchie d'exceptions métier et fournit un gestionnaire global d'exceptions qui convertit
ces exceptions en réponses HTTP appropriées.

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // Gestion des exceptions métier
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        // ...
    }

    // Gestion des exceptions de ressource non trouvée
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        // ...
    }

    // ...
}
```

**Internationalisation (i18n)** :

Le module Common fournit un système d'internationalisation modulaire qui permet à chaque module de gérer ses propres
fichiers de traduction.

```java
public interface MessageService {
    String getMessage(String code);
    String getMessage(String code, Object[] args);
    String getMessage(String code, Object[] args, String locale);
}

@Service
@RequiredArgsConstructor
@Slf4j
public class ModularMessageService implements MessageService {
    private final List<ModuleMessageProvider> messageProviders;

    // ...
}
```

**Notifications** :

Le module Common fournit un système de notification qui permet d'envoyer des notifications aux utilisateurs via
différents canaux.

```java
public interface NotificationService {
    String sendNotification(String userId, String notificationCode);
    String sendNotification(String userId, String notificationCode, Object[] params);
    String sendNotification(String userId, String notificationCode, Object[] params, Map<String, Object> data);
    // ...
}
```

### Module Core

Le module Core contient les fonctionnalités de base de la plateforme, telles que la sécurité, la gestion des
organisations et l'audit.

#### Structure

```
core/
├── audit/               # Audit des modifications
│   ├── api/             # API d'audit
│   ├── application/     # Services d'application d'audit
│   ├── domain/          # Modèle de domaine d'audit
│   └── infrastructure/  # Infrastructure d'audit
├── organization/        # Gestion des organisations
│   ├── api/             # API d'organisation
│   ├── application/     # Services d'application d'organisation
│   ├── domain/          # Modèle de domaine d'organisation
│   ├── i18n/            # Internationalisation spécifique aux organisations
│   └── infrastructure/  # Infrastructure d'organisation
└── security/           # Sécurité
    ├── api/             # API de sécurité
    ├── application/     # Services d'application de sécurité
    ├── domain/          # Modèle de domaine de sécurité
    ├── i18n/            # Internationalisation spécifique à la sécurité
    └── infrastructure/  # Infrastructure de sécurité
```

#### Composants Principaux

**Sécurité** :

Le module Security gère l'authentification, l'autorisation et la gestion des utilisateurs, des rôles et des permissions.

```java
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(UserDetails userDetails) {
        // ...
    }

    public String getUsernameFromToken(String token) {
        // ...
    }

    public boolean validateToken(String token) {
        // ...
    }
}
```

**Organisation** :

Le module Organization gère les organisations et la hiérarchie organisationnelle.

```java
@Entity
@Table(name = "organizations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Organization parent;

    // ...
}
```

**Audit** :

Le module Audit gère le suivi des modifications apportées aux entités.

```java
@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "organization_id")
    private UUID organizationId;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    // ...
}
```

### Module Insurance

Le module Insurance contient les fonctionnalités spécifiques à l'assurance, telles que la gestion des polices
d'assurance, des véhicules, des conducteurs, etc.

#### Structure

```
insurance/
├── common/              # Composants communs d'assurance
│   ├── application/     # Services d'application communs
│   ├── domain/          # Modèle de domaine commun
│   └── i18n/            # Internationalisation commune
├── i18n/                # Internationalisation spécifique à l'assurance
├── life/                # Assurance vie
│   └── references/      # Références d'assurance vie
└── nonlife/             # Assurance non-vie
    ├── auto/            # Assurance automobile
    │   ├── api/         # API d'assurance automobile
    │   ├── application/ # Services d'application d'assurance automobile
    │   ├── domain/      # Modèle de domaine d'assurance automobile
    │   ├── i18n/        # Internationalisation spécifique à l'assurance automobile
    │   └── infrastructure/ # Infrastructure d'assurance automobile
    └── references/      # Références d'assurance non-vie
```

#### Composants Principaux

**Assurance Automobile** :

Le module Auto gère les polices d'assurance automobile, les véhicules, les conducteurs, etc.

```java
@Entity
@Table(name = "auto_policies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoPolicy extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "premium", nullable = false)
    private BigDecimal premium;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "policy")
    private List<Driver> drivers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "policy")
    private List<Coverage> coverages;

    // ...
}
```

## Base de Données

### Modèle de Données

La plateforme SaaS utilise PostgreSQL comme système de gestion de base de données. Le modèle de données est organisé en
plusieurs tables, chacune correspondant à une entité métier.

#### Tables Principales

**Organizations** :

```sql
CREATE TABLE IF NOT EXISTS organizations
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    code                 VARCHAR(50)  NOT NULL UNIQUE,
    type                 VARCHAR(50)  NOT NULL,
    status               VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    parent_id            UUID         NULL,
    address              TEXT,
    phone                VARCHAR(20),
    email                VARCHAR(255),
    website              VARCHAR(255),
    logo_url             VARCHAR(255),
    primary_contact_name VARCHAR(255),
    description          TEXT,
    settings             JSONB,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP,
    created_by           UUID,
    updated_by           UUID,
    organization_id      UUID,
    version              BIGINT,
    active               BOOLEAN      NOT NULL DEFAULT TRUE,
    FOREIGN KEY (parent_id) REFERENCES organizations (id)
);
```

**Users** :

```sql
CREATE TABLE IF NOT EXISTS users
(
    id                    UUID PRIMARY KEY,
    username              VARCHAR(50)  NOT NULL UNIQUE,
    email                 VARCHAR(255) NOT NULL UNIQUE,
    password_hash         VARCHAR(255) NOT NULL,
    first_name            VARCHAR(100),
    last_name             VARCHAR(100),
    phone                 VARCHAR(20),
    status                VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    locked                BOOLEAN      NOT NULL DEFAULT FALSE,
    failed_login_attempts INT          NOT NULL DEFAULT 0,
    last_login_at         TIMESTAMP,
    profile_picture_url   VARCHAR(255),
    created_at            TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMP,
    created_by            UUID,
    updated_by            UUID,
    organization_id       UUID,
    version               BIGINT,
    active                BOOLEAN      NOT NULL DEFAULT TRUE
);
```

**Roles** :

```sql
CREATE TABLE IF NOT EXISTS roles
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    description     TEXT,
    organization_id UUID,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    version         BIGINT,
    system_defined  BOOLEAN     NOT NULL DEFAULT FALSE,
    active     BOOLEAN     NOT NULL DEFAULT TRUE,
    UNIQUE (name, organization_id)
);
```

**Permissions** :

```sql
CREATE TABLE IF NOT EXISTS permissions
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT,
    resource_type   VARCHAR(50)  NOT NULL,
    action          VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    system_defined  BOOLEAN      NOT NULL DEFAULT FALSE,
    UNIQUE (resource_type, action)
);
```

**Translations** :

```sql
CREATE TABLE translations (
    id BIGSERIAL PRIMARY KEY,
    module VARCHAR(50) NOT NULL,
    message_type VARCHAR(50) NOT NULL,
    message_key VARCHAR(255) NOT NULL,
    locale VARCHAR(10) NOT NULL,
    message_text TEXT NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    CONSTRAINT uk_translation UNIQUE (module, message_type, message_key, locale)
);
```

**Notifications** :

```sql
CREATE TABLE notifications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    notification_code VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(20) NOT NULL,
    data TEXT,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    is_sent BOOLEAN NOT NULL DEFAULT FALSE,
    sent_at TIMESTAMP
);
```

### Migrations

La plateforme SaaS utilise Flyway pour gérer les migrations de base de données. Les scripts de migration sont stockés
dans le répertoire `src/main/resources/db/migration` et sont exécutés automatiquement au démarrage de l'application.

```
db/migration/
├── V1__init_schema.sql       # Schéma initial
├── V2__core_application.sql  # Tables du module Core
├── V3__insurance_domain.sql  # Tables du module Insurance
└── V4__i18n_and_notifications.sql # Tables pour i18n et notifications
```

Pour créer une nouvelle migration, il suffit d'ajouter un nouveau fichier SQL dans le répertoire
`src/main/resources/db/migration` avec un nom suivant la convention `V{version}__{description}.sql`.

### Accès aux Données

La plateforme SaaS utilise Spring Data JPA pour accéder aux données. Chaque entité a un repository correspondant qui
fournit des méthodes pour effectuer des opérations CRUD (Create, Read, Update, Delete).

```java
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Optional<Organization> findByCode(String code);
    List<Organization> findByParentId(UUID parentId);
    boolean existsByCode(String code);
}
```

Pour les requêtes plus complexes, Spring Data JPA permet de définir des méthodes personnalisées en utilisant des
conventions de nommage ou des annotations `@Query`.

```java
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByOrganizationId(UUID organizationId);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.organizationId = :organizationId")
    List<User> findByStatusAndOrganizationId(@Param("status") UserStatus status, @Param("organizationId") UUID organizationId);
}
```

## API REST

### Contrôleurs

Les contrôleurs REST sont responsables de la gestion des requêtes HTTP et de la conversion des données entre le format
JSON et les objets Java. Ils sont annotés avec `@RestController` et définissent les endpoints de l'API.

```java
@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    @PreAuthorize("hasAuthority('ORGANIZATION_READ')")
    public ResponseEntity<Page<OrganizationDTO>> getAllOrganizations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) OrganizationType type,
            @RequestParam(required = false) OrganizationStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(organizationService.findAll(name, code, type, status, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZATION_READ')")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZATION_CREATE')")
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationDTO organizationDTO) {
        return new ResponseEntity<>(organizationService.create(organizationDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZATION_UPDATE')")
    public ResponseEntity<OrganizationDTO> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody OrganizationDTO organizationDTO) {
        return ResponseEntity.ok(organizationService.update(id, organizationDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZATION_DELETE')")
    public ResponseEntity<Void> deleteOrganization(@PathVariable UUID id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### DTOs

Les DTOs (Data Transfer Objects) sont utilisés pour transférer des données entre les couches de l'application et pour
exposer des données via l'API REST. Ils sont généralement des classes simples avec des getters et des setters, et
peuvent inclure des annotations de validation.

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {

    private UUID id;

    @NotBlank(message = "organization.name.required")
    @Size(min = 3, max = 255, message = "organization.name.size")
    private String name;

    @NotBlank(message = "organization.code.required")
    @Size(min = 2, max = 50, message = "organization.code.size")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "organization.code.pattern")
    private String code;

    @NotNull(message = "organization.type.required")
    private OrganizationType type;

    private OrganizationStatus status;

    private UUID parentId;

    private String address;

    private String phone;

    private String email;

    private String website;

    private String logoUrl;

    private String primaryContactName;

    private String description;

    private Map<String, Object> settings;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UUID createdBy;

    private UUID updatedBy;

    private boolean active;
}
```

### Validation

La validation des données est effectuée à l'aide des annotations de validation de Jakarta Bean Validation (anciennement
Hibernate Validator). Ces annotations sont appliquées aux champs des DTOs et sont vérifiées automatiquement par Spring
MVC lors de la réception des requêtes.

```java
public class UserDTO {

    private UUID id;

    @NotBlank(message = "user.username.required")
    @Size(min = 3, max = 50, message = "user.username.size")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "user.username.pattern")
    private String username;

    @NotBlank(message = "user.email.required")
    @Email(message = "user.email.invalid")
    private String email;

    @NotBlank(message = "user.password.required")
    @Size(min = 8, max = 100, message = "user.password.size")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "user.password.pattern")
    private String password;

    // ...
}
```

Les messages d'erreur de validation sont définis dans les fichiers de propriétés de validation et peuvent être
internationalisés.

```properties
# src/main/java/com/devolution/saas/common/i18n/validation/validation_fr.properties
user.username.required=Le nom d'utilisateur est obligatoire
user.username.size=Le nom d'utilisateur doit contenir entre {min} et {max} caractères
user.username.pattern=Le nom d'utilisateur ne peut contenir que des lettres, des chiffres, des points, des tirets et des underscores
user.email.required=L'adresse email est obligatoire
user.email.invalid=L'adresse email est invalide
user.password.required=Le mot de passe est obligatoire
user.password.size=Le mot de passe doit contenir entre {min} et {max} caractères
user.password.pattern=Le mot de passe doit contenir au moins un chiffre, une lettre minuscule, une lettre majuscule et un caractère spécial
```

### Documentation (Swagger/OpenAPI)

La documentation de l'API est générée automatiquement à l'aide de Swagger/OpenAPI. Les annotations `@Operation`,
`@ApiResponse`, `@Parameter`, etc. sont utilisées pour documenter les endpoints, les paramètres et les réponses.

```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API pour la gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    @Operation(summary = "Récupérer tous les utilisateurs", description = "Récupère la liste des utilisateurs avec pagination et filtrage")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès refusé")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @Parameter(description = "Nom d'utilisateur (optionnel)") @RequestParam(required = false) String username,
            @Parameter(description = "Email (optionnel)") @RequestParam(required = false) String email,
            @Parameter(description = "Statut (optionnel)") @RequestParam(required = false) UserStatus status,
            @Parameter(description = "Pagination et tri") Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(username, email, status, pageable));
    }

    // ...
}
```

La documentation de l'API est accessible à l'URL `/swagger-ui.html` lorsque l'application est en cours d'exécution.

## Sécurité

### Authentification

La plateforme SaaS utilise JWT (JSON Web Token) pour l'authentification. Le processus d'authentification est le
suivant :

1. L'utilisateur envoie ses identifiants (nom d'utilisateur et mot de passe) à l'endpoint `/api/v1/auth/login`.
2. Le serveur vérifie les identifiants et, s'ils sont valides, génère un token JWT et un token de rafraîchissement.
3. Le client stocke ces tokens et inclut le token JWT dans l'en-tête `Authorization` de chaque requête.
4. Le serveur vérifie la validité du token JWT pour chaque requête.
5. Lorsque le token JWT expire, le client peut utiliser le token de rafraîchissement pour obtenir un nouveau token JWT.

```java
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);
        return ResponseEntity.noContent().build();
    }
}
```

### Autorisation

L'autorisation est basée sur un système de rôles et de permissions. Chaque utilisateur peut avoir plusieurs rôles, et
chaque rôle peut avoir plusieurs permissions. Les permissions sont vérifiées à l'aide des annotations `@PreAuthorize` de
Spring Security.

```java
@GetMapping
@PreAuthorize("hasAuthority('ORGANIZATION_READ')")
public ResponseEntity<Page<OrganizationDTO>> getAllOrganizations(...) {
    // ...
}

@PostMapping
@PreAuthorize("hasAuthority('ORGANIZATION_CREATE')")
public ResponseEntity<OrganizationDTO> createOrganization(...) {
    // ...
}
```

Les permissions sont définies dans la base de données et sont chargées au démarrage de l'application. Elles suivent le
format `{ressource}:{action}`, par exemple `ORGANIZATION_READ`, `ORGANIZATION_CREATE`, etc.

### Gestion des Utilisateurs

La gestion des utilisateurs est assurée par le module Security. Les utilisateurs peuvent être créés, modifiés,
désactivés et supprimés via l'API REST.

```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(...) {
        // ...
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        // ...
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        // ...
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        // ...
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        // ...
    }
}
```

## Internationalisation (i18n)

### Architecture Modulaire

L'internationalisation (i18n) est gérée de manière modulaire, avec chaque module gérant ses propres fichiers de
traduction. Cette approche permet une meilleure organisation et facilite la maintenance des traductions.

```
i18n/
├── common/
│   ├── errors/
│   │   ├── errors_fr.properties
│   │   └── errors_en.properties
│   ├── validation/
│   │   ├── validation_fr.properties
│   │   └── validation_en.properties
│   └── notifications/
│       ├── notifications_fr.properties
│       └── notifications_en.properties
├── core/
│   ├── security/
│   │   ├── errors/
│   │   │   ├── errors_fr.properties
│   │   │   └── errors_en.properties
│   │   ├── validation/
│   │   │   ├── validation_fr.properties
│   │   │   └── validation_en.properties
│   │   └── notifications/
│   │       ├── notifications_fr.properties
│   │       └── notifications_en.properties
│   └── organization/
│       ├── errors/
│       │   ├── errors_fr.properties
│       │   └── errors_en.properties
│       └── validation/
│           ├── validation_fr.properties
│           └── validation_en.properties
└── insurance/
    ├── errors/
    │   ├── errors_fr.properties
    │   └── errors_en.properties
    ├── validation/
    │   ├── validation_fr.properties
    │   └── validation_en.properties
    └── notifications/
        ├── notifications_fr.properties
        └── notifications_en.properties
```

### Fournisseurs de Messages

Chaque module définit un fournisseur de messages qui est responsable de la gestion des fichiers de traduction du module.
Ces fournisseurs implémentent l'interface `ModuleMessageProvider`.

```java
public interface ModuleMessageProvider {
    String getModuleName();
    List<String> getMessageTypes();
    Properties getMessages(String messageType, Locale locale);
    String getMessage(String code, Object[] args, Locale locale);
    boolean canHandle(String code);
}

@Component
public class CommonMessageProvider extends AbstractModuleMessageProvider {

    private static final String MODULE_NAME = "common";
    private static final List<String> MESSAGE_TYPES = Arrays.asList("errors", "validation", "notifications");

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public List<String> getMessageTypes() {
        return MESSAGE_TYPES;
    }

    @Override
    protected String getBasePath() {
        return "com/devolution/saas/common/i18n";
    }

    @Override
    public boolean canHandle(String code) {
        // Le module Common peut gérer les codes qui commencent par "common."
        // ou qui ne commencent pas par un nom de module connu
        if (code.startsWith(MODULE_NAME + ".")) {
            return true;
        }

        // Vérifier si le code ne commence pas par un nom de module connu
        String[] knownModules = {"core", "insurance"};
        for (String module : knownModules) {
            if (code.startsWith(module + ".")) {
                return false;
            }
        }

        // Si le code ne commence pas par un nom de module connu,
        // le module Common peut le gérer
        return true;
    }
}
```

### Service de Messages

Le service de messages est responsable de la résolution des messages à partir des codes. Il délègue la résolution aux
fournisseurs de messages des modules.

```java
public interface MessageService {
    String getMessage(String code);
    String getMessage(String code, Object[] args);
    String getMessage(String code, Object[] args, String locale);
}

@Service
@RequiredArgsConstructor
@Slf4j
public class ModularMessageService implements MessageService {

    private final List<ModuleMessageProvider> messageProviders;

    @Override
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    @Override
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, null);
    }

    @Override
    public String getMessage(String code, Object[] args, String localeStr) {
        Locale locale = localeStr != null ? Locale.forLanguageTag(localeStr) : LocaleContextHolder.getLocale();

        // Chercher le fournisseur de messages qui peut gérer ce code
        for (ModuleMessageProvider provider : messageProviders) {
            if (provider.canHandle(code)) {
                String message = provider.getMessage(code, args, locale);
                if (message != null) {
                    return message;
                }
            }
        }

        // Si aucun fournisseur ne peut gérer ce code, retourner le code lui-même
        log.debug("Message non trouvé pour le code: {}, locale: {}", code, locale);
        return code;
    }
}
```

### Administration des Traductions

L'administration des traductions est assurée par un module dédié qui permet de gérer les traductions via une interface
utilisateur ou une API REST.

```java
@RestController
@RequestMapping("/api/v1/translations")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping
    @PreAuthorize("hasAuthority('TRANSLATION_READ')")
    public ResponseEntity<Page<TranslationDTO>> searchTranslations(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String messageType,
            @RequestParam(required = false) String locale,
            @RequestParam(required = false) String searchTerm,
            Pageable pageable) {
        return ResponseEntity.ok(translationService.searchTranslations(
                module, messageType, locale, searchTerm, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSLATION_READ')")
    public ResponseEntity<TranslationDTO> getTranslationById(@PathVariable Long id) {
        return ResponseEntity.ok(translationService.getTranslationById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TRANSLATION_CREATE')")
    public ResponseEntity<TranslationDTO> createTranslation(@Valid @RequestBody TranslationDTO translationDTO) {
        return new ResponseEntity<>(translationService.createTranslation(translationDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSLATION_UPDATE')")
    public ResponseEntity<TranslationDTO> updateTranslation(
            @PathVariable Long id, @Valid @RequestBody TranslationDTO translationDTO) {
        return ResponseEntity.ok(translationService.updateTranslation(id, translationDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSLATION_DELETE')")
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        translationService.deleteTranslation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('TRANSLATION_IMPORT')")
    public ResponseEntity<Integer> importTranslations(
            @RequestParam String module,
            @RequestParam String messageType,
            @RequestParam String locale,
            @RequestBody Map<String, String> properties) {
        int count = translationService.importTranslations(module, messageType, locale, properties);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/export")
    @PreAuthorize("hasAuthority('TRANSLATION_EXPORT')")
    public ResponseEntity<Map<String, String>> exportTranslations(
            @RequestParam String module,
            @RequestParam String messageType,
            @RequestParam String locale) {
        return ResponseEntity.ok(translationService.exportTranslations(module, messageType, locale));
    }

    @PostMapping("/synchronize")
    @PreAuthorize("hasAuthority('TRANSLATION_SYNCHRONIZE')")
    public ResponseEntity<Integer> synchronizeTranslations() {
        int count = translationService.synchronizeTranslations();
        return ResponseEntity.ok(count);
    }
}
```

## Notifications

### Architecture des Notifications

Le système de notification est conçu pour envoyer des notifications aux utilisateurs via différents canaux (interface
utilisateur, email, SMS, etc.). Il est composé des éléments suivants :

- **Modèle de domaine** : Entités représentant les notifications
- **Service de notification** : Service pour envoyer des notifications
- **API REST** : Endpoints pour gérer les notifications
- **Fichiers de messages** : Fichiers de propriétés contenant les messages de notification

```java
@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    /**
     * ID de l'utilisateur destinataire de la notification.
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * Code de la notification (ex: "notification.security.login.success").
     */
    @Column(name = "notification_code", nullable = false)
    private String notificationCode;

    /**
     * Message de la notification.
     */
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * Type de la notification (ex: "INFO", "WARNING", "ERROR").
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    /**
     * Données supplémentaires pour la notification.
     */
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;

    /**
     * Indique si la notification a été lue.
     */
    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    /**
     * Date de lecture de la notification.
     */
    @Column(name = "read_at")
    private LocalDateTime readAt;

    /**
     * Date de création de la notification.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Indique si la notification a été envoyée.
     */
    @Column(name = "is_sent", nullable = false)
    private boolean isSent;

    /**
     * Date d'envoi de la notification.
     */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

### Service de Notification

Le service de notification est responsable de l'envoi des notifications aux utilisateurs. Il utilise le service de
messages pour résoudre les messages de notification à partir des codes.

```java
public interface NotificationService {
    String sendNotification(String userId, String notificationCode);
    String sendNotification(String userId, String notificationCode, Object[] params);
    String sendNotification(String userId, String notificationCode, Object[] params, Map<String, Object> data);
    int sendNotificationToUsers(String[] userIds, String notificationCode);
    int sendNotificationToUsers(String[] userIds, String notificationCode, Object[] params);
    int sendNotificationToUsers(String[] userIds, String notificationCode, Object[] params, Map<String, Object> data);
    int sendNotificationToRole(String role, String notificationCode);
    int sendNotificationToRole(String role, String notificationCode, Object[] params);
    int sendNotificationToRole(String role, String notificationCode, Object[] params, Map<String, Object> data);
    int sendNotificationToAll(String notificationCode);
    int sendNotificationToAll(String notificationCode, Object[] params);
    int sendNotificationToAll(String notificationCode, Object[] params, Map<String, Object> data);
}

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final MessageService messageService;
    // Injecter ici les services nécessaires pour envoyer les notifications
    // (ex: UserService, NotificationRepository, etc.)

    @Override
    public String sendNotification(String userId, String notificationCode) {
        return sendNotification(userId, notificationCode, null, null);
    }

    @Override
    public String sendNotification(String userId, String notificationCode, Object[] params) {
        return sendNotification(userId, notificationCode, params, null);
    }

    @Override
    public String sendNotification(String userId, String notificationCode, Object[] params, Map<String, Object> data) {
        // Récupérer le message localisé
        String message = messageService.getMessage(notificationCode, params);

        // Créer la notification
        String notificationId = UUID.randomUUID().toString();

        // Enregistrer la notification dans la base de données
        // TODO: Implémenter l'enregistrement de la notification

        // Envoyer la notification à l'utilisateur
        // TODO: Implémenter l'envoi de la notification

        log.info("Notification envoyée à l'utilisateur {}: {} ({})", userId, message, notificationId);

        return notificationId;
    }

    // ...
}
```

### Types de Notifications

Les notifications peuvent être de différents types, définis par l'énumération `NotificationType` :

```java
public enum NotificationType {
    /**
     * Notification d'information.
     */
    INFO,

    /**
     * Notification de succès.
     */
    SUCCESS,

    /**
     * Notification d'avertissement.
     */
    WARNING,

    /**
     * Notification d'erreur.
     */
    ERROR,

    /**
     * Notification système.
     */
    SYSTEM
}
```

## Qualité du Code

La qualité du code est un aspect essentiel du développement de la plateforme SaaS. Cette section décrit les standards de
qualité, le processus de revue de code et les outils d'analyse statique utilisés dans le projet.

### Standards de Qualité

Les standards de qualité suivants sont appliqués à tout le code de la plateforme :

1. **Couverture de code** : Minimum 80% de couverture de code par les tests unitaires et d'intégration.
2. **Dette technique** : Maintenir la dette technique en dessous de 5% du code total.
3. **Duplication de code** : Moins de 3% de duplication de code.
4. **Complexité cyclomatique** : Maximum 15 par méthode.
5. **Longueur des méthodes** : Maximum 50 lignes par méthode.
6. **Nombre de paramètres** : Maximum 5 paramètres par méthode.
7. **Profondeur d'héritage** : Maximum 5 niveaux.
8. **Couplage afférent** : Maximum 20 par classe.
9. **Couplage efférent** : Maximum 15 par classe.
10. **Bugs potentiels** : Zéro bug potentiel de sévérité élevée ou critique.

Ces métriques sont surveillées par SonarQube et font partie des critères de validation des pull requests.

### Revue de Code

Le processus de revue de code est une étape essentielle pour maintenir la qualité du code. Voici les principes à suivre
lors des revues de code :

1. **Objectivité** : Se concentrer sur le code, pas sur la personne.
2. **Constructivité** : Fournir des commentaires constructifs et des suggestions d'amélioration.
3. **Clarté** : Être clair et précis dans les commentaires.
4. **Exhaustivité** : Examiner tous les aspects du code (fonctionnalité, performance, sécurité, lisibilité, etc.).

#### Liste de contrôle pour les revues de code

- [ ] Le code respecte-t-il les conventions de nommage et de style ?
- [ ] Le code est-il bien documenté (Javadoc, commentaires) ?
- [ ] Les tests unitaires et d'intégration sont-ils présents et pertinents ?
- [ ] Les exceptions sont-elles gérées correctement ?
- [ ] Le code est-il efficace et performant ?
- [ ] Le code est-il sécurisé (injection, XSS, CSRF, etc.) ?
- [ ] Le code est-il conforme aux principes SOLID ?
- [ ] Le code est-il facilement maintenable et extensible ?
- [ ] Les logs sont-ils présents et pertinents ?
- [ ] Les dépendances sont-elles gérées correctement ?

### Analyse Statique

L'analyse statique du code est effectuée automatiquement lors de chaque build et pull request. Les outils suivants sont
utilisés :

1. **SonarQube** : Analyse complète de la qualité du code, détection des bugs, vulnérabilités, code smells, etc.
    - Configuration : `.sonarqube/sonar-project.properties`
    - URL du serveur : `https://sonarqube.saas-platform.com`

2. **Checkstyle** : Vérification du respect des conventions de style.
    - Configuration : `config/checkstyle/checkstyle.xml`
    - Exécution : `./gradlew checkstyleMain checkstyleTest`

3. **SpotBugs** : Détection des bugs potentiels.
    - Configuration : `config/spotbugs/spotbugs.xml`
    - Exécution : `./gradlew spotbugsMain spotbugsTest`

4. **PMD** : Détection des pratiques de programmation douteuses.
    - Configuration : `config/pmd/ruleset.xml`
    - Exécution : `./gradlew pmdMain pmdTest`

5. **OWASP Dependency Check** : Vérification des vulnérabilités dans les dépendances.
    - Configuration : `config/owasp/dependency-check.xml`
    - Exécution : `./gradlew dependencyCheckAnalyze`

Les rapports générés par ces outils sont disponibles dans le répertoire `build/reports/` après l'exécution des tâches
correspondantes.

## Performance

La performance est un aspect critique de la plateforme SaaS. Cette section décrit les techniques d'optimisation, les
benchmarks et le profiling utilisés pour assurer de bonnes performances.

### Optimisation

Les techniques d'optimisation suivantes sont appliquées dans le projet :

1. **Optimisation des requêtes SQL** :
    - Utilisation d'index appropriés
    - Éviter les requêtes N+1
    - Utilisation de projections et de requêtes spécifiques
    - Pagination pour les grandes collections

2. **Mise en cache** :
    - Cache de premier niveau (cache de session Hibernate)
    - Cache de deuxième niveau (Ehcache)
    - Cache d'application (Spring Cache)
    - Cache distribué (Redis) pour les environnements multi-instances

3. **Optimisation des collections** :
    - Utilisation des structures de données appropriées
    - Éviter les itérations inutiles
    - Utilisation de Stream API pour les opérations sur les collections

4. **Optimisation des ressources** :
    - Pooling de connexions
    - Gestion efficace des threads
    - Libération rapide des ressources

5. **Lazy Loading** :
    - Chargement paresseux des relations JPA
    - Chargement à la demande des données

### Benchmarks

Les benchmarks sont utilisés pour mesurer et comparer les performances de différentes implémentations. Le projet utilise
JMH (Java Microbenchmark Harness) pour les benchmarks.

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class UserServiceBenchmark {

    private UserService userService;
    private UUID userId;

    @Setup
    public void setup() {
        // Initialisation du benchmark
        userService = new UserServiceImpl(...);
        userId = UUID.randomUUID();
        // ...
    }

    @Benchmark
    public UserDTO findUserById() {
        return userService.findById(userId);
    }

    @Benchmark
    public Page<UserDTO> findAllUsers() {
        return userService.findAll(null, null, null, PageRequest.of(0, 10));
    }
}
```

Exécution des benchmarks :

```bash
./gradlew jmh
```

Les résultats des benchmarks sont disponibles dans le répertoire `build/reports/jmh/`.

### Profiling

Le profiling est utilisé pour identifier les goulots d'étranglement et les problèmes de performance. Les outils suivants
sont utilisés :

1. **VisualVM** : Profiling de la JVM, analyse de la mémoire, des threads, etc.
    - Téléchargement : [https://visualvm.github.io/](https://visualvm.github.io/)
    - Utilisation : `visualvm --jdkhome /path/to/jdk`

2. **JProfiler** : Profiling avancé de la JVM, analyse des performances, etc.
    -
    Téléchargement : [https://www.ej-technologies.com/products/jprofiler/overview.html](https://www.ej-technologies.com/products/jprofiler/overview.html)
    - Utilisation : Voir la documentation de JProfiler

3. **Spring Boot Actuator** : Monitoring des performances de l'application Spring Boot.
    - Configuration : `application.properties` ou `application.yml`
    - Endpoints : `/actuator/metrics`, `/actuator/health`, etc.

4. **Micrometer** : Collecte de métriques de performance.
    - Configuration : `application.properties` ou `application.yml`
    - Intégration avec Prometheus, Grafana, etc.

Les règles suivantes doivent être respectées pour les performances :

1. **Temps de réponse** : 95% des requêtes doivent être traitées en moins de 500 ms.
2. **Débit** : L'application doit pouvoir traiter au moins 100 requêtes par seconde.
3. **Utilisation de la mémoire** : L'application ne doit pas utiliser plus de 2 Go de mémoire.
4. **Utilisation du CPU** : L'application ne doit pas utiliser plus de 70% du CPU en charge normale.

## Déploiement

### Environnements

La plateforme SaaS est déployée dans plusieurs environnements, chacun ayant un objectif spécifique :

1. **Développement** : Environnement utilisé par les développeurs pour tester leurs modifications.
    - URL : `https://dev-api.saas-platform.com`
    - Base de données : Instance PostgreSQL dédiée
    - Déploiement : Automatique à chaque push sur la branche `dev`

2. **Test** : Environnement utilisé pour les tests d'intégration et les tests fonctionnels.
    - URL : `https://test-api.saas-platform.com`
    - Base de données : Instance PostgreSQL dédiée
    - Déploiement : Manuel depuis la console CI/CD

3. **Pré-production** : Environnement identique à la production, utilisé pour les tests finaux.
    - URL : `https://preprod-api.saas-platform.com`
    - Base de données : Instance PostgreSQL dédiée
    - Déploiement : Manuel depuis la console CI/CD

4. **Production** : Environnement utilisé par les clients.
    - URL : `https://api.saas-platform.com`
    - Base de données : Cluster PostgreSQL avec réplication
    - Déploiement : Manuel depuis la console CI/CD

### Stratégies de Déploiement

Les stratégies de déploiement suivantes sont utilisées :

1. **Blue/Green Deployment** : Deux environnements identiques (bleu et vert) sont maintenus. Le trafic est redirigé de
   l'environnement actif vers l'environnement inactif après le déploiement.
    - Avantages : Zéro temps d'arrêt, rollback facile
    - Inconvénients : Coût plus élevé (deux environnements)

2. **Canary Deployment** : Le déploiement est effectué progressivement, en commençant par un petit pourcentage
   d'utilisateurs.
    - Avantages : Risque réduit, détection précoce des problèmes
    - Inconvénients : Complexité accrue, durée de déploiement plus longue

3. **Rolling Deployment** : Le déploiement est effectué progressivement sur les différentes instances de l'application.
    - Avantages : Risque réduit, pas besoin d'infrastructure supplémentaire
    - Inconvénients : Durée de déploiement plus longue, complexité accrue

### Configuration des Environnements

La configuration des environnements est gérée par Spring Profiles et des variables d'environnement. Les fichiers de
configuration sont les suivants :

1. **application.yml** : Configuration commune à tous les environnements.
2. **application-dev.yml** : Configuration spécifique à l'environnement de développement.
3. **application-test.yml** : Configuration spécifique à l'environnement de test.
4. **application-preprod.yml** : Configuration spécifique à l'environnement de pré-production.
5. **application-prod.yml** : Configuration spécifique à l'environnement de production.

Les variables d'environnement sont utilisées pour les informations sensibles (mots de passe, clés API, etc.) et sont
définies dans le système d'orchestration (Kubernetes, Docker Swarm, etc.).

## Monitoring et Observabilité

### Instrumentation

L'instrumentation est utilisée pour collecter des données sur le comportement de l'application. Les techniques suivantes
sont utilisées :

1. **Logs** : Utilisation de SLF4J avec Logback pour les logs.
    - Configuration : `logback-spring.xml`
    - Niveaux de log : ERROR, WARN, INFO, DEBUG, TRACE

2. **Métriques** : Utilisation de Micrometer pour collecter des métriques.
    - Configuration : `application.yml`
    - Types de métriques : compteurs, jauges, histogrammes, etc.

3. **Traces** : Utilisation de Spring Cloud Sleuth et Zipkin pour le traçage distribué.
    - Configuration : `application.yml`
    - Visualisation : Zipkin UI

4. **Health Checks** : Utilisation de Spring Boot Actuator pour les vérifications de santé.
    - Configuration : `application.yml`
    - Endpoints : `/actuator/health`, `/actuator/info`, etc.

### Outils de Monitoring

Les outils suivants sont utilisés pour le monitoring :

1. **Prometheus** : Collecte et stockage des métriques.
    - Configuration : `prometheus.yml`
    - URL : `https://prometheus.saas-platform.com`

2. **Grafana** : Visualisation des métriques.
    - Configuration : `grafana.yml`
    - URL : `https://grafana.saas-platform.com`
    - Dashboards : Performance, JVM, API, Business, etc.

3. **Zipkin** : Traçage distribué.
    - Configuration : `zipkin.yml`
    - URL : `https://zipkin.saas-platform.com`

4. **Alertmanager** : Gestion des alertes.
    - Configuration : `alertmanager.yml`
    - Intégration : Slack, Email, PagerDuty, etc.

### Gestion des Logs

Les logs sont centralisés et analysés à l'aide de la stack ELK (Elasticsearch, Logstash, Kibana) :

1. **Elasticsearch** : Stockage et indexation des logs.
    - Configuration : `elasticsearch.yml`
    - URL : `https://elasticsearch.saas-platform.com`

2. **Logstash** : Collecte et transformation des logs.
    - Configuration : `logstash.conf`
    - Pipelines : Parsing, enrichissement, filtrage, etc.

3. **Kibana** : Visualisation et analyse des logs.
    - Configuration : `kibana.yml`
    - URL : `https://kibana.saas-platform.com`
    - Dashboards : Erreurs, Avertissements, Activité utilisateur, etc.

4. **Filebeat** : Collecte des logs des applications.
    - Configuration : `filebeat.yml`
    - Intégration avec Logstash

## Gestion des Dépendances

### Stratégie de Gestion

La gestion des dépendances est un aspect important de la maintenance du projet. Les principes suivants sont appliqués :

1. **Minimisation des dépendances** : N'ajouter que les dépendances nécessaires.
2. **Versions explicites** : Spécifier explicitement les versions des dépendances.
3. **Gestion centralisée** : Utiliser `gradle/libs.versions.toml` pour centraliser les versions des dépendances.
4. **Analyse des dépendances** : Utiliser `./gradlew dependencies` pour analyser les dépendances.
5. **Résolution des conflits** : Résoudre les conflits de dépendances de manière explicite.

### Mises à Jour de Sécurité

Les mises à jour de sécurité sont traitées en priorité. Le processus suivant est appliqué :

1. **Surveillance** : Utiliser Dependabot pour surveiller les vulnérabilités.
2. **Évaluation** : Évaluer l'impact et la gravité de chaque vulnérabilité.
3. **Planification** : Planifier les mises à jour en fonction de la gravité.
4. **Test** : Tester les mises à jour dans un environnement de test.
5. **Déploiement** : Déployer les mises à jour en production.

### Vérification des Vulnérabilités

Les vulnérabilités dans les dépendances sont vérifiées à l'aide des outils suivants :

1. **OWASP Dependency Check** : Vérification des vulnérabilités connues.
    - Configuration : `config/owasp/dependency-check.xml`
    - Exécution : `./gradlew dependencyCheckAnalyze`

2. **Snyk** : Vérification des vulnérabilités et des licences.
    - Configuration : `.snyk`
    - Exécution : `snyk test`

3. **GitHub Dependabot** : Surveillance automatique des vulnérabilités.
    - Configuration : `.github/dependabot.yml`
    - Intégration avec GitHub

## Documentation du Code

### Standards de Documentation

La documentation du code suit les standards suivants :

1. **Javadoc** : Documentation des classes, méthodes, champs, etc.
    - Classes : Description, auteur, version, etc.
    - Méthodes : Description, paramètres, retour, exceptions, etc.
    - Champs : Description, usage, etc.

2. **Commentaires** : Commentaires dans le code pour expliquer les parties complexes.
    - Algorithmes complexes
    - Logique métier
    - Workarounds et hacks

3. **README** : Documentation générale du projet.
    - Description
    - Installation
    - Configuration
    - Utilisation
    - Contribution

4. **Diagrammes** : Diagrammes UML, diagrammes de séquence, etc.
    - Utilisation de PlantUML
    - Intégration avec la documentation

### Génération de Documentation

La documentation est générée à l'aide des outils suivants :

1. **Javadoc** : Génération de la documentation API.
    - Exécution : `./gradlew javadoc`
    - Résultat : `build/docs/javadoc/`

2. **Asciidoctor** : Génération de la documentation technique.
    - Exécution : `./gradlew asciidoctor`
    - Résultat : `build/docs/asciidoc/`

3. **Swagger/OpenAPI** : Génération de la documentation API REST.
    - Configuration : `springdoc-openapi.properties`
    - URL : `/swagger-ui.html`

## Contribution

### Guide pour les Nouveaux Contributeurs

Ce guide est destiné aux nouveaux contributeurs qui souhaitent participer au développement de la plateforme SaaS.

1. **Prérequis** :
    - Connaissance de Java et Spring Boot
    - Connaissance de Git
    - Environnement de développement configuré

2. **Premiers pas** :
    - Cloner le dépôt
    - Configurer l'environnement de développement
    - Exécuter l'application
    - Exécuter les tests

3. **Contribution** :
    - Choisir une tâche à réaliser
    - Créer une branche
    - Implémenter la fonctionnalité ou corriger le bug
    - Exécuter les tests
    - Soumettre une pull request

### Processus de Contribution

Le processus de contribution est le suivant :

1. **Choix d'une tâche** :
    - Consulter les issues ouvertes
    - Discuter avec l'équipe
    - Créer une nouvelle issue si nécessaire

2. **Développement** :
    - Créer une branche à partir de `dev`
    - Implémenter la fonctionnalité ou corriger le bug
    - Exécuter les tests
    - S'assurer que le code respecte les standards de qualité

3. **Soumission** :
    - Créer une pull request
    - Décrire les changements
    - Lier la pull request à l'issue correspondante

4. **Revue** :
    - Attendre la revue de code
    - Répondre aux commentaires
    - Apporter les modifications demandées

5. **Fusion** :
    - Une fois la pull request approuvée, elle sera fusionnée dans `dev`
    - La branche sera supprimée

## Scalabilité

### Scalabilité Horizontale et Verticale

La plateforme SaaS est conçue pour être scalable horizontalement et verticalement :

1. **Scalabilité horizontale** :
    - Ajout de nouvelles instances de l'application
    - Utilisation d'un équilibreur de charge
    - Sessions sans état (stateless)
    - Cache distribué

2. **Scalabilité verticale** :
    - Augmentation des ressources (CPU, mémoire, etc.)
    - Optimisation des performances
    - Utilisation efficace des ressources

### Patterns de Résilience

Les patterns de résilience suivants sont utilisés pour améliorer la robustesse de l'application :

1. **Circuit Breaker** : Utilisation de Resilience4j pour empêcher les appels à des services défaillants.
    - Configuration : `application.yml`
    - Implémentation : Annotations `@CircuitBreaker`, `@Retry`, etc.

2. **Bulkhead** : Isolation des ressources pour éviter la propagation des défaillances.
    - Configuration : `application.yml`
    - Implémentation : Annotation `@Bulkhead`

3. **Timeout** : Définition de délais d'attente pour les opérations.
    - Configuration : `application.yml`
    - Implémentation : Annotation `@Timeout`

4. **Retry** : Réessai des opérations en cas d'échec.
    - Configuration : `application.yml`
    - Implémentation : Annotation `@Retry`

5. **Rate Limiter** : Limitation du nombre de requêtes.
    - Configuration : `application.yml`
    - Implémentation : Annotation `@RateLimiter`

## Bonnes Pratiques

### Conventions de Nommage

La plateforme SaaS suit les conventions de nommage suivantes :

**Classes** :

- Noms en PascalCase (ex: `OrganizationService`, `UserController`)
- Noms significatifs qui décrivent clairement la responsabilité de la classe
- Suffixes courants : `Controller`, `Service`, `Repository`, `DTO`, `Entity`, `Exception`, etc.

**Méthodes** :

- Noms en camelCase (ex: `findById`, `createUser`)
- Verbes qui décrivent l'action effectuée par la méthode
- Préfixes courants pour les méthodes de repository : `findBy`, `existsBy`, `countBy`, etc.

**Variables** :

- Noms en camelCase (ex: `userId`, `organizationName`)
- Noms significatifs qui décrivent clairement le contenu de la variable

**Constantes** :

- Noms en SNAKE_CASE_MAJUSCULE (ex: `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE`)

**Packages** :

- Noms en minuscules, séparés par des points (ex: `com.devolution.saas.common.util`)
- Structure hiérarchique qui reflète l'architecture de l'application

**Endpoints REST** :

- URLs en kebab-case (ex: `/api/v1/organizations`, `/api/v1/users/{id}/roles`)
- Noms au pluriel pour les collections (ex: `/organizations`, `/users`)
- Verbes HTTP appropriés (GET, POST, PUT, DELETE, PATCH)

### Gestion des Exceptions

La plateforme SaaS utilise une hiérarchie d'exceptions métier pour gérer les erreurs de manière cohérente :

```java
public class BusinessException extends RuntimeException {
    private final String code;
    private final Object[] args;

    public BusinessException(String code) {
        this(code, null, null);
    }

    public BusinessException(String code, String message) {
        this(code, message, null);
    }

    public BusinessException(String code, String message, Object[] args) {
        super(message != null ? message : code);
        this.code = code;
        this.args = args;
    }

    // ...
}

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resourceName, Object id) {
        super(
            "resource.not.found",
            String.format("Resource %s with id %s not found", resourceName, id),
            new Object[]{resourceName, id}
        );
    }

    // ...
}

public class ResourceAlreadyExistsException extends BusinessException {
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(
            "resource.already.exists",
            String.format("Resource %s with %s %s already exists", resourceName, fieldName, fieldValue),
            new Object[]{resourceName, fieldName, fieldValue}
        );
    }

    // ...
}
```

Les exceptions métier sont capturées par le gestionnaire global d'exceptions (`GlobalExceptionHandler`) qui les
convertit en réponses HTTP appropriées.

### Logging

La plateforme SaaS utilise SLF4J avec Logback pour le logging. Les bonnes pratiques de logging sont les suivantes :

- Utiliser les niveaux de log appropriés (ERROR, WARN, INFO, DEBUG, TRACE)
- Inclure des informations contextuelles dans les messages de log
- Utiliser des placeholders (`{}`) plutôt que la concaténation de chaînes
- Inclure les exceptions dans les messages de log

```java
@Slf4j
public class UserService {
    public UserDTO findById(UUID id) {
        log.debug("Recherche de l'utilisateur avec l'ID: {}", id);
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", id));
            log.debug("Utilisateur trouvé: {}", user.getUsername());
            return userMapper.toDTO(user);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche de l'utilisateur avec l'ID: {}", id, e);
            throw e;
        }
    }
}
```

### Tests

La plateforme SaaS utilise JUnit 5 et Mockito pour les tests. Les bonnes pratiques de test sont les suivantes :

- Écrire des tests unitaires pour chaque classe
- Utiliser des mocks pour isoler la classe testée
- Utiliser des assertions expressives
- Suivre le pattern AAA (Arrange, Act, Assert)

```java
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());

        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(userId));

        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }
}
```

## Annexes

### Glossaire

- **API** : Application Programming Interface, interface de programmation d'application
- **DTO** : Data Transfer Object, objet de transfert de données
- **JPA** : Java Persistence API, API de persistance Java
- **JWT** : JSON Web Token, jeton web JSON
- **REST** : Representational State Transfer, transfert d'état représentationnel
- **CRUD** : Create, Read, Update, Delete, créer, lire, mettre à jour, supprimer
- **i18n** : Internationalisation
- **l10n** : Localisation
- **DDD** : Domain-Driven Design, conception pilotée par le domaine
- **TDD** : Test-Driven Development, développement piloté par les tests
- **CI/CD** : Continuous Integration/Continuous Deployment, intégration continue/déploiement continu

### Références

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [JWT Documentation](https://jwt.io/introduction)
- [Swagger/OpenAPI Documentation](https://swagger.io/docs/)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
