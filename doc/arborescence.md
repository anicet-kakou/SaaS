# Structure détaillée du projet

## Vue d'ensemble

Cette structure suit une architecture hexagonale (Ports & Adapters) avec une organisation modulaire par domaine métier.
Chaque module respecte les principes DDD avec une séparation claire entre l'API, l'application, le domaine et
l'infrastructure.

## Arborescence complète

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── devolution/
│   │           └── saas/
│   │               ├── common/
│   │               │   ├── domain/
│   │               │   │   ├── model/
│   │               │   │   │   ├── AuditableEntity.java
│   │               │   │   │   ├── BaseEntity.java
│   │               │   │   │   ├── SoftDeletableEntity.java
│   │               │   │   │   └── TenantAwareEntity.java      # Support multi-tenant
│   │               │   │   ├── validation/
│   │               │   │   │   ├── ValidationGroups.java
│   │               │   │   │   └── ValidationConstants.java     # Constantes de validation
│   │               │   │   └── exception/
│   │               │   │       ├── BusinessException.java       # Exceptions métier
│   │               │   │       └── TechnicalException.java      # Exceptions techniques
│   │               │   ├── infrastructure/
│   │               │   │   ├── config/
│   │               │   │   │   ├── JpaConfig.java
│   │               │   │   │   ├── WebConfig.java
│   │               │   │   │   ├── AsyncConfig.java            # Configuration asynchrone
│   │               │   │   │   ├── CacheConfig.java            # Configuration du cache
│   │               │   │   │   └── MetricsConfig.java          # Configuration des métriques
│   │               │   │   └── persistence/
│   │               │   │       ├── BaseRepository.java
│   │               │   │       └── TenantAwareRepository.java   # Repository multi-tenant
│   │               │   ├── util/
│   │               │   │   ├── DateUtils.java                  # Utilitaires de dates
│   │               │   │   ├── StringUtils.java                # Utilitaires de chaînes
│   │               │   │   └── SecurityUtils.java              # Utilitaires de sécurité
│   │               │   └── aspect/
│   │               │       ├── LoggingAspect.java              # Aspect de logging
│   │               │       └── MetricsAspect.java              # Aspect de métriques
│   │               ├── core/                            # Socle technique
│   │               │   ├── settings/                    # Paramètres globaux
│   │               │   │   ├── api/
│   │               │   │   │   ├── GlobalSettingsController.java
│   │               │   │   │   └── dto/
│   │               │   │   │       ├── SettingDTO.java
│   │               │   │   │       └── SettingUpdateRequest.java
│   │               │   │   ├── domain/
│   │               │   │   │   ├── model/
│   │               │   │   │   │   ├── Setting.java
│   │               │   │   │   │   └── SettingCategory.java
│   │               │   │   │   └── repository/
│   │               │   │   │       └── SettingRepository.java
│   │               │   │   └── infrastructure/
│   │               │   │       └── persistence/
│   │               │   │           └── JpaSettingRepository.java
│   │               │   │
│   │               │   ├── security/                    # Sécurité et authentification
│   │               │   │   ├── api/
│   │               │   │   │   ├── AuthController.java
│   │               │   │   │   └── dto/
│   │               │   │   │       ├── LoginRequest.java
│   │               │   │   │       └── TokenResponse.java
│   │               │   │   ├── domain/
│   │               │   │   │   ├── model/
│   │               │   │   │   │   ├── Role.java
│   │               │   │   │   │   ├── Permission.java
│   │               │   │   │   │   └── RefreshToken.java
│   │               │   │   │   └── service/
│   │               │   │   │       └── TokenService.java
│   │               │   │   └── infrastructure/
│   │               │   │       ├── jwt/
│   │               │   │       │   ├── JwtTokenProvider.java
│   │               │   │       │   └── JwtAuthenticationFilter.java
│   │               │   │       └── config/
│   │               │   │           └── SecurityConfig.java
│   │               │   │
│   │               │   ├── organization/                # Gestion multi-organisation
│   │               │   │   ├── api/
│   │               │   │   │   ├── OrganizationController.java
│   │               │   │   │   └── dto/
│   │               │   │   │       ├── OrganizationDTO.java
│   │               │   │   │       └── OrganizationCreateRequest.java
│   │               │   │   ├── domain/
│   │               │   │   │   ├── model/
│   │               │   │   │   │   ├── Organization.java
│   │               │   │   │   │   ├── OrganizationType.java
│   │               │   │   │   │   └── OrganizationHierarchy.java
│   │               │   │   │   └── repository/
│   │               │   │   │       └── OrganizationRepository.java
│   │               │   │   ├── application/
│   │               │   │   │   ├── service/
│   │               │   │   │   │   ├── OrganizationService.java
│   │               │   │   │   │   └── OrganizationServiceImpl.java
│   │               │   │   │   └── mapper/
│   │               │   │   │       └── OrganizationMapper.java
│   │               │   │   └── infrastructure/
│   │               │   │       ├── persistence/
│   │               │   │       │   └── JpaOrganizationRepository.java
│   │               │   │       └── context/
│   │               │   │           └── OrganizationContext.java
│   │               │   │
│   │               │   └── audit/                      # Audit et traçabilité
│   │               │       ├── domain/
│   │               │       │   ├── model/
│   │               │       │   │   └── AuditLog.java
│   │               │       │   └── repository/
│   │               │       │       └── AuditLogRepository.java
│   │               │       └── infrastructure/
│   │               │           ├── aspect/
│   │               │           │   └── AuditAspect.java
│   │               │           └── persistence/
│   │               │               └── JpaAuditLogRepository.java
│   │               │
│   │               └── insurance/                      # Modules métier assurance
│   │                   ├── product/                    # Gestion des produits
│   │                   │   ├── api/
│   │                   │   │   ├── ProductController.java
│   │                   │   │   └── dto/
│   │                   │   │       ├── ProductDTO.java
│   │                   │   │       └── ProductCreateRequest.java
│   │                   │   ├── domain/
│   │                   │   │   ├── model/
│   │                   │   │   │   ├── Product.java
│   │                   │   │   │   └── ProductType.java
│   │                   │   │   └── repository/
│   │                   │   │       └── ProductRepository.java
│   │                   │   └── infrastructure/
│   │                   │       └── persistence/
│   │                   │           └── JpaProductRepository.java
│   │                   │
│   │                   ├── policy/                     # Gestion des polices
│   │                   │   ├── api/
│   │                   │   │   ├── PolicyController.java
│   │                   │   │   └── dto/
│   │                   │   │       ├── PolicyDTO.java
│   │                   │   │       └── PolicyCreateRequest.java
│   │                   │   ├── domain/
│   │                   │   │   ├── model/
│   │                   │   │   │   ├── Policy.java
│   │                   │   │   │   └── PolicyStatus.java
│   │                   │   │   └── repository/
│   │                   │   │       └── PolicyRepository.java
│   │                   │   └── infrastructure/
│   │                   │       └── persistence/
│   │                   │           └── JpaPolicyRepository.java
│   │                   │
│   │                   └── claim/                      # Gestion des sinistres
│   │                       ├── api/
│   │                       │   ├── ClaimController.java
│   │                       │   └── dto/
│   │                       │       ├── ClaimDTO.java
│   │                       │       └── ClaimCreateRequest.java
│   │                       ├── domain/
│   │                       │   ├── model/
│   │                       │   │   ├── Claim.java
│   │                       │   │   └── ClaimStatus.java
│   │                       │   └── repository/
│   │                       │       └── ClaimRepository.java
│   │                       └── infrastructure/
│   │                           └── persistence/
│   │                               └── JpaClaimRepository.java
│   │
│   └── resources/
│       ├── application.yml                    # Configuration principale
│       ├── application-dev.yml                # Configuration développement
│       ├── application-prod.yml               # Configuration production
│       └── db/
│           └── migration/                     # Scripts Flyway
│               ├── V1__init.sql
│               ├── V2__organization.sql
│               └── V3__security.sql

