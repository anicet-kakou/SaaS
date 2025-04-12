# Document d'Architecture (ADR) - Module Auto

## 1. Introduction

Ce document décrit l'architecture du module Auto du système d'assurance SaaS. Il présente les décisions architecturales
prises, les patterns utilisés et les justifications de ces choix.

### 1.1 Objectif du module

Le module Auto a pour objectif de gérer les fonctionnalités liées à l'assurance automobile, notamment:

- La gestion des véhicules
- La gestion des conducteurs
- La gestion des polices d'assurance automobile
- La gestion des produits d'assurance auto
- Le calcul des primes d'assurance
- La gestion du bonus-malus

### 1.2 Contexte

Ce module s'intègre dans une plateforme SaaS d'assurance plus large, permettant aux compagnies d'assurance de gérer
leurs produits et leurs clients. Le module Auto est l'un des modules métier de cette plateforme, aux côtés d'autres
modules comme Habitation, Santé, etc.

## 2. Vue d'ensemble de l'architecture

### 2.1 Style architectural

Le module Auto suit une **architecture hexagonale** (également connue sous le nom d'architecture en ports et adaptateurs
ou architecture en oignon). Cette architecture permet de:

- Isoler le domaine métier des détails techniques
- Faciliter les tests unitaires
- Permettre le remplacement des composants techniques sans impacter le domaine

### 2.2 Structure des packages

```
com.devolution.saas.insurance.nonlife.auto
├── api                     # Couche API (contrôleurs REST, requêtes, réponses)
├── application             # Couche application (services, DTOs, mappers, exceptions)
│   ├── command             # Commandes pour les opérations d'écriture
│   ├── dto                 # Objets de transfert de données
│   ├── exception           # Exceptions métier
│   ├── mapper              # Mappers entre entités et DTOs
│   ├── service             # Services d'application
│   └── usecase             # Cas d'utilisation spécifiques
├── domain                  # Couche domaine (entités, repositories, services de domaine)
│   ├── model               # Entités du domaine
│   ├── repository          # Interfaces des repositories
│   └── service             # Services de domaine
├── infrastructure          # Couche infrastructure (implémentations techniques)
│   └── persistence         # Implémentations JPA des repositories
└── reference               # Données de référence (sous-module)
    ├── api                 # API pour les données de référence
    ├── application         # Services pour les données de référence
    ├── domain              # Entités et repositories pour les données de référence
    └── infrastructure      # Implémentations pour les données de référence
```

## 3. Décisions architecturales

### ADR-001: Architecture Hexagonale

**Contexte:**
Nous avions besoin d'une architecture qui permette une séparation claire des préoccupations et qui facilite les tests.

**Décision:**
Nous avons choisi l'architecture hexagonale pour isoler le domaine métier des détails techniques.

**Statut:** Accepté

**Conséquences:**

- Positives:
    - Séparation claire des préoccupations
    - Facilité pour les tests unitaires
    - Indépendance du domaine vis-à-vis des technologies
- Négatives:
    - Plus de code à écrire (interfaces, implémentations)
    - Courbe d'apprentissage pour les nouveaux développeurs

### ADR-002: Utilisation de Spring Data JPA

**Contexte:**
Nous avions besoin d'une solution de persistance pour les entités du domaine.

**Décision:**
Nous avons choisi Spring Data JPA pour la persistance des données.

**Statut:** Accepté

**Conséquences:**

- Positives:
    - Réduction du code boilerplate pour les opérations CRUD
    - Support des requêtes dérivées des noms de méthodes
    - Intégration avec l'écosystème Spring
- Négatives:
    - Risque de couplage avec JPA dans le domaine
    - Risque de problèmes de performance avec des requêtes complexes

### ADR-003: DTOs et Mappers

**Contexte:**
Nous avions besoin de séparer les entités du domaine des objets exposés par l'API.

**Décision:**
Nous avons choisi d'utiliser des DTOs (Data Transfer Objects) et des mappers pour convertir entre les entités et les
DTOs.

**Statut:** Accepté

**Conséquences:**

- Positives:
    - Séparation claire entre le domaine et l'API
    - Contrôle sur les données exposées
    - Évolution indépendante du domaine et de l'API
- Négatives:
    - Code supplémentaire à maintenir
    - Risque d'incohérence entre les entités et les DTOs

### ADR-004: Validation avec Jakarta Bean Validation

**Contexte:**
Nous avions besoin de valider les données d'entrée de l'API.

**Décision:**
Nous avons choisi Jakarta Bean Validation pour la validation des données d'entrée.

**Statut:** Accepté

**Conséquences:**

- Positives:
    - Validation déclarative avec des annotations
    - Messages d'erreur personnalisables
    - Intégration avec Spring MVC
