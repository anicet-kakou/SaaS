-- Add active column to refresh_tokens table
ALTER TABLE refresh_tokens
    ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT true;
