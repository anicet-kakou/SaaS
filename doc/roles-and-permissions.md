# Rôles et Permissions

Ce document détaille les rôles et permissions définis dans le système, leur hiérarchie et leur utilisation.

## Rôles Système

### ADMIN

- **Description** : Administrateur système avec accès complet
- **Permissions** : Toutes les permissions
- **Contexte** : Accès global à toutes les organisations
- **Système** : Oui (ne peut pas être supprimé)

### MANAGER

- **Description** : Gestionnaire avec droits étendus
- **Permissions** :
    - ORGANIZATION_READ, ORGANIZATION_LIST
    - USER_CREATE, USER_READ, USER_UPDATE, USER_LIST, USER_ACTIVATE, USER_DEACTIVATE
    - POLICY_CREATE, POLICY_READ, POLICY_UPDATE, POLICY_LIST, POLICY_RENEW, POLICY_CANCEL, POLICY_SUSPEND,
      POLICY_REACTIVATE
    - CLAIM_CREATE, CLAIM_READ, CLAIM_UPDATE, CLAIM_LIST, CLAIM_APPROVE, CLAIM_REJECT, CLAIM_CLOSE, CLAIM_REOPEN
- **Contexte** : Limité à l'organisation de l'utilisateur
- **Système** : Non (peut être personnalisé)

### USER

- **Description** : Utilisateur standard
- **Permissions** :
    - ORGANIZATION_READ, ORGANIZATION_LIST
    - USER_READ
    - POLICY_READ, POLICY_LIST
    - CLAIM_READ, CLAIM_LIST, CLAIM_CREATE
- **Contexte** : Limité à l'organisation de l'utilisateur
- **Système** : Oui (ne peut pas être supprimé)

## Permissions par Ressource

### ORGANIZATION (Organisation)

| Permission              | Description                         | Rôles                |
|-------------------------|-------------------------------------|----------------------|
| ORGANIZATION_CREATE     | Créer une organisation              | ADMIN                |
| ORGANIZATION_READ       | Lire les détails d'une organisation | ADMIN, MANAGER, USER |
| ORGANIZATION_UPDATE     | Mettre à jour une organisation      | ADMIN, MANAGER       |
| ORGANIZATION_DELETE     | Supprimer une organisation          | ADMIN                |
| ORGANIZATION_LIST       | Lister les organisations            | ADMIN, MANAGER, USER |
| ORGANIZATION_ACTIVATE   | Activer une organisation            | ADMIN                |
| ORGANIZATION_DEACTIVATE | Désactiver une organisation         | ADMIN                |

### USER (Utilisateur)

| Permission           | Description                              | Rôles                |
|----------------------|------------------------------------------|----------------------|
| USER_CREATE          | Créer un utilisateur                     | ADMIN, MANAGER       |
| USER_READ            | Lire les détails d'un utilisateur        | ADMIN, MANAGER, USER |
| USER_UPDATE          | Mettre à jour un utilisateur             | ADMIN, MANAGER       |
| USER_DELETE          | Supprimer un utilisateur                 | ADMIN                |
| USER_LIST            | Lister les utilisateurs                  | ADMIN, MANAGER       |
| USER_ACTIVATE        | Activer un utilisateur                   | ADMIN, MANAGER       |
| USER_DEACTIVATE      | Désactiver un utilisateur                | ADMIN, MANAGER       |
| USER_CHANGE_PASSWORD | Changer le mot de passe d'un utilisateur | ADMIN, MANAGER       |
| USER_ASSIGN_ROLE     | Attribuer un rôle à un utilisateur       | ADMIN                |

### ROLE (Rôle)

| Permission             | Description                        | Rôles |
|------------------------|------------------------------------|-------|
| ROLE_CREATE            | Créer un rôle                      | ADMIN |
| ROLE_READ              | Lire les détails d'un rôle         | ADMIN |
| ROLE_UPDATE            | Mettre à jour un rôle              | ADMIN |
| ROLE_DELETE            | Supprimer un rôle                  | ADMIN |
| ROLE_LIST              | Lister les rôles                   | ADMIN |
| ROLE_ASSIGN_PERMISSION | Attribuer une permission à un rôle | ADMIN |

### PERMISSION (Permission)

| Permission        | Description                       | Rôles |
|-------------------|-----------------------------------|-------|
| PERMISSION_CREATE | Créer une permission              | ADMIN |
| PERMISSION_READ   | Lire les détails d'une permission | ADMIN |
| PERMISSION_UPDATE | Mettre à jour une permission      | ADMIN |
| PERMISSION_DELETE | Supprimer une permission          | ADMIN |
| PERMISSION_LIST   | Lister les permissions            | ADMIN |

### POLICY (Police d'assurance)

| Permission        | Description                               | Rôles                |
|-------------------|-------------------------------------------|----------------------|
| POLICY_CREATE     | Créer une police d'assurance              | ADMIN, MANAGER       |
| POLICY_READ       | Lire les détails d'une police d'assurance | ADMIN, MANAGER, USER |
| POLICY_UPDATE     | Mettre à jour une police d'assurance      | ADMIN, MANAGER       |
| POLICY_DELETE     | Supprimer une police d'assurance          | ADMIN                |
| POLICY_LIST       | Lister les polices d'assurance            | ADMIN, MANAGER, USER |
| POLICY_RENEW      | Renouveler une police d'assurance         | ADMIN, MANAGER       |
| POLICY_CANCEL     | Annuler une police d'assurance            | ADMIN, MANAGER       |
| POLICY_SUSPEND    | Suspendre une police d'assurance          | ADMIN, MANAGER       |
| POLICY_REACTIVATE | Réactiver une police d'assurance          | ADMIN, MANAGER       |

