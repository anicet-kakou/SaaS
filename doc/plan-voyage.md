# Plan d'implémentation du produit Assurance Voyage

**Version :** 1.0.0  
**Date de dernière mise à jour :** 2023-11-15  
**Statut :** Document de travail  
**Classification :** Confidentiel - Usage interne uniquement

## 1. Introduction

Ce document décrit le plan d'implémentation du produit d'assurance voyage dans le cadre du projet SaaS multi-tenant
assurantiel. Il détaille l'architecture, les modèles de données, les API et les règles métier spécifiques à ce produit.

### 1.1 Objectifs

- Définir l'architecture spécifique au produit d'assurance voyage
- Détailler les modèles de données propres à ce produit
- Spécifier les API et endpoints nécessaires
- Documenter les règles métier spécifiques à l'assurance voyage
- Établir un plan d'implémentation clair pour l'équipe de développement

### 1.2 Portée

Ce document couvre l'implémentation complète du produit d'assurance voyage, y compris :

- La gestion des voyages et destinations
- Les couvertures temporaires
- L'assistance internationale
- Les garanties d'annulation
- La tarification voyage

## 2. Architecture et structure du module

### 2.1 Structure du module

Le module d'assurance voyage suivra la structure commune définie dans le document `plan-commun.md`, avec les
spécificités suivantes :

```
com.devolution.saas.product.travel
├── api                            # Contrôleurs REST spécifiques au voyage
│   ├── TripController.java        # API de gestion des voyages
│   ├── TravelPolicyController.java # API de gestion des polices voyage
│   └── AssistanceController.java  # API de gestion de l'assistance
├── application                    # Services d'application
│   ├── service                    # Implémentations des services
│   │   ├── TravelPricingService.java # Service de tarification voyage
│   │   └── TripService.java       # Service de gestion des voyages
│   ├── dto                        # DTOs spécifiques au voyage
│   │   ├── TripDTO.java           # DTO pour les voyages
│   │   └── DestinationDTO.java    # DTO pour les destinations
│   ├── command                    # Commandes spécifiques au voyage
│   │   ├── CreateTripCommand.java # Création de voyage
│   │   └── RequestAssistanceCommand.java # Demande d'assistance
│   └── usecase                    # Cas d'utilisation spécifiques au voyage
│       ├── CalculateTravelPremium.java # Calcul de prime voyage
│       └── ProcessCancellation.java # Traitement d'annulation
├── domain                         # Modèles et repositories de domaine
│   ├── model                      # Modèles spécifiques au voyage
│   │   ├── Trip.java              # Entité Voyage
│   │   ├── Destination.java       # Entité Destination
│   │   ├── TravelPolicy.java      # Entité Police Voyage
│   │   └── Traveler.java          # Entité Voyageur
│   ├── repository                 # Repositories spécifiques au voyage
│   │   ├── TripRepository.java    # Repository pour les voyages
│   │   └── DestinationRepository.java # Repository pour les destinations
│   └── service                    # Services de domaine spécifiques au voyage
│       └── RiskAssessmentService.java # Service d'évaluation des risques
└── infrastructure                 # Implémentations d'infrastructure
    └── persistence                # Implémentations de persistence
        ├── JpaTripRepository.java # Implémentation JPA du repository voyage
        └── JpaDestinationRepository.java # Implémentation JPA du repository destination
```

### 2.2 Intégration avec les composants communs

Le module d'assurance voyage implémentera les interfaces communes définies dans le document `plan-commun.md` :

```java
// Implémentation de l'interface InsuranceProduct pour l'assurance voyage
public class TravelInsuranceProduct implements InsuranceProduct {
    // Implémentation des méthodes de l'interface
    @Override
    public BigDecimal calculatePremium(PolicyCalculationContext context) {
        // Logique spécifique au calcul de prime voyage
        // Prise en compte de la destination, de la durée, des garanties, etc.
    }

    @Override
    public boolean validateSubscription(SubscriptionContext context) {
        // Validation spécifique à l'assurance voyage
        // Vérification des dates, des destinations, etc.
    }

    // Autres méthodes de l'interface
}
```

## 3. Modèle de données

### 3.1 Entités spécifiques à l'assurance voyage

#### 3.1.1 Voyage

