-- Security module schema
-- This script contains all tables and data specific to the security module

-- Refresh tokens table
CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id              UUID PRIMARY KEY,
    token           VARCHAR(255) NOT NULL UNIQUE,
    user_id         UUID         NOT NULL,
    expires_at      TIMESTAMP    NOT NULL,
    revoked         BOOLEAN      NOT NULL DEFAULT FALSE,
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- API keys table
CREATE TABLE IF NOT EXISTS api_keys
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    prefix          VARCHAR(8)   NOT NULL,
    key_hash        VARCHAR(255) NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    expires_at      TIMESTAMP,
    last_used_at    TIMESTAMP,
    permissions     JSONB,
    rate_limit      INT,
    description     TEXT,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    active          BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Audit logs table
CREATE TABLE IF NOT EXISTS audit_logs
(
    id              UUID PRIMARY KEY,
    action          VARCHAR(100) NOT NULL,
    description     TEXT,
    entity_type     VARCHAR(100),
    entity_id       VARCHAR(100),
    before_data     JSONB,
    after_data      JSONB,
    user_id         UUID,
    username        VARCHAR(100),
    organization_id UUID,
    ip_address      VARCHAR(50),
    user_agent      VARCHAR(255),
    request_url     VARCHAR(255),
    http_method     VARCHAR(10),
    status_code     INT,
    execution_time  BIGINT,
    status          VARCHAR(20)  NOT NULL,
    error_message   TEXT,
    stack_trace     TEXT,
    action_time     TIMESTAMP    NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    version         BIGINT
);

-- Insert default system permissions
INSERT INTO permissions (id, name, description, resource_type, action, created_at, system_defined)
VALUES (gen_random_uuid(), 'ORGANIZATION_VIEW', 'View organization details', 'ORGANIZATION', 'VIEW', NOW(), TRUE),
       (gen_random_uuid(), 'ORGANIZATION_CREATE', 'Create new organizations', 'ORGANIZATION', 'CREATE', NOW(), TRUE),
       (gen_random_uuid(), 'ORGANIZATION_UPDATE', 'Update organization details', 'ORGANIZATION', 'UPDATE', NOW(), TRUE),
       (gen_random_uuid(), 'ORGANIZATION_DELETE', 'Delete organizations', 'ORGANIZATION', 'DELETE', NOW(), TRUE),
       (gen_random_uuid(), 'USER_VIEW', 'View user details', 'USER', 'VIEW', NOW(), TRUE),
       (gen_random_uuid(), 'USER_CREATE', 'Create new users', 'USER', 'CREATE', NOW(), TRUE),
       (gen_random_uuid(), 'USER_UPDATE', 'Update user details', 'USER', 'UPDATE', NOW(), TRUE),
       (gen_random_uuid(), 'USER_DELETE', 'Delete users', 'USER', 'DELETE', NOW(), TRUE),
       (gen_random_uuid(), 'ROLE_VIEW', 'View role details', 'ROLE', 'VIEW', NOW(), TRUE),
       (gen_random_uuid(), 'ROLE_CREATE', 'Create new roles', 'ROLE', 'CREATE', NOW(), TRUE),
       (gen_random_uuid(), 'ROLE_UPDATE', 'Update role details', 'ROLE', 'UPDATE', NOW(), TRUE),
       (gen_random_uuid(), 'ROLE_DELETE', 'Delete roles', 'ROLE', 'DELETE', NOW(), TRUE),
       (gen_random_uuid(), 'PERMISSION_VIEW', 'View permission details', 'PERMISSION', 'VIEW', NOW(), TRUE),
       (gen_random_uuid(), 'PERMISSION_ASSIGN', 'Assign permissions to roles', 'PERMISSION', 'ASSIGN', NOW(), TRUE)
ON CONFLICT (name) DO NOTHING;

-- Insert default system roles
INSERT INTO roles (id, name, description, created_at, system_defined)
VALUES (gen_random_uuid(), 'ADMIN', 'System administrator with full access', NOW(), TRUE),
       (gen_random_uuid(), 'USER', 'Regular user with limited access', NOW(), TRUE)
ON CONFLICT (name, organization_id) DO NOTHING;

-- Assign all permissions to ADMIN role
INSERT INTO role_permissions (id, role_id, permission_id, created_at)
SELECT gen_random_uuid(),
       r.id,
       p.id,
       NOW()
FROM roles r
         CROSS JOIN permissions p
WHERE r.name = 'ADMIN'
  AND r.system_defined = TRUE
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Create indexes for security tables
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON refresh_tokens (user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_token ON refresh_tokens (token);
CREATE INDEX IF NOT EXISTS idx_api_keys_prefix ON api_keys (prefix);
CREATE INDEX IF NOT EXISTS idx_api_keys_organization_id ON api_keys (organization_id);
CREATE INDEX IF NOT EXISTS idx_api_keys_status ON api_keys (status);
CREATE INDEX IF NOT EXISTS idx_audit_logs_action ON audit_logs (action);
CREATE INDEX IF NOT EXISTS idx_audit_logs_entity ON audit_logs (entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_user_id ON audit_logs (user_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_organization_id ON audit_logs (organization_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_action_time ON audit_logs (action_time);
CREATE INDEX IF NOT EXISTS idx_audit_logs_status ON audit_logs (status);
