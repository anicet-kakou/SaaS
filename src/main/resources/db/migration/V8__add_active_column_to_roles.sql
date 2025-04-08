-- Add active column to roles table
ALTER TABLE roles
    ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT true;
