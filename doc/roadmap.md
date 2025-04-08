# Roadmap du API SaaS Multi-organisation

**Auteur :** Cyr Léonce Anicet KAKOU
**Email :** cyrkakou@gmail.com
**Version :** 1.0.0
**Date de dernière mise à jour :** 2025-04-04
**Statut :** Document de travail
**Classification :** Confidentiel - Usage interne uniquement

## 1. Vue d'ensemble du projet

### Présentation

Ce document présente la feuille de route détaillée pour le développement d'une plateforme SaaS multi-tenant destinée au
secteur de l'assurance. Il décrit l'architecture technique, la structure du projet, les fonctionnalités clés et le
calendrier de développement prévisionnel.

### Objectif

L'objectif principal est de développer une plateforme SaaS multi-tenant pour la gestion des solutions d'assurance,
permettant aux différents acteurs du secteur (assureurs, courtiers, agents généraux, coassureurs, apporteurs d'affaires,
etc.) de gérer leurs utilisateurs, rôles, permissions et données métier de manière sécurisée et isolée.

La plateforme respectera la hiérarchie naturelle du secteur assurantiel en offrant à chaque entité un espace cloisonné
avec une visibilité adaptée à sa position hiérarchique. Les organisations de niveau supérieur (comme les assureurs)
bénéficieront d'une visibilité complète sur les ressources des organisations qui leur sont subordonnées, tandis que les
organisations de niveau inférieur n'auront accès qu'à leurs propres données.

### Architecture globale

#### Principes architecturaux

La plateforme sera développée selon une architecture hexagonale (Ports & Adapters) avec une organisation modulaire par
domaine métier. Cette approche permettra une séparation claire des préoccupations et facilitera la maintenance et
l'évolution du système.

#### Choix technologiques

- **Type d'architecture** : Monolithe modulaire avec possibilité d'évolution vers des microservices
- **Approche multi-tenant** : Isolation par base de données partagée avec discrimination par ID d'organisation
- **Langage et framework** : Java 17 avec Spring Boot 3.x
- **Base de données** : PostgreSQL 17 avec Flyway pour les migrations
- **Interface utilisateur** : Application web responsive avec React et Material-UI

#### Sécurité

La sécurité est une préoccupation majeure pour cette plateforme multi-tenant. Les mesures suivantes seront mises en
place :

- **Authentification** :
    - JWT Bearer token pour l'authentification des utilisateurs avec refresh tokens
    - API Keys pour l'authentification des systèmes externes et intégrations
- **Autorisation** :
    - Contrôle d'accès basé sur les rôles (RBAC)
    - Filtrage contextuel des données basé sur la hiérarchie organisationnelle
- **Protection des données** :
    - Chiffrement des données sensibles
    - Journalisation complète des accès et modifications
    - Conformité RGPD

## 2. Structure du projet

### Organisation des packages

La structure du projet suit l'architecture hexagonale (Ports & Adapters) avec une organisation modulaire par domaine
métier. Chaque module est organisé selon les principes du Domain-Driven Design (DDD) avec une séparation claire entre
l'API, l'application, le domaine et l'infrastructure. Cette structure a été implémentée et améliorée au cours du
développement pour renforcer la cohérence architecturale.

Cette organisation permet :

- Une séparation claire des préoccupations
- Une meilleure testabilité des composants
- Une évolution indépendante des différents modules
- Une maintenance facilitée
- Une possibilité d'évolution vers des microservices

Voici la structure détaillée des packages :

