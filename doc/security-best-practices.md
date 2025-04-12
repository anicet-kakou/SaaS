# Security Best Practices for SaaS Platform

## Overview

This document outlines security best practices and recommendations for the SaaS multi-tenant insurance platform. It
covers authentication, authorization, data protection, API security, and multi-tenant isolation from a security
perspective.

## Current Security Implementation

The platform currently implements several security mechanisms:

1. **Authentication**
    - JWT-based authentication
    - API key authentication
    - Support for OAuth2 providers (Keycloak, Auth0, etc.)

2. **Authorization**
    - Role-based access control (RBAC)
    - Method-level security with Spring Security annotations
    - Tenant-based data isolation

3. **API Security**
    - Rate limiting
    - Request logging
    - API key validation

4. **Data Protection**
    - Password hashing
    - Tenant data isolation
    - Audit logging

## Security Findings and Recommendations

### 1. Authentication

#### 1.1 JWT Implementation

**Current State:**

- JWT authentication is implemented using `JwtAuthenticationFilter`
- JWT tokens include user information and tenant context
- JWT secrets are configured in properties files

**Issues:**

- JWT secrets are stored in properties files, including version control
- No token revocation mechanism
- JWT expiration times may be too long (86400 seconds / 24 hours)

**Recommendations:**

- Move JWT secrets to environment variables or a secure vault
- Implement a token blacklist for revocation
- Reduce JWT expiration time and implement proper refresh token flow
- Add JWT claims validation (issuer, audience, etc.)
- Consider using asymmetric keys (RS256) instead of symmetric keys (HS256)

```java
@Configuration
public class JwtConfig {
    @Value("${jwt.private-key-location}")
    private String privateKeyLocation;
    
    @Value("${jwt.public-key-location}")
    private String publicKeyLocation;
    
    @Bean
    public RSAPrivateKey jwtSigningKey() {
        // Load private key from secure location
    }
    
    @Bean
    public RSAPublicKey jwtValidationKey() {
        // Load public key from secure location
    }
}
```

#### 1.2 API Key Authentication

**Current State:**

- API keys are stored in the database
- API keys are validated in `ApiKeyAuthenticationFilter`
- API keys are associated with organizations

**Issues:**

- No API key rotation mechanism
- No API key usage restrictions (IP, rate limits)
- API keys have unlimited lifetime

**Recommendations:**

- Implement API key rotation mechanism
- Add API key restrictions (IP whitelist, rate limits)
- Add API key expiration and automatic rotation
- Implement more granular permissions for API keys
- Add comprehensive logging for API key usage

```java
public class ApiKey extends TenantAwareEntity {
    // Existing fields
    
    private LocalDateTime expiresAt;
    private Set<String> allowedIpAddresses;
    private Integer rateLimit;
    private Set<String> allowedOperations;
}
```

### 2. Authorization

#### 2.1 Role-Based Access Control

**Current State:**

- Roles and permissions are defined in the database
- Method-level security with `@PreAuthorize` annotations
- Role hierarchy is not implemented

**Issues:**

- Inconsistent use of authorization annotations
- No clear role hierarchy
- Permissions are not granular enough

**Recommendations:**

- Implement consistent authorization checks across all endpoints
- Define and implement role hierarchy
- Create more granular permissions
- Document role and permission structure
- Implement permission-based (not role-based) authorization

```java
@Configuration
public class RoleHierarchyConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(
            "ROLE_ADMIN > ROLE_MANAGER\n" +
            "ROLE_MANAGER > ROLE_USER"
        );
        return hierarchy;
    }
}
```

#### 2.2 Multi-tenant Authorization

**Current State:**

- Tenant context is set in authentication filters
- `TenantRequired` annotation enforces tenant context presence
- `TenantFilterAspect` adds tenant filtering to queries

**Issues:**

- Tenant context validation is not consistent
- No clear separation between global and tenant-specific resources
- Potential for tenant context bypass

**Recommendations:**

- Implement tenant validation middleware for all API endpoints
- Clearly separate global and tenant-specific resources
- Add tenant context to security context
- Implement tenant-aware authorization evaluator
- Add comprehensive logging for cross-tenant access attempts

