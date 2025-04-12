# Entity Inheritance Audit Report

This document contains the results of auditing entity classes for duplicate fields from parent classes.

## Inheritance Structure

The codebase uses the following inheritance hierarchy for entities:

1. `BaseEntity` - Base class with common fields:
    - `id` (UUID)
    - `createdAt` (LocalDateTime)
    - `updatedAt` (LocalDateTime)
    - `createdBy` (UUID)
    - `updatedBy` (UUID)
    - `version` (Long) - Used for optimistic locking

2. `AuditableEntity` extends `BaseEntity` and adds:
    - `active` (boolean) - For soft delete functionality

3. `TenantAwareEntity` extends `AuditableEntity` and adds:
    - `organizationId` (UUID) - For multi-tenant data isolation

4. `SystemDefinedEntity` extends `TenantAwareEntity` and adds:
    - `systemDefined` (boolean) - Indicates if the entity is defined by the system

## Issues Found

### Auto Module

#### 1. Vehicle Entity

**File:** `src/main/java/com/devolution/saas/insurance/nonlife/auto/domain/model/Vehicle.java`

**Issues:**

- The entity has a `versionName` field which might cause confusion with the inherited `version` field from `BaseEntity`
- The entity properly extends `TenantAwareEntity` but has a redundant comment "The organizationId field is inherited
  from TenantAwareEntity"
- The entity has a `hasAntiTheftDevice` field which duplicates the same field in `AutoPolicy`

**Recommendation:**

- Rename `versionName` to `modelVersion` or another more descriptive name to avoid confusion
- Remove redundant comments about inherited fields
- Consider if the duplication of `hasAntiTheftDevice` between `Vehicle` and `AutoPolicy` is intentional or if it should
  be in only one entity

#### 2. AutoPolicy Entity

**File:** `src/main/java/com/devolution/saas/insurance/nonlife/auto/domain/model/AutoPolicy.java`

**Issues:**

- The entity has a redundant comment "The organizationId field is inherited from TenantAwareEntity"
- The entity has a redundant method `getOrganizationId()` that simply calls the parent method

**Recommendation:**

- Remove redundant comments about inherited fields
- Remove the redundant `getOrganizationId()` method

#### 3. BonusMalus Entity

**File:** `src/main/java/com/devolution/saas/insurance/nonlife/auto/domain/model/BonusMalus.java`

**Issues:**

- The entity has a redundant comment "The organizationId field is inherited from TenantAwareEntity"

**Recommendation:**

- Remove redundant comments about inherited fields

### Organization Module

#### 1. Organization Entity

**File:** `src/main/java/com/devolution/saas/core/organization/domain/model/Organization.java`

**Issues:**

- The entity extends `AuditableEntity` instead of `TenantAwareEntity`, which is inconsistent with the multi-tenant
  architecture
- Organizations should be tenant-aware to support multi-tenant hierarchies

**Recommendation:**

- Consider if `Organization` should extend `TenantAwareEntity` to be consistent with the multi-tenant architecture
- If organizations are global entities, document this design decision

### Security Module

#### 1. Role Entity

**File:** `src/main/java/com/devolution/saas/core/security/domain/model/Role.java`

**Issues:**

- The entity has a `systemDefined` field which duplicates the functionality provided by `SystemDefinedEntity`

**Recommendation:**

- Consider having `Role` extend `SystemDefinedEntity` instead of `TenantAwareEntity` to leverage the `systemDefined`
  field

#### 2. User Entity

**File:** `src/main/java/com/devolution/saas/core/security/domain/model/User.java`

**Issues:**

- No duplicate fields found, but the entity implements `UserDetails` which adds complexity to the domain model

**Recommendation:**

- Consider separating the domain model from the Spring Security implementation details

### Audit Module

#### 1. AuditLog Entity

**File:** `src/main/java/com/devolution/saas/core/audit/domain/model/AuditLog.java`

**Issues:**

- The entity extends `BaseEntity` but should likely extend `TenantAwareEntity` since audit logs should be
  tenant-specific

**Recommendation:**

- Consider having `AuditLog` extend `TenantAwareEntity` to be consistent with the multi-tenant architecture

## General Recommendations

1. **Remove Redundant Comments**
    - Remove comments that simply state that a field is inherited from a parent class
    - These comments add noise and can become outdated if the inheritance structure changes

2. **Consistent Inheritance**
    - Ensure all entities follow a consistent inheritance pattern based on their requirements
    - Tenant-specific entities should extend `TenantAwareEntity`
    - System-defined entities should extend `SystemDefinedEntity`

3. **Avoid Duplicate Fields**
    - Avoid defining fields in child classes that are already defined in parent classes
    - If a field with a similar name is needed, use a more descriptive name to avoid confusion

4. **Document Design Decisions**
    - Document why certain entities deviate from the standard inheritance pattern
    - This helps future developers understand the design intent

## Next Steps

1. Fix the identified issues, starting with the `Vehicle` entity's `versionName` field
2. Create test cases to verify entity inheritance
3. Review and standardize JPA annotations across all entities
