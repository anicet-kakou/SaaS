-- Organization module schema
-- This script contains all tables and data specific to the organization module

-- Organization hierarchies table
CREATE TABLE IF NOT EXISTS organization_hierarchies
(
    id              UUID PRIMARY KEY,
    ancestor_id     UUID      NOT NULL,
    descendant_id   UUID      NOT NULL,
    depth           INT       NOT NULL,
    distance        INTEGER   NOT NULL DEFAULT 0,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    FOREIGN KEY (ancestor_id) REFERENCES organizations (id),
    FOREIGN KEY (descendant_id) REFERENCES organizations (id),
    UNIQUE (ancestor_id, descendant_id)
);

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

-- Create indexes for organization hierarchies
CREATE INDEX IF NOT EXISTS idx_organization_hierarchies_ancestor_id ON organization_hierarchies (ancestor_id);
CREATE INDEX IF NOT EXISTS idx_organization_hierarchies_descendant_id ON organization_hierarchies (descendant_id);
CREATE INDEX IF NOT EXISTS idx_product_categories_parent ON product_categories (parent_category_id);
