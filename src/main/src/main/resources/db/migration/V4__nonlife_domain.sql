-- Non-life insurance domain schema
-- This script contains all tables and data specific to the non-life insurance domain

-- AUTO MODULE TABLES

-- Vehicle categories table
CREATE TABLE IF NOT EXISTS vehicle_categories
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

-- Vehicle subcategories table
CREATE TABLE IF NOT EXISTS vehicle_subcategories
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

-- Vehicle makes table
CREATE TABLE IF NOT EXISTS vehicle_makes
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

-- Vehicle models table
CREATE TABLE IF NOT EXISTS vehicle_models
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
    UNIQUE (code, make_id, organization_id)
);

-- Vehicle usages table
CREATE TABLE IF NOT EXISTS vehicle_usages
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

-- Geographic zones table
CREATE TABLE IF NOT EXISTS geographic_zones
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

-- Vehicle body types table
CREATE TABLE IF NOT EXISTS vehicle_body_types
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)   NOT NULL,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL DEFAULT 1.0,
    is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID,
    organization_id  UUID          NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Vehicle genres table
CREATE TABLE IF NOT EXISTS vehicle_genres
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)   NOT NULL,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL DEFAULT 1.0,
    is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID,
    organization_id  UUID          NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Circulation zones table
CREATE TABLE IF NOT EXISTS circulation_zones
(
    id               UUID PRIMARY KEY,
    code             VARCHAR(20)   NOT NULL,
    name             VARCHAR(100)  NOT NULL,
    description      TEXT,
    risk_coefficient DECIMAL(5, 2) NOT NULL DEFAULT 1.0,
    is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID,
    organization_id  UUID          NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (code, organization_id)
);

-- Fuel types table
CREATE TABLE IF NOT EXISTS fuel_types
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

-- Vehicle colors table
CREATE TABLE IF NOT EXISTS vehicle_colors
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

-- Claim history categories table
CREATE TABLE IF NOT EXISTS claim_history_categories
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

-- Auto policies table
CREATE TABLE IF NOT EXISTS auto_policies
(
    id                        UUID PRIMARY KEY,
    policy_number             VARCHAR(50)    NOT NULL,
    status                    VARCHAR(20)    NOT NULL,
    start_date                DATE           NOT NULL,
    end_date                  DATE           NOT NULL,
    premium_amount            DECIMAL(19, 2) NOT NULL,
    coverage_type             VARCHAR(20)    NOT NULL, -- THIRD_PARTY, COMPREHENSIVE
    bonus_malus_coefficient   DECIMAL(5, 2)  NOT NULL,
    annual_mileage            INT,
    parking_type              VARCHAR(20),             -- GARAGE, STREET, PARKING_LOT
    has_anti_theft_device     BOOLEAN        NOT NULL DEFAULT FALSE,
    claim_history_category_id UUID           NOT NULL,
    created_at                TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMP,
    created_by                UUID,
    updated_by                UUID,
    organization_id           UUID           NOT NULL,
    FOREIGN KEY (claim_history_category_id) REFERENCES claim_history_categories (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (policy_number, organization_id)
);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles
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
    body_type_id        UUID        NOT NULL,
    genre_id            UUID        NOT NULL,
    circulation_zone_id UUID        NOT NULL,
    purchase_date       DATE,
    purchase_value      DECIMAL(19, 2),
    current_value       DECIMAL(19, 2),
    mileage             INT,
    vin                 VARCHAR(50), -- Vehicle Identification Number
    color_id            UUID        NOT NULL,
    owner_id            UUID        NOT NULL,
    auto_policy_id      UUID,
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
    FOREIGN KEY (body_type_id) REFERENCES vehicle_body_types (id),
    FOREIGN KEY (genre_id) REFERENCES vehicle_genres (id),
    FOREIGN KEY (circulation_zone_id) REFERENCES circulation_zones (id),
    FOREIGN KEY (color_id) REFERENCES vehicle_colors (id),
    FOREIGN KEY (auto_policy_id) REFERENCES auto_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (registration_number, organization_id)
);

-- Insert default vehicle categories according to CIMA classification
INSERT INTO vehicle_categories (id, code, name, description, tariff_coefficient, organization_id)
VALUES (gen_random_uuid(), 'CAT1', 'Véhicules de tourisme', 'Véhicules particuliers et commerciaux à usage privé', 1.00,
        '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'CAT2', 'Véhicules de transport de marchandises', 'Camions, camionnettes et fourgonnettes',
        1.50, '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'CAT3', 'Véhicules de transport en commun', 'Autobus, autocars et minibus', 1.75,
        '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'CAT4', 'Engins de chantier', 'Tracteurs, bulldozers et autres engins de chantier', 2.00,
        '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'CAT5', 'Véhicules agricoles', 'Tracteurs agricoles et machines agricoles automotrices',
        1.25, '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'CAT6', 'Deux-roues', 'Motocyclettes, scooters et cyclomoteurs', 1.80,
        '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'CAT7', 'Remorques', 'Remorques et semi-remorques', 0.75,
        '00000000-0000-0000-0000-000000000000')
ON CONFLICT (code, organization_id) DO NOTHING;

-- Insert default fuel types
INSERT INTO fuel_types (id, code, name, description, is_active, organization_id)
VALUES (gen_random_uuid(), 'PETROL', 'Essence', 'Carburant essence standard', TRUE,
        '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'DIESEL', 'Diesel', 'Carburant diesel standard', TRUE,
        '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'ELECTRIC', 'Électrique', 'Véhicule 100% électrique', TRUE,
        '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'HYBRID', 'Hybride', 'Véhicule hybride (essence/électrique)', TRUE,
        '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'LPG', 'GPL', 'Gaz de pétrole liquéfié', TRUE, '00000000-0000-0000-0000-000000000000'),
       (gen_random_uuid(), 'CNG', 'GNV', 'Gaz naturel pour véhicules', TRUE, '00000000-0000-0000-0000-000000000000')
ON CONFLICT (code, organization_id) DO NOTHING;

-- Create indexes for auto module tables
CREATE INDEX IF NOT EXISTS idx_vehicle_categories_organization ON vehicle_categories (organization_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_subcategories_category ON vehicle_subcategories (category_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_subcategories_organization ON vehicle_subcategories (organization_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_makes_organization ON vehicle_makes (organization_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_models_make ON vehicle_models (make_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_models_category ON vehicle_models (category_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_models_organization ON vehicle_models (organization_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_usages_organization ON vehicle_usages (organization_id);
CREATE INDEX IF NOT EXISTS idx_geographic_zones_organization ON geographic_zones (organization_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_body_types_organization ON vehicle_body_types (organization_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_genres_organization ON vehicle_genres (organization_id);
CREATE INDEX IF NOT EXISTS idx_circulation_zones_organization ON circulation_zones (organization_id);
CREATE INDEX IF NOT EXISTS idx_fuel_types_organization ON fuel_types (organization_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_colors_organization ON vehicle_colors (organization_id);
CREATE INDEX IF NOT EXISTS idx_claim_history_categories_organization ON claim_history_categories (organization_id);
CREATE INDEX IF NOT EXISTS idx_auto_policies_organization ON auto_policies (organization_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_registration ON vehicles (registration_number);
CREATE INDEX IF NOT EXISTS idx_vehicles_make_model ON vehicles (make_id, model_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_category ON vehicles (category_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_policy ON vehicles (auto_policy_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_organization ON vehicles (organization_id);
