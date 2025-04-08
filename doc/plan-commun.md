# Plan d'implémentation des éléments communs IARDT

**Version :** 1.0.0
**Date de dernière mise à jour :** 2023-11-15
**Statut :** Document de travail
**Classification :** Confidentiel - Usage interne uniquement

## 1. Introduction

Ce document décrit les éléments communs à tous les produits d'assurance IARDT (Incendie, Accidents, Risques Divers,
Transport) qui doivent être implémentés avant le développement spécifique de chaque produit. Il s'agit des composants
partagés, des interfaces communes et des services transversaux qui seront utilisés par toutes les équipes produit.

### 1.1 Objectifs

- Définir une architecture commune pour tous les produits IARDT
- Établir les interfaces et contrats que chaque produit doit implémenter
- Identifier les services partagés et les composants réutilisables
- Fournir un cadre de développement cohérent pour toutes les équipes produit

### 1.2 Portée

Ce document couvre les éléments communs pour les produits suivants :

- Assurance Automobile
- Assurance Habitation (MRH)
- Assurance Voyage
- Risques Divers (Protection juridique, responsabilité civile, etc.)

## 2. Architecture commune

### 2.1 Structure de base

Tous les produits d'assurance partageront la même structure de base conforme à l'architecture hexagonale (Ports &
Adapters). Voici une représentation détaillée de l'arborescence des fichiers pour un produit générique, qui servira de
modèle pour tous les produits IARDT :

```
com.devolution.saas.product.[produit]
├── api                                 # Contrôleurs REST
│   ├── [Produit]Controller.java        # Contrôleur principal du produit
│   ├── [Entité]Controller.java         # Contrôleur pour chaque entité spécifique
│   ├── advice                          # Gestionnaires d'exceptions
│   │   └── [Produit]ExceptionHandler.java # Gestionnaire d'exceptions spécifique
│   └── request                         # Objets de requête
│       ├── Create[Entité]Request.java  # Requête de création
│       └── Update[Entité]Request.java  # Requête de mise à jour
│
├── application                         # Services d'application
│   ├── service                         # Implémentations des services
│   │   ├── [Produit]Service.java       # Interface du service principal
│   │   ├── [Produit]ServiceImpl.java   # Implémentation du service principal
│   │   ├── [Entité]Service.java        # Interface du service pour chaque entité
│   │   └── [Entité]ServiceImpl.java    # Implémentation du service pour chaque entité
│   ├── dto                             # Objets de transfert de données
│   │   ├── [Produit]DTO.java           # DTO principal du produit
│   │   ├── [Entité]DTO.java            # DTO pour chaque entité spécifique
│   │   └── mapper                      # Mappeurs entre entités et DTOs
│   │       ├── [Produit]Mapper.java    # Mappeur principal
│   │       └── [Entité]Mapper.java     # Mappeur pour chaque entité
│   ├── command                         # Commandes (pattern CQRS)
│   │   ├── Create[Entité]Command.java  # Commande de création
│   │   └── Update[Entité]Command.java  # Commande de mise à jour
│   ├── query                           # Requêtes (pattern CQRS)
│   │   ├── Get[Entité]Query.java       # Requête de récupération
│   │   └── List[Entité]Query.java      # Requête de listage
│   ├── command/handler                 # Gestionnaires de commandes
│   │   ├── Create[Entité]Handler.java  # Gestionnaire de création
│   │   └── Update[Entité]Handler.java  # Gestionnaire de mise à jour
│   ├── query/handler                   # Gestionnaires de requêtes
│   │   ├── Get[Entité]Handler.java     # Gestionnaire de récupération
│   │   └── List[Entité]Handler.java    # Gestionnaire de listage
│   └── usecase                         # Cas d'utilisation
│       ├── Create[Entité]UseCase.java  # Cas d'utilisation de création
│       ├── Update[Entité]UseCase.java  # Cas d'utilisation de mise à jour
│       ├── Delete[Entité]UseCase.java  # Cas d'utilisation de suppression
│       └── Calculate[Produit]PremiumUseCase.java # Cas d'utilisation de calcul de prime
│
├── domain                              # Modèles et repositories de domaine
│   ├── model                           # Modèles de domaine
│   │   ├── [Produit].java              # Entité principale du produit
│   │   ├── [Entité].java               # Entités spécifiques au produit
│   │   ├── [Produit]Policy.java        # Entité Police spécifique au produit
│   │   ├── enums                       # Énumérations
│   │   │   ├── [Produit]Status.java    # Statuts spécifiques au produit
│   │   │   └── [Entité]Type.java       # Types spécifiques aux entités
│   │   └── valueobject                 # Objets valeur
│   │       └── [Produit]Characteristics.java # Caractéristiques spécifiques
│   ├── repository                      # Repositories de domaine
│   │   ├── [Produit]Repository.java    # Repository principal
│   │   └── [Entité]Repository.java     # Repository pour chaque entité
│   ├── service                         # Services de domaine
│   │   ├── [Produit]DomainService.java # Interface du service de domaine
│   │   └── [Produit]DomainServiceImpl.java # Implémentation du service de domaine
│   └── event                           # Événements de domaine
│       ├── [Produit]Event.java         # Événement de base
│       ├── [Produit]CreatedEvent.java  # Événement de création
│       └── [Produit]UpdatedEvent.java  # Événement de mise à jour
│
└── infrastructure                      # Implémentations d'infrastructure
    ├── persistence                     # Implémentations de persistence
    │   ├── entity                      # Entités JPA
    │   │   ├── [Produit]Entity.java    # Entité JPA principale
    │   │   └── [Entité]Entity.java     # Entités JPA spécifiques
    │   ├── repository                  # Repositories JPA
    │   │   ├── Jpa[Produit]Repository.java # Repository JPA principal
    │   │   └── Jpa[Entité]Repository.java # Repositories JPA spécifiques
    │   └── mapper                      # Mappeurs entre entités domaine et JPA
    │       ├── [Produit]EntityMapper.java # Mappeur principal
    │       └── [Entité]EntityMapper.java # Mappeurs spécifiques
    ├── messaging                       # Implémentations de messaging
    │   ├── listener                    # Écouteurs d'événements
    │   │   └── [Produit]EventListener.java # Écouteur d'événements
    │   └── publisher                   # Publieur d'événements
    │       └── [Produit]EventPublisher.java # Publieur d'événements
    └── config                          # Configuration spécifique au produit
        └── [Produit]Config.java        # Configuration du module
```

