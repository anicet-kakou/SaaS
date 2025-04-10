# Plan d'implémentation du produit Assurance Automobile - Version 2.0

**Version :** 2.0.0  
**Date de dernière mise à jour :** 2023-12-01  
**Statut :** Document de travail  
**Classification :** Confidentiel - Usage interne uniquement

## 1. Introduction

Ce document est une évolution du plan d'implémentation initial du produit d'assurance automobile. Il intègre une analyse
de cohérence des modèles de données et propose une structure de tâches et sous-tâches pour faciliter le suivi de la mise
en œuvre.

### 1.1 Objectifs

- Assurer la cohérence des modèles de données du produit d'assurance automobile
- Structurer le plan d'implémentation sous forme de tâches et sous-tâches
- Faciliter le suivi de l'avancement du projet
- Garantir l'alignement avec les principes SOLID et les bonnes pratiques du domaine assurantiel

### 1.2 Portée

Ce document couvre l'implémentation complète du produit d'assurance automobile, y compris :

- La gestion des véhicules et leurs caractéristiques
- Les formules tous risques/tiers
- Le système de bonus-malus
- Les garanties spécifiques à l'automobile
- La tarification automobile

## 2. Analyse de cohérence des modèles de données

### 2.1 Analyse des tables de référence spécifiques aux véhicules

#### 2.1.1 Constatations

1. **Tables présentes dans le plan initial :**
    - Tables de référence générales définies dans le document `plan-commun.md` (professions, types de carburant,
      couleurs de véhicules, etc.)
    - Tables de référence spécifiques à l'automobile définies dans le document `plan-auto.md` :
        - Catégories de véhicules (`vehicle_categories`)
        - Sous-catégories de véhicules (`vehicle_subcategories`)
        - Marques de véhicules (`vehicle_makes`)
        - Modèles de véhicules (`vehicle_models`)
        - Types d'usage des véhicules (`vehicle_usages`)
        - Zones géographiques (`geographic_zones`)
        - Carrosseries (`vehicle_body_types`)
        - Genres de véhicules (`vehicle_genres`)
        - Zones de circulation (`circulation_zones`)

2. **Incohérences identifiées :**
    - Duplication conceptuelle entre `geographic_zones` (section 3.1.6) et `circulation_zones` (section 4.1.3)
    - Incohérence de nommage : utilisation de `tariff_coefficient` dans `geographic_zones` vs `risk_coefficient` dans
      `circulation_zones` pour des concepts similaires
    - Absence de coefficient de risque dans les tables `vehicle_body_types` et `vehicle_genres` alors qu'elles impactent
      la tarification
    - Manque d'harmonisation dans la structure des tables de référence
    - Incohérence dans la gestion des coefficients tarifaires entre les différentes tables

#### 2.1.2 Recommandations

1. **Harmonisation des noms de tables :**
    - Adopter une convention de nommage cohérente avec le préfixe `vehicle_` pour toutes les tables de référence liées
      aux véhicules
    - Renommer `circulation_zones` en `vehicle_circulation_zones` pour maintenir la cohérence
    - Conserver les tables existantes (`vehicle_categories`, `vehicle_subcategories`, etc.) en assurant leur cohérence
      avec les nouvelles tables

2. **Consolidation des zones géographiques :**
    - Fusionner les concepts de `geographic_zones` (section 3.1.6) et `circulation_zones` (section 4.1.3) en une seule
      table
    - Utiliser `vehicle_geographic_zones` comme nom standardisé
    - Harmoniser les coefficients en utilisant systématiquement `risk_coefficient`
    - Assurer la compatibilité avec les références existantes dans la table `vehicles`
    - Préserver les données et les relations existantes lors de la migration

