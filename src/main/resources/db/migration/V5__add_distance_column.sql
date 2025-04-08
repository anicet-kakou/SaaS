-- Add distance column to organization_hierarchies table
ALTER TABLE organization_hierarchies
    ADD COLUMN IF NOT EXISTS distance INTEGER NOT NULL DEFAULT 0;
