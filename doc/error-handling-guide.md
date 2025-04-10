# Guide de Gestion des Erreurs

Ce document décrit l'approche standardisée de gestion des erreurs dans l'application DEVOLUTION SaaS.

## 1. Hiérarchie des Exceptions

### 1.1 Exceptions de Base

| Exception                        | Description                                     | Code HTTP       |
|----------------------------------|-------------------------------------------------|-----------------|
| `BusinessException`              | Exception racine pour toutes les erreurs métier | 400 Bad Request |
| `ResourceNotFoundException`      | Ressource non trouvée                           | 404 Not Found   |
| `ResourceAlreadyExistsException` | Ressource déjà existante                        | 409 Conflict    |
| `ValidationException`            | Erreur de validation des données                | 400 Bad Request |
| `ConcurrencyException`           | Erreur de concurrence (conflit de version)      | 409 Conflict    |
| `SecurityException`              | Erreur liée à la sécurité                       | 403 Forbidden   |
| `TenantException`                | Erreur liée au multi-tenant                     | 400/403         |

### 1.2 Exceptions Spécifiques aux Modules

Toutes les exceptions spécifiques aux modules doivent étendre les exceptions de base appropriées.

#### Module d'Assurance (Common)

| Exception                     | Étend                        | Description                                          | Code HTTP       |
|-------------------------------|------------------------------|------------------------------------------------------|-----------------|
| `InsuranceBusinessException`  | `BusinessException`          | Exception racine pour les erreurs métier d'assurance | 400 Bad Request |
| `InvalidPolicyException`      | `InsuranceBusinessException` | Police d'assurance invalide                          | 400 Bad Request |
| `InvalidProductException`     | `InsuranceBusinessException` | Produit d'assurance invalide                         | 400 Bad Request |
| `PremiumCalculationException` | `InsuranceBusinessException` | Erreur de calcul de prime                            | 400 Bad Request |

#### Module Auto

| Exception                            | Étend                            | Description                                             | Code HTTP       |
|--------------------------------------|----------------------------------|---------------------------------------------------------|-----------------|
| `AutoBusinessException`              | `InsuranceBusinessException`     | Exception racine pour les erreurs métier du module auto | 400 Bad Request |
| `AutoResourceNotFoundException`      | `ResourceNotFoundException`      | Ressource auto non trouvée                              | 404 Not Found   |
| `AutoResourceAlreadyExistsException` | `ResourceAlreadyExistsException` | Ressource auto déjà existante                           | 409 Conflict    |
| `BusinessRuleViolationException`     | `AutoBusinessException`          | Violation d'une règle métier du module auto             | 400 Bad Request |

## 2. Format de Réponse d'Erreur

Toutes les erreurs API sont renvoyées dans un format standardisé :

```json
{
  "code": "error.code",
  "message": "Message d'erreur descriptif",
  "status": 400,
  "path": "/api/resource",
  "timestamp": "2025-04-01T12:34:56.789",
  "errors": [
    {
      "field": "fieldName",
      "message": "Message d'erreur spécifique au champ"
    }
  ]
}
```

### 2.1 Champs de la Réponse d'Erreur

| Champ       | Description                     | Toujours présent                                   |
|-------------|---------------------------------|----------------------------------------------------|
| `code`      | Code d'erreur unique            | Oui                                                |
| `message`   | Message d'erreur descriptif     | Oui                                                |
| `status`    | Code de statut HTTP             | Oui                                                |
| `path`      | Chemin de la requête            | Oui                                                |
| `timestamp` | Horodatage de l'erreur          | Oui                                                |
| `errors`    | Liste des erreurs de validation | Non (uniquement pour les erreurs de validation)    |
| `trace`     | Trace de la pile                | Non (uniquement en environnement de développement) |

## 3. Codes d'Erreur

Les codes d'erreur suivent une convention de nommage `domaine.ressource.operation.erreur`.

### 3.1 Codes d'Erreur Communs

| Code                             | Description                  |
|----------------------------------|------------------------------|
| `resource.not.found`             | Ressource non trouvée        |
| `resource.already.exists`        | Ressource déjà existante     |
| `validation.error`               | Erreur de validation         |
| `concurrency.optimistic.locking` | Conflit d'optimistic locking |
| `concurrency.version.conflict`   | Conflit de version           |
| `security.error`                 | Erreur de sécurité           |
| `tenant.error`                   | Erreur de tenant             |
| `tenant.required`                | Tenant requis                |
| `tenant.not.authorized`          | Tenant non autorisé          |
| `internal.error`                 | Erreur interne du serveur    |

### 3.2 Codes d'Erreur Spécifiques aux Modules

#### Module d'Assurance

| Code                                  | Description                  |
|---------------------------------------|------------------------------|
| `insurance.error`                     | Erreur métier d'assurance    |
| `insurance.policy.invalid`            | Police d'assurance invalide  |
| `insurance.product.invalid`           | Produit d'assurance invalide |
| `insurance.premium.calculation.error` | Erreur de calcul de prime    |

#### Module Auto

| Code                                     | Description                                 |
|------------------------------------------|---------------------------------------------|
| `insurance.auto.error`                   | Erreur métier du module auto                |
| `insurance.auto.resource.not.found`      | Ressource auto non trouvée                  |
| `insurance.auto.resource.already.exists` | Ressource auto déjà existante               |
| `insurance.auto.business.rule.violation` | Violation d'une règle métier du module auto |

## 4. Bonnes Pratiques

### 4.1 Lancer des Exceptions

```java
// Lancer une exception de ressource non trouvée
throw new ResourceNotFoundException("user",userId);

// Lancer une exception de ressource déjà existante
throw new

ResourceAlreadyExistsException("user",userId);

// Lancer une exception de validation
throw new

ValidationException()
    .

addError("email","L'email est invalide")
    .

addError("password","Le mot de passe doit contenir au moins 8 caractères");

// Lancer une exception métier
throw new

BusinessException("user.email.duplicate","Un utilisateur avec cet email existe déjà");

// Lancer une exception métier d'assurance
throw new

InsuranceBusinessException("insurance.policy.expired","La police d'assurance est expirée");

// Lancer une exception métier du module auto
throw new

AutoBusinessException("insurance.auto.vehicle.invalid","Le véhicule est invalide");
```

### 4.2 Journalisation des Exceptions

- Les exceptions attendues (BusinessException, ResourceNotFoundException, etc.) doivent être journalisées au niveau WARN
- Les exceptions inattendues doivent être journalisées au niveau ERROR avec la trace complète

### 4.3 Messages d'Erreur

- Les messages d'erreur doivent être clairs et descriptifs
- Ils doivent être orientés utilisateur pour les erreurs attendues
- Ils ne doivent pas exposer de détails techniques ou sensibles en production

## 5. Gestion des Exceptions dans les Contrôleurs

Les contrôleurs ne doivent pas gérer les exceptions directement. Toutes les exceptions doivent être propagées au
`GlobalExceptionHandler`.

```java
// À éviter
@GetMapping("/{id}")
public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
    try {
        return ResponseEntity.ok(userService.getUser(id));
    } catch (ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}

// Recommandé
@GetMapping("/{id}")
public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.getUser(id));
}
```

## 6. Internationalisation des Messages d'Erreur

Pour l'internationalisation des messages d'erreur, utilisez les codes d'erreur comme clés dans les fichiers de messages.

```properties
# messages_fr.properties
resource.not.found=La ressource demandée n'a pas été trouvée
user.email.duplicate=Un utilisateur avec cet email existe déjà
# messages_en.properties
resource.not.found=The requested resource was not found
user.email.duplicate=A user with this email already exists
```