Cette structure détaillée servira de guide pour l'implémentation de chaque produit IARDT. Les équipes de développement
devront suivre cette organisation pour assurer la cohérence entre les différents modules.

### 2.2 Interfaces communes

Les interfaces suivantes doivent être implémentées par tous les produits d'assurance :

```java
// Interface commune pour tous les produits d'assurance
public interface InsuranceProduct {
    UUID getId();

    String getCode();

    String getName();

    ProductStatus getStatus();

    LocalDate getEffectiveDate();

    LocalDate getExpiryDate();

    BigDecimal calculatePremium(PolicyCalculationContext context);

    List<Coverage> getAvailableCoverages();

    boolean validateSubscription(SubscriptionContext context);
}

// Interface pour les garanties
public interface Coverage {
    UUID getId();

    String getCode();

    String getName();

    String getDescription();

    CoverageType getType();

    boolean isOptional();

    BigDecimal calculatePremium(PolicyCalculationContext context);
}

// Interface pour les formules
public interface Formula {
    UUID getId();

    String getCode();

    String getName();

    String getDescription();

    List<Coverage> getIncludedCoverages();

    BigDecimal calculatePremium(PolicyCalculationContext context);
}
```

## 3. Modèle de données commun

### 3.1 Tables de référence générales

Ces tables de référence sont communes à plusieurs produits d'assurance et sont gérées de manière centralisée,
indépendamment des organisations.

#### 3.1.1 Professions