3. **Standardisation des colonnes :**
    - Utiliser systématiquement `risk_coefficient` pour les coefficients liés au risque
    - Utiliser systématiquement `tariff_coefficient` pour les coefficients liés au tarif
    - Ajouter un coefficient de risque aux tables `vehicle_body_types` et `vehicle_genres`
    - Maintenir la cohérence des types de données et des contraintes
    - Assurer la compatibilité avec le système de calcul de prime existant

## 3. Plan d'implémentation structuré

### 3.1 Tâche : Préparation de l'environnement de développement

#### 3.1.1 Sous-tâche : Configuration du projet

- Créer la structure de packages selon l'architecture définie
- Configurer les dépendances Maven/Gradle
- Mettre en place les environnements de développement, test et production

#### 3.1.2 Sous-tâche : Mise en place des outils de qualité

- Configurer les outils d'analyse statique de code
- Mettre en place les tests automatisés
- Configurer l'intégration continue

### 3.2 Tâche : Implémentation du modèle de données

#### 3.2.1 Sous-tâche : Création des tables de référence

- Implémenter les entités et repositories pour les catégories de véhicules
- Implémenter les entités et repositories pour les marques et modèles
- Implémenter les entités et repositories pour les types d'usage

#### 3.2.2 Sous-tâche : Création des tables spécifiques aux véhicules

- Implémenter l'entité et repository pour les carrosseries (`vehicle_body_types`)
- Implémenter l'entité et repository pour les genres de véhicules (`vehicle_genres`)
- Implémenter l'entité et repository pour les zones de circulation (`vehicle_circulation_zones`)

#### 3.2.3 Sous-tâche : Création des tables principales

- Implémenter l'entité et repository pour les véhicules
- Implémenter l'entité et repository pour le bonus-malus
- Implémenter l'entité et repository pour les conducteurs
- Implémenter l'entité et repository pour les polices auto

### 3.3 Tâche : Implémentation des services métier

#### 3.3.1 Sous-tâche : Services de gestion des véhicules

- Implémenter le service de création et mise à jour des véhicules
- Implémenter le service de validation des données véhicules
- Implémenter les règles métier spécifiques aux véhicules

#### 3.3.2 Sous-tâche : Services de gestion du bonus-malus

- Implémenter le calculateur de bonus-malus
- Implémenter les règles d'évolution du coefficient
- Implémenter le service de mise à jour annuelle

#### 3.3.3 Sous-tâche : Services de tarification

- Implémenter le calculateur de prime de base
- Implémenter les règles de majoration/réduction
- Implémenter le service de simulation tarifaire

### 3.4 Tâche : Implémentation des API

#### 3.4.1 Sous-tâche : API de gestion des véhicules

- Implémenter les endpoints CRUD pour les véhicules
- Implémenter les validations et transformations de données
- Documenter l'API avec Swagger/OpenAPI

#### 3.4.2 Sous-tâche : API de gestion des polices

- Implémenter les endpoints CRUD pour les polices auto
- Implémenter les endpoints de gestion des conducteurs
- Implémenter les endpoints de gestion des garanties

#### 3.4.3 Sous-tâche : API de tarification

- Implémenter les endpoints de calcul de prime
- Implémenter les endpoints de simulation
- Documenter l'API avec Swagger/OpenAPI

### 3.5 Tâche : Intégrations externes

#### 3.5.1 Sous-tâche : Intégration avec DIOTALI

- Implémenter l'adaptateur pour l'API DIOTALI
- Mettre en place les mécanismes d'authentification
- Implémenter les mappings de données

#### 3.5.2 Sous-tâche : Intégration avec les services nationaux

- Implémenter l'intégration avec le service d'immatriculation
- Implémenter l'intégration avec le service de permis de conduire
- Implémenter l'intégration avec la base de données des sinistres

### 3.6 Tâche : Tests et validation

#### 3.6.1 Sous-tâche : Tests unitaires

- Développer les tests unitaires pour les services métier
- Développer les tests unitaires pour les repositories
- Atteindre une couverture de code minimale de 80%

