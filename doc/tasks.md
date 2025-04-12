# Implementation Tasks

This document outlines the specific tasks needed to address the code quality and consistency issues identified in the
code review. Each major area is broken down into smaller, actionable tasks.

## 1. Controller Layer Standardization

### 1.1 Controller Naming and Structure

- [ ] **1.1.1** Create a naming convention document for controllers
- [ ] **1.1.2** Rename all controllers to follow the pattern `EntityController` (remove `-Management` suffix)
- [ ] **1.1.3** Update request mappings to follow consistent URL pattern:
    - [ ] Auto module controllers
    - [ ] Organization module controllers
    - [ ] Security module controllers
    - [ ] Reference data controllers

### 1.2 Request/Response Standardization

- [ ] **1.2.1** Standardize all controller methods to return `ResponseEntity<T>`
- [ ] **1.2.2** Create consistent approach for organization ID:
    - [ ] Decide between `@PathVariable` vs `@RequestParam`
    - [ ] Update all controllers to use the chosen approach
- [ ] **1.2.3** Standardize request validation:
    - [ ] Ensure all DTOs have proper validation annotations
    - [ ] Implement consistent validation error handling

### 1.3 API Documentation

- [ ] **1.3.1** Add OpenAPI annotations to all controllers
- [ ] **1.3.2** Document request/response models
- [ ] **1.3.3** Add example requests and responses

## 2. Service Layer Refactoring

### 2.1 Service Interface Standardization

- [ ] **2.1.1** Ensure all services have corresponding interfaces
- [ ] **2.1.2** Refactor services without interfaces:
    - [ ] Auto module services
    - [ ] Reference data services
    - [ ] Security services

### 2.2 Transaction Management

- [ ] **2.2.1** Review and standardize `@Transactional` usage:
    - [ ] Apply at method level consistently
    - [ ] Set appropriate `readOnly` flag for query methods
    - [ ] Set appropriate isolation levels where needed
- [ ] **2.2.2** Document transaction management approach

### 2.3 Error Handling in Services

- [ ] **2.3.1** Standardize exception throwing:
    - [ ] Create domain-specific exceptions where needed
    - [ ] Ensure consistent error messages
- [ ] **2.3.2** Improve logging in service methods:
    - [ ] Add debug logs for method entry/exit
    - [ ] Add appropriate error logs

## 3. Domain Model Cleanup

### 3.1 Entity Inheritance Review

- [x] **3.1.1** Audit all entity classes for duplicate fields from parent classes:
    - [x] Auto module entities
    - [x] Organization module entities
    - [x] Security module entities
- [x] **3.1.2** Remove duplicate fields (like the `version` field in `Vehicle`)
- [ ] **3.1.3** Create test cases to verify entity inheritance

### 3.2 JPA Annotation Standardization

- [x] **3.2.1** Review and fix entity annotations:
    - [x] Ensure all entities have `@Entity` and `@Table` annotations
    - [x] Standardize column definitions
    - [x] Add appropriate indexes
- [x] **3.2.2** Standardize relationship mappings:
    - [x] Use consistent cascade types
    - [x] Set appropriate fetch types
    - [x] Add proper join column definitions

### 3.3 Validation Rules

- [ ] **3.3.1** Implement consistent validation in domain models:
    - [ ] Add Bean Validation annotations
    - [ ] Create custom validators where needed
- [ ] **3.3.2** Document validation rules for each entity

## 4. Repository Layer Standardization

### 4.1 Repository Interface Consistency

- [x] **4.1.1** Standardize repository interfaces:
    - [x] Decide on extending Spring Data JPA vs custom interfaces
    - [x] Apply the chosen approach consistently
- [x] **4.1.2** Standardize method naming:
    - [x] Use consistent prefixes (`findBy`, `getBy`, etc.)
    - [x] Use consistent parameter ordering

### 4.2 Query Methods

- [x] **4.2.1** Review and optimize query methods:
    - [x] Replace complex queries with named queries where appropriate
    - [x] Add query hints for performance where needed
- [x] **4.2.2** Add appropriate indexes to support queries

### 4.3 Multi-tenant Filtering

- [x] **4.3.1** Ensure consistent organization ID filtering:
    - [x] Review all repository methods
    - [x] Add organization ID filtering where missing
- [x] **4.3.2** Implement or improve tenant filter aspect

## 5. Configuration and Security Enhancements

### 5.1 Configuration Externalization

- [ ] **5.1.1** Identify and externalize hardcoded values:
    - [ ] Security settings
    - [ ] Business rules
    - [ ] Default values
- [ ] **5.1.2** Create environment-specific configuration profiles:
    - [ ] Development
    - [ ] Testing
    - [ ] Production

### 5.2 Security Hardening

- [x] **5.2.1** Review and enhance JWT implementation:
    - [x] Ensure secure token generation
    - [x] Implement proper token validation
    - [x] Add token refresh mechanism
- [x] **5.2.2** Enhance CORS configuration:
    - [x] Restrict allowed origins
    - [x] Set appropriate headers