```java
public class TenantAwarePermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        // Check if user has permission on the object
        // Verify tenant context matches object's tenant
    }
}
```

### 3. API Security

#### 3.1 Input Validation

**Current State:**

- Bean validation with annotations
- Custom validation in service layer
- Exception handling for validation errors

**Issues:**

- Inconsistent validation across endpoints
- Some endpoints lack proper validation
- Validation error messages are not standardized

**Recommendations:**

- Implement consistent validation across all endpoints
- Add request object validation for all controllers
- Standardize validation error messages
- Implement input sanitization for free-text fields
- Add validation for file uploads and other non-JSON inputs

```java
@RestController
@RequestMapping("/api/v1/organizations")
@Validated
public class OrganizationController {
    @PostMapping
    public ResponseEntity<OrganizationDTO> createOrganization(
            @Valid @RequestBody CreateOrganizationCommand command) {
        // Implementation
    }
}
```

#### 3.2 Rate Limiting and Throttling

**Current State:**

- Basic rate limiting with `RateLimitFilter`
- Global rate limits applied to all endpoints

**Issues:**

- Rate limits are not tenant-specific
- No differentiation between critical and non-critical endpoints
- No gradual throttling mechanism

**Recommendations:**

- Implement tenant-specific rate limits
- Add endpoint-specific rate limits
- Implement gradual throttling (429 responses with increasing backoff)
- Add rate limit headers to responses
- Implement IP-based rate limiting for unauthenticated endpoints

```java
public class TenantAwareRateLimitService {
    public boolean tryConsume(UUID tenantId, String endpoint, int tokens) {
        // Get bucket for tenant and endpoint
        // Try to consume tokens
        // Return true if successful, false if rate limited
    }
}
```

#### 3.3 CSRF Protection

**Current State:**

- CSRF protection is disabled in `SecurityConfig`

**Issues:**

- No CSRF protection for browser-based clients
- No justification for disabling CSRF protection

**Recommendations:**

- Enable CSRF protection for browser-based endpoints
- Use CSRF tokens for form submissions
- Document CSRF protection strategy
- Consider using SameSite cookies for additional protection

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers("/api/**") // Ignore for API endpoints
        )
        // Other configuration
        .build();
}
```

### 4. Data Protection

#### 4.1 Sensitive Data Handling

**Current State:**

- Passwords are hashed with BCrypt
- Some sensitive data is stored in plain text
- No encryption for data at rest

**Issues:**

- Inconsistent handling of sensitive data
- No clear policy for sensitive data classification
- No data masking in logs

**Recommendations:**

- Implement field-level encryption for sensitive data
- Define and document sensitive data classification
- Add data masking for logs containing sensitive information
- Implement secure audit logging for sensitive data access
- Consider using a dedicated secrets management solution

```java
@Entity
public class PaymentInformation extends TenantAwareEntity {
    @Convert(converter = EncryptedStringConverter.class)
    private String cardNumber;
    
    @Convert(converter = EncryptedStringConverter.class)
    private String cvv;
    
    // Other fields
}
```

#### 4.2 Database Security

**Current State:**

- Database credentials in properties files
- No database-level encryption
- No row-level security

**Issues:**

- Database credentials in version control
- No separation of duties at database level
- No database-level tenant isolation

**Recommendations:**

- Move database credentials to environment variables or secrets manager
- Implement database-level encryption for sensitive columns
- Consider implementing row-level security for tenant isolation
- Use separate database users with limited permissions for application
- Implement database connection pooling with proper security settings

```sql
-- Example of row-level security in PostgreSQL
ALTER TABLE organizations ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_isolation_policy ON organizations
    USING (id = current_setting('app.current_tenant_id')::uuid);
