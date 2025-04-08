# Plan d'implémentation du produit Assurance Habitation (MRH)

**Version :** 1.0.0  
**Date de dernière mise à jour :** 2023-11-15  
**Statut :** Document de travail  
**Classification :** Confidentiel - Usage interne uniquement

## 1. Introduction

Ce document décrit le plan d'implémentation du produit d'assurance habitation (Multirisque Habitation - MRH) dans le
cadre du projet SaaS multi-tenant assurantiel. Il détaille l'architecture, les modèles de données, les API et les règles
métier spécifiques à ce produit.

### 1.1 Objectifs

- Définir l'architecture spécifique au produit d'assurance habitation
- Détailler les modèles de données propres à ce produit
- Spécifier les API et endpoints nécessaires
- Documenter les règles métier spécifiques à l'assurance habitation
- Établir un plan d'implémentation clair pour l'équipe de développement

### 1.2 Portée

Ce document couvre l'implémentation complète du produit d'assurance habitation, y compris :

- La gestion des biens immobiliers
- Les risques locatifs
- La valeur à neuf
- Les garanties spécifiques à l'habitation
- La tarification habitation

## 2. Architecture et structure du module

### 2.1 Structure du module

Le module d'assurance habitation suivra la structure commune définie dans le document `plan-commun.md`, avec les
spécificités suivantes :

```
com.devolution.saas.product.home
├── api                            # Contrôleurs REST spécifiques à l'habitation
│   ├── PropertyController.java    # API de gestion des biens immobiliers
│   ├── HomeInsuranceController.java # API de gestion des polices habitation
│   └── ValuationController.java   # API d'évaluation des biens
├── application                    # Services d'application
│   ├── service                    # Implémentations des services
│   │   ├── HomePricingService.java # Service de tarification habitation
│   │   └── PropertyService.java   # Service de gestion des biens
│   ├── dto                        # DTOs spécifiques à l'habitation
│   │   ├── PropertyDTO.java       # DTO pour les biens immobiliers
│   │   └── ValuationDTO.java      # DTO pour l'évaluation des biens
│   ├── command                    # Commandes spécifiques à l'habitation
│   │   ├── CreatePropertyCommand.java # Création de bien immobilier
│   │   └── UpdateValuationCommand.java # Mise à jour de l'évaluation
│   └── usecase                    # Cas d'utilisation spécifiques à l'habitation
│       ├── CalculateHomePremium.java # Calcul de prime habitation
│       └── AssessPropertyValue.java # Évaluation de la valeur du bien
├── domain                         # Modèles et repositories de domaine
│   ├── model                      # Modèles spécifiques à l'habitation
│   │   ├── Property.java          # Entité Bien immobilier
│   │   ├── Valuation.java         # Entité Évaluation
│   │   ├── HomePolicy.java        # Entité Police Habitation
│   │   └── OccupancyType.java     # Entité Type d'occupation
│   ├── repository                 # Repositories spécifiques à l'habitation
│   │   ├── PropertyRepository.java # Repository pour les biens
│   │   └── ValuationRepository.java # Repository pour les évaluations
│   └── service                    # Services de domaine spécifiques à l'habitation
│       └── PropertyValuationService.java # Service d'évaluation des biens
└── infrastructure                 # Implémentations d'infrastructure
    └── persistence                # Implémentations de persistence
        ├── JpaPropertyRepository.java # Implémentation JPA du repository bien
        └── JpaValuationRepository.java # Implémentation JPA du repository évaluation
```

### 2.2 Intégration avec les composants communs

Le module d'assurance habitation implémentera les interfaces communes définies dans le document `plan-commun.md` :

```java
// Implémentation de l'interface InsuranceProduct pour l'assurance habitation
public class HomeInsuranceProduct implements InsuranceProduct {
    // Implémentation des méthodes de l'interface
    @Override
    public BigDecimal calculatePremium(PolicyCalculationContext context) {
        // Logique spécifique au calcul de prime habitation
        // Prise en compte du type de bien, de la surface, de la localisation, etc.
    }

    @Override
    public boolean validateSubscription(SubscriptionContext context) {
        // Validation spécifique à l'assurance habitation
        // Vérification de la propriété, des mesures de sécurité, etc.
    }

    // Autres méthodes de l'interface
}
```

## 3. Modèle de données

### 3.1 Entités spécifiques à l'assurance habitation

#### 3.1.1 Bien immobilier