```sql
CREATE TABLE professions
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)   NOT NULL UNIQUE,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL,
    is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID
    -- Pas de organization_id car les professions sont communes à toutes les organisations
);

-- Insertion des professions avec leurs coefficients de risque
INSERT INTO professions (id, code, name, description, risk_coefficient)
VALUES (uuid_generate_v4(), 'PROF_TEACHER', 'Enseignant', 'Professeurs, instituteurs et personnel éducatif', 0.90),
       (uuid_generate_v4(), 'PROF_OFFICE', 'Employé de bureau', 'Personnel administratif et de bureau', 0.95),
       (uuid_generate_v4(), 'PROF_MEDICAL', 'Professionnel de santé', 'Médecins, infirmiers et personnel médical',
        1.00),
       (uuid_generate_v4(), 'PROF_SALES', 'Commercial', 'Représentants et agents commerciaux', 1.10),
       (uuid_generate_v4(), 'PROF_DRIVER', 'Chauffeur professionnel', 'Chauffeurs de taxi, livreurs et transporteurs',
        1.30),
       (uuid_generate_v4(), 'PROF_MANAGER', 'Cadre', 'Cadres et dirigeants d\'entreprise', 0.95),
(uuid_generate_v4(), 'PROF_STUDENT', 'Étudiant', 'Étudiants et apprentis', 1.20),
(uuid_generate_v4(), 'PROF_RETIRED', 'Retraité', 'Personnes retraitées', 1.05),
(uuid_generate_v4(), 'PROF_ARTISAN', 'Artisan', 'Artisans et métiers manuels', 1.10),
(uuid_generate_v4(), 'PROF_FARMER', 'Agriculteur', 'Agriculteurs et travailleurs agricoles', 1.00);

-- Table pour les ajustements spécifiques par organisation (si nécessaire)
CREATE TABLE organization_profession_adjustments (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    profession_id UUID NOT NULL,
    risk_coefficient_adjustment DECIMAL(5, 2),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    FOREIGN KEY (organization_id) REFERENCES organizations(id),
    FOREIGN KEY (profession_id) REFERENCES professions(id),
    UNIQUE (organization_id, profession_id)
);
```

#### 3.1.2 Types de carburant (Fuel Types)

```sql
CREATE TABLE fuel_types
(
    id                      UUID PRIMARY KEY,
    code                    VARCHAR(20)   NOT NULL UNIQUE,
    name                    VARCHAR(100)  NOT NULL,
    description             TEXT,
    tariff_coefficient      DECIMAL(5, 2) NOT NULL,
    risk_factor             DECIMAL(5, 2) NOT NULL, -- Facteur de risque pour les calculs de prime
    power_conversion_factor DECIMAL(5, 2) NOT NULL, -- Facteur de conversion de puissance pour les calculs
    is_active               BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP,
    created_by              UUID,
    updated_by              UUID
    -- Pas de organization_id car les types de carburant sont communs à toutes les organisations
);

-- Insertion des types de carburant
INSERT INTO fuel_types (id, code, name, description, tariff_coefficient, risk_factor, power_conversion_factor)
VALUES (uuid_generate_v4(), 'ESSENCE', 'Essence', 'Moteur à essence', 1.00, 1.20, 1.00),
       (uuid_generate_v4(), 'DIESEL', 'Diesel', 'Moteur diesel', 0.95, 1.15, 1.10),
       (uuid_generate_v4(), 'ELECTRIQUE', 'Électrique', 'Moteur électrique', 0.85, 0.90, 0.85),
       (uuid_generate_v4(), 'HYBRIDE', 'Hybride', 'Moteur hybride (essence/électrique)', 0.90, 1.00, 0.95),
       (uuid_generate_v4(), 'GPL', 'GPL', 'Gaz de pétrole liquéfié', 0.92, 1.10, 0.98),
       (uuid_generate_v4(), 'GNV', 'GNV', 'Gaz naturel pour véhicules', 0.90, 1.05, 0.97),
       (uuid_generate_v4(), 'BIOETHANOL', 'Bioéthanol', 'Carburant E85', 0.95, 1.08, 1.05),
       (uuid_generate_v4(), 'HYDROGENE', 'Hydrogène', 'Pile à combustible à hydrogène', 0.80, 0.85, 0.80);

-- Table pour les ajustements spécifiques par organisation (si nécessaire)
CREATE TABLE organization_fuel_type_adjustments
(
    id                                 UUID PRIMARY KEY,
    organization_id                    UUID      NOT NULL,
    fuel_type_id                       UUID      NOT NULL,
    tariff_coefficient_adjustment      DECIMAL(5, 2),
    risk_factor_adjustment             DECIMAL(5, 2),
    power_conversion_factor_adjustment DECIMAL(5, 2),
    is_active                          BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at                         TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                         TIMESTAMP,
    created_by                         UUID,
    updated_by                         UUID,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    FOREIGN KEY (fuel_type_id) REFERENCES fuel_types (id),
    UNIQUE (organization_id, fuel_type_id)
);
```

