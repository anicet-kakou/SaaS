# JPA Annotation Standards

This document defines the standards for JPA annotations in the SaaS platform to ensure consistency and best practices
across the codebase.

## Entity Annotations

### Basic Entity Structure

```java
@Entity
@Table(
        name = "table_name",
        indexes = {
                @Index(name = "idx_table_field1", columnList = "field1"),
                @Index(name = "idx_table_field2", columnList = "field2")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_table_field3", columnNames = {"field3"})
        }
)
@Getter
@Setter
public class EntityName extends BaseEntity {
    // Fields and methods
}
```

### Entity Inheritance

- Use the appropriate base entity class based on the entity's requirements:
    - `BaseEntity`: For basic entities with ID and audit fields
    - `AuditableEntity`: For entities that need soft delete functionality
    - `TenantAwareEntity`: For tenant-specific entities
    - `SystemDefinedEntity`: For entities that can be defined by the system

### Column Definitions

```java
// Required string field with length constraint
@Column(name = "field_name", nullable = false, length = 100)
private String fieldName;

// Optional string field with length constraint
@Column(name = "optional_field", length = 255)
private String optionalField;

// Numeric field with precision and scale
@Column(name = "amount", precision = 10, scale = 2)
private BigDecimal amount;

// Boolean field with default value
@Column(name = "active", nullable = false)
private boolean active = true;

// Enum field
@Enumerated(EnumType.STRING)
@Column(name = "status", nullable = false)
private EntityStatus status = EntityStatus.ACTIVE;

// Date field
@Column(name = "effective_date", nullable = false)
private LocalDate effectiveDate;

// Timestamp field
@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;
```

### Field Naming Conventions

- Use camelCase for Java field names
- Use snake_case for database column names
- Always specify the column name explicitly with `name` attribute

## Relationship Mappings

### Many-to-One Relationship

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(
        name = "parent_id",
        foreignKey = @ForeignKey(name = "fk_entity_parent")
)
private Parent parent;
```

### One-to-Many Relationship

```java
@OneToMany(
        mappedBy = "parent",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true,
        fetch = FetchType.LAZY
)
private Set<Child> children = new HashSet<>();
```

### Many-to-Many Relationship

```java
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(
        name = "entity_related",
        joinColumns = @JoinColumn(
                name = "entity_id",
                foreignKey = @ForeignKey(name = "fk_entity_related_entity")
        ),
        inverseJoinColumns = @JoinColumn(
                name = "related_id",
                foreignKey = @ForeignKey(name = "fk_entity_related_related")
        )
)
private Set<Related> relatedEntities = new HashSet<>();
```

### One-to-One Relationship

```java
@OneToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY,
        optional = false
)
@JoinColumn(
        name = "details_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_entity_details")
)
private EntityDetails details;
```

## Fetch Types

- Use `FetchType.LAZY` as the default for all relationships
- Only use `FetchType.EAGER` when absolutely necessary and document the reason
- Use fetch joins in repository queries for specific use cases that need eager loading

## Cascade Types

- Be explicit about cascade types
- Avoid using `CascadeType.ALL` unless all cascade operations are truly needed
- Common patterns:
    - `{CascadeType.PERSIST, CascadeType.MERGE}`: For most parent-child relationships
    - `CascadeType.ALL` with `orphanRemoval = true`: For true composition relationships

## Index Naming Conventions

- Use the format `idx_table_column(s)` for index names
- For multi-column indexes, include all column names: `idx_table_col1_col2`

## Foreign Key Naming Conventions

- Use the format `fk_table_referenced_table` for foreign key names
- For multiple foreign keys to the same table, add a descriptor: `fk_table_role_referenced_table`

## Unique Constraint Naming Conventions

- Use the format `uk_table_column(s)` for unique constraint names
- For multi-column constraints, include all column names: `uk_table_col1_col2`

## Best Practices

1. **Always specify column nullability**
    - Use `nullable = false` for required fields
    - Default is `nullable = true` for optional fields, but be explicit when possible

2. **Add length constraints to all string columns**
    - Choose appropriate lengths based on business requirements
    - Common lengths:
        - Short codes: 10-20 characters
        - Names: 50-100 characters
        - Descriptions: 255-1000 characters
        - URLs: 255 characters

3. **Use appropriate precision and scale for numeric fields**
    - Money amounts: precision=19, scale=4
    - Percentages: precision=5, scale=2
    - Quantities: precision based on maximum expected value

4. **Add indexes for frequently queried fields**
    - Primary keys are automatically indexed
    - Foreign keys should generally be indexed
    - Fields used in WHERE clauses should be indexed
    - Consider composite indexes for fields frequently used together

5. **Be cautious with bidirectional relationships**
    - Only use bidirectional relationships when navigation in both directions is needed
    - Properly maintain both sides of bidirectional relationships
    - Consider using unidirectional relationships when possible

6. **Document non-standard choices**
    - If deviating from these standards, add a comment explaining why
    - Document database-specific features (like PostgreSQL jsonb)