### CLAIM (Sinistre)

| Permission    | Description                    | Rôles                |
|---------------|--------------------------------|----------------------|
| CLAIM_CREATE  | Créer un sinistre              | ADMIN, MANAGER, USER |
| CLAIM_READ    | Lire les détails d'un sinistre | ADMIN, MANAGER, USER |
| CLAIM_UPDATE  | Mettre à jour un sinistre      | ADMIN, MANAGER       |
| CLAIM_DELETE  | Supprimer un sinistre          | ADMIN                |
| CLAIM_LIST    | Lister les sinistres           | ADMIN, MANAGER, USER |
| CLAIM_APPROVE | Approuver un sinistre          | ADMIN, MANAGER       |
| CLAIM_REJECT  | Rejeter un sinistre            | ADMIN, MANAGER       |
| CLAIM_CLOSE   | Clôturer un sinistre           | ADMIN, MANAGER       |
| CLAIM_REOPEN  | Rouvrir un sinistre            | ADMIN, MANAGER       |

### API_KEY (Clé API)

| Permission     | Description                    | Rôles |
|----------------|--------------------------------|-------|
| API_KEY_CREATE | Créer une clé API              | ADMIN |
| API_KEY_READ   | Lire les détails d'une clé API | ADMIN |
| API_KEY_UPDATE | Mettre à jour une clé API      | ADMIN |
| API_KEY_DELETE | Supprimer une clé API          | ADMIN |
| API_KEY_LIST   | Lister les clés API            | ADMIN |
| API_KEY_REVOKE | Révoquer une clé API           | ADMIN |

## Règles de Contrôle d'Accès

### Règles Générales

1. **Isolation par Organisation** : Les utilisateurs ne peuvent accéder qu'aux données de leur propre organisation, sauf
   les administrateurs qui ont un accès global.

2. **Propriété des Ressources** : Les utilisateurs peuvent toujours accéder à leurs propres ressources, même s'ils n'ont
   pas la permission explicite.

3. **Héritage des Permissions** : Les rôles héritent des permissions de leurs rôles parents.

4. **Rôles Système** : Les rôles système (ADMIN, USER) ne peuvent pas être supprimés ou modifiés fondamentalement.

### Règles Spécifiques

1. **Modification de Profil** : Les utilisateurs peuvent toujours modifier leur propre profil, même s'ils n'ont pas la
   permission USER_UPDATE.

2. **Changement de Mot de Passe** : Les utilisateurs peuvent toujours changer leur propre mot de passe, même s'ils n'ont
   pas la permission USER_CHANGE_PASSWORD.

3. **Accès aux Polices** : Les utilisateurs ne peuvent accéder qu'aux polices d'assurance dont ils sont le souscripteur
   ou le bénéficiaire, sauf s'ils ont des permissions étendues.

4. **Accès aux Sinistres** : Les utilisateurs ne peuvent accéder qu'aux sinistres qu'ils ont déclarés, sauf s'ils ont
   des permissions étendues.

## Bonnes Pratiques

1. **Principe du Moindre Privilège** : Attribuer uniquement les permissions nécessaires pour accomplir une tâche.

2. **Séparation des Responsabilités** : Éviter de concentrer trop de permissions dans un seul rôle.

3. **Audit des Accès** : Toutes les actions sensibles doivent être auditées.

4. **Revue Périodique** : Les rôles et permissions doivent être revus périodiquement pour s'assurer qu'ils sont toujours
   appropriés.

5. **Documentation** : Toute modification des rôles et permissions doit être documentée.

## Exemples d'Utilisation

### Exemple 1 : Contrôleur REST

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @PostMapping
    @PreAuthorize("hasPermission('USER_CREATE')")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserCommand command) {
        // ...
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('USER_READ') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        // ...
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('USER_UPDATE') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UpdateUserCommand command) {
        // ...
    }
}
```

### Exemple 2 : Service Métier

```java
@Service
public class PolicyService {
    
    @PreAuthorize("hasPermission('POLICY_CREATE')")
    public PolicyDTO createPolicy(CreatePolicyCommand command) {
        // ...
    }
    
    @PostFilter("hasPermission('POLICY_READ') or filterObject.subscriberId == authentication.principal.id")
    public List<PolicyDTO> getPolicies() {
        // ...
    }
}
```

## Évolution Future

### Hiérarchie des Rôles

Mise en place d'une hiérarchie des rôles pour simplifier la gestion des permissions :

```
ADMIN
  |
  +-- MANAGER
        |
        +-- USER
```

### Permissions Dynamiques

Implémentation d'un système de permissions dynamiques basé sur les métadonnées des ressources et les règles métier.

### Interface d'Administration

Développement d'une interface utilisateur pour la gestion des rôles et permissions, avec visualisation de la hiérarchie
et simulation des effets des modifications.