#### 3.6.2 Sous-tâche : Tests d'intégration

- Développer les tests d'intégration pour les API
- Développer les tests d'intégration pour les services externes
- Valider les flux complets de bout en bout

#### 3.6.3 Sous-tâche : Tests de performance

- Réaliser des tests de charge sur les API critiques
- Optimiser les requêtes de base de données
- Valider les temps de réponse pour les calculs de tarification

### 3.7 Tâche : Déploiement et mise en production

#### 3.7.1 Sous-tâche : Préparation de l'environnement de production

- Configurer l'infrastructure de production
- Mettre en place les mécanismes de sauvegarde
- Configurer le monitoring et les alertes

#### 3.7.2 Sous-tâche : Migration des données

- Développer les scripts de migration
- Tester la migration sur un environnement de pré-production
- Planifier et exécuter la migration en production

#### 3.7.3 Sous-tâche : Mise en production

- Déployer l'application en production
- Réaliser les tests de validation post-déploiement
- Former les utilisateurs finaux

## 4. Modèle de données révisé

### 4.1 Tables de référence spécifiques aux véhicules

#### 4.1.1 Carrosseries (vehicle_body_types)

```sql
CREATE TABLE vehicle_body_types
(
    id              UUID PRIMARY KEY,
    code            VARCHAR(20)  NOT NULL,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL DEFAULT 1.0,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID         NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);
```

#### 4.1.2 Genres de véhicules (vehicle_genres)

```sql
CREATE TABLE vehicle_genres
(
    id              UUID PRIMARY KEY,
    code            VARCHAR(20)  NOT NULL,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL DEFAULT 1.0,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID         NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);
```

#### 4.1.3 Zones géographiques de circulation (vehicle_geographic_zones)

```sql
CREATE TABLE vehicle_geographic_zones
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)   NOT NULL,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL,
    is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID,
    organization_id  UUID          NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);
```

#### 4.1.4 Modification de la table vehicles

```sql
ALTER TABLE vehicles
    ADD COLUMN body_type_id UUID NOT NULL REFERENCES vehicle_body_types (id),
    ADD COLUMN genre_id UUID NOT NULL REFERENCES vehicle_genres (id),
    ADD COLUMN geographic_zone_id UUID NOT NULL REFERENCES vehicle_geographic_zones (id);
```

### 4.2 Données de référence initiales et migration

#### 4.2.1 Migration des données existantes

La migration des données existantes sera effectuée en plusieurs étapes :

1. Ajout des colonnes de coefficient de risque aux tables existantes
2. Mise à jour des données existantes avec des coefficients de risque appropriés
3. Création de la nouvelle table `vehicle_geographic_zones`
4. Migration des données de `geographic_zones` et `circulation_zones` vers `vehicle_geographic_zones`
5. Mise à jour des références dans la table `vehicles`

```sql
-- 1. Ajout des colonnes de coefficient de risque
ALTER TABLE vehicle_body_types ADD COLUMN risk_coefficient DECIMAL(5, 2) NOT NULL DEFAULT 1.0;
ALTER TABLE vehicle_genres ADD COLUMN risk_coefficient DECIMAL(5, 2) NOT NULL DEFAULT 1.0;

-- 2. Mise à jour des données existantes (exemple)
UPDATE vehicle_body_types SET risk_coefficient = 1.00 WHERE code = 'BERLINE';
UPDATE vehicle_body_types SET risk_coefficient = 1.05 WHERE code = 'BREAK';
UPDATE vehicle_body_types SET risk_coefficient = 1.10 WHERE code = 'SUV';
UPDATE vehicle_body_types SET risk_coefficient = 1.15 WHERE code = 'PICKUP';
UPDATE vehicle_body_types SET risk_coefficient = 1.20 WHERE code = 'FOURGON';
```

#### 4.2.2 Carrosseries standard (après migration)

