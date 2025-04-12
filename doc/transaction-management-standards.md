# Standards de Gestion des Transactions

Ce document définit les standards pour l'utilisation de l'annotation `@Transactional` dans le projet SaaS. Ces standards
visent à assurer une gestion cohérente et optimale des transactions dans toute l'application.

## Principes Généraux

1. **Niveau d'Application** : L'annotation `@Transactional` doit être appliquée au niveau de la méthode plutôt qu'au
   niveau de la classe pour un contrôle plus précis.

2. **Lisibilité du Code** : Toujours spécifier explicitement les attributs de transaction, même s'ils correspondent aux
   valeurs par défaut, pour améliorer la lisibilité.

3. **Isolation des Transactions** : Utiliser le niveau d'isolation approprié en fonction des besoins spécifiques de la
   méthode.

4. **Propagation des Transactions** : Utiliser le mode de propagation approprié en fonction du contexte d'appel.

5. **Gestion des Exceptions** : Configurer correctement les règles de rollback pour les exceptions.

## Standards d'Utilisation

### 1. Méthodes de Lecture (Query)

Pour les méthodes qui ne modifient pas les données (lecture seule) :

```java
@Transactional(readOnly = true)
public List<EntityDTO> getAllEntities() {
    // Implémentation
}
```

- **readOnly = true** : Optimise les performances pour les opérations de lecture en indiquant à la base de données
  qu'aucune modification ne sera effectuée.
- **Propagation** : Utiliser `Propagation.SUPPORTS` si la méthode peut s'exécuter sans transaction existante.

### 2. Méthodes d'Écriture (Command)

Pour les méthodes qui modifient les données :

```java
@Transactional
public EntityDTO createEntity(CreateEntityCommand command) {
    // Implémentation
}
```

- **readOnly = false** (valeur par défaut) : Indique que la méthode modifie des données.
- **Propagation** : Utiliser `Propagation.REQUIRED` (valeur par défaut) pour s'assurer que la méthode s'exécute dans une
  transaction.

### 3. Méthodes Critiques

Pour les méthodes qui nécessitent une isolation plus stricte :

```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public EntityDTO updateCriticalEntity(UpdateEntityCommand command) {
    // Implémentation
}
```

- **isolation** : Choisir le niveau d'isolation approprié en fonction des besoins de concurrence.
- **timeout** : Définir un timeout pour éviter les blocages prolongés.

### 4. Méthodes avec Gestion d'Exceptions Spécifiques

Pour les méthodes qui doivent gérer des exceptions spécifiques :

```java
@Transactional(rollbackFor = {BusinessException.class, ValidationException.class})
public EntityDTO updateEntityWithValidation(UpdateEntityCommand command) {
    // Implémentation
}
```

- **rollbackFor** : Spécifier les exceptions qui doivent déclencher un rollback.
- **noRollbackFor** : Spécifier les exceptions qui ne doivent pas déclencher un rollback.

## Niveaux d'Isolation

Choisir le niveau d'isolation approprié en fonction des besoins spécifiques :

1. **READ_UNCOMMITTED** : Le niveau le plus bas d'isolation. Permet les lectures sales, les lectures non répétables et
   les lectures fantômes.
    - Utilisation : Rarement approprié en production, sauf pour des rapports où la cohérence absolue n'est pas critique.

2. **READ_COMMITTED** : Empêche les lectures sales, mais permet les lectures non répétables et les lectures fantômes.
    - Utilisation : Pour les opérations de lecture standard où la cohérence absolue n'est pas critique.

3. **REPEATABLE_READ** : Empêche les lectures sales et les lectures non répétables, mais permet les lectures fantômes.
    - Utilisation : Pour les opérations qui nécessitent une cohérence des données pendant toute la durée de la
      transaction.

4. **SERIALIZABLE** : Le niveau le plus élevé d'isolation. Empêche les lectures sales, les lectures non répétables et
   les lectures fantômes.
    - Utilisation : Pour les opérations critiques qui nécessitent une cohérence absolue des données.

## Modes de Propagation