```
com.devolution.saas
├── SaasApplication.java                # Point d'entrée de l'application
│
├── common                             # Composants communs et utilitaires
│   ├── annotation                     # Annotations personnalisées
│   │   ├── TenantRequired.java        # Annotation pour marquer les endpoints nécessitant un tenant
│   │   └── Auditable.java             # Annotation pour marquer les entités auditables
│   │
│   ├── config                         # Configurations globales
│   │   ├── WebConfig.java             # Configuration MVC et CORS
│   │   ├── ReactiveConfig.java        # Configuration pour les opérations asynchrones
│   │   ├── CacheConfig.java           # Configuration du cache
│   │   ├── AsyncConfig.java           # Configuration des tâches asynchrones
│   │   └── SwaggerConfig.java         # Configuration de la documentation API
│   │
│   ├── domain                         # Modèles de domaine communs
│   │   ├── model                      # Modèles communs
│   │   │   ├── BaseEntity.java        # Classe de base pour toutes les entités
│   │   │   ├── AuditableEntity.java   # Classe de base pour les entités auditables
│   │   │   └── TenantAwareEntity.java # Classe de base pour les entités multi-tenant
│   │   │
│   │   └── repository                 # Repositories communs
│   │       └── BaseRepository.java    # Interface de base pour tous les repositories
│   │
│   ├── exception                      # Gestion des exceptions
│   │   ├── GlobalExceptionHandler.java # Gestionnaire global d'exceptions
│   │   ├── BusinessException.java     # Exception métier de base
│   │   ├── ResourceNotFoundException.java # Exception pour ressource non trouvée
│   │   ├── ValidationException.java   # Exception pour erreurs de validation
│   │   └── SecurityException.java     # Exception pour erreurs de sécurité
│   │
│   ├── logging                        # Configuration et utilitaires de logging
│   │   ├── MDCFilter.java             # Filtre pour ajouter des informations contextuelles aux logs
│   │   ├── LoggingAspect.java         # Aspect pour le logging des méthodes
│   │   └── RequestLoggingFilter.java  # Filtre pour le logging des requêtes HTTP
│   │
│   ├── security                       # Configuration de sécurité commune
│   │   ├── TenantContext.java         # Contexte pour stocker l'ID du tenant courant
│   │   ├── OrganizationContext.java   # Contexte pour stocker l'ID de l'organisation courante
│   │   └── SecurityUtils.java         # Utilitaires de sécurité
│   │
│   └── util                           # Classes utilitaires
│       ├── DateUtils.java             # Utilitaires pour les dates
│       ├── StringUtils.java           # Utilitaires pour les chaînes de caractères
│       ├── ValidationUtils.java       # Utilitaires pour la validation
│       └── JsonUtils.java             # Utilitaires pour la manipulation JSON
├── organization                       # Module de gestion des organisations
│   ├── api                            # Contrôleurs REST
│   │   ├── OrganizationController.java # API pour la gestion des organisations
│   │   ├── OrganizationTypeController.java # API pour les types d'organisations
│   │   └── OrganizationHierarchyController.java # API pour la hiérarchie des organisations
│   │
│   ├── application                    # Services d'application
│   │   ├── service                    # Implémentations des services
│   │   │   └── OrganizationService.java # Service principal pour les organisations
│   │   │
│   │   ├── command                    # Commandes (pattern CQRS)
│   │   │   ├── CreateOrganizationCommand.java # Commande de création d'organisation
│   │   │   └── UpdateOrganizationCommand.java # Commande de mise à jour d'organisation
│   │   │
│   │   ├── query                      # Requêtes (pattern CQRS)
│   │   │   ├── GetOrganizationQuery.java # Requête pour obtenir une organisation
│   │   │   └── ListOrganizationsQuery.java # Requête pour lister les organisations
│   │   │
│   │   ├── command/handler           # Gestionnaires de commandes
│   │   │   ├── CreateOrganizationHandler.java # Gestionnaire de création
│   │   │   └── UpdateOrganizationHandler.java # Gestionnaire de mise à jour
│   │   │
│   │   ├── query/handler             # Gestionnaires de requêtes
│   │   │   ├── GetOrganizationHandler.java # Gestionnaire pour obtenir une organisation
│   │   │   └── ListOrganizationsHandler.java # Gestionnaire pour lister les organisations
│   │   │
│   │   ├── dto                        # Objets de transfert de données
│   │   │   ├── OrganizationDTO.java   # DTO pour les organisations
│   │   │   ├── OrganizationTypeDTO.java # DTO pour les types d'organisations
│   │   │   └── OrganizationHierarchyDTO.java # DTO pour la hiérarchie
│   │   │
│   │   ├── mapper                     # Mappers pour la conversion entités/DTOs
│   │   │   └── OrganizationMapper.java # Mapper pour les organisations
│   │   │
│   │   └── usecase                    # Cas d'utilisation
│   │       ├── CreateOrganization.java # Cas d'utilisation de création
│   │       ├── UpdateOrganization.java # Cas d'utilisation de mise à jour
│   │       ├── GetOrganization.java    # Cas d'utilisation pour obtenir une organisation
│   │       ├── ListOrganizations.java  # Cas d'utilisation pour lister les organisations
│   │       ├── GetOrganizationHierarchy.java # Cas d'utilisation pour la hiérarchie
│   │       ├── DeleteOrganization.java # Cas d'utilisation pour la suppression
│   │       └── CreateHierarchyEntries.java # Cas d'utilisation pour la hiérarchie
│   │
│   ├── domain                         # Modèles et repositories de domaine
│   │   ├── model                      # Modèles de domaine
│   │   │   ├── Organization.java      # Entité Organisation
│   │   │   ├── OrganizationType.java  # Enum des types d'organisations
│   │   │   └── OrganizationHierarchy.java # Entité pour la hiérarchie
│   │   │
│   │   ├── repository                 # Repositories de domaine
│   │   │   ├── OrganizationRepository.java # Repository pour les organisations
│   │   │   └── OrganizationHierarchyRepository.java # Repository pour la hiérarchie
│   │   │
│   │   ├── service                    # Services de domaine
│   │   │   └── OrganizationDomainService.java # Service de domaine
│   │   │
│   │   └── event                      # Événements de domaine
│   │       ├── OrganizationCreatedEvent.java # Événement de création
│   │       └── OrganizationUpdatedEvent.java # Événement de mise à jour
│   │
│   └── infrastructure                 # Implémentations d'infrastructure
│       ├── persistence                # Implémentations de persistence
│       │   ├── JpaOrganizationRepository.java # Implémentation JPA du repository
│       │   └── JpaOrganizationHierarchyRepository.java # Implémentation JPA
│       │
│       └── messaging                  # Implémentations de messaging
│           └── OrganizationEventPublisher.java # Publieur d'événements
├── security                           # Module de gestion de la sécurité
│   ├── api                            # Contrôleurs REST
│   │   ├── AuthController.java        # API d'authentification
│   │   ├── UserController.java        # API de gestion des utilisateurs
│   │   ├── RoleController.java        # API de gestion des rôles
│   │   └── PermissionController.java  # API de gestion des permissions
│   │
│   ├── application                    # Services d'application
│   │   ├── service                    # Implémentations des services
│   │   │   ├── AuthService.java       # Service d'authentification
│   │   │   ├── UserService.java       # Service de gestion des utilisateurs
│   │   │   └── RoleService.java       # Service de gestion des rôles
│   │   │
│   │   ├── command                    # Commandes
│   │   │   ├── RegisterUserCommand.java # Commande d'enregistrement utilisateur
│   │   │   └── CreateRoleCommand.java # Commande de création de rôle
│   │   │
│   │   ├── dto                        # Objets de transfert de données
│   │   │   ├── UserDTO.java           # DTO pour les utilisateurs
│   │   │   ├── RoleDTO.java           # DTO pour les rôles
│   │   │   ├── PermissionDTO.java     # DTO pour les permissions
│   │   │   ├── AuthRequestDTO.java    # DTO pour les requêtes d'authentification
│   │   │   └── AuthResponseDTO.java   # DTO pour les réponses d'authentification
│   │   │
│   │   ├── mapper                     # Mappers pour la conversion entités/DTOs
│   │   │   └── UserMapper.java        # Mapper pour les utilisateurs
│   │   │
│   │   └── usecase                    # Cas d'utilisation
│   │       ├── Authenticate.java       # Cas d'utilisation d'authentification
│   │       ├── RegisterUser.java       # Cas d'utilisation d'enregistrement
│   │       ├── CreateUser.java         # Cas d'utilisation de création d'utilisateur
│   │       ├── UpdateUser.java         # Cas d'utilisation de mise à jour d'utilisateur
│   │       ├── GetUser.java            # Cas d'utilisation pour obtenir un utilisateur
│   │       ├── GetUserByUsername.java  # Cas d'utilisation pour obtenir un utilisateur par nom
│   │       ├── GetUserByEmail.java     # Cas d'utilisation pour obtenir un utilisateur par email
│   │       ├── ListUsers.java          # Cas d'utilisation pour lister les utilisateurs
│   │       ├── ListUsersByStatus.java  # Cas d'utilisation pour lister par statut
│   │       ├── ListUsersByOrganization.java # Cas d'utilisation pour lister par organisation
│   │       ├── ChangePassword.java     # Cas d'utilisation pour changer le mot de passe
│   │       ├── ActivateUser.java       # Cas d'utilisation pour activer un utilisateur
│   │       ├── DeactivateUser.java     # Cas d'utilisation pour désactiver un utilisateur
│   │       ├── LockUser.java           # Cas d'utilisation pour verrouiller un utilisateur
│   │       └── UnlockUser.java         # Cas d'utilisation pour déverrouiller un utilisateur
│   │
│   ├── domain                         # Modèles et repositories de domaine
│   │   ├── model                      # Modèles de domaine
│   │   │   ├── User.java              # Entité Utilisateur
│   │   │   ├── Role.java              # Entité Rôle
│   │   │   ├── Permission.java        # Entité Permission
│   │   │   └── RefreshToken.java      # Entité Token de rafraîchissement
│   │   │
│   │   ├── repository                 # Repositories de domaine
│   │   │   ├── UserRepository.java    # Repository pour les utilisateurs
│   │   │   ├── RoleRepository.java    # Repository pour les rôles
│   │   │   └── PermissionRepository.java # Repository pour les permissions
│   │   │
│   │   └── service                    # Services de domaine
│   │       └── PasswordService.java   # Service de gestion des mots de passe
│   │
│   └── infrastructure                 # Implémentations d'infrastructure
│       ├── config                     # Configuration de sécurité
│       │   ├── SecurityConfig.java    # Configuration Spring Security
│       │   └── JwtConfig.java         # Configuration JWT
│       │
│       ├── filter                     # Filtres de sécurité
│       │   ├── JwtAuthFilter.java     # Filtre d'authentification JWT
│       │   └── ApiKeyAuthFilter.java  # Filtre d'authentification par API Key
│       │
│       └── service                    # Services d'infrastructure
│           ├── JwtService.java        # Service de gestion des JWT
│           └── UserDetailsServiceImpl.java # Implémentation UserDetailsService
├── insurance                          # Module de gestion des produits d'assurance
│   ├── api                            # Contrôleurs REST
│   │   ├── ProductController.java     # API pour les produits d'assurance
│   │   ├── CoverageController.java    # API pour les garanties
│   │   └── CategoryController.java    # API pour les catégories de produits
│   │
│   ├── application                    # Services d'application
│   │   ├── service                    # Implémentations des services
│   │   │   └── ProductService.java    # Service de gestion des produits
│   │   │
│   │   ├── dto                        # Objets de transfert de données
│   │   │   ├── ProductDTO.java        # DTO pour les produits
│   │   │   ├── CoverageDTO.java       # DTO pour les garanties
│   │   │   └── CategoryDTO.java       # DTO pour les catégories
│   │   │
│   │   └── usecase                    # Cas d'utilisation
│   │       ├── CreateProduct.java # Cas d'utilisation de création de produit
│   │       └── ConfigureCoverage.java # Cas d'utilisation de configuration
│   │
│   ├── domain                         # Modèles et repositories de domaine
│   │   ├── model                      # Modèles de domaine
│   │   │   ├── Product.java           # Entité Produit d'assurance
│   │   │   ├── Coverage.java          # Entité Garantie
│   │   │   └── Category.java          # Entité Catégorie de produit
│   │   │
│   │   └── repository                 # Repositories de domaine
│   │       ├── ProductRepository.java # Repository pour les produits
│   │       └── CoverageRepository.java # Repository pour les garanties
│   │
│   └── infrastructure                 # Implémentations d'infrastructure
│       └── persistence                # Implémentations de persistence
│           └── JpaProductRepository.java # Implémentation JPA du repository
├── policy                             # Module de gestion des polices d'assurance
│   ├── api                            # Contrôleurs REST
│   │   ├── PolicyController.java      # API pour les polices d'assurance
│   │   ├── QuoteController.java       # API pour les devis
│   │   └── EndorsementController.java # API pour les avenants
│   │
│   ├── application                    # Services d'application
│   │   ├── service                    # Implémentations des services
│   │   │   ├── PolicyService.java     # Service de gestion des polices
│   │   │   └── QuoteService.java      # Service de gestion des devis
│   │   │
│   │   ├── dto                        # Objets de transfert de données
│   │   │   ├── PolicyDTO.java         # DTO pour les polices
│   │   │   ├── QuoteDTO.java          # DTO pour les devis
│   │   │   └── EndorsementDTO.java    # DTO pour les avenants
│   │   │
│   │   └── usecase                    # Cas d'utilisation
│   │       ├── CreatePolicy.java # Cas d'utilisation de création de police
│   │       └── GenerateQuote.java # Cas d'utilisation de génération de devis
│   │
│   ├── domain                         # Modèles et repositories de domaine
│   │   ├── model                      # Modèles de domaine
│   │   │   ├── Policy.java            # Entité Police d'assurance
│   │   │   ├── Quote.java             # Entité Devis
│   │   │   └── Endorsement.java       # Entité Avenant
│   │   │
│   │   ├── repository                 # Repositories de domaine
│   │   │   ├── PolicyRepository.java  # Repository pour les polices
│   │   │   └── QuoteRepository.java   # Repository pour les devis
│   │   │
│   │   └── service                    # Services de domaine
│   │       ├── PricingService.java    # Service de tarification
│   │       └── RenewalService.java    # Service de renouvellement
│   │
│   └── infrastructure                 # Implémentations d'infrastructure
│       └── persistence                # Implémentations de persistence
│           └── JpaPolicyRepository.java # Implémentation JPA du repository
├── nonelife                           # Module spécifique aux produits nonelife
│   ├── auto                           # Module d'assurance automobile
│   │   ├── api                        # Contrôleurs REST
│   │   │   ├── AutoPolicyController.java # API pour les polices auto
│   │   │   └── VehicleController.java  # API pour les véhicules
│   │   │
│   │   ├── application                # Services d'application
│   │   │   ├── service                # Implémentations des services
│   │   │   │   ├── AutoPolicyService.java # Service pour les polices auto
│   │   │   │   └── VehicleService.java  # Service pour les véhicules
│   │   │   │
│   │   │   ├── dto                    # Objets de transfert de données
│   │   │   │   ├── AutoPolicyDTO.java  # DTO pour les polices auto
│   │   │   │   └── VehicleDTO.java     # DTO pour les véhicules
│   │   │   │
│   │   │   └── usecase                # Cas d'utilisation
│   │   │       └── CreateAutoPolicy.java # Cas d'utilisation
│   │   │
│   │   ├── domain                     # Modèles et repositories de domaine
│   │   │   ├── model                  # Modèles de domaine
│   │   │   │   ├── AutoPolicy.java     # Entité Police auto
│   │   │   │   └── Vehicle.java        # Entité Véhicule
│   │   │   │
│   │   │   └── repository             # Repositories de domaine
│   │   │       ├── AutoPolicyRepository.java # Repository pour les polices auto
│   │   │       └── VehicleRepository.java # Repository pour les véhicules
│   │   │
│   │   └── infrastructure             # Implémentations d'infrastructure
│   │       └── persistence            # Implémentations de persistence
│   │           └── JpaAutoPolicyRepository.java # Implémentation JPA
│   │
│   ├── home                           # Module d'assurance habitation (MRH)
│   │   ├── api                        # Contrôleurs REST
│   │   │   ├── HomePolicyController.java # API pour les polices habitation
│   │   │   └── PropertyController.java # API pour les biens immobiliers
│   │   │
│   │   ├── application                # Services d'application
│   │   │   ├── service                # Implémentations des services
│   │   │   │   ├── HomePolicyService.java # Service pour les polices habitation
│   │   │   │   └── PropertyService.java # Service pour les biens immobiliers
│   │   │   │
│   │   │   ├── dto                    # Objets de transfert de données
│   │   │   │   ├── HomePolicyDTO.java  # DTO pour les polices habitation
│   │   │   │   └── PropertyDTO.java    # DTO pour les biens immobiliers
│   │   │   │
│   │   │   └── usecase                # Cas d'utilisation
│   │   │       └── CreateHomePolicy.java # Cas d'utilisation
│   │   │
│   │   ├── domain                     # Modèles et repositories de domaine
│   │   │   ├── model                  # Modèles de domaine
│   │   │   │   ├── HomePolicy.java     # Entité Police habitation
│   │   │   │   └── Property.java       # Entité Bien immobilier
│   │   │   │
│   │   │   └── repository             # Repositories de domaine
│   │   │       ├── HomePolicyRepository.java # Repository pour les polices habitation
│   │   │       └── PropertyRepository.java # Repository pour les biens immobiliers
│   │   │
│   │   └── infrastructure             # Implémentations d'infrastructure
│   │       └── persistence            # Implémentations de persistence
│   │           └── JpaHomePolicyRepository.java # Implémentation JPA
│   │
│   ├── travel                         # Module d'assurance voyage
│   │   ├── api                        # Contrôleurs REST
│   │   │   ├── TravelPolicyController.java # API pour les polices voyage
│   │   │   └── TripController.java    # API pour les voyages
│   │   │
│   │   ├── application                # Services d'application
│   │   │   ├── service                # Implémentations des services
│   │   │   │   ├── TravelPolicyService.java # Service pour les polices voyage
│   │   │   │   └── TripService.java    # Service pour les voyages
│   │   │   │
│   │   │   ├── dto                    # Objets de transfert de données
│   │   │   │   ├── TravelPolicyDTO.java # DTO pour les polices voyage
│   │   │   │   └── TripDTO.java        # DTO pour les voyages
│   │   │   │
│   │   │   └── usecase                # Cas d'utilisation
│   │   │       └── CreateTravelPolicy.java # Cas d'utilisation
│   │   │
│   │   ├── domain                     # Modèles et repositories de domaine
│   │   │   ├── model                  # Modèles de domaine
│   │   │   │   ├── TravelPolicy.java   # Entité Police voyage
│   │   │   │   └── Trip.java           # Entité Voyage
│   │   │   │
│   │   │   └── repository             # Repositories de domaine
│   │   │       ├── TravelPolicyRepository.java # Repository pour les polices voyage
│   │   │       └── TripRepository.java  # Repository pour les voyages
│   │   │
│   │   └── infrastructure             # Implémentations d'infrastructure
│   │       └── persistence            # Implémentations de persistence
│   │           └── JpaTravelPolicyRepository.java # Implémentation JPA
│   │
│   └── misc                           # Module risques divers
│       ├── api                        # Contrôleurs REST
│       │   └── MiscPolicyController.java # API pour les polices risques divers
│       │
│       ├── application                # Services d'application
│       │   ├── service                # Implémentations des services
│       │   │   └── MiscPolicyService.java # Service pour les polices risques divers
│       │   │
│       │   ├── dto                    # Objets de transfert de données
│       │   │   └── MiscPolicyDTO.java   # DTO pour les polices risques divers
│       │   │
│       │   └── usecase                # Cas d'utilisation
│       │       └── CreateMiscPolicy.java # Cas d'utilisation
│       │
│       ├── domain                     # Modèles et repositories de domaine
│       │   ├── model                  # Modèles de domaine
│       │   │   └── MiscPolicy.java      # Entité Police risques divers
│       │   │
│       │   └── repository             # Repositories de domaine
│       │       └── MiscPolicyRepository.java # Repository pour les polices risques divers
│       │
│       └── infrastructure             # Implémentations d'infrastructure
│           └── persistence            # Implémentations de persistence
│               └── JpaMiscPolicyRepository.java # Implémentation JPA
├── claim                              # Module de gestion des sinistres
│   ├── api                            # Contrôleurs REST
│   │   ├── ClaimController.java       # API pour les sinistres
│   │   └── SettlementController.java  # API pour les règlements
│   │
│   ├── application                    # Services d'application
│   │   ├── service                    # Implémentations des services
│   │   │   └── ClaimService.java      # Service de gestion des sinistres
│   │   │
│   │   ├── dto                        # Objets de transfert de données
│   │   │   ├── ClaimDTO.java          # DTO pour les sinistres
│   │   │   └── SettlementDTO.java     # DTO pour les règlements
│   │   │
│   │   └── usecase                    # Cas d'utilisation
│   │       ├── FileClaim.java  # Cas d'utilisation de déclaration de sinistre
│   │       └── ProcessClaim.java # Cas d'utilisation de traitement de sinistre
│   │
│   ├── domain                         # Modèles et repositories de domaine
│   │   ├── model                      # Modèles de domaine
│   │   │   ├── Claim.java             # Entité Sinistre
│   │   │   └── Settlement.java        # Entité Règlement
│   │   │
│   │   └── repository                 # Repositories de domaine
│   │       └── ClaimRepository.java   # Repository pour les sinistres
│   │
│   └── infrastructure                 # Implémentations d'infrastructure
│       └── persistence                # Implémentations de persistence
│           └── JpaClaimRepository.java # Implémentation JPA du repository
│
└── reporting                          # Module de reporting et d'analytics
    ├── api                            # Contrôleurs REST
    │   ├── ReportController.java      # API pour les rapports
    │   └── DashboardController.java   # API pour les tableaux de bord
    │
    ├── application                    # Services d'application
    │   ├── service                    # Implémentations des services
    │   │   └── ReportingService.java  # Service de reporting
    │   │
    │   └── dto                        # Objets de transfert de données
    │       ├── ReportDTO.java         # DTO pour les rapports
    │       └── DashboardDTO.java      # DTO pour les tableaux de bord
    │
    ├── domain                         # Modèles et repositories de domaine
    │   ├── model                      # Modèles de domaine
    │   │   ├── Report.java            # Entité Rapport
    │   │   └── Dashboard.java         # Entité Tableau de bord
    │   │
    │   └── repository                 # Repositories de domaine
    │       └── ReportRepository.java  # Repository pour les rapports
    │
    └── infrastructure                 # Implémentations d'infrastructure
        └── persistence                # Implémentations de persistence
            └── JpaReportRepository.java # Implémentation JPA du repository
```

