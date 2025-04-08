# Documentation technique

# Intégration API Assurance Digitale AAS

*Version 1.9 - Juillet 2024*
© 2024 Diotali

---

## Identification

| Référence        | AAS/API                                                                                                                                                                                            |
|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Titre            | Description APIs Assurance Digitale AAS                                                                                                                                                            |
| Type de Document | Description Technique                                                                                                                                                                              |
| Projet           | AAS – Assurance Digitale                                                                                                                                                                           |
| Résumé           | Ce document décrit les différentes APIs mis à disposition des compagnies d'assurance dans le cadre de la digitalisation de l'assurance automobile par l'AAS (Association des Assureurs du Sénégal) |

## Révisions

| Version | Auteur  | Objet                                                                                                                                                                                                                                                                                                         |
|---------|---------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.0     | Diotali | Version Initiale                                                                                                                                                                                                                                                                                              |
| 1.2     | Diotali | Mise à jour URL de vérification Statut Attestation                                                                                                                                                                                                                                                            |
| 1.3     | Diotali | Ajout API de de gestion des flottes                                                                                                                                                                                                                                                                           |
| 1.4     | Diotali | Ajout tableau description Genre                                                                                                                                                                                                                                                                               |
| 1.5     | Diotali | Ajout Meta données de calcul de la RC<br>Mise à jour Payload et Response des différents API<br>Ajout des codes d'erreurs                                                                                                                                                                                      |
| 1.6     | Diotali | Modification process et API gestion commande                                                                                                                                                                                                                                                                  |
| 1.7     | Diotali | API Annulation Commande<br>API d'incorporation à une flotte<br>API de suppression de véhicule d'une flotte                                                                                                                                                                                                    |
| 1.8     | Diotali | Mise à jour tableau des Métadonnées attendues<br>Rajout lien vers l'attestation Carte Brune                                                                                                                                                                                                                   |
| 1.9     | Diotali | Mise à jour web service de calcul de la flotte (rc-flotte-reques)<br>Mise à jour Web service de génération attestations flotte (qrcode-flotte-request)<br>Mise à jour Web service d'incorporation dans une flotte (incorpore-flotte-request)<br>Mise à jour Web service suppression (subtract-flotte-request) |
| 2.0     | Diotali | Ajout Web Service dédié gestion des Motocycle<br>Ajout Web service dédié gestion des Remorques<br>Ajout des catégories (genres) supplémentaires                                                                                                                                                               |

## Diffusion

| Destinataires          | Pour Info | Pour Action |
|------------------------|-----------|-------------|
| Compagnie Affiliée AAS | X         | X           |

## Table des matières

