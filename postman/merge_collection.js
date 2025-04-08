/**
 * Script pour fusionner les fichiers de collection Postman
 * Auteur: Cyr Leonce Anicet KAKOU
 * Email: cyrkakou@gmail.com
 */

const fs = require('fs');
const path = require('path');

// Chemins des fichiers
const mainCollectionPath = path.join(__dirname, 'SaaS_API_Collection.json');
const usersApiPath = path.join(__dirname, 'Users_API.json');
const rolesApiPath = path.join(__dirname, 'Roles_API.json');
const permissionsApiPath = path.join(__dirname, 'Permissions_API.json');
const apiKeysApiPath = path.join(__dirname, 'ApiKeys_API.json');
const organizationsApiPath = path.join(__dirname, 'Organizations_API.json');
const outputPath = path.join(__dirname, 'SaaS_Complete_Collection.json');

// Lire le fichier principal
const mainCollection = JSON.parse(fs.readFileSync(mainCollectionPath, 'utf8'));

// Lire et fusionner les autres fichiers
const usersApi = JSON.parse(fs.readFileSync(usersApiPath, 'utf8'));
const rolesApi = JSON.parse(fs.readFileSync(rolesApiPath, 'utf8'));
const permissionsApi = JSON.parse(fs.readFileSync(permissionsApiPath, 'utf8'));
const apiKeysApi = JSON.parse(fs.readFileSync(apiKeysApiPath, 'utf8'));
const organizationsApi = JSON.parse(fs.readFileSync(organizationsApiPath, 'utf8'));

// Ajouter les items aux items de la collection principale
mainCollection.item = [
    ...mainCollection.item,
    ...usersApi.item,
    ...rolesApi.item,
    ...permissionsApi.item,
    ...apiKeysApi.item,
    ...organizationsApi.item
];

// Écrire la collection complète dans un nouveau fichier
fs.writeFileSync(outputPath, JSON.stringify(mainCollection, null, 2));

console.log('Collection complète générée avec succès: ' + outputPath);
