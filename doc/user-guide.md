# Guide Utilisateur - Plateforme SaaS

## Table des matières

1. [Introduction](#introduction)
2. [Démarrage Rapide](#démarrage-rapide)
    - [Inscription et Accès](#inscription-et-accès)
    - [Premier Appel API](#premier-appel-api)
    - [Exemples de Code](#exemples-de-code)
3. [Architecture Générale](#architecture-générale)
4. [Modules Fonctionnels](#modules-fonctionnels)
    - [Module Common](#module-common)
    - [Module Core](#module-core)
    - [Module Insurance](#module-insurance)
5. [API REST](#api-rest)
    - [Conventions de Nommage](#conventions-de-nommage)
    - [Formats de Requête et Réponse](#formats-de-requête-et-réponse)
    - [Gestion des Erreurs](#gestion-des-erreurs)
    - [Pagination et Filtrage](#pagination-et-filtrage)
    - [Versionnement](#versionnement)
6. [Environnements](#environnements)
    - [Développement](#environnement-de-développement)
    - [Test](#environnement-de-test)
    - [Production](#environnement-de-production)
7. [Authentification et Autorisation](#authentification-et-autorisation)
    - [JWT (JSON Web Token)](#jwt-json-web-token)
    - [OAuth 2.0](#oauth-20)
    - [Gestion des Rôles et Permissions](#gestion-des-rôles-et-permissions)
8. [Limites et Quotas](#limites-et-quotas)
    - [Limites d'Appels API](#limites-dappels-api)
    - [Quotas par Organisation](#quotas-par-organisation)
9. [Multi-tenant](#multi-tenant)
    - [Isolation des Données](#isolation-des-données)
    - [Gestion des Organisations](#gestion-des-organisations)
10. [Internationalisation (i18n)](#internationalisation-i18n)
    - [Gestion des Traductions](#gestion-des-traductions)
    - [Interface d'Administration](#interface-dadministration)
11. [Notifications](#notifications)
    - [Types de Notifications](#types-de-notifications)
    - [Configuration des Notifications](#configuration-des-notifications)
    - [Webhooks](#webhooks)
12. [Cas d'Utilisation Métier](#cas-dutilisation-métier)
    - [Assurance Automobile](#assurance-automobile)
    - [Assurance Habitation](#assurance-habitation)
    - [Assurance Vie](#assurance-vie)
    - [Workflows Complets](#workflows-complets)
13. [Sécurité et Conformité](#sécurité-et-conformité)
    - [Conformité RGPD](#conformité-rgpd)
    - [Bonnes Pratiques de Sécurité](#bonnes-pratiques-de-sécurité)
14. [Monitoring et Observabilité](#monitoring-et-observabilité)
    - [Métriques Disponibles](#métriques-disponibles)
    - [Surveillance des Intégrations](#surveillance-des-intégrations)
15. [Annexes](#annexes)
    - [Glossaire](#glossaire)
    - [Catalogue des Erreurs](#catalogue-des-erreurs)
    - [FAQ](#faq)

## Introduction

La plateforme SaaS est une solution complète pour la gestion des produits d'assurance, conçue selon une architecture
hexagonale (ou architecture en ports et adaptateurs). Cette architecture permet une séparation claire entre la logique
métier et les détails techniques, facilitant ainsi la maintenance, l'évolution et l'extension de la plateforme.

Cette plateforme offre une API REST complète permettant d'intégrer facilement les fonctionnalités d'assurance dans
d'autres applications ou de construire des interfaces utilisateur personnalisées. Elle est conçue pour être
multi-tenant, permettant ainsi à plusieurs organisations d'utiliser la même instance de l'application tout en maintenant
une isolation complète des données.

Ce guide utilisateur est destiné aux experts métier, MOA, MOE, BA et autres parties prenantes qui souhaitent comprendre
le fonctionnement de l'API et comment l'utiliser efficacement pour répondre aux besoins métier.

## Démarrage Rapide

Cette section vous guide à travers les premières étapes pour commencer à utiliser l'API de la plateforme SaaS.

### Inscription et Accès

Pour accéder à l'API de la plateforme SaaS, vous devez disposer d'un compte utilisateur et d'une organisation. Voici les
étapes pour obtenir un accès :

1. **Création de compte** : Contactez l'équipe d'administration pour créer un compte administrateur pour votre
   organisation.

2. **Configuration de l'organisation** : Une fois votre compte créé, vous pourrez configurer votre organisation et créer
   des comptes utilisateurs supplémentaires.

3. **Obtention des identifiants API** : Pour accéder à l'API, vous aurez besoin d'identifiants (nom d'utilisateur et mot
   de passe) ou d'une clé API.

### Premier Appel API

Voici comment effectuer votre premier appel à l'API :

1. **Obtention d'un token JWT** :

```bash
curl -X POST \
  https://api.saas-platform.com/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "votre_email@exemple.com",
    "password": "votre_mot_de_passe"
  }'
```

Réponse :

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

2. **Utilisation du token pour un appel API** :

```bash
curl -X GET \
  https://api.saas-platform.com/api/v1/organizations \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
```

### Exemples de Code

Voici des exemples d'utilisation de l'API dans différents langages :

**JavaScript (Node.js avec Axios) :**

```javascript
const axios = require('axios');

async function getAuthToken() {
  try {
    const response = await axios.post('https://api.saas-platform.com/api/v1/auth/login', {
      username: 'votre_email@exemple.com',
      password: 'votre_mot_de_passe'
    });
    return response.data.accessToken;
  } catch (error) {
    console.error('Erreur d\'authentification:', error);
    throw error;
  }
}

async function getOrganizations() {
  try {
    const token = await getAuthToken();
    const response = await axios.get('https://api.saas-platform.com/api/v1/organizations', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des organisations:', error);
    throw error;
  }
}

// Utilisation
getOrganizations().then(organizations => {
  console.log('Organisations:', organizations);
});
```

**Python (avec requests) :**

```python
import requests

def get_auth_token():
    url = "https://api.saas-platform.com/api/v1/auth/login"
    payload = {
        "username": "votre_email@exemple.com",
        "password": "votre_mot_de_passe"
    }
    response = requests.post(url, json=payload)
    response.raise_for_status()  # Lève une exception en cas d'erreur HTTP
    return response.json()["accessToken"]

def get_organizations():
    token = get_auth_token()
    url = "https://api.saas-platform.com/api/v1/organizations"
    headers = {
        "Authorization": f"Bearer {token}"
    }
    response = requests.get(url, headers=headers)
    response.raise_for_status()
    return response.json()

# Utilisation
organizations = get_organizations()
print("Organisations:", organizations)
```

## Architecture Générale

La plateforme SaaS est construite selon une architecture hexagonale (ou architecture en ports et adaptateurs), qui
sépare clairement la logique métier des détails techniques. Cette architecture est organisée en trois couches
principales :

1. **Couche Domain (Domaine)** : Contient la logique métier pure, indépendante de toute technologie ou framework. Cette
   couche définit les entités métier, les règles métier et les ports (interfaces) que les adaptateurs doivent
   implémenter.

2. **Couche Application** : Orchestre les cas d'utilisation en coordonnant les entités du domaine et en utilisant les
   ports pour communiquer avec le monde extérieur. Cette couche contient les services d'application, les DTOs (Data
   Transfer Objects) et les mappers.

3. **Couche Infrastructure** : Implémente les adaptateurs qui permettent à l'application de communiquer avec le monde
   extérieur (base de données, API externes, interface utilisateur, etc.). Cette couche contient les contrôleurs REST,
   les repositories JPA, les implémentations de services externes, etc.

Cette architecture permet une grande flexibilité et facilite les tests, car la logique métier peut être testée
indépendamment des détails techniques.

## Modules Fonctionnels

La plateforme SaaS est organisée en plusieurs modules fonctionnels, chacun ayant une responsabilité spécifique :

### Module Common

Le module Common contient les composants partagés par tous les autres modules, tels que :

- **Gestion des exceptions** : Définition des exceptions métier et techniques, ainsi que leur traitement global.
- **Internationalisation (i18n)** : Gestion des messages et des traductions.
- **Notifications** : Système de notification pour informer les utilisateurs des événements importants.
- **Utilitaires** : Classes utilitaires pour faciliter le développement.
- **Configuration** : Configuration commune à tous les modules.

### Module Core

Le module Core contient les fonctionnalités de base de la plateforme, telles que :

- **Sécurité** : Authentification, autorisation, gestion des utilisateurs, des rôles et des permissions.
- **Organisation** : Gestion des organisations et de la hiérarchie organisationnelle.
- **Audit** : Suivi des modifications apportées aux entités.

### Module Insurance

Le module Insurance contient les fonctionnalités spécifiques à l'assurance, telles que :

- **Assurance Automobile** : Gestion des polices d'assurance automobile, des véhicules, des conducteurs, etc.
- **Assurance Habitation** : Gestion des polices d'assurance habitation, des biens immobiliers, etc.
- **Assurance Vie** : Gestion des polices d'assurance vie, des bénéficiaires, etc.
- **Références** : Gestion des données de référence communes à tous les types d'assurance.

## API REST

L'API REST de la plateforme SaaS suit les principes REST (Representational State Transfer) et utilise le format JSON
pour les échanges de données.

### Conventions de Nommage

Les endpoints de l'API suivent une convention de nommage cohérente :

- **Base URL** : `/api/v1`
- **Ressources** : Noms au pluriel, en minuscules, séparés par des tirets (kebab-case)
- **Identifiants** : Inclus dans l'URL pour les opérations sur une ressource spécifique
- **Actions** : Verbes HTTP appropriés (GET, POST, PUT, DELETE, PATCH)

Exemples :

- `GET /api/v1/organizations` : Récupère la liste des organisations
- `GET /api/v1/organizations/{id}` : Récupère une organisation spécifique
- `POST /api/v1/organizations` : Crée une nouvelle organisation
- `PUT /api/v1/organizations/{id}` : Met à jour une organisation existante
- `DELETE /api/v1/organizations/{id}` : Supprime une organisation

### Formats de Requête et Réponse

**Format de Requête** :

- Content-Type : `application/json`
- Corps de la requête : Objet JSON contenant les données à traiter

**Format de Réponse** :

- Content-Type : `application/json`
- Corps de la réponse : Objet JSON contenant les données demandées ou un message d'erreur

Exemple de réponse réussie :

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Acme Inc.",
  "code": "ACME",
  "type": "ENTERPRISE",
  "status": "ACTIVE",
  "createdAt": "2023-01-01T12:00:00Z",
  "updatedAt": "2023-01-02T14:30:00Z"
}
```

### Gestion des Erreurs

En cas d'erreur, l'API renvoie une réponse avec un code HTTP approprié et un corps JSON contenant des informations sur
l'erreur :

```json
{
  "code": "resource.not.found",
  "message": "La ressource demandée n'a pas été trouvée",
  "status": 404,
  "path": "/api/v1/organizations/123",
  "timestamp": "2023-01-01T12:00:00Z",
  "correlationId": "abc123",
  "errors": [
    {
      "field": "id",
      "message": "L'identifiant spécifié n'existe pas"
    }
  ]
}
```

Les codes d'erreur sont organisés de manière hiérarchique pour faciliter leur identification et leur traitement :

- Erreurs communes : `common.*`
- Erreurs de sécurité : `security.*`
- Erreurs d'organisation : `organization.*`
- Erreurs d'assurance : `insurance.*`

### Pagination et Filtrage

Pour les endpoints qui renvoient des collections, l'API prend en charge la pagination et le filtrage :

**Pagination** :

- `page` : Numéro de page (commence à 0)
- `size` : Nombre d'éléments par page
- `sort` : Champ de tri, suivi de `,asc` ou `,desc` pour spécifier l'ordre

Exemple : `GET /api/v1/organizations?page=0&size=10&sort=name,asc`

**Filtrage** :

- Paramètres de requête spécifiques à chaque ressource

Exemple : `GET /api/v1/organizations?status=ACTIVE&type=ENTERPRISE`

**Réponse paginée** :

```json
{
  "content": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "Acme Inc.",
      "code": "ACME",
      "type": "ENTERPRISE",
      "status": "ACTIVE"
    },
    {
      "id": "223e4567-e89b-12d3-a456-426614174001",
      "name": "Beta Corp.",
      "code": "BETA",
      "type": "ENTERPRISE",
      "status": "ACTIVE"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageNumber": 0,
    "pageSize": 10,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 42,
  "totalPages": 5,
  "last": false,
  "first": true,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 10,
  "size": 10,
  "number": 0,
  "empty": false
}
```

### Versionnement

L'API est versionnée pour assurer la compatibilité ascendante. La version est incluse dans l'URL de base :

- Version actuelle : `/api/v1`
- Versions futures : `/api/v2`, `/api/v3`, etc.

La politique de versionnement de l'API est la suivante :

- Les modifications non compatibles avec les versions antérieures entraînent une incrémentation de la version majeure (
  v1 → v2)
- Les ajouts de fonctionnalités compatibles avec les versions antérieures sont effectués dans la version actuelle
- Les versions antérieures sont maintenues pendant au moins 12 mois après la sortie d'une nouvelle version majeure
- Un préavis de 3 mois est donné avant la dépréciation d'une version

## Environnements

La plateforme SaaS est disponible dans plusieurs environnements, chacun ayant un objectif spécifique.

### Environnement de Développement

L'environnement de développement est destiné aux tests initiaux et au développement des intégrations.

- **URL de base** : `https://dev-api.saas-platform.com`
- **Stabilité** : Peut contenir des fonctionnalités en cours de développement
- **Données** : Données de test, régulièrement réinitialisées
- **Disponibilité** : 99% (maintenance planifiée possible sans préavis)

### Environnement de Test

L'environnement de test (ou pré-production) est destiné aux tests d'intégration avant le déploiement en production.

- **URL de base** : `https://test-api.saas-platform.com`
- **Stabilité** : Stable, contient les mêmes fonctionnalités que la production
- **Données** : Données de test, plus stables que l'environnement de développement
- **Disponibilité** : 99,5% (maintenance planifiée avec préavis)

### Environnement de Production

L'environnement de production est destiné aux applications en production.

- **URL de base** : `https://api.saas-platform.com`
- **Stabilité** : Très stable, fonctionnalités complètement testées
- **Données** : Données réelles
- **Disponibilité** : 99,9% (SLA garanti)

## Limites et Quotas

Pour assurer la stabilité et les performances de la plateforme, des limites et des quotas sont appliqués aux appels API.

### Limites d'Appels API

Les limites suivantes s'appliquent à tous les utilisateurs :

| Environnement | Limite par seconde | Limite par minute  | Limite par heure  |
|---------------|--------------------|--------------------|-------------------|
| Développement | 10 requêtes/s      | 300 requêtes/min   | 5 000 requêtes/h  |
| Test          | 20 requêtes/s      | 600 requêtes/min   | 10 000 requêtes/h |
| Production    | 50 requêtes/s      | 1 500 requêtes/min | 30 000 requêtes/h |

En cas de dépassement de ces limites, l'API renvoie une réponse avec le code HTTP 429 (Too Many Requests) et un en-tête
`Retry-After` indiquant le nombre de secondes à attendre avant de réessayer.

### Quotas par Organisation

Des quotas mensuels sont également appliqués par organisation :

| Plan         | Quota mensuel       | Coût par requête supplémentaire |
|--------------|---------------------|---------------------------------|
| Basic        | 1 000 000 requêtes  | 0,001 €                         |
| Professional | 5 000 000 requêtes  | 0,0008 €                        |
| Enterprise   | 20 000 000 requêtes | 0,0005 €                        |

Pour demander une augmentation des limites ou des quotas, veuillez contacter votre gestionnaire de compte.

## Authentification et Autorisation

La plateforme SaaS utilise plusieurs mécanismes d'authentification et d'autorisation pour sécuriser l'accès à l'API.

### JWT (JSON Web Token)

L'authentification principale se fait via JWT (JSON Web Token) :

1. **Obtention du token** :
   ```
   POST /api/v1/auth/login
   {
     "username": "user@example.com",
     "password": "password123"
   }
   ```

   Réponse :
   ```json
   {
     "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "tokenType": "Bearer",
     "expiresIn": 900
   }
   ```

2. **Utilisation du token** :
   Inclure le token dans l'en-tête `Authorization` de chaque requête :
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

3. **Rafraîchissement du token** :
   ```
   POST /api/v1/auth/refresh
   {
     "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }
   ```

### OAuth 2.0

La plateforme prend également en charge l'authentification OAuth 2.0 pour l'intégration avec des fournisseurs d'identité
externes.

### Gestion des Rôles et Permissions

L'autorisation est basée sur un système de rôles et de permissions :

- **Rôles** : Groupes de permissions assignés aux utilisateurs
- **Permissions** : Droits d'accès spécifiques à des ressources et des actions

Les permissions suivent le format `{ressource}:{action}`, par exemple :

- `organization:read` : Permission de lire les informations des organisations
- `organization:write` : Permission de créer ou modifier des organisations
- `policy:create` : Permission de créer des polices d'assurance

## Multi-tenant

La plateforme SaaS est conçue pour être multi-tenant, permettant à plusieurs organisations d'utiliser la même instance
de l'application tout en maintenant une isolation complète des données.

### Isolation des Données

L'isolation des données est assurée par un mécanisme de filtrage automatique basé sur l'organisation de l'utilisateur
connecté. Chaque entité métier est associée à une organisation, et les utilisateurs ne peuvent accéder qu'aux données de
leur propre organisation.

### Gestion des Organisations

Les organisations peuvent être organisées de manière hiérarchique, avec des organisations parentes et des organisations
enfants. Cette hiérarchie permet de modéliser des structures organisationnelles complexes, comme des groupes
d'entreprises, des filiales, des départements, etc.

## Internationalisation (i18n)

La plateforme SaaS prend en charge l'internationalisation (i18n) pour permettre la traduction de l'interface utilisateur
et des messages d'erreur dans différentes langues.

### Gestion des Traductions

Les traductions sont organisées de manière modulaire, avec chaque module gérant ses propres fichiers de traduction.
Cette approche permet une meilleure organisation et facilite la maintenance des traductions.

Structure des fichiers de traduction :

- `common/i18n/errors/errors_fr.properties` : Messages d'erreur communs en français
- `common/i18n/validation/validation_fr.properties` : Messages de validation communs en français
- `core/security/i18n/errors/errors_fr.properties` : Messages d'erreur spécifiques à la sécurité en français
- etc.

### Interface d'Administration

Une interface d'administration des traductions est disponible pour permettre aux utilisateurs de gérer les traductions
sans avoir à modifier les fichiers de propriétés directement.

Endpoints de l'API de gestion des traductions :

- `GET /api/v1/translations` : Récupère la liste des traductions
- `GET /api/v1/translations/{id}` : Récupère une traduction spécifique
- `POST /api/v1/translations` : Crée une nouvelle traduction
- `PUT /api/v1/translations/{id}` : Met à jour une traduction existante
- `DELETE /api/v1/translations/{id}` : Supprime une traduction

## Notifications

La plateforme SaaS inclut un système de notification pour informer les utilisateurs des événements importants.

### Types de Notifications

Plusieurs types de notifications sont disponibles :

- **INFO** : Informations générales
- **SUCCESS** : Opérations réussies
- **WARNING** : Avertissements
- **ERROR** : Erreurs
- **SYSTEM** : Notifications système

### Configuration des Notifications

Les notifications peuvent être configurées pour être envoyées via différents canaux :

- Interface utilisateur (notifications in-app)
- Email
- SMS (si configuré)
- Webhooks (pour l'intégration avec des systèmes externes)

Endpoints de l'API de gestion des notifications :

- `GET /api/v1/notifications` : Récupère la liste des notifications de l'utilisateur courant
- `GET /api/v1/notifications/unread` : Récupère la liste des notifications non lues
- `GET /api/v1/notifications/{id}` : Récupère une notification spécifique
- `PUT /api/v1/notifications/{id}/read` : Marque une notification comme lue
- `PUT /api/v1/notifications/read-all` : Marque toutes les notifications comme lues
- `DELETE /api/v1/notifications/{id}` : Supprime une notification

### Webhooks

Les webhooks permettent à la plateforme SaaS d'envoyer des notifications en temps réel à votre système lorsque certains
événements se produisent. Cela vous permet de réagir immédiatement aux changements sans avoir à interroger régulièrement
l'API.

**Configuration des webhooks** :

```
POST /api/v1/webhooks
{
  "url": "https://votre-serveur.com/webhook-endpoint",
  "events": ["policy.created", "policy.updated", "claim.created"],
  "secret": "votre_secret_pour_verifier_la_signature"
}
```

**Format des événements** :

Lorsqu'un événement se produit, la plateforme envoie une requête POST à l'URL configurée avec les données suivantes :

```json
{
  "id": "evt_123456789",
  "type": "policy.created",
  "created": "2023-01-01T12:00:00Z",
  "data": {
    "policy": {
      "id": "pol_123456789",
      "policyNumber": "AUTO-2023-001",
      "startDate": "2023-01-01",
      "endDate": "2024-01-01",
      "premium": 1200.00,
      "status": "ACTIVE"
    }
  }
}
```

**Vérification de la signature** :

Pour vérifier l'authenticité des webhooks, la plateforme inclut un en-tête `X-Signature` contenant une signature
HMAC-SHA256 du corps de la requête, générée avec le secret que vous avez fourni lors de la configuration du webhook.

**Gestion des échecs** :

En cas d'échec de livraison d'un webhook (si votre serveur renvoie un code d'erreur ou ne répond pas), la plateforme
réessaie selon le calendrier suivant :

- 1ère tentative : immédiatement
- 2ème tentative : après 5 minutes
- 3ème tentative : après 15 minutes
- 4ème tentative : après 30 minutes
- 5ème tentative : après 1 heure
- 6ème tentative : après 3 heures
- 7ème tentative : après 6 heures
- 8ème tentative : après 12 heures

Après 8 tentatives infructueuses, l'événement est marqué comme échoué et n'est plus réessayé.

## Cas d'Utilisation Métier

### Assurance Automobile

Le module d'assurance automobile permet de gérer les polices d'assurance automobile, les véhicules, les conducteurs,
etc.

**Création d'une police d'assurance automobile** :

```
POST /api/v1/insurance/auto/policies
{
  "policyNumber": "AUTO-2023-001",
  "startDate": "2023-01-01",
  "endDate": "2024-01-01",
  "premium": 1200.00,
  "vehicle": {
    "registrationNumber": "AB-123-CD",
    "make": "Toyota",
    "model": "Corolla",
    "year": 2020,
    "value": 25000.00
  },
  "drivers": [
    {
      "firstName": "John",
      "lastName": "Doe",
      "birthDate": "1980-01-01",
      "licenseNumber": "12345678",
      "licenseType": "B",
      "licenseIssueDate": "2000-01-01"
    }
  ],
  "coverages": [
    {
      "type": "LIABILITY",
      "limit": 1000000.00,
      "deductible": 500.00
    },
    {
      "type": "COLLISION",
      "limit": 25000.00,
      "deductible": 1000.00
    }
  ]
}
```

**Récupération d'une police d'assurance automobile** :

```
GET /api/v1/insurance/auto/policies/{id}
```

**Mise à jour d'une police d'assurance automobile** :

```
PUT /api/v1/insurance/auto/policies/{id}
{
  "premium": 1300.00,
  "coverages": [
    {
      "type": "LIABILITY",
      "limit": 1500000.00,
      "deductible": 500.00
    },
    {
      "type": "COLLISION",
      "limit": 25000.00,
      "deductible": 1000.00
    }
  ]
}
```

### Assurance Habitation

Le module d'assurance habitation permet de gérer les polices d'assurance habitation, les biens immobiliers, etc.

**Création d'une police d'assurance habitation** :

```
POST /api/v1/insurance/home/policies
{
  "policyNumber": "HOME-2023-001",
  "startDate": "2023-01-01",
  "endDate": "2024-01-01",
  "premium": 800.00,
  "property": {
    "address": {
      "street": "123 Main St",
      "city": "Anytown",
      "state": "CA",
      "zipCode": "12345",
      "country": "USA"
    },
    "type": "HOUSE",
    "yearBuilt": 2000,
    "squareMeters": 150,
    "value": 350000.00
  },
  "coverages": [
    {
      "type": "BUILDING",
      "limit": 350000.00,
      "deductible": 1000.00
    },
    {
      "type": "CONTENTS",
      "limit": 100000.00,
      "deductible": 500.00
    },
    {
      "type": "LIABILITY",
      "limit": 500000.00,
      "deductible": 0.00
    }
  ]
}
```

**Récupération d'une police d'assurance habitation** :

```
GET /api/v1/insurance/home/policies/{id}
```

### Assurance Vie

Le module d'assurance vie permet de gérer les polices d'assurance vie, les bénéficiaires, etc.

**Création d'une police d'assurance vie** :

```
POST /api/v1/insurance/life/policies
{
  "policyNumber": "LIFE-2023-001",
  "startDate": "2023-01-01",
  "endDate": "2053-01-01",
  "premium": 1500.00,
  "premiumFrequency": "MONTHLY",
  "insured": {
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "1980-01-01",
    "gender": "MALE",
    "smoker": false,
    "occupation": "Engineer"
  },
  "coverageAmount": 500000.00,
  "policyType": "TERM",
  "termYears": 30,
  "beneficiaries": [
    {
      "firstName": "Jane",
      "lastName": "Doe",
      "relationship": "SPOUSE",
      "percentage": 100
    }
  ]
}
```

**Récupération d'une police d'assurance vie** :

```
GET /api/v1/insurance/life/policies/{id}
```

### Workflows Complets

Voici des exemples de workflows complets qui illustrent comment utiliser l'API pour des scénarios métier courants.

#### Création d'un client et souscription d'une police d'assurance automobile

1. **Création d'un client** :

```
POST /api/v1/customers
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "zipCode": "12345",
    "country": "USA"
  },
  "birthDate": "1980-01-01"
}
```

2. **Création d'une police d'assurance automobile** :

```
POST /api/v1/insurance/auto/policies
{
  "customerId": "cus_123456789",
  "policyNumber": "AUTO-2023-001",
  "startDate": "2023-01-01",
  "endDate": "2024-01-01",
  "premium": 1200.00,
  "vehicle": {
    "registrationNumber": "AB-123-CD",
    "make": "Toyota",
    "model": "Corolla",
    "year": 2020,
    "value": 25000.00
  },
  "drivers": [
    {
      "firstName": "John",
      "lastName": "Doe",
      "birthDate": "1980-01-01",
      "licenseNumber": "12345678",
      "licenseType": "B",
      "licenseIssueDate": "2000-01-01"
    }
  ],
  "coverages": [
    {
      "type": "LIABILITY",
      "limit": 1000000.00,
      "deductible": 500.00
    },
    {
      "type": "COLLISION",
      "limit": 25000.00,
      "deductible": 1000.00
    }
  ]
}
```

3. **Génération et envoi du contrat** :

```
POST /api/v1/insurance/auto/policies/{id}/contract
{
  "format": "PDF",
  "sendEmail": true,
  "emailRecipients": ["john.doe@example.com"]
}
```

4. **Paiement de la prime** :

```
POST /api/v1/payments
{
  "policyId": "pol_123456789",
  "amount": 1200.00,
  "method": "CREDIT_CARD",
  "paymentDetails": {
    "cardNumber": "4111111111111111",
    "expiryMonth": 12,
    "expiryYear": 2025,
    "cvv": "123"
  }
}
```

#### Déclaration et traitement d'un sinistre

1. **Déclaration d'un sinistre** :

```
POST /api/v1/insurance/auto/claims
{
  "policyId": "pol_123456789",
  "incidentDate": "2023-06-15T14:30:00Z",
  "description": "Accident de la route avec un autre véhicule",
  "location": {
    "latitude": 48.8566,
    "longitude": 2.3522,
    "address": "Avenue des Champs-Élysées, Paris, France"
  },
  "damageType": "COLLISION",
  "thirdParties": [
    {
      "name": "Jane Smith",
      "phone": "+1987654321",
      "email": "jane.smith@example.com",
      "vehicleMake": "Honda",
      "vehicleModel": "Civic",
      "vehicleYear": 2019,
      "insuranceCompany": "Other Insurance Co",
      "policyNumber": "OIC-2023-001"
    }
  ],
  "witnesses": [
    {
      "name": "Bob Johnson",
      "phone": "+1122334455",
      "email": "bob.johnson@example.com"
    }
  ]
}
```

2. **Ajout de documents au sinistre** :

```
POST /api/v1/insurance/auto/claims/{id}/documents
{
  "type": "PHOTO",
  "description": "Photo des dommages",
  "file": "base64_encoded_file_content"
}
```

3. **Mise à jour du statut du sinistre** :

```
PATCH /api/v1/insurance/auto/claims/{id}
{
  "status": "IN_PROGRESS",
  "assignedTo": "agent_123456789"
}
```

4. **Ajout d'une estimation** :

```
POST /api/v1/insurance/auto/claims/{id}/estimates
{
  "amount": 3500.00,
  "description": "Réparation du pare-chocs et de l'aile avant",
  "repairShop": "Auto Repair Shop",
  "estimateDate": "2023-06-20T10:00:00Z"
}
```

5. **Approbation du sinistre et paiement** :

```
POST /api/v1/insurance/auto/claims/{id}/approve
{
  "approvedAmount": 3200.00,
  "paymentMethod": "BANK_TRANSFER",
  "paymentDetails": {
    "bankName": "Example Bank",
    "accountNumber": "1234567890",
    "routingNumber": "987654321"
  },
  "notes": "Sinistre approuvé après vérification des documents et de l'estimation"
}
```

## Sécurité et Conformité

### Conformité RGPD

La plateforme SaaS est conçue pour être conforme au Règlement Général sur la Protection des Données (RGPD) de l'Union
européenne. Voici les principales mesures mises en place :

- **Consentement** : Les utilisateurs doivent donner leur consentement explicite pour la collecte et le traitement de
  leurs données personnelles.
- **Droit d'accès** : Les utilisateurs peuvent accéder à leurs données personnelles via l'API.
- **Droit à l'oubli** : Les utilisateurs peuvent demander la suppression de leurs données personnelles.
- **Portabilité des données** : Les utilisateurs peuvent exporter leurs données dans un format structuré.
- **Sécurité des données** : Les données sont chiffrées en transit et au repos.
- **Journalisation des accès** : Tous les accès aux données personnelles sont journalisés.

Endpoints RGPD :

- `GET /api/v1/gdpr/data-export` : Exporte les données personnelles de l'utilisateur courant
- `POST /api/v1/gdpr/data-deletion-request` : Demande la suppression des données personnelles de l'utilisateur courant
- `GET /api/v1/gdpr/data-processing-activities` : Liste les activités de traitement des données

### Bonnes Pratiques de Sécurité

Pour sécuriser votre intégration avec l'API, nous recommandons les bonnes pratiques suivantes :

1. **Gestion des tokens** :
    - Stockez les tokens JWT de manière sécurisée (pas dans le stockage local du navigateur)
    - Renouvelez régulièrement les tokens avec le token de rafraîchissement
    - Ne transmettez jamais les tokens via des canaux non sécurisés

2. **Validation des entrées** :
    - Validez toutes les entrées utilisateur côté client avant de les envoyer à l'API
    - Utilisez des bibliothèques de validation pour vérifier les formats, les types et les plages de valeurs

3. **Gestion des erreurs** :
    - Ne montrez pas les détails techniques des erreurs aux utilisateurs finaux
    - Journalisez les erreurs pour le débogage, mais sans inclure de données sensibles

4. **Protection contre les attaques courantes** :
    - Implémentez une protection contre les attaques CSRF
    - Utilisez des en-têtes de sécurité comme Content-Security-Policy, X-XSS-Protection, etc.
    - Limitez le nombre de tentatives de connexion pour prévenir les attaques par force brute

5. **Audit et surveillance** :
    - Surveillez les activités suspectes (nombreuses requêtes, accès à des ressources inhabituelles, etc.)
    - Mettez en place des alertes pour les activités anormales

## Monitoring et Observabilité

### Métriques Disponibles

La plateforme SaaS expose diverses métriques pour surveiller l'état et les performances de vos intégrations :

1. **Métriques d'API** :
    - Nombre total d'appels API
    - Taux de réussite/échec
    - Temps de réponse moyen, médian, 95e et 99e percentiles
    - Distribution des codes de statut HTTP

2. **Métriques métier** :
    - Nombre de polices créées/modifiées/supprimées
    - Nombre de sinistres déclarés/traités/clôturés
    - Montant total des primes/sinistres

3. **Métriques de quota** :
    - Utilisation actuelle du quota
    - Pourcentage du quota utilisé
    - Prévision d'utilisation du quota

Ces métriques sont accessibles via l'API de monitoring :

```
GET /api/v1/monitoring/metrics
```

### Surveillance des Intégrations

Pour surveiller vos intégrations, vous pouvez utiliser les outils suivants :

1. **Tableau de bord** :
    - Un tableau de bord web est disponible à l'adresse `https://dashboard.saas-platform.com`
    - Il affiche les métriques en temps réel et historiques
    - Il permet de configurer des alertes personnalisées

2. **Alertes** :
    - Configurez des alertes basées sur des seuils pour être notifié en cas de problème
    - Les alertes peuvent être envoyées par email, SMS, webhook ou intégrées à des outils comme Slack ou PagerDuty

3. **Journaux d'audit** :
    - Tous les appels API sont journalisés avec des informations détaillées
    - Les journaux peuvent être consultés via l'API ou le tableau de bord
    - Les journaux sont conservés pendant 90 jours

4. **Statut du service** :
    - L'état actuel du service est disponible à l'adresse `https://status.saas-platform.com`
    - Il affiche les incidents en cours et planifiés
    - Vous pouvez vous abonner aux notifications de statut par email ou RSS

## Annexes

### Glossaire

- **API** : Application Programming Interface, interface de programmation d'application
- **REST** : Representational State Transfer, style d'architecture pour les systèmes distribués
- **JWT** : JSON Web Token, standard ouvert pour la création de jetons d'accès
- **OAuth** : Protocole d'autorisation ouvert
- **Multi-tenant** : Architecture permettant à plusieurs organisations d'utiliser la même instance d'une application
- **i18n** : Internationalisation, processus de conception d'une application pour qu'elle puisse être adaptée à
  différentes langues et régions
- **SLA** : Service Level Agreement, accord de niveau de service
- **RGPD** : Règlement Général sur la Protection des Données
- **Webhook** : Mécanisme permettant à une application d'envoyer des données en temps réel à d'autres applications

### Catalogue des Erreurs

Voici un catalogue des principaux codes d'erreur que vous pouvez rencontrer lors de l'utilisation de l'API :

#### Erreurs communes (common.*)

| Code                       | Message                                   | Description                                       | Solution                                                                  |
|----------------------------|-------------------------------------------|---------------------------------------------------|---------------------------------------------------------------------------|
| common.resource.not.found  | La ressource demandée n'a pas été trouvée | L'identifiant spécifié n'existe pas               | Vérifiez l'identifiant et assurez-vous qu'il est correct                  |
| common.validation.error    | Erreur de validation                      | Les données fournies ne sont pas valides          | Consultez le champ `errors` pour plus de détails                          |
| common.unauthorized        | Non autorisé                              | L'utilisateur n'est pas authentifié               | Authentifiez-vous avec des identifiants valides                           |
| common.forbidden           | Accès refusé                              | L'utilisateur n'a pas les permissions nécessaires | Demandez les permissions nécessaires à votre administrateur               |
| common.rate.limit.exceeded | Limite de débit dépassée                  | Trop de requêtes ont été effectuées               | Attendez le temps indiqué dans l'en-tête `Retry-After` avant de réessayer |
| common.internal.error      | Erreur interne du serveur                 | Une erreur s'est produite sur le serveur          | Contactez le support technique                                            |

#### Erreurs de sécurité (security.*)

| Code                         | Message                | Description                                                                      | Solution                                                            |
|------------------------------|------------------------|----------------------------------------------------------------------------------|---------------------------------------------------------------------|
| security.invalid.credentials | Identifiants invalides | Le nom d'utilisateur ou le mot de passe est incorrect                            | Vérifiez vos identifiants                                           |
| security.account.locked      | Compte verrouillé      | Le compte a été verrouillé suite à trop de tentatives de connexion infructueuses | Contactez votre administrateur pour déverrouiller le compte         |
| security.token.expired       | Token expiré           | Le token JWT a expiré                                                            | Utilisez le token de rafraîchissement pour obtenir un nouveau token |
| security.token.invalid       | Token invalide         | Le token JWT est invalide ou a été falsifié                                      | Authentifiez-vous à nouveau pour obtenir un nouveau token           |

#### Erreurs d'organisation (organization.*)

| Code                        | Message                    | Description                               | Solution                                                   |
|-----------------------------|----------------------------|-------------------------------------------|------------------------------------------------------------|
| organization.not.found      | Organisation non trouvée   | L'organisation spécifiée n'existe pas     | Vérifiez l'identifiant de l'organisation                   |
| organization.already.exists | L'organisation existe déjà | Une organisation avec ce code existe déjà | Utilisez un code différent                                 |
| organization.inactive       | Organisation inactive      | L'organisation est inactive               | Contactez votre administrateur pour activer l'organisation |

#### Erreurs d'assurance (insurance.*)

| Code                           | Message               | Description                       | Solution                                                    |
|--------------------------------|-----------------------|-----------------------------------|-------------------------------------------------------------|
| insurance.policy.not.found     | Police non trouvée    | La police spécifiée n'existe pas  | Vérifiez l'identifiant de la police                         |
| insurance.policy.expired       | Police expirée        | La police a expiré                | Renouvelez la police                                        |
| insurance.claim.not.found      | Sinistre non trouvé   | Le sinistre spécifié n'existe pas | Vérifiez l'identifiant du sinistre                          |
| insurance.claim.already.closed | Sinistre déjà clôturé | Le sinistre a déjà été clôturé    | Aucune action supplémentaire n'est possible sur ce sinistre |

### FAQ

**Q: Comment puis-je obtenir un token d'authentification ?**
R: Vous pouvez obtenir un token d'authentification en envoyant une requête POST à `/api/v1/auth/login` avec vos
identifiants.

**Q: Comment puis-je filtrer les résultats d'une requête ?**
R: Vous pouvez filtrer les résultats en ajoutant des paramètres de requête à l'URL, par exemple :
`/api/v1/organizations?status=ACTIVE&type=ENTERPRISE`.

**Q: Comment puis-je paginer les résultats d'une requête ?**
R: Vous pouvez paginer les résultats en ajoutant les paramètres `page` et `size` à l'URL, par exemple :
`/api/v1/organizations?page=0&size=10`.

**Q: Comment puis-je trier les résultats d'une requête ?**
R: Vous pouvez trier les résultats en ajoutant le paramètre `sort` à l'URL, par exemple :
`/api/v1/organizations?sort=name,asc`.

**Q: Comment puis-je gérer les erreurs ?**
R: Les erreurs sont renvoyées avec un code HTTP approprié et un corps JSON contenant des informations sur l'erreur. Vous
pouvez utiliser le champ `code` pour identifier le type d'erreur et le champ `message` pour obtenir une description de
l'erreur.

**Q: Comment puis-je augmenter mes limites d'appels API ?**
R: Contactez votre gestionnaire de compte pour demander une augmentation des limites d'appels API.

**Q: Comment puis-je signaler un problème avec l'API ?**
R: Vous pouvez signaler un problème en envoyant un email à `api-support@saas-platform.com` ou en ouvrant un ticket de
support via le portail client.

**Q: L'API est-elle disponible dans d'autres régions ?**
R: Actuellement, l'API est disponible dans les régions Europe, Amérique du Nord et Asie-Pacifique. Contactez-nous pour
plus d'informations sur la disponibilité dans d'autres régions.

**Q: Comment puis-je tester l'API sans affecter les données de production ?**
R: Utilisez l'environnement de développement ou de test pour effectuer vos tests sans affecter les données de
production.