#### 3.1.3 Couleurs de véhicules

```sql
CREATE TABLE vehicle_colors
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)   NOT NULL UNIQUE,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL,
    is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID
);

-- Insertion des couleurs de véhicules avec leurs coefficients de risque
INSERT INTO vehicle_colors (id, code, name, description, risk_coefficient)
VALUES (uuid_generate_v4(), 'WHITE', 'Blanc', 'Couleur blanche', 0.95),
       (uuid_generate_v4(), 'BLACK', 'Noir', 'Couleur noire', 1.05),
       (uuid_generate_v4(), 'GRAY', 'Gris', 'Couleur grise', 0.98),
       (uuid_generate_v4(), 'SILVER', 'Argent', 'Couleur argentée', 0.97),
       (uuid_generate_v4(), 'BLUE', 'Bleu', 'Couleur bleue', 1.00),
       (uuid_generate_v4(), 'RED', 'Rouge', 'Couleur rouge', 1.10),
       (uuid_generate_v4(), 'GREEN', 'Vert', 'Couleur verte', 1.00),
       (uuid_generate_v4(), 'YELLOW', 'Jaune', 'Couleur jaune', 1.05),
       (uuid_generate_v4(), 'BROWN', 'Marron', 'Couleur marron', 0.98),
       (uuid_generate_v4(), 'BEIGE', 'Beige', 'Couleur beige', 0.97),
       (uuid_generate_v4(), 'ORANGE', 'Orange', 'Couleur orange', 1.05),
       (uuid_generate_v4(), 'PURPLE', 'Violet', 'Couleur violette', 1.02),
       (uuid_generate_v4(), 'GOLD', 'Or', 'Couleur dorée', 1.10),
       (uuid_generate_v4(), 'OTHER', 'Autre', 'Autre couleur', 1.00);
```

#### 3.1.4 Types de permis de conduire

```sql
CREATE TABLE driving_license_types
(
    id          UUID PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    min_age     INT          NOT NULL,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    created_by  UUID,
    updated_by  UUID
);

-- Insertion des types de permis de conduire
INSERT INTO driving_license_types (id, code, name, description, min_age)
VALUES (uuid_generate_v4(), 'A1', 'Permis A1', 'Motocyclettes légères (125 cm³)', 16),
       (uuid_generate_v4(), 'A', 'Permis A', 'Motocyclettes', 18),
       (uuid_generate_v4(), 'B', 'Permis B', 'Véhicules légers', 18),
       (uuid_generate_v4(), 'C', 'Permis C', 'Poids lourds', 21),
       (uuid_generate_v4(), 'D', 'Permis D', 'Transport en commun', 24),
       (uuid_generate_v4(), 'E', 'Permis E', 'Remorques', 18),
       (uuid_generate_v4(), 'F', 'Permis F', 'Véhicules agricoles', 16);
```

#### 3.1.5 Types de documents d'identité

```sql
CREATE TABLE identity_document_types
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)  NOT NULL UNIQUE,
    name             VARCHAR(100) NOT NULL,
    description      TEXT,
    validation_regex VARCHAR(255),
    is_active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID
);

-- Insertion des types de documents d'identité
INSERT INTO identity_document_types (id, code, name, description, validation_regex)
VALUES (uuid_generate_v4(), 'ID_CARD', 'Carte d\'identité nationale', 'Carte d\'identité nationale', NULL),
       (uuid_generate_v4(), 'PASSPORT', 'Passeport', 'Passeport international', NULL),
       (uuid_generate_v4(), 'DRIVER_LICENSE', 'Permis de conduire', 'Permis de conduire', NULL),
       (uuid_generate_v4(), 'RESIDENCE_PERMIT', 'Carte de séjour', 'Carte de séjour pour résidents étrangers', NULL),
       (uuid_generate_v4(), 'VOTER_CARD', 'Carte d\'électeur', 'Carte d\'électeur', NULL),
       (uuid_generate_v4(), 'PROFESSIONAL_CARD', 'Carte professionnelle', 'Carte professionnelle', NULL);
```

