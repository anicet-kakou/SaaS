# Plan d'implémentation du produit Assurance Automobile

**Version :** 1.0.0
**Date de dernière mise à jour :** 2023-11-15
**Statut :** Document de travail
**Classification :** Confidentiel - Usage interne uniquement

## 1. Introduction

Ce document décrit le plan d'implémentation du produit d'assurance automobile dans le cadre du projet SaaS multi-tenant
assurantiel. Il détaille l'architecture, les modèles de données, les API et les règles métier spécifiques à ce produit.

### 1.1 Objectifs

- Définir l'architecture spécifique au produit d'assurance automobile
- Détailler les modèles de données propres à ce produit
- Spécifier les API et endpoints nécessaires
- Documenter les règles métier spécifiques à l'assurance automobile
- Établir un plan d'implémentation clair pour l'équipe de développement

### 1.2 Portée

Ce document couvre l'implémentation complète du produit d'assurance automobile, y compris :

- La gestion des véhicules
- Les formules tous risques/tiers
- Le système de bonus-malus
- Les garanties spécifiques à l'automobile
- La tarification automobile

## 2. Architecture et structure du module

### 2.1 Structure du module

Le module d'assurance automobile suivra la structure commune définie dans le document `plan-commun.md`, avec les
spécificités suivantes :

```
com.devolution.saas.product.auto
├── api                            # Contrôleurs REST spécifiques à l'auto
│   ├── VehicleController.java     # API de gestion des véhicules
│   ├── AutoPolicyController.java  # API de gestion des polices auto
│   └── BonusMalusController.java  # API de gestion du bonus-malus
├── application                    # Services d'application
│   ├── service                    # Implémentations des services
│   │   ├── AutoPricingService.java # Service de tarification auto
│   │   └── VehicleService.java    # Service de gestion des véhicules
│   ├── dto                        # DTOs spécifiques à l'auto
│   │   ├── VehicleDTO.java        # DTO pour les véhicules
│   │   └── BonusMalusDTO.java     # DTO pour le bonus-malus
│   ├── command                    # Commandes spécifiques à l'auto
│   │   ├── CreateVehicleCommand.java # Création de véhicule
│   │   └── UpdateBonusMalusCommand.java # Mise à jour du bonus-malus
│   └── usecase                    # Cas d'utilisation spécifiques à l'auto
│       ├── CalculateAutoPremium.java # Calcul de prime auto
│       └── ApplyBonusMalus.java   # Application du bonus-malus
├── domain                         # Modèles et repositories de domaine
│   ├── model                      # Modèles spécifiques à l'auto
│   │   ├── Vehicle.java           # Entité Véhicule
│   │   ├── BonusMalus.java        # Entité Bonus-Malus
│   │   ├── AutoPolicy.java        # Entité Police Auto
│   │   └── VehicleUsage.java      # Entité Usage du véhicule
│   ├── repository                 # Repositories spécifiques à l'auto
│   │   ├── VehicleRepository.java # Repository pour les véhicules
│   │   └── BonusMalusRepository.java # Repository pour le bonus-malus
│   └── service                    # Services de domaine spécifiques à l'auto
│       └── BonusMalusCalculator.java # Calculateur de bonus-malus
└── infrastructure                 # Implémentations d'infrastructure
    └── persistence                # Implémentations de persistence
        ├── JpaVehicleRepository.java # Implémentation JPA du repository véhicule
        └── JpaBonusMalusRepository.java # Implémentation JPA du repository bonus-malus
```

### 2.2 Intégration avec les composants communs

Le module d'assurance automobile implémentera les interfaces communes définies dans le document `plan-commun.md` :

```java
// Implémentation de l'interface InsuranceProduct pour l'assurance auto
public class AutoInsuranceProduct implements InsuranceProduct {
    // Implémentation des méthodes de l'interface
    @Override
    public BigDecimal calculatePremium(PolicyCalculationContext context) {
        // Logique spécifique au calcul de prime auto
        // Prise en compte du type de véhicule, de l'usage, du bonus-malus, etc.
    }

    @Override
    public boolean validateSubscription(SubscriptionContext context) {
        // Validation spécifique à l'assurance auto
        // Vérification de l'âge du conducteur, du permis, etc.
    }

    // Autres méthodes de l'interface
}
```

## 3. Modèle de données

### 3.1 Tables de référence spécifiques à l'assurance automobile

