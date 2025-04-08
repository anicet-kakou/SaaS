-- Add revoked column to refresh_tokens table
ALTER TABLE refresh_tokens
    ADD COLUMN IF NOT EXISTS revoked BOOLEAN NOT NULL DEFAULT false;
