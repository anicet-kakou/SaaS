# Controller Renaming Plan

This document outlines the specific controllers that need to be renamed to follow the naming conventions defined in
`controller-naming-convention.md`.

## 1. Controllers to Rename

### 1.1 Auto Module Reference Controllers

These controllers need to be renamed to follow the `{EntityName}ReferenceController` pattern and have their URL paths
updated to remove the `-management` suffix.

| Current Name                    | New Name                                 | Current Path                                              | New Path                                       |
|---------------------------------|------------------------------------------|-----------------------------------------------------------|------------------------------------------------|
| `VehicleBodyTypeController`     | `VehicleBodyTypeReferenceController`     | `/api/v1/auto/reference/vehicle-body-types-management`    | `/api/v1/auto/reference/vehicle-body-types`    |
| `VehicleManufacturerController` | `VehicleManufacturerReferenceController` | `/api/v1/auto/reference/vehicle-manufacturers-management` | `/api/v1/auto/reference/vehicle-manufacturers` |
| `VehicleGenreController`        | `VehicleGenreReferenceController`        | `/api/v1/auto/reference/vehicle-genres-management`        | `/api/v1/auto/reference/vehicle-genres`        |
| `VehicleModelController`        | `VehicleModelReferenceController`        | `/api/v1/auto/reference/vehicle-models-management`        | `/api/v1/auto/reference/vehicle-models`        |
| `VehicleCategoryController`     | `VehicleCategoryReferenceController`     | `/api/v1/auto/reference/vehicle-categories-management`    | `/api/v1/auto/reference/vehicle-categories`    |
| `CirculationZoneController`     | `CirculationZoneReferenceController`     | `/api/v1/auto/reference/circulation-zones-management`     | `/api/v1/auto/reference/circulation-zones`     |

### 1.2 Auto Module Controllers

These controllers need to be updated to follow the `Auto{EntityName}Controller` pattern for clarity in the auto module.

| Current Name                     | New Name                   | Current Path                                                     | New Path                   |
|----------------------------------|----------------------------|------------------------------------------------------------------|----------------------------|
| `VehicleController`              | `AutoVehicleController`    | `/api/v1/organizations/{organizationId}/vehicles`                | `/api/v1/auto/vehicles`    |
| `DriverController`               | `AutoDriverController`     | `/api/v1/organizations/{organizationId}/drivers`                 | `/api/v1/auto/drivers`     |
| `BonusMalusController`           | `AutoBonusMalusController` | `/api/v1/auto/bonus-malus`                                       | `/api/v1/auto/bonus-malus` |
| `AutoInsuranceProductController` | `AutoProductController`    | `/api/v1/organizations/{organizationId}/auto-insurance-products` | `/api/v1/auto/products`    |

### 1.3 Core Module Controllers

These controllers already follow the naming convention but may need URL path adjustments.

| Current Name                 | New Name  | Current Path                 | New Path  |
|------------------------------|-----------|------------------------------|-----------|
| `OrganizationController`     | No change | `/api/v1/organizations`      | No change |
| `OrganizationTypeController` | No change | `/api/v1/organization-types` | No change |
| `UserController`             | No change | `/api/v1/users`              | No change |
| `RoleController`             | No change | `/api/v1/roles`              | No change |
| `AuthController`             | No change | `/api/v1/auth`               | No change |
| `ApiKeyController`           | No change | `/api/v1/api-keys`           | No change |
| `AuditLogController`         | No change | `/api/v1/audit-logs`         | No change |

## 2. Implementation Plan

### 2.1 Preparation

1. Create a new branch for controller renaming: `git checkout -b feature/controller-naming-standardization`
2. Ensure all tests are passing before making changes

### 2.2 Implementation Steps

For each controller that needs to be renamed:

1. Create a new controller class with the correct name
2. Copy the content from the old controller to the new one
3. Update the `@RequestMapping` annotation with the new path
4. Update any references to the controller in tests
5. Add `@Deprecated` annotation to the old controller with a message pointing to the new one
6. Keep both controllers temporarily to maintain backward compatibility

Example of a deprecated controller:

```java
/**
 * @deprecated Use {@link VehicleBodyTypeReferenceController} instead.
 * This controller will be removed in a future release.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-body-types-management")
@RequiredArgsConstructor
public class VehicleBodyTypeController {
    // ...
}
```

### 2.3 Testing

1. Run all tests to ensure they pass with the new controllers
2. Manually test API endpoints to verify they work as expected
3. Update API documentation to reflect the new controller names and paths

### 2.4 Cleanup

After a suitable deprecation period (e.g., one release cycle):

1. Remove the deprecated controllers
2. Remove any references to the old paths in documentation
3. Update client applications to use the new paths

## 3. Tracking

| Controller                       | Renamed | Tests Updated | Documentation Updated | Deprecated Old Controller | Removed Old Controller |
|----------------------------------|---------|---------------|-----------------------|---------------------------|------------------------|
| `VehicleBodyTypeController`      | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `VehicleManufacturerController`  | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `VehicleGenreController`         | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `VehicleModelController`         | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `VehicleCategoryController`      | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `VehicleFuelTypeController`      | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `VehicleUsageController`         | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `VehicleColorController`         | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `CirculationZoneController`      | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `GeographicZoneController`       | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `VehicleController`              | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `DriverController`               | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `BonusMalusController`           | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
| `AutoInsuranceProductController` | [x]     | [ ]           | [ ]                   | [x]                       | [ ]                    |