#### 3.1.6 Pays

```sql
CREATE TABLE countries
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(2)    NOT NULL UNIQUE,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL,
    is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID
);

-- Insertion des pays pour la zone CIMA
INSERT INTO countries (id, code, name, description, risk_coefficient)
VALUES (uuid_generate_v4(), 'BJ', 'Bénin', 'République du Bénin', 1.00),
       (uuid_generate_v4(), 'BF', 'Burkina Faso', 'Burkina Faso', 1.00),
       (uuid_generate_v4(), 'CI', 'Côte d\'Ivoire', 'République de Côte d\'Ivoire', 1.00),
       (uuid_generate_v4(), 'GW', 'Guinée-Bissau', 'République de Guinée-Bissau', 1.05),
       (uuid_generate_v4(), 'ML', 'Mali', 'République du Mali', 1.00),
       (uuid_generate_v4(), 'NE', 'Niger', 'République du Niger', 1.00),
       (uuid_generate_v4(), 'SN', 'Sénégal', 'République du Sénégal', 1.00),
       (uuid_generate_v4(), 'TG', 'Togo', 'République Togolaise', 1.00),
       (uuid_generate_v4(), 'CM', 'Cameroun', 'République du Cameroun', 1.00),
       (uuid_generate_v4(), 'CF', 'Centrafrique', 'République centrafricaine', 1.10),
       (uuid_generate_v4(), 'CG', 'Congo', 'République du Congo', 1.05),
       (uuid_generate_v4(), 'GA', 'Gabon', 'République gabonaise', 1.00),
       (uuid_generate_v4(), 'GQ', 'Guinée équatoriale', 'République de Guinée équatoriale', 1.05),
       (uuid_generate_v4(), 'TD', 'Tchad', 'République du Tchad', 1.10),
       (uuid_generate_v4(), 'KM', 'Comores', 'Union des Comores', 1.05);
```

### 3.2 Entités communes

#### 3.2.1 Produit d'assurance

```sql
CREATE TABLE products
(
    id              UUID PRIMARY KEY,
    code            VARCHAR(50)  NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    product_type    VARCHAR(50)  NOT NULL, -- AUTO, HOME, TRAVEL, MISC
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    effective_date  DATE         NOT NULL,
    expiry_date     DATE,
    settings        JSONB,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID         NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);
```

#### 3.2.2 Garanties

```sql
CREATE TABLE coverages
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(50)  NOT NULL,
    name               VARCHAR(255) NOT NULL,
    description        TEXT,
    coverage_type      VARCHAR(50)  NOT NULL,
    is_optional        BOOLEAN      NOT NULL DEFAULT FALSE,
    default_limit      DECIMAL(19, 2),
    default_deductible DECIMAL(19, 2),
    settings           JSONB,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID         NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);
```

#### 3.2.3 Formules

```sql
CREATE TABLE formulas
(
    id              UUID PRIMARY KEY,
    code            VARCHAR(50)  NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    product_id      UUID         NOT NULL,
    settings        JSONB,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID         NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, product_id, organization_id)
);
```

#### 3.2.4 Association Formule-Garantie

