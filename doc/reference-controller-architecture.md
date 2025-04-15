# Reference Controller Architecture

## Overview

This document describes the architecture of the reference controllers and services in the application. The architecture
has been simplified to use a consistent pattern across all reference data types.

## Architecture Components

### 1. TenantAwareCrudController

The `TenantAwareCrudController` is a base class that extends `AbstractCrudController` and adds tenant/organization
awareness. It provides standard CRUD operations for reference data, with all operations requiring an organization ID.

```java
public abstract class TenantAwareCrudController<T, C, U> extends AbstractCrudController<T, UUID, C, U> {
    // Common methods for tenant-aware CRUD operations
}
```

Key features:

- Provides standard endpoints for CRUD operations
- Requires organization ID for all operations
- Uses consistent error handling with ResourceNotFoundException
- Supports activation/deactivation of entities

### 2. TenantAwareCrudService

The `TenantAwareCrudService` is an interface that extends `AbstractCrudService` and adds tenant/organization awareness.
It defines standard methods for CRUD operations on reference data.

```java
public interface TenantAwareCrudService<T, E extends TenantAwareEntity> {
    // Common methods for tenant-aware CRUD operations
}
```

Key features:

- Defines standard methods for CRUD operations
- Requires organization ID for all operations
- Uses consistent return types (Optional for operations that might fail)
- Supports activation/deactivation of entities

### 3. Reference Controllers

Reference controllers extend `TenantAwareCrudController` and implement the required methods. They delegate to their
corresponding service for actual operations.

```java
@RestController
@RequestMapping("/api/v1/auto/reference/{resource}")
public class SomeReferenceController extends TenantAwareCrudController<SomeDTO, SomeEntity, SomeEntity> {
    private final SomeService someService;
    
    // Implementation of required methods
}
```

Key features:

- Consistent URL pattern: `/api/v1/auto/reference/{resource}`
- Consistent method names and signatures
- Proper error handling with ResourceNotFoundException
- Swagger/OpenAPI annotations for documentation

### 4. Reference Services

Reference services implement `TenantAwareCrudService` and provide the actual implementation of CRUD operations. They
interact with repositories to perform database operations.

```java
@Service
public class SomeServiceImpl implements SomeService {
    private final SomeRepository someRepository;
    private final SomeMapper someMapper;
    
    // Implementation of required methods
}
```

Key features:

- Consistent method names and signatures
- Proper organization ID validation
- Consistent use of mappers for DTO conversion
- Transaction management annotations

## Standard Endpoints

All reference controllers provide the following standard endpoints:

| HTTP Method | URL Pattern                                         | Description                            |
|-------------|-----------------------------------------------------|----------------------------------------|
| GET         | `/api/v1/auto/reference/{resource}`                 | Get all active entities                |
| GET         | `/api/v1/auto/reference/{resource}/all`             | Get all entities (active and inactive) |
| GET         | `/api/v1/auto/reference/{resource}/{id}`            | Get entity by ID                       |
| GET         | `/api/v1/auto/reference/{resource}/code/{code}`     | Get entity by code                     |
| POST        | `/api/v1/auto/reference/{resource}`                 | Create a new entity                    |
| PUT         | `/api/v1/auto/reference/{resource}/{id}`            | Update an existing entity              |
| PUT         | `/api/v1/auto/reference/{resource}/{id}/activate`   | Activate an entity                     |
| PUT         | `/api/v1/auto/reference/{resource}/{id}/deactivate` | Deactivate an entity                   |
| DELETE      | `/api/v1/auto/reference/{resource}/{id}`            | Delete an entity                       |

All endpoints require an `organizationId` query parameter.

## Error Handling

The architecture uses a consistent error handling approach:

- `ResourceNotFoundException`: Thrown when an entity is not found
- HTTP 404: Returned when an entity is not found
- HTTP 400: Returned for validation errors
- HTTP 500: Returned for server errors

## Example Implementation

### Controller

```java
@RestController
@RequestMapping("/api/v1/auto/reference/circulation-zones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Circulation Zones", description = "API pour la gestion des zones de circulation")
public class CirculationZoneReferenceController extends TenantAwareCrudController<CirculationZoneDTO, CirculationZone, CirculationZone> {

    private final CirculationZoneService circulationZoneService;

    @Override
    protected List<CirculationZoneDTO> listActive(UUID organizationId) {
        return circulationZoneService.getAllActiveCirculationZones(organizationId);
    }

    @Override
    protected List<CirculationZoneDTO> list(UUID organizationId) {
        return circulationZoneService.getAllCirculationZones(organizationId);
    }

    @Override
    protected CirculationZoneDTO get(UUID id, UUID organizationId) {
        return circulationZoneService.getCirculationZoneById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Zone de circulation", id));
    }

    // Other methods...
}
```

### Service

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CirculationZoneServiceImpl implements CirculationZoneService {

    private final CirculationZoneRepository circulationZoneRepository;
    private final CirculationZoneMapper circulationZoneMapper;

    @Override
    public List<CirculationZoneDTO> getAllActiveCirculationZones(UUID organizationId) {
        return circulationZoneRepository.findAllByOrganizationIdAndActiveIsTrue(organizationId)
                .stream()
                .map(circulationZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CirculationZoneDTO> getAllCirculationZones(UUID organizationId) {
        return circulationZoneRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(circulationZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    // Other methods...
}
```

## Benefits of This Architecture

1. **Consistency**: All reference controllers and services follow the same pattern
2. **Reduced Duplication**: Common functionality is implemented in base classes
3. **Improved Maintainability**: Changes to common functionality only need to be made in one place
4. **Better Developer Experience**: Developers can easily understand how to use and extend the architecture
5. **Standardized Error Handling**: All controllers use the same error handling approach
6. **Simplified Testing**: Base classes can be tested once, and concrete implementations can focus on their specific
   behavior

## Migration from Previous Architecture

The previous architecture used separate `AbstractReferenceController` and `AbstractReferenceService` classes, which
duplicated functionality already present in `AbstractCrudController` and `AbstractCrudService`. The new architecture
consolidates these abstractions, resulting in a simpler and more maintainable codebase.

## Future Improvements

1. **Pagination**: Add support for paginated results
2. **Filtering**: Add support for filtering results
3. **Sorting**: Add support for sorting results
4. **Caching**: Add caching for frequently accessed reference data
5. **Validation**: Add more comprehensive validation for reference data
