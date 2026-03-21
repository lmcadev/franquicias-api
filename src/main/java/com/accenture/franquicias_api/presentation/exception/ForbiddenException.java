package com.accenture.franquicias_api.presentation.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Excepción lanzada cuando el usuario no tiene permisos para realizar una acción.
 * 
 * Retorna: HTTP 403 Forbidden
 * ErrorCode: FORBIDDEN
 * 
 * Causas comunes:
 * - Usuario autenticado pero sin rol requerido
 * - Usuario intenta actualizar/eliminar recurso de otro usuario
 * - Rol ADMIN requerido pero usuario es USER o READ_ONLY
 * - Rol USER requerido pero usuario es READ_ONLY
 * 
 * Diferencia con UnauthorizedException:
 * - UnauthorizedException (401): Usuario no autenticado o token inválido
 * - ForbiddenException (403): Usuario autenticado pero sin permisos
 * 
 * @see GlobalExceptionHandler
 */
@Schema(description = "Excepción lanzada cuando el usuario no tiene permisos (HTTP 403)")
public class ForbiddenException extends BusinessException {

    /**
     * Constructor con mensaje personalizado
     * 
     * @param message Descripción de por qué no tiene permisos (ej: "No tienes rol ADMIN")
     */
    public ForbiddenException(String message) {
        super(message, 403, "FORBIDDEN");
    }

    /**
     * Constructor sin parámetros (mensaje por defecto)
     */
    public ForbiddenException() {
        super("No tienes permisos para realizar esta acción", 403, "FORBIDDEN");
    }

    /**
     * Factory method para crear excepción cuando se requiere un rol específico
     * 
     * @param requiredRole Rol requerido (ej: "ADMIN", "USER")
     * @return Nueva instancia de ForbiddenException
     */
    public static ForbiddenException forRole(String requiredRole) {
        return new ForbiddenException(
            String.format("Se requiere rol '%s' para esta operación", requiredRole)
        );
    }
}