- Négatives:
    - Validation limitée aux contraintes simples
    - Nécessité de validations métier supplémentaires dans les services

### ADR-005: Gestion des exceptions

**Contexte:**
Nous avions besoin d'une gestion cohérente des erreurs dans l'API.

**Décision:**
Nous avons créé des exceptions métier personnalisées et un gestionnaire global d'exceptions.

**Statut:** Accepté

**Conséquences:**

- Positives:
    - Réponses d'erreur cohérentes
    - Séparation entre les exceptions techniques et métier
    - Facilité pour les tests
- Négatives:
    - Code supplémentaire à maintenir
    - Risque d'incohérence dans la gestion des erreurs

### ADR-006: Multi-organisation

**Contexte:**
Le système doit supporter plusieurs organisations (compagnies d'assurance) sur la même instance.

**Décision:**
Nous avons ajouté un champ organizationId à toutes les entités et filtré toutes les requêtes par ce champ.

**Statut:** Accepté

**Conséquences:**

- Positives:
    - Support natif du multi-organisation
    - Isolation des données entre organisations
- Négatives:
    - Complexité supplémentaire dans les requêtes
    - Risque d'erreurs si le filtrage est oublié

## 4. Modèle de domaine

### 4.1 Entités principales

#### Vehicle

Représente un véhicule assuré avec ses caractéristiques (immatriculation, marque, modèle, etc.).

#### Driver

Représente un conducteur avec ses informations (permis, expérience, etc.).

#### AutoPolicy

Représente une police d'assurance automobile.

#### BonusMalus

Représente le coefficient de bonus-malus d'un client.

#### AutoInsuranceProduct

Représente un produit d'assurance auto.

### 4.2 Données de référence

Le module contient également de nombreuses entités de référence:

- VehicleCategory/Subcategory: Catégories et sous-catégories de véhicules
- VehicleMake/Model: Marques et modèles de véhicules
- FuelType: Types de carburant
- VehicleUsage: Types d'usage de véhicule
- GeographicZone/CirculationZone: Zones géographiques et de circulation

### 4.3 Relations entre entités

```
AutoPolicy ---> Vehicle
AutoPolicy ---> Driver
Vehicle ---> VehicleMake
Vehicle ---> VehicleModel
Vehicle ---> VehicleCategory
Vehicle ---> VehicleSubcategory
Vehicle ---> FuelType
Vehicle ---> VehicleUsage
Vehicle ---> GeographicZone
BonusMalus ---> Customer (module externe)
```

## 5. API REST

### 5.1 Principes de conception

L'API REST du module Auto suit les principes suivants:

- Utilisation des verbes HTTP (GET, POST, PUT, DELETE)
- Utilisation des codes de statut HTTP appropriés
- Utilisation des formats JSON pour les requêtes et réponses
- Validation des données d'entrée
- Gestion cohérente des erreurs

### 5.2 Endpoints principaux

#### Véhicules

- `GET /api/v1/organizations/{organizationId}/vehicles`: Liste tous les véhicules
- `GET /api/v1/organizations/{organizationId}/vehicles/{id}`: Récupère un véhicule par son ID
- `POST /api/v1/organizations/{organizationId}/vehicles`: Crée un nouveau véhicule
- `PUT /api/v1/organizations/{organizationId}/vehicles/{id}`: Met à jour un véhicule
- `DELETE /api/v1/organizations/{organizationId}/vehicles/{id}`: Supprime un véhicule

#### Conducteurs

- `GET /api/v1/organizations/{organizationId}/drivers`: Liste tous les conducteurs
- `GET /api/v1/organizations/{organizationId}/drivers/{id}`: Récupère un conducteur par son ID
- `POST /api/v1/organizations/{organizationId}/drivers`: Crée un nouveau conducteur
- `PUT /api/v1/organizations/{organizationId}/drivers/{id}`: Met à jour un conducteur
- `DELETE /api/v1/organizations/{organizationId}/drivers/{id}`: Supprime un conducteur

#### Polices d'assurance

- `GET /api/v1/organizations/{organizationId}/auto-policies`: Liste toutes les polices
- `GET /api/v1/organizations/{organizationId}/auto-policies/{id}`: Récupère une police par son ID
- `POST /api/v1/organizations/{organizationId}/auto-policies`: Crée une nouvelle police
- `PUT /api/v1/organizations/{organizationId}/auto-policies/{id}`: Met à jour une police
- `DELETE /api/v1/organizations/{organizationId}/auto-policies/{id}`: Supprime une police

#### Produits d'assurance

- `GET /api/v1/organizations/{organizationId}/auto-insurance-products`: Liste tous les produits
- `GET /api/v1/organizations/{organizationId}/auto-insurance-products/{id}`: Récupère un produit par son ID
- `POST /api/v1/organizations/{organizationId}/auto-insurance-products`: Crée un nouveau produit
- `PUT /api/v1/organizations/{organizationId}/auto-insurance-products/{id}`: Met à jour un produit
- `DELETE /api/v1/organizations/{organizationId}/auto-insurance-products/{id}`: Supprime un produit

## 6. Persistence

### 6.1 Technologie

Le module utilise JPA (Java Persistence API) avec Hibernate comme implémentation et Spring Data JPA comme couche
d'abstraction.

### 6.2 Stratégie de mapping

- Chaque entité du domaine est mappée à une table dans la base de données
- Les relations entre entités sont mappées avec des clés étrangères
- Les énumérations sont mappées en tant que chaînes de caractères (EnumType.STRING)
- Les identifiants sont des UUID générés côté application

### 6.3 Optimisations

- Utilisation de requêtes JPQL optimisées pour les cas d'utilisation spécifiques
- Transactions en lecture seule pour les opérations de lecture
- Pagination pour les méthodes retournant des listes (à implémenter)

## 7. Sécurité

### 7.1 Authentification et autorisation

Le module s'appuie sur le système d'authentification et d'autorisation global de la plateforme (à implémenter).

### 7.2 Validation des données

- Validation des données d'entrée avec Jakarta Bean Validation
- Validations métier supplémentaires dans les services

### 7.3 Protection contre les attaques

- Protection contre les injections SQL via JPA
- Protection CSRF à implémenter
- Protection XSS à implémenter

## 8. Évolutivité et maintenance

### 8.1 Stratégie de versionnement

- Versionnement sémantique pour le module
- Versionnement de l'API via le chemin (/api/v1/...)

### 8.2 Tests

- Tests unitaires pour les services et repositories (à implémenter)
- Tests d'intégration pour les API REST (à implémenter)
- Tests de performance (à implémenter)

### 8.3 Monitoring

- Logging avec SLF4J
- Métriques avec Spring Actuator (à implémenter)
- Tracing avec Spring Cloud Sleuth (à implémenter)

## 9. Intégrations

### 9.1 Intégrations internes

Le module Auto s'intègre avec d'autres modules de la plateforme:

- Module Client pour les informations client
- Module Facturation pour la facturation des primes
- Module Sinistre pour la gestion des sinistres

### 9.2 Intégrations externes

Le module pourrait s'intégrer avec des systèmes externes:

- Services d'immatriculation pour la vérification des véhicules
- Services de tarification pour le calcul des primes
- Services de géolocalisation pour les zones géographiques

## 10. Glossaire

- **Véhicule**: Objet assuré dans une police d'assurance automobile
- **Conducteur**: Personne autorisée à conduire le véhicule assuré
- **Police**: Contrat d'assurance entre l'assureur et l'assuré
- **Bonus-Malus**: Coefficient appliqué à la prime d'assurance en fonction de l'historique de sinistres
- **Prime**: Montant payé par l'assuré pour la couverture d'assurance
- **Produit d'assurance**: Offre commerciale d'assurance avec des garanties spécifiques

## 11. Annexes

### 11.1 Diagramme de classes simplifié

```
+----------------+       +----------------+       +----------------+
|    Vehicle     |<------+   AutoPolicy   +------>|     Driver     |
+----------------+       +----------------+       +----------------+
| id             |       | id             |       | id             |
| registrationNo |       | policyNumber   |       | licenseNumber  |
| makeId         |       | vehicleId      |       | customerId     |
| modelId        |       | primaryDriverId|       | licenseTypeId  |
| ...            |       | ...            |       | ...            |
+----------------+       +----------------+       +----------------+
        |                        |                        |
        v                        v                        v
+----------------+       +----------------+       +----------------+
| VehicleMake    |       |  BonusMalus    |       | LicenseType   |
+----------------+       +----------------+       +----------------+
| id             |       | id             |       | id             |
| code           |       | customerId     |       | code           |
| name           |       | coefficient    |       | name           |
| ...            |       | ...            |       | ...            |
+----------------+       +----------------+       +----------------+
```

### 11.2 Diagramme de séquence pour la création d'une police

```
Client                  API                 Service              Repository
  |                      |                     |                     |
  | POST /auto-policies  |                     |                     |
  |--------------------->|                     |                     |
  |                      | createPolicy()      |                     |
  |                      |-------------------->|                     |
  |                      |                     | validatePolicy()    |
  |                      |                     |-------------------->|
  |                      |                     | save()              |
  |                      |                     |-------------------->|
  |                      |                     |                     |
  |                      |                     |<--------------------|
  |                      |<--------------------|                     |
  |<---------------------|                     |                     |
  |                      |                     |                     |
```

### 11.3 Références

- [Architecture Hexagonale](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design](https://domainlanguage.com/ddd/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [REST API Design](https://restfulapi.net/)
