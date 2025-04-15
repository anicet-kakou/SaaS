package com.devolution.saas.common.exception;

import java.util.UUID;

/**
 * Exception thrown when a resource is not found.
 * This is a common exception that can be used across all modules.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceType;
    private final String identifier;
    private final String identifierValue;

    /**
     * Creates a new ResourceNotFoundException for a resource with the given ID.
     *
     * @param resourceType The type of resource that was not found
     * @param id           The ID of the resource that was not found
     */
    public ResourceNotFoundException(String resourceType, UUID id) {
        super(String.format("%s not found with ID: %s", resourceType, id));
        this.resourceType = resourceType;
        this.identifier = "ID";
        this.identifierValue = id.toString();
    }

    /**
     * Creates a new ResourceNotFoundException for a resource with the given identifier.
     *
     * @param resourceType    The type of resource that was not found
     * @param identifier      The type of identifier used to look up the resource
     * @param identifierValue The value of the identifier
     */
    public ResourceNotFoundException(String resourceType, String identifier, String identifierValue) {
        super(String.format("%s not found with %s: %s", resourceType, identifier, identifierValue));
        this.resourceType = resourceType;
        this.identifier = identifier;
        this.identifierValue = identifierValue;
    }

    /**
     * Factory method to create a ResourceNotFoundException for a resource with the given ID.
     *
     * @param resourceType The type of resource that was not found
     * @param id           The ID of the resource that was not found
     * @return A new ResourceNotFoundException
     */
    public static ResourceNotFoundException forId(String resourceType, UUID id) {
        return new ResourceNotFoundException(resourceType, id);
    }

    /**
     * Factory method to create a ResourceNotFoundException for a resource with the given code.
     *
     * @param resourceType The type of resource that was not found
     * @param code         The code of the resource that was not found
     * @return A new ResourceNotFoundException
     */
    public static ResourceNotFoundException forCode(String resourceType, String code) {
        return new ResourceNotFoundException(resourceType, "code", code);
    }

    /**
     * Gets the type of resource that was not found.
     *
     * @return The resource type
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * Gets the type of identifier used to look up the resource.
     *
     * @return The identifier type
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the value of the identifier.
     *
     * @return The identifier value
     */
    public String getIdentifierValue() {
        return identifierValue;
    }
}