**Note :** Les tables de référence générales (professions, types de carburant, couleurs de véhicules, types de permis de
conduire, types de documents d'identité, pays) sont définies dans le document `plan-commun.md` car elles sont communes à
plusieurs produits d'assurance.

#### 3.1.1 Catégories de véhicules

```sql
CREATE TABLE vehicle_categories
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(20)   NOT NULL,
    name               VARCHAR(100)  NOT NULL,
    description        TEXT,
    tariff_coefficient DECIMAL(5, 2) NOT NULL,
    is_active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID          NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion des catégories de véhicules selon la classification CIMA
INSERT INTO vehicle_categories (id, code, name, description, tariff_coefficient, organization_id)
VALUES (uuid_generate_v4(), 'CAT1', 'Véhicules de tourisme', 'Véhicules particuliers et commerciaux à usage privé',
        1.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'CAT2', 'Véhicules de transport de marchandises', 'Camions, camionnettes et fourgonnettes',
        1.50, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'CAT3', 'Véhicules de transport en commun', 'Autobus, autocars et minibus', 1.75,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'CAT4', 'Engins de chantier', 'Tracteurs, bulldozers et autres engins de chantier', 2.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'CAT5', 'Véhicules à deux roues', 'Motos, scooters et cyclomoteurs', 0.80,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'CAT6', 'Véhicules agricoles', 'Tracteurs agricoles et machines agricoles automotrices',
        1.20, '00000000-0000-0000-0000-000000000000');
```

#### 3.1.2 Sous-catégories de véhicules

```sql
CREATE TABLE vehicle_subcategories
(
    id                 UUID PRIMARY KEY,
    category_id        UUID          NOT NULL,
    code               VARCHAR(20)   NOT NULL,
    name               VARCHAR(100)  NOT NULL,
    description        TEXT,
    tariff_coefficient DECIMAL(5, 2) NOT NULL,
    is_active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID          NOT NULL,
    FOREIGN KEY (category_id) REFERENCES vehicle_categories (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion des sous-catégories pour les véhicules de tourisme
INSERT INTO vehicle_subcategories (id, category_id, code, name, description, tariff_coefficient, organization_id)
VALUES (uuid_generate_v4(), '[CAT1_ID]', 'SUBCAT1_1', 'Véhicules de tourisme < 9 CV',
        'Véhicules de tourisme de puissance inférieure à 9 CV', 0.90, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), '[CAT1_ID]', 'SUBCAT1_2', 'Véhicules de tourisme 9-15 CV',
        'Véhicules de tourisme de puissance entre 9 et 15 CV', 1.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), '[CAT1_ID]', 'SUBCAT1_3', 'Véhicules de tourisme > 15 CV',
        'Véhicules de tourisme de puissance supérieure à 15 CV', 1.20, '00000000-0000-0000-0000-000000000000');

-- Insertion des sous-catégories pour les véhicules de transport de marchandises
INSERT INTO vehicle_subcategories (id, category_id, code, name, description, tariff_coefficient, organization_id)
VALUES (uuid_generate_v4(), '[CAT2_ID]', 'SUBCAT2_1', 'Véhicules < 3.5 tonnes',
        'Véhicules de transport de marchandises de PTAC inférieur à 3.5 tonnes', 1.30,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), '[CAT2_ID]', 'SUBCAT2_2', 'Véhicules 3.5-10 tonnes',
        'Véhicules de transport de marchandises de PTAC entre 3.5 et 10 tonnes', 1.50,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), '[CAT2_ID]', 'SUBCAT2_3', 'Véhicules > 10 tonnes',
        'Véhicules de transport de marchandises de PTAC supérieur à 10 tonnes', 1.80,
        '00000000-0000-0000-0000-000000000000');
```

#### 3.1.3 Marques de véhicules

```sql
CREATE TABLE vehicle_makes
(
    id                UUID PRIMARY KEY,
    code              VARCHAR(20)  NOT NULL,
    name              VARCHAR(100) NOT NULL,
    description       TEXT,
    country_of_origin VARCHAR(50),
    is_active         BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP,
    created_by        UUID,
    updated_by        UUID,
    organization_id   UUID         NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion des marques courantes en Afrique
INSERT INTO vehicle_makes (id, code, name, country_of_origin, organization_id)
VALUES (uuid_generate_v4(), 'TOYOTA', 'Toyota', 'Japon', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'NISSAN', 'Nissan', 'Japon', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'MITSUBISHI', 'Mitsubishi', 'Japon', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'HYUNDAI', 'Hyundai', 'Corée du Sud', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'KIA', 'Kia', 'Corée du Sud', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'PEUGEOT', 'Peugeot', 'France', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'RENAULT', 'Renault', 'France', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'MERCEDES', 'Mercedes-Benz', 'Allemagne', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'FORD', 'Ford', 'États-Unis', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'ISUZU', 'Isuzu', 'Japon', '00000000-0000-0000-0000-000000000000');
```

#### 3.1.4 Modèles de véhicules

```sql
CREATE TABLE vehicle_models
(
    id              UUID PRIMARY KEY,
    make_id         UUID         NOT NULL,
    code            VARCHAR(20)  NOT NULL,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    category_id     UUID         NOT NULL,
    subcategory_id  UUID,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID         NOT NULL,
    FOREIGN KEY (make_id) REFERENCES vehicle_makes (id),
    FOREIGN KEY (category_id) REFERENCES vehicle_categories (id),
    FOREIGN KEY (subcategory_id) REFERENCES vehicle_subcategories (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (make_id, code, organization_id)
);

-- Insertion de quelques modèles Toyota
INSERT INTO vehicle_models (id, make_id, code, name, category_id, subcategory_id, organization_id)
VALUES (uuid_generate_v4(), '[TOYOTA_ID]', 'COROLLA', 'Corolla', '[CAT1_ID]', '[SUBCAT1_1_ID]',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), '[TOYOTA_ID]', 'CAMRY', 'Camry', '[CAT1_ID]', '[SUBCAT1_2_ID]',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), '[TOYOTA_ID]', 'LAND_CRUISER', 'Land Cruiser', '[CAT1_ID]', '[SUBCAT1_3_ID]',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), '[TOYOTA_ID]', 'HILUX', 'Hilux', '[CAT2_ID]', '[SUBCAT2_1_ID]',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), '[TOYOTA_ID]', 'HIACE', 'Hiace', '[CAT3_ID]', NULL, '00000000-0000-0000-0000-000000000000');
```

#### 3.1.5 Types d'usage des véhicules

```sql
CREATE TABLE vehicle_usages
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(20)   NOT NULL,
    name               VARCHAR(100)  NOT NULL,
    description        TEXT,
    tariff_coefficient DECIMAL(5, 2) NOT NULL,
    is_active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID          NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion des types d'usage selon la classification CIMA
INSERT INTO vehicle_usages (id, code, name, description, tariff_coefficient, organization_id)
VALUES (uuid_generate_v4(), 'PRIVATE', 'Usage privé', 'Véhicule utilisé à des fins personnelles', 1.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'COMMUTE', 'Trajet domicile-travail', 'Véhicule utilisé pour les trajets domicile-travail',
        1.10, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'PROFESSIONAL', 'Usage professionnel', 'Véhicule utilisé à des fins professionnelles', 1.30,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'COMMERCIAL', 'Transport commercial',
        'Véhicule utilisé pour le transport commercial de marchandises', 1.50, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'PASSENGER', 'Transport de passagers', 'Véhicule utilisé pour le transport de passagers',
        1.70, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'RENTAL', 'Location', 'Véhicule de location', 1.80, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'TEACHING', 'Auto-école',
        'Véhicule utilisé pour l\'apprentissage de la conduite', 2.00, '00000000-0000-0000-0000-000000000000');
```

#### 3.1.6 Zones géographiques (spécifiques à la zone CIMA)

```sql
CREATE TABLE geographic_zones
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(20)   NOT NULL,
    name               VARCHAR(100)  NOT NULL,
    description        TEXT,
    tariff_coefficient DECIMAL(5, 2) NOT NULL,
    is_active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID          NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion des zones géographiques pour les pays de la zone CIMA
INSERT INTO geographic_zones (id, code, name, description, tariff_coefficient, organization_id)
VALUES (uuid_generate_v4(), 'ZONE1', 'Zone urbaine principale', 'Capitale et grandes villes', 1.30,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'ZONE2', 'Zone urbaine secondaire', 'Villes moyennes', 1.15,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'ZONE3', 'Zone semi-urbaine', 'Petites villes et périphéries', 1.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'ZONE4', 'Zone rurale', 'Villages et zones rurales', 0.90,
        '00000000-0000-0000-0000-000000000000');
```

#### 3.1.7 Antécédents de sinistres

```sql
CREATE TABLE claim_history_categories
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)   NOT NULL,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    min_claims       INT           NOT NULL,
    max_claims       INT,
    period_years     INT           NOT NULL,
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

-- Insertion des catégories d'antécédents de sinistres
INSERT INTO claim_history_categories (id, code, name, description, min_claims, max_claims, period_years,
                                      risk_coefficient, organization_id)
VALUES (uuid_generate_v4(), 'NO_CLAIMS', 'Aucun sinistre', 'Aucun sinistre déclaré sur la période', 0, 0, 3, 0.90,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'LOW_CLAIMS', 'Sinistres faibles', '1 sinistre déclaré sur la période', 1, 1, 3, 1.10,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'MEDIUM_CLAIMS', 'Sinistres moyens', '2 sinistres déclarés sur la période', 2, 2, 3, 1.25,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'HIGH_CLAIMS', 'Sinistres élevés', '3 sinistres ou plus déclarés sur la période', 3, NULL,
        3, 1.50, '00000000-0000-0000-0000-000000000000');
```

#### 3.1.8 Types de garanties spécifiques

```sql
CREATE TABLE specific_coverage_types
(
    id              UUID PRIMARY KEY,
    code            VARCHAR(20)  NOT NULL,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID         NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion des types de garanties spécifiques
INSERT INTO specific_coverage_types (id, code, name, description, organization_id)
VALUES (uuid_generate_v4(), 'GLASS', 'Bris de glace', 'Couverture des dommages aux vitres et pare-brise',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'NATURAL', 'Catastrophes naturelles',
        'Couverture des dommages causés par des catastrophes naturelles', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'THEFT', 'Vol', 'Couverture en cas de vol du véhicule',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'FIRE', 'Incendie', 'Couverture des dommages causés par un incendie',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'PERSONAL', 'Dommages corporels', 'Couverture des dommages corporels du conducteur',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'ASSISTANCE', 'Assistance', 'Services d\'assistance en cas de panne ou d\'accident',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'LEGAL', 'Protection juridique', 'Assistance juridique en cas de litige',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'GAP', 'Garantie valeur à neuf', 'Remboursement de la valeur à neuf en cas de perte totale',
        '00000000-0000-0000-0000-000000000000');
```

#### 3.1.9 Franchises standard

```sql
CREATE TABLE standard_deductibles
(
    id                         UUID PRIMARY KEY,
    code                       VARCHAR(20)    NOT NULL,
    name                       VARCHAR(100)   NOT NULL,
    description                TEXT,
    amount                     DECIMAL(10, 2) NOT NULL,
    premium_adjustment_percent DECIMAL(5, 2)  NOT NULL,
    is_active                  BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at                 TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMP,
    created_by                 UUID,
    updated_by                 UUID,
    organization_id            UUID           NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion des franchises standard
INSERT INTO standard_deductibles (id, code, name, description, amount, premium_adjustment_percent, organization_id)
VALUES (uuid_generate_v4(), 'DEDUCT_NONE', 'Sans franchise', 'Aucune franchise applicable', 0.00, 0.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'DEDUCT_LOW', 'Franchise basse', 'Franchise de niveau bas', 150.00, -5.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'DEDUCT_MEDIUM', 'Franchise moyenne', 'Franchise de niveau moyen', 300.00, -10.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'DEDUCT_HIGH', 'Franchise élevée', 'Franchise de niveau élevé', 500.00, -15.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'DEDUCT_VERY_HIGH', 'Franchise très élevée', 'Franchise de niveau très élevé', 1000.00,
        -25.00, '00000000-0000-0000-0000-000000000000');
```

#### 3.1.10 Réductions/Majorations standard

```sql
CREATE TABLE standard_adjustments
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(20)   NOT NULL,
    name               VARCHAR(100)  NOT NULL,
    description        TEXT,
    adjustment_type    VARCHAR(20)   NOT NULL, -- DISCOUNT, SURCHARGE
    adjustment_percent DECIMAL(5, 2) NOT NULL,
    is_active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID          NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion des réductions/majorations standard
INSERT INTO standard_adjustments (id, code, name, description, adjustment_type, adjustment_percent, organization_id)
VALUES (uuid_generate_v4(), 'LOYALTY', 'Fidélité', 'Réduction pour fidélité client', 'DISCOUNT', 5.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'MULTI_POLICY', 'Multi-contrats', 'Réduction pour détention de plusieurs contrats',
        'DISCOUNT', 10.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'YOUNG_DRIVER', 'Jeune conducteur', 'Majoration pour conducteur de moins de 25 ans',
        'SURCHARGE', 25.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'NEW_LICENSE', 'Permis récent', 'Majoration pour permis de moins de 2 ans', 'SURCHARGE',
        20.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'SAFE_DRIVER', 'Conducteur prudent', 'Réduction pour absence de sinistre sur 5 ans',
        'DISCOUNT', 15.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'LOW_MILEAGE', 'Faible kilométrage',
        'Réduction pour kilométrage annuel inférieur à 10 000 km', 'DISCOUNT', 8.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'SECURITY_DEVICE', 'Dispositif de sécurité',
        'Réduction pour dispositif antivol ou de localisation', 'DISCOUNT', 7.00,
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'ELECTRIC_VEHICLE', 'Véhicule électrique', 'Réduction pour véhicule électrique', 'DISCOUNT',
        10.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'PROFESSIONAL_USE', 'Usage professionnel', 'Majoration pour usage professionnel',
        'SURCHARGE', 15.00, '00000000-0000-0000-0000-000000000000');
```

### 3.2 Entités spécifiques à l'assurance automobile

#### 3.2.1 Véhicule

```sql
CREATE TABLE vehicles
(
    id                  UUID PRIMARY KEY,
    registration_number VARCHAR(20) NOT NULL,
    make_id             UUID        NOT NULL,
    model_id            UUID        NOT NULL,
    version             VARCHAR(100),
    year                INT         NOT NULL,
    engine_power        INT,
    engine_size         INT,
    fuel_type_id        UUID        NOT NULL,
    category_id         UUID        NOT NULL,
    subcategory_id      UUID,
    usage_id            UUID        NOT NULL,
    geographic_zone_id  UUID        NOT NULL,
    purchase_date       DATE,
    purchase_value      DECIMAL(19, 2),
    current_value       DECIMAL(19, 2),
    mileage             INT,
    vin                 VARCHAR(50), -- Vehicle Identification Number
    color_id            UUID        NOT NULL,
    owner_id            UUID        NOT NULL,
    created_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          UUID,
    updated_by          UUID,
    organization_id     UUID        NOT NULL,
    FOREIGN KEY (make_id) REFERENCES vehicle_makes (id),
    FOREIGN KEY (model_id) REFERENCES vehicle_models (id),
    FOREIGN KEY (fuel_type_id) REFERENCES fuel_types (id),
    FOREIGN KEY (category_id) REFERENCES vehicle_categories (id),
    FOREIGN KEY (subcategory_id) REFERENCES vehicle_subcategories (id),
    FOREIGN KEY (usage_id) REFERENCES vehicle_usages (id),
    FOREIGN KEY (geographic_zone_id) REFERENCES geographic_zones (id),
    FOREIGN KEY (color_id) REFERENCES vehicle_colors (id),
    FOREIGN KEY (owner_id) REFERENCES customers (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (registration_number, organization_id)
);
```

#### 3.2.2 Bonus-Malus

```sql
CREATE TABLE bonus_malus
(
    id                   UUID PRIMARY KEY,
    customer_id          UUID          NOT NULL,
    coefficient          DECIMAL(5, 2) NOT NULL, -- 0.50 to 3.50
    effective_date       DATE          NOT NULL,
    expiry_date          DATE,
    previous_coefficient DECIMAL(5, 2),
    years_without_claim  INT           NOT NULL DEFAULT 0,
    created_at           TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP,
    created_by           UUID,
    updated_by           UUID,
    organization_id      UUID          NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.2.3 Conducteur

```sql
CREATE TABLE drivers
(
    id                          UUID PRIMARY KEY,
    customer_id                 UUID        NOT NULL,
    license_number              VARCHAR(50) NOT NULL,
    license_type_id             UUID        NOT NULL,
    license_issue_date          DATE        NOT NULL,
    license_expiry_date         DATE,
    is_primary_driver           BOOLEAN     NOT NULL DEFAULT FALSE,
    years_of_driving_experience INT         NOT NULL DEFAULT 0,
    created_at                  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMP,
    created_by                  UUID,
    updated_by                  UUID,
    organization_id             UUID        NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (license_type_id) REFERENCES driving_license_types (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (license_number, organization_id)
);
```

#### 3.2.4 Police automobile

```sql
CREATE TABLE auto_policies
(
    policy_id                 UUID PRIMARY KEY,
    vehicle_id                UUID          NOT NULL,
    primary_driver_id         UUID          NOT NULL,
    coverage_type             VARCHAR(20)   NOT NULL, -- THIRD_PARTY, COMPREHENSIVE
    bonus_malus_coefficient   DECIMAL(5, 2) NOT NULL,
    annual_mileage            INT,
    parking_type              VARCHAR(20),            -- GARAGE, STREET, PARKING_LOT
    has_anti_theft_device     BOOLEAN       NOT NULL DEFAULT FALSE,
    claim_history_category_id UUID          NOT NULL,
    created_at                TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMP,
    created_by                UUID,
    updated_by                UUID,
    organization_id           UUID          NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles (id),
    FOREIGN KEY (primary_driver_id) REFERENCES drivers (id),
    FOREIGN KEY (claim_history_category_id) REFERENCES claim_history_categories (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.2.5 Conducteurs additionnels

```sql
CREATE TABLE additional_drivers
(
    auto_policy_id  UUID      NOT NULL,
    driver_id       UUID      NOT NULL,
    is_occasional   BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID      NOT NULL,
    PRIMARY KEY (auto_policy_id, driver_id),
    FOREIGN KEY (auto_policy_id) REFERENCES auto_policies (policy_id),
    FOREIGN KEY (driver_id) REFERENCES drivers (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

### 3.3 Garanties spécifiques à l'assurance automobile

```sql
-- Insertion des garanties spécifiques à l'assurance auto
INSERT INTO coverages (id, code, name, description, coverage_type, is_optional, default_limit, deductible_id,
                       organization_id)
VALUES (uuid_generate_v4(), 'AUTO_TPL', 'Responsabilité Civile', 'Couvre les dommages causés à des tiers', 'LIABILITY',
        FALSE, 1000000.00, '[DEDUCT_NONE_ID]', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_FIRE', 'Incendie', 'Couvre les dommages causés par un incendie', 'DAMAGE', TRUE,
        50000.00, '[DEDUCT_MEDIUM_ID]', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_THEFT', 'Vol', 'Couvre le vol du véhicule', 'DAMAGE', TRUE, 50000.00,
        '[DEDUCT_MEDIUM_ID]', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_GLASS', 'Bris de Glace', 'Couvre les dommages aux vitres', 'DAMAGE', TRUE, 2000.00,
        '[DEDUCT_LOW_ID]', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_COLLISION', 'Collision', 'Couvre les dommages en cas de collision', 'DAMAGE', TRUE,
        50000.00, '[DEDUCT_HIGH_ID]', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_NATURAL', 'Catastrophes Naturelles',
        'Couvre les dommages causés par des catastrophes naturelles', 'DAMAGE', TRUE, 50000.00, '[DEDUCT_MEDIUM_ID]',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_ASSISTANCE', 'Assistance', 'Service d\'assistance en cas de panne ou d\'accident',
        'SERVICE', TRUE, NULL, '[DEDUCT_NONE_ID]', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_DRIVER', 'Protection du Conducteur', 'Couvre les dommages corporels du conducteur',
        'PERSONAL', TRUE, 50000.00, '[DEDUCT_NONE_ID]', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_LEGAL', 'Protection Juridique', 'Assistance juridique en cas de litige', 'LEGAL',
        TRUE, 10000.00, '[DEDUCT_NONE_ID]', '00000000-0000-0000-0000-000000000000');
```

### 3.4 Formules spécifiques à l'assurance automobile

```sql
-- Insertion des formules spécifiques à l'assurance auto
INSERT INTO formulas (id, code, name, description, product_id, organization_id)
VALUES (uuid_generate_v4(), 'AUTO_THIRD_PARTY', 'Tiers Simple',
        'Formule de base couvrant uniquement la responsabilité civile', '[PRODUCT_ID]',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_THIRD_PARTY_PLUS', 'Tiers Étendu',
        'Formule intermédiaire avec quelques garanties additionnelles', '[PRODUCT_ID]',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'AUTO_COMPREHENSIVE', 'Tous Risques', 'Formule complète couvrant tous les risques',
        '[PRODUCT_ID]', '00000000-0000-0000-0000-000000000000');

-- Association des garanties aux formules
INSERT INTO formula_coverages (formula_id, coverage_id, is_included, organization_id)
VALUES
    -- Tiers Simple
    ('[FORMULA_ID_THIRD_PARTY]', '[COVERAGE_ID_TPL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_THIRD_PARTY]', '[COVERAGE_ID_ASSISTANCE]', TRUE, '00000000-0000-0000-0000-000000000000'),

    -- Tiers Étendu
    ('[FORMULA_ID_THIRD_PARTY_PLUS]', '[COVERAGE_ID_TPL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_THIRD_PARTY_PLUS]', '[COVERAGE_ID_ASSISTANCE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_THIRD_PARTY_PLUS]', '[COVERAGE_ID_GLASS]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_THIRD_PARTY_PLUS]', '[COVERAGE_ID_FIRE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_THIRD_PARTY_PLUS]', '[COVERAGE_ID_THEFT]', TRUE, '00000000-0000-0000-0000-000000000000'),

    -- Tous Risques
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_TPL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_ASSISTANCE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_GLASS]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_FIRE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_THEFT]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_COLLISION]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_NATURAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_DRIVER]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMPREHENSIVE]', '[COVERAGE_ID_LEGAL]', TRUE, '00000000-0000-0000-0000-000000000000');
```

#### 3.1.8 Barèmes de tarification

```sql
CREATE TABLE tariff_scales
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(20)    NOT NULL,
    name               VARCHAR(100)   NOT NULL,
    description        TEXT,
    category_id        UUID           NOT NULL,
    subcategory_id     UUID,
    geographic_zone_id UUID           NOT NULL,
    usage_id           UUID           NOT NULL,
    base_premium       DECIMAL(19, 2) NOT NULL,
    is_active          BOOLEAN        NOT NULL DEFAULT TRUE,
    effective_date     DATE           NOT NULL,
    expiry_date        DATE,
    created_at         TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID           NOT NULL,
    FOREIGN KEY (category_id) REFERENCES vehicle_categories (id),
    FOREIGN KEY (subcategory_id) REFERENCES vehicle_subcategories (id),
    FOREIGN KEY (geographic_zone_id) REFERENCES geographic_zones (id),
    FOREIGN KEY (usage_id) REFERENCES vehicle_usages (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Insertion de quelques barèmes de tarification
INSERT INTO tariff_scales (id, code, name, description, category_id, subcategory_id, geographic_zone_id, usage_id,
                           base_premium, effective_date, organization_id)
VALUES (uuid_generate_v4(), 'TARIFF_1_1_1_1', 'Barème tourisme standard zone urbaine',
        'Barème pour véhicules de tourisme < 9 CV en zone urbaine principale à usage privé', '[CAT1_ID]',
        '[SUBCAT1_1_ID]', '[ZONE1_ID]', '[PRIVATE_ID]', 150000.00, '2023-01-01',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'TARIFF_1_2_1_1', 'Barème tourisme moyen zone urbaine',
        'Barème pour véhicules de tourisme 9-15 CV en zone urbaine principale à usage privé', '[CAT1_ID]',
        '[SUBCAT1_2_ID]', '[ZONE1_ID]', '[PRIVATE_ID]', 200000.00, '2023-01-01',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'TARIFF_1_3_1_1', 'Barème tourisme luxe zone urbaine',
        'Barème pour véhicules de tourisme > 15 CV en zone urbaine principale à usage privé', '[CAT1_ID]',
        '[SUBCAT1_3_ID]', '[ZONE1_ID]', '[PRIVATE_ID]', 300000.00, '2023-01-01',
        '00000000-0000-0000-0000-000000000000');
```

#### 3.2.7 Antécédents de sinistres

```sql
CREATE TABLE claim_history_categories
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)   NOT NULL UNIQUE,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    min_claims       INT           NOT NULL,
    max_claims       INT,
    period_years     INT           NOT NULL,
    risk_coefficient DECIMAL(5, 2) NOT NULL,
    is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID
);

