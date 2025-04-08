package com.devolution.saas.core.security.infrastructure.config;

import com.devolution.saas.core.security.domain.model.Permission;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.repository.PermissionRepository;
import com.devolution.saas.core.security.domain.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Configuration pour charger les données initiales (rôles et permissions).
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class InitialDataLoader {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    /**
     * Initialise les données de base (rôles et permissions).
     */
    @PostConstruct
    public void init() {
        try {
            // Désactiver temporairement l'initialisation des données pour éviter les erreurs
            // initializePermissions();
            // initializeRoles();
            log.info("Initialisation des données désactivée temporairement");
        } catch (Exception e) {
            log.error("Erreur lors de l'initialisation des données", e);
        }
    }

    /**
     * Initialise les permissions système.
     */
    @Transactional
    public void initializePermissions() {
        log.info("Initialisation des permissions système");

        List<Permission> systemPermissions = new ArrayList<>();

        // Permissions pour les organisations
        systemPermissions.addAll(Arrays.asList(
                Permission.createSystemPermission("ORGANIZATION", "CREATE", "Créer une organisation"),
                Permission.createSystemPermission("ORGANIZATION", "READ", "Lire les informations d'une organisation"),
                Permission.createSystemPermission("ORGANIZATION", "UPDATE", "Mettre à jour une organisation"),
                Permission.createSystemPermission("ORGANIZATION", "DELETE", "Supprimer une organisation"),
                Permission.createSystemPermission("ORGANIZATION", "LIST", "Lister les organisations")
        ));

        // Permissions pour les utilisateurs
        systemPermissions.addAll(Arrays.asList(
                Permission.createSystemPermission("USER", "CREATE", "Créer un utilisateur"),
                Permission.createSystemPermission("USER", "READ", "Lire les informations d'un utilisateur"),
                Permission.createSystemPermission("USER", "UPDATE", "Mettre à jour un utilisateur"),
                Permission.createSystemPermission("USER", "DELETE", "Supprimer un utilisateur"),
                Permission.createSystemPermission("USER", "LIST", "Lister les utilisateurs"),
                Permission.createSystemPermission("USER", "CHANGE_PASSWORD", "Changer le mot de passe d'un utilisateur"),
                Permission.createSystemPermission("USER", "ACTIVATE", "Activer un utilisateur"),
                Permission.createSystemPermission("USER", "DEACTIVATE", "Désactiver un utilisateur"),
                Permission.createSystemPermission("USER", "LOCK", "Verrouiller un utilisateur"),
                Permission.createSystemPermission("USER", "UNLOCK", "Déverrouiller un utilisateur")
        ));

        // Permissions pour les rôles
        systemPermissions.addAll(Arrays.asList(
                Permission.createSystemPermission("ROLE", "CREATE", "Créer un rôle"),
                Permission.createSystemPermission("ROLE", "READ", "Lire les informations d'un rôle"),
                Permission.createSystemPermission("ROLE", "UPDATE", "Mettre à jour un rôle"),
                Permission.createSystemPermission("ROLE", "DELETE", "Supprimer un rôle"),
                Permission.createSystemPermission("ROLE", "LIST", "Lister les rôles"),
                Permission.createSystemPermission("ROLE", "ASSIGN", "Assigner un rôle à un utilisateur"),
                Permission.createSystemPermission("ROLE", "REVOKE", "Révoquer un rôle d'un utilisateur")
        ));

        // Permissions pour les permissions
        systemPermissions.addAll(Arrays.asList(
                Permission.createSystemPermission("PERMISSION", "READ", "Lire les informations d'une permission"),
                Permission.createSystemPermission("PERMISSION", "LIST", "Lister les permissions"),
                Permission.createSystemPermission("PERMISSION", "ASSIGN", "Assigner une permission à un rôle"),
                Permission.createSystemPermission("PERMISSION", "REVOKE", "Révoquer une permission d'un rôle")
        ));

        // Permissions pour les polices d'assurance
        systemPermissions.addAll(Arrays.asList(
                Permission.createSystemPermission("POLICY", "CREATE", "Créer une police d'assurance"),
                Permission.createSystemPermission("POLICY", "READ", "Lire les informations d'une police d'assurance"),
                Permission.createSystemPermission("POLICY", "UPDATE", "Mettre à jour une police d'assurance"),
                Permission.createSystemPermission("POLICY", "DELETE", "Supprimer une police d'assurance"),
                Permission.createSystemPermission("POLICY", "LIST", "Lister les polices d'assurance"),
                Permission.createSystemPermission("POLICY", "RENEW", "Renouveler une police d'assurance"),
                Permission.createSystemPermission("POLICY", "CANCEL", "Annuler une police d'assurance"),
                Permission.createSystemPermission("POLICY", "SUSPEND", "Suspendre une police d'assurance"),
                Permission.createSystemPermission("POLICY", "REACTIVATE", "Réactiver une police d'assurance")
        ));

        // Permissions pour les sinistres
        systemPermissions.addAll(Arrays.asList(
                Permission.createSystemPermission("CLAIM", "CREATE", "Créer un sinistre"),
                Permission.createSystemPermission("CLAIM", "READ", "Lire les informations d'un sinistre"),
                Permission.createSystemPermission("CLAIM", "UPDATE", "Mettre à jour un sinistre"),
                Permission.createSystemPermission("CLAIM", "DELETE", "Supprimer un sinistre"),
                Permission.createSystemPermission("CLAIM", "LIST", "Lister les sinistres"),
                Permission.createSystemPermission("CLAIM", "APPROVE", "Approuver un sinistre"),
                Permission.createSystemPermission("CLAIM", "REJECT", "Rejeter un sinistre"),
                Permission.createSystemPermission("CLAIM", "CLOSE", "Clôturer un sinistre"),
                Permission.createSystemPermission("CLAIM", "REOPEN", "Rouvrir un sinistre")
        ));

        // Sauvegarde des permissions
        for (Permission permission : systemPermissions) {
            Optional<Permission> existingPermission = permissionRepository.findByName(permission.getName());
            if (existingPermission.isEmpty()) {
                permissionRepository.save(permission);
                log.debug("Permission créée: {}", permission.getName());
            }
        }

        log.info("{} permissions système initialisées", systemPermissions.size());
    }

    /**
     * Initialise les rôles système.
     */
    @Transactional
    public void initializeRoles() {
        log.info("Initialisation des rôles système");

        // Rôle ADMIN
        Role adminRole = createRoleIfNotExists("ADMIN", "Administrateur avec tous les droits");
        if (adminRole != null) {
            // Attribution de toutes les permissions au rôle ADMIN
            List<Permission> allPermissions = permissionRepository.findAll();
            for (Permission permission : allPermissions) {
                adminRole.addPermission(permission);
            }
            roleRepository.save(adminRole);
            log.debug("Rôle ADMIN créé avec {} permissions", allPermissions.size());
        }

        // Rôle USER
        Role userRole = createRoleIfNotExists("USER", "Utilisateur standard");
        if (userRole != null) {
            // Attribution des permissions de base au rôle USER
            List<String> userPermissions = Arrays.asList(
                    "ORGANIZATION_READ",
                    "ORGANIZATION_LIST",
                    "USER_READ",
                    "POLICY_READ",
                    "POLICY_LIST",
                    "CLAIM_READ",
                    "CLAIM_LIST",
                    "CLAIM_CREATE"
            );

            for (String permissionName : userPermissions) {
                Optional<Permission> permission = permissionRepository.findByName(permissionName);
                permission.ifPresent(userRole::addPermission);
            }

            roleRepository.save(userRole);
            log.debug("Rôle USER créé avec {} permissions", userPermissions.size());
        }

        // Rôle MANAGER
        Role managerRole = createRoleIfNotExists("MANAGER", "Gestionnaire avec droits étendus");
        if (managerRole != null) {
            // Attribution des permissions de gestion au rôle MANAGER
            List<String> managerPermissions = Arrays.asList(
                    "ORGANIZATION_READ",
                    "ORGANIZATION_LIST",
                    "USER_CREATE",
                    "USER_READ",
                    "USER_UPDATE",
                    "USER_LIST",
                    "USER_ACTIVATE",
                    "USER_DEACTIVATE",
                    "POLICY_CREATE",
                    "POLICY_READ",
                    "POLICY_UPDATE",
                    "POLICY_LIST",
                    "POLICY_RENEW",
                    "POLICY_CANCEL",
                    "POLICY_SUSPEND",
                    "POLICY_REACTIVATE",
                    "CLAIM_CREATE",
                    "CLAIM_READ",
                    "CLAIM_UPDATE",
                    "CLAIM_LIST",
                    "CLAIM_APPROVE",
                    "CLAIM_REJECT",
                    "CLAIM_CLOSE",
                    "CLAIM_REOPEN"
            );

            for (String permissionName : managerPermissions) {
                Optional<Permission> permission = permissionRepository.findByName(permissionName);
                permission.ifPresent(managerRole::addPermission);
            }

            roleRepository.save(managerRole);
            log.debug("Rôle MANAGER créé avec {} permissions", managerPermissions.size());
        }

        log.info("Rôles système initialisés");
    }

    /**
     * Crée un rôle s'il n'existe pas déjà.
     *
     * @param name        Nom du rôle
     * @param description Description du rôle
     * @return Rôle créé ou null si le rôle existe déjà
     */
    private Role createRoleIfNotExists(String name, String description) {
        Optional<Role> existingRole = roleRepository.findByName(name);
        if (existingRole.isPresent()) {
            return null;
        }

        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setSystemDefined(true);
        return roleRepository.save(role);
    }
}