### Modèle de données

Le modèle de données est structuré autour des entités principales suivantes :

#### Entités de base

1. **Organization** : Représente une organisation cliente (assureur, courtier, agent général, etc.)
2. **OrganizationType** : Définit le type d'organisation et son niveau dans la hiérarchie assurantielle
3. **User** : Représente un utilisateur du système
4. **Role** : Représente un rôle dans le système
5. **Permission** : Représente une permission spécifique
6. **RefreshToken** : Stocke les tokens de rafraîchissement
7. **Policy** : Représente une police d'assurance
8. **Claim** : Représente un sinistre
9. **Product** : Représente un produit d'assurance
10. **ProductCategory** : Représente une catégorie de produits d'assurance (Auto, MRH, Voyage, etc.)
11. **Coverage** : Représente une garantie associée à un produit d'assurance
12. **AuditLog** : Enregistre toutes les actions effectuées dans le système

#### Relations

- Une **Organization** peut avoir plusieurs **Organizations** enfants (hiérarchie)
- Chaque **Organization** a un **OrganizationType** qui définit son niveau hiérarchique
- Un **User** peut appartenir à plusieurs **Organizations** avec des rôles différents
- Un **User** peut avoir plusieurs **Roles** dans une même organisation
- Un **Role** peut avoir plusieurs **Permissions**
- Une **Organization** de niveau supérieur peut voir les données des **Organizations** de niveau inférieur
- Une **Policy** est liée à une ou plusieurs **Organizations** selon leur rôle (assureur, courtier, etc.)
- Un **Claim** est lié à une **Policy** et peut être géré par différentes **Organizations**

## 3. Fonctionnalités par module

### Module Common

- Configuration de base de l'application
- Gestion des exceptions globales
- Utilitaires communs
- Filtres de sécurité et intercepteurs
- Mécanisme de filtrage automatique des données basé sur la hiérarchie des organisations
- Annotations personnalisées pour la gestion des permissions hiérarchiques

### Module Organization

- Gestion des organisations (CRUD)
- Gestion des types d'organisations spécifiques au secteur assurantiel (assureur, courtier, agent général, etc.)
- Gestion de la hiérarchie des organisations avec visibilité descendante
- Gestion des contacts d'organisation avec types de contacts (principal, facturation, technique, etc.)
- Gestion des paramètres d'organisation avec valeurs personnalisées par organisation
- Gestion des relations entre organisations (parent-enfant, coassurance, courtage, etc.)
- Association des utilisateurs aux organisations avec gestion des rôles spécifiques
- Gestion des filiales et agences pour les assureurs et courtiers
- Gestion des structures multi-filiales et multi-agences avec visibilité adaptée
- Tableaux de bord adaptés à chaque type d'organisation
- Interface utilisateur personnalisée selon le type d'organisation et son niveau hiérarchique
- Système de navigation contextuelle adaptée à la position dans la hiérarchie

### Module Security

- Authentification utilisateur par JWT Bearer token (login, logout, refresh token)
- Authentification des systèmes externes par API Keys avec gestion des droits granulaires
- Gestion des utilisateurs (CRUD)
- Gestion des rôles et permissions (CRUD)
- Contrôle d'accès basé sur les rôles (RBAC) avec contexte organisationnel
- Permissions spécifiques pour la visibilité hiérarchique (descendante et latérale)
- Système de cloisonnement des données par organisation avec filtrage automatique
- Gestion des délégations de droits entre organisations avec workflows d'approbation
- Mécanisme de partage contrôlé de données entre organisations de même niveau
- Système de masquage de données sensibles selon le niveau hiérarchique
- Gestion des accès temporaires et des invitations inter-organisations
- Système de détection des accès anormaux basé sur la hiérarchie
- Système d'audit avancé :
    - Audit complet des actions des utilisateurs par niveau d'organisation
    - Traçabilité des modifications de données sensibles avec conservation des valeurs avant/après
    - Journalisation des accès aux données confidentielles
    - Alertes automatiques sur activités suspectes ou anormales
    - Rapports d'audit personnalisables par type d'organisation
    - Conservation des logs d'audit selon les exigences réglementaires
    - Piste d'audit inaltérable pour les opérations critiques
    - Audit spécifique des opérations sur les produits IARD
- Tableau de bord de sécurité pour les administrateurs d'organisations
- Rotation automatique des API Keys et gestion de leur cycle de vie

### Modules métier assurantiels

- **Gestion des polices d'assurance**
    - Création et gestion des contrats d'assurance
    - Suivi des échéances et renouvellements
    - Gestion des avenants et modifications de contrat
    - Visibilité des contrats selon le niveau hiérarchique

- **Gestion des sinistres**
    - Déclaration et suivi des sinistres
    - Workflow de traitement adapté au type d'organisation
    - Gestion des indemnisations et recours
    - Partage d'informations entre les différents intervenants

- **Gestion des produits d'assurance**
    - Configuration des produits et garanties
    - Tarification et règles de souscription
    - Gestion des formules et options
    - Personnalisation des produits par courtier/agent
    - Gestion des produits IARD spécifiques :
        - **Assurance Auto** : Gestion des véhicules, formules tous risques/tiers, bonus-malus
        - **Assurance Habitation (MRH)** : Gestion des biens, risques locatifs, valeur à neuf
        - **Assurance Voyage** : Couvertures temporaires, assistance internationale, annulation
        - **Risques Divers** : Protection juridique, responsabilité civile, assurances affinitaires

- **Gestion de la relation client**
    - Suivi des interactions avec les clients
    - Gestion des réclamations
    - Campagnes marketing et fidélisation
    - Partage des données clients selon les rôles

- **Reporting et analytics**
    - Tableaux de bord spécifiques par type d'organisation
    - Rapports de production et de sinistralité
    - Analyses prédictives et aide à la décision
    - Consolidation des données à différents niveaux hiérarchiques

## 4. Plan d'implémentation

### Phase 1 : Fondation (Sprint 1-2)

- [x] Configuration initiale du projet
- [ ] Mise en place de la structure des packages selon l'architecture hexagonale :
    - [ ] Création des packages communs (common)
        - [ ] Annotations personnalisées (TenantRequired.java, Auditable.java)
        - [ ] Configuration globale (WebConfig.java, ReactiveConfig.java, etc.)
        - [ ] Modèles de base (BaseEntity.java, AuditableEntity.java, TenantAwareEntity.java)
        - [ ] Gestion des exceptions (GlobalExceptionHandler.java, BusinessException.java, etc.)
        - [ ] Configuration de logging (MDCFilter.java, LoggingAspect.java)
        - [ ] Utilitaires (DateUtils.java, StringUtils.java, ValidationUtils.java, JsonUtils.java)
    - [ ] Création des packages métier avec structure hexagonale (api, application, domain, infrastructure)
        - [ ] Module organization
        - [ ] Module security
        - [ ] Module insurance
        - [ ] Module policy
        - [ ] Module claim
        - [ ] Module reporting
        - [ ] Module IARD (auto, home, travel, misc)
- [ ] Implémentation des entités de base :
    - [ ] BaseEntity.java avec champs communs (id, createdAt, updatedAt, etc.)
    - [ ] Organization.java avec structure complète
    - [ ] OrganizationType.java avec types spécifiques au secteur assurantiel
    - [ ] OrganizationHierarchy.java pour la gestion des relations
- [ ] Configuration de la base de données et migrations Flyway :
    - [ ] Scripts de création des tables de base (organizations, organization_types, etc.)
    - [ ] Configuration des index pour optimiser les requêtes hiérarchiques
- [ ] Implémentation des repositories de base :
    - [ ] BaseRepository.java avec méthodes communes
    - [ ] OrganizationRepository.java avec méthodes spécifiques
    - [ ] OrganizationHierarchyRepository.java pour les requêtes hiérarchiques
- [ ] Conception du mécanisme de filtrage contextuel des données :
    - [ ] TenantContext.java pour stocker l'ID du tenant courant
    - [ ] OrganizationContext.java pour le contexte organisationnel

### Phase 2 : Sécurité et authentification (Sprint 3-4)

- [ ] Implémentation du module security :
    - [ ] Modèles de domaine :
        - [ ] User.java - Entité utilisateur
        - [ ] Role.java - Entité rôle
        - [ ] Permission.java - Entité permission
        - [ ] RefreshToken.java - Entité token de rafraîchissement
    - [ ] Repositories :
        - [ ] UserRepository.java - Repository pour les utilisateurs
        - [ ] RoleRepository.java - Repository pour les rôles
        - [ ] PermissionRepository.java - Repository pour les permissions
    - [ ] Services :
        - [ ] AuthService.java - Service d'authentification
        - [ ] UserService.java - Service de gestion des utilisateurs
        - [ ] RoleService.java - Service de gestion des rôles
        - [ ] PasswordService.java - Service de gestion des mots de passe
    - [ ] Infrastructure :
        - [ ] SecurityConfig.java - Configuration Spring Security
        - [ ] JwtConfig.java - Configuration JWT
        - [ ] JwtAuthFilter.java - Filtre d'authentification JWT
        - [ ] ApiKeyAuthFilter.java - Filtre d'authentification par API Key
        - [ ] JwtService.java - Service de gestion des JWT
        - [ ] UserDetailsServiceImpl.java - Implémentation UserDetailsService
    - [ ] API :
        - [ ] AuthController.java - API d'authentification
        - [ ] UserController.java - API de gestion des utilisateurs
        - [ ] RoleController.java - API de gestion des rôles
        - [ ] PermissionController.java - API de gestion des permissions
    - [ ] DTOs :
        - [ ] UserDTO.java - DTO pour les utilisateurs
        - [ ] RoleDTO.java - DTO pour les rôles
        - [ ] PermissionDTO.java - DTO pour les permissions
        - [ ] AuthRequestDTO.java - DTO pour les requêtes d'authentification
        - [ ] AuthResponseDTO.java - DTO pour les réponses d'authentification
- [ ] Implémentation des cas d'utilisation de sécurité :
    - [ ] Authenticate.java - Authentification utilisateur avec JWT
    - [ ] RegisterUser.java - Enregistrement d'un nouvel utilisateur
    - [ ] Gestion des refresh tokens
    - [ ] Contrôle d'accès basé sur les rôles avec contexte organisationnel
    - [ ] Permissions spécifiques pour la visibilité hiérarchique
    - [ ] Système de cloisonnement des données par organisation
- [ ] Implémentation du système d'audit avancé :
    - [ ] AuditLog.java - Entité pour les logs d'audit
    - [ ] AuditLogRepository.java - Repository pour les logs d'audit
    - [ ] AuditService.java - Service de gestion des audits
    - [ ] LoggingAspect.java - Aspect pour la capture automatique des événements
    - [ ] Mécanisme de capture des valeurs avant/après modification
    - [ ] Système de journalisation des accès aux données sensibles
    - [ ] Configuration des règles d'alerte sur activités suspectes
    - [ ] Rapports d'audit personnalisables
    - [ ] Piste d'audit inaltérable
- [ ] Implémentation du système d'API Keys :
    - [ ] ApiKey.java - Entité pour les API Keys
    - [ ] ApiKeyRepository.java - Repository pour les API Keys
    - [ ] ApiKeyService.java - Service de gestion des API Keys
    - [ ] Mécanisme de rotation et révocation des API Keys

### Phase 3 : Gestion des organisations assurantielles (Sprint 5-7)

- [ ] Développement complet du module organization :
    - [ ] Finalisation des modèles de domaine :
        - [ ] Organization.java - Implémentation complète avec tous les attributs
        - [ ] OrganizationType.java - Types spécifiques au secteur assurantiel
        - [ ] OrganizationHierarchy.java - Gestion des relations hiérarchiques
        - [ ] OrganizationContact.java - Gestion des contacts d'organisation
        - [ ] OrganizationSetting.java - Paramètres personnalisés par organisation
        - [ ] OrganizationRelationship.java - Relations entre organisations
    - [ ] Implémentation des repositories :
        - [ ] OrganizationRepository.java - Méthodes de recherche avancées
        - [ ] OrganizationHierarchyRepository.java - Requêtes récursives optimisées
        - [ ] OrganizationRelationshipRepository.java - Gestion des relations
    - [ ] Services de domaine :
        - [ ] OrganizationDomainService.java - Logique métier complexe
    - [ ] Événements de domaine :
        - [ ] OrganizationCreatedEvent.java - Événement de création
        - [ ] OrganizationUpdatedEvent.java - Événement de mise à jour
        - [ ] OrganizationRelationshipChangedEvent.java - Événement de changement de relation
    - [ ] Implémentation des cas d'utilisation :
        - [ ] CreateOrganization.java - Création d'organisation
        - [ ] UpdateOrganization.java - Mise à jour d'organisation
        - [ ] CreateOrganizationRelationship.java - Création de relation
        - [ ] ManageOrganizationHierarchy.java - Gestion de la hiérarchie
    - [ ] API complète :
        - [ ] OrganizationController.java - API pour les organisations
        - [ ] OrganizationTypeController.java - API pour les types d'organisations
        - [ ] OrganizationHierarchyController.java - API pour la hiérarchie
        - [ ] OrganizationRelationshipController.java - API pour les relations
        - [ ] OrganizationContactController.java - API pour les contacts
        - [ ] OrganizationSettingController.java - API pour les paramètres