```sql
INSERT INTO vehicle_body_types (id, code, name, description, risk_coefficient, organization_id)
VALUES 
    (uuid_generate_v4(), 'BERLINE', 'Berline', 'Véhicule à coffre fermé', 1.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'BREAK', 'Break', 'Véhicule familial à grand volume', 1.05, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'SUV', 'SUV', 'Sport Utility Vehicle', 1.10, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'PICKUP', 'Pick-up', 'Véhicule utilitaire avec benne', 1.15, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'FOURGON', 'Fourgon', 'Véhicule utilitaire fermé', 1.20, '00000000-0000-0000-0000-000000000000');
```

#### 4.2.2 Genres de véhicules standard

```sql
INSERT INTO vehicle_genres (id, code, name, description, risk_coefficient, organization_id)
VALUES 
    (uuid_generate_v4(), 'VP', 'Véhicule Particulier', 'Voiture de tourisme', 1.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'CTTE', 'Camionnette', 'Véhicule utilitaire léger', 1.15, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'CAM', 'Camion', 'Véhicule de transport de marchandises', 1.30, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'TRR', 'Tracteur Routier', 'Véhicule de traction', 1.40, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'MOTO', 'Motocyclette', 'Deux-roues motorisé', 1.50, '00000000-0000-0000-0000-000000000000');
```

#### 4.2.3 Zones géographiques de circulation standard

```sql
INSERT INTO vehicle_geographic_zones (id, code, name, description, risk_coefficient, organization_id)
VALUES 
    (uuid_generate_v4(), 'ZONE1', 'Zone urbaine principale', 'Capitale et grandes villes', 1.30, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'ZONE2', 'Zone urbaine secondaire', 'Villes moyennes', 1.15, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'ZONE3', 'Zone semi-urbaine', 'Petites villes et périphéries', 1.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'ZONE4', 'Zone rurale', 'Villages et zones rurales', 0.90, '00000000-0000-0000-0000-000000000000');
```

### 4.3 Impact sur la tarification

Ces tables de référence ont un impact direct sur la tarification des polices auto, en cohérence avec le système de
tarification défini dans la version 1 du document :

- Le genre du véhicule influence le calcul de la prime de base via son coefficient de risque
- La carrosserie peut entraîner des majorations ou réductions selon le risque associé
- La zone géographique de circulation applique un coefficient multiplicateur sur la prime
- Les catégories et sous-catégories de véhicules continuent d'appliquer leurs coefficients tarifaires
- Les types d'usage des véhicules conservent leur impact sur la tarification

Le service de tarification (`AutoPricingService`) devra prendre en compte ces paramètres dans son calcul selon la
formule complète :

```
Prime = PrimeBase * CoefCategorie * CoefSousCategorie * CoefGenre * CoefCarrosserie * CoefZoneGéographique * CoefUsage * CoefBonusMalus
```

Cette formule étend celle définie dans la version 1 en intégrant les nouveaux coefficients de risque tout en préservant
la compatibilité avec les barèmes de tarification existants (`tariff_scales`).

## 5. Implémentation des entités et repositories

### 5.1 Intégration dans l'architecture existante

Les nouvelles entités et modifications s'intégreront dans l'architecture hexagonale définie dans la version 1 du
document. Pour limiter les risques d'erreurs d'implémentation, voici l'arborescence complète incluant tous les
composants concernés :