-- Insertion des catégories d'antécédents de sinistres
INSERT INTO claim_history_categories (id, code, name, description, min_claims, max_claims, period_years,
                                      risk_coefficient)
VALUES (uuid_generate_v4(), 'NO_CLAIMS', 'Aucun sinistre', 'Aucun sinistre déclaré sur la période', 0, 0, 3, 0.90),
       (uuid_generate_v4(), 'LOW_CLAIMS', 'Sinistres faibles', '1 sinistre déclaré sur la période', 1, 1, 3, 1.10),
       (uuid_generate_v4(), 'MEDIUM_CLAIMS', 'Sinistres moyens', '2 sinistres déclarés sur la période', 2, 2, 3, 1.25),
       (uuid_generate_v4(), 'HIGH_CLAIMS', 'Sinistres élevés', '3 sinistres ou plus déclarés sur la période', 3, NULL,
        3, 1.50);
```

#### 3.2.8 Types de garanties spécifiques

```sql
CREATE TABLE specific_coverage_types
(
    id          UUID PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    created_by  UUID,
    updated_by  UUID
);

-- Insertion des types de garanties spécifiques
INSERT INTO specific_coverage_types (id, code, name, description)
VALUES (uuid_generate_v4(), 'GLASS', 'Bris de glace', 'Couverture des dommages aux vitres et pare-brise'),
       (uuid_generate_v4(), 'NATURAL', 'Catastrophes naturelles',
        'Couverture des dommages causés par des catastrophes naturelles'),
       (uuid_generate_v4(), 'THEFT', 'Vol', 'Couverture en cas de vol du véhicule'),
       (uuid_generate_v4(), 'FIRE', 'Incendie', 'Couverture des dommages causés par un incendie'),
       (uuid_generate_v4(), 'PERSONAL', 'Dommages corporels', 'Couverture des dommages corporels du conducteur'),
       (uuid_generate_v4(), 'ASSISTANCE', 'Assistance', 'Services d\'assistance en cas de panne ou d\'accident'),
       (uuid_generate_v4(), 'LEGAL', 'Protection juridique', 'Assistance juridique en cas de litige'),
       (uuid_generate_v4(), 'GAP', 'Garantie valeur à neuf',
        'Remboursement de la valeur à neuf en cas de perte totale');