Choisir le mode de propagation approprié en fonction du contexte d'appel :

1. **REQUIRED** (défaut) : Utilise une transaction existante ou en crée une nouvelle si aucune n'existe.
    - Utilisation : Pour la plupart des méthodes qui modifient des données.

2. **SUPPORTS** : Utilise une transaction existante si elle existe, sinon s'exécute sans transaction.
    - Utilisation : Pour les méthodes de lecture qui peuvent s'exécuter sans transaction.

3. **REQUIRES_NEW** : Crée toujours une nouvelle transaction, suspendant toute transaction existante.
    - Utilisation : Pour les opérations qui doivent être isolées de la transaction appelante.

4. **NESTED** : Crée une transaction imbriquée si une transaction existe, sinon se comporte comme REQUIRED.
    - Utilisation : Pour les opérations qui peuvent être annulées indépendamment de la transaction principale.

5. **NEVER** : S'exécute sans transaction et lève une exception si une transaction existe.
    - Utilisation : Pour les méthodes qui ne doivent jamais être exécutées dans une transaction.

6. **NOT_SUPPORTED** : S'exécute sans transaction, suspendant toute transaction existante.
    - Utilisation : Pour les opérations longues qui n'ont pas besoin de transaction.

7. **MANDATORY** : Utilise une transaction existante et lève une exception si aucune n'existe.
    - Utilisation : Pour les méthodes qui doivent être appelées dans le contexte d'une transaction existante.

## Bonnes Pratiques

1. **Transactions Courtes** : Garder les transactions aussi courtes que possible pour minimiser les blocages.

2. **Éviter les Appels Externes** : Éviter les appels à des services externes dans une transaction.

3. **Éviter les Transactions Imbriquées** : Préférer une conception qui minimise les transactions imbriquées.

4. **Documenter les Choix** : Documenter les choix de niveau d'isolation et de mode de propagation non standard.

5. **Tests de Concurrence** : Tester les scénarios de concurrence pour valider les choix de gestion des transactions.

## Exemples Concrets

### Service de Lecture Standard

```java
@Service
public class EntityQueryService {
    private final EntityRepository entityRepository;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<EntityDTO> getAllEntities() {
        return entityRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
```

### Service d'Écriture Standard

```java
@Service
public class EntityCommandService {
    private final EntityRepository entityRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public EntityDTO createEntity(CreateEntityCommand command) {
        Entity entity = mapToEntity(command);
        Entity savedEntity = entityRepository.save(entity);
        return mapToDTO(savedEntity);
    }
}
```

### Service avec Opération Critique

```java
@Service
public class CriticalEntityService {
    private final EntityRepository entityRepository;

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            timeout = 30,
            rollbackFor = {BusinessException.class, ValidationException.class}
    )
    public EntityDTO updateCriticalEntity(UpdateEntityCommand command) {
        // Implémentation
    }
}
```

## Configuration par Défaut

Pour assurer une cohérence dans toute l'application, les valeurs par défaut suivantes sont recommandées :

- **Propagation** : `Propagation.REQUIRED` pour les méthodes d'écriture, `Propagation.SUPPORTS` pour les méthodes de
  lecture.
- **Isolation** : `Isolation.READ_COMMITTED` pour la plupart des opérations.
- **Timeout** : 30 secondes pour les opérations standard, à ajuster selon les besoins.
- **readOnly** : `true` pour les méthodes de lecture, `false` pour les méthodes d'écriture.
- **rollbackFor** : `{Exception.class}` pour capturer toutes les exceptions.

## Implémentation dans le Projet

Pour standardiser l'utilisation de `@Transactional` dans le projet, les actions suivantes sont recommandées :

1. **Audit des Services Existants** : Examiner tous les services existants pour identifier les incohérences.

2. **Refactoring Progressif** : Mettre à jour progressivement les services pour se conformer à ces standards.

3. **Tests de Régression** : S'assurer que les modifications n'introduisent pas de régressions.

4. **Documentation** : Documenter les choix non standard et les raisons de ces choix.

5. **Formation** : Former l'équipe sur ces standards et leur importance.
