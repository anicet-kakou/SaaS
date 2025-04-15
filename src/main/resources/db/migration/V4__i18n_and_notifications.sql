-- Migration pour ajouter les tables de traductions et de notifications

-- Table des traductions
CREATE TABLE translations
(
    id           BIGSERIAL PRIMARY KEY,
    module       VARCHAR(50)  NOT NULL,
    message_type VARCHAR(50)  NOT NULL,
    message_key  VARCHAR(255) NOT NULL,
    locale       VARCHAR(10)  NOT NULL,
    message_text TEXT         NOT NULL,
    is_default   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    created_by   VARCHAR(100) NOT NULL,
    updated_by   VARCHAR(100) NOT NULL,
    CONSTRAINT uk_translation UNIQUE (module, message_type, message_key, locale)
);

-- Index pour améliorer les performances des recherches de traductions
CREATE INDEX idx_translation_module ON translations (module);
CREATE INDEX idx_translation_message_type ON translations (message_type);
CREATE INDEX idx_translation_locale ON translations (locale);
CREATE INDEX idx_translation_key ON translations (message_key);

-- Table des notifications
CREATE TABLE notifications
(
    id                VARCHAR(36) PRIMARY KEY,
    user_id           VARCHAR(36)  NOT NULL,
    notification_code VARCHAR(255) NOT NULL,
    message           TEXT         NOT NULL,
    type              VARCHAR(20)  NOT NULL,
    data              TEXT,
    is_read           BOOLEAN      NOT NULL DEFAULT FALSE,
    read_at           TIMESTAMP,
    created_at        TIMESTAMP    NOT NULL,
    is_sent           BOOLEAN      NOT NULL DEFAULT FALSE,
    sent_at           TIMESTAMP
);

-- Index pour améliorer les performances des recherches de notifications
CREATE INDEX idx_notification_user_id ON notifications (user_id);
CREATE INDEX idx_notification_is_read ON notifications (is_read);
CREATE INDEX idx_notification_created_at ON notifications (created_at);
CREATE INDEX idx_notification_is_sent ON notifications (is_sent);

-- Commentaires sur les tables
COMMENT ON TABLE translations IS 'Table des traductions pour l''internationalisation';
COMMENT ON COLUMN translations.id IS 'Identifiant unique de la traduction';
COMMENT ON COLUMN translations.module IS 'Module auquel appartient cette traduction (ex: common, security, insurance)';
COMMENT ON COLUMN translations.message_type IS 'Type de message (ex: errors, validation, notifications)';
COMMENT ON COLUMN translations.message_key IS 'Clé du message (ex: user.not.found)';
COMMENT ON COLUMN translations.locale IS 'Locale de la traduction (ex: fr, en)';
COMMENT ON COLUMN translations.message_text IS 'Texte de la traduction';
COMMENT ON COLUMN translations.is_default IS 'Indique si cette traduction est la version par défaut';
COMMENT ON COLUMN translations.created_at IS 'Date de création de la traduction';
COMMENT ON COLUMN translations.updated_at IS 'Date de dernière modification de la traduction';
COMMENT ON COLUMN translations.created_by IS 'Utilisateur ayant créé la traduction';
COMMENT ON COLUMN translations.updated_by IS 'Utilisateur ayant modifié la traduction en dernier';

COMMENT ON TABLE notifications IS 'Table des notifications envoyées aux utilisateurs';
COMMENT ON COLUMN notifications.id IS 'Identifiant unique de la notification';
COMMENT ON COLUMN notifications.user_id IS 'ID de l''utilisateur destinataire de la notification';
COMMENT ON COLUMN notifications.notification_code IS 'Code de la notification (ex: notification.security.login.success)';
COMMENT ON COLUMN notifications.message IS 'Message de la notification';
COMMENT ON COLUMN notifications.type IS 'Type de la notification (ex: INFO, WARNING, ERROR)';
COMMENT ON COLUMN notifications.data IS 'Données supplémentaires pour la notification au format JSON';
COMMENT ON COLUMN notifications.is_read IS 'Indique si la notification a été lue';
COMMENT ON COLUMN notifications.read_at IS 'Date de lecture de la notification';
COMMENT ON COLUMN notifications.created_at IS 'Date de création de la notification';
COMMENT ON COLUMN notifications.is_sent IS 'Indique si la notification a été envoyée';
COMMENT ON COLUMN notifications.sent_at IS 'Date d''envoi de la notification';

-- Ajout des permissions pour les traductions
INSERT INTO permissions (name, description)
VALUES ('TRANSLATION_READ', 'Permission to read translations'),
       ('TRANSLATION_CREATE', 'Permission to create translations'),
       ('TRANSLATION_UPDATE', 'Permission to update translations'),
       ('TRANSLATION_DELETE', 'Permission to delete translations'),
       ('TRANSLATION_IMPORT', 'Permission to import translations'),
       ('TRANSLATION_EXPORT', 'Permission to export translations'),
       ('TRANSLATION_SYNCHRONIZE', 'Permission to synchronize translations');

-- Ajout des permissions pour les notifications
INSERT INTO permissions (name, description)
VALUES ('NOTIFICATION_READ', 'Permission to read notifications'),
       ('NOTIFICATION_SEND', 'Permission to send notifications'),
       ('NOTIFICATION_MANAGE', 'Permission to manage notifications');

-- Attribution des permissions aux rôles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r,
     permissions p
WHERE r.name = 'ADMIN'
  AND p.name IN (
                 'TRANSLATION_READ', 'TRANSLATION_CREATE', 'TRANSLATION_UPDATE', 'TRANSLATION_DELETE',
                 'TRANSLATION_IMPORT', 'TRANSLATION_EXPORT', 'TRANSLATION_SYNCHRONIZE',
                 'NOTIFICATION_READ', 'NOTIFICATION_SEND', 'NOTIFICATION_MANAGE'
    );

-- Attribution des permissions de lecture aux utilisateurs
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r,
     permissions p
WHERE r.name = 'USER'
  AND p.name IN ('TRANSLATION_READ', 'NOTIFICATION_READ');
