# Entity Annotation Audit Report

This document contains the results of auditing entity classes for JPA annotation issues and standardization
opportunities.

## Overview

The codebase uses JPA annotations for entity mapping with a well-defined inheritance hierarchy. However, there are
several inconsistencies and potential issues with the annotations that should be addressed to ensure proper database
mapping and optimal performance.

## Key Findings

1. **Inconsistent Column Definitions**: Column definitions vary across entities, with inconsistent use of `nullable`,
   `length`, and other attributes.
2. **Missing Indexes**: Many entities lack proper index definitions, which can impact query performance.
3. **Relationship Mapping Issues**: Relationship mappings have inconsistent cascade and fetch types.
4. **Potential Schema-Database Mismatches**: Some entities may have annotations that don't match the actual database
   schema.
5. **Inconsistent Use of Constraints**: Unique constraints and foreign key constraints are not consistently defined.

## Detailed Findings

### 1. Column Definition Issues

#### 1.1 Inconsistent Nullable Definitions

Many entity fields lack explicit `nullable` attributes in their `@Column` annotations, which can lead to schema
validation issues and unexpected behavior.

**Examples:**

- In `Organization.java`, some fields have `nullable = false` while others don't have the attribute defined at all.
- In `User.java`, critical fields like `email` have `nullable = false`, but other important fields don't specify
  nullability.

**Recommendation:**

- Explicitly define `nullable` for all columns based on business requirements.
- Use `nullable = false` for all required fields.

#### 1.2 Missing Length Constraints

String columns often lack length constraints, which can lead to inefficient database storage and potential truncation
issues.

**Examples:**

- In `Vehicle.java`, string fields like `registrationNumber` don't specify a maximum length.
- In `Organization.java`, fields like `name`, `code`, and `address` don't have length constraints.

**Recommendation:**

- Add appropriate `length` attributes to all string columns.
- Use domain-specific length constraints based on business requirements.

#### 1.3 Inconsistent Column Names

Some entities use camelCase field names without explicit column name mapping, while others use explicit `name`
attributes in `@Column` annotations.

**Recommendation:**

- Consistently use explicit column names in `@Column` annotations to ensure proper mapping to database columns.
- Follow a consistent naming convention for database columns (e.g., snake_case).

### 2. Index Definition Issues

#### 2.1 Missing Index Annotations

Many entities lack `@Index` annotations, even for fields that are frequently used in queries.

**Examples:**

- In `Organization.java`, there's no index defined for the `code` field, which is likely used for lookups.
- In `Vehicle.java`, there's no index for `registrationNumber`, which is a common lookup field.

**Recommendation:**

- Add `@Index` annotations to the `@Table` annotation for frequently queried fields.
- Ensure that all fields used in WHERE clauses have appropriate indexes.

#### 2.2 Database-Level vs. Entity-Level Indexes

The codebase defines many indexes at the database level (in migration scripts) but not at the entity level, which can
lead to inconsistencies.

**Examples:**

- In `V1__init_schema.sql`, there are indexes like `idx_organizations_parent_id` that aren't reflected in the entity
  classes.
- In `V3__security_module.sql`, there are indexes for audit logs and API keys that aren't defined in the entity classes.

**Recommendation:**

- Ensure that all database-level indexes are also defined at the entity level for better documentation and consistency.
- Use `@Table(indexes = {@Index(name = "idx_name", columnList = "column_name")})` for entity-level index definitions.

### 3. Relationship Mapping Issues

#### 3.1 Inconsistent Fetch Types

The codebase uses a mix of eager and lazy fetching without a clear pattern.

**Examples:**

- In `Organization.java`, the `parent` relationship uses `FetchType.LAZY`, but the `children` relationship doesn't
  specify a fetch type.
- In `User.java`, the `roles` relationship uses `FetchType.LAZY`, but other relationships don't specify fetch types.

**Recommendation:**

- Use `FetchType.LAZY` as the default for all relationships to prevent N+1 query issues.
- Only use `FetchType.EAGER` when absolutely necessary and document the reason.

#### 3.2 Inconsistent Cascade Types

Cascade types are not consistently defined across relationships.

**Examples:**

- In `Organization.java`, the `children` relationship uses `CascadeType.ALL`, which might not be appropriate for all
  operations.
- In `Role.java`, the `permissions` relationship doesn't specify cascade types.

**Recommendation:**

- Define explicit cascade types for all relationships based on business requirements.
- Avoid using `CascadeType.ALL` unless all cascade operations are truly needed.
- Document the rationale for cascade choices in comments.

#### 3.3 Missing Join Column Definitions

Some relationships don't have explicit join column definitions, which can lead to non-intuitive column names in the
database.

**Examples:**

- In `User.java`, the `organizations` relationship uses `@JoinColumn(name = "user_id")` but doesn't specify other
  attributes.

**Recommendation:**

- Use explicit `@JoinColumn` annotations for all relationships.
- Include attributes like `nullable`, `foreignKey`, and `updatable` as appropriate.

### 4. Schema-Database Mismatch Issues