└── test/
    ├── java/
    │   └── com/
    │       └── devolution/
    │           └── saas/
    │               ├── common/
    │               │   └── domain/
    │               │       └── model/
    │               │           └── BaseEntityTest.java
    │               ├── security/
    │               │   ├── api/
    │               │   │   └── AuthControllerTest.java
    │               │   └── service/
    │               │       └── TokenServiceTest.java
    │               └── organization/
    │                   ├── api/
    │                   │   └── OrganizationControllerTest.java
    │                   └── service/
    │                       └── OrganizationServiceTest.java
    └── resources/
        ├── application-test.yml
        └── data/
            └── test-data.sql
```

## Points clés de l'architecture

1. **Structure modulaire**
    - Modules techniques dans `core/`
    - Modules métier dans `insurance/`
    - Classes communes dans `common/`

2. **Architecture hexagonale**
   Chaque module suit la structure:
    - `api/` : Contrôleurs REST et DTOs
    - `domain/` : Modèles et interfaces repository
    - `application/` : Services et mappers
    - `infrastructure/` : Implémentations techniques

3. **Gestion multi-organisation**
    - Contexte organisation dans `core/organization/`
    - Hiérarchie des organisations
    - Filtrage par organisation

4. **Sécurité**
    - Authentication JWT
    - Gestion des rôles et permissions
    - Refresh tokens

5. **Configuration**
    - Paramètres globaux dans `core/settings/`
    - Configurations par environnement
    - Migration base de données avec Flyway

## Conventions de nommage

1. **Classes**
    - Controllers: `*Controller`
    - Services: `*Service`, `*ServiceImpl`
    - Repositories: `*Repository`
    - Entités: Nom simple (`User`, `Policy`)
    - DTOs: `*DTO`, `*Request`, `*Response`

2. **Packages**
    - API: `api`, `api.dto`
    - Domain: `domain`, `domain.model`, `domain.repository`
    - Infrastructure: `infrastructure`, `infrastructure.persistence`

## Notes d'implémentation

1. Commencer par les modules `common` et `core`
2. Implémenter la sécurité et la gestion des organisations
3. Développer progressivement les modules métier
4. Maintenir une couverture de tests complète

Cette structure permet une évolution vers des microservices si nécessaire, chaque module étant déjà bien isolé.