```sql
CREATE TABLE properties
(
    id                   UUID PRIMARY KEY,
    address              TEXT           NOT NULL,
    postal_code          VARCHAR(10)    NOT NULL,
    city                 VARCHAR(100)   NOT NULL,
    country              VARCHAR(50)    NOT NULL DEFAULT 'France',
    property_type        VARCHAR(20)    NOT NULL, -- HOUSE, APARTMENT, BUILDING, etc.
    construction_year    INT,
    living_area          DECIMAL(10, 2) NOT NULL, -- en m²
    total_area           DECIMAL(10, 2),          -- en m²
    number_of_rooms      INT            NOT NULL,
    number_of_floors     INT,
    has_basement         BOOLEAN        NOT NULL DEFAULT FALSE,
    has_garage           BOOLEAN        NOT NULL DEFAULT FALSE,
    has_garden           BOOLEAN        NOT NULL DEFAULT FALSE,
    has_pool             BOOLEAN        NOT NULL DEFAULT FALSE,
    has_alarm_system     BOOLEAN        NOT NULL DEFAULT FALSE,
    construction_quality VARCHAR(20),             -- STANDARD, LUXURY, BASIC
    roof_type            VARCHAR(20),             -- TILE, SLATE, METAL, etc.
    heating_type         VARCHAR(20),             -- GAS, ELECTRIC, OIL, etc.
    owner_id             UUID           NOT NULL,
    created_at           TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP,
    created_by           UUID,
    updated_by           UUID,
    organization_id      UUID           NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES customers (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.1.2 Évaluation du bien

```sql
CREATE TABLE valuations
(
    id               UUID PRIMARY KEY,
    property_id      UUID           NOT NULL,
    valuation_date   DATE           NOT NULL,
    building_value   DECIMAL(19, 2) NOT NULL,
    contents_value   DECIMAL(19, 2) NOT NULL,
    valuation_method VARCHAR(20)    NOT NULL, -- MARKET, RECONSTRUCTION, DECLARED
    valuation_source VARCHAR(20)    NOT NULL, -- EXPERT, AUTOMATED, CUSTOMER
    expiry_date      DATE,
    notes            TEXT,
    created_at       TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID,
    organization_id  UUID           NOT NULL,
    FOREIGN KEY (property_id) REFERENCES properties (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.1.3 Occupation

```sql
CREATE TABLE occupancies
(
    id                  UUID PRIMARY KEY,
    property_id         UUID        NOT NULL,
    occupancy_type      VARCHAR(20) NOT NULL, -- OWNER, TENANT, LANDLORD
    is_main_residence   BOOLEAN     NOT NULL DEFAULT TRUE,
    is_furnished        BOOLEAN     NOT NULL DEFAULT FALSE,
    number_of_occupants INT         NOT NULL DEFAULT 1,
    start_date          DATE        NOT NULL,
    end_date            DATE,
    rent_amount         DECIMAL(19, 2),       -- Si location
    security_deposit    DECIMAL(19, 2),       -- Si location
    created_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          UUID,
    updated_by          UUID,
    organization_id     UUID        NOT NULL,
    FOREIGN KEY (property_id) REFERENCES properties (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.1.4 Police habitation

```sql
CREATE TABLE home_policies
(
    policy_id                 UUID PRIMARY KEY,
    property_id               UUID      NOT NULL,
    occupancy_id              UUID      NOT NULL,
    building_coverage_amount  DECIMAL(19, 2),
    contents_coverage_amount  DECIMAL(19, 2),
    liability_coverage_amount DECIMAL(19, 2),
    is_new_for_old            BOOLEAN   NOT NULL DEFAULT FALSE,
    has_legal_protection      BOOLEAN   NOT NULL DEFAULT FALSE,
    has_school_insurance      BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at                TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMP,
    created_by                UUID,
    updated_by                UUID,
    organization_id           UUID      NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (property_id) REFERENCES properties (id),
    FOREIGN KEY (occupancy_id) REFERENCES occupancies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

### 3.2 Garanties spécifiques à l'assurance habitation

```sql
-- Insertion des garanties spécifiques à l'assurance habitation
INSERT INTO coverages (id, code, name, description, coverage_type, is_optional, default_limit, default_deductible,
                       organization_id)
VALUES (uuid_generate_v4(), 'HOME_FIRE', 'Incendie', 'Couvre les dommages causés par un incendie', 'DAMAGE', FALSE,
        NULL, 150.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'HOME_WATER', 'Dégâts des eaux',
        'Couvre les dommages causés par l\'eau', 'DAMAGE', FALSE, NULL, 150.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_THEFT', 'Vol et vandalisme', 'Couvre le vol et les actes de vandalisme', 'DAMAGE', TRUE, NULL, 300.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_GLASS', 'Bris de glace', 'Couvre les dommages aux vitres et miroirs', 'DAMAGE', TRUE, 5000.00, 100.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_NATURAL', 'Catastrophes naturelles', 'Couvre les dommages causés par des catastrophes naturelles', 'DAMAGE', FALSE, NULL, 380.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_LIABILITY', 'Responsabilité civile', 'Couvre les dommages causés à des tiers', 'LIABILITY', FALSE, 5000000.00, 0.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_LEGAL', 'Protection juridique', 'Assistance juridique en cas de litige', 'LEGAL', TRUE, 10000.00, 0.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_ELECTRICAL', 'Dommages électriques', 'Couvre les dommages aux appareils électriques', 'DAMAGE', TRUE, 5000.00, 150.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_NEWOLD', 'Valeur à neuf', 'Remboursement en valeur à neuf', 'ENHANCEMENT', TRUE, NULL, 0.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_SCHOOL', 'Assurance scolaire', 'Couvre les enfants à l\'école', 'PERSONAL', TRUE,
        50000.00, 0.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'HOME_ASSISTANCE', 'Assistance',
        'Services d\'assistance en cas de sinistre', 'SERVICE', TRUE, NULL, 0.00, '00000000-0000-0000-0000-000000000000');
```

### 3.3 Formules spécifiques à l'assurance habitation

```sql
-- Insertion des formules spécifiques à l'assurance habitation
INSERT INTO formulas (id, code, name, description, product_id, organization_id)
VALUES (uuid_generate_v4(), 'HOME_BASIC', 'Formule Essentielle',
        'Couverture de base pour l\'habitation', '[PRODUCT_ID]', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_COMFORT', 'Formule Confort', 'Couverture intermédiaire avec garanties supplémentaires', '[PRODUCT_ID]', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'HOME_PREMIUM', 'Formule Premium', 'Couverture complète avec toutes les garanties', '[PRODUCT_ID]', '00000000-0000-0000-0000-000000000000');

-- Association des garanties aux formules
INSERT INTO formula_coverages (formula_id, coverage_id, is_included, organization_id)
VALUES
    -- Formule Essentielle
    ('[FORMULA_ID_BASIC]', '[COVERAGE_ID_FIRE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_BASIC]', '[COVERAGE_ID_WATER]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_BASIC]', '[COVERAGE_ID_NATURAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_BASIC]', '[COVERAGE_ID_LIABILITY]', TRUE, '00000000-0000-0000-0000-000000000000'),
    
    -- Formule Confort
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_FIRE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_WATER]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_NATURAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_LIABILITY]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_THEFT]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_GLASS]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_ELECTRICAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_ASSISTANCE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    
    -- Formule Premium
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_FIRE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_WATER]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_NATURAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_LIABILITY]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_THEFT]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_GLASS]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_ELECTRICAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_ASSISTANCE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_LEGAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_NEWOLD]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_SCHOOL]', TRUE, '00000000-0000-0000-0000-000000000000');
```

## 4. API et endpoints

### 4.1 API de gestion des biens immobiliers

```
GET /api/home/properties - Liste tous les biens immobiliers
GET /api/home/properties/{id} - Récupère un bien immobilier par son ID
POST /api/home/properties - Crée un nouveau bien immobilier
PUT /api/home/properties/{id} - Met à jour un bien immobilier existant
DELETE /api/home/properties/{id} - Supprime un bien immobilier
```

### 4.2 API de gestion des évaluations

```
GET /api/home/properties/{propertyId}/valuations - Liste toutes les évaluations d'un bien
GET /api/home/valuations/{id} - Récupère une évaluation par son ID
POST /api/home/properties/{propertyId}/valuations - Crée une nouvelle évaluation
PUT /api/home/valuations/{id} - Met à jour une évaluation existante
DELETE /api/home/valuations/{id} - Supprime une évaluation
```

### 4.3 API de gestion des occupations

```
GET /api/home/properties/{propertyId}/occupancies - Liste toutes les occupations d'un bien
GET /api/home/occupancies/{id} - Récupère une occupation par son ID
POST /api/home/properties/{propertyId}/occupancies - Crée une nouvelle occupation
PUT /api/home/occupancies/{id} - Met à jour une occupation existante
DELETE /api/home/occupancies/{id} - Supprime une occupation
```

### 4.4 API de gestion des polices habitation

```
GET /api/home/policies - Liste toutes les polices habitation
GET /api/home/policies/{id} - Récupère une police habitation par son ID
POST /api/home/policies - Crée une nouvelle police habitation
PUT /api/home/policies/{id} - Met à jour une police habitation existante
DELETE /api/home/policies/{id} - Supprime une police habitation
```

### 4.5 API de tarification habitation

```
POST /api/home/pricing/calculate - Calcule le prix d'une police habitation
POST /api/home/pricing/simulate - Simule différentes options de tarification
```

## 5. Règles métier spécifiques

### 5.1 Règles de tarification habitation

- Le tarif de base dépend de la surface habitable, du type de bien et de sa localisation
- Des majorations sont appliquées pour les biens de grande valeur
- Des réductions sont accordées pour les biens équipés de systèmes de sécurité (alarme, portes blindées)
- Des majorations sont appliquées pour les biens situés dans des zones à risque (inondation, cambriolage)
- Des réductions sont accordées pour les résidences principales par rapport aux résidences secondaires
- La valeur à neuf entraîne une majoration de la prime
- Le nombre d'occupants influence le tarif de la garantie responsabilité civile

### 5.2 Règles d'évaluation des biens

- L'évaluation du bâtiment est basée sur la surface, le type de construction et la localisation
- L'évaluation du contenu est basée sur la valeur déclarée ou un pourcentage de la valeur du bâtiment
- Les biens de valeur (bijoux, œuvres d'art) doivent être déclarés séparément
- L'évaluation doit être mise à jour tous les 3 ans
- La valeur à neuf ne s'applique qu'aux biens de moins de 10 ans

### 5.3 Règles de souscription habitation

- Pour les locataires, une assurance responsabilité locative est obligatoire
- Pour les propriétaires non occupants, une assurance propriétaire non occupant est requise
- Les biens inoccupés depuis plus de 90 jours nécessitent une déclaration spécifique
- Les biens en cours de construction nécessitent une assurance dommages-ouvrage
- Les biens classés ou historiques peuvent nécessiter une évaluation spécifique

## 6. Intégrations

### 6.1 Intégration avec les services externes

- **Service de géolocalisation** : Évaluation des risques liés à la localisation
- **Base de données des catastrophes naturelles** : Vérification des zones à risque
- **Service d'évaluation immobilière** : Estimation de la valeur des biens
- **Base de données des sinistres** : Récupération de l'historique des sinistres

### 6.2 Intégration avec les modules internes

- **Module client** : Récupération des informations client
- **Module de paiement** : Gestion des paiements de prime
- **Module de sinistre** : Déclaration et suivi des sinistres habitation
- **Module de document** : Génération des documents spécifiques à l'assurance habitation

## 7. Plan d'implémentation

### 7.1 Étapes d'implémentation

1. **Phase 1 : Mise en place des modèles de données**
    - Création des entités spécifiques à l'habitation
    - Mise en place des repositories
    - Implémentation des migrations de base de données

2. **Phase 2 : Développement des services métier**
    - Implémentation du service de gestion des biens immobiliers
    - Implémentation du service d'évaluation des biens
    - Implémentation du service de gestion des occupations
    - Implémentation du service de tarification habitation

3. **Phase 3 : Développement des API**
    - Implémentation des contrôleurs REST
    - Mise en place des DTOs
    - Implémentation des validateurs

4. **Phase 4 : Intégration avec les composants communs**
    - Intégration avec le service de police commun
    - Intégration avec le service de tarification commun
    - Intégration avec le service de client

5. **Phase 5 : Tests et validation**
    - Tests unitaires
    - Tests d'intégration
    - Tests de performance
    - Tests de validation métier

### 7.2 Dépendances

- Module commun (plan-commun.md)
- Module de gestion des clients
- Module de gestion des organisations
- Module de sécurité et authentification

### 7.3 Calendrier prévisionnel

| Phase   | Durée estimée | Dépendances            |
|---------|---------------|------------------------|
| Phase 1 | 2 semaines    | Module commun          |
| Phase 2 | 3 semaines    | Phase 1                |
| Phase 3 | 2 semaines    | Phase 2                |
| Phase 4 | 2 semaines    | Phase 3, Module commun |
| Phase 5 | 2 semaines    | Phase 4                |

## 8. Tests

### 8.1 Tests unitaires

- Tests des services de gestion des biens immobiliers
- Tests des services d'évaluation des biens
- Tests des services de gestion des occupations
- Tests des services de tarification habitation
- Tests des règles métier spécifiques à l'habitation

### 8.2 Tests d'intégration

- Tests d'intégration avec la base de données
- Tests d'intégration avec les services communs
- Tests des API REST

### 8.3 Tests de performance

- Tests de charge pour le calcul de prime habitation
- Tests de performance pour la recherche de biens
- Tests de performance pour la création de polices habitation

### 8.4 Tests métier

- Validation des règles de tarification
- Validation des règles de souscription
- Validation des règles d'évaluation des biens

## 9. Documentation

### 9.1 Documentation technique

- Documentation des API avec Swagger/OpenAPI
- Documentation des modèles de données
- Documentation des règles métier implémentées

### 9.2 Documentation utilisateur

- Guide d'utilisation des API
- Guide de configuration des produits habitation
- Guide de tarification habitation

## 10. Conclusion

Ce document fournit un plan détaillé pour l'implémentation du produit d'assurance habitation. Il servira de guide pour
l'équipe de développement et assurera une intégration harmonieuse avec les autres modules du système.

L'équipe en charge de l'implémentation du produit habitation devra suivre ce plan tout en respectant les directives
communes définies dans le document `plan-commun.md`.
