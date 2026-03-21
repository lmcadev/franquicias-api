package com.franquicias.presentation.exception;

/**
 * Excepción para falta de permisos (403)
 */
public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(message, 403, "FORBIDDEN");
    }

    public ForbiddenException() {
        super("No tienes permisos para realizar esta acción", 403, "FORBIDDEN");
    }

    public static ForbiddenException forRole(String requiredRole) {
        return new ForbiddenException(
            String.format("Se requiere rol '%s' para esta operación", requiredRole)
        );
    }
}
