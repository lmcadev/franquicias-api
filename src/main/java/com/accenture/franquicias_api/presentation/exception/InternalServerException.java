package com.accenture.franquicias_api.presentation.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Excepción lanzada cuando ocurre un error inesperado en el servidor.
 * 
 * Retorna: HTTP 500 Internal Server Error
 * ErrorCode: INTERNAL_SERVER_ERROR
 * 
 * Causas comunes:
 * - Error en la base de datos (conexión, query, transacción)
 * - Error en mapeo de entidades
 * - Error en repositorio o UseCase
 * - Excepción no manejada
 * - Timeout en operación reactiva
 * 
 * Nota: GlobalExceptionHandler automáticamente captura excepciones no manejadas
 * y retorna HTTP 500 con esta excepción
 * 
 * @see GlobalExceptionHandler
 */
@Schema(description = "Excepción lanzada cuando ocurre un error interno del servidor (HTTP 500)")
public class InternalServerException extends BusinessException {

    /**
     * Constructor con mensaje personalizado
     * 
     * @param message Descripción del error
     */
    public InternalServerException(String message) {
        super(message, 500, "INTERNAL_SERVER_ERROR");
    }

    /**
     * Constructor con mensaje y causa raíz
     * 
     * @param message Descripción del error
     * @param cause Excepción original que causó el error
     */
    public InternalServerException(String message, Throwable cause) {
        super(message, 500, "INTERNAL_SERVER_ERROR");
        initCause(cause);
    }

    /**
     * Constructor sin parámetros (mensaje genérico)
     */
    public InternalServerException() {
        super("Error interno del servidor", 500, "INTERNAL_SERVER_ERROR");
    }
}
