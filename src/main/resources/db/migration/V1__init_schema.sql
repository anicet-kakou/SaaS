-- Base tables
CREATE TABLE IF NOT EXISTS organizations
(
    id
                         UUID
        PRIMARY
            KEY,
    name
                         VARCHAR(255) NOT NULL,
    code                 VARCHAR(50)  NOT NULL UNIQUE,
    type                 VARCHAR(50)  NOT NULL,
    status               VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    parent_id            UUID         NULL,
    address              TEXT,
    phone                VARCHAR(20),
    email                VARCHAR(255),
    website              VARCHAR(255),
    logo_url             VARCHAR(255),
    primary_contact_name VARCHAR(255),
    description          TEXT,
    settings             JSONB,
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW
                                                       (
                                                       ),
    updated_at           TIMESTAMP,
    created_by           UUID,
    updated_by           UUID,
    organization_id      UUID,
    version              BIGINT,
    active               BOOLEAN      NOT NULL DEFAULT TRUE,
    FOREIGN KEY
        (
         parent_id
            ) REFERENCES organizations
        (
         id
            )
);

CREATE TABLE IF NOT EXISTS organization_hierarchies
(
    id
                    UUID
        PRIMARY
            KEY,
    ancestor_id
                    UUID
        NOT
            NULL,
    descendant_id
                    UUID
        NOT
            NULL,
    depth
                    INT
        NOT
            NULL,
    created_at
                    TIMESTAMP
        NOT
            NULL
        DEFAULT
            NOW
            (
            ),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    FOREIGN KEY
        (
         ancestor_id
            ) REFERENCES organizations
        (
         id
            ),
    FOREIGN KEY
        (
         descendant_id
            ) REFERENCES organizations
        (
         id
            ),
    UNIQUE
        (
         ancestor_id,
         descendant_id
            )
);

CREATE TABLE IF NOT EXISTS users
(
    id
                              UUID
        PRIMARY
            KEY,
    username
                              VARCHAR(50)  NOT NULL UNIQUE,
    email                     VARCHAR(255) NOT NULL UNIQUE,
    password_hash             VARCHAR(255) NOT NULL,
    first_name                VARCHAR(100),
    last_name                 VARCHAR(100),
    phone                     VARCHAR(20),
    status                    VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    locked                    BOOLEAN      NOT NULL DEFAULT FALSE,
    failed_login_attempts     INT          NOT NULL DEFAULT 0,
    last_login_at             TIMESTAMP,
    profile_picture_url       VARCHAR(255),
    created_at                TIMESTAMP    NOT NULL DEFAULT NOW
                                                            (
                                                            ),
    updated_at                TIMESTAMP,
    created_by                UUID,
    updated_by                UUID,
    organization_id           UUID,
    version                   BIGINT,
    account_non_expired       BOOLEAN      NOT NULL DEFAULT TRUE,
    account_non_locked        BOOLEAN      NOT NULL DEFAULT TRUE,
    credentials_non_expired   BOOLEAN      NOT NULL DEFAULT TRUE,
    enabled                   BOOLEAN      NOT NULL DEFAULT TRUE,
    last_password_change_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_organizations
(
    id
               UUID
        PRIMARY
            KEY,
    user_id
               UUID
        NOT
            NULL,
    organization_id
               UUID
        NOT
            NULL,
    created_at
               TIMESTAMP
        NOT
            NULL
        DEFAULT
            NOW
            (
            ),
    updated_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    version    BIGINT,
    FOREIGN KEY
        (
         user_id
            ) REFERENCES users
        (
         id
            ),
    FOREIGN KEY
        (
         organization_id
            ) REFERENCES organizations
        (
         id
            ),
    UNIQUE
        (
         user_id,
         organization_id
            )
);

CREATE TABLE IF NOT EXISTS roles
(
    id
                    UUID
        PRIMARY
            KEY,
    name
                    VARCHAR(50) NOT NULL,
    description     TEXT,
    organization_id UUID,
    created_at      TIMESTAMP   NOT NULL DEFAULT NOW
                                                 (
                                                 ),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    version         BIGINT,
    system_defined  BOOLEAN     NOT NULL DEFAULT FALSE,
    UNIQUE
        (
         name,
         organization_id
            )
);

CREATE TABLE IF NOT EXISTS permissions
(
    id
                    UUID
        PRIMARY
            KEY,
    name
                    VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT,
    resource_type   VARCHAR(50)  NOT NULL,
    action          VARCHAR(50)  NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW
                                                  (
                                                  ),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    system_defined  BOOLEAN      NOT NULL DEFAULT FALSE,
    UNIQUE
        (
         resource_type,
         action
            )
);

CREATE TABLE IF NOT EXISTS role_permissions
(
    id
                    UUID
        PRIMARY
            KEY,
    role_id
                    UUID
        NOT
            NULL,
    permission_id
                    UUID
        NOT
            NULL,
    created_at
                    TIMESTAMP
        NOT
            NULL
        DEFAULT
            NOW
            (
            ),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    FOREIGN KEY
        (
         role_id
            ) REFERENCES roles
        (
         id
            ),
    FOREIGN KEY
        (
         permission_id
            ) REFERENCES permissions
        (
         id
            ),
    UNIQUE
        (
         role_id,
         permission_id
            )
);

CREATE TABLE IF NOT EXISTS user_roles
(
    id
               UUID
        PRIMARY
            KEY,
    user_id
               UUID
        NOT
            NULL,
    role_id
               UUID
        NOT
            NULL,
    organization_id
               UUID
        NOT
            NULL,
    created_at
               TIMESTAMP
        NOT
            NULL
        DEFAULT
            NOW
            (
            ),
    updated_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    version    BIGINT,
    FOREIGN KEY
        (
         user_id
            ) REFERENCES users
        (
         id
            ),
    FOREIGN KEY
        (
         role_id
            ) REFERENCES roles
        (
         id
            ),
    FOREIGN KEY
        (
         organization_id
            ) REFERENCES organizations
        (
         id
            ),
    UNIQUE
        (
         user_id,
         role_id,
         organization_id
            )
);

CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id
                    UUID
        PRIMARY
            KEY,
    token
                    VARCHAR(255) NOT NULL UNIQUE,
    user_id         UUID         NOT NULL,
    expires_at      TIMESTAMP    NOT NULL,
    revoked         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW
                                                  (
                                                  ),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    version         BIGINT,
    FOREIGN KEY
        (
         user_id
            ) REFERENCES users
        (
         id
            )
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_organizations_parent_id ON organizations (parent_id);
CREATE INDEX IF NOT EXISTS idx_organization_hierarchies_ancestor_id ON organization_hierarchies (ancestor_id);
CREATE INDEX IF NOT EXISTS idx_organization_hierarchies_descendant_id ON organization_hierarchies (descendant_id);
CREATE INDEX IF NOT EXISTS idx_users_organization_id ON users (organization_id);
CREATE INDEX IF NOT EXISTS idx_user_organizations_user_id ON user_organizations (user_id);
CREATE INDEX IF NOT EXISTS idx_user_organizations_organization_id ON user_organizations (organization_id);
CREATE INDEX IF NOT EXISTS idx_roles_organization_id ON roles (organization_id);
CREATE INDEX IF NOT EXISTS idx_permissions_resource_action ON permissions (resource_type, action);
CREATE INDEX IF NOT EXISTS idx_role_permissions_role_id ON role_permissions (role_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_permission_id ON role_permissions (permission_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles (user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles (role_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_organization_id ON user_roles (organization_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON refresh_tokens (user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_token ON refresh_tokens (token);