```sql
CREATE TABLE formula_coverages
(
    formula_id        UUID      NOT NULL,
    coverage_id       UUID      NOT NULL,
    is_included       BOOLEAN   NOT NULL DEFAULT TRUE,
    limit_amount      DECIMAL(19, 2),
    deductible_amount DECIMAL(19, 2),
    settings          JSONB,
    created_at        TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP,
    created_by        UUID,
    updated_by        UUID,
    organization_id   UUID      NOT NULL,
    PRIMARY KEY (formula_id, coverage_id),
    FOREIGN KEY (formula_id) REFERENCES formulas (id),
    FOREIGN KEY (coverage_id) REFERENCES coverages (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

### 3.3 Entités liées aux polices

#### 3.3.1 Police d'assurance

```sql
CREATE TABLE policies
(
    id                UUID PRIMARY KEY,
    policy_number     VARCHAR(50)    NOT NULL,
    customer_id       UUID           NOT NULL,
    product_id        UUID           NOT NULL,
    formula_id        UUID,
    status            VARCHAR(20)    NOT NULL, -- DRAFT, QUOTE, ACTIVE, CANCELLED, EXPIRED
    effective_date    DATE           NOT NULL,
    expiry_date       DATE           NOT NULL,
    premium_amount    DECIMAL(19, 2) NOT NULL,
    tax_amount        DECIMAL(19, 2) NOT NULL,
    total_amount      DECIMAL(19, 2) NOT NULL,
    payment_frequency VARCHAR(20)    NOT NULL, -- MONTHLY, QUARTERLY, ANNUALLY
    underwriter_id    UUID,
    broker_id         UUID,
    agent_id          UUID,
    data              JSONB,                   -- Données spécifiques au produit
    created_at        TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP,
    created_by        UUID,
    updated_by        UUID,
    organization_id   UUID           NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (formula_id) REFERENCES formulas (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (policy_number, organization_id)
);
```

#### 3.3.2 Garanties de la police

```sql
CREATE TABLE policy_coverages
(
    policy_id         UUID      NOT NULL,
    coverage_id       UUID      NOT NULL,
    is_selected       BOOLEAN   NOT NULL DEFAULT TRUE,
    limit_amount      DECIMAL(19, 2),
    deductible_amount DECIMAL(19, 2),
    premium_amount    DECIMAL(19, 2),
    settings          JSONB,
    created_at        TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP,
    created_by        UUID,
    updated_by        UUID,
    organization_id   UUID      NOT NULL,
    PRIMARY KEY (policy_id, coverage_id),
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (coverage_id) REFERENCES coverages (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

## 4. API communes

### 4.1 API de gestion des produits

```
GET /api/products - Liste tous les produits
GET /api/products/{id} - Récupère un produit par son ID
POST /api/products - Crée un nouveau produit
PUT /api/products/{id} - Met à jour un produit existant
DELETE /api/products/{id} - Supprime un produit

GET /api/products/{productId}/coverages - Liste toutes les garanties d'un produit
POST /api/products/{productId}/coverages - Ajoute une garantie à un produit

GET /api/products/{productId}/formulas - Liste toutes les formules d'un produit
GET /api/products/{productId}/formulas/{formulaId} - Récupère une formule par son ID
POST /api/products/{productId}/formulas - Crée une nouvelle formule
PUT /api/products/{productId}/formulas/{formulaId} - Met à jour une formule existante
DELETE /api/products/{productId}/formulas/{formulaId} - Supprime une formule
```

### 4.2 API de gestion des polices

```
GET /api/policies - Liste toutes les polices
GET /api/policies/{id} - Récupère une police par son ID
POST /api/policies - Crée une nouvelle police
PUT /api/policies/{id} - Met à jour une police existante
DELETE /api/policies/{id} - Supprime une police

POST /api/policies/{id}/issue - Émet une police
POST /api/policies/{id}/cancel - Annule une police
POST /api/policies/{id}/renew - Renouvelle une police

GET /api/policies/{policyId}/coverages - Liste toutes les garanties d'une police
POST /api/policies/{policyId}/coverages - Ajoute une garantie à une police
```

### 4.3 API de tarification

```
POST /api/pricing/calculate - Calcule le prix d'une police
POST /api/pricing/simulate - Simule différentes options de tarification
```

## 5. Services communs

### 5.1 Service de tarification

Le service de tarification est responsable du calcul des primes d'assurance pour tous les produits. Il doit être
extensible pour prendre en compte les spécificités de chaque produit.

```java
public interface PricingService {
    PricingResult calculatePremium(PricingRequest request);

    List<PricingSimulation> simulateOptions(PricingSimulationRequest request);
}
```

### 5.2 Service de gestion des polices

```java
public interface PolicyService {
    Policy createPolicy(CreatePolicyCommand command);

    Policy updatePolicy(UpdatePolicyCommand command);

    Policy issuePolicy(IssuePolicyCommand command);

    Policy cancelPolicy(CancelPolicyCommand command);

    Policy renewPolicy(RenewPolicyCommand command);

    Optional<Policy> findById(UUID id);

    List<Policy> findAllByCustomerId(UUID customerId);
}
```

### 5.3 Service de gestion des produits

```java
public interface ProductService {
    Product createProduct(CreateProductCommand command);

    Product updateProduct(UpdateProductCommand command);

    Optional<Product> findById(UUID id);

    List<Product> findAllByType(String productType);

    List<Product> findAllActive();
}
```

## 6. Règles métier communes

### 6.1 Règles de tarification

- Toute prime calculée doit être supérieure ou égale à la prime minimale définie pour le produit
- Les remises commerciales ne peuvent pas dépasser un certain pourcentage de la prime de base
- Les taxes d'assurance doivent être calculées selon les taux en vigueur
- Les commissions des intermédiaires doivent être calculées selon les taux définis

### 6.2 Règles de souscription

- Vérification de l'éligibilité du client selon les critères du produit
- Validation des informations obligatoires selon le type de produit
- Application des règles de souscription spécifiques au produit
- Vérification des antécédents de sinistres

### 6.3 Règles de gestion des polices

- Une police ne peut être émise que si toutes les informations obligatoires sont fournies
- Une police ne peut être annulée que si elle est active
- Le renouvellement d'une police doit être effectué avant sa date d'expiration
- Les modifications de garanties ne sont possibles que pendant la période de validité de la police

## 7. Plan d'implémentation

### 7.1 Étapes d'implémentation

1. **Phase 1 : Mise en place de l'infrastructure commune**
    - Création des interfaces communes
    - Implémentation des entités de base
    - Mise en place des repositories communs
    - Implémentation des services partagés

2. **Phase 2 : Développement des cas d'utilisation communs**
    - Implémentation du cas d'utilisation de création de police
    - Implémentation du cas d'utilisation de sélection des garanties
    - Implémentation du cas d'utilisation de calcul de prime

3. **Phase 3 : Développement des API communes**
    - Implémentation des contrôleurs REST communs
    - Mise en place des DTOs partagés
    - Implémentation des validateurs communs

4. **Phase 4 : Intégration avec les modules existants**
    - Intégration avec le module de gestion des clients
    - Intégration avec le module de gestion des organisations
    - Intégration avec le module de sécurité

### 7.2 Dépendances

- Module de gestion des clients
- Module de gestion des organisations
- Module de sécurité et authentification
- Module de gestion des utilisateurs

### 7.3 Calendrier prévisionnel

| Phase   | Durée estimée | Dépendances                |
|---------|---------------|----------------------------|
| Phase 1 | 2 semaines    | Aucune                     |
| Phase 2 | 3 semaines    | Phase 1                    |
| Phase 3 | 2 semaines    | Phase 2                    |
| Phase 4 | 2 semaines    | Phase 3, Modules existants |

## 8. Tests

### 8.1 Tests unitaires

- Tests des services de tarification
- Tests des services de gestion des polices
- Tests des services de gestion des produits
- Tests des règles métier communes

### 8.2 Tests d'intégration

- Tests d'intégration avec la base de données
- Tests d'intégration avec les autres modules
- Tests des API REST

### 8.3 Tests de performance

- Tests de charge pour le calcul de prime
- Tests de performance pour la recherche de polices
- Tests de performance pour la création de polices

## 9. Documentation

La documentation suivante devra être produite :

1. **Documentation des API** avec Swagger/OpenAPI
2. **Documentation des interfaces** pour les équipes produit
3. **Guide d'intégration** expliquant comment étendre les composants communs
4. **Exemples d'implémentation** pour chaque produit

## 10. Conclusion

Ce document fournit un cadre commun pour l'implémentation des différents produits d'assurance IARDT. Il est essentiel
que toutes les équipes produit suivent ces directives pour garantir la cohérence et l'interopérabilité des différents
modules.

Les tables de référence générales (professions, types de carburant, couleurs de véhicules, types de permis, types de
documents d'identité, pays) sont définies dans ce document car elles sont communes à plusieurs produits d'assurance.
Cette centralisation permet d'éviter la duplication des données et d'assurer une cohérence globale, tout en permettant
des ajustements spécifiques par organisation via des tables d'ajustements.

Les éléments communs seront développés par l'équipe socle, tandis que les fonctionnalités spécifiques à chaque produit
seront implémentées par les équipes produit dédiées.
