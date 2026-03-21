package com.accenture.franquicias_api.presentation.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Excepción lanzada cuando la autenticación falla o el token es inválido.
 * 
 * Retorna: HTTP 401 Unauthorized
 * ErrorCode: UNAUTHORIZED
 * 
 * Causas comunes:
 * - Header Authorization ausente
 * - Token JWT inválido
 * - Token JWT expirado
 * - Firma JWT no válida
 * - Intento de acceder a endpoint protegido sin autenticación
 * 
 * Nota: SecurityFilter de la API valida tokens JWT y lanza esta excepción
 * 
 * @see GlobalExceptionHandler
 */
@Schema(description = "Excepción lanzada cuando la autenticación falla (HTTP 401)")
public class UnauthorizedException extends BusinessException {

    /**
     * Constructor con mensaje personalizado
     * 
     * @param message Mensaje de error (ej: "Token inválido o expirado")
     */
    public UnauthorizedException(String message) {
        super(message, 401, "UNAUTHORIZED");
    }

    /**
     * Constructor sin parámetros (mensaje por defecto)
     */
    public UnauthorizedException() {
        super("Token inválido o expirado", 401, "UNAUTHORIZED");
    }
}
