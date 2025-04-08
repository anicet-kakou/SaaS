-- Création de la table api_keys
CREATE TABLE IF NOT EXISTS api_keys
(
    id
                    UUID
        PRIMARY
            KEY,
    name
                    VARCHAR(255) NOT NULL,
    prefix          VARCHAR(8)   NOT NULL,
    key_hash        VARCHAR(255) NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    expires_at      TIMESTAMP,
    last_used_at    TIMESTAMP,
    permissions     JSONB,
    rate_limit      INT,
    description     TEXT,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW
                                                  (
                                                  ),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    active          BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS api_key_allowed_ips
(
    api_key_id
        UUID
                    NOT
                        NULL,
    ip_address
        VARCHAR(50) NOT NULL,
    PRIMARY KEY
        (
         api_key_id,
         ip_address
            ),
    FOREIGN KEY
        (
         api_key_id
            ) REFERENCES api_keys
        (
         id
            )
);

-- Création de la table audit_logs
CREATE TABLE IF NOT EXISTS audit_logs
(
    id
                    UUID
        PRIMARY
            KEY,
    action
                    VARCHAR(100) NOT NULL,
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
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW
                                                  (
                                                  ),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    version         BIGINT
);

-- Création des index
CREATE INDEX IF NOT EXISTS idx_api_keys_prefix ON api_keys (prefix);
CREATE INDEX IF NOT EXISTS idx_api_keys_organization_id ON api_keys (organization_id);
CREATE INDEX IF NOT EXISTS idx_api_keys_status ON api_keys (status);

CREATE INDEX IF NOT EXISTS idx_audit_logs_action ON audit_logs (action);
CREATE INDEX IF NOT EXISTS idx_audit_logs_entity ON audit_logs (entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_user_id ON audit_logs (user_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_organization_id ON audit_logs (organization_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_action_time ON audit_logs (action_time);
CREATE INDEX IF NOT EXISTS idx_audit_logs_status ON audit_logs (status);
