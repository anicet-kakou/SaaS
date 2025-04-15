# Controller Refactoring Plan

## 1. Overview

This document outlines the plan to consolidate the controller and service abstractions in the codebase. Currently, there
are duplicate abstractions (`AbstractReferenceController`/`AbstractReferenceService` and `AbstractCrudController`/
`AbstractCrudService`) that serve similar purposes. This plan aims to eliminate this duplication and simplify the
architecture.

## 2. Current State

- `AbstractCrudController` and `AbstractCrudService` provide generic CRUD operations
- `AbstractReferenceController` and `AbstractReferenceService` provide similar functionality but with
  tenant/organization awareness
- Many controllers and services implement these abstractions inconsistently

## 3. Target State

- Use `TenantAwareCrudController` (extending `AbstractCrudController`) for all tenant-aware controllers
- Use `TenantAwareCrudService` (extending `AbstractCrudService`) for all tenant-aware services
- Remove the obsolete `AbstractReferenceController` and `AbstractReferenceService`
- Ensure consistent method signatures and error handling across all controllers and services

## 4. Implementation Steps

### 4.1 Create New Base Classes (Completed)

- [x] Create `TenantAwareCrudController` extending `AbstractCrudController`
- [x] Create `TenantAwareCrudService` interface extending `AbstractCrudService`

### 4.2 Update Reference Controllers

- [x] Update `CirculationZoneReferenceController` to extend `TenantAwareCrudController`
- [x] Update `GeographicZoneReferenceController` to extend `TenantAwareCrudController`
- [x] Update `VehicleBodyTypeReferenceController` to extend `TenantAwareCrudController`
- [x] Update `VehicleModelReferenceController` to extend `TenantAwareCrudController`
- [x] Update `VehicleManufacturerReferenceController` to extend `TenantAwareCrudController`
- [x] Update `VehicleGenreReferenceController` to extend `TenantAwareCrudController`
- [x] Update `VehicleCategoryReferenceController` to extend `TenantAwareCrudController`
- [x] Update `VehicleFuelTypeReferenceController` to extend `TenantAwareCrudController`
- [x] Update `VehicleUsageReferenceController` to extend `TenantAwareCrudController`
- [x] Update `VehicleColorReferenceController` to extend `TenantAwareCrudController`

### 4.3 Update Reference Services

- [x] Update `CirculationZoneService` to implement `TenantAwareCrudService`
- [x] Update `GeographicZoneService` to implement `TenantAwareCrudService`
- [x] Update `VehicleBodyTypeService` to implement `TenantAwareCrudService`
- [x] Update `VehicleModelService` to implement `TenantAwareCrudService`
- [x] Update `VehicleManufacturerService` to implement `TenantAwareCrudService`
- [x] Update `VehicleGenreService` to implement `TenantAwareCrudService`
- [x] Update `VehicleCategoryService` to implement `TenantAwareCrudService`
- [x] Update `VehicleFuelTypeService` to implement `TenantAwareCrudService`
- [x] Update `VehicleUsageService` to implement `TenantAwareCrudService`
- [x] Update `VehicleColorService` to implement `TenantAwareCrudService`

### 4.4 Implement Missing Methods

- [x] Implement `setActive` method in `VehicleBodyTypeService`
- [x] Implement `setActive` method in `VehicleModelService`
- [x] Implement `setActive` method in `VehicleManufacturerService`
- [x] Implement `setActive` method in `VehicleGenreService`
- [x] Implement `setActive` method in `VehicleCategoryService`
- [x] Implement `setActive` method in `VehicleFuelTypeService`
- [x] Implement `setActive` method in `VehicleUsageService`
- [x] Implement `setActive` method in `VehicleColorService`

### 4.5 Remove Obsolete Classes

- [x] Remove `AbstractReferenceController` after all controllers have been updated
- [x] Remove `AbstractReferenceService` after all services have been updated

### 4.6 Update Tests

- [ ] Update tests for all modified controllers
- [ ] Update tests for all modified services
- [ ] Add tests for the new `TenantAwareCrudController` and `TenantAwareCrudService`

## 5. Benefits

- Simplified architecture with clear inheritance hierarchy
- Reduced code duplication
- Consistent method signatures and error handling
- Improved maintainability and readability

## 6. Risks and Mitigations

| Risk                                      | Mitigation                                                        |
|-------------------------------------------|-------------------------------------------------------------------|
| Breaking existing API contracts           | Maintain backward compatibility with deprecated endpoints         |
| Missing functionality in new abstractions | Ensure all required methods are implemented                       |
| Regression in error handling              | Use consistent `ResourceNotFoundException` across all controllers |
| Impact on existing tests                  | Update tests to match new method signatures                       |

## 7. Timeline

- Phase 1 (Completed): Create new base classes and update first set of controllers/services
- Phase 2: Update remaining controllers and services
- Phase 3: Remove obsolete classes and update tests
- Phase 4: Validate and finalize changes
