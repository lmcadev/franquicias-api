package com.accenture.franquicias_api.presentation.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Excepción lanzada cuando hay un conflicto en los datos (violación de restricción única, etc.).
 * 
 * Retorna: HTTP 409 Conflict
 * ErrorCode: CONFLICT
 * 
 * Causas comunes:
 * - Nombre de franquicia ya existe
 * - Email de usuario ya existe
 * - Violación de constraint UNIQUE en la base de datos
 * - Intento de crear recurso duplicado
 * 
 * Ejemplos:
 * - POST /api/franchises con nombre que ya existe
 * - POST /auth/register con email que ya está registrado
 * 
 * @see GlobalExceptionHandler
 */
@Schema(description = "Excepción lanzada cuando hay un conflicto en los datos (HTTP 409)")
public class ConflictException extends BusinessException {

    /**
     * Constructor con mensaje personalizado
     * 
     * @param message Descripción del conflicto
     */
    public ConflictException(String message) {
        super(message, 409, "CONFLICT");
    }

    /**
     * Constructor con detalles de recurso y campo
     * 
     * @param resource Tipo de recurso (ej: "Franquicia", "Usuario")
     * @param field Campo que causó el conflicto (ej: "nombre", "email")
     * @param value Valor que causó el conflicto
     */
    public ConflictException(String resource, String field, String value) {
        super(String.format("%s con %s '%s' ya existe", resource, field, value), 409, "CONFLICT");
    }
}