- [ ] **5.2.3** Implement proper role-based access control:
    - [ ] Review and update method security annotations
    - [ ] Implement fine-grained permission checks

### 5.3 Rate Limiting and API Protection

- [x] **5.3.1** Enhance rate limiting:
    - [x] Configure per-endpoint limits
    - [x] Implement tenant-specific limits
- [x] **5.3.2** Add API key validation:
    - [x] Implement key rotation
    - [x] Add usage tracking

## 6. Error Handling Standardization

### 6.1 Global Exception Handler

- [x] **6.1.1** Enhance global exception handler:
    - [x] Add handling for all custom exceptions
    - [x] Standardize error response format
- [ ] **6.1.2** Create consistent error codes:
    - [ ] Define error code format
    - [ ] Document error codes

### 6.2 Logging Enhancements

- [x] **6.2.1** Standardize logging:
    - [x] Define log levels for different scenarios
    - [x] Add correlation IDs to logs
    - [x] Mask sensitive data in logs
- [ ] **6.2.2** Implement centralized logging:
    - [ ] Configure log aggregation
    - [ ] Set up log monitoring

## 7. Testing Improvements

### 7.1 Unit Testing

- [ ] **7.1.1** Increase unit test coverage:
    - [ ] Service layer tests
    - [ ] Repository layer tests
    - [ ] Domain model tests
- [ ] **7.1.2** Standardize test naming and structure

### 7.2 Integration Testing

- [ ] **7.2.1** Implement integration tests:
    - [ ] API endpoint tests
    - [ ] Database integration tests
- [ ] **7.2.2** Set up test data management

### 7.3 Performance Testing

- [ ] **7.3.1** Implement performance tests:
    - [ ] Load testing
    - [ ] Stress testing
- [ ] **7.3.2** Establish performance baselines and thresholds

### 7.4 Code Quality

- [ ] **7.4.1** Implement code quality checks:
    - [ ] Add SonarQube integration
    - [ ] Configure code style checks
    - [ ] Set up code coverage thresholds
- [ ] **7.4.2** Reduce code duplication:
    - [ ] Create `ReferenceDataEntity` base class
    - [ ] Implement `TenantAwareRepository` interface
    - [ ] Create `AbstractReferenceDataService` class
    - [ ] Refactor reference data controllers

## 8. Documentation Enhancements

### 8.1 Code Documentation

- [ ] **8.1.1** Standardize Javadoc:
    - [ ] Ensure all public methods have Javadoc
    - [ ] Document parameters and return values
- [ ] **8.1.2** Add architectural documentation:
    - [ ] Update module diagrams
    - [ ] Document design decisions

### 8.2 API Documentation

- [ ] **8.2.1** Enhance OpenAPI documentation:
    - [ ] Add detailed descriptions
    - [ ] Document error responses
- [ ] **8.2.2** Create API usage examples

### 8.3 Developer Documentation

- [ ] **8.3.1** Create developer onboarding guide
- [ ] **8.3.2** Document development workflows:
    - [ ] Local setup
    - [ ] Testing procedures
    - [ ] Deployment process

## 9. Build and Deployment

### 9.1 Build Process

- [ ] **9.1.1** Enhance Gradle build:
    - [ ] Add code quality plugins
    - [ ] Configure build profiles
- [ ] **9.1.2** Set up continuous integration:
    - [ ] Configure automated builds
    - [ ] Set up test execution

### 9.2 Deployment Automation

- [ ] **9.2.1** Create deployment scripts:
    - [ ] Development environment
    - [ ] Staging environment
    - [ ] Production environment
- [ ] **9.2.2** Implement database migration strategy:
    - [ ] Review and organize Flyway migrations
    - [ ] Create migration testing process

## 10. Monitoring and Operations

### 10.1 Application Monitoring

- [ ] **10.1.1** Implement health checks:
    - [ ] Database connectivity
    - [ ] External service connectivity
- [ ] **10.1.2** Set up metrics collection:
    - [ ] Performance metrics
    - [ ] Business metrics

### 10.2 Alerting

- [ ] **10.2.1** Configure alerts:
    - [ ] Error rate thresholds
    - [ ] Performance degradation
- [ ] **10.2.2** Set up notification channels

## Priority Order

1. **Critical Fixes**:
    - Entity inheritance issues (3.1)
    - JPA annotation standardization (3.2)
    - Transaction management (2.2)
    - Security hardening (5.2)

2. **High Priority**:
    - Controller standardization (1.1, 1.2)
    - Error handling standardization (6.1)
    - Repository layer standardization (4.1, 4.3)

3. **Medium Priority**:
    - Service layer refactoring (2.1, 2.3)
    - Configuration externalization (5.1)
    - Testing improvements (7.1, 7.2)

4. **Lower Priority**:
    - Documentation enhancements (8.1, 8.2, 8.3)
    - Build and deployment (9.1, 9.2)
    - Monitoring and operations (10.1, 10.2)

## Completed Tasks

### April 11, 2025

1. **Fixed compilation errors in VehicleController**:
    - Changed `versionName(request.getVersion())` to `modelVariant(request.getVersion())` to match the field name in the
      `CreateVehicleCommand` class.