```sql
CREATE TABLE trips
(
    id                  UUID PRIMARY KEY,
    trip_name           VARCHAR(255),
    departure_date      DATE        NOT NULL,
    return_date         DATE        NOT NULL,
    trip_purpose        VARCHAR(50) NOT NULL, -- TOURISM, BUSINESS, STUDY, etc.
    trip_type           VARCHAR(50) NOT NULL, -- SINGLE, MULTI_DESTINATION, ANNUAL
    is_group_trip       BOOLEAN     NOT NULL DEFAULT FALSE,
    number_of_travelers INT         NOT NULL DEFAULT 1,
    booking_reference   VARCHAR(50),
    booking_date        DATE,
    total_trip_cost     DECIMAL(19, 2),
    customer_id         UUID        NOT NULL,
    created_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          UUID,
    updated_by          UUID,
    organization_id     UUID        NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.1.2 Destination

```sql
CREATE TABLE destinations
(
    id                      UUID PRIMARY KEY,
    trip_id                 UUID         NOT NULL,
    country                 VARCHAR(100) NOT NULL,
    city                    VARCHAR(100),
    arrival_date            DATE         NOT NULL,
    departure_date          DATE         NOT NULL,
    accommodation_type      VARCHAR(50), -- HOTEL, APARTMENT, CAMPING, etc.
    accommodation_name      VARCHAR(255),
    accommodation_reference VARCHAR(50),
    risk_level              VARCHAR(20), -- LOW, MEDIUM, HIGH, EXTREME
    created_at              TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP,
    created_by              UUID,
    updated_by              UUID,
    organization_id         UUID         NOT NULL,
    FOREIGN KEY (trip_id) REFERENCES trips (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.1.3 Voyageur

```sql
CREATE TABLE travelers
(
    id                      UUID PRIMARY KEY,
    trip_id                 UUID         NOT NULL,
    first_name              VARCHAR(100) NOT NULL,
    last_name               VARCHAR(100) NOT NULL,
    date_of_birth           DATE         NOT NULL,
    passport_number         VARCHAR(50),
    passport_expiry_date    DATE,
    nationality             VARCHAR(50)  NOT NULL,
    address                 TEXT,
    phone                   VARCHAR(20),
    email                   VARCHAR(255),
    emergency_contact_name  VARCHAR(255),
    emergency_contact_phone VARCHAR(20),
    medical_conditions      TEXT,
    is_main_traveler        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP,
    created_by              UUID,
    updated_by              UUID,
    organization_id         UUID         NOT NULL,
    FOREIGN KEY (trip_id) REFERENCES trips (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.1.4 Police voyage

```sql
CREATE TABLE travel_policies
(
    policy_id                       UUID PRIMARY KEY,
    trip_id                         UUID      NOT NULL,
    cancellation_coverage_amount    DECIMAL(19, 2),
    medical_coverage_amount         DECIMAL(19, 2),
    baggage_coverage_amount         DECIMAL(19, 2),
    liability_coverage_amount       DECIMAL(19, 2),
    has_sports_coverage             BOOLEAN   NOT NULL DEFAULT FALSE,
    has_electronic_devices_coverage BOOLEAN   NOT NULL DEFAULT FALSE,
    has_flight_delay_coverage       BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at                      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                      TIMESTAMP,
    created_by                      UUID,
    updated_by                      UUID,
    organization_id                 UUID      NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (trip_id) REFERENCES trips (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

#### 3.1.5 Demande d'assistance

```sql
CREATE TABLE assistance_requests
(
    id               UUID PRIMARY KEY,
    travel_policy_id UUID         NOT NULL,
    request_date     TIMESTAMP    NOT NULL,
    request_type     VARCHAR(50)  NOT NULL, -- MEDICAL, REPATRIATION, LEGAL, BAGGAGE, etc.
    description      TEXT         NOT NULL,
    location         VARCHAR(255) NOT NULL,
    status           VARCHAR(20)  NOT NULL, -- PENDING, IN_PROGRESS, RESOLVED, CANCELLED
    resolution_date  TIMESTAMP,
    resolution_notes TEXT,
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID,
    organization_id  UUID         NOT NULL,
    FOREIGN KEY (travel_policy_id) REFERENCES travel_policies (policy_id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
```

### 3.2 Garanties spécifiques à l'assurance voyage

```sql
-- Insertion des garanties spécifiques à l'assurance voyage
INSERT INTO coverages (id, code, name, description, coverage_type, is_optional, default_limit, default_deductible,
                       organization_id)
VALUES (uuid_generate_v4(), 'TRAVEL_CANCEL', 'Annulation de voyage',
        'Remboursement en cas d\'annulation de voyage', 'CANCELLATION', TRUE, 5000.00, 50.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'TRAVEL_MEDICAL', 'Frais médicaux à l\'étranger',
        'Prise en charge des frais médicaux à l\'étranger', 'MEDICAL', FALSE, 100000.00, 0.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'TRAVEL_REPATRIATION', 'Rapatriement sanitaire', 'Prise en charge du rapatriement en cas de maladie ou accident', 'ASSISTANCE', FALSE, NULL, 0.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'TRAVEL_BAGGAGE', 'Bagages', 'Indemnisation en cas de perte,
        vol ou détérioration des bagages', 'PROPERTY', TRUE, 2000.00, 50.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'TRAVEL_LIABILITY', 'Responsabilité civile', 'Couverture des dommages causés à des tiers', 'LIABILITY', TRUE, 1000000.00, 0.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'TRAVEL_DELAY', 'Retard de transport', 'Indemnisation en cas de retard de transport', 'DELAY', TRUE, 500.00, 4.00, '00000000-0000-0000-0000-000000000000'),
    (uuid_generate_v4(), 'TRAVEL_INTERRUPTION', 'Interruption de séjour', 'Remboursement en cas d\'interruption de séjour',
        'INTERRUPTION', TRUE, 3000.00, 0.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'TRAVEL_SPORTS', 'Activités sportives', 'Couverture des activités sportives à risque',
        'SPORTS', TRUE, 10000.00, 100.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'TRAVEL_ELECTRONIC', 'Appareils électroniques', 'Couverture des appareils électroniques',
        'PROPERTY', TRUE, 1000.00, 100.00, '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'TRAVEL_LEGAL', 'Assistance juridique',
        'Assistance juridique à l\'étranger', 'LEGAL', TRUE, 5000.00, 0.00, '00000000-0000-0000-0000-000000000000');
```

### 3.3 Formules spécifiques à l'assurance voyage

```sql
-- Insertion des formules spécifiques à l'assurance voyage
INSERT INTO formulas (id, code, name, description, product_id, organization_id)
VALUES (uuid_generate_v4(), 'TRAVEL_BASIC', 'Essentielle', 'Couverture de base pour les voyages', '[PRODUCT_ID]',
        '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'TRAVEL_COMFORT', 'Confort', 'Couverture intermédiaire avec garanties supplémentaires',
        '[PRODUCT_ID]', '00000000-0000-0000-0000-000000000000'),
       (uuid_generate_v4(), 'TRAVEL_PREMIUM', 'Premium', 'Couverture complète avec toutes les garanties',
        '[PRODUCT_ID]', '00000000-0000-0000-0000-000000000000');

-- Association des garanties aux formules
INSERT INTO formula_coverages (formula_id, coverage_id, is_included, organization_id)
VALUES
    -- Formule Essentielle
    ('[FORMULA_ID_BASIC]', '[COVERAGE_ID_MEDICAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_BASIC]', '[COVERAGE_ID_REPATRIATION]', TRUE, '00000000-0000-0000-0000-000000000000'),

    -- Formule Confort
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_MEDICAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_REPATRIATION]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_CANCEL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_BAGGAGE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_COMFORT]', '[COVERAGE_ID_LIABILITY]', TRUE, '00000000-0000-0000-0000-000000000000'),

    -- Formule Premium
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_MEDICAL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_REPATRIATION]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_CANCEL]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_BAGGAGE]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_LIABILITY]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_DELAY]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_INTERRUPTION]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_SPORTS]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_ELECTRONIC]', TRUE, '00000000-0000-0000-0000-000000000000'),
    ('[FORMULA_ID_PREMIUM]', '[COVERAGE_ID_LEGAL]', TRUE, '00000000-0000-0000-0000-000000000000');
```

## 4. API et endpoints

### 4.1 API de gestion des voyages

```
GET /api/travel/trips - Liste tous les voyages
GET /api/travel/trips/{id} - Récupère un voyage par son ID
POST /api/travel/trips - Crée un nouveau voyage
PUT /api/travel/trips/{id} - Met à jour un voyage existant
DELETE /api/travel/trips/{id} - Supprime un voyage
```

### 4.2 API de gestion des destinations

```
GET /api/travel/trips/{tripId}/destinations - Liste toutes les destinations d'un voyage
GET /api/travel/destinations/{id} - Récupère une destination par son ID
POST /api/travel/trips/{tripId}/destinations - Ajoute une destination à un voyage
PUT /api/travel/destinations/{id} - Met à jour une destination existante
DELETE /api/travel/destinations/{id} - Supprime une destination
```

### 4.3 API de gestion des voyageurs

```
GET /api/travel/trips/{tripId}/travelers - Liste tous les voyageurs d'un voyage
GET /api/travel/travelers/{id} - Récupère un voyageur par son ID
POST /api/travel/trips/{tripId}/travelers - Ajoute un voyageur à un voyage
PUT /api/travel/travelers/{id} - Met à jour un voyageur existant
DELETE /api/travel/travelers/{id} - Supprime un voyageur
```

### 4.4 API de gestion des polices voyage

```
GET /api/travel/policies - Liste toutes les polices voyage
GET /api/travel/policies/{id} - Récupère une police voyage par son ID
POST /api/travel/policies - Crée une nouvelle police voyage
PUT /api/travel/policies/{id} - Met à jour une police voyage existante
DELETE /api/travel/policies/{id} - Supprime une police voyage
```

### 4.5 API de gestion de l'assistance

```
GET /api/travel/policies/{policyId}/assistance-requests - Liste toutes les demandes d'assistance d'une police
GET /api/travel/assistance-requests/{id} - Récupère une demande d'assistance par son ID
POST /api/travel/policies/{policyId}/assistance-requests - Crée une nouvelle demande d'assistance
PUT /api/travel/assistance-requests/{id} - Met à jour une demande d'assistance existante
PUT /api/travel/assistance-requests/{id}/resolve - Résout une demande d'assistance
```

### 4.6 API de tarification voyage

```
POST /api/travel/pricing/calculate - Calcule le prix d'une police voyage
POST /api/travel/pricing/simulate - Simule différentes options de tarification
```

## 5. Règles métier spécifiques

### 5.1 Règles de tarification voyage

- Le tarif de base dépend de la destination, de la durée du voyage et de l'âge des voyageurs
- Des majorations sont appliquées pour les destinations à haut risque
- Des réductions sont accordées pour les voyages en groupe
- Des majorations sont appliquées pour les voyageurs âgés ou présentant des conditions médicales préexistantes
- Des majorations sont appliquées pour les activités sportives à risque
- Le montant de la couverture annulation est proportionnel au coût total du voyage
- Des réductions sont accordées pour les souscriptions anticipées (plus de 30 jours avant le départ)

### 5.2 Règles de souscription voyage

- La souscription doit être effectuée avant le départ
- Pour la garantie annulation, la souscription doit être effectuée au plus tard 48h après la réservation
- Les voyageurs de plus de 75 ans peuvent nécessiter un questionnaire médical
- Les voyages dans des pays déconseillés par le ministère des Affaires étrangères peuvent être refusés
- La durée maximale d'un voyage est généralement limitée à 90 jours
- Pour les voyages de longue durée (plus de 3 mois), des conditions spéciales s'appliquent

### 5.3 Règles de gestion des sinistres voyage

- Les demandes d'assistance doivent être déclarées immédiatement
- Les sinistres bagages doivent être déclarés dans les 48h
- Les annulations doivent être justifiées par un motif prévu au contrat
- Les frais médicaux doivent être justifiés par des factures et ordonnances
- Les retards de transport doivent être justifiés par une attestation de la compagnie

## 6. Intégrations

### 6.1 Intégration avec les services externes

- **Service de géolocalisation** : Évaluation des risques liés à la destination
- **Service d'alertes voyage** : Informations sur les risques sanitaires, politiques, etc.
- **Service d'assistance internationale** : Coordination des interventions d'urgence
- **Base de données des établissements médicaux** : Localisation des hôpitaux et cliniques partenaires

### 6.2 Intégration avec les modules internes

- **Module client** : Récupération des informations client
- **Module de paiement** : Gestion des paiements de prime
- **Module de sinistre** : Déclaration et suivi des sinistres voyage
- **Module de document** : Génération des documents spécifiques à l'assurance voyage

## 7. Plan d'implémentation

### 7.1 Étapes d'implémentation

1. **Phase 1 : Mise en place des modèles de données**
    - Création des entités spécifiques au voyage
    - Mise en place des repositories
    - Implémentation des migrations de base de données

2. **Phase 2 : Développement des services métier**
    - Implémentation du service de gestion des voyages
    - Implémentation du service de gestion des destinations
    - Implémentation du service de gestion des voyageurs
    - Implémentation du service de tarification voyage

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

- Tests des services de gestion des voyages
- Tests des services de gestion des destinations
- Tests des services de gestion des voyageurs
- Tests des services de tarification voyage
- Tests des règles métier spécifiques au voyage

### 8.2 Tests d'intégration

- Tests d'intégration avec la base de données
- Tests d'intégration avec les services communs
- Tests des API REST

### 8.3 Tests de performance

- Tests de charge pour le calcul de prime voyage
- Tests de performance pour la recherche de voyages
- Tests de performance pour la création de polices voyage

### 8.4 Tests métier

- Validation des règles de tarification
- Validation des règles de souscription
- Validation des règles de gestion des sinistres

## 9. Documentation

### 9.1 Documentation technique

- Documentation des API avec Swagger/OpenAPI
- Documentation des modèles de données
- Documentation des règles métier implémentées

### 9.2 Documentation utilisateur

- Guide d'utilisation des API
- Guide de configuration des produits voyage
- Guide de tarification voyage

## 10. Conclusion

Ce document fournit un plan détaillé pour l'implémentation du produit d'assurance voyage. Il servira de guide pour
l'équipe de développement et assurera une intégration harmonieuse avec les autres modules du système.

L'équipe en charge de l'implémentation du produit voyage devra suivre ce plan tout en respectant les directives communes
définies dans le document `plan-commun.md`.