1. [Introduction](#1-introduction)
    1. [Objet du document](#11-objet-du-document)
    2. [Contenu et structure](#12-contenu-et-structure)
2. [Sécurité](#2-sécurité)
    1. [Identification](#21-identification)
    2. [Authentification](#22-authentification)
3. [Description des APIs](#3-description-des-apis)
    1. [Principe des échanges](#31-principe-des-échanges)
    2. [URLs de bases](#32-urls-de-bases)
    3. [Méta Données](#33-méta-données)
        1. [Périodicité et durée](#331-périodicité-et-durée)
        2. [Genre](#332-genre)
        3. [Energie](#333-energie)
        4. [Type Personne](#334-type-personne)
        5. [Type d'usage catégorie 5 deux roues](#335-type-dusage-catégorie-5-deux-roues)
4. [Authentification](#4-authentification)
    1. [API de création d'une session](#41-api-de-création-dune-session)
5. [APIs de Gestion des commandes](#5-apis-de-gestion-des-commandes)
    1. [API d'initialisation d'une commande (commande)](#51-api-dinitialisation-dune-commande-commande)
    2. [API de validation d'une commande (valid-commande)](#52-api-de-validation-dune-commande-valid-commande)
    3. [API liste des commandes (commandes)](#53-api-liste-des-commandes-commandes)
    4. [API de vérification solde Qr Code (stock-qr)](#54-api-de-vérification-solde-qr-code-stock-qr)
    5. [API d'annulation d'une commande (cancel)](#55-api-dannulation-dune-commande-cancel)
6. [APIs de Gestion des attestations Mono](#6-apis-de-gestion-des-attestations-mono)
    1. [API de calcul de la RC d'une attestation Mono (rc-request)](#61-api-de-calcul-de-la-rc-dune-attestation-mono-rc-request)
    2. [API de génération d'une attestation Mono (qrcode-request)](#62-api-de-génération-dune-attestation-mono-qrcode-request)
    3. [API d'invalidation d'une attestation Mono (qrcode-cancel)](#63-api-dinvalidation-dune-attestation-mono-qrcode-cancel)
    4. [API de calcul de la RC d'une attestation Remorque (remorque-rc-request)](#64-api-de-calcul-de-la-rc-dune-attestation-remorque-remorque-rc-request)
    5. [API de génération d'une attestation Remorque (remorque-qrcode-request)](#65-api-de-génération-dune-attestation-remorque-remorque-qrcode-request)
7. [APIs de Gestion des attestations deux roues (C5)](#7-apis-de-gestion-des-attestations-deux-roues-c5)
    1. [API de calcul de la RC d'une attestation deux roues (moto-rc-request)](#71-api-de-calcul-de-la-rc-dune-attestation-deux-roues-moto-rc-request)
    2. [API de génération d'une attestation deux roues (moto-qrcode-request)](#72-api-de-génération-dune-attestation-deux-roues-moto-qrcode-request)
8. [APIs de Gestion des attestations Flotte](#8-apis-de-gestion-des-attestations-flotte)
    1. [API de calcul de la RC pour flotte (rc-flotte-request)](#81-api-de-calcul-de-la-rc-pour-flotte-rc-flotte-request)
    2. [API de génération d'attestations flotte (qrcode-flotte-request)](#82-api-de-génération-dattestations-flotte-qrcode-flotte-request)
    3. [API d'incorporation à une flotte (incorpore-flotte-request)](#83-api-dincorporation-à-une-flotte-incorpore-flotte-request)
    4. [API de suppression de véhicule(s) à une flotte (subtract-flotte-request)](#84-api-de-suppression-de-véhicules-à-une-flotte-subtract-flotte-request)
9. [API de vérification statut attestation (check-qrcode-status)](#9-api-de-vérification-statut-attestation-check-qrcode-status)

---

## 1. Introduction

### 1.1 Objet du document

Ce document a pour but de décrire le mode opératoire d'intégration des API de l'Assurance Digitale AAS pour la branche
AUTOMOBILE.

### 1.2 Contenu et structure

La première section décrit les informations d'identification et d'authentification aux API de l'Assurance Digitale AAS.
La section suivante présente les différents API et de leur fonctionnement.

Pour chaque API, une URL de test est fourni afin de valider l'intégration. L'URL de Prod est à utiliser pour la mise en
production de vos services avec les identifiants de production qui vous seront fournis par Diotali.

---

## 2. Sécurité

### 2.1 Identification

La compagnie s'identifie avec les paramètres suivants : username, password et grant_type. Ces paramètres seront fournis
par Diotali à la création du compte de la compagnie. Avec ces trois paramètres la compagnie pourra utiliser les API
d'authentification pour créer une session.

### 2.2 Authentification

L'authentification est faite via le protocole OAuth2. Une fois connecté, la compagnie reçoit un access_token pour les
appels des autres API.

Un refresh_token est également fournis pour renouveler la session sans utiliser les accès username et password.

---

## 3. Description des APIs

### 3.1 Principe des échanges

* Les données échanges sont au format json.
* Un code d'erreur http signifie que la requête a échoué causé par un problème dans les données de la requête ou une
  exception. Un code de réussite (codes 200, 201, 202), est renvoyé en cas de réussite de la requête http et le
  paramètre operationStatus doit être en SUCCESS. Toute autre valeur que SUCCESS signifie que la requête a échoué.

### 3.2 URLs de bases

Les URLs de base sont les suivantes :

* Sandbox: https://apiaastest.diotali.com
* Production : https://apiaas.diotali.com

Toutes les API sont accessible en HTTPS afin de garantir la sécurité des données. Les échanges HTTP non chiffrées n'est
pas pris en charge.

Dans la suite le paramètre {URL} doit être remplacé par l'URL de la Sandbox pour les tests et l'URL de Production pour
l'intégration en production.

### 3.3 Méta Données

#### 3.3.1 Périodicité et durée

Tableau des valeurs attendus pour les paramètres périodicite et duree :

| Périodicité | Durée          |
|-------------|----------------|
| JOUR        | Entre 1 et 366 |
| MOIS        | Entre 1 et 12  |

#### 3.3.2 Genre

Tableau des valeurs attendus pour le paramètre genre :

| Catégorie | Genre               | Description                                                                                                                 |
|-----------|---------------------|-----------------------------------------------------------------------------------------------------------------------------|
| C1        | VP                  | Véhicule Particulier                                                                                                        |
| C2        | TPC                 | Véhicules utilitaires à carrosserie Tourisme (ex: Break…)                                                                   |
|           | TPC3T500            | Véhicules utilitaires autres carrosseries jusqu'à 3T 500                                                                    |
|           | TPC3T500P           | Véhicules utilitaires autres carrosseries au-delà de 3T 500                                                                 |
| C3        | TPM3T500            | Véhicules transports publics de marchandises jusqu'à 3T 500                                                                 |
|           | TPM3T500P           | Véhicules transports publics de marchandises au-delà de 3T 500                                                              |
| C4        | TPV8                | Véhicules utilisés pour transports de personnes à titre onéreux 8 places au plus                                            |
|           | TPV9                | Véhicules utilisés pour transports de personnes à titre onéreux 9 places et plus                                            |
| C5        | 2RCYC               | Véhicules motorisés à deux roues ou trois roues - Cyclomoteurs                                                              |
|           | 2RSCO               | Véhicules motorisés à deux roues ou trois roues - Scooters et vélomoteurs jusqu'à 125 cm3                                   |
|           | 2RMOT               | Véhicules motorisés à deux roues ou trois roues - Motocyclettes et scooters de plus de 125 cm3                              |
|           | 2RSID               | Véhicules motorisés à deux roues ou trois roues - Side-cars (toutes cylindrées)                                             |
| C6        | C6-WG-4R            | Garage Véhicule à 04 roues                                                                                                  |
|           | C6-WG-ATELIERAUTRE  | Garage Véhicule à 02 ou 03 roues pour atelier autre                                                                         |
| C7        | C7-AE-SCVTSDC_2R    | Side-cars Sans Double Commande                                                                                              |
|           | C7-AE-VTADC         | Véhicule de Tourisme Avec Double Commande                                                                                   |
|           | C7-AEVTADC_TPC      | Véhicule des catégories 2, 3 Avec Double Commande                                                                           |
|           | C7-AE-VTSDC         | Véhicule de Tourisme Sans Double Commande                                                                                   |
|           | C7-AEVTSDC_TPC      | Véhicule des catégories 2, 3 Sans Double Commande                                                                           |
| C8        | C8-VLSC             | Véhicule de Location Sans Chauffeur                                                                                         |
|           | C8-VLSC             | Véhicule de Location Sans Chauffeur                                                                                         |
|           | C8-VLSC_TPC         | Véhicule de Location Sans Chauffeur TPC                                                                                     |
|           | C8-VLSC_TPM3T500    | Véhicule de Location Sans Chauffeur TPM moins de 3T500                                                                      |
|           | C8-VLSC_TPM3T500P   | Véhicule de Location Sans Chauffeur TPM plus de 3T500                                                                       |
| C9        | C9-EMCEXCLUSION     | Engins Mobiles de Chantier avec exclusions des accidents                                                                    |
|           | C9-EMCEXTENSION     | Engins Mobiles de Chantier avec extension des accidents                                                                     |
| C10       | C10-VS-EMC          | Engins mobiles de chantiers - Tracteurs forestiers (avec ou sans chenilles) ne circulant pas sur la route                   |
|           | C10-VSEMC_TPC3T500  | Engins mobiles de chantiers - Tracteurs forestiers (avec ou sans chenilles) ne circulant pas sur la route de moins de 3T500 |
|           | C10-VSEMC_TPC3T500P | Engins mobiles de chantiers - Tracteurs forestiers (avec ou sans chenilles) ne circulant pas sur la route de plus de 3T500  |
|           | C10-VS-TAR          | Tracteurs agricoles et routiers (avec ou sans chenilles)                                                                    |
|           | C10-VSTAR_TPC3T500  | Tracteurs agricoles et routiers (avec ou sans chenilles) de moins de 3T500                                                  |
|           | C10-VSTAR_TPC3T500P | Tracteurs agricoles et routiers (avec ou sans chenilles) de plus de 3T500                                                   |
|           | C10-VS-VACFF        | Voitures d'ambulances, corbillards et fourgons funéraires                                                                   |
|           | C10-VS-VAME         | Véhicules automobiles à moteur électrique                                                                                   |
|           | C10-VS-VCP          | Véhicules des collectivités publiques                                                                                       |
|           | C10-VSVCP_TPC3T500  | Véhicules des collectivités publiques de moins de 3T500                                                                     |
|           | C10-VSVCP_TPC3T500P | Véhicules des collectivités publiques de plus de 3T500                                                                      |
| BUS_ECOLE | BE-VTA              | Véhicule de Transport dans des autocars                                                                                     |
|           | BE-VTCATP           | Transport dans des camions aménagés pour le transport de personnes                                                          |
| REMORQUE  | REMORQUE            | Remorque                                                                                                                    |

**Important** : Les véhicules du Pool TPV (C4) sont exclus pour le moment de la digitalisation des assurances
automobiles.

#### 3.3.3 Energie

Tableau des valeurs attendus pour le paramètre energie :

| energie |
|---------|
| ESSENCE |
| DIESEL  |

#### 3.3.4 Type Personne

Tableau des valeurs attendus pour le paramètre typePersonne, qui représente le type de souscripteur : personne morale ou
physique.

| typePersonne |
|--------------|
| PHYSIQUE     |
| MORALE       |

#### 3.3.5 Type d'usage catégorie 5 deux roues

Tableau des valeurs attendus pour le paramètre usage, qui représente l'usage qui est fait d'un véhicule 2 roues :
commerciale ou non commerciale.

| usage           |
|-----------------|
| commerciale     |
| non_commerciale |

---

## 4. Authentification

### 4.1 API de création d'une session

Cette API permet de s'authentifier et de récupérer un token de session qui sera utilisée ensuite pour l'ensemble des
requêtes.

**Méthode** : POST  
**URL**: {URL}/compagnie/session/token

**Request** :

| Nom        | Obligatoire | Type   | Description                                        |
|------------|-------------|--------|----------------------------------------------------|
| username   | X           | String | Nom d'utilisateur (Identifiant fourni par Diotali) |
| password   | X           | String | Mot de passe (fourni par Diotali)                  |
| grant_type | X           | String | Type d'accès                                       |

Exemple:

```json
{
  "username": "<string>",
  "password": "<string>",
  "grant_type": "<string>"
}
```

**Response** :

| Nom                | Type   | Description                             |
|--------------------|--------|-----------------------------------------|
| operationStatus    | String | Statut de la requête : SUCCESS ou ERROR |
| operationMessage   | String | Message lié au statut de la requête     |
| access_token       | String | Token d'accès                           |
| refresh_token      | String | Refresh Tocken                          |
| expires_in         | Int    | Durée du token en seconde               |
| refresh_expires_in | Int    | Durée du refresh token en seconde       |
| token_type         | String | Type de tocken. Valeur Bearer           |

Exemple:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Operation effectuée avec succès. ",
  "data": {
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTQUFSRzhMTDBQS1N6MVVFQkJzbnMzR3kiLCJyb2xlcyI6WyJQQVJURU5BSVJFIl0sImV4cCI6MTcwMjg5OTQ3OX0.tn9aEQ_J4wT3nx4XcuHsCMTWBDHjUJ7DR3Dy2PQt85Y",
    "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTQUFSIiwiZXhwIjoxNzAyOTAxMjc5fQ.mRPKD9r6DO3IriPhc3kMHXpdHSchBLL35QJWZmub3uk",
    "expires_in": 1800,
    "refresh_expires_in": 3600,
    "token_type": "Bearer"
  }
}
```

**Codes d'erreurs** :

| Code | Type    | Message                         |
|------|---------|---------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès |
| 4000 | ERROR   | Identité invalide               |
| 4001 | ERROR   | Mot de passe invalide           |
| 4003 | ERROR   | Accès non authorisé             |
| 4004 | ERROR   | Invalide grant-type             |
| 6000 | ERROR   | Erreur de traitement            |

---

## 5. APIs de Gestion des commandes

Cette section décrit les différents API permettant à une compagnie de gérer ses commandes de stock de « QR Code
virtuel » auprès de l'AAS.

En effet avant de pourvoir générer des attestations digitales la compagnie doit disposer sur le système centralisé de
l'AAS d'un stock de « QR Code virtuel ». Les paiements se font à l'avance par virement bancaire ou par chèque adressé à
l'AAS.

La commande de stock de « QR Code virtuel » se fait en trois étape :

1) Virement sur le compte de l'AAS ou envoie du chèque à l'AAS du montant correspondant au nombre de QR Code vierge
   souhaité
2) Initialisation de la commande par la compagnie via l'API en renseignant le nombre de QR
3) Validation de la commande, par la compagnie, en joignant le justificatif (copie virement ou chéque)
4) Validation de la commande par l'AAS et mise à jour automatique du stock de la compagnie

Le tableau suivant liste les différentes API de gestion des commandes et leur description :

| Nom           | Description                                                                                                                                           |
|---------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| init-command  | Permet d'initier une commande de QR en renseignant le nombre de QR Code virtuel désiré. En retour vous aurez le montant de la commande correspondante |
| valid-command | Permet de valider la commande en joignant le justificatif de la commande                                                                              |
| commandes     | Permet d'avoir la liste des commandes effectuées et leur statut                                                                                       |
| stock-qr      | Permet d'obtenir le stock actuel de QR Code virtuel                                                                                                   |
| cancel        | Permet d'annuler une commande à partir de la référence de la commande                                                                                 |

NB : Pour les compagnies qui n'ont pas de système de gestion de commande ou qui ne souhaite pas utiliser les API pour la
gestion des commandes, l'Association vous met à disposition un backoffice de gestion des commandes accessibles à partir
des URL suivantes :

Sandbox : https://compagnieaastest.diotali.com  
Prod : https://compagnieaas.diotali.com

### 5.1 API d'initialisation d'une commande (commande)

Cette API permet à la compagnie d'effectuer/initier une commande de « QR Code vierge » au prés de l'AAS.

**Méthode** : POST  
**URL** : {URL}/compagnie/commandes/init-commande

**Request Params** :

| Nom      | Obligatoire | Type | Description                                     |
|----------|-------------|------|-------------------------------------------------|
| nombreQR | X           | Long | Le nombre de « QR Code virtuel » de la commande |

Exemple init-commande :

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
params : {
 "nombreQr":100,
}
```

**Response init-commande** :

| Nom              | Type   | Description                                                                       |
|------------------|--------|-----------------------------------------------------------------------------------|
| operationStatus  | string | Statut de la requête : SUCCESS ou ERROR                                           |
| operationMessage | string | Message lié au statut de la requête                                               |
| data             | Object |                                                                                   |
| reference        | string | Référence de la commande à garder et à utiliser pour la validation de la commande |
| nombreQr         | long   | Nombre de QR Code commandé                                                        |
| montant          | long   | Montant de la commande correspondante                                             |
| date             | Date   | Date de la commande au format AAA-MM-JJ                                           |
| status           | string | Statut de la commande : INITIE, VALIDE, FACTURE                                   |

Exemple:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès."
  "data": {
    "reference": "1710236279013257",
    "nombreQr": 100,
    "montant": 12000,
    "dateCmd": "2024-03-12",
    "status": "INITIE"
  }
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                |
|------|---------|----------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès        |
| 4013 | ERROR   | Accès non autorisé pour cette commande |
| 6000 | ERROR   | Erreur de traitement                   |

### 5.2 API de validation d'une commande (valid-commande)

Cette API permet à la compagnie de compléter et valider une commande en statut initiée en joignant le justificatif de
paiement.

**Méthode** : POST  
**URL** : {URL}/compagnie/commandes/valid-commande

**Request Body** :

| Nom              | Obligatoire | Type   | Description                                                                                                                |
|------------------|-------------|--------|----------------------------------------------------------------------------------------------------------------------------|
| modePayment      | X           | String | Mode de Paiement utilisé pour le règlement de la commande. Deux valeurs possibles : VIREMENT ou CHEQUE                     |
| referencePayment | X           | String | Reference du justification de paiement : numéro du chèque ou référence du virement                                         |
| reference        | X           | String | Référence de la commande à valider. Il s'agit de la référence renvoyée par l'API initcommande                              |
| file             | X           | file   | Justificatif de paiement (copie bordereau de virement ou du chèque) au format .PDF ou image (.png, jpg) limité à 5 Mo maxi |

Exemple request :

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Body : {
 "modePayment": VIREMENT,
 "referencePayment":"0001234567",
 "reference":"1710236279013257",
 "file":<file_path>,
}
```

**Response valid-commande** :

| Nom              | Type   | Description                                      |
|------------------|--------|--------------------------------------------------|
| code             | long   | Code de retour de la requête                     |
| operationStatus  | string | Statut de la requête : SUCCESS ou ERROR          |
| operationMessage | string | Message lié au statut de la requête              |
| data             | Object |                                                  |
| reference        | string | Référence de la commande                         |
| nombreQr         | long   | Nombre de QR Code commandé                       |
| montant          | long   | Montant de la commande correspondante            |
| urlFile          | string | Lien vers le justificatif de paiement            |
| dateCmd          | Date   | Date de la commande au format AAA-MM-JJ hh:mm:ss |
| validateAt       | Date   |                                                  |
| status           | string | Statut de la commande : INITIE, VALIDE, FACTURE  |

Exemple de Response:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès."
  "data": {
    "reference": "1710236279013257",
    "nombreQr": 100,
    "montant": 12000,
    "urlFile": "<url>",
    "dateCmd": "2024-03-10 09:35:52",
    "status": "VALIDE"
  }
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                |
|------|---------|----------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès        |
| 4013 | ERROR   | Accès non autorisé pour cette commande |
| 6000 | ERROR   | Erreur de traitement                   |

### 5.3 API liste des commandes (commandes)

Cette API permet de retourner la liste des commandes effectuées par la compagnie à partir de leurs statuts.

**Méthode** : GET  
**URL**: {URL}/compagnie/commandes/commandes

**Request Params**:

| Nom    | Type   | Description                                                                   |
|--------|--------|-------------------------------------------------------------------------------|
| status | String | Statut des commandes à récupérer selon leur statuts : INITIE, VALIDE, FACTURE |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Params:
commandes?status=FACTURE
```

**Response** :

| Nom              | Type       | Description                             |
|------------------|------------|-----------------------------------------|
| operationStatus  | String     | Statut de la requête : SUCCESS ou ERROR |
| operationMessage | String     | Message lié au statut de la requête     |
| items            |            | Liste des commandes                     |
| Id               | Long       | ID de la commande                       |
| Reference        | String     | Reference de la commande                |
| modePayment      | String     | Mode de paiement de la commande         |
| referencePayment | String     | Reference justificatif de paiement      |
| nombreQR         | Long       | Nombre de QR code commandé              |
| montant          | BigDecimal | Montant de la commande                  |
| urlFile          | String     | Chemin vers le justificatif de paiement |
| dateCmd          | String     | Date de création de la commande         |
| validateAt       | String     | Date de validation de la commande       |
| doneAt           | String     | Date de validation de la commande       |
| status           | String     | Statut de la commande                   |

Exemple de response:

```json
{
  "operationStatus": "<string>",
  "operationMessage": "<string>",
  "items": [
    {
      "id": 102,
      "reference": "1702984695278102",
      "modePayment": "<string>",
      "referencePayment": "<string>",
      "nombreQr": "<long>",
      "montant": "<BigDecimal>",
      "urlFile": "<string>",
      "dateCmd": "<string>",
      "dateAt": "<string>",
      "status": "<string>"
    }
  ]
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                |
|------|---------|----------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès        |
| 4013 | ERROR   | Accès non autorisé pour cette commande |
| 6000 | ERROR   | Erreur de traitement                   |

## 5.4 API de vérification solde Qr Code (stock-qr)

Cet API permet de récupérer le solde (stock) courant de QR Code virtuel disponible sur le système. Ce solde est mis à
jour à chaque commande de QR validée (incrémenté) et à chaque génération/production d'une attestation (décrémenté).

**Méthode** : GET  
**URL**: {URL}/compagnie/service/stock-qr

**Request Body**:  
Aucun

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Body : { }
```

**Response**:

| Nom              | Type    | Description                             |
|------------------|---------|-----------------------------------------|
| operationStatus  | String  | Statut de la requête : SUCCESS ou ERROR |
| operationMessage | String  | Message lié au statut de la requête     |
| data             | Integer | Solde QrCode virtuel                    |

Exemple de response:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "data": 80
}
```

**Codes d'erreurs** :

| Code | Type    | Message                         |
|------|---------|---------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès |
| 6000 | ERROR   | Erreur de traitement            |

## 5.5 API d'annulation d'une commande (cancel)

Cette API permet de retourner la liste des commandes effectuées par la compagnie à partir de leurs statuts.

**Méthode** : GET  
**URL**: {URL}/compagnie/commandes/commandes/cancel

**Path Params**:

| Nom       | Type   | Description                        |
|-----------|--------|------------------------------------|
| reference | String | Référence de la commande à annuler |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Params:
Reference = 1711045343315322
```

**Response** :

| Nom              | Type   | Description                             |
|------------------|--------|-----------------------------------------|
| operationStatus  | String | Statut de la requête : SUCCESS ou ERROR |
| operationMessage | String | Message lié au statut de la requête     |
| data             |        | Liste des commandes                     |

Exemple de response:

```json
{
  "operationStatus": "<string>",
  "operationMessage": "<string>",
  "items": [
    {
      "id": 102,
      "reference": "1702984695278102",
      "modePayment": "<string>",
      "referencePayment": "<string>",
      "nombreQr": "<long>",
      "montant": "<BigDecimal>",
      "urlFile": "<string>",
      "dateCmd": "<string>",
      "dateAt": "<string>",
      "status": "<string>"
    }
  ]
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                |
|------|---------|----------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès        |
| 4013 | ERROR   | Accès non autorisé pour cette commande |
| 6000 | ERROR   | Erreur de traitement                   |

## 6. APIs de Gestion des attestations Mono

Cette section décrit les différents API pour la gestion/production des attestations digitales automobiles Mono.

Le tableau suivant liste les différentes API de gestion des attestations Mono et leur description :

| Nom                     | Description                                                                                                  |
|-------------------------|--------------------------------------------------------------------------------------------------------------|
| rc-request              | Permet d'obtenir le montant de la prime RC selon les caractéristiques du véhicule et de la durée             |
| qrcode-request          | Permet de générer un QR Code et une attestation automobile digitale mono                                     |
| qrcode-cancel           | Permet d'annuler/ invalider une attestation digitale                                                         |
| remorque-rc-request     | Permet d'obtenir le montant de la prime RC selon les caractéristiques de la remorque et de la voiture morice |
| remorque-qrcode-request | Permet de générer un QR Code et une attestation automobile digitale pour une remorque                        |

### 6.1 API de calcul de la RC d'une attestation Mono (rc-request)

Cette API permet d'obtenir le montant de la prime RC selon les caractéristiques du véhicule.

**Méthode** : GET  
**URL**: {URL}/compagnie/service/rc-request?puissanceFiscale=X&duree=Y&genre=Z

**Request Params**:

| Nom              | Obligatoire | Type   | Description                                                                                   |
|------------------|-------------|--------|-----------------------------------------------------------------------------------------------|
| puissanceFiscale | X           | String | Puissance fiscale du véhicule                                                                 |
| duree            | X           | String | Durée de la souscription en mois ou en jour                                                   |
| periodicite      | X           | String | Type de période MOIS ou JOUR                                                                  |
| genre            | X           | String | Genre du véhicule (VP, TPC, …) permettant de calculer la prime RC. Voir le tableau des Genres |
| energie          | X           | String | Type d'énergie (ESSENCE ou DIESEL)                                                            |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Path variables:
{
 " puissanceFiscale ":14,
 "duree": 6,
 "periodicite": MOIS,
 "genre":"VP",
 "energie":"ESSENCE",
}
```

**Response** :

| Nom              | Type       | Description                             |
|------------------|------------|-----------------------------------------|
| code             | BigDecimal | Code de retour de la requête            |
| operationStatus  | String     | Statut de la requête : SUCCESS ou ERROR |
| operationMessage | String     | Message lié au statut de la requête     |
| data             | BigDecimal | Prime Net RC                            |

Exemple de Response:

```json
{
  "code": 2000
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "data": 49915
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                |
|------|---------|----------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès        |
| 4007 | ERROR   | Merci de renseigner un genre valide    |
| 4008 | ERROR   | Merci de renseigner une énergie valide |

### 6.2 API de génération d'une attestation Mono (qrcode-request)

Cet API permet de générer un QR Code et une attestation automobile digitale mono. Le QR Code généré sera déduit du stock
de « QR code virtuel » de la compagnie.

**Méthode** : POST  
**URL**: {URL}/compagnie/service/qrcode-request

**Request Body** :

| Nom                  | Obligatoire | Type       | Description                                                                     |
|----------------------|-------------|------------|---------------------------------------------------------------------------------|
| responsabiliteCivile | X           | BigDecimal | Montant Responsabilité civile                                                   |
| dateEffet            | X           | String     | Date effet de l'attestation automobile Format : AAAA-MM-JJ                      |
| duree                | X           | String     | Durée souscription en mois ou jour. Voir tableau des valeurs attendues          |
| periodicite          | X           | String     | Type de période MOIS ou JOUR. Voir tableau des valeurs attendues                |
| police               | X           | String     | Numéro de la police d'assurance.                                                |
| typePersonne         | X           | string     | Type du souscripteur : PHYSIQUE ou MORALE                                       |
| souscripteur         | X           | Objet      | Informations sur le souscripteurs du contrat                                    |
| nom                  | X           | String     | Nom du souscripteur                                                             |
| prenom               | X           | String     | Prénom du souscripteur                                                          |
| cellulaire           |             | String     | Cellulaire du souscripteur                                                      |
| email                |             | String     | Email du souscripteur                                                           |
| assure               | X           | Objet      | Informations sur le bénéficiaire du contrat                                     |
| nom                  | X           | String     | Nom du bénéficiaire                                                             |
| prenom               | X           | String     | Prénom du bénéficiaire                                                          |
| cellulaire           |             | String     | Cellulaire du bénéficiaire                                                      |
| email                |             | String     | Email du bénéficiaire                                                           |
| vehicule             |             | Objet      | Informations technique du véhicule                                              |
| puissanceFiscale     | X           | Int        | Puissance fiscale du véhicule                                                   |
| dateMiseCirculation  | X           | String     | Date de mise en circulation du véhicule Format : AAAA-MM-JJ                     |
| nombrePlace          | X           | Int        | Nombre de place du véhicule                                                     |
| valeurNeuve          |             | BigDecimal | Valeur vénal du véhicule                                                        |
| valeurActuelle       |             | BigDecimal | Valeur actuelle du véhicule                                                     |
| immatriculation      | X           | String     | Plaque d'Immatriculation du véhicule. Optionnelle si voiture neuve              |
| energie              | X           | String     | Energie (ESSENCE ou DIESEL)                                                     |
| genre                | X           | String     | Genre du véhicule (VP, TPC,…). Voir tableau des valeurs attendues pour le genre |
| modele               | X           | String     | Modèle du véhicule                                                              |
| marque               | X           | String     | Marque du véhicule                                                              |
| chassis              | X           | String     | Numéro de châssis du véhicule. Obligatoire si pas de plaque d'immatriculation.  |
| referenceTrxPartner  | X           | String     | Référence interne de la transaction au niveau de la compagnie                   |

Exemple de request :

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Body:
{
 "responsabiliteCivile":49915,
 "dateEffet":"2024-03-01",
 "police":"123456789",
 "duree":6,
 "periodicite": "MOIS",
 "souscripteur":{
   "nom":"FALL",
   "prenom":"MODOU",
   "cellulaire":"771234578",
   "email":"modou.fall@test.com"
 },
 "assure":{
   "nom":"FALL",
   "prenom":"MODOU",
   "cellulaire":"771234578",
   "email":"modou.fall@test.com"
 },
 "vehicule":{
   "puissanceFiscale":14,
   "dateMiseCirculation":"1999-11-08",
   "nombrePlace":5,
   "valeurNeuve":4500000.00,
   "valeurActuelle":3500000.00,
   "immatriculation":"DK-0000-KL",
   "energie":"ESSENCE",
   "genre":"VP",
   "modele":"LOGAN",
   "marque":"RENAULD",
   "chassis":"1234567890OIUYTREZQSDF"
 },
 "referenceTrxPartner":"12345678909876543"
}
```

**Response** :

| Nom               | Type   | Description                                                                                                                        |
|-------------------|--------|------------------------------------------------------------------------------------------------------------------------------------|
| code              | Int    | Code retour                                                                                                                        |
| status            | String | Statut de la requête : SUCCESS ou ERROR                                                                                            |
| message           | String | Message lié au statut de la requête                                                                                                |
| data              | Objet  |                                                                                                                                    |
| referenceExterne  | String | Référence interne de la transaction au niveau de la compagnie                                                                      |
| attestationNumber | String | Numéro de l'attestation                                                                                                            |
| linkAttestation   | String | Lien URL vers l'attestation digitale                                                                                               |
| secureKey         | String | A ignorer                                                                                                                          |
| dateExpiration    | String | Date d'expiration de l'attestation calculée automatiquement à partir de la date d'effet et de la durée Format: AAAA-MM-JJ-HH-MM-SS |
| linkCarteBrune    | String | Lien URL vers l'attestation carte brune                                                                                            |

Exemple de response:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "data": {
    "referenceExterne": "PRO12244",
    "linkAttestation": "https://aas.diotali.com/#/attestation/SN00JXXXXXX",
    "attestationNumber": " SN00JXXXXXX ",
    "secureKey": "xxxxxxxxxxxxxx",
    "dateExpiration": "2024-08-26T23:59:59 "
    " linkCarteBrune ": "https://aas.diotali.com/#/attestation/SN00JXXXXXX"
  }
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                                                 |
|------|---------|-------------------------------------------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès                                         |
| 4006 | ERROR   | Merci de renseigner une Responsabilité civile valide                    |
| 4007 | ERROR   | Merci de renseigner un genre valide                                     |
| 4008 | ERROR   | Merci de renseigner une énergie valide                                  |
| 4010 | ERROR   | Votre stock de Qr est épuisé. Merci de passer votre commande de QR      |
| 5006 | ERROR   | La référence n'est pas unique. Merci de renseigner une référence valide |

### 6.3 API d'invalidation d'une attestation Mono (qrcode-cancel)

Cette API permet à une compagnie d'invalider une attestation digitale Mono. L'invalidation d'une attestation Mono peut
être faite selon les cas suivants :

- Annulation : Erreur ou annulation d'une attestation en cours (ex : avenant, attestation provisoire avant mutation)
- Résiliation : Résiliation d'une attestation d'assurance encours (ex : vente, vol)
- Suspension : Suspension d'une attestation

**Méthode** : POST  
**URL**: {URL}/compagnie/service/qrcode-cancel?referenceTrxPartner=X&methode=Y&motif=Z

**Query Params**:

| Nom                 | Obligatoire | Type   | Description                                                                                                                                                          |
|---------------------|-------------|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| referenceTrxPartner | X           | String | Référence de la transaction fournie par la compagnie lors de la génération de l'attestation                                                                          |
| methode             | X           | String | Il s'agit d'une information technique permettant de coder le motif de l'invalidation de l'attestation. Peut prendre trois valeurs : - ANNULER - RESILIER - SUSPENDRE |
| motif               |             | String | Un champ commentaire permettant de renseigner les motifs de l'invalidation d'une attestation                                                                         |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Query Params:
{
 " referenceTrxPartner ":"123456789",
 "methode": "RESILIER",
 "motif":"Résiliation suite vente", 
}
```

**Response** :

| Nom              | Type   | Description                             |
|------------------|--------|-----------------------------------------|
| operationStatus  | String | Statut de la requête : SUCCESS ou ERROR |
| operationMessage | String | Message lié au statut de la requête     |

Exemple:

```json
{
  "operationStatus": "<string>",
  "operationMessage": "<string>"
}
```

### 6.4 API de calcul de la RC d'une attestation Remorque (remorque-rc-request)

Cette API permet d'obtenir le montant de la prime RC d'une remorque selon la catégorie de la voiture motrice et de la
durée.

**Méthode** : GET  
**URL**: {URL}/compagnie/service/remorque-rc-request?puissanceFiscale=X&duree=Y&genre=Z

**Body**:

| Nom               | Obligatoire | Type   | Description                                                    |
|-------------------|-------------|--------|----------------------------------------------------------------|
| duree             | X           | String | Durée de la souscription en mois ou en jour                    |
| periodicite       | X           | String | Type de période MOIS ou JOUR                                   |
| referenceVehicule | X           | String | Référence de la voiture motrice de rattachement de la remorque |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Body:
{
 "duree": 12,
 "periodicite": MOIS,
 "referenceVehicule":"123456789",
}
```

**Response** :

| Nom              | Type       | Description                             |
|------------------|------------|-----------------------------------------|
| code             | BigDecimal | Code de retour de la requête            |
| operationStatus  | String     | Statut de la requête : SUCCESS ou ERROR |
| operationMessage | String     | Message lié au statut de la requête     |
| data             | BigDecimal | Prime Net RC                            |

Exemple de Response:

```json
{
  "code": 2000
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "data": 45970
}
```

**Codes d'erreurs** :

| Code | Type    | Message                              |
|------|---------|--------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès      |
| 4007 | ERROR   | Merci de renseigner une durée valide |
| 4008 | ERROR   | Référence de véhicule inconnue       |

### 6.5 API de génération d'une attestation Remorque (remorque-qrcode-request)

Cet API permet de générer un QR Code et une attestation automobile digitale pour une remorque. Le QR Code généré sera
déduit du stock de « QR code virtuel » de la compagnie.

La voiture motrice doit au préalable disposer d'une assurance digitale valable et souscrite dans la même compagnie.

**Méthode** : POST  
**URL**: {URL}/compagnie/service/remorque-qrcode-request

**Request Body** :

| Nom                  | Obligatoire | Type       | Description                                                            |
|----------------------|-------------|------------|------------------------------------------------------------------------|
| responsabiliteCivile | X           | BigDecimal | Montant Responsabilité civile calculé pour la remorque                 |
| dateEffet            | X           | String     | Date effet de l'attestation remorque Format : AAAA-MM-JJ               |
| dateExpiration       | X           | String     | Date expiration de l'attestation remorque Format : AAAA-MM-JJ          |
| duree                | X           | String     | Durée souscription en mois ou jour. Voir tableau des valeurs attendues |
| periodicite          | X           | String     | Type de période MOIS ou JOUR. Voir tableau des valeurs attendues       |
| police               | X           | String     | Numéro de la police d'assurance du contrat.                            |
| referenceVehicule    | X           | String     | Référence du véhicule auquel la remorque sera attelée.                 |
| referenceTrxPartner  | X           | String     | Référence interne de la transaction au niveau de la compagnie          |
| typePersonne         | X           | string     | Type du souscripteur : PHYSIQUE ou MORALE                              |
| immatriculation      | X           | String     | Plaque d'Immatriculation de la remorque                                |
| modele               | X           | String     | Modèle du de la remorque                                               |
| marque               | X           | String     | Marque de la remorque                                                  |
| souscripteur         | X           | Objet      | Informations sur le souscripteurs du contrat                           |
| nom                  | X           | String     | Nom du souscripteur                                                    |
| prenom               | X           | String     | Prénom du souscripteur                                                 |
| cellulaire           |             | String     | Cellulaire du souscripteur                                             |
| email                |             | String     | Email du souscripteur                                                  |
| assure               | X           | Objet      | Informations sur le bénéficiaire du contrat                            |
| nom                  | X           | String     | Nom du bénéficiaire                                                    |
| prenom               | X           | String     | Prénom du bénéficiaire                                                 |
| cellulaire           |             | String     | Cellulaire du bénéficiaire                                             |
| email                |             | String     | Email du bénéficiaire                                                  |
| energie              | X           | String     | Energie (ESSENCE ou DIESEL)                                            |

Exemple de request :

```json
{
  "dateEffet": "2024-07-11",
  "dateExpiration": "2025-01-10",
  "duree": 12,
  "periodicite": "MOIS",
  "police": "poltestremorque01",
  "referenceTrxPartner": "refRemorque02",
  "referenceVehicule": "reftestRemorque01",
  "responsabiliteCivile": 45970,
  "immatriculation": "KL2365HA",
  "marque": "Remoque",
  "modele": "Remoque",
  "typePersonne": "PHYSIQUE",
  "assure": {
    "cellulaire": "770000000",
    "email": "test@test.com",
    "nom": "Fall",
    "prenom": "Modou"
  },
  "souscripteur": {
    "cellulaire": "770000000",
    "email": "test@test.com",
    "nom": "Fall",
    "prenom": "Modou"
  }
}
```

**Response** :

| Nom               | Type   | Description                                                                                                                         |
|-------------------|--------|-------------------------------------------------------------------------------------------------------------------------------------|
| code              | Int    | Code retour                                                                                                                         |
| status            | String | Statut de la requête : SUCCESS ou ERROR                                                                                             |
| message           | String | Message lié au statut de la requête                                                                                                 |
| data              | Objet  |                                                                                                                                     |
| referenceExterne  | String | Référence interne de la transaction au niveau de la compagnie                                                                       |
| attestationNumber | String | Numéro de l'attestation                                                                                                             |
| linkAttestation   | String | Lien URL vers l'attestation digitale                                                                                                |
| secureKey         | String | A ignorer                                                                                                                           |
| dateExpiration    | String | Date d'expiration de l'attestation calculée automatiquement à partir de la date d'effet et de la durée Format : AAAA-MM-JJ-HH-MM-SS |
| linkCarteBrune    | String | Lien URL vers l'attestation carte brune                                                                                             |

Exemple de response:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "data": {
    "referenceExterne": "refRemorque02",
    "linkAttestation": "https://aas.diotali.com/#/attestation/SN00JXXXXXX",
    "attestationNumber": " SN00JXXXXXX ",
    "secureKey": "xxxxxxxxxxxxxx",
    "dateExpiration": "2024-08-26T23:59:59 "
    " linkCarteBrune ": "https://aas.diotali.com/#/attestation/SN00JXXXXXX"
  }
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                                                 |
|------|---------|-------------------------------------------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès                                         |
| 4006 | ERROR   | Merci de renseigner une Responsabilité civile valide                    |
| 4007 | ERROR   | Merci de renseigner un genre valide                                     |
| 4008 | ERROR   | Merci de renseigner une énergie valide                                  |
| 4010 | ERROR   | Votre stock de Qr est épuisé. Merci de passer votre commande de QR      |
| 5006 | ERROR   | La référence n'est pas unique. Merci de renseigner une référence valide |

## 7. APIs de Gestion des attestations deux roues (C5)

Cette section décrit les différents API pour la gestion/production des attestations digitales pour les véhicules à deux
ou trois roues Catégorie 5.

Le tableau suivant liste les différentes API de gestion des attestations deux roues et leur description :

| Nom                 | Description                                                                                                  |
|---------------------|--------------------------------------------------------------------------------------------------------------|
| moto-rc-request     | Permet d'obtenir le montant de la prime RC selon les caractéristiques du véhicule, de la durée et de l'usage |
| moto-qrcode-request | Permet de générer un QR Code et une attestation moto digitale                                                |
| qrcode-cancel       | Permet d'invalider une attestation deux roues (voir 6.3 API d'invalidation d'une attestation Mono)           |

### 7.1 API de calcul de la RC d'une attestation deux roues (moto-rc-request)

Cette permet d'obtenir le montant de la prime RC selon les caractéristiques du véhicule.

**Méthode** : GET  
**URL**: {URL}/compagnie/service/moto-rc-request

**Request Body**:

| Nom         | Obligatoire | Type    | Description                                                                                                   |
|-------------|-------------|---------|---------------------------------------------------------------------------------------------------------------|
| cylindre    | X           | Integer | Cylindre du véhicule. Mettre 0 si pas applicable pour la sous-catégorie                                       |
| duree       | X           | String  | Durée de la souscription en mois ou en jour                                                                   |
| periodicite | X           | String  | Type de période MOIS ou JOUR                                                                                  |
| genre       | X           | String  | Genre du véhicule (2RCYC, 2RSCO, 2RMOT, 2RSID) permettant de calculer la prime RC. Voir le tableau des Genres |
| energie     | X           | String  | Type d'énergie (ESSENCE ou DIESEL)                                                                            |
| usage       | X           | String  | Type d'usage du véhicule (COMMERCIAL ou NON_COMMERCIAL)                                                       |
| nombrePlace | X           | String  | Nombre de places du véhicule                                                                                  |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Body:
{
 "cylindre ":126,
 "duree": 6,
 "periodicite": "MOIS",
 "genre":" 2RCYC",
 "energie":"ESSENCE",
 "usage" : "NON_COMMERCIAL",
 "nombrePlace":2
}
```

**Response** :

| Nom              | Type       | Description                             |
|------------------|------------|-----------------------------------------|
| code             | BigDecimal | Code de retour de la requête            |
| operationStatus  | String     | Statut de la requête : SUCCESS ou ERROR |
| operationMessage | String     | Message lié au statut de la requête     |
| data             | BigDecimal | Prime Net RC                            |

Exemple de Response:

```json
{
  "code": 2000
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "data": 14273
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                |
|------|---------|----------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès        |
| 4007 | ERROR   | Merci de renseigner un genre valide    |
| 4008 | ERROR   | Merci de renseigner une énergie valide |

### 7.2 API de génération d'une attestation deux roues (moto-qrcode-request)

Cet API permet de générer un QR Code et une attestation moto digitale. Le QR Code généré sera déduit du stock de « QR
code virtuel » de la compagnie.

**Méthode** : POST  
**URL**: {URL}/compagnie/service/moto-qrcode-request

**Request Body** :

| Nom                  | Obligatoire | Type       | Description                                                                                      |
|----------------------|-------------|------------|--------------------------------------------------------------------------------------------------|
| responsabiliteCivile | X           | BigDecimal | Montant Responsabilité civile                                                                    |
| dateEffet            | X           | String     | Date effet de l'attestation moto Format : AAAA-MM-JJ                                             |
| dateExpiration       | X           | String     | Date de fin de l'attestation moto Format : AAAA-MM-JJ                                            |
| duree                | X           | String     | Durée souscription en mois ou jour. Voir tableau des valeurs attendues                           |
| periodicite          | X           | String     | Type de période MOIS ou JOUR. Voir tableau des valeurs attendues                                 |
| police               | X           | String     | Numéro de la police d'assurance.                                                                 |
| typePersonne         | X           | string     | Type du souscripteur : PHYSIQUE ou MORALE                                                        |
| referenceTrxPartner  | X           | String     | Référence interne de la transaction au niveau de la compagnie                                    |
| souscripteur         | X           | Objet      | Informations sur le souscripteurs du contrat                                                     |
| nom                  | X           | String     | Nom du souscripteur                                                                              |
| prenom               | X           | String     | Prénom du souscripteur                                                                           |
| cellulaire           |             | String     | Cellulaire du souscripteur                                                                       |
| email                |             | String     | Email du souscripteur                                                                            |
| assure               | X           | Objet      | Informations sur le bénéficiaire du contrat                                                      |
| nom                  | X           | String     | Nom du bénéficiaire                                                                              |
| prenom               | X           | String     | Prénom du bénéficiaire                                                                           |
| cellulaire           |             | String     | Cellulaire du bénéficiaire                                                                       |
| email                |             | String     | Email du bénéficiaire                                                                            |
| vehicule             |             | Objet      | Informations technique de la moto                                                                |
| cyclindre            | X           | Int        | Cylindre de la moto                                                                              |
| dateMiseCirculation  | X           | String     | Date de mise en circulation du véhicule Format : AAAA-MM-JJ                                      |
| nombrePlace          | X           | Int        | Nombre de place de la moto                                                                       |
| immatriculation      | X           | String     | Plaque d'Immatriculation de la moto.                                                             |
| energie              | X           | String     | Energie (ESSENCE ou DIESEL)                                                                      |
| genre                | X           | String     | Genre du véhicule (2RCYC, 2RSCO, 2RMOT, 2RSID). Voir tableau des valeurs attendues pour le genre |
| modele               | X           | String     | Modèle de la moto                                                                                |
| marque               | X           | String     | Marque de la moto                                                                                |

Exemple de request :

```json
{
  "dateEffet": "2024-07-11",
  "dateExpiration": "2025-01-10",
  "duree": 6,
  "periodicite": "MOIS",
  "police": "poltest123456",
  "referenceTrxPartner": "reftest123456",
  "responsabiliteCivile": 14273,
  "typePersonne": "PHYSIQUE",
  "souscripteur": {
    "nom": "FALL",
    "prenom": "MODOU",
    "cellulaire": "771234578",
    "email": "modou.fall@test.com"
  },
  "assure": {
    "nom": "FALL",
    "prenom": "MODOU",
    "cellulaire": "771234578",
    "email": "modou.fall@test.com"
  },
  "vehicule": {
    "cylindre": 126,
    "dateMiseCirculation": "2022-11-08",
    "nombrePlace": 2,
    "immatriculation": "DK-0000-MT",
    "energie": "ESSENCE",
    "genre": "2RCYC",
    "modele": "Vespa",
    "marque": "MTN",
    "usage": "NON_COMMERCIALE"
  },
  "referenceTrxPartner": "reftest123456"
}
```

**Response** :

| Nom               | Type   | Description                                                              |
|-------------------|--------|--------------------------------------------------------------------------|
| code              | Int    | Code retour                                                              |
| status            | String | Statut de la requête : SUCCESS ou ERROR                                  |
| message           | String | Message lié au statut de la requête                                      |
| data              | Objet  |                                                                          |
| referenceExterne  | String | Référence interne de la transaction au niveau de la compagnie            |
| attestationNumber | String | Numéro de l'attestation                                                  |
| linkAttestation   | String | Lien URL vers l'attestation digitale                                     |
| secureKey         | String | A ignorer                                                                |
| dateExpiration    | String | Date d'expiration de l'attestation fournit. Format : AAAA-MM-JJ-HH-MM-SS |
| linkCarteBrune    | String | Lien URL vers l'attestation carte brune                                  |

Exemple de response:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "data": {
    "referenceExterne": "reftest123456",
    "linkAttestation": "https://aas.diotali.com/#/attestation/SN00JXXXXXX",
    "attestationNumber": " SN00JXXXXXX ",
    "secureKey": "xxxxxxxxxxxxxx",
    "dateExpiration": "2025-01-10T23:59:59"
    " linkCarteBrune ": "https://aas.diotali.com/#/attestation/SN00JXXXXXX"
  }
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                                                 |
|------|---------|-------------------------------------------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès                                         |
| 4006 | ERROR   | Merci de renseigner une Responsabilité civile valide                    |
| 4007 | ERROR   | Merci de renseigner un genre valide                                     |
| 4008 | ERROR   | Merci de renseigner une énergie valide                                  |
| 4010 | ERROR   | Votre stock de Qr est épuisé. Merci de passer votre commande de QR      |
| 5006 | ERROR   | La référence n'est pas unique. Merci de renseigner une référence valide |

---

## 8. APIs de Gestion des attestations Flotte

Cette section décrit les différents API pour la gestion/production des attestations digitales automobiles Flotte.

Le tableau suivant liste les différentes API de gestion des attestations Flotte et leur description :

| Nom                      | Description                                                                                                         |
|--------------------------|---------------------------------------------------------------------------------------------------------------------|
| rc-flotte-request        | Permet d'obtenir le montant de la prime RC selon les caractéristiques du véhicule et de la durée                    |
| qrcode-flotte-request    | Permet de générer un lot de QR Code et d'attestations automobiles digitales flotte                                  |
| incorpore-flotte-request | Permet d'incorporer une ou plusieurs véhicule(s) à une flotte existante et de générer les attestations équivalentes |
| subtract-flotte-request  | Permet de supprimer un ou plusieurs véhicules d'une flotte existante                                                |

### 8.1 API de calcul de la RC pour flotte (rc-flotte-request)

Cette permet d'obtenir le montant de la prime RC selon le nombre de véhicule de la flotte et les caractéristiques de
chaque véhicule.

**Méthode** : GET  
**URL**: {URL}/compagnie/service/rc-flotte-request

**Request Body**:

| Nom              | Obligatoire | Type    | Description                                                                                   |
|------------------|-------------|---------|-----------------------------------------------------------------------------------------------|
| referenceFlotte  | X           | String  | Reference de la nouvelle flotte. Ou référence d'une flotte existante si insertion             |
| periodicite      | X           | String  | Type de période MOIS ou JOUR                                                                  |
| duree            | X           | Integer | Durée du contrat flotte en mois ou en jour                                                    |
| requests         |             | Object  | Liste des caractéristiques de chaque véhicule de la flotte                                    |
| puissanceFiscale | X           | String  | Puissance fiscale du véhicule                                                                 |
| genre            | X           | String  | Genre du véhicule (VP, TPC, …) permettant de calculer la prime RC. Voir le tableau des Genres |
| energie          | X           | String  | Type d'énergie (ESSENCE ou DIESEL)                                                            |
| requestId        | X           | String  | Référence interne de la transaction au niveau de la compagnie                                 |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
{
 "referenceFlotte": "123f1",
 "periodicite": "MOIS",
 "duree": 5 ,
 "requests": [
   {
     "puissanceFiscale": 10,
     "genre": "VP",
     "energie": "ESSENCE",
     "requestId": "123f11"
   },
   {
     "puissanceFiscale": 10,
     "genre": "VP",
     "energie": "ESSENCE",
     "requestId": "123f12"
   }
 ]
}
```

**Important** :  
La RC appliquée sur la flotte est calculée selon le barème suivant :

| NOMBRE DE VEHICULES | RABAIS DE PRIME |
|---------------------|-----------------|
| 10 à 20             | 10%             |
| 21 à 40             | 15%             |
| 41 à 60             | 20%             |
| Plus de 60          | 25%             |

**Response** :

| Nom                  | Type       | Description                                                   |
|----------------------|------------|---------------------------------------------------------------|
| code                 | BigDecimal | Code de retour de la requête                                  |
| operationStatus      | String     | Statut de la requête : SUCCESS ou ERROR                       |
| operationMessage     | String     | Message lié au statut de la requête                           |
| items                |            |                                                               |
| requestId            | String     | Référence interne de la transaction au niveau de la compagnie |
| responsabiliteCivile | number     | Prime RC du véhicule                                          |

Exemple de Response:

```json
{
  "code": 2000,
  "status": "SUCCESS",
  "message": "Opération effectuée avec succès.",
  "items": [
    {
      "requestId": "123f11",
      "responsabiliteCivile": 34733
    },
    {
      "requestId": "123f12",
      "responsabiliteCivile": 34733
    }
  ]
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                |
|------|---------|----------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès        |
| 4007 | ERROR   | Merci de renseigner un genre valide    |
| 4008 | ERROR   | Merci de renseigner une énergie valide |

### 8.2 API de génération d'attestations flotte (qrcode-flotte-request)

Cet API permet de générer un lot de QR Code et d'attestations automobiles digitales pour une flotte. Il faudrait fournir
la liste contenant les informations des véhicules de la flotte.

L'ensemble des véhicules de la flotte doivent être inclus dans la liste.

Les QR Code générés seront déduits du stock de « QR code virtuel » de la compagnie.

**Méthode** : POST  
**URL**: {URL}/compagnie/service/qrcode-flotte-request

**Request** :

| Nom                  | Obligatoire | Type       | Description                                                                                                           |
|----------------------|-------------|------------|-----------------------------------------------------------------------------------------------------------------------|
| referenceFlotte      | X           | String     | Reference de la flotte au niveau de la compagnie. Sera utilisée pour les lors d'une demande d'insertion               |
| dateEffet            | X           | String     | Date effet des attestations automobiles de la flotte Format : AAAA-MM-JJ                                              |
| duree                | X           | String     | Durée souscription en mois ou jour. Voir tableau des valeurs attendues                                                |
| periodicite          | X           | String     | Type de durée MOIS ou JOUR. Voir tableau des valeurs attendues                                                        |
| typePersonne         | X           | String     | Type du souscripteur : PHYSIQUE ou MORALE                                                                             |
| police               | X           | String     | Numéro de la police                                                                                                   |
| souscripteur         | X           | Objet      | Informations sur le souscripteur du contrat                                                                           |
| nom                  | X           | String     | Nom du souscripteur                                                                                                   |
| prenom               | X           | String     | Prénom du souscripteur                                                                                                |
| cellulaire           |             | String     | Téléphone du souscripteur                                                                                             |
| email                |             | String     | Email du souscripteur                                                                                                 |
| items                | X           | Objet      | Liste des informations des véhicules de la flotte                                                                     |
| referenceTrxPartner  | X           | String     | Référence interne de la transaction au niveau de la compagnie. Sera utilisée pour les demandes d'annulation (Avenant) |
| responsabiliteCivile | X           | BigDecimal | Montant Responsabilité civile du véhicule                                                                             |
| assure               | X           | Objet      | Informations sur le bénéficiaire du contrat                                                                           |
| nom                  | X           | String     | Nom du bénéficiaire                                                                                                   |
| prenom               | X           | String     | Prénom du bénéficiaire                                                                                                |
| cellulaire           |             | String     | Cellulaire du bénéficiaire                                                                                            |
| email                |             | String     | Email du bénéficiaire                                                                                                 |
| vehicule             |             | Objet      | Informations technique du véhicule                                                                                    |
| puissanceFiscale     | X           | Int        | Puissance fiscale du véhicule                                                                                         |
| dateMiseCirculation  | X           | String     | Date de mise en circulation du véhicule Format : AAAA-MM-JJ                                                           |
| nombrePlace          | X           | Int        | Nombre de place du véhicule                                                                                           |
| valeurNeuve          |             | BigDecimal | Valeur vénal du véhicule                                                                                              |
| valeurActuelle       |             | BigDecimal | Valeur actuelle du véhicule                                                                                           |
| immatriculation      | X           | String     | Immatriculation du véhicule                                                                                           |
| energie              | X           | String     | Energie (ESSENCE ou DIESEL)                                                                                           |
| genre                | X           | String     | Genre du véhicule (VP, TPC,…). Voir tableau des valeurs attendues pour le genre                                       |
| modele               | X           | String     | Modèle du véhicule                                                                                                    |
| marque               | X           | String     | Marque du véhicule                                                                                                    |
| chassis              |             | String     | Numéro de châssis du véhicule                                                                                         |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Body:
{
"referenceFlotte":"123f2",
"dateEffet":"2024-07-10",
"duree":5,
 "periodicite":"MOIS",
 "typePersonne": "MORALE",
 "police":"1234567",
"souscripteur":{
"nom":"Test ",
"prenom":" SARL ",
"cellulaire":"770000000",
"email":"test@test.com"
},
"items":[
{
"responsabiliteCivile":34733,
"referenceTrxPartner":"123f21",
"assure":{
"nom":" Test ",
"prenom":" SARL ",
"cellulaire":"770000000",
"email":"test@diotali.com"
},
"vehicule":{
"puissanceFiscale":10,
"dateMiseCirculation":"1999-11-08",
"nombrePlace":5,
"valeurNeuve":4500000,
"valeurActuelle":3500000,
"immatriculation":"DK-0000-KL",
"energie":"ESSENCE",
"genre":"VP",
"modele":"LOGAN",
"marque":"RENAULD",
"chassis":""
}
},
 {
"responsabiliteCivile":34733,
"referenceTrxPartner":"123f22",
"assure":{
"nom":" Test ",
"prenom":" SARL ",
"cellulaire":"770000000",
"email":"test@diotali.com"
},
"vehicule":{
"puissanceFiscale":10,
"dateMiseCirculation":"2000-11-08",
"nombrePlace":5,
"valeurNeuve":4500000,
"valeurActuelle":3500000,
"immatriculation":"DK-1000-KL",
"energie":"ESSENCE",
"genre":"VP",
"modele":"LOGAN",
"marque":"RENAULD",
"chassis":""
}
},
{
"responsabiliteCivile":34733,
"referenceTrxPartner":"123f23",
"assure":{
"nom":" Test ",
"prenom":" SARL ",
"cellulaire":"770000000",
"email":"test@diotali.com"
},
"vehicule":{
"puissanceFiscale":10,
"dateMiseCirculation":"2006-11-08",
"nombrePlace":5,
"valeurNeuve":4500000,
"valeurActuelle":3500000,
"immatriculation":"DK-2000-KL",
"energie":"ESSENCE",
"genre":"VP",
"modele":"LOGAN",
"marque":"RENAULD",
"chassis":""
}
},
]
}
```

**Response** :

| Nom               | Type   | Description                                                                                                                         |
|-------------------|--------|-------------------------------------------------------------------------------------------------------------------------------------|
| code              | Int    | Code retour                                                                                                                         |
| status            | String | Statut de la requête : SUCCESS ou ERROR                                                                                             |
| message           | String | Message lié au statut de la requête                                                                                                 |
| items             | Objet  |                                                                                                                                     |
| referenceExterne  | String | Référence interne de la transaction au niveau de la compagnie                                                                       |
| attestationNumber | String | Numéro de l'attestation                                                                                                             |
| linkAttestation   | String | Lien URL vers l'attestation digitale                                                                                                |
| secureKey         | String |                                                                                                                                     |
| dateExpiration    | String | Date d'expiration de l'attestation calculée automatiquement à partir de la date d'effet et de la durée Format : AAAA-MM-JJ-HH-MM-SS |
| linkCarteBrune    | String | Lien URL vers l'attestation carte brune                                                                                             |

Exemple de response:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "items": [
    {
      "referenceExterne": "123f21",
      "linkAttestation": "https://aas.diotali.com/#/attestation/SN00JXXXXXX",
      "attestationNumber": " SN00JXXXXXX ",
      "secureKey": "xxxxxxxxxxxxxx",
      "dateExpiration": "2024-12-09T23:59:59 ",
      " linkCarteBrune ": "https://aas.diotali.com/#/attestation/SN00JXXXXXX"
    },
    {
      "referenceExterne": "123f22",
      "linkAttestation": "https://aas.diotali.com/#/attestation/SN00JXXXXXX",
      "attestationNumber": " SN00JXXXXXX ",
      "secureKey": "xxxxxxxxxxxxxx",
      "dateExpiration": "2024-12-09T23:59:59 ",
      " linkCarteBrune ": "https://aas.diotali.com/#/attestation/SN00JXXXXXX"
    },
    {
      "referenceExterne": "123f23",
      "linkAttestation": "https://aas.diotali.com/#/attestation/SN00JXXXXXX",
      "attestationNumber": " SN00JXXXXXX ",
      "secureKey": "xxxxxxxxxxxxxx",
      "dateExpiration": "2024-12-09T23:59:59 ",
      " linkCarteBrune ": "https://aas.diotali.com/#/attestation/SN00JXXXXXX"
    },
    }
```

**Codes d'erreurs** :

| Code | Type    | Message                                                                 |
|------|---------|-------------------------------------------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès                                         |
| 4006 | ERROR   | Merci de renseigner une Responsabilité civile valide                    |
| 4007 | ERROR   | Merci de renseigner un genre valide                                     |
| 4008 | ERROR   | Merci de renseigner une énergie valide                                  |
| 4010 | ERROR   | Votre stock de Qr est épuisé. Merci de passer votre commande de QR      |
| 5006 | ERROR   | La référence n'est pas unique. Merci de renseigner une référence valide |

### 8.3 API d'incorporation à une flotte (incorpore-flotte-request)

Cet API permet d'incorporer un ou plusieurs véhicule(s) à une flotte existante et de générer les nouvelles attestations
automobiles digitales. Il faudrait fournir la référence de la flotte existante te la liste contenant les informations
des nouveaux véhicules à ajouter à la flotte.

Les QR Code générés seront déduits du stock de « QR code virtuel » de la compagnie.

**Méthode** : POST  
**URL**: {URL}/compagnie/service/ incorpore-flotte-request

**Request** :

| Nom                  | Obligatoire | Type       | Description                                                                                                                            |
|----------------------|-------------|------------|----------------------------------------------------------------------------------------------------------------------------------------|
| referenceFlotte      | X           | String     | Reference de la flotte existante au niveau de la compagnie.                                                                            |
| dateEffet            | X           | String     | Date effet de l'attestation automobile Format : AAAA-MM-JJ                                                                             |
| items                | X           | Objet      | Liste des informations des véhicules à ajouter à la flotte                                                                             |
| referenceTrxPartner  | X           | String     | Référence interne de la transaction au niveau de la compagnie. Sera utilisée pour les demandes d'annulation (Avenant) pour ce véhicule |
| responsabiliteCivile | X           | BigDecimal | Montant Responsabilité civile                                                                                                          |
| typePersonne         | X           | String     | Type du souscripteur : PHYSIQUE ou MORALE                                                                                              |
| assure               | X           | Objet      | Informations sur le bénéficiaire du contrat                                                                                            |
| nom                  | X           | String     | Nom du bénéficiaire                                                                                                                    |
| prenom               | X           | String     | Prénom du bénéficiaire                                                                                                                 |
| cellulaire           |             | String     | Cellulaire du bénéficiaire                                                                                                             |
| email                |             | String     | Email du bénéficiaire                                                                                                                  |
| vehicule             |             | Objet      | Informations technique du véhicule                                                                                                     |
| puissanceFiscale     | X           | Int        | Puissance fiscale du véhicule                                                                                                          |
| dateMiseCirculation  | X           | String     | Date de mise en circulation du véhicule Format : AAAA-MM-JJ                                                                            |
| nombrePlace          | X           | Int        | Nombre de place du véhicule                                                                                                            |
| valeurNeuve          |             | BigDecimal | Valeur vénal du véhicule                                                                                                               |
| valeurActuelle       |             | BigDecimal | Valeur actuelle du véhicule                                                                                                            |
| immatriculation      | X           | String     | Immatriculation du véhicule                                                                                                            |
| energie              | X           | String     | Energie (ESSENCE ou DIESEL)                                                                                                            |
| genre                | X           | String     | Genre du véhicule (VP, TPC,…). Voir tableau des valeurs attendues pour le genre                                                        |
| modele               | X           | String     | Modèle du véhicule                                                                                                                     |
| marque               | X           | String     | Marque du véhicule                                                                                                                     |
| chassis              |             | String     | Numéro de châssis du véhicule                                                                                                          |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Body:
{
"referenceFlotte":"123f2",
 "dateEffet":"2024-08-01",
"items":[
{
"responsabiliteCivile":39406,
"referenceTrxPartner":"123f4",
"assure":{
"nom":" Test ",
"prenom":" SARL ",
"cellulaire":"770000000",
"email":"test@diotali.com"
},
"vehicule":{
"puissanceFiscale":14,
"dateMiseCirculation":"1999-11-08",
"nombrePlace":5,
"valeurNeuve":4500000,
"valeurActuelle":3500000,
"immatriculation":"DK-6000-KL",
"energie":"ESSENCE",
"genre":"VP",
"modele":"LOGAN",
"marque":"RENAULD",
"chassis":""
},
},
{
"responsabiliteCivile":39406,
"referenceTrxPartner":"123f5"
"assure":{
"nom":"Test",
"prenom":"SARL",
"cellulaire":"770000000",
"email":"test@diotali.com"
},
"vehicule":{
"puissanceFiscale":14,
"dateMiseCirculation":"2015-11-08",
"nombrePlace":5,
"valeurNeuve":4500000,
"valeurActuelle":3500000,
"immatriculation":"DK-7000-KL",
"energie":"ESSENCE",
"genre":"VP",
"modele":"LOGAN",
"marque":"RENAULD",
"chassis":""
}
}
]
}
```

**Response** :

| Nom               | Type   | Description                                                                                                                         |
|-------------------|--------|-------------------------------------------------------------------------------------------------------------------------------------|
| code              | Int    | Code retour                                                                                                                         |
| status            | String | Statut de la requête : SUCCESS ou ERROR                                                                                             |
| message           | String | Message lié au statut de la requête                                                                                                 |
| items             | Objet  |                                                                                                                                     |
| referenceExterne  | String | Référence interne de la transaction au niveau de la compagnie                                                                       |
| attestationNumber | String | Numéro de l'attestation                                                                                                             |
| linkAttestation   | String | Lien URL vers l'attestation digitale                                                                                                |
| secureKey         | String |                                                                                                                                     |
| dateExpiration    | String | Date d'expiration de l'attestation calculée automatiquement à partir de la date d'effet et de la durée Format : AAAA-MM-JJ-HH-MM-SS |
| linkCarteBrune    | String | Lien URL vers l'attestation Carte brune                                                                                             |

Exemple de response:

```json
{
  "operationStatus": "SUCCESS",
  "operationMessage": "Opération effectuée avec succès.",
  "items": [
    {
      "referenceExterne": "123f24",
      "linkAttestation": "https://aas.diotali.com/#/attestation/SN00JXXXXXX",
      "attestationNumber": " SN00JXXXXXX ",
      "secureKey": "xxxxxxxxxxxxxx",
      "dateExpiration": "2024-12-09T23:59:59 ",
      "linkCarteBrune": "https://aas.diotali.com/#/attestation/SN00JXXXXXX"
    },
    {
      "referenceExterne": "123f25",
      "linkAttestation": "https://aas.diotali.com/#/attestation/SN00JXXXXXX",
      "attestationNumber": " SN00JXXXXXX ",
      "secureKey": "xxxxxxxxxxxxxx",
      "dateExpiration": "2024-12-09T23:59:59 ",
      "linkCarteBrune": "https://aas.diotali.com/#/attestation/SN00JXXXXXX"
    },
    }
```

**Codes d'erreurs** :

| Code | Type    | Message                                                                 |
|------|---------|-------------------------------------------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès                                         |
| 4006 | ERROR   | Merci de renseigner une Responsabilité civile valide                    |
| 4007 | ERROR   | Merci de renseigner un genre valide                                     |
| 4008 | ERROR   | Merci de renseigner une énergie valide                                  |
| 4010 | ERROR   | Votre stock de Qr est épuisé. Merci de passer votre commande de QR      |
| 5006 | ERROR   | La référence n'est pas unique. Merci de renseigner une référence valide |

### 8.4 API de suppression de véhicule(s) à une flotte (subtract-flotte-request)

Cet API permet de supprimer un ou plusieurs véhicule(s) d'une flotte existante en annulant les attestations automobiles
digitales des véhicules. Il faudrait fournir la référence de la flotte existante et la liste contenant les informations
des véhicules (immatriculation ou châssis) à supprimer de la flotte.

**Méthode** : POST  
**URL**: {URL}/compagnie/service/ subtract-flotte-request

**Request** :

| Nom                 | Obligatoire | Type   | Description                                                                                                                              |
|---------------------|-------------|--------|------------------------------------------------------------------------------------------------------------------------------------------|
| referenceFlotte     | X           | String | Reference de la flotte                                                                                                                   |
| items               |             | Objet  | Objet                                                                                                                                    |
| referenceTrxPartner | X           | string | Référence interne, à la compagnie, de l'attestation à annuler. Il s'agit de la référence fournit lors de la génération de l'attestation. |
| attestationNumber   | X           | string | Numéro d'attestation digitale associé à referenceTrxpartner                                                                              |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Body:
{
"referenceFlotte":"123f2",
"items":[
{
 "referenceTrxPartner": "123f24",
 "attestationNumber": "SN000XXXXX"
 },
 {
 "referenceTrxPartner": "123f25",
 "attestationNumber": "SN000XXXXX"
}
}
]
}
```

**Response** :

| Nom                 | Type    | Description                                                   |
|---------------------|---------|---------------------------------------------------------------|
| code                | integer | Code retour                                                   |
| status              | string  | Statut de la requête : SUCCESS ou ERROR                       |
| message             | string  | Message lié au statut de la requête                           |
| items               | Objet   |                                                               |
| referenceTrxPartner | String  | Référence interne de la transaction au niveau de la compagnie |

Example de response:

**Codes d'erreurs** :

| Code | Type    | Message                                                             |
|------|---------|---------------------------------------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès                                     |
| 5006 | ERROR   | La référence n'existe pas. Merci de renseigner une référence valide |

---

## 9. API de vérification statut attestation (check-qrcode-status)

Cette API permet de vérifier le statut d'une attestation à partir de la référence de la transaction fournie par la
compagnie lors de la génération de l'attestation.

**Méthode** : GET  
**URL**: {URL}/compagnie/service/check-qrcode-status/:referenceTrxPartner

**Path Variables**:

| Nom                 | Obligatoire | Type   | Description                                                                                         |
|---------------------|-------------|--------|-----------------------------------------------------------------------------------------------------|
| referenceTrxPartner | X           | String | Référence interne de la transaction fournie par la compagnie lors de la génération de l'attestation |

Exemple de request:

```
Headers :
Authorization : {{Bearer}} {{ACCESS_TOKEN}}
Path Variables:
{
 " referenceTrxPartner ":"123456789",
}
```

**Response** :

| Nom                 | Type   | Description                                                                                                              |
|---------------------|--------|--------------------------------------------------------------------------------------------------------------------------|
| status              | String | Statut de la requête : SUCCESS ou ERROR                                                                                  |
| message             | String | Message lié au statut de la requête                                                                                      |
| data                | Objet  | Infirmations sur le statut de l'attestation                                                                              |
| referenceTrxPartner | String | Référence de la transaction fournie par la compagnie                                                                     |
| partnerAutoResponse |        |                                                                                                                          |
| referenceExterne    | string | Référence de la Transaction initiale                                                                                     |
| linkAttestation     | string | Lien URL vers l'attestation                                                                                              |
| attestationNumber   | string | Numéro de l'attestation                                                                                                  |
| secureKey           | string |                                                                                                                          |
| dateExpiration      | string | Date d'expiration de l'attestation                                                                                       |
| linkCarteBrune      | string | Lien vers l'attestation carte brune                                                                                      |
| status              | String | Statut de l'Attestation. Qui peut avoir 4 valeurs :<br>- EN COURS<br>- EXPIREE<br>- ANNULEE<br>- RESILIEE<br>- SUSPENDUE |

Exemple de response:

```json
{
  "code": "<integer>",
  "status": "<string>",
  "message": "<string>",
  "data": {
    "referenceTrxPartner": "<string>",
    "partnerAutoResponse": {
      "referenceExterne": "<string>",
      "linkAttestation": "<string>",
      "attestationNumber": "<string>",
      "secureKey": "<string>",
      "dateExpiration": "<string>",
      "linkCarteBrune": "<string>"
    },
    "status": "<string>"
    "
  }
}
```

**Codes d'erreurs** :

| Code | Type    | Message                                            |
|------|---------|----------------------------------------------------|
| 2000 | SUCCESS | Opération effectuée avec succès                    |
| 4011 | ERROR   | Aucune attestation ne correspond à cette référence |