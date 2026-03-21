package com.accenture.franquicias_api.presentation.exception;

/**
 * Excepción cuando un recurso no es encontrado (404)
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(message, 404, "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s no encontrado(a): %s", resourceType, identifier), 404, "RESOURCE_NOT_FOUND");
    }
}