2. **Fixed compilation errors in AutoPolicyController**:
    - Modified the approach for setting ID fields in entity builders. Instead of using builder methods for ID fields
      that are used for relationships, we used a two-step approach:
        - First, create the entity using the builder with only the fields that are directly accessible
        - Then, use setter methods to set the ID fields that are used for relationships
    - Applied this pattern to several classes:
        - `AutoPolicyController`
        - `AutoPolicyMapper`
        - `CreateAutoPolicyImpl`
        - `UpdateAutoPolicyImpl`
        - `DriverController`
        - `DriverMapper`
        - `VehicleMapper`
        - `VehicleServiceImpl`
        - `CreateVehicleImpl`
        - `UpdateVehicleImpl`

3. **Fixed runtime errors in JpaAutoPolicyRepository**:
    - Updated the JPQL queries to use the correct field names:
        - Changed `p.vehicleId` to `p.vehicle.id`
        - Changed `p.primaryDriverId` to `p.primaryDriver.id`

4. **Fixed entity relationship issues**:
    - Fixed the `Vehicle` class to use the correct `VehicleUsage` class from the reference domain model package.

5. **Fixed bean conflict issues**:
    - Renamed `AuthController` in the `application.controller` package to `DeprecatedAuthController` to avoid conflict
      with `AuthController` in the `api` package.
    - Commented out `@RestController` and `@RequestMapping` annotations in the deprecated controller.

6. **Fixed JwtBlacklistService initialization**:
    - Removed Redis import that was causing compilation errors.
    - Refactored the service to use `@PostConstruct` and `@PreDestroy` annotations for proper initialization and
      cleanup.
    - Fixed the issue with `cleanupIntervalSeconds` being null during constructor execution.

7. **Migrated from Java EE to Jakarta EE**:
    - Updated imports in `SecureSpecifications.java` from `javax.persistence.criteria` to
      `jakarta.persistence.criteria`.
    - Fixed compilation errors related to the migration from Java EE to Jakarta EE.
    - Ensured consistent use of Jakarta EE APIs throughout the codebase.

### April 12, 2025

**Code Review and Recommendations**:

1. **Architecture Hexagonale**:
    - L'architecture hexagonale est bien implémentée avec une séparation claire entre les couches API, application,
      domaine et infrastructure.
    - Chaque module respecte les principes DDD avec une organisation cohérente.

2. **Prochaines étapes recommandées**:
    - Compléter la standardisation des contrôleurs (tâche 1.1)
    - Améliorer la gestion des transactions (tâche 2.2)
    - Implémenter un contrôle d'accès basé sur les rôles plus fin (tâche 5.2.3)
    - Augmenter la couverture des tests unitaires (tâche 7.1)

3. **Points d'attention**:
    - Vérifier la cohérence des annotations JPA dans les nouvelles entités
    - S'assurer que tous les services utilisent les interfaces appropriées
    - Standardiser la gestion des erreurs dans l'ensemble de l'application
    - Réduire la duplication de code dans les contrôleurs et services de référence

### April 13, 2025

**Analyse des Duplications de Code**:

1. **Duplication dans les Contrôleurs**:
    - Identification de structures similaires dans les contrôleurs de référence (`VehicleModelController`,
      `VehicleMakeController`, etc.)
    - Recommandation: Utiliser `AbstractCrudController` et créer une interface générique `ReferenceDataController`

2. **Duplication dans les Services**:
    - Identification de méthodes similaires dans les services d'implémentation (`VehicleModelServiceImpl`,
      `VehicleMakeServiceImpl`, etc.)
    - Recommandation: Créer une classe abstraite `AbstractReferenceDataService` pour factoriser le code commun

3. **Duplication dans les Repositories**:
    - Identification de méthodes similaires dans les repositories JPA
    - Recommandation: Créer une interface `TenantAwareRepository` et une implémentation `BaseTenantAwareRepository`

4. **Duplication dans les Entités**:
    - Identification de champs communs dans les entités de référence
    - Recommandation: Créer une classe abstraite `ReferenceDataEntity` qui étend `TenantAwareEntity`

Un rapport détaillé a été créé dans `doc/code-duplication-analysis.md` avec des exemples d'implémentation et un plan d'
action.

### April 14, 2025

**Refactorisation pour plus de cohérence**:

1. **Renommage des entités**:
    - `FuelType` renommé en `VehicleFuelType` pour une meilleure cohérence avec les autres entités de référence
    - `VehicleMake` renommé en `VehicleManufacturer` pour une terminologie plus précise

2. **Mise à jour des références**:
    - Création de nouvelles classes, DTOs, repositories, services et contrôleurs
    - Mise à jour des références dans la classe `Vehicle`
    - Création de scripts de migration pour renommer les tables et colonnes

3. **Amélioration de la cohérence**:
    - Standardisation des noms d'entités avec le préfixe "Vehicle" pour toutes les entités de référence liées aux
      véhicules
    - Utilisation du terme "Manufacturer" au lieu de "Make" pour une terminologie plus précise
