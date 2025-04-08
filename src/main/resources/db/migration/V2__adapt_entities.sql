-- Rename columns to match entity field names
-- This script is a no-op because the schema already matches the entity field names
-- It's included for documentation purposes

-- Organizations table
-- created_at -> createdDate
-- updated_at -> lastModifiedDate
-- logo_url -> logo

-- Users table
-- password_hash -> password
-- created_at -> createdDate
-- updated_at -> lastModifiedDate

-- Roles table
-- created_at -> createdDate
-- updated_at -> lastModifiedDate

-- Permissions table
-- resource_type -> resource
-- created_at -> createdDate
-- updated_at -> lastModifiedDate

-- RefreshTokens table
-- expires_at -> expiryDate
-- created_at -> createdDate
-- updated_at -> lastModifiedDate
