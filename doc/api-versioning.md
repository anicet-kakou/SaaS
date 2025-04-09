# Stratégie de Versionnement d'API

## Introduction

Ce document décrit la stratégie de versionnement d'API adoptée pour le projet SaaS multi-tenant assurantiel. Il explique
pourquoi nous avons choisi cette approche, comment elle est implémentée, et comment elle doit être utilisée par les
développeurs.

## Approche de Versionnement

Nous avons adopté une approche de versionnement d'API basée sur l'URL (URL Path Versioning) pour les raisons suivantes :

1. **Simplicité** : Le versionnement par URL est simple à comprendre et à implémenter
2. **Visibilité** : La version est clairement visible dans l'URL, facilitant le débogage et la documentation
3. **Compatibilité** : Fonctionne avec tous les clients HTTP et outils
4. **Documentation** : Facile à documenter et à référencer dans la documentation de l'API
5. **Évolution** : Permet une évolution claire de l'API sans casser les clients existants

## Format des URLs

Toutes les URLs d'API suivent le format suivant :

```
/api/v{version_number}/{module}/{resource}
```

Exemples :

- `/api/v1/auto/vehicles` - API de gestion des véhicules (version 1)
- `/api/v1/auto/policies` - API de gestion des polices auto (version 1)
- `/api/v1/auto/reference/fuel-types` - API de gestion des types de carburant (version 1)

## Implémentation

Pour assurer la cohérence dans l'implémentation du versionnement, nous utilisons :

1. **Classe de constantes** : `ApiVersions` centralise toutes les constantes de versionnement
2. **Annotations Spring** : `@RequestMapping` avec le préfixe de version approprié

Exemple d'utilisation :

```java
@RestController
@RequestMapping(ApiVersions.API_V1_AUTO + "/vehicles")
public class VehicleController {
    // ...
}
```

## Politique de Versionnement

### Quand créer une nouvelle version

Une nouvelle version d'API doit être créée dans les cas suivants :

1. **Changements incompatibles** : Suppression ou renommage de champs, changement de types, etc.
2. **Modifications structurelles** : Changement dans la structure des ressources ou des URLs
3. **Changements de comportement** : Modifications significatives dans le comportement de l'API

### Cycle de vie des versions

1. **Support** : Chaque version d'API est supportée pendant au moins 12 mois après la sortie de la version suivante
2. **Dépréciation** : Les versions dépréciées sont marquées comme telles dans la documentation et les en-têtes de
   réponse
3. **Retrait** : Les versions retirées ne sont plus accessibles après la période de support

## Bonnes Pratiques

1. **Commencer par v1** : Toujours commencer par la version 1, même pour la première release
2. **Versionner au niveau de l'API** : Versionner l'API entière, pas les endpoints individuels
3. **Maintenir la compatibilité** : Éviter les changements incompatibles au sein d'une même version
4. **Documenter la dépréciation** : Clairement documenter quand une version d'API est dépréciée
5. **Minimiser les versions** : Essayer de minimiser le nombre de versions actives
6. **Utiliser le versionnement sémantique** : Changements majeurs de version pour les changements incompatibles

## Documentation

Chaque version d'API doit être documentée séparément. La documentation doit inclure :

1. **Endpoints disponibles** : Liste complète des endpoints avec leurs paramètres et réponses
2. **Exemples d'utilisation** : Exemples concrets d'utilisation de l'API
3. **Changements** : Liste des changements par rapport à la version précédente
4. **Statut** : Statut de la version (active, dépréciée, retirée)
5. **Date de fin de support** : Date prévue de fin de support pour les versions dépréciées