- [ ] Implémentation des fonctionnalités avancées :
    - [ ] Système d'indexation pour les requêtes hiérarchiques fréquentes
    - [ ] Isolation des données par organisation avec visibilité hiérarchique configurable
    - [ ] Mécanisme de filtrage automatique des données basé sur le contexte organisationnel
    - [ ] Système de délégation de droits entre organisations avec contrôles granulaires
    - [ ] Interface de visualisation et gestion de la hiérarchie organisationnelle
- [ ] Implémentation des relations spécifiques au secteur assurantiel :
    - [ ] Relation assureur-courtier avec visibilité descendante
    - [ ] Relation courtier-apporteur avec visibilité limitée
    - [ ] Relation de coassurance avec visibilité partagée
    - [ ] Relation assureur-filiale avec visibilité bidirectionnelle
    - [ ] Relation courtier-agence avec visibilité descendante

### Phase 4 : Modules métier assurantiels (Sprint 7-10)

- [ ] Implémentation du module policy (gestion des polices d'assurance) :
    - [ ] Modèles de domaine :
        - [ ] Policy.java - Entité Police d'assurance
        - [ ] Quote.java - Entité Devis
        - [ ] Endorsement.java - Entité Avenant
    - [ ] Repositories :
        - [ ] PolicyRepository.java - Repository pour les polices
        - [ ] QuoteRepository.java - Repository pour les devis
    - [ ] Services de domaine :
        - [ ] PricingService.java - Service de tarification
        - [ ] RenewalService.java - Service de renouvellement
    - [ ] API :
        - [ ] PolicyController.java - API pour les polices d'assurance
        - [ ] QuoteController.java - API pour les devis
        - [ ] EndorsementController.java - API pour les avenants
    - [ ] Cas d'utilisation :
        - [ ] CreatePolicy.java - Création de police
        - [ ] GenerateQuote.java - Génération de devis
        - [ ] RenewPolicy.java - Renouvellement de police
        - [ ] CreateEndorsement.java - Création d'avenant

- [ ] Implémentation du module claim (gestion des sinistres) :
    - [ ] Modèles de domaine :
        - [ ] Claim.java - Entité Sinistre
        - [ ] Settlement.java - Entité Règlement
        - [ ] ClaimDocument.java - Entité Document de sinistre
    - [ ] Repositories :
        - [ ] ClaimRepository.java - Repository pour les sinistres
        - [ ] SettlementRepository.java - Repository pour les règlements
    - [ ] API :
        - [ ] ClaimController.java - API pour les sinistres
        - [ ] SettlementController.java - API pour les règlements
    - [ ] Cas d'utilisation :
        - [ ] FileClaim.java - Déclaration de sinistre
        - [ ] ProcessClaim.java - Traitement de sinistre
        - [ ] SettleClaim.java - Règlement de sinistre

- [ ] Implémentation du module insurance (gestion des produits d'assurance) :
    - [ ] Modèles de domaine :
        - [ ] Product.java - Entité Produit d'assurance
        - [ ] Coverage.java - Entité Garantie
        - [ ] Category.java - Entité Catégorie de produit
    - [ ] Repositories :
        - [ ] ProductRepository.java - Repository pour les produits
        - [ ] CoverageRepository.java - Repository pour les garanties
    - [ ] API :
        - [ ] ProductController.java - API pour les produits d'assurance
        - [ ] CoverageController.java - API pour les garanties
        - [ ] CategoryController.java - API pour les catégories de produits
    - [ ] Cas d'utilisation :
        - [ ] CreateProduct.java - Création de produit
        - [ ] ConfigureCoverage.java - Configuration de garantie

- [ ] Implémentation des modules IARD spécifiques :
    - [ ] Module auto (assurance automobile) :
        - [ ] Modèles de domaine :
            - [ ] AutoPolicy.java - Entité Police auto
            - [ ] Vehicle.java - Entité Véhicule
            - [ ] Driver.java - Entité Conducteur
            - [ ] AutoCoverage.java - Entité Garantie auto
            - [ ] AutoFormula.java - Entité Formule auto (pack de garanties)
            - [ ] VehicleUsage.java - Entité Usage du véhicule
            - [ ] Claim.java - Entité Sinistre auto
            - [ ] BonusMalus.java - Entité Coefficient bonus/malus
        - [ ] Repositories :
            - [ ] AutoPolicyRepository.java - Repository pour les polices auto
            - [ ] VehicleRepository.java - Repository pour les véhicules
            - [ ] DriverRepository.java - Repository pour les conducteurs
            - [ ] AutoCoverageRepository.java - Repository pour les garanties auto
            - [ ] AutoFormulaRepository.java - Repository pour les formules auto
        - [ ] Services de domaine :
            - [ ] AutoPricingService.java - Service de tarification auto
            - [ ] BonusMalusCalculationService.java - Service de calcul du bonus/malus
            - [ ] VehicleValuationService.java - Service d'évaluation de la valeur du véhicule
        - [ ] API :
            - [ ] AutoPolicyController.java - API pour les polices auto
            - [ ] VehicleController.java - API pour les véhicules
            - [ ] DriverController.java - API pour les conducteurs
            - [ ] AutoCoverageController.java - API pour les garanties auto
            - [ ] AutoFormulaController.java - API pour les formules auto
        - [ ] DTOs :
            - [ ] AutoPolicyDTO.java - DTO pour les polices auto
            - [ ] VehicleDTO.java - DTO pour les véhicules
            - [ ] DriverDTO.java - DTO pour les conducteurs
            - [ ] AutoCoverageDTO.java - DTO pour les garanties auto
            - [ ] AutoFormulaDTO.java - DTO pour les formules auto
        - [ ] Cas d'utilisation :
            - [ ] CreateAutoPolicy.java - Création d'une police auto
            - [ ] AddVehicle.java - Ajout d'un véhicule
            - [ ] AddDriver.java - Ajout d'un conducteur
            - [ ] SelectCoverages.java - Sélection des garanties
            - [ ] CalculateAutoPremium.java - Calcul de la prime d'assurance auto
            - [ ] ApplyBonusMalus.java - Application du coefficient bonus/malus
    - [ ] Module home (assurance habitation) :
        - [ ] Modèles de domaine :
            - [ ] HomePolicy.java - Entité Police habitation
            - [ ] Property.java - Entité Bien immobilier
            - [ ] PropertyType.java - Entité Type de bien (appartement, maison, etc.)
            - [ ] PropertyOccupancy.java - Entité Type d'occupation (propriétaire, locataire)
            - [ ] HomeCoverage.java - Entité Garantie habitation
            - [ ] HomeFormula.java - Entité Formule habitation (pack de garanties)
            - [ ] PropertyContent.java - Entité Contenu du bien (mobilier, objets de valeur)
            - [ ] PropertyRisk.java - Entité Risques spécifiques (inondation, vol, etc.)
        - [ ] Repositories :
            - [ ] HomePolicyRepository.java - Repository pour les polices habitation
            - [ ] PropertyRepository.java - Repository pour les biens immobiliers
            - [ ] HomeCoverageRepository.java - Repository pour les garanties habitation
            - [ ] HomeFormulaRepository.java - Repository pour les formules habitation
            - [ ] PropertyContentRepository.java - Repository pour le contenu du bien
        - [ ] Services de domaine :
            - [ ] HomePricingService.java - Service de tarification habitation
            - [ ] PropertyValuationService.java - Service d'évaluation de la valeur du bien
            - [ ] RiskAssessmentService.java - Service d'évaluation des risques
        - [ ] API :
            - [ ] HomePolicyController.java - API pour les polices habitation
            - [ ] PropertyController.java - API pour les biens immobiliers
            - [ ] HomeCoverageController.java - API pour les garanties habitation
            - [ ] HomeFormulaController.java - API pour les formules habitation
            - [ ] PropertyContentController.java - API pour le contenu du bien
        - [ ] DTOs :
            - [ ] HomePolicyDTO.java - DTO pour les polices habitation
            - [ ] PropertyDTO.java - DTO pour les biens immobiliers
            - [ ] HomeCoverageDTO.java - DTO pour les garanties habitation
            - [ ] HomeFormulaDTO.java - DTO pour les formules habitation
            - [ ] PropertyContentDTO.java - DTO pour le contenu du bien
        - [ ] Cas d'utilisation :
            - [ ] CreateHomePolicy.java - Création d'une police habitation
            - [ ] AddProperty.java - Ajout d'un bien immobilier
            - [ ] DeclarePropertyContent.java - Déclaration du contenu du bien
            - [ ] SelectHomeCoverages.java - Sélection des garanties habitation
            - [ ] CalculateHomePremium.java - Calcul de la prime d'assurance habitation
            - [ ] AssessPropertyRisks.java - Évaluation des risques du bien
    - [ ] Module travel (assurance voyage) :
        - [ ] Modèles de domaine :
            - [ ] TravelPolicy.java - Entité Police voyage
            - [ ] Trip.java - Entité Voyage
            - [ ] Traveler.java - Entité Voyageur
            - [ ] TravelCoverage.java - Entité Garantie voyage
            - [ ] TravelFormula.java - Entité Formule voyage (pack de garanties)
            - [ ] Destination.java - Entité Destination
            - [ ] TravelActivity.java - Entité Activité pratiquée pendant le voyage
            - [ ] MedicalHistory.java - Entité Antécédents médicaux
        - [ ] Repositories :
            - [ ] TravelPolicyRepository.java - Repository pour les polices voyage
            - [ ] TripRepository.java - Repository pour les voyages
            - [ ] TravelerRepository.java - Repository pour les voyageurs
            - [ ] TravelCoverageRepository.java - Repository pour les garanties voyage
            - [ ] TravelFormulaRepository.java - Repository pour les formules voyage
        - [ ] Services de domaine :
            - [ ] TravelPricingService.java - Service de tarification voyage
            - [ ] DestinationRiskService.java - Service d'évaluation des risques par destination
            - [ ] MedicalAssessmentService.java - Service d'évaluation médicale
        - [ ] API :
            - [ ] TravelPolicyController.java - API pour les polices voyage
            - [ ] TripController.java - API pour les voyages
            - [ ] TravelerController.java - API pour les voyageurs
            - [ ] TravelCoverageController.java - API pour les garanties voyage
            - [ ] TravelFormulaController.java - API pour les formules voyage
        - [ ] DTOs :
            - [ ] TravelPolicyDTO.java - DTO pour les polices voyage
            - [ ] TripDTO.java - DTO pour les voyages
            - [ ] TravelerDTO.java - DTO pour les voyageurs
            - [ ] TravelCoverageDTO.java - DTO pour les garanties voyage
            - [ ] TravelFormulaDTO.java - DTO pour les formules voyage
        - [ ] Cas d'utilisation :
            - [ ] CreateTravelPolicy.java - Création d'une police voyage
            - [ ] AddTrip.java - Ajout d'un voyage
            - [ ] AddTraveler.java - Ajout d'un voyageur
            - [ ] SelectTravelCoverages.java - Sélection des garanties voyage
            - [ ] CalculateTravelPremium.java - Calcul de la prime d'assurance voyage
            - [ ] AssessMedicalRisks.java - Évaluation des risques médicaux
    - [ ] Module misc (risques divers) :
        - [ ] Modèles de domaine :
            - [ ] MiscPolicy.java - Entité Police risques divers
            - [ ] InsuredItem.java - Entité Objet assuré
            - [ ] MiscCoverage.java - Entité Garantie risques divers
            - [ ] MiscFormula.java - Entité Formule risques divers (pack de garanties)
            - [ ] ItemCategory.java - Entité Catégorie d'objet (bijoux, électronique, etc.)
            - [ ] ItemValue.java - Entité Valeur de l'objet
            - [ ] ItemRisk.java - Entité Risques spécifiques à l'objet
        - [ ] Repositories :
            - [ ] MiscPolicyRepository.java - Repository pour les polices risques divers
            - [ ] InsuredItemRepository.java - Repository pour les objets assurés
            - [ ] MiscCoverageRepository.java - Repository pour les garanties risques divers
            - [ ] MiscFormulaRepository.java - Repository pour les formules risques divers
        - [ ] Services de domaine :
            - [ ] MiscPricingService.java - Service de tarification risques divers
            - [ ] ItemValuationService.java - Service d'évaluation de la valeur des objets
            - [ ] ItemRiskAssessmentService.java - Service d'évaluation des risques par objet
        - [ ] API :
            - [ ] MiscPolicyController.java - API pour les polices risques divers
            - [ ] InsuredItemController.java - API pour les objets assurés
            - [ ] MiscCoverageController.java - API pour les garanties risques divers
            - [ ] MiscFormulaController.java - API pour les formules risques divers
        - [ ] DTOs :
            - [ ] MiscPolicyDTO.java - DTO pour les polices risques divers
            - [ ] InsuredItemDTO.java - DTO pour les objets assurés
            - [ ] MiscCoverageDTO.java - DTO pour les garanties risques divers
            - [ ] MiscFormulaDTO.java - DTO pour les formules risques divers
        - [ ] Cas d'utilisation :
            - [ ] CreateMiscPolicy.java - Création d'une police risques divers
            - [ ] AddInsuredItem.java - Ajout d'un objet assuré
            - [ ] DeclareItemValue.java - Déclaration de la valeur d'un objet
            - [ ] SelectMiscCoverages.java - Sélection des garanties risques divers
            - [ ] CalculateMiscPremium.java - Calcul de la prime d'assurance risques divers
            - [ ] AssessItemRisks.java - Évaluation des risques par objet

### Phase 5 : Fonctionnalités avancées (Sprint 11-14)

- [ ] Implémentation du module reporting (reporting et analytics) :
    - [ ] Modèles de domaine :
        - [ ] Report.java - Entité Rapport
        - [ ] Dashboard.java - Entité Tableau de bord
        - [ ] ReportTemplate.java - Entité Modèle de rapport
    - [ ] Repositories :
        - [ ] ReportRepository.java - Repository pour les rapports
        - [ ] DashboardRepository.java - Repository pour les tableaux de bord
    - [ ] API :
        - [ ] ReportController.java - API pour les rapports
        - [ ] DashboardController.java - API pour les tableaux de bord
    - [ ] Cas d'utilisation :
        - [ ] GenerateReport.java - Génération de rapport
        - [ ] CreateDashboard.java - Création de tableau de bord
        - [ ] ExportReport.java - Exportation de rapport

- [ ] Implémentation du module customer (gestion de la relation client) :
    - [ ] Modèles de domaine :
        - [ ] Customer.java - Entité Client
        - [ ] Interaction.java - Entité Interaction
        - [ ] CustomerDocument.java - Entité Document client
    - [ ] Repositories :
        - [ ] CustomerRepository.java - Repository pour les clients
        - [ ] InteractionRepository.java - Repository pour les interactions
    - [ ] API :
        - [ ] CustomerController.java - API pour les clients
        - [ ] InteractionController.java - API pour les interactions
    - [ ] Cas d'utilisation :
        - [ ] CreateCustomer.java - Création de client
        - [ ] RecordInteraction.java - Enregistrement d'interaction

- [ ] Intégrations avec les systèmes externes :
    - [ ] Module d'intégration avec les comparateurs d'assurance
    - [ ] Module d'intégration avec les bases sectorielles
    - [ ] API pour les partenaires externes
    - [ ] Webhooks pour les notifications en temps réel

- [ ] Tests d'intégration complets :
    - [ ] Tests de bout en bout pour les flux principaux
    - [ ] Tests de performance et de charge
    - [ ] Tests de sécurité et de pénétration
    - [ ] Tests de régression automatiques

- [ ] Optimisation des performances :
    - [ ] Mise en place du caching pour les données fréquemment accédées
    - [ ] Optimisation des requêtes SQL complexes
    - [ ] Mise en place d'index spécifiques pour les requêtes fréquentes
    - [ ] Implémentation de la pagination et du lazy loading
    - [ ] Optimisation des performances de l'API

## 5. Modèle de données détaillé

### Table `organization_types`

```sql
CREATE TABLE organization_types
(
    id                  UUID PRIMARY KEY,
    code                VARCHAR(50)  NOT NULL UNIQUE,
    name                VARCHAR(100) NOT NULL,
    description         TEXT,
    hierarchy_level     INT          NOT NULL,
    can_see_children    BOOLEAN      NOT NULL DEFAULT TRUE,
    can_see_siblings    BOOLEAN      NOT NULL DEFAULT FALSE,
    default_permissions JSONB,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          UUID,
    updated_by          UUID
);

-- Insertion des types d'organisation spécifiques à l'assurance
INSERT INTO organization_types (id, code, name, hierarchy_level, can_see_children, can_see_siblings)
VALUES (gen_random_uuid(), 'INSURER', 'Assureur', 1, true, true),
       (gen_random_uuid(), 'COINSURER', 'Coassureur', 2, true, true),
       (gen_random_uuid(), 'BROKER', 'Courtier', 3, true, false),
       (gen_random_uuid(), 'GENERAL_AGENT', 'Agent Général', 3, true, false),
       (gen_random_uuid(), 'BUSINESS_PROVIDER', 'Apporteur d''Affaires', 4, false, false),
       (gen_random_uuid(), 'AGENCY', 'Agence', 4, false, false);
```

### Table `organizations`

```sql
CREATE TABLE organizations
(
    id                   UUID PRIMARY KEY,
    name                 VARCHAR(255) NOT NULL,
    code                 VARCHAR(50)  NOT NULL UNIQUE,
    organization_type_id UUID         NOT NULL,
    status               VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    parent_id            UUID         NULL,
    address              TEXT,
    phone                VARCHAR(20),
    email                VARCHAR(255),
    website              VARCHAR(255),
    logo_url             VARCHAR(255),
    description          TEXT,
    territory            JSONB, -- Définition du territoire géographique
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP,
    created_by           UUID,
    updated_by           UUID,
    organization_id      UUID,
    FOREIGN KEY (parent_id) REFERENCES organizations (id),
    FOREIGN KEY (organization_type_id) REFERENCES organization_types (id),
    CONSTRAINT org_not_own_parent CHECK (id != parent_id)
);
```

### Table `organization_contacts`

```sql
CREATE TABLE organization_contacts
(
    id                     UUID PRIMARY KEY,
    organization_id        UUID        NOT NULL,
    contact_type           VARCHAR(50) NOT NULL, -- PRIMARY, BILLING, TECHNICAL, etc.
    first_name             VARCHAR(100),
    last_name              VARCHAR(100),
    position               VARCHAR(100),
    email                  VARCHAR(255),
    phone                  VARCHAR(20),
    is_primary             BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at             TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMP,
    created_by             UUID,
    updated_by             UUID,
    organization_id_tenant UUID,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (organization_id, contact_type, is_primary)
);
```

### Table `organization_settings`

```sql
CREATE TABLE organization_settings
(
    id                     UUID PRIMARY KEY,
    organization_id        UUID         NOT NULL,
    setting_key            VARCHAR(100) NOT NULL,
    setting_value          JSONB        NOT NULL,
    setting_group          VARCHAR(50),
    created_at             TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMP,
    created_by             UUID,
    updated_by             UUID,
    organization_id_tenant UUID,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (organization_id, setting_key)
);
```

### Table `organization_relationships`

```sql
CREATE TABLE organization_relationships
(
    id                UUID PRIMARY KEY,
    source_org_id     UUID        NOT NULL,
    target_org_id     UUID        NOT NULL,
    relationship_type VARCHAR(50) NOT NULL,              -- PARENT_CHILD, COINSURANCE, BROKER_AGENT, etc.
    is_direct         BOOLEAN     NOT NULL DEFAULT TRUE, -- TRUE pour relation directe, FALSE pour relation calculée
    depth             INT         NOT NULL DEFAULT 1,    -- 1 pour relation directe, >1 pour relations indirectes
    status            VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    visibility_rules  JSONB,                             -- Règles de visibilité spécifiques à cette relation
    access_level      VARCHAR(50) NOT NULL DEFAULT 'STANDARD',
    start_date        DATE,
    end_date          DATE,
    -- Pas de stockage du chemin pour éviter la redondance, utilisation de requêtes récursives à la place
    metadata          JSONB,
    created_at        TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP,
    created_by        UUID,
    updated_by        UUID,
    organization_id   UUID,
    FOREIGN KEY (source_org_id) REFERENCES organizations (id),
    FOREIGN KEY (target_org_id) REFERENCES organizations (id),
    UNIQUE (source_org_id, target_org_id, relationship_type, is_direct),
    CONSTRAINT diff_orgs CHECK (source_org_id != target_org_id)
);

-- Index pour optimiser les requêtes hiérarchiques
CREATE INDEX idx_org_rel_source_type ON organization_relationships (source_org_id, relationship_type, is_direct);
CREATE INDEX idx_org_rel_target_type ON organization_relationships (target_org_id, relationship_type, is_direct);
CREATE INDEX idx_org_rel_depth ON organization_relationships (relationship_type, depth);
```

-- Exemple de requête récursive pour naviguer dans la hiérarchie

```sql
WITH RECURSIVE org_hierarchy AS (
    -- Cas de base: relations directes
    SELECT source_org_id, target_org_id, relationship_type, depth, ARRAY [source_org_id, target_org_id] AS path
    FROM organization_relationships
    WHERE source_org_id = :rootOrgId
      AND relationship_type = 'PARENT_CHILD'
      AND is_direct = TRUE

    UNION ALL

    -- Cas récursif: ajouter les enfants des organisations déjà trouvées
    SELECT r.source_org_id, r.target_org_id, r.relationship_type, h.depth + 1, h.path || r.target_org_id
    FROM organization_relationships r
             JOIN org_hierarchy h ON r.source_org_id = h.target_org_id
    WHERE r.relationship_type = 'PARENT_CHILD'
      AND r.is_direct = TRUE)
SELECT *
FROM org_hierarchy;
```

### Table `users`

```sql
CREATE TABLE users
(
    id                        UUID PRIMARY KEY,
    username                  VARCHAR(50)  NOT NULL UNIQUE,
    email                     VARCHAR(255) NOT NULL UNIQUE,
    password_hash             VARCHAR(255) NOT NULL,
    first_name                VARCHAR(100),
    last_name                 VARCHAR(100),
    phone                     VARCHAR(20),
    status                    VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    locked                    BOOLEAN      NOT NULL DEFAULT FALSE,
    failed_login_attempts     INT          NOT NULL DEFAULT 0,
    last_login_at             TIMESTAMP,
    profile_picture_url       VARCHAR(255),
    created_at                TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMP,
    created_by                UUID,
    updated_by                UUID,
    organization_id           UUID,
    account_non_expired       BOOLEAN      NOT NULL DEFAULT TRUE,
    account_non_locked        BOOLEAN      NOT NULL DEFAULT TRUE,
    credentials_non_expired   BOOLEAN      NOT NULL DEFAULT TRUE,
    enabled                   BOOLEAN      NOT NULL DEFAULT TRUE,
    last_password_change_date TIMESTAMP
);
```

### Table `user_organizations`

```sql
CREATE TABLE user_organizations
(
    id                     UUID PRIMARY KEY,
    user_id                UUID        NOT NULL,
    organization_id        UUID        NOT NULL,
    role_id                UUID        NOT NULL,
    is_primary             BOOLEAN     NOT NULL DEFAULT FALSE,
    access_level           VARCHAR(50) NOT NULL DEFAULT 'STANDARD',
    created_at             TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMP,
    created_by             UUID,
    updated_by             UUID,
    organization_id_tenant UUID,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    UNIQUE (user_id, organization_id)
);
```

### Table `roles`

```sql
CREATE TABLE roles
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    description     TEXT,
    system_defined  BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    UNIQUE (name, organization_id)
);
```

### Table `permissions`

```sql
CREATE TABLE permissions
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT,
    resource_type   VARCHAR(50)  NOT NULL,
    action          VARCHAR(50)  NOT NULL,
    system_defined  BOOLEAN      NOT NULL DEFAULT FALSE,
    scope           VARCHAR(50)  NOT NULL DEFAULT 'SELF', -- SELF, CHILDREN, ALL
    hierarchy_level INT          NOT NULL DEFAULT 0,      -- 0 = all levels, 1+ = specific level
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    UNIQUE (resource_type, action, scope)
);
```

### Table `role_permissions`

```sql
CREATE TABLE role_permissions
(
    id              UUID PRIMARY KEY,
    role_id         UUID      NOT NULL,
    permission_id   UUID      NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (permission_id) REFERENCES permissions (id),
    UNIQUE (role_id, permission_id)
);
```

### Table `user_roles`

```sql
CREATE TABLE user_roles
(
    id              UUID PRIMARY KEY,
    user_id         UUID      NOT NULL,
    role_id         UUID      NOT NULL,
    organization_id UUID      NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (user_id, role_id, organization_id)
);
```

### Table `refresh_tokens`

```sql
CREATE TABLE refresh_tokens
(
    id              UUID PRIMARY KEY,
    token           VARCHAR(255) NOT NULL UNIQUE,
    user_id         UUID         NOT NULL,
    expires_at      TIMESTAMP    NOT NULL,
    revoked         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
```

### Table `api_keys`

```sql
CREATE TABLE api_keys
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    key_hash        VARCHAR(255) NOT NULL,
    organization_id UUID         NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    expires_at      TIMESTAMP,
    last_used_at    TIMESTAMP,
    permissions     JSONB,
    allowed_ips     TEXT[],
    rate_limit      INT,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE audit_logs
(
    id              UUID PRIMARY KEY,
    event_type      VARCHAR(50) NOT NULL,
    entity_type     VARCHAR(50) NOT NULL,
    entity_id       UUID,
    action          VARCHAR(50) NOT NULL,
    user_id         UUID,
    organization_id UUID,
    timestamp       TIMESTAMP   NOT NULL DEFAULT NOW(),
    ip_address      VARCHAR(50),
    user_agent      TEXT,
    old_values      JSONB,
    new_values      JSONB,
    additional_data JSONB,
    severity        VARCHAR(20) NOT NULL DEFAULT 'INFO',
    is_success      BOOLEAN     NOT NULL DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE INDEX idx_audit_logs_entity ON audit_logs (entity_type, entity_id);
CREATE INDEX idx_audit_logs_user ON audit_logs (user_id);
CREATE INDEX idx_audit_logs_org ON audit_logs (organization_id);
CREATE INDEX idx_audit_logs_timestamp ON audit_logs (timestamp);
CREATE INDEX idx_audit_logs_action ON audit_logs (action);
CREATE INDEX idx_audit_logs_severity ON audit_logs (severity);
```

### Tables pour les modules métier assurantiels

```sql
CREATE TABLE product_categories
(
    id                 UUID PRIMARY KEY,
    code               VARCHAR(50)  NOT NULL UNIQUE,
    name               VARCHAR(255) NOT NULL,
    description        TEXT,
    parent_category_id UUID,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         UUID,
    updated_by         UUID,
    organization_id    UUID,
    FOREIGN KEY (parent_category_id) REFERENCES product_categories (id)
);

-- Insertion des catégories de produits IARD
INSERT INTO product_categories (id, code, name)
VALUES (gen_random_uuid(), 'AUTO', 'Assurance Automobile'),
       (gen_random_uuid(), 'HOME', 'Assurance Habitation'),
       (gen_random_uuid(), 'TRAVEL', 'Assurance Voyage'),
       (gen_random_uuid(), 'MISC_RISKS', 'Risques Divers');

CREATE TABLE insurance_products
(
    id                  UUID PRIMARY KEY,
    code                VARCHAR(50)  NOT NULL UNIQUE,
    name                VARCHAR(255) NOT NULL,
    description         TEXT,
    product_category_id UUID         NOT NULL,
    product_type        VARCHAR(50)  NOT NULL,
    status              VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    settings            JSONB,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          UUID,
    updated_by          UUID,
    organization_id     UUID         NOT NULL,
    FOREIGN KEY (product_category_id) REFERENCES product_categories (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE policies
(
    id              UUID PRIMARY KEY,
    policy_number   VARCHAR(50) NOT NULL UNIQUE,
    product_id      UUID        NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    start_date      DATE        NOT NULL,
    end_date        DATE        NOT NULL,
    premium         DECIMAL(15, 2),
    customer_id     UUID,
    underwriter_id  UUID,
    broker_id       UUID,
    agent_id        UUID,
    data            JSONB,
    created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID        NOT NULL,
    FOREIGN KEY (product_id) REFERENCES insurance_products (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE policy_organizations
(
    id                     UUID PRIMARY KEY,
    policy_id              UUID        NOT NULL,
    organization_id        UUID        NOT NULL,
    role_type              VARCHAR(50) NOT NULL, -- INSURER, BROKER, AGENT, etc.
    commission_rate        DECIMAL(5, 2),
    created_at             TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMP,
    created_by             UUID,
    updated_by             UUID,
    organization_id_tenant UUID,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (policy_id, organization_id, role_type)
);

CREATE TABLE claims
(
    id               UUID PRIMARY KEY,
    claim_number     VARCHAR(50) NOT NULL UNIQUE,
    policy_id        UUID        NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    incident_date    DATE        NOT NULL,
    report_date      DATE        NOT NULL,
    description      TEXT,
    estimated_amount DECIMAL(15, 2),
    settled_amount   DECIMAL(15, 2),
    data             JSONB,
    created_at       TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID,
    organization_id  UUID        NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

-- Tables spécifiques pour l'assurance automobile
CREATE TABLE auto_policies
(
    id              UUID PRIMARY KEY,
    policy_id       UUID          NOT NULL,
    bonus_malus     DECIMAL(5, 2) NOT NULL DEFAULT 1.0,
    effective_date  DATE          NOT NULL,
    expiry_date     DATE          NOT NULL,
    data            JSONB,
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID          NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE vehicles
(
    id                  UUID PRIMARY KEY,
    auto_policy_id      UUID        NOT NULL,
    registration_number VARCHAR(20) NOT NULL,
    make                VARCHAR(50) NOT NULL,
    model               VARCHAR(50) NOT NULL,
    year                INTEGER     NOT NULL,
    vin                 VARCHAR(50),
    usage_type          VARCHAR(30) NOT NULL,
    mileage             INTEGER,
    value               DECIMAL(15, 2),
    engine_power        INTEGER,
    fuel_type           VARCHAR(20),
    data                JSONB,
    created_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          UUID,
    updated_by          UUID,
    organization_id     UUID        NOT NULL,
    FOREIGN KEY (auto_policy_id) REFERENCES auto_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE drivers
(
    id              UUID PRIMARY KEY,
    auto_policy_id  UUID         NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    birth_date      DATE         NOT NULL,
    license_number  VARCHAR(50)  NOT NULL,
    license_date    DATE         NOT NULL,
    is_primary      BOOLEAN      NOT NULL DEFAULT FALSE,
    data            JSONB,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID         NOT NULL,
    FOREIGN KEY (auto_policy_id) REFERENCES auto_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE auto_coverages
(
    id              UUID PRIMARY KEY,
    auto_policy_id  UUID        NOT NULL,
    coverage_type   VARCHAR(50) NOT NULL,
    coverage_limit  DECIMAL(15, 2),
    deductible      DECIMAL(15, 2),
    premium         DECIMAL(15, 2),
    is_mandatory    BOOLEAN     NOT NULL DEFAULT FALSE,
    data            JSONB,
    created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID        NOT NULL,
    FOREIGN KEY (auto_policy_id) REFERENCES auto_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

-- Tables spécifiques pour l'assurance habitation
CREATE TABLE home_policies
(
    id              UUID PRIMARY KEY,
    policy_id       UUID      NOT NULL,
    effective_date  DATE      NOT NULL,
    expiry_date     DATE      NOT NULL,
    data            JSONB,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID      NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE properties
(
    id                  UUID PRIMARY KEY,
    home_policy_id      UUID         NOT NULL,
    address_line1       VARCHAR(255) NOT NULL,
    address_line2       VARCHAR(255),
    city                VARCHAR(100) NOT NULL,
    state               VARCHAR(100),
    postal_code         VARCHAR(20)  NOT NULL,
    country             VARCHAR(100) NOT NULL,
    property_type       VARCHAR(50)  NOT NULL,
    occupancy_type      VARCHAR(50)  NOT NULL,
    year_built          INTEGER,
    living_area         DECIMAL(10, 2),
    number_of_rooms     INTEGER,
    number_of_floors    INTEGER,
    has_alarm_system    BOOLEAN      NOT NULL DEFAULT FALSE,
    has_fire_protection BOOLEAN      NOT NULL DEFAULT FALSE,
    data                JSONB,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          UUID,
    updated_by          UUID,
    organization_id     UUID         NOT NULL,
    FOREIGN KEY (home_policy_id) REFERENCES home_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE property_contents
(
    id              UUID PRIMARY KEY,
    home_policy_id  UUID           NOT NULL,
    content_type    VARCHAR(50)    NOT NULL,
    value           DECIMAL(15, 2) NOT NULL,
    description     TEXT,
    data            JSONB,
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID           NOT NULL,
    FOREIGN KEY (home_policy_id) REFERENCES home_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE home_coverages
(
    id              UUID PRIMARY KEY,
    home_policy_id  UUID        NOT NULL,
    coverage_type   VARCHAR(50) NOT NULL,
    coverage_limit  DECIMAL(15, 2),
    deductible      DECIMAL(15, 2),
    premium         DECIMAL(15, 2),
    is_mandatory    BOOLEAN     NOT NULL DEFAULT FALSE,
    data            JSONB,
    created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID        NOT NULL,
    FOREIGN KEY (home_policy_id) REFERENCES home_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

-- Tables spécifiques pour l'assurance voyage
CREATE TABLE travel_policies
(
    id              UUID PRIMARY KEY,
    policy_id       UUID      NOT NULL,
    effective_date  DATE      NOT NULL,
    expiry_date     DATE      NOT NULL,
    data            JSONB,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID      NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE trips
(
    id                  UUID PRIMARY KEY,
    travel_policy_id    UUID         NOT NULL,
    destination_country VARCHAR(100) NOT NULL,
    departure_date      DATE         NOT NULL,
    return_date         DATE         NOT NULL,
    purpose             VARCHAR(50)  NOT NULL,
    data                JSONB,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          UUID,
    updated_by          UUID,
    organization_id     UUID         NOT NULL,
    FOREIGN KEY (travel_policy_id) REFERENCES travel_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE travelers
(
    id                    UUID PRIMARY KEY,
    travel_policy_id      UUID         NOT NULL,
    first_name            VARCHAR(100) NOT NULL,
    last_name             VARCHAR(100) NOT NULL,
    birth_date            DATE         NOT NULL,
    passport_number       VARCHAR(50),
    nationality           VARCHAR(100),
    has_medical_condition BOOLEAN      NOT NULL DEFAULT FALSE,
    medical_details       TEXT,
    data                  JSONB,
    created_at            TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMP,
    created_by            UUID,
    updated_by            UUID,
    organization_id       UUID         NOT NULL,
    FOREIGN KEY (travel_policy_id) REFERENCES travel_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE travel_coverages
(
    id               UUID PRIMARY KEY,
    travel_policy_id UUID        NOT NULL,
    coverage_type    VARCHAR(50) NOT NULL,
    coverage_limit   DECIMAL(15, 2),
    deductible       DECIMAL(15, 2),
    premium          DECIMAL(15, 2),
    is_mandatory     BOOLEAN     NOT NULL DEFAULT FALSE,
    data             JSONB,
    created_at       TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       UUID,
    updated_by       UUID,
    organization_id  UUID        NOT NULL,
    FOREIGN KEY (travel_policy_id) REFERENCES travel_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

-- Tables spécifiques pour l'assurance risques divers
CREATE TABLE misc_policies
(
    id              UUID PRIMARY KEY,
    policy_id       UUID      NOT NULL,
    effective_date  DATE      NOT NULL,
    expiry_date     DATE      NOT NULL,
    data            JSONB,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID      NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE insured_items
(
    id              UUID PRIMARY KEY,
    misc_policy_id  UUID           NOT NULL,
    item_type       VARCHAR(50)    NOT NULL,
    description     TEXT           NOT NULL,
    value           DECIMAL(15, 2) NOT NULL,
    purchase_date   DATE,
    serial_number   VARCHAR(100),
    brand           VARCHAR(100),
    model           VARCHAR(100),
    data            JSONB,
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID           NOT NULL,
    FOREIGN KEY (misc_policy_id) REFERENCES misc_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE misc_coverages
(
    id              UUID PRIMARY KEY,
    misc_policy_id  UUID        NOT NULL,
    coverage_type   VARCHAR(50) NOT NULL,
    coverage_limit  DECIMAL(15, 2),
    deductible      DECIMAL(15, 2),
    premium         DECIMAL(15, 2),
    is_mandatory    BOOLEAN     NOT NULL DEFAULT FALSE,
    data            JSONB,
    created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      UUID,
    updated_by      UUID,
    organization_id UUID        NOT NULL,
    FOREIGN KEY (misc_policy_id) REFERENCES misc_policies (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id)
);

CREATE TABLE claim_organizations
(
    id                     UUID PRIMARY KEY,
    claim_id               UUID        NOT NULL,
    organization_id        UUID        NOT NULL,
    role_type              VARCHAR(50) NOT NULL, -- INSURER, BROKER, AGENT, etc.
    created_at             TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMP,
    created_by             UUID,
    updated_by             UUID,
    organization_id_tenant UUID,
    FOREIGN KEY (claim_id) REFERENCES claims (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    UNIQUE (claim_id, organization_id, role_type)
);
```

## 6. API Endpoints

### Authentication API

- `POST /api/auth/login` - Authentification utilisateur (retourne un JWT Bearer token)
- `POST /api/auth/refresh` - Rafraîchissement du token
- `POST /api/auth/logout` - Déconnexion
- `GET /api/auth/me` - Informations sur l'utilisateur connecté
- `GET /api/auth/me/organizations` - Liste des organisations auxquelles l'utilisateur a accès
- `POST /api/auth/switch-organization/{orgId}` - Changer le contexte organisationnel de l'utilisateur

### API Keys Management

- `GET /api/apikeys` - Liste des API Keys de l'organisation
- `POST /api/apikeys` - Création d'une nouvelle API Key
- `PUT /api/apikeys/{id}` - Mise à jour des permissions d'une API Key
- `DELETE /api/apikeys/{id}` - Révocation d'une API Key
- `POST /api/apikeys/{id}/rotate` - Rotation d'une API Key (génération d'une nouvelle clé avec période de transition)

### Organizations API

- `GET /api/organizations` - Liste des organisations (filtrée selon le niveau hiérarchique de l'utilisateur)
- `GET /api/organizations/{id}` - Détails d'une organisation
- `POST /api/organizations` - Création d'une organisation
- `PUT /api/organizations/{id}` - Mise à jour d'une organisation
- `DELETE /api/organizations/{id}` - Suppression d'une organisation
- `GET /api/organizations/{id}/children` - Liste des organisations enfants
- `GET /api/organizations/{id}/users` - Liste des utilisateurs d'une organisation
- `GET /api/organizations/{id}/hierarchy` - Affiche la hiérarchie complète de l'organisation
- `GET /api/organizations/{id}/hierarchy/visual` - Représentation visuelle de la hiérarchie
- `GET /api/organizations/types` - Liste des types d'organisations disponibles
- `GET /api/organizations/{id}/policies` - Liste des polices d'assurance liées à l'organisation
- `GET /api/organizations/{id}/claims` - Liste des sinistres liés à l'organisation
- `POST /api/organizations/{id}/delegate/{targetId}` - Délègue des droits à une autre organisation
- `GET /api/organizations/{id}/related` - Liste des organisations liées (toutes relations confondues)
- `POST /api/organizations/{id}/relate/{targetId}` - Établit une relation spécifique avec une autre organisation
- `GET /api/organizations/{id}/visibility-settings` - Obtient les paramètres de visibilité
- `PUT /api/organizations/{id}/visibility-settings` - Met à jour les paramètres de visibilité

### Organization Contacts API

- `GET /api/organizations/{id}/contacts` - Liste des contacts d'une organisation
- `GET /api/organizations/{id}/contacts/{contactId}` - Détails d'un contact
- `POST /api/organizations/{id}/contacts` - Ajout d'un contact à une organisation
- `PUT /api/organizations/{id}/contacts/{contactId}` - Mise à jour d'un contact
- `DELETE /api/organizations/{id}/contacts/{contactId}` - Suppression d'un contact
- `POST /api/organizations/{id}/contacts/{contactId}/set-primary` - Définit un contact comme principal pour un type

### Organization Settings API

- `GET /api/organizations/{id}/settings` - Liste des paramètres d'une organisation
- `GET /api/organizations/{id}/settings/{key}` - Valeur d'un paramètre spécifique
- `PUT /api/organizations/{id}/settings/{key}` - Mise à jour d'un paramètre
- `DELETE /api/organizations/{id}/settings/{key}` - Suppression d'un paramètre
- `GET /api/organizations/{id}/settings/group/{group}` - Paramètres par groupe

### Users API

- `GET /api/users` - Liste des utilisateurs
- `GET /api/users/{id}` - Détails d'un utilisateur
- `POST /api/users` - Création d'un utilisateur
- `PUT /api/users/{id}` - Mise à jour d'un utilisateur
- `DELETE /api/users/{id}` - Suppression d'un utilisateur
- `GET /api/users/{id}/roles` - Liste des rôles d'un utilisateur
- `POST /api/users/{id}/roles` - Attribution d'un rôle à un utilisateur
- `DELETE /api/users/{id}/roles/{roleId}` - Suppression d'un rôle d'un utilisateur

### Roles API

- `GET /api/roles` - Liste des rôles
- `GET /api/roles/{id}` - Détails d'un rôle
- `POST /api/roles` - Création d'un rôle
- `PUT /api/roles/{id}` - Mise à jour d'un rôle
- `DELETE /api/roles/{id}` - Suppression d'un rôle
- `GET /api/roles/{id}/permissions` - Liste des permissions d'un rôle
- `POST /api/roles/{id}/permissions` - Attribution d'une permission à un rôle
- `DELETE /api/roles/{id}/permissions/{permissionId}` - Suppression d'une permission d'un rôle

### Permissions API

- `GET /api/permissions` - Liste des permissions
- `GET /api/permissions/{id}` - Détails d'une permission
- `POST /api/permissions` - Création d'une permission
- `PUT /api/permissions/{id}` - Mise à jour d'une permission
- `DELETE /api/permissions/{id}` - Suppression d'une permission

### Audit API

- `GET /api/audit/logs` - Liste des logs d'audit avec filtrage
- `GET /api/audit/logs/{id}` - Détails d'un log d'audit
- `GET /api/audit/logs/entity/{entityType}/{entityId}` - Logs d'audit pour une entité spécifique
- `GET /api/audit/logs/user/{userId}` - Logs d'audit pour un utilisateur spécifique
- `GET /api/audit/reports/security` - Rapport de sécurité basé sur les logs d'audit
- `GET /api/audit/reports/activity` - Rapport d'activité basé sur les logs d'audit
- `GET /api/audit/alerts` - Liste des alertes générées par le système d'audit

### Product Categories API

- `GET /api/product-categories` - Liste des catégories de produits
- `GET /api/product-categories/{id}` - Détails d'une catégorie de produits
- `POST /api/product-categories` - Création d'une catégorie de produits
- `PUT /api/product-categories/{id}` - Mise à jour d'une catégorie de produits
- `DELETE /api/product-categories/{id}` - Suppression d'une catégorie de produits

### IARD Products API

- `GET /api/products/auto` - Liste des produits d'assurance auto
- `GET /api/products/home` - Liste des produits d'assurance habitation
- `GET /api/products/travel` - Liste des produits d'assurance voyage
- `GET /api/products/misc` - Liste des produits d'assurance risques divers
- `POST /api/products/auto` - Création d'un produit d'assurance auto
- `POST /api/products/home` - Création d'un produit d'assurance habitation
- `POST /api/products/travel` - Création d'un produit d'assurance voyage
- `POST /api/products/misc` - Création d'un produit d'assurance risques divers

## 7. Suivi d'avancement

| Tâche                                          | Statut  | Date de début | Date de fin | Responsable   |
|------------------------------------------------|---------|---------------|-------------|---------------|
| Configuration initiale du projet               | Terminé | 01/01/2023    | 05/01/2023  | Équipe DevOps |
| Structure des packages                         | À faire | -             | -           | -             |
| Entités de base                                | À faire | -             | -           | -             |
| Configuration de la base de données            | À faire | -             | -           | -             |
| Migrations Flyway                              | À faire | -             | -           | -             |
| Repositories de base                           | À faire | -             | -           | -             |
| Système d'authentification JWT                 | À faire | -             | -           | -             |
| Gestion des refresh tokens                     | À faire | -             | -           | -             |
| Contrôle d'accès basé sur les rôles            | À faire | -             | -           | -             |
| API de gestion des utilisateurs                | À faire | -             | -           | -             |
| API de gestion des rôles et permissions        | À faire | -             | -           | -             |
| API de gestion des organisations               | À faire | -             | -           | -             |
| Hiérarchie des organisations                   | À faire | -             | -           | -             |
| Association des utilisateurs aux organisations | À faire | -             | -           | -             |
| Isolation des données par organisation         | À faire | -             | -           | -             |

## 8. Bonnes pratiques de développement

### Conventions de nommage

- **Packages** : Utiliser la convention `com.devolution.saas.[module].[couche]`
- **Classes** : Utiliser le PascalCase (ex: `OrganizationService`)
- **Méthodes et variables** : Utiliser le camelCase (ex: `findByOrganizationId`)
- **Constantes** : Utiliser le SNAKE_CASE en majuscules (ex: `MAX_LOGIN_ATTEMPTS`)
- **Entités hiérarchiques** : Préfixer les classes liées à la hiérarchie avec `Hierarchical` (ex:
  `HierarchicalPermissionEvaluator`)

### Structure des classes

- Suivre les principes SOLID
- Utiliser l'injection de dépendances par constructeur
- Éviter les classes utilitaires avec méthodes statiques quand possible
- Préférer les interfaces aux classes concrètes pour les dépendances

### Tests

- Écrire des tests unitaires pour chaque classe
- Écrire des tests d'intégration pour les flux principaux
- Viser une couverture de code d'au moins 80%
- Utiliser des mocks pour isoler les tests unitaires

### Documentation

- Documenter toutes les API avec Swagger/OpenAPI
- Ajouter des commentaires JavaDoc pour les classes et méthodes publiques
- Maintenir à jour la documentation technique
- Documenter les décisions d'architecture importantes

## 9. Environnements et déploiement

### Environnements

- **Développement** : Pour le développement actif
- **Test** : Pour les tests d'intégration et fonctionnels
- **Staging** : Pour les tests de pré-production
- **Production** : Environnement de production

### CI/CD

- Utiliser GitHub Actions pour l'intégration continue
- Exécuter les tests automatiquement à chaque push
- Déployer automatiquement sur l'environnement de test après les tests réussis
- Déployer manuellement sur les environnements staging et production

### Monitoring et logging

- Utiliser Spring Actuator pour le monitoring
- Configurer Logback pour la journalisation
- Mettre en place ELK Stack pour la centralisation des logs
- Configurer des alertes pour les erreurs critiques

## 10. Risques et mitigations

| Risque                                                                 | Impact | Probabilité | Mitigation                                                                                   |
|------------------------------------------------------------------------|--------|-------------|----------------------------------------------------------------------------------------------|
| Problèmes de performance avec l'approche multi-tenant                  | Élevé  | Moyenne     | Optimiser les requêtes, mettre en place du caching, monitorer les performances               |
| Failles de sécurité dans l'isolation des données                       | Élevé  | Faible      | Tests de pénétration réguliers, audits de sécurité                                           |
| Complexité de la gestion des permissions                               | Moyen  | Élevée      | Documentation claire, tests exhaustifs, interface utilisateur intuitive                      |
| Évolutivité limitée du monolithe                                       | Moyen  | Moyenne     | Conception modulaire dès le départ, préparation à la migration vers des microservices        |
| Dépendances obsolètes ou vulnérables                                   | Moyen  | Élevée      | Analyse régulière des dépendances, mises à jour planifiées                                   |
| Complexité de la hiérarchie assurantielle                              | Élevé  | Élevée      | Modélisation précise des relations, tests exhaustifs avec cas réels, documentation détaillée |
| Difficulté d'adaptation aux spécificités de chaque type d'organisation | Moyen  | Élevée      | Architecture flexible, paramétrage avancé, possibilité d'extensions spécifiques              |
| Confusion des utilisateurs face à la navigation contextuelle           | Moyen  | Moyenne     | UX soignée, indicateurs visuels clairs du contexte, tutoriels intégrés                       |

## 11. Catalogue des cas d'utilisation (Use Cases)

### Use Cases - Gestion des organisations

| ID      | Cas d'utilisation                           | Description                                                                   | Acteurs                                               | Préconditions                                                                    | Postconditions                                             |
|---------|---------------------------------------------|-------------------------------------------------------------------------------|-------------------------------------------------------|----------------------------------------------------------------------------------|------------------------------------------------------------|
| ORG-001 | Création d'une organisation                 | Créer une nouvelle organisation dans le système avec ses informations de base | Administrateur système, Administrateur d'organisation | Utilisateur authentifié avec droits suffisants                                   | Organisation créée et active dans le système               |
| ORG-002 | Création d'une filiale                      | Créer une organisation enfant liée à une organisation parent                  | Administrateur d'organisation                         | Organisation parent existante, Utilisateur avec droits sur l'organisation parent | Filiale créée et relation parent-enfant établie            |
| ORG-003 | Établissement d'une relation de courtage    | Établir une relation entre un assureur et un courtier                         | Administrateur d'assureur                             | Organisations assureur et courtier existantes                                    | Relation de courtage établie avec paramètres de visibilité |
| ORG-004 | Établissement d'une relation de coassurance | Configurer une relation de coassurance entre plusieurs assureurs              | Administrateur d'assureur                             | Organisations assureurs existantes                                               | Relation de coassurance établie avec règles de partage     |
| ORG-005 | Gestion des contacts d'organisation         | Ajouter, modifier ou supprimer des contacts pour une organisation             | Gestionnaire d'organisation                           | Organisation existante                                                           | Contacts d'organisation mis à jour                         |
| ORG-006 | Configuration des paramètres d'organisation | Définir des paramètres spécifiques pour une organisation                      | Administrateur d'organisation                         | Organisation existante                                                           | Paramètres d'organisation configurés                       |
| ORG-007 | Visualisation de la hiérarchie              | Afficher la structure hiérarchique complète d'une organisation                | Tout utilisateur avec accès                           | Organisation existante                                                           | Hiérarchie affichée selon les droits de l'utilisateur      |
| ORG-008 | Délégation de droits                        | Déléguer des droits spécifiques d'une organisation à une autre                | Administrateur d'organisation                         | Organisations source et cible existantes                                         | Droits délégués selon les paramètres spécifiés             |

### Use Cases - Gestion des utilisateurs et sécurité

| ID      | Cas d'utilisation                      | Description                                                           | Acteurs                                               | Préconditions                                     | Postconditions                                              |
|---------|----------------------------------------|-----------------------------------------------------------------------|-------------------------------------------------------|---------------------------------------------------|-------------------------------------------------------------|
| SEC-001 | Authentification utilisateur           | Authentifier un utilisateur avec JWT                                  | Tout utilisateur                                      | Utilisateur enregistré                            | Utilisateur authentifié, JWT généré                         |
| SEC-002 | Création d'un utilisateur              | Créer un nouvel utilisateur dans le système                           | Administrateur système, Administrateur d'organisation | Droits suffisants                                 | Utilisateur créé                                            |
| SEC-003 | Attribution de rôles                   | Attribuer un ou plusieurs rôles à un utilisateur                      | Administrateur d'organisation                         | Utilisateur et rôles existants                    | Rôles attribués à l'utilisateur                             |
| SEC-004 | Création d'une API Key                 | Générer une nouvelle API Key pour l'intégration externe               | Administrateur technique                              | Organisation existante                            | API Key générée avec permissions définies                   |
| SEC-005 | Changement de contexte organisationnel | Changer l'organisation active pour un utilisateur multi-organisations | Utilisateur                                           | Utilisateur appartenant à plusieurs organisations | Contexte organisationnel changé                             |
| SEC-006 | Gestion des permissions                | Configurer les permissions pour un rôle                               | Administrateur d'organisation                         | Rôle existant                                     | Permissions mises à jour pour le rôle                       |
| SEC-007 | Audit des actions utilisateur          | Consulter l'historique des actions d'un utilisateur                   | Administrateur, Auditeur                              | Actions enregistrées                              | Journal d'audit affiché                                     |
| SEC-008 | Rotation d'API Key                     | Remplacer une API Key existante par une nouvelle                      | Administrateur technique                              | API Key existante                                 | Nouvelle API Key générée, ancienne en période de transition |

### Use Cases - Gestion des polices d'assurance

| ID      | Cas d'utilisation                        | Description                                                   | Acteurs                     | Préconditions                         | Postconditions                                                  |
|---------|------------------------------------------|---------------------------------------------------------------|-----------------------------|---------------------------------------|-----------------------------------------------------------------|
| POL-001 | Création d'une police                    | Créer une nouvelle police d'assurance                         | Gestionnaire de contrats    | Produit d'assurance existant          | Police créée                                                    |
| POL-002 | Association d'organisations à une police | Lier différentes organisations à une police selon leurs rôles | Gestionnaire de contrats    | Police et organisations existantes    | Organisations associées à la police avec leurs rôles respectifs |
| POL-003 | Renouvellement de police                 | Renouveler une police existante                               | Gestionnaire de contrats    | Police existante proche de l'échéance | Police renouvelée avec nouvelles dates                          |
| POL-004 | Avenant de modification                  | Créer un avenant pour modifier une police existante           | Gestionnaire de contrats    | Police existante                      | Avenant créé et appliqué à la police                            |
| POL-005 | Consultation des polices                 | Consulter les polices selon le contexte organisationnel       | Tout utilisateur avec accès | Utilisateur authentifié               | Polices affichées selon les droits de visibilité                |

### Use Cases - Assurance automobile

| ID       | Cas d'utilisation          | Description                                            | Acteurs                  | Préconditions                                      | Postconditions                        |
|----------|----------------------------|--------------------------------------------------------|--------------------------|----------------------------------------------------|---------------------------------------|
| AUTO-001 | Création d'une police auto | Créer une nouvelle police d'assurance automobile       | Gestionnaire de contrats | Produit d'assurance auto existant                  | Police auto créée                     |
| AUTO-002 | Ajout d'un véhicule        | Ajouter un véhicule à une police auto                  | Gestionnaire de contrats | Police auto existante                              | Véhicule ajouté à la police           |
| AUTO-003 | Ajout d'un conducteur      | Ajouter un conducteur à une police auto                | Gestionnaire de contrats | Police auto existante                              | Conducteur ajouté à la police         |
| AUTO-004 | Sélection des garanties    | Sélectionner les garanties pour une police auto        | Gestionnaire de contrats | Police auto existante                              | Garanties sélectionnées et appliquées |
| AUTO-005 | Calcul de prime            | Calculer la prime d'assurance pour une police auto     | Système                  | Police auto avec véhicule, conducteur et garanties | Prime calculée                        |
| AUTO-006 | Application du bonus/malus | Appliquer le coefficient bonus/malus à une police auto | Système                  | Historique de sinistres disponible                 | Coefficient bonus/malus appliqué      |

### Use Cases - Assurance habitation

| ID       | Cas d'utilisation                  | Description                                              | Acteurs                  | Préconditions                           | Postconditions                        |
|----------|------------------------------------|----------------------------------------------------------|--------------------------|-----------------------------------------|---------------------------------------|
| HOME-001 | Création d'une police habitation   | Créer une nouvelle police d'assurance habitation         | Gestionnaire de contrats | Produit d'assurance habitation existant | Police habitation créée               |
| HOME-002 | Ajout d'un bien immobilier         | Ajouter un bien immobilier à une police habitation       | Gestionnaire de contrats | Police habitation existante             | Bien immobilier ajouté à la police    |
| HOME-003 | Déclaration du contenu             | Déclarer le contenu du bien à assurer                    | Gestionnaire de contrats | Police habitation avec bien immobilier  | Contenu déclaré et assuré             |
| HOME-004 | Sélection des garanties habitation | Sélectionner les garanties pour une police habitation    | Gestionnaire de contrats | Police habitation existante             | Garanties sélectionnées et appliquées |
| HOME-005 | Calcul de prime habitation         | Calculer la prime d'assurance pour une police habitation | Système                  | Police habitation complète              | Prime calculée                        |
| HOME-006 | Évaluation des risques             | Évaluer les risques spécifiques au bien immobilier       | Système                  | Informations complètes sur le bien      | Risques évalués et pris en compte     |

### Use Cases - Assurance voyage

| ID         | Cas d'utilisation               | Description                                          | Acteurs                  | Préconditions                        | Postconditions                        |
|------------|---------------------------------|------------------------------------------------------|--------------------------|--------------------------------------|---------------------------------------|
| TRAVEL-001 | Création d'une police voyage    | Créer une nouvelle police d'assurance voyage         | Gestionnaire de contrats | Produit d'assurance voyage existant  | Police voyage créée                   |
| TRAVEL-002 | Ajout d'un voyage               | Ajouter un voyage à une police voyage                | Gestionnaire de contrats | Police voyage existante              | Voyage ajouté à la police             |
| TRAVEL-003 | Ajout d'un voyageur             | Ajouter un voyageur à une police voyage              | Gestionnaire de contrats | Police voyage existante              | Voyageur ajouté à la police           |
| TRAVEL-004 | Sélection des garanties voyage  | Sélectionner les garanties pour une police voyage    | Gestionnaire de contrats | Police voyage existante              | Garanties sélectionnées et appliquées |
| TRAVEL-005 | Calcul de prime voyage          | Calculer la prime d'assurance pour une police voyage | Système                  | Police voyage complète               | Prime calculée                        |
| TRAVEL-006 | Évaluation des risques médicaux | Évaluer les risques médicaux des voyageurs           | Système                  | Informations médicales des voyageurs | Risques évalués et pris en compte     |

### Use Cases - Assurance risques divers

| ID       | Cas d'utilisation                      | Description                                                  | Acteurs                  | Préconditions                               | Postconditions                        |
|----------|----------------------------------------|--------------------------------------------------------------|--------------------------|---------------------------------------------|---------------------------------------|
| MISC-001 | Création d'une police risques divers   | Créer une nouvelle police d'assurance risques divers         | Gestionnaire de contrats | Produit d'assurance risques divers existant | Police risques divers créée           |
| MISC-002 | Ajout d'un objet assuré                | Ajouter un objet à assurer à une police risques divers       | Gestionnaire de contrats | Police risques divers existante             | Objet ajouté à la police              |
| MISC-003 | Déclaration de la valeur               | Déclarer la valeur d'un objet assuré                         | Gestionnaire de contrats | Objet assuré existant                       | Valeur déclarée et enregistrée        |
| MISC-004 | Sélection des garanties risques divers | Sélectionner les garanties pour une police risques divers    | Gestionnaire de contrats | Police risques divers existante             | Garanties sélectionnées et appliquées |
| MISC-005 | Calcul de prime risques divers         | Calculer la prime d'assurance pour une police risques divers | Système                  | Police risques divers complète              | Prime calculée                        |
| MISC-006 | Évaluation des risques par objet       | Évaluer les risques spécifiques à chaque objet assuré        | Système                  | Informations complètes sur l'objet          | Risques évalués et pris en compte     |

### Use Cases - Gestion des sinistres

| ID      | Cas d'utilisation                       | Description                                                    | Acteurs                           | Préconditions           | Postconditions                                    |
|---------|-----------------------------------------|----------------------------------------------------------------|-----------------------------------|-------------------------|---------------------------------------------------|
| CLM-001 | Déclaration de sinistre                 | Enregistrer un nouveau sinistre                                | Gestionnaire de sinistres, Client | Police active existante | Sinistre créé et associé à la police              |
| CLM-002 | Traitement de sinistre                  | Mettre à jour le statut et les informations d'un sinistre      | Gestionnaire de sinistres         | Sinistre existant       | Statut du sinistre mis à jour                     |
| CLM-003 | Association d'organisations au sinistre | Lier différentes organisations à un sinistre selon leurs rôles | Gestionnaire de sinistres         | Sinistre existant       | Organisations associées au sinistre               |
| CLM-004 | Consultation des sinistres              | Consulter les sinistres selon le contexte organisationnel      | Tout utilisateur avec accès       | Utilisateur authentifié | Sinistres affichés selon les droits de visibilité |

### Use Cases - Reporting et analytics

| ID      | Cas d'utilisation                   | Description                                               | Acteurs           | Préconditions                     | Postconditions                                   |
|---------|-------------------------------------|-----------------------------------------------------------|-------------------|-----------------------------------|--------------------------------------------------|
| REP-001 | Génération de rapport de production | Générer un rapport sur la production de polices           | Manager, Analyste | Données de production disponibles | Rapport généré avec filtrage par organisation    |
| REP-002 | Génération de rapport de sinistres  | Générer un rapport sur les sinistres                      | Manager, Analyste | Données de sinistres disponibles  | Rapport généré avec filtrage par organisation    |
| REP-003 | Tableau de bord hiérarchique        | Afficher un tableau de bord adapté au niveau hiérarchique | Manager           | Utilisateur authentifié           | Tableau de bord affiché avec données pertinentes |

## 12. Mécanismes spécifiques à la hiérarchie assurantielle

### Modèle de relations flexible

Le système utilise un modèle de relations flexible qui permet de représenter tous les types de relations entre
organisations du secteur assurantiel:

- Une seule table `organization_relationships` gère à la fois les relations directes et indirectes
- Le champ `is_direct` permet de distinguer les relations explicitement créées des relations calculées
- Le champ `depth` indique la distance entre les organisations dans la hiérarchie
- Les requêtes récursives (Common Table Expressions) permettent de naviguer efficacement dans la hiérarchie
- Pas de stockage redondant des chemins complets, calculés à la demande pour garantir la cohérence
- Indexation optimisée pour les requêtes hiérarchiques fréquentes

### Filtrage contextuel des données

Le système implémente un mécanisme de filtrage automatique des données basé sur le contexte organisationnel de
l'utilisateur connecté:

```java

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantFilter {
    boolean includeDescendants() default false;

    String organizationIdField() default "organizationId";
}
```

Ce mécanisme est complété par un aspect qui intercepte les appels aux méthodes annotées et ajoute automatiquement des
critères de filtrage basés sur le contexte du tenant. Le service `OrganizationHierarchyService` fournit les méthodes
nécessaires pour récupérer les organisations visibles dans la hiérarchie.

## Conclusion

### Synthèse

Cette feuille de route présente une vision complète et détaillée du développement de la plateforme SaaS multi-tenant
pour le secteur de l'assurance. L'architecture hexagonale proposée, combinée à une organisation modulaire par domaine
métier, permettra de construire un système robuste, évolutif et maintenable.

La structure détaillée des packages et le plan d'implémentation en phases progressives offrent un cadre clair pour le
développement, facilitant la coordination entre les équipes et le suivi de l'avancement du projet.

### Prochaines étapes

Les prochaines étapes du projet, après l'implémentation des modules de base (organisation et sécurité) et le refactoring
de l'architecture, sont :

1. **Tests et qualité du code** :
    - Implémentation des tests unitaires pour les cas d'utilisation
    - Implémentation des tests d'intégration pour les repositories
    - Mise en place d'une couverture de code adéquate

2. **Optimisation des performances** :
    - Mise en place d'une stratégie de cache
    - Optimisation des requêtes N+1
    - Amélioration de la pagination

3. **Modules métier** :
    - Implémentation du module de gestion des polices d'assurance
    - Implémentation du module de gestion des sinistres
    - Implémentation des intégrations avec les systèmes externes

4. **Documentation et formation** :
    - Finalisation de la documentation technique
    - Création de la documentation utilisateur
    - Préparation des matériaux de formation

### Facteurs clés de succès

- Adhésion stricte aux principes d'architecture définis
- Revues de code régulières pour maintenir la qualité
- Tests automatisés exhaustifs
- Communication claire et régulière entre les équipes
- Feedback continu des utilisateurs pilotes

---

*Document confidentiel - Propriété de Cyr Léonce Anicet KAKOU*
