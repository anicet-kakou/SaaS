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
       (gen_random_uuid(), 'PERMISSION_ASSIGN', 'Assign permissions to roles', 'PERMISSION', 'ASSIGN', NOW(),
        TRUE)
ON CONFLICT (name) DO NOTHING;

-- Insert default system roles
INSERT INTO roles (id, name, description, created_at, system_defined)
VALUES (gen_random_uuid(), 'ADMIN', 'System administrator with full access', NOW(), TRUE),
       (gen_random_uuid(), 'USER', 'Regular user with limited access', NOW(),
        TRUE)
ON CONFLICT (name, organization_id) DO NOTHING;

-- Assign all permissions to ADMIN role
INSERT INTO role_permissions (id, role_id, permission_id, created_at)
SELECT gen_random_uuid(),
       r.id,
       p.id,
       NOW()
FROM roles r
         CROSS JOIN
     permissions p
WHERE r.name = 'ADMIN'
  AND r.system_defined = TRUE
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Assign basic permissions to USER role
INSERT INTO role_permissions (id, role_id, permission_id, created_at)
SELECT gen_random_uuid(),
       r.id,
       p.id,
       NOW()
FROM roles r
         CROSS JOIN
     permissions p
WHERE r.name = 'USER'
  AND r.system_defined = TRUE
  AND p.name IN ('ORGANIZATION_VIEW', 'USER_VIEW', 'ROLE_VIEW',
                 'PERMISSION_VIEW')
ON CONFLICT (role_id, permission_id) DO NOTHING;