```

### 5. Infrastructure Security

#### 5.1 Secure Configuration

**Current State:**

- Configuration in properties files
- Some sensitive values in version control
- No clear separation between environments

**Issues:**

- Sensitive configuration in version control
- Inconsistent configuration across environments
- No validation of security-critical configuration

**Recommendations:**

- Move sensitive configuration to environment variables or secrets manager
- Implement configuration validation at startup
- Create separate configuration profiles for each environment
- Document security-critical configuration options
- Implement configuration auditing

```java
@ConfigurationProperties(prefix = "security")
@Validated
public class SecurityConfigProperties {
    @NotBlank
    private String jwtIssuer;
    
    @NotBlank
    private String jwtAudience;
    
    @Min(300) // Minimum 5 minutes
    @Max(3600) // Maximum 1 hour
    private int jwtExpirationSeconds;
    
    // Getters and setters
}
```

#### 5.2 Logging and Monitoring

**Current State:**

- Basic logging with Logback
- No centralized monitoring
- Debug level logging in some environments

**Issues:**

- Excessive logging in production
- Potential for sensitive data in logs
- No security event monitoring

**Recommendations:**

- Implement structured logging with JSON format
- Add correlation IDs for request tracing
- Implement log masking for sensitive data
- Create security event logging for authentication and authorization events
- Set up centralized log collection and analysis
- Implement alerts for security-related events

```java
@Aspect
@Component
public class SecurityEventLoggingAspect {
    @AfterReturning("execution(* com.devolution.saas.core.security..*(..))")
    public void logSecurityEvent(JoinPoint joinPoint) {
        // Log security-related events with structured format
    }
}
```

## Security Checklist

Use this checklist to verify that security best practices are being followed:

### Authentication

- [ ] JWT secrets stored securely (not in version control)
- [ ] JWT tokens have appropriate expiration time
- [ ] Refresh token mechanism implemented
- [ ] API keys have expiration and rotation mechanism
- [ ] Password storage uses strong hashing algorithm (BCrypt)
- [ ] Multi-factor authentication supported

### Authorization

- [ ] All endpoints have appropriate authorization checks
- [ ] Role hierarchy defined and implemented
- [ ] Permissions are granular and well-documented
- [ ] Tenant context validated for all tenant-specific operations
- [ ] Authorization decisions logged for audit purposes

### API Security

- [ ] All endpoints have appropriate input validation
- [ ] Rate limiting implemented for all endpoints
- [ ] CSRF protection enabled for browser-based clients
- [ ] Security headers set (Content-Security-Policy, X-XSS-Protection, etc.)
- [ ] API documentation does not expose sensitive information

### Data Protection

- [ ] Sensitive data encrypted at rest
- [ ] Sensitive data masked in logs
- [ ] Database credentials stored securely
- [ ] Tenant data properly isolated
- [ ] Audit logging for sensitive data access

### Infrastructure Security

- [ ] Secure configuration management
- [ ] Environment-specific security settings
- [ ] Centralized logging and monitoring
- [ ] Security event alerting
- [ ] Regular security scanning and testing

## Implementation Roadmap

### Phase 1: Critical Security Improvements (1-2 weeks)

1. Move sensitive configuration to environment variables
2. Enable CSRF protection for browser-based clients
3. Implement consistent authorization checks
4. Add input validation for all endpoints

### Phase 2: Authentication Enhancements (2-3 weeks)

1. Improve JWT implementation with proper key management
2. Implement token revocation mechanism
3. Enhance API key security with restrictions and expiration
4. Add comprehensive logging for authentication events

### Phase 3: Data Protection (3-4 weeks)

1. Implement field-level encryption for sensitive data
2. Add data masking for logs
3. Improve database security with row-level security
4. Enhance audit logging for sensitive operations

### Phase 4: Monitoring and Continuous Improvement (Ongoing)

1. Set up centralized security monitoring
2. Implement security event alerting
3. Conduct regular security reviews
4. Stay updated on security best practices and vulnerabilities

## Conclusion

Implementing these security best practices will significantly enhance the security posture of the SaaS platform. By
addressing authentication, authorization, API security, and data protection concerns, the platform will be better
protected against common security threats while maintaining compliance with industry standards.

The recommended approach is to prioritize critical security improvements first, followed by systematic enhancements to
authentication, authorization, and data protection mechanisms. Regular security reviews and continuous monitoring will
ensure that the platform remains secure as it evolves.
