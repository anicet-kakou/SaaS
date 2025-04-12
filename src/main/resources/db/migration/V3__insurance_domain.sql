-- Insurance domain module schema
-- This script contains all tables and data specific to the insurance domain

-- Product categories table
CREATE TABLE IF NOT EXISTS product_categories
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(50)  NOT NULL UNIQUE,
    name               VARCHAR(255) NOT NULL,
    description        TEXT,
    parent_category_id UUID,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID,
    FOREIGN KEY (parent_category_id) REFERENCES product_categories (id)
);

-- Insert default product categories
INSERT INTO product_categories (id, code, name)
VALUES (gen_random_uuid(), 'AUTO', 'Assurance Automobile'),
       (gen_random_uuid(), 'HOME', 'Assurance Habitation'),
       (gen_random_uuid(), 'TRAVEL', 'Assurance Voyage'),
       (gen_random_uuid(), 'MISC_RISKS', 'Risques Divers')
ON CONFLICT (code) DO NOTHING;

-- Create index for product categories
CREATE INDEX IF NOT EXISTS idx_product_categories_parent ON product_categories (parent_category_id);

-- Migration des tables fuel_types vers vehicle_fuel_types
-- Vérifier si la table fuel_types existe encore et la supprimer si c'est le cas
-- Nous devons d'abord supprimer ou mettre à jour les contraintes de clé étrangère
DO
$$
    BEGIN
        -- Vérifier si la contrainte existe et la supprimer
        IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_vehicle_fuel_type') THEN
            ALTER TABLE vehicles
                DROP CONSTRAINT fk_vehicle_fuel_type;

            -- Ajouter une nouvelle contrainte qui pointe vers la nouvelle table
            IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'vehicle_fuel_types') THEN
                ALTER TABLE vehicles
                    ADD CONSTRAINT fk_vehicle_vehicle_fuel_type
                        FOREIGN KEY (fuel_type_id) REFERENCES vehicle_fuel_types (id);
            END IF;
        END IF;

        -- Maintenant nous pouvons supprimer l'ancienne table en toute sécurité
        IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'fuel_types') THEN
            DROP TABLE fuel_types;
        END IF;
    END
$$;

-- Migration des tables vehicle_makes vers manufacturers
-- Mise à jour des références restantes de make_id à manufacturer_id dans la table vehicle_models
ALTER TABLE vehicle_models
    RENAME COLUMN make_id TO manufacturer_id;

-- Renommer la contrainte de clé étrangère si elle existe
-- (Vérifier d'abord si la contrainte existe pour éviter les erreurs)
DO
$$
    BEGIN
        IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_vehicle_model_make') THEN
            ALTER TABLE vehicle_models
                RENAME CONSTRAINT fk_vehicle_model_make TO fk_vehicle_model_manufacturer;
        END IF;
    END
$$;

-- Mettre à jour les index si nécessaire
DO
$$
    BEGIN
        IF EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_vehicle_models_make_id') THEN
            DROP INDEX idx_vehicle_models_make_id;
            CREATE INDEX idx_vehicle_models_manufacturer_id ON vehicle_models (manufacturer_id);
        END IF;
    END
$$;
