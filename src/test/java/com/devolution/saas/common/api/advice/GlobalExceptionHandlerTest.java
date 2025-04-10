package com.devolution.saas.common.api.advice;

import com.devolution.saas.common.api.dto.ErrorResponse;
import com.devolution.saas.common.domain.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private Environment environment;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void handleBusinessException_ShouldReturnBadRequest() {
        // Given
        BusinessException exception = new BusinessException("business.error", "Business error message");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(exception, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("business.error", errorResponse.getCode());
        assertEquals("Business error message", errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("/api/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleResourceNotFoundException_ShouldReturnNotFound() {
        // Given
        UUID resourceId = UUID.randomUUID();
        ResourceNotFoundException exception = new ResourceNotFoundException("User", resourceId);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("resource.not.found", errorResponse.getCode());
        assertTrue(errorResponse.getMessage().contains(resourceId.toString()));
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("/api/test", errorResponse.getPath());
    }

    @Test
    void handleValidationException_ShouldReturnBadRequest() {
        // Given
        ValidationException exception = new ValidationException("Validation error");
        exception.addError("username", "Username is required");
        exception.addError("email", "Email is invalid");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(exception, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("validation.error", errorResponse.getCode());
        assertEquals("Validation error", errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("/api/test", errorResponse.getPath());
        assertEquals(2, errorResponse.getErrors().size());

        // Verify field errors
        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>(errorResponse.getErrors());
        assertEquals("username", fieldErrors.get(0).getField());
        assertEquals("Username is required", fieldErrors.get(0).getMessage());
        assertEquals("email", fieldErrors.get(1).getField());
        assertEquals("Email is invalid", fieldErrors.get(1).getMessage());
    }

    @Test
    void handleAuthenticationException_ShouldReturnUnauthorized() {
        // Given
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAuthenticationException(exception, request);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("auth.invalid.credentials", errorResponse.getCode());
        assertEquals("Identifiants invalides", errorResponse.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
        assertEquals("/api/test", errorResponse.getPath());
    }

    @Test
    void handleAccessDeniedException_ShouldReturnForbidden() {
        // Given
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDeniedException(exception, request);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("access.denied", errorResponse.getCode());
        assertEquals("Accès refusé", errorResponse.getMessage());
        assertEquals(HttpStatus.FORBIDDEN.value(), errorResponse.getStatus());
        assertEquals("/api/test", errorResponse.getPath());
    }

    @Test
    void handleSecurityException_ShouldReturnForbidden() {
        // Given
        SecurityException exception = new SecurityException("Security error message");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(exception, request);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("security.error", errorResponse.getCode());
        assertEquals("Security error message", errorResponse.getMessage());
    }

    @Test
    void handleTenantException_ShouldReturnBadRequest() {
        // Given
        TenantException exception = TenantException.tenantRequired();

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(exception, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("tenant.required", errorResponse.getCode());
        assertEquals("Un tenant est requis pour cette opération", errorResponse.getMessage());
    }

    @Test
    void handleConcurrencyException_ShouldReturnConflict() {
        // Given
        UUID entityId = UUID.randomUUID();
        ConcurrencyException exception = ConcurrencyException.optimisticLockingConflict("User", entityId);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(exception, request);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("concurrency.optimistic.locking", errorResponse.getCode());
        assertTrue(errorResponse.getMessage().contains(entityId.toString()));
    }

    @Test
    void handleAllUncaughtException_ShouldReturnInternalServerError() {
        // Given
        Exception exception = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAllUncaughtException(exception, request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("internal.error", errorResponse.getCode());
        assertEquals("Une erreur interne est survenue", errorResponse.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals("/api/test", errorResponse.getPath());
    }

    @Test
    void handleAllUncaughtException_InDevEnvironment_ShouldIncludeStackTrace() {
        // Given
        Exception exception = new RuntimeException("Unexpected error");
        when(environment.getActiveProfiles()).thenReturn(new String[0]);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAllUncaughtException(exception, request);

        // Then
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Unexpected error", errorResponse.getMessage());
        assertNotNull(errorResponse.getTrace());
    }

    @Test
    void handleAllUncaughtException_InProdEnvironment_ShouldNotIncludeStackTrace() {
        // Given
        Exception exception = new RuntimeException("Unexpected error");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAllUncaughtException(exception, request);

        // Then
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Une erreur interne est survenue", errorResponse.getMessage());
        assertNull(errorResponse.getTrace());
    }
}
