package com.accenture.franquicias_api.presentation.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Excepción lanzada cuando un recurso solicitado no es encontrado en la base de datos.
 * 
 * Retorna: HTTP 404 Not Found
 * ErrorCode: RESOURCE_NOT_FOUND
 * 
 * Causas comunes:
 * - GET /api/franchises/999 (franquicia no existe)
 * - PUT /api/franchises/999 (intento actualizar franquicia no existente)
 * - DELETE /api/franchises/999 (intento eliminar franquicia no existente)
 * 
 * @see GlobalExceptionHandler
 */
@Schema(description = "Excepción lanzada cuando un recurso no es encontrado (HTTP 404)")
public class ResourceNotFoundException extends BusinessException {

    /**
     * Constructor con mensaje personalizado
     * 
     * @param message Mensaje descriptivo del error (ej: "Franquicia no encontrada")
     */
    public ResourceNotFoundException(String message) {
        super(message, 404, "RESOURCE_NOT_FOUND");
    }

    /**
     * Constructor con tipo de recurso e identificador
     * 
     * @param resourceType Tipo de recurso (ej: "Franquicia", "Sucursal", "Producto")
     * @param identifier Identificador del recurso (ej: ID, email, etc.)
     */
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s no encontrado(a): %s", resourceType, identifier), 404, "RESOURCE_NOT_FOUND");
    }
}