#### 4.1 Missing Columns in Database

Some entities reference columns that might not exist in the database schema.

**Examples:**

- In `Role.java`, the entity inherits the `active` field from `AuditableEntity`, but according to the error in
  `tryrun.md`, this column is missing in the `roles` table.

**Recommendation:**

- Ensure that all columns referenced in entity classes exist in the database schema.
- Add missing columns through migration scripts.
- Consider using schema validation during development to catch these issues early.

#### 4.2 Enum Mapping Issues

Enum mappings don't consistently specify the storage type.

**Examples:**

- In `Organization.java`, the `status` field uses `@Enumerated(EnumType.STRING)`, but other enum fields might not
  specify the enum type.

**Recommendation:**

- Consistently use `@Enumerated(EnumType.STRING)` for all enum fields to ensure readability and maintainability.
- Avoid using `EnumType.ORDINAL` as it's brittle to enum order changes.

### 5. Constraint Issues

#### 5.1 Missing Unique Constraints

Some fields that should be unique don't have unique constraints defined.

**Examples:**

- In `Organization.java`, the `code` field has `unique = true` in the `@Column` annotation, but other entities might be
  missing similar constraints.

**Recommendation:**

- Add `unique = true` to all columns that should have unique values.
- For multi-column unique constraints, use
  `@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"column1", "column2"})})`.

#### 5.2 Foreign Key Constraints

Foreign key constraints are often defined at the database level but not at the entity level.

**Recommendation:**

- Use `@JoinColumn(foreignKey = @ForeignKey(name = "fk_name"))` to explicitly define foreign key constraints at the
  entity level.
- Ensure that foreign key names follow a consistent naming convention.

## Specific Entity Issues

### 1. Organization Entity

**File:** `src/main/java/com/devolution/saas/core/organization/domain/model/Organization.java`

**Issues:**

- Extends `AuditableEntity` instead of `TenantAwareEntity`, which is inconsistent with the multi-tenant architecture.
- The `settings` field uses a non-portable `columnDefinition = "jsonb"`.
- No index defined for the `code` field, which is likely used for lookups.
- The `children` relationship uses `CascadeType.ALL`, which might be too aggressive.

**Recommendations:**

- Consider if `Organization` should extend `TenantAwareEntity` for consistency.
- Use a more portable approach for JSON storage, such as a custom converter.
- Add an index for the `code` field.
- Review and potentially restrict the cascade types for the `children` relationship.

### 2. User Entity

**File:** `src/main/java/com/devolution/saas/core/security/domain/model/User.java`

**Issues:**

- Implements `UserDetails` directly, which couples the domain model to Spring Security.
- The `organizations` relationship uses a unidirectional `@OneToMany` with a join column, which is not optimal.
- No indexes defined for frequently queried fields like `username` and `email`.

**Recommendations:**

- Consider separating the domain model from Spring Security implementation details.
- Review the `organizations` relationship mapping for correctness and performance.
- Add indexes for `username` and `email` fields.

### 3. Role Entity

**File:** `src/main/java/com/devolution/saas/core/security/domain/model/Role.java`

**Issues:**

- Has a `systemDefined` field that duplicates functionality from `SystemDefinedEntity`.
- Missing the `active` column in the database according to error reports.
- No index defined for the `name` field, which is likely used for lookups.

**Recommendations:**

- Consider having `Role` extend `SystemDefinedEntity` instead of `TenantAwareEntity`.
- Add the missing `active` column to the database schema.
- Add an index for the `name` field.

### 4. Vehicle Entity

**File:** `src/main/java/com/devolution/saas/insurance/nonlife/auto/domain/model/Vehicle.java`

**Issues:**

- Many string fields don't have length constraints.
- No indexes defined for frequently queried fields like `registrationNumber`.
- Relationships to reference data don't specify fetch types.

**Recommendations:**

- Add appropriate length constraints to all string fields.
- Add indexes for frequently queried fields.
- Explicitly set fetch types for all relationships, preferably to `FetchType.LAZY`.

## Action Plan

1. **Standardize Column Definitions**
    - Add explicit `nullable` attributes to all columns
    - Add appropriate `length` constraints to all string columns
    - Use consistent column naming conventions

2. **Add Missing Indexes**
    - Add entity-level index definitions for frequently queried fields
    - Ensure consistency between database-level and entity-level indexes

3. **Standardize Relationship Mappings**
    - Use `FetchType.LAZY` as the default for all relationships
    - Define explicit cascade types based on business requirements
    - Add proper join column definitions for all relationships

4. **Fix Schema-Database Mismatches**
    - Add missing columns to the database schema
    - Use consistent enum mapping with `EnumType.STRING`

5. **Add Missing Constraints**
    - Add unique constraints for fields that should have unique values
    - Define foreign key constraints at the entity level

## Implementation Priority

1. Fix the missing `active` column in the `roles` table
2. Standardize column definitions in core entities (Organization, User, Role)
3. Add missing indexes for frequently queried fields
4. Standardize relationship mappings
5. Add missing constraints