```

#### 3.2.9 Franchises standard

```sql
CREATE TABLE standard_deductibles
(
    id                         UUID PRIMARY KEY,
    code                       VARCHAR(20)    NOT NULL UNIQUE,
    name                       VARCHAR(100)   NOT NULL,
    description                TEXT,
    amount                     DECIMAL(10, 2) NOT NULL,
    premium_adjustment_percent DECIMAL(5, 2)  NOT NULL,
    is_active                  BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at                 TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMP,
    created_by                 UUID,
    updated_by                 UUID
);

-- Insertion des franchises standard
INSERT INTO standard_deductibles (id, code, name, description, amount, premium_adjustment_percent)
VALUES (uuid_generate_v4(), 'DEDUCT_NONE', 'Sans franchise', 'Aucune franchise applicable', 0.00, 0.00),
       (uuid_generate_v4(), 'DEDUCT_LOW', 'Franchise basse', 'Franchise de niveau bas', 150.00, -5.00),
       (uuid_generate_v4(), 'DEDUCT_MEDIUM', 'Franchise moyenne', 'Franchise de niveau moyen', 300.00, -10.00),
       (uuid_generate_v4(), 'DEDUCT_HIGH', 'Franchise élevée', 'Franchise de niveau élevé', 500.00, -15.00),
       (uuid_generate_v4(), 'DEDUCT_VERY_HIGH', 'Franchise très élevée', 'Franchise de niveau très élevé', 1000.00,
        -25.00);
```

#### 3.2.10 Réductions/Majorations standard

```sql
CREATE TABLE standard_adjustments
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(20)   NOT NULL UNIQUE,
    name               VARCHAR(100)  NOT NULL,
    description        TEXT,
    adjustment_type    VARCHAR(20)   NOT NULL, -- DISCOUNT, SURCHARGE
    adjustment_percent DECIMAL(5, 2) NOT NULL,
    is_active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID
);

-- Insertion des réductions/majorations standard
INSERT INTO standard_adjustments (id, code, name, description, adjustment_type, adjustment_percent)
VALUES (uuid_generate_v4(), 'LOYALTY', 'Fidélité', 'Réduction pour fidélité client', 'DISCOUNT', 5.00),
       (uuid_generate_v4(), 'MULTI_POLICY', 'Multi-contrats', 'Réduction pour détention de plusieurs contrats',
        'DISCOUNT', 10.00),
       (uuid_generate_v4(), 'YOUNG_DRIVER', 'Jeune conducteur', 'Majoration pour conducteur de moins de 25 ans',
        'SURCHARGE', 25.00),
       (uuid_generate_v4(), 'NEW_LICENSE', 'Permis récent', 'Majoration pour permis de moins de 2 ans', 'SURCHARGE',
        20.00),
       (uuid_generate_v4(), 'SAFE_DRIVER', 'Conducteur prudent', 'Réduction pour absence de sinistre sur 5 ans',
        'DISCOUNT', 15.00),
       (uuid_generate_v4(), 'LOW_MILEAGE', 'Faible kilométrage',
        'Réduction pour kilométrage annuel inférieur à 10 000 km', 'DISCOUNT', 8.00),
       (uuid_generate_v4(), 'SECURITY_DEVICE', 'Dispositif de sécurité',
        'Réduction pour dispositif antivol ou de localisation', 'DISCOUNT', 7.00),
       (uuid_generate_v4(), 'ELECTRIC_VEHICLE', 'Véhicule électrique', 'Réduction pour véhicule électrique', 'DISCOUNT',
        10.00),
       (uuid_generate_v4(), 'PROFESSIONAL_USE', 'Usage professionnel', 'Majoration pour usage professionnel',
        'SURCHARGE', 15.00);
```

### 3.3 Entités spécifiques à l'assurance automobile

```sql
CREATE TABLE registration_countries
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

-- Insertion des pays d'immatriculation pour la zone CIMA
INSERT INTO registration_countries (id, code, name, description, risk_coefficient)
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