```
com.devolution.saas.product.auto
├── application                    # Couche application (cas d'utilisation)
│   ├── dto                        # Objets de transfert de données
│   │   ├── vehicle                # DTOs spécifiques aux véhicules
│   │   │   ├── VehicleBodyTypeDto.java
│   │   │   ├── VehicleGenreDto.java
│   │   │   ├── VehicleGeographicZoneDto.java
│   │   │   └── VehicleDto.java    # DTO existant à mettre à jour
│   │   └── pricing                # DTOs liés à la tarification
│   │       └── PricingParametersDto.java # Mise à jour pour inclure les nouveaux coefficients
│   ├── mapper                     # Convertisseurs entre entités et DTOs
│   │   ├── VehicleBodyTypeMapper.java
│   │   ├── VehicleGenreMapper.java
│   │   ├── VehicleGeographicZoneMapper.java
│   │   └── VehicleMapper.java     # Mapper existant à mettre à jour
│   ├── service                    # Services applicatifs
│   │   ├── VehicleBodyTypeService.java
│   │   ├── VehicleGenreService.java
│   │   ├── VehicleGeographicZoneService.java
│   │   ├── VehicleService.java    # Service existant à mettre à jour
│   │   └── AutoPricingService.java # Service de tarification à mettre à jour
│   └── validator                  # Validateurs de données
│       ├── VehicleBodyTypeValidator.java
│       ├── VehicleGenreValidator.java
│       └── VehicleGeographicZoneValidator.java
├── domain                         # Couche domaine (cœur métier)
│   ├── model                      # Modèles de domaine
│   │   ├── vehicle                # Entités liées aux véhicules
│   │   │   ├── VehicleBodyType.java   # Nouvelle entité pour les carrosseries
│   │   │   ├── VehicleGenre.java      # Nouvelle entité pour les genres de véhicules
│   │   │   ├── VehicleGeographicZone.java # Nouvelle entité pour les zones géographiques
│   │   │   └── Vehicle.java       # Entité existante à mettre à jour
│   │   ├── policy                 # Entités liées aux polices d'assurance
│   │   │   └── AutoPolicy.java    # Entité existante
│   │   └── pricing                # Entités liées à la tarification
│   │       ├── BonusMalus.java    # Entité existante
│   │       └── TariffScale.java   # Entité existante
│   ├── repository                 # Interfaces des repositories
│   │   ├── VehicleBodyTypeRepository.java
│   │   ├── VehicleGenreRepository.java
│   │   ├── VehicleGeographicZoneRepository.java
│   │   ├── VehicleRepository.java # Repository existant
│   │   └── TariffScaleRepository.java # Repository existant
│   └── service                    # Services de domaine
│       ├── PricingCalculationService.java # Service de calcul à mettre à jour
│       └── RiskEvaluationService.java    # Service d'évaluation du risque
└── infrastructure                 # Couche infrastructure
    ├── persistence                # Implémentation de la persistance
    │   ├── repository             # Implémentations des repositories
    │   │   ├── VehicleBodyTypeRepositoryImpl.java
    │   │   ├── VehicleGenreRepositoryImpl.java
    │   │   ├── VehicleGeographicZoneRepositoryImpl.java
    │   │   └── VehicleRepositoryImpl.java # Implémentation existante
    │   └── entity                 # Entités JPA (si différentes des entités de domaine)
    │       ├── VehicleBodyTypeEntity.java
    │       ├── VehicleGenreEntity.java
    │       └── VehicleGeographicZoneEntity.java
    ├── api                        # API REST
    │   ├── controller             # Contrôleurs REST
    │   │   ├── VehicleBodyTypeController.java
    │   │   ├── VehicleGenreController.java
    │   │   ├── VehicleGeographicZoneController.java
    │   │   ├── VehicleController.java # Contrôleur existant
    │   │   └── PricingController.java # Contrôleur existant
    │   └── advice                 # Gestionnaires d'exceptions
    │       └── ApiExceptionHandler.java
    ├── config                     # Configuration
    │   └── AutoModuleConfig.java  # Configuration du module auto
    └── integration                # Intégrations externes
        ├── diotali                # Intégration avec DIOTALI
        │   └── DiotaliVehicleClient.java # Client existant à mettre à jour
        └── national               # Intégrations avec services nationaux
            ├── RegistrationServiceClient.java
            └── DriverLicenseServiceClient.java
```

