# Améliorations de l'Implémentation RBAC

Ce document détaille les problèmes identifiés dans l'implémentation actuelle du contrôle d'accès basé sur les rôles (
RBAC) et propose des améliorations pour renforcer la sécurité et la flexibilité du système.

## Analyse de l'Implémentation Actuelle

### Points Forts

1. **Architecture à Trois Niveaux** : L'implémentation actuelle utilise une architecture à trois niveaux (utilisateurs,
   rôles, permissions) qui offre une bonne flexibilité.

2. **Contexte Multi-Tenant** : Le système prend en compte le contexte organisationnel (tenant) pour les autorisations,
   ce qui est essentiel pour une application SaaS.

3. **Annotations de Sécurité** : Utilisation des annotations `@PreAuthorize` pour sécuriser les endpoints REST.

4. **Service de Sécurité Personnalisé** : Implémentation d'un `SecurityService` pour les vérifications d'autorisation
   personnalisées.

### Problèmes Identifiés

1. **Utilisation Limitée des Permissions** : Les contrôleurs utilisent principalement des vérifications basées sur les
   rôles (`hasRole('ADMIN')`) plutôt que sur les permissions spécifiques.

2. **Absence de Hiérarchie des Rôles** : Pas de support pour la hiérarchie des rôles, ce qui peut conduire à une
   duplication des permissions.

3. **Gestion Manuelle des Permissions** : Les permissions sont attribuées manuellement aux rôles, sans mécanisme
   automatisé basé sur les annotations ou les métadonnées.

4. **Absence de Contrôle d'Accès au Niveau des Données** : Pas de mécanisme systématique pour filtrer les données en
   fonction des autorisations de l'utilisateur.

5. **Vérifications d'Autorisation Incohérentes** : Mélange de vérifications basées sur les rôles et de vérifications
   personnalisées.

6. **Documentation Insuffisante** : Manque de documentation sur les rôles, les permissions et leur utilisation.

7. **Tests de Sécurité Limités** : Couverture de test insuffisante pour les scénarios d'autorisation.

## Améliorations Proposées

### 1. Standardisation des Vérifications d'Autorisation

#### 1.1 Utilisation Cohérente des Permissions

Remplacer les vérifications basées sur les rôles par des vérifications basées sur les permissions spécifiques :

```java
// Avant
@PreAuthorize("hasRole('ADMIN')")

// Après
@PreAuthorize("hasPermission('ROLE_CREATE')")
```

#### 1.2 Création d'Expressions de Sécurité Personnalisées

Étendre le `SecurityService` avec des méthodes plus expressives :

```java
public boolean canManageRole(UUID roleId) {
    // Vérifier si l'utilisateur peut gérer ce rôle spécifique
    // en tenant compte du contexte organisationnel
}

public boolean canAccessResource(String resourceType, UUID resourceId, String action) {
    // Vérification générique pour l'accès aux ressources
}
```

### 2. Implémentation de la Hiérarchie des Rôles

#### 2.1 Modèle de Données pour la Hiérarchie

Ajouter une relation parent-enfant entre les rôles :

```java
@Entity
@Table(name = "roles")
public class Role extends SystemDefinedEntity {
    // Champs existants...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_role_parent"))
    private Role parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Role> children = new HashSet<>();
}
```

#### 2.2 Service de Résolution de Hiérarchie

Créer un service pour résoudre les permissions héritées :

```java
@Service
public class RoleHierarchyService {
    private final RoleRepository roleRepository;

    public Set<Permission> getAllPermissions(Role role) {
        Set<Permission> allPermissions = new HashSet<>(role.getPermissions());
        
        // Ajouter les permissions des rôles parents
        Role parent = role.getParent();
        while (parent != null) {
            allPermissions.addAll(parent.getPermissions());
            parent = parent.getParent();
        }
        
        return allPermissions;
    }
}
```

### 3. Contrôle d'Accès au Niveau des Données

#### 3.1 Filtres de Données Automatiques

Implémenter des filtres JPA pour restreindre automatiquement les données en fonction du contexte de sécurité :

```java
@Component
public class DataSecurityFilter {
    @Autowired
    private SecurityService securityService;
    
    @PostFilter("hasPermission(filterObject, 'READ') or @securityService.isOwner(authentication, filterObject)")
    public <T> List<T> filterReadAccess(List<T> entities) {
        return entities;
    }
}
```

#### 3.2 Spécifications JPA Sécurisées

Créer des spécifications JPA qui intègrent les contraintes de sécurité :

```java
@Component
public class SecureSpecifications {
    public <T> Specification<T> forCurrentUser(Class<T> entityClass) {
        return (root, query, cb) -> {
            // Construire une spécification qui limite les résultats
            // aux entités accessibles par l'utilisateur courant
        };
    }
}
```

### 4. Annotations de Sécurité Personnalisées

#### 4.1 Annotations Métier

Créer des annotations qui expriment les besoins de sécurité en termes métier :

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#id, 'ROLE', 'UPDATE')")
public @interface CanUpdateRole {}

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#command.organizationId, 'ORGANIZATION', 'MANAGE_USERS')")
public @interface CanManageOrganizationUsers {}
```

#### 4.2 Aspect pour la Génération Automatique des Permissions

Créer un aspect qui analyse les annotations et génère automatiquement les permissions nécessaires :

```java
@Aspect
@Component
public class PermissionDiscoveryAspect {
    @Autowired
    private PermissionRepository permissionRepository;
    
