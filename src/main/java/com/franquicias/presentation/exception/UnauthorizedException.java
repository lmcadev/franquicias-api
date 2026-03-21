package com.franquicias.presentation.exception;

/**
 * Excepción para autenticación fallida (401)
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(message, 401, "UNAUTHORIZED");
    }

    public UnauthorizedException() {
        super("Token inválido o expirado", 401, "UNAUTHORIZED");
    }
}
