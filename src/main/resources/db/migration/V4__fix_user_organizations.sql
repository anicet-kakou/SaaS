-- Drop existing table if it exists
DROP TABLE IF EXISTS user_organizations;

-- Create the table with the correct structure
CREATE TABLE user_organizations
(
    user_id         UUID      NOT NULL,
    organization_id UUID      NOT NULL,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP,
    PRIMARY KEY (user_id, organization_id),
    CONSTRAINT fk_user_organizations_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_organizations_organization FOREIGN KEY (organization_id) REFERENCES organizations (id)
);
