package com.devolution.saas.common.api.advice;

import com.devolution.saas.common.api.dto.ErrorResponse;
import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ConcurrencyException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.SecurityException;
import com.devolution.saas.common.domain.exception.TenantException;
import com.devolution.saas.common.domain.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Gestionnaire global d'exceptions pour l'API.
 * Intercepte toutes les exceptions lancées par les contrôleurs et les convertit en réponses d'erreur standardisées.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String PROD_PROFILE = "prod";
    private final Environment environment;

    /**
     * Constructeur.
     *
     * @param environment Environnement Spring
     */
    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    /**
     * Gère les exceptions métier.
     *
     * @param ex      Exception métier
     * @param request Requête HTTP
     * @return Réponse d'erreur
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn("BusinessException: {}", ex.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Déterminer le statut HTTP en fonction du type d'exception
        if (ex instanceof ConcurrencyException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof SecurityException) {
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof TenantException && "tenant.not.authorized".equals(ex.getCode())) {
            status = HttpStatus.FORBIDDEN;
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .status(status.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Gère les exceptions de ressource non trouvée.
     *
     * @param ex      Exception de ressource non trouvée
     * @param request Requête HTTP
     * @return Réponse d'erreur
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("ResourceNotFoundException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les exceptions de validation.
     *
     * @param ex      Exception de validation
     * @param request Requête HTTP
     * @return Réponse d'erreur
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, HttpServletRequest request) {
        log.warn("ValidationException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        if (ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            errorResponse.addValidationErrors(ex.getErrors());
        }

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions d'authentification.
     *
     * @param ex      Exception d'authentification
     * @param request Requête HTTP
     * @return Réponse d'erreur
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.warn("AuthenticationException: {}", ex.getMessage());

        String code = "auth.error";
        String message = "Erreur d'authentification";

        if (ex instanceof BadCredentialsException) {
            code = "auth.invalid.credentials";
            message = "Identifiants invalides";
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(code)
                .message(message)
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gère les exceptions d'accès refusé.
     *
     * @param ex      Exception d'accès refusé
     * @param request Requête HTTP
     * @return Réponse d'erreur
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("AccessDeniedException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("access.denied")
                .message("Accès refusé")
                .status(HttpStatus.FORBIDDEN.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Gère les exceptions de taille de fichier dépassée.
     * Cette méthode est commentée car elle entre en conflit avec la méthode héritée
     * de ResponseEntityExceptionHandler qui gère déjà cette exception.
     *
     * @param ex      Exception de taille de fichier dépassée
     * @param request Requête HTTP
     * @return Réponse d'erreur
     */
    // @ExceptionHandler(MaxUploadSizeExceededException.class)
    // public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
    //     log.warn("MaxUploadSizeExceededException: {}", ex.getMessage());
    //
    //     ErrorResponse errorResponse = ErrorResponse.builder()
    //             .code("file.size.exceeded")
    //             .message("La taille du fichier dépasse la limite autorisée")
    //             .status(HttpStatus.PAYLOAD_TOO_LARGE.value())
    //             .path(request.getRequestURI())
    //             .timestamp(LocalDateTime.now())
    //             .build();
    //
    //     if (isDevEnvironment()) {
    //         errorResponse.setTrace(getStackTrace(ex));
    //     }
    //
    //     return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    // }

    /**
     * Gère les exceptions de type d'argument de méthode incorrect.
     *
     * @param ex      Exception de type d'argument de méthode incorrect
     * @param request Requête HTTP
     * @return Réponse d'erreur
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.warn("MethodArgumentTypeMismatchException: {}", ex.getMessage());

        String message = String.format("Le paramètre '%s' de valeur '%s' ne peut pas être converti en type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "inconnu");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("argument.type.mismatch")
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions non gérées.
     *
     * @param ex      Exception non gérée
     * @param request Requête HTTP
     * @return Réponse d'erreur
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex, HttpServletRequest request) {
        log.error("Exception non gérée: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("internal.error")
                .message("Une erreur interne est survenue")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
            errorResponse.setMessage(ex.getMessage());
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gère les exceptions de méthode HTTP non supportée.
     *
     * @param ex      Exception de méthode HTTP non supportée
     * @param headers En-têtes HTTP
     * @param status  Statut HTTP
     * @param request Requête web
     * @return Réponse d'erreur
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("HttpRequestMethodNotSupportedException: {}", ex.getMessage());

        StringBuilder builder = new StringBuilder();
        builder.append("La méthode ");
        builder.append(ex.getMethod());
        builder.append(" n'est pas supportée pour cette requête. Les méthodes supportées sont ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("method.not.supported")
                .message(builder.toString())
                .status(status instanceof HttpStatus ? ((HttpStatus) status).value() : status.value())
                .path(getRequestPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Gère les exceptions de type de média non supporté.
     *
     * @param ex      Exception de type de média non supporté
     * @param headers En-têtes HTTP
     * @param status  Statut HTTP
     * @param request Requête web
     * @return Réponse d'erreur
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("HttpMediaTypeNotSupportedException: {}", ex.getMessage());

        StringBuilder builder = new StringBuilder();
        builder.append("Le type de média ");
        builder.append(ex.getContentType());
        builder.append(" n'est pas supporté. Les types de média supportés sont ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("media.type.not.supported")
                .message(builder.toString())
                .status(status instanceof HttpStatus ? ((HttpStatus) status).value() : status.value())
                .path(getRequestPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Gère les exceptions de paramètre de requête manquant.
     *
     * @param ex      Exception de paramètre de requête manquant
     * @param headers En-têtes HTTP
     * @param status  Statut HTTP
     * @param request Requête web
     * @return Réponse d'erreur
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("MissingServletRequestParameterException: {}", ex.getMessage());

        String message = String.format("Le paramètre '%s' de type '%s' est manquant", ex.getParameterName(), ex.getParameterType());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("parameter.missing")
                .message(message)
                .status(status instanceof HttpStatus ? ((HttpStatus) status).value() : status.value())
                .path(getRequestPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Gère les exceptions de message HTTP non lisible.
     *
     * @param ex      Exception de message HTTP non lisible
     * @param headers En-têtes HTTP
     * @param status  Statut HTTP
     * @param request Requête web
     * @return Réponse d'erreur
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("HttpMessageNotReadableException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("message.not.readable")
                .message("Le corps de la requête est invalide")
                .status(status instanceof HttpStatus ? ((HttpStatus) status).value() : status.value())
                .path(getRequestPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
            errorResponse.setMessage(ex.getMessage());
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Gère les exceptions d'argument de méthode non valide.
     *
     * @param ex      Exception d'argument de méthode non valide
     * @param headers En-têtes HTTP
     * @param status  Statut HTTP
     * @param request Requête web
     * @return Réponse d'erreur
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("MethodArgumentNotValidException: {}", ex.getMessage());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("validation.error")
                .message("Erreur de validation")
                .status(status instanceof HttpStatus ? ((HttpStatus) status).value() : status.value())
                .path(getRequestPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        fieldErrors.forEach(fieldError ->
                errorResponse.addFieldError(fieldError.getField(), fieldError.getDefaultMessage())
        );

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Gère les exceptions de liaison.
     *
     * @param ex      Exception de liaison
     * @param headers En-têtes HTTP
     * @param status  Statut HTTP
     * @param request Requête web
     * @return Réponse d'erreur
     */
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("BindException: {}", ex.getMessage());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("binding.error")
                .message("Erreur de liaison")
                .status(status instanceof HttpStatus ? ((HttpStatus) status).value() : status.value())
                .path(getRequestPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        fieldErrors.forEach(fieldError ->
                errorResponse.addFieldError(fieldError.getField(), fieldError.getDefaultMessage())
        );

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Gère les exceptions de type incompatible.
     *
     * @param ex      Exception de type incompatible
     * @param headers En-têtes HTTP
     * @param status  Statut HTTP
     * @param request Requête web
     * @return Réponse d'erreur
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("TypeMismatchException: {}", ex.getMessage());

        String message = String.format("La valeur '%s' pour la propriété '%s' ne peut pas être convertie en type '%s'",
                ex.getValue(), ex.getPropertyName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "inconnu");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("type.mismatch")
                .message(message)
                .status(status instanceof HttpStatus ? ((HttpStatus) status).value() : status.value())
                .path(getRequestPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Gère les exceptions de gestionnaire non trouvé.
     *
     * @param ex      Exception de gestionnaire non trouvé
     * @param headers En-têtes HTTP
     * @param status  Statut HTTP
     * @param request Requête web
     * @return Réponse d'erreur
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("NoHandlerFoundException: {}", ex.getMessage());

        String message = String.format("Aucun gestionnaire trouvé pour %s %s", ex.getHttpMethod(), ex.getRequestURL());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("no.handler")
                .message(message)
                .status(status instanceof HttpStatus ? ((HttpStatus) status).value() : status.value())
                .path(getRequestPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        if (isDevEnvironment()) {
            errorResponse.setTrace(getStackTrace(ex));
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Vérifie si l'environnement est de développement.
     *
     * @return true si l'environnement est de développement, false sinon
     */
    private boolean isDevEnvironment() {
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles.length == 0 || !Arrays.asList(activeProfiles).contains(PROD_PROFILE);
    }

    /**
     * Récupère la trace de la pile d'une exception.
     *
     * @param ex Exception
     * @return Trace de la pile
     */
    private String getStackTrace(Throwable ex) {
        return Arrays.stream(ex.getStackTrace())
                .limit(10)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Récupère le chemin de la requête.
     *
     * @param request Requête web
     * @return Chemin de la requête
     */
    private String getRequestPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return ((ServletWebRequest) request).getRequest().getRequestURI();
        }
        return "";
    }
}
