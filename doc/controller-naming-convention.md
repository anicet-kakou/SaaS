# Controller Naming Convention

## 1. Introduction

This document defines the standard naming conventions for controllers in our SaaS multi-tenant insurance platform.
Following these conventions ensures consistency across the codebase, improves readability, and makes the API more
intuitive for consumers.

## 2. Controller Class Naming

### 2.1 General Patterns

| Pattern                       | Description                                         | Example                                              |
|-------------------------------|-----------------------------------------------------|------------------------------------------------------|
| `{EntityName}Controller`      | For controllers managing a single primary entity    | `UserController`, `OrganizationController`           |
| `{Domain}{Purpose}Controller` | For controllers handling specific domain operations | `AutoPricingController`, `ClaimProcessingController` |

### 2.2 Specific Rules

1. **Always use the `Controller` suffix**
    - ✅ `UserController`
    - ❌ `UserResource`, `UserAPI`, `UserEndpoint`

2. **Do not use the `Management` suffix**
    - ✅ `VehicleModelController`
    - ❌ `VehicleModelManagementController`

3. **Use module prefix for domain-specific controllers**
    - ✅ `AutoVehicleController`, `HomePropertyController`
    - ❌ `VehicleController` (when in auto module)

4. **Use `Reference` suffix for reference data controllers**
    - ✅ `VehicleMakeReferenceController`
    - ❌ `VehicleMakeController` (when it's reference data)

## 3. URL Path Conventions

### 3.1 Base URL Structure

| Resource Type     | URL Pattern                                              | Example                                        |
|-------------------|----------------------------------------------------------|------------------------------------------------|
| Primary resources | `/api/v1/{resource}`                                     | `/api/v1/users`                                |
| Nested resources  | `/api/v1/{parent-resource}/{parent-id}/{child-resource}` | `/api/v1/organizations/{organizationId}/users` |
| Actions           | `/api/v1/{resource}/{action}`                            | `/api/v1/policies/calculate-premium`           |
| Reference data    | `/api/v1/reference/{resource}`                           | `/api/v1/reference/vehicle-makes`              |

### 3.2 URL Naming Rules

1. **Use kebab-case for multi-word resources in URLs**
    - ✅ `/api/v1/vehicle-models`
    - ❌ `/api/v1/vehicleModels` or `/api/v1/vehicle_models`

2. **Use plural nouns for resource collections**
    - ✅ `/api/v1/users`, `/api/v1/policies`
    - ❌ `/api/v1/user`, `/api/v1/policy`

3. **Use domain-specific prefixes for module-specific endpoints**
    - ✅ `/api/v1/auto/vehicles`, `/api/v1/home/properties`
    - ❌ `/api/v1/vehicles` (when in auto module)

## 4. HTTP Method Usage

| Operation      | HTTP Method | URL Pattern                   | Example                                   |
|----------------|-------------|-------------------------------|-------------------------------------------|
| List all       | GET         | `/api/v1/{resource}`          | `GET /api/v1/users`                       |
| Get one        | GET         | `/api/v1/{resource}/{id}`     | `GET /api/v1/users/123`                   |
| Create         | POST        | `/api/v1/{resource}`          | `POST /api/v1/users`                      |
| Update         | PUT         | `/api/v1/{resource}/{id}`     | `PUT /api/v1/users/123`                   |
| Partial update | PATCH       | `/api/v1/{resource}/{id}`     | `PATCH /api/v1/users/123`                 |
| Delete         | DELETE      | `/api/v1/{resource}/{id}`     | `DELETE /api/v1/users/123`                |
| Custom action  | POST        | `/api/v1/{resource}/{action}` | `POST /api/v1/policies/calculate-premium` |

## 5. Controller Implementation

### 5.1 Class Structure

```java
@RestController
@RequestMapping("/api/v1/{resource}")
@Tag(name = "{Resource}", description = "API for managing {resources}")
@RequiredArgsConstructor
@Slf4j
public class {Resource}

Controller {
    // Dependencies
    private final {
        Resource
    } Service {
        resource
    } Service;

    // Endpoints
    // ...
}
```

### 5.2 Method Structure

```java
@Operation(summary = "Create a new {resource}", description = "Creates a new {resource} with the provided data")
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public {
    Resource
}

DTO create{Resource}(

@Valid
@RequestBody
Create {
    Resource
}

Command command){
        log.

debug("REST request to create {Resource}: {}",command);
    return{resource}Service.

create {
    Resource
}(command);
        }
```

## 6. Documentation Requirements

1. **Class-level documentation**
    - Javadoc describing the controller's purpose
    - `@Tag` annotation for Swagger/OpenAPI grouping
    - `@RequestMapping` with consistent base path

2. **Method-level documentation**
    - Javadoc describing the operation
    - `@Operation` annotation with summary and description
    - Appropriate HTTP method annotation
    - Response status annotation

## 7. Examples

### 7.1 Entity Controller Example

```java
/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API for managing users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        log.debug("REST request to get all Users");
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID", description = "Returns a user by ID")
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable UUID id) {
        log.debug("REST request to get User: {}", id);
        return userService.getUserById(id);
    }
}
```

### 7.2 Domain-Specific Controller Example

```java
/**
 * REST controller for auto insurance pricing operations.
 */
@RestController
@RequestMapping("/api/v1/auto/pricing")
@Tag(name = "Auto Pricing", description = "API for auto insurance pricing operations")
@RequiredArgsConstructor
@Slf4j
public class AutoPricingController {
    private final AutoPricingService autoPricingService;

    @Operation(summary = "Calculate premium", description = "Calculates premium for auto insurance")
    @PostMapping("/calculate")
    public PremiumCalculationResultDTO calculatePremium(@Valid @RequestBody PremiumCalculationCommand command) {
        log.debug("REST request to calculate auto premium: {}", command);
        return autoPricingService.calculatePremium(command);
    }
}
```

## 8. Migration Plan

When renaming existing controllers to follow these conventions:

1. Update the controller class name
2. Update the request mapping path
3. Update any references in tests
4. Update API documentation
5. Ensure backward compatibility with existing clients if needed

## 9. Conclusion

Following these naming conventions will ensure consistency across our codebase and make our API more intuitive for
consumers. All new controllers should adhere to these conventions, and existing controllers should be refactored to
follow them as part of the ongoing code quality improvement initiative.