Cette structure complète permet de visualiser l'ensemble des composants à créer ou à modifier pour intégrer les
nouvelles entités dans l'architecture existante. Les principales modifications concernent :

1. **Création des nouvelles entités de domaine** : `VehicleBodyType`, `VehicleGenre`, `VehicleGeographicZone`
2. **Mise à jour de l'entité `Vehicle`** pour inclure les références aux nouvelles entités
3. **Création des repositories** correspondants aux nouvelles entités
4. **Mise à jour du service de tarification** (`AutoPricingService`) pour prendre en compte les nouveaux coefficients de
   risque
5. **Création des DTOs, mappers et validateurs** pour les nouvelles entités
6. **Création des contrôleurs REST** pour exposer les nouvelles fonctionnalités

L'architecture suit le principe de la séparation des préoccupations avec trois couches principales :

- **Domain** : contient la logique métier et les entités du domaine
- **Application** : orchestre les cas d'utilisation en utilisant les services du domaine
- **Infrastructure** : fournit les implémentations techniques (persistance, API, intégrations)

### 5.2 Entités de domaine

#### 5.2.1 Entité VehicleBodyType

```java
package com.devolution.saas.product.auto.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité représentant les types de carrosserie de véhicules.
 * Cette entité étend le modèle initial en ajoutant un coefficient de risque
 * qui sera utilisé dans le calcul de la prime d'assurance.
 */
@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "vehicle_body_types")
public class VehicleBodyType {
    
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    /**
     * Coefficient de risque associé à ce type de carrosserie.
     * Ce coefficient est utilisé dans le calcul de la prime d'assurance.
     */
    @Column(nullable = false)
    private BigDecimal riskCoefficient;
    
    @Column(nullable = false)
    private boolean isActive = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private UUID createdBy;
    
    private UUID updatedBy;
    
    @Column(nullable = false)
    private UUID organizationId;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

#### 5.1.2 Entité VehicleGenre

```java
package com.devolution.saas.product.auto.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "vehicle_genres")
public class VehicleGenre {
    
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
    private BigDecimal riskCoefficient;
    
    @Column(nullable = false)
    private boolean isActive = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private UUID createdBy;
    
    private UUID updatedBy;
    
    @Column(nullable = false)
    private UUID organizationId;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

#### 5.1.3 Entité VehicleGeographicZone

```java
package com.devolution.saas.product.auto.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "vehicle_geographic_zones")
public class VehicleGeographicZone {
    
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
    private BigDecimal riskCoefficient;
    
    @Column(nullable = false)
    private boolean isActive = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private UUID createdBy;
    
    private UUID updatedBy;
    
    @Column(nullable = false)
    private UUID organizationId;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

## 6. Conclusion

Cette version 2.0 du plan d'implémentation du produit d'assurance automobile s'appuie sur les fondations solides
établies dans la version 1.0 tout en apportant une meilleure cohérence dans la structure des données et une organisation
plus claire des tâches à réaliser. Les principales améliorations sont :

1. **Analyse approfondie de l'existant** avec une prise en compte détaillée des tables et structures définies dans la
   version 1.0
2. **Harmonisation des noms de tables et de colonnes** pour une meilleure cohérence et maintenabilité
3. **Consolidation des concepts redondants** (zones géographiques et zones de circulation) tout en préservant la
   compatibilité avec l'existant
4. **Structuration du plan en tâches et sous-tâches** pour faciliter le suivi de l'avancement
5. **Ajout de coefficients de risque standardisés** pour toutes les tables de référence impactant la tarification
6. **Implémentation d'entités de domaine** respectant les principes SOLID et s'intégrant dans l'architecture existante
7. **Plan de migration des données** assurant une transition fluide vers la nouvelle structure

Ces améliorations permettront une implémentation plus efficace et maintenable du produit d'assurance automobile, tout en
facilitant le suivi de l'avancement du projet et en garantissant la continuité avec les développements déjà réalisés
dans la version 1.0.