    @PostConstruct
    public void discoverPermissions() {
        // Analyser les annotations @PreAuthorize et extraire les permissions
        // Créer automatiquement les permissions manquantes
    }
}
```

### 5. Documentation et Outils

#### 5.1 Documentation des Rôles et Permissions

Créer une documentation complète des rôles et permissions :

```markdown
# Rôles et Permissions

## Rôles Système

### ADMIN
- Description: Administrateur système avec accès complet
- Permissions: Toutes les permissions

### USER
- Description: Utilisateur standard
- Permissions:
  - ORGANIZATION_READ
  - ORGANIZATION_LIST
  - USER_READ
  - ...

## Permissions par Ressource

### ORGANIZATION
- ORGANIZATION_CREATE: Créer une organisation
- ORGANIZATION_READ: Lire les détails d'une organisation
- ...
```

#### 5.2 Interface d'Administration des Rôles et Permissions

Développer une interface utilisateur pour la gestion des rôles et permissions :

- Visualisation de la hiérarchie des rôles
- Attribution des permissions aux rôles
- Prévisualisation des permissions effectives pour un utilisateur

### 6. Tests de Sécurité

#### 6.1 Tests Unitaires pour les Vérifications d'Autorisation

```java
@Test
void testUserCannotAccessAdminResource() {
    // Configurer un utilisateur avec le rôle USER
    // Vérifier qu'il ne peut pas accéder à une ressource réservée aux ADMIN
}

@Test
void testRoleHierarchy() {
    // Vérifier qu'un utilisateur avec un rôle enfant hérite des permissions du rôle parent
}
```

#### 6.2 Tests d'Intégration pour les Scénarios d'Autorisation

```java
@Test
void testUserCanOnlyAccessOwnOrganizationData() {
    // Créer deux organisations avec des données
    // Vérifier qu'un utilisateur d'une organisation ne peut pas accéder aux données de l'autre
}
```

## Plan d'Implémentation

### Phase 1 : Standardisation et Documentation

1. Documenter tous les rôles et permissions existants
2. Standardiser les vérifications d'autorisation dans les contrôleurs
3. Améliorer le `SecurityService` avec des méthodes plus expressives
4. Créer des tests unitaires pour les vérifications d'autorisation

### Phase 2 : Hiérarchie des Rôles et Contrôle d'Accès aux Données

1. Implémenter le modèle de hiérarchie des rôles
2. Développer le service de résolution de hiérarchie
3. Créer des filtres de données automatiques
4. Implémenter des spécifications JPA sécurisées

### Phase 3 : Annotations et Outils

1. Créer des annotations de sécurité personnalisées
2. Développer l'aspect de découverte des permissions
3. Créer une interface d'administration des rôles et permissions
4. Mettre en place des tests d'intégration pour les scénarios d'autorisation

## Exemple d'Implémentation

### Modèle de Données Amélioré

```java
@Entity
@Table(name = "roles")
public class Role extends SystemDefinedEntity {
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_role_parent"))
    private Role parent;
    
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Role> children = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_role_permissions_role")),
        inverseJoinColumns = @JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "fk_role_permissions_permission"))
    )
    private Set<Permission> permissions = new HashSet<>();
}
```

### Service de Sécurité Amélioré

```java
@Service
public class SecurityService {
    private final TenantContextHolder tenantContextHolder;
    private final RoleHierarchyService roleHierarchyService;
    private final UserRepository userRepository;
    
    public boolean hasPermission(String permissionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(permissionName));
    }
    
    public boolean hasPermission(UUID resourceId, String resourceType, String action) {
        // Vérifier si l'utilisateur a la permission spécifiée pour la ressource
        String permissionName = resourceType + "_" + action;
        if (hasPermission(permissionName)) {
            return true;
        }
        
        // Vérifier si l'utilisateur est propriétaire de la ressource
        return isOwner(resourceId, resourceType);
    }
    
    public boolean isOwner(UUID resourceId, String resourceType) {
        // Logique pour déterminer si l'utilisateur est propriétaire de la ressource
    }
    
    public boolean canAccessOrganization(UUID organizationId) {
        // Vérifier si l'utilisateur a accès à l'organisation spécifiée
        UUID currentTenant = tenantContextHolder.getCurrentTenant();
        return currentTenant != null && currentTenant.equals(organizationId);
    }
}
```

### Contrôleur avec Annotations Améliorées

```java
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;
    
    @PostMapping
    @PreAuthorize("hasPermission('ROLE_CREATE')")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody CreateRoleCommand command) {
        return ResponseEntity.ok(roleService.createRole(command));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'ROLE', 'UPDATE')")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable UUID id, @Valid @RequestBody UpdateRoleCommand command) {
        return ResponseEntity.ok(roleService.updateRole(id, command));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'ROLE', 'READ')")
    public ResponseEntity<RoleDTO> getRole(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRole(id));
    }
    
    @GetMapping
    @PreAuthorize("hasPermission('ROLE_LIST')")
    public ResponseEntity<List<RoleDTO>> listRoles() {
        return ResponseEntity.ok(roleService.listRoles());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'ROLE', 'DELETE')")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
```

## Conclusion

L'implémentation de ces améliorations renforcera considérablement la sécurité et la flexibilité du système RBAC. La
standardisation des vérifications d'autorisation, l'introduction de la hiérarchie des rôles et l'amélioration du
contrôle d'accès au niveau des données permettront une gestion plus fine et plus cohérente des autorisations dans l'
application.