#### 3.1.17 Types de documents d'identité

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
```

## 4. Tables de référence spécifiques aux véhicules

### 4.1 Structure des tables

Les tables suivantes sont nécessaires pour la gestion des caractéristiques des véhicules :

#### 4.1.1 Carrosseries (vehicle_body_types)

```sql
CREATE TABLE vehicle_body_types
(
    id              UUID PRIMARY KEY,
    code            VARCHAR(20)  NOT NULL,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
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

#### 4.1.3 Zones de circulation (circulation_zones)

```sql
CREATE TABLE circulation_zones
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
    ADD COLUMN circulation_zone_id UUID NOT NULL REFERENCES circulation_zones (id);
```

### 4.2 Données de référence initiales

#### 4.2.1 Carrosseries standard

```sql
INSERT INTO vehicle_body_types (id, code, name, description, organization_id)
VALUES 
    (uuid_generate_v4(), 'BERLINE', 'Berline', 'Véhicule à coffre fermé', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'BREAK', 'Break', 'Véhicule familial à grand volume', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'SUV', 'SUV', 'Sport Utility Vehicle', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'PICKUP', 'Pick-up', 'Véhicule utilitaire avec benne', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'FOURGON', 'Fourgon', 'Véhicule utilitaire fermé', '00000000-0000-0000-0000-000000000000');
```

#### 4.2.2 Genres de véhicules standard

```sql
INSERT INTO vehicle_genres (id, code, name, description, organization_id)
VALUES 
    (uuid_generate_v4(), 'VP', 'Véhicule Particulier', 'Voiture de tourisme', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'CTTE', 'Camionnette', 'Véhicule utilitaire léger', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'CAM', 'Camion', 'Véhicule de transport de marchandises', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'TRR', 'Tracteur Routier', 'Véhicule de traction', '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'MOTO', 'Motocyclette', 'Deux-roues motorisé', '00000000-0000-0000-0000-000000000000');
```

#### 4.2.3 Zones de circulation standard

```sql
INSERT INTO circulation_zones (id, code, name, description, risk_coefficient, organization_id)
VALUES 
    (uuid_generate_v4(), 'ZONE1', 'Zone urbaine principale', 'Capitale et grandes villes', 1.30, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'ZONE2', 'Zone urbaine secondaire', 'Villes moyennes', 1.15, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'ZONE3', 'Zone semi-urbaine', 'Petites villes et périphéries', 1.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'ZONE4', 'Zone rurale', 'Villages et zones rurales', 0.90, '00000000-0000-0000-0000-000000000000');
```

### 4.3 Impact sur la tarification

Ces tables de référence ont un impact direct sur la tarification des polices auto :

- Le genre du véhicule influence le calcul de la prime de base
- La carrosserie peut entraîner des majorations ou réductions selon le risque associé
- La zone de circulation applique un coefficient multiplicateur sur la prime

Le service de tarification (`AutoPricingService`) devra prendre en compte ces paramètres dans son calcul.
       (uuid_generate_v4(), 'PASSPORT', 'Passeport', 'Passeport international', NULL),
       (uuid_generate_v4(), 'DRIVER_LICENSE', 'Permis de conduire', 'Permis de conduire', NULL),
       (uuid_generate_v4(), 'RESIDENCE_PERMIT', 'Carte de séjour', 'Carte de séjour pour résidents étrangers', NULL),
       (uuid_generate_v4(), 'VOTER_CARD', 'Carte d\'électeur', 'Carte d\'électeur', NULL),
       (uuid_generate_v4(), 'PROFESSIONAL_CARD', 'Carte professionnelle', 'Carte professionnelle', NULL);
```

## 4. API et endpoints

### 4.1 API de gestion des véhicules

```
GET /api/auto/vehicles - Liste tous les véhicules
GET /api/auto/vehicles/{id} - Récupère un véhicule par son ID
POST /api/auto/vehicles - Crée un nouveau véhicule
PUT /api/auto/vehicles/{id} - Met à jour un véhicule existant
DELETE /api/auto/vehicles/{id} - Supprime un véhicule
```

### 4.2 API de gestion des conducteurs

```
GET /api/auto/drivers - Liste tous les conducteurs
GET /api/auto/drivers/{id} - Récupère un conducteur par son ID
POST /api/auto/drivers - Crée un nouveau conducteur
PUT /api/auto/drivers/{id} - Met à jour un conducteur existant
DELETE /api/auto/drivers/{id} - Supprime un conducteur
```

### 4.3 API de gestion du bonus-malus

```
GET /api/auto/bonus-malus/{customerId} - Récupère le bonus-malus d'un client
POST /api/auto/bonus-malus - Crée un nouveau bonus-malus
PUT /api/auto/bonus-malus/{id} - Met à jour un bonus-malus existant
POST /api/auto/bonus-malus/{id}/calculate - Recalcule le coefficient de bonus-malus
```

### 4.4 API de gestion des polices auto

```
GET /api/auto/policies - Liste toutes les polices auto
GET /api/auto/policies/{id} - Récupère une police auto par son ID
POST /api/auto/policies - Crée une nouvelle police auto
PUT /api/auto/policies/{id} - Met à jour une police auto existante
DELETE /api/auto/policies/{id} - Supprime une police auto

POST /api/auto/policies/{id}/add-driver - Ajoute un conducteur à une police
DELETE /api/auto/policies/{id}/drivers/{driverId} - Supprime un conducteur d'une police
```

### 4.5 API de tarification auto

```
POST /api/auto/pricing/calculate - Calcule le prix d'une police auto
POST /api/auto/pricing/simulate - Simule différentes options de tarification
```

## 5. Règles métier spécifiques

### 5.1 Règles de tarification automobile

- Le tarif de base dépend de la puissance du véhicule, de son âge et de sa valeur
- Le coefficient de bonus-malus s'applique à la prime de base
- Des majorations sont appliquées pour les jeunes conducteurs (moins de 3 ans de permis)
- Des réductions sont accordées pour les conducteurs expérimentés (plus de 10 ans sans sinistre)
- Des réductions sont accordées pour les véhicules peu utilisés (faible kilométrage annuel)
- Des réductions sont accordées pour les véhicules garés dans un garage fermé
- Des majorations sont appliquées pour les véhicules utilisés à des fins professionnelles

### 5.2 Règles de gestion du bonus-malus

- Le coefficient de bonus-malus est compris entre 0.50 et 3.50
- Chaque année sans sinistre responsable entraîne une réduction de 5% du coefficient
- Chaque sinistre responsable entraîne une majoration de 25% du coefficient
- Le coefficient ne peut pas descendre en dessous de 0.50
- Le coefficient est personnel et suit le conducteur, pas le véhicule

### 5.3 Règles de souscription automobile

- Le conducteur principal doit être titulaire d'un permis de conduire valide
- Le véhicule doit être immatriculé au nom du souscripteur ou d'un membre de sa famille
- Pour les véhicules de plus de 4 ans, un contrôle technique valide est requis
- Pour les véhicules de forte puissance, une expérience de conduite minimale est requise
- Les conducteurs ayant eu plus de 2 sinistres responsables dans les 2 dernières années peuvent être refusés

## 6. Intégrations

### 6.1 Intégration avec les services externes

- **Service d'immatriculation** : Vérification des informations du véhicule
- **Service de permis de conduire** : Vérification de la validité du permis
- **Base de données des véhicules volés** : Vérification que le véhicule n'est pas signalé comme volé
- **Base de données des sinistres** : Récupération de l'historique des sinistres

### 6.2 Intégration avec les API nationales d'attestation

#### 6.2.1 Spécifications de l'API DIOTALI

DIOTALI (Dispositif Informatique Opérationnel de Traitement des Attestations et Livrets d'Identification) est le système
national sénégalais de gestion des attestations d'assurance automobile développé pour l'AAS (Association des Assureurs
du Sénégal). Voici ses principales caractéristiques selon la documentation officielle (version 1.9 - Juillet 2024) :

**Informations générales :**

- **URL de base** : `https://api.diotali.sn`
- **Environnement de test** : Fourni par Diotali
- **Format d'échange** : JSON
- **Méthode d'authentification** : OAuth 2.0 avec Bearer Token
- **Encodage** : UTF-8

**Endpoints principaux :**

| Nom                   | Description                                | Méthode | URL                                        |
|-----------------------|--------------------------------------------|---------|--------------------------------------------|
| init-command          | Initier une commande de QR Code virtuel    | POST    | `/compagnie/service/init-command`          |
| valid-command         | Valider une commande                       | POST    | `/compagnie/service/valid-command`         |
| commandes             | Lister les commandes effectuées            | GET     | `/compagnie/service/commandes`             |
| stock-qr              | Obtenir le stock actuel de QR Code virtuel | GET     | `/compagnie/service/stock-qr`              |
| cancel                | Annuler une commande                       | POST    | `/compagnie/service/cancel`                |
| rc-request            | Calculer le montant de la prime RC         | GET     | `/compagnie/service/rc-request`            |
| qrcode-request        | Générer un QR Code et une attestation      | POST    | `/compagnie/service/qrcode-request`        |
| qrcode-cancel         | Annuler/invalider une attestation          | POST    | `/compagnie/service/qrcode-cancel`         |
| moto-rc-request       | Calculer la prime RC pour deux roues       | GET     | `/compagnie/service/moto-rc-request`       |
| moto-qrcode-request   | Générer une attestation deux roues         | POST    | `/compagnie/service/moto-qrcode-request`   |
| rc-flotte-request     | Calculer la prime RC pour une flotte       | POST    | `/compagnie/service/rc-flotte-request`     |
| qrcode-flotte-request | Générer des attestations pour une flotte   | POST    | `/compagnie/service/qrcode-flotte-request` |

**Paramètres spécifiques :**

1. **Calcul de prime RC (rc-request) :**
    - puissanceFiscale : Puissance fiscale du véhicule
    - duree : Durée de l'assurance
    - periodicite : Unité de durée (JOUR, MOIS, ANNEE)
    - genre : Type de véhicule (VP, VU, etc.)
    - energie : Type d'énergie (ESSENCE, DIESEL)

2. **Génération d'attestation (qrcode-request) :**
    - responsabiliteCivile : Montant de la prime RC
    - referenceTrxPartner : Référence unique de la transaction
    - assure : Informations sur l'assuré (nom, prénom, cellulaire, email)
    - vehicule : Informations sur le véhicule (immatriculation, puissanceFiscale, genre, etc.)

**Métadonnées standardisées :**

1. **Genre (catégorie de véhicule) :**
    - VP : Véhicule Particulier
    - VU : Véhicule Utilitaire
    - TC : Transport en Commun
    - TM : Transport de Marchandises
    - ER : Engin de Route
    - DR : Deux Roues

2. **Energie :**
    - ESSENCE
    - DIESEL
    - ELECTRIQUE
    - HYBRIDE

3. **Périodicité :**
    - JOUR
    - MOIS
    - ANNEE

#### 6.2.2 Architecture d'intégration conforme à l'architecture hexagonale

Pour intégrer l'API DIOTALI au Sénégal, nous utiliserons une architecture hexagonale (Ports & Adapters) avec une
stratégie de configuration par pays :

```
com.devolution.saas.product.auto
├── domain                                # Domaine métier
│   ├── model                            # Modèles de domaine
│   │   └── attestation                  # Modèles liés aux attestations
│   │       ├── Attestation.java         # Entité d'attestation
│   │       └── AttestationRequest.java  # Objet de valeur pour la demande d'attestation
│   ├── port                             # Ports (interfaces définies par le domaine)
│   │   ├── in                           # Ports d'entrée (utilisés par les cas d'utilisation)
│   │   │   └── GenerateAttestationUseCase.java # Cas d'utilisation pour générer une attestation
│   │   └── out                          # Ports de sortie (utilisés par le domaine pour communiquer avec l'extérieur)
│   │       └── AttestationPort.java     # Port pour l'intégration avec les systèmes d'attestation
│   └── service                          # Services de domaine
│       └── AttestationService.java      # Service implémentant les cas d'utilisation liés aux attestations
│
├── application                           # Couche application
│   ├── port                             # Implémentations des ports d'entrée
│   │   └── in                           # Adaptateurs d'entrée
│   │       └── web                       # Adaptateurs web
│   │           └── AttestationController.java # Contrôleur REST pour les attestations
│   └── service                          # Services d'application
│       └── AttestationApplicationService.java # Service d'application pour les attestations
│
└── infrastructure                        # Couche infrastructure
    └── adapter                          # Adaptateurs d'infrastructure
        └── out                           # Adaptateurs de sortie
            └── attestation                # Adaptateurs pour les systèmes d'attestation
                ├── config                   # Configuration des adaptateurs
                │   ├── AttestationAdapterConfig.java # Configuration générique
                │   └── CountryApiProperties.java # Propriétés spécifiques par pays
                ├── entity                   # Entités de persistance
                │   └── AttestationApiConfigEntity.java # Entité pour la configuration des API
                ├── mapper                   # Mappeurs entre domaine et infrastructure
                │   └── AttestationMapper.java # Mappeur pour les attestations
                ├── repository               # Repositories
                │   └── AttestationApiConfigRepository.java # Repository pour les configurations d'API
                ├── client                   # Clients HTTP pour les API externes
                │   ├── DiotaliApiClient.java # Client pour l'API DIOTALI
                │   └── FanafApiClient.java   # Client pour une autre API nationale
                ├── DiotaliAttestationAdapter.java # Adaptateur pour DIOTALI
                ├── FanafAttestationAdapter.java # Adaptateur pour une autre API
                ├── DefaultAttestationAdapter.java # Adaptateur par défaut
                └── AttestationAdapterFactory.java # Factory pour les adaptateurs
```

#### 6.2.3 Implémentation du port d'attestation pour DIOTALI

Basé sur les spécifications détaillées de l'API DIOTALI, nous allons implémenter l'adaptateur spécifique pour le
Sénégal. Voici les détails de l'implémentation :

##### 6.2.3.1 Modèle de domaine pour l'attestation

```java
// Dans domain.model.attestation
public class Attestation {
    private UUID id;
    private String attestationNumber;
    private LocalDate issueDate;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String qrCodeUrl;
    private String pdfUrl;
    private AttestationStatus status;
    private Vehicle vehicle;
    private Insured insured;
    private BigDecimal premium;
    private String transactionReference;

    // Getters, setters, etc.
}

public class Vehicle {
    private String registrationNumber;
    private String make;
    private String model;
    private String chassisNumber;
    private int fiscalPower;
    private String category; // Genre dans DIOTALI (VP, VU, TC, etc.)
    private String energyType; // ESSENCE, DIESEL, etc.
    private LocalDate circulationDate;
    private int seats;
    private BigDecimal newValue;
    private BigDecimal currentValue;

    // Getters, setters, etc.
}

public class Insured {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;

    // Getters, setters, etc.
}

public enum AttestationStatus {
    DRAFT,
    ISSUED,
    CANCELLED,
    EXPIRED
}
```

##### 6.2.3.2 Port d'attestation enrichi pour DIOTALI

```java
// Dans domain.port.out
public interface AttestationPort {
    /**
     * Génère une attestation d'assurance
     * @param policyId ID de la police d'assurance
     * @param countryCode Code du pays (ISO 3166-1 alpha-2)
     * @return Attestation générée
     */
    Attestation generateAttestation(UUID policyId, String countryCode);

    /**
     * Génère des attestations pour une flotte de véhicules
     * @param fleetPolicyId ID de la police flotte
     * @param countryCode Code du pays (ISO 3166-1 alpha-2)
     * @return Liste des attestations générées
     */
    List<Attestation> generateFleetAttestations(UUID fleetPolicyId, String countryCode);

    /**
     * Vérifie la validité d'une attestation d'assurance
     * @param attestationNumber Numéro de l'attestation
     * @param countryCode Code du pays (ISO 3166-1 alpha-2)
     * @return true si l'attestation est valide, false sinon
     */
    boolean verifyAttestation(String attestationNumber, String countryCode);

    /**
     * Annule une attestation d'assurance
     * @param attestationNumber Numéro de l'attestation
     * @param countryCode Code du pays (ISO 3166-1 alpha-2)
     * @param reason Raison de l'annulation
     * @return true si l'annulation a réussi, false sinon
     */
    boolean cancelAttestation(String attestationNumber, String countryCode, String reason);

    /**
     * Calcule le montant de la prime RC
     * @param vehicleId ID du véhicule
     * @param duration Durée de l'assurance
     * @param periodicity Périodicité (JOUR, MOIS, ANNEE)
     * @param countryCode Code du pays (ISO 3166-1 alpha-2)
     * @return Montant de la prime RC
     */
    BigDecimal calculateRCPremium(UUID vehicleId, int duration, String periodicity, String countryCode);

    /**
     * Incorpore un véhicule dans une flotte existante
     * @param fleetPolicyId ID de la police flotte
     * @param vehicleId ID du véhicule à incorporer
     * @param countryCode Code du pays (ISO 3166-1 alpha-2)
     * @return Attestation générée pour le véhicule incorporé
     */
    Attestation incorporateVehicleToFleet(UUID fleetPolicyId, UUID vehicleId, String countryCode);

    /**
     * Supprime un véhicule d'une flotte existante
     * @param fleetPolicyId ID de la police flotte
     * @param vehicleId ID du véhicule à supprimer
     * @param countryCode Code du pays (ISO 3166-1 alpha-2)
     * @return true si la suppression a réussi, false sinon
     */
    boolean removeVehicleFromFleet(UUID fleetPolicyId, UUID vehicleId, String countryCode);
}
```

##### 6.2.3.3 Client API pour DIOTALI

```java
// Dans infrastructure.adapter.out.attestation.client
@Component
public class DiotaliApiClient {
    private final RestTemplate restTemplate;
    private final AttestationApiConfigRepository configRepository;
    private final String baseUrl;
    private String accessToken;
    private LocalDateTime tokenExpiration;

    @Autowired
    public DiotaliApiClient(RestTemplate restTemplate,
                            AttestationApiConfigRepository configRepository,
                            @Value("${diotali.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.configRepository = configRepository;
        this.baseUrl = baseUrl;
    }

    /**
     * Authentification à l'API DIOTALI
     */
    private void authenticate() {
        // Récupérer les identifiants de configuration
        AttestationApiConfigEntity config = configRepository.findByCountryCode("SN")
                .orElseThrow(() -> new ConfigurationException("Configuration DIOTALI non trouvée"));

        // Préparer la requête d'authentification
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("username", config.getAuthCredentials().get("username").asText());
        authRequest.put("password", config.getAuthCredentials().get("password").asText());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(authRequest, headers);

        // Appeler l'API d'authentification
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/compagnie/auth/login",
                HttpMethod.POST,
                entity,
                Map.class
        );

        // Extraire le token et sa durée de validité
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("access_token")) {
            this.accessToken = (String) responseBody.get("access_token");
            int expiresIn = (Integer) responseBody.getOrDefault("expires_in", 3600);
            this.tokenExpiration = LocalDateTime.now().plusSeconds(expiresIn);
        } else {
            throw new AuthenticationException("Authentification DIOTALI échouée");
        }
    }

    /**
     * Vérifie si le token est valide, sinon réauthentifie
     */
    private void ensureValidToken() {
        if (accessToken == null || LocalDateTime.now().isAfter(tokenExpiration)) {
            authenticate();
        }
    }

    /**
     * Calcule le montant de la prime RC
     */
    public BigDecimal calculateRCPremium(int fiscalPower, int duration, String periodicity, String genre, String energy) {
        ensureValidToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/compagnie/service/rc-request")
                .queryParam("puissanceFiscale", fiscalPower)
                .queryParam("duree", duration)
                .queryParam("periodicite", periodicity)
                .queryParam("genre", genre)
                .queryParam("energie", energy);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("responsabiliteCivile")) {
            return new BigDecimal(responseBody.get("responsabiliteCivile").toString());
        } else {
            throw new ApiException("Calcul de prime RC échoué");
        }
    }

    /**
     * Génère une attestation
     */
    public Map<String, Object> generateAttestation(DiotaliAttestationRequest request) {
        ensureValidToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DiotaliAttestationRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/compagnie/service/qrcode-request",
                HttpMethod.POST,
                entity,
                Map.class
        );

        return response.getBody();
    }

    // Autres méthodes pour les différentes opérations DIOTALI
    // ...
}
```

##### 6.2.3.4 Implémentation de l'adaptateur DIOTALI

```java
// Dans infrastructure.adapter.out.attestation
@Service
@ConditionalOnProperty(name = "attestation.api.country", havingValue = "SN")
public class DiotaliAttestationAdapter implements AttestationPort {

    private final DiotaliApiClient apiClient;
    private final PolicyRepository policyRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final FuelTypeRepository fuelTypeRepository;
    private final OrganizationFuelTypeAdjustmentRepository organizationFuelTypeAdjustmentRepository;
    private final AttestationMapper mapper;

    @Autowired
    public DiotaliAttestationAdapter(DiotaliApiClient apiClient,
                                     PolicyRepository policyRepository,
                                     VehicleRepository vehicleRepository,
                                     CustomerRepository customerRepository,
                                     FuelTypeRepository fuelTypeRepository,
                                     OrganizationFuelTypeAdjustmentRepository organizationFuelTypeAdjustmentRepository,
                                     AttestationMapper mapper) {
        this.apiClient = apiClient;
        this.policyRepository = policyRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.organizationFuelTypeAdjustmentRepository = organizationFuelTypeAdjustmentRepository;
        this.mapper = mapper;
    }

    @Override
    public Attestation generateAttestation(UUID policyId, String countryCode) {
        // 1. Récupérer les données de la police
        AutoPolicy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new EntityNotFoundException("Police non trouvée"));

        // 2. Récupérer les données du véhicule
        Vehicle vehicle = vehicleRepository.findById(policy.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Véhicule non trouvé"));

        // 3. Récupérer les données du client
        Customer customer = customerRepository.findById(policy.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));

        // 4. Préparer la requête pour DIOTALI
        DiotaliAttestationRequest request = new DiotaliAttestationRequest();
        request.setResponsabiliteCivile(policy.getPremiumAmount());
        request.setReferenceTrxPartner(policy.getPolicyNumber());

        // Informations sur l'assuré
        DiotaliAssureRequest assure = new DiotaliAssureRequest();
        assure.setNom(customer.getLastName());
        assure.setPrenom(customer.getFirstName());
        assure.setCellulaire(customer.getPhoneNumber());
        assure.setEmail(customer.getEmail());
        request.setAssure(assure);

        // Informations sur le véhicule
        DiotaliVehiculeRequest vehicule = new DiotaliVehiculeRequest();
        vehicule.setPuissanceFiscale(vehicle.getFiscalPower());
        vehicule.setDateMiseCirculation(vehicle.getCirculationDate().format(DateTimeFormatter.ISO_DATE));
        vehicule.setNombrePlace(vehicle.getSeats());
        vehicule.setValeurNeuve(vehicle.getNewValue());
        vehicule.setValeurActuelle(vehicle.getCurrentValue());
        vehicule.setImmatriculation(vehicle.getRegistrationNumber());
        // Récupérer le type de carburant à partir de l'ID
        FuelType fuelType = fuelTypeRepository.findById(vehicle.getFuelTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type de carburant non trouvé"));

        // Vérifier s'il existe des ajustements spécifiques à l'organisation
        BigDecimal riskFactor = fuelType.getRiskFactor();
        Optional<OrganizationFuelTypeAdjustment> adjustment = organizationFuelTypeAdjustmentRepository
                .findByOrganizationIdAndFuelTypeId(policy.getOrganizationId(), vehicle.getFuelTypeId());

        if (adjustment.isPresent() && adjustment.get().getRiskFactorAdjustment() != null) {
            riskFactor = riskFactor.add(adjustment.get().getRiskFactorAdjustment());
        }

        // Appliquer le facteur de risque dans le calcul de la prime
        BigDecimal adjustedPremium = policy.getPremiumAmount().multiply(riskFactor);
        request.setResponsabiliteCivile(adjustedPremium);

        vehicule.setEnergie(fuelType.getCode());
        vehicule.setGenre(mapVehicleCategory(vehicle.getCategory()));
        vehicule.setModele(vehicle.getModel());
        vehicule.setMarque(vehicle.getMake());
        vehicule.setChassis(vehicle.getChassisNumber());
        request.setVehicule(vehicule);

        // 5. Appeler l'API DIOTALI
        Map<String, Object> response = apiClient.generateAttestation(request);

        // 6. Mapper la réponse vers le modèle de domaine
        return mapper.mapToDomainAttestation(response, policy, vehicle, customer);
    }

    @Override
    public boolean verifyAttestation(String attestationNumber, String countryCode) {
        try {
            Map<String, Object> response = apiClient.verifyAttestation(attestationNumber);
            return response != null && "VALID".equals(response.get("status"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean cancelAttestation(String attestationNumber, String countryCode, String reason) {
        try {
            Map<String, Object> response = apiClient.cancelAttestation(attestationNumber, reason);
            return response != null && "SUCCESS".equals(response.get("status"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public BigDecimal calculateRCPremium(UUID vehicleId, int duration, String periodicity, String countryCode) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Véhicule non trouvé"));

        // Récupérer le type de carburant à partir de l'ID
        FuelType fuelType = fuelTypeRepository.findById(vehicle.getFuelTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type de carburant non trouvé"));

        // Vérifier s'il existe des ajustements spécifiques à l'organisation
        BigDecimal powerConversionFactor = BigDecimal.valueOf(fuelType.getPowerConversionFactor());
        Optional<OrganizationFuelTypeAdjustment> adjustment = organizationFuelTypeAdjustmentRepository
                .findByOrganizationIdAndFuelTypeId(vehicle.getOrganizationId(), vehicle.getFuelTypeId());

        if (adjustment.isPresent() && adjustment.get().getPowerConversionFactorAdjustment() != null) {
            powerConversionFactor = powerConversionFactor.add(adjustment.get().getPowerConversionFactorAdjustment());
        }

        // Appliquer le facteur de conversion de puissance spécifique au type de carburant
        int adjustedFiscalPower = (int) Math.ceil(vehicle.getFiscalPower() * powerConversionFactor.doubleValue());

        return apiClient.calculateRCPremium(
                adjustedFiscalPower,
                duration,
                mapPeriodicity(periodicity),
                mapVehicleCategory(vehicle.getCategory()),
                fuelType.getCode()
        );
    }

    // Implémentation des autres méthodes du port
    // ...

    // Autres méthodes de mappage...

    /**
     * Mappe la catégorie de véhicule interne vers le format DIOTALI
     */
    private String mapVehicleCategory(String category) {
        switch (category.toUpperCase()) {
            case "PASSENGER_CAR":
                return "VP";
            case "UTILITY_VEHICLE":
                return "VU";
            case "PUBLIC_TRANSPORT":
                return "TC";
            case "GOODS_TRANSPORT":
                return "TM";
            case "ROAD_EQUIPMENT":
                return "ER";
            case "TWO_WHEELER":
                return "DR";
            default:
                return "VP";
        }
    }

    /**
     * Mappe la périodicité interne vers le format DIOTALI
     */
    private String mapPeriodicity(String periodicity) {
        switch (periodicity.toUpperCase()) {
            case "DAY":
                return "JOUR";
            case "MONTH":
                return "MOIS";
            case "YEAR":
                return "ANNEE";
            default:
                return "MOIS";
        }
    }
}
```

#### 6.2.4 Configuration et déploiement

Pour finaliser l'intégration avec l'API DIOTALI, nous devons ajouter les configurations nécessaires dans notre
application :

##### 6.2.4.1 Propriétés de configuration

```properties
# Configuration DIOTALI
diotali.api.base-url=https://api.diotali.sn
diotali.api.timeout=30000
diotali.api.retry-count=3
diotali.api.token-cache-duration=3600
```

##### 6.2.4.2 Configuration Spring

```java

@Configuration
public class DiotaliConfig {

    @Bean
    @ConditionalOnProperty(name = "attestation.api.country", havingValue = "SN")
    public RestTemplate diotaliRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Configurer les timeouts
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        restTemplate.setRequestFactory(factory);

        // Ajouter les intercepteurs pour la gestion des erreurs
        restTemplate.setErrorHandler(new DiotaliResponseErrorHandler());

        return restTemplate;
    }

    @Bean
    @ConditionalOnProperty(name = "attestation.api.country", havingValue = "SN")
    public DiotaliApiClient diotaliApiClient(RestTemplate restTemplate,
                                             AttestationApiConfigRepository configRepository,
                                             @Value("${diotali.api.base-url}") String baseUrl) {
        return new DiotaliApiClient(restTemplate, configRepository, baseUrl);
    }
}
```

##### 6.2.4.3 Initialisation des données de configuration

```sql
-- Insertion de la configuration pour l'API DIOTALI
INSERT INTO attestation_api_configs
(id, country_code, api_name, api_url, api_version, auth_type, auth_credentials, request_format, response_format,
 timeout_seconds, retry_count, is_active, additional_params, organization_id)
VALUES (uuid_generate_v4(), 'SN', 'DIOTALI', 'https://api.diotali.sn', 'v1.9', 'OAUTH',
        '{"username": "your_username", "password": "your_password"}', 'JSON', 'JSON', 30, 3, true,
        '{"required_fields": ["puissanceFiscale", "duree", "periodicite", "genre", "energie"], "genres": ["VP", "VU", "TC", "TM", "ER", "DR"], "energies": ["ESSENCE", "DIESEL", "ELECTRIQUE", "HYBRIDE"], "periodicites": ["JOUR", "MOIS", "ANNEE"]}',
        '00000000-0000-0000-0000-000000000000');
```

#### 6.2.5 Gestion des cas spécifiques

L'API DIOTALI offre des fonctionnalités spécifiques pour certains types de véhicules qui nécessitent une attention
particulière :

##### 6.2.5.1 Gestion des deux-roues (motos)

L'API DIOTALI propose des endpoints spécifiques pour les véhicules à deux roues (catégorie 5) :

```java
// Dans DiotaliApiClient
public BigDecimal calculateMotorcyclePremium(int fiscalPower, int duration, String periodicity, String usage) {
    ensureValidToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/compagnie/service/moto-rc-request")
            .queryParam("puissanceFiscale", fiscalPower)
            .queryParam("duree", duration)
            .queryParam("periodicite", periodicity)
            .queryParam("usage", usage);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<Map> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            entity,
            Map.class
    );

    Map<String, Object> responseBody = response.getBody();
    if (responseBody != null && responseBody.containsKey("responsabiliteCivile")) {
        return new BigDecimal(responseBody.get("responsabiliteCivile").toString());
    } else {
        throw new ApiException("Calcul de prime RC moto échoué");
    }
}

public Map<String, Object> generateMotorcycleAttestation(DiotaliMotoAttestationRequest request) {
    ensureValidToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiotaliMotoAttestationRequest> entity = new HttpEntity<>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/compagnie/service/moto-qrcode-request",
            HttpMethod.POST,
            entity,
            Map.class
    );

    return response.getBody();
}
```

##### 6.2.5.2 Gestion des flottes

L'API DIOTALI propose également des endpoints spécifiques pour la gestion des flottes de véhicules :

```java
// Dans DiotaliApiClient
public Map<String, Object> generateFleetAttestations(DiotaliFleetAttestationRequest request) {
    ensureValidToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiotaliFleetAttestationRequest> entity = new HttpEntity<>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/compagnie/service/qrcode-flotte-request",
            HttpMethod.POST,
            entity,
            Map.class
    );

    return response.getBody();
}

public Map<String, Object> incorporateVehicleToFleet(DiotaliFleetIncorporationRequest request) {
    ensureValidToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiotaliFleetIncorporationRequest> entity = new HttpEntity<>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/compagnie/service/incorpore-flotte-request",
            HttpMethod.POST,
            entity,
            Map.class
    );

    return response.getBody();
}

public Map<String, Object> removeVehicleFromFleet(DiotaliFleetSubtractionRequest request) {
    ensureValidToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiotaliFleetSubtractionRequest> entity = new HttpEntity<>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/compagnie/service/subtract-flotte-request",
            HttpMethod.POST,
            entity,
            Map.class
    );

    return response.getBody();
}
```

##### 6.2.5.3 Gestion des remorques

L'API DIOTALI propose des endpoints spécifiques pour les remorques :

```java
// Dans DiotaliApiClient
public BigDecimal calculateTrailerPremium(int fiscalPower, int duration, String periodicity, String genre, String energy) {
    ensureValidToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/compagnie/service/remorque-rc-request")
            .queryParam("puissanceFiscale", fiscalPower)
            .queryParam("duree", duration)
            .queryParam("periodicite", periodicity)
            .queryParam("genre", genre)
            .queryParam("energie", energy);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<Map> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            entity,
            Map.class
    );

    Map<String, Object> responseBody = response.getBody();
    if (responseBody != null && responseBody.containsKey("responsabiliteCivile")) {
        return new BigDecimal(responseBody.get("responsabiliteCivile").toString());
    } else {
        throw new ApiException("Calcul de prime RC remorque échoué");
    }
}

public Map<String, Object> generateTrailerAttestation(DiotaliTrailerAttestationRequest request) {
    ensureValidToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiotaliTrailerAttestationRequest> entity = new HttpEntity<>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/compagnie/service/remorque-qrcode-request",
            HttpMethod.POST,
            entity,
            Map.class
    );

    return response.getBody();
}
```

#### 6.2.6 Conclusion sur l'intégration avec DIOTALI

L'intégration avec l'API DIOTALI pour la génération d'attestations d'assurance automobile au Sénégal est implémentée en
suivant les principes de l'architecture hexagonale. Cette approche permet :

1. **Indépendance du domaine** : Le domaine métier reste indépendant des détails techniques de l'API DIOTALI
2. **Flexibilité** : Possibilité d'ajouter facilement d'autres adaptateurs pour d'autres pays
3. **Testabilité** : Facilité de tester le domaine indépendamment de l'API externe
4. **Maintenabilité** : Séparation claire des responsabilités

Les spécificités de l'API DIOTALI (gestion des deux-roues, des flottes, des remorques) sont prises en compte dans
l'implémentation, tout en maintenant une interface cohérente pour le domaine métier.

### 6.3 Intégration avec les modules internes

- **Module client** : Récupération des informations client
- **Module de paiement** : Gestion des paiements de prime
- **Module de sinistre** : Déclaration et suivi des sinistres automobiles
- **Module de document** : Génération des documents spécifiques à l'assurance auto

## 7. Plan d'implémentation

### 7.1 Étapes d'implémentation

1. **Phase 1 : Mise en place des tables de paramétrage spécifiques à la zone CIMA**
    - Création des tables de paramétrage spécifiques à la zone CIMA
    - Création des entités spécifiques à l'auto
    - Mise en place des repositories
    - Implémentation des migrations de base de données

2. **Phase 2 : Développement des services métier**
    - Implémentation du service de gestion des véhicules
    - Implémentation du service de gestion des conducteurs
    - Implémentation du service de gestion du bonus-malus
    - Implémentation du service de tarification auto

3. **Phase 3 : Développement des API**
    - Implémentation des contrôleurs REST
    - Mise en place des DTOs
    - Implémentation des validateurs

4. **Phase 4 : Intégration avec les composants communs**
    - Intégration avec le service de police commun
    - Intégration avec le service de tarification commun
    - Intégration avec le service de client

5. **Phase 5 : Intégration avec les API nationales d'attestation**
    - Mise en place de l'infrastructure d'intégration
    - Implémentation de l'adaptateur pour DIOTALI (Sénégal)
    - Implémentation de l'adaptateur par défaut
    - Mise en place du système de configuration par pays
    - Tests d'intégration avec les API nationales

6. **Phase 6 : Tests et validation**
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
| Phase 1 | 3 semaines    | Module commun          |
| Phase 2 | 3 semaines    | Phase 1                |
| Phase 3 | 2 semaines    | Phase 2                |
| Phase 4 | 2 semaines    | Phase 3, Module commun |
| Phase 5 | 3 semaines    | Phase 4                |
| Phase 6 | 2 semaines    | Phase 5                |

## 8. Tests

### 8.1 Tests unitaires

- Tests des services de gestion des véhicules
- Tests des services de gestion des conducteurs
- Tests des services de gestion du bonus-malus
- Tests des services de tarification auto
- Tests des règles métier spécifiques à l'auto

### 8.2 Tests d'intégration

- Tests d'intégration avec la base de données
- Tests d'intégration avec les services communs
- Tests des API REST

### 8.3 Tests de performance

- Tests de charge pour le calcul de prime auto
- Tests de performance pour la recherche de véhicules
- Tests de performance pour la création de polices auto

### 8.4 Tests d'intégration avec les API nationales

- Tests de connexion aux API nationales d'attestation
- Tests de génération d'attestation via DIOTALI
- Tests de vérification d'attestation
- Tests de gestion des erreurs et des cas limites
- Tests de performance des appels API

### 8.5 Tests métier

- Validation des règles de tarification
- Validation des règles de souscription
- Validation des règles de gestion du bonus-malus
- Validation des processus d'attestation par pays

## 9. Documentation

### 9.1 Documentation technique

- Documentation des API avec Swagger/OpenAPI
- Documentation des modèles de données
- Documentation des règles métier implémentées
- Documentation détaillée de l'intégration avec DIOTALI

### 9.2 Documentation utilisateur

- Guide d'utilisation des API
- Guide de configuration des produits auto
- Guide de tarification auto
- Guide d'intégration avec les API nationales d'attestation
- Documentation spécifique par pays pour les attestations

## 10. Conclusion

Ce document fournit un plan détaillé pour l'implémentation du produit d'assurance automobile, incluant les spécificités
de la zone CIMA et l'intégration avec les API nationales d'attestation comme DIOTALI au Sénégal. Il servira de guide
pour l'équipe de développement et assurera une intégration harmonieuse avec les autres modules du système.

L'architecture d'intégration avec les API nationales a été conçue pour être flexible et adaptable, permettant d'ajouter
facilement de nouvelles intégrations pour d'autres pays sans modifier le code existant. Cette approche basée sur le
pattern Adapter et la configuration par pays garantit la maintenabilité et l'extensibilité du système.

Les tables de paramétrage spécifiques à la zone CIMA (catégories, sous-catégories, marques, modèles, types de carburant,
usages, zones géographiques) permettent une gestion fine des spécificités réglementaires et commerciales de chaque pays
de la zone.

L'équipe en charge de l'implémentation du produit auto devra suivre ce plan tout en respectant les directives communes
définies dans le document `plan-commun.md`.

- Création des tables de paramétrage spécifiques à la zone CIMA
- Création des entités spécifiques à l'auto
- Mise en place des repositories
- Implémentation des migrations de base de données

2. **Phase 2 : Développement des services métier**
    - Implémentation du service de gestion des véhicules
    - Implémentation du service de gestion des conducteurs
    - Implémentation du service de gestion du bonus-malus
    - Implémentation du service de tarification auto

3. **Phase 3 : Développement des API**
    - Implémentation des contrôleurs REST
    - Mise en place des DTOs
    - Implémentation des validateurs

4. **Phase 4 : Intégration avec les composants communs**
    - Intégration avec le service de police commun
    - Intégration avec le service de tarification commun
    - Intégration avec le service de client

5. **Phase 5 : Intégration avec les API nationales d'attestation**
    - Mise en place de l'infrastructure d'intégration
    - Implémentation de l'adaptateur pour DIOTALI (Sénégal)
    - Implémentation de l'adaptateur par défaut
    - Mise en place du système de configuration par pays
    - Tests d'intégration avec les API nationales

6. **Phase 6 : Tests et validation**
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
| Phase 1 | 3 semaines    | Module commun          |
| Phase 2 | 3 semaines    | Phase 1                |
| Phase 3 | 2 semaines    | Phase 2                |
| Phase 4 | 2 semaines    | Phase 3, Module commun |
| Phase 5 | 3 semaines    | Phase 4                |
| Phase 6 | 2 semaines    | Phase 5                |

## 8. Tests

### 8.1 Tests unitaires

- Tests des services de gestion des véhicules
- Tests des services de gestion des conducteurs
- Tests des services de gestion du bonus-malus
- Tests des services de tarification auto
- Tests des règles métier spécifiques à l'auto

### 8.2 Tests d'intégration

- Tests d'intégration avec la base de données
- Tests d'intégration avec les services communs
- Tests des API REST

### 8.3 Tests de performance

- Tests de charge pour le calcul de prime auto
- Tests de performance pour la recherche de véhicules
- Tests de performance pour la création de polices auto

### 8.4 Tests d'intégration avec les API nationales

- Tests de connexion aux API nationales d'attestation
- Tests de génération d'attestation via DIOTALI
- Tests de vérification d'attestation
- Tests de gestion des erreurs et des cas limites
- Tests de performance des appels API

### 8.5 Tests métier

- Validation des règles de tarification
- Validation des règles de souscription
- Validation des règles de gestion du bonus-malus
- Validation des processus d'attestation par pays

## 9. Documentation

### 9.1 Documentation technique

- Documentation des API avec Swagger/OpenAPI
- Documentation des modèles de données
- Documentation des règles métier implémentées

### 9.2 Documentation utilisateur

- Guide d'utilisation des API
- Guide de configuration des produits auto
- Guide de tarification auto
- Guide d'intégration avec les API nationales d'attestation
- Documentation spécifique par pays pour les attestations

## 10. Conclusion

Ce document fournit un plan détaillé pour l'implémentation du produit d'assurance automobile, incluant les spécificités
de la zone CIMA et l'intégration avec les API nationales d'attestation comme DIOTALI au Sénégal. Il servira de guide
pour l'équipe de développement et assurera une intégration harmonieuse avec les autres modules du système.

L'architecture d'intégration avec les API nationales a été conçue pour être flexible et adaptable, permettant d'ajouter
facilement de nouvelles intégrations pour d'autres pays sans modifier le code existant. Cette approche basée sur le
pattern Adapter et la configuration par pays garantit la maintenabilité et l'extensibilité du système.

Ce document se concentre sur les éléments spécifiques à l'assurance automobile, tandis que les tables de référence
générales (professions, types de carburant, couleurs de véhicules, types de permis, types de documents d'identité, pays)
sont définies dans le document `plan-commun.md`.

Les tables de référence spécifiques à l'assurance automobile (catégories de véhicules, sous-catégories, marques,
modèles, usages, zones géographiques, antécédents de sinistres, garanties, franchises, réductions/majorations) sont
personnalisées par organisation pour permettre une gestion fine des spécificités réglementaires et commerciales de
chaque pays de la zone CIMA.

L'architecture d'intégration avec les API nationales d'attestation comme DIOTALI au Sénégal a été conçue selon les
principes de l'architecture hexagonale, avec une séparation claire entre le domaine métier et les adaptateurs
d'infrastructure.

L'équipe en charge de l'implémentation du produit auto devra suivre ce plan tout en respectant les directives communes
définies dans le document `plan-commun.md`.
