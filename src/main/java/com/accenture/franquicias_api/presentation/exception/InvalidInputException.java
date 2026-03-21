package com.accenture.franquicias_api.presentation.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Excepción lanzada cuando la validación de entrada falla.
 * 
 * Retorna: HTTP 400 Bad Request
 * ErrorCode: INVALID_INPUT
 * 
 * Causas comunes:
 * - Campo requerido vacío o nulo
 * - Formato inválido (email, números, etc.)
 * - Longitud de texto fuera de rango
 * - Validación de negocio fallida
 * 
 * Ejemplos:
 * - InvalidInputException("name", "no puede estar vacío")
 * - InvalidInputException("email", "debe ser un email válido")
 * 
 * @see GlobalExceptionHandler
 */
@Schema(description = "Excepción lanzada cuando los datos de entrada son inválidos (HTTP 400)")
public class InvalidInputException extends BusinessException {

    /**
     * Constructor con mensaje personalizado
     * 
     * @param message Mensaje descriptivo de por qué la entrada es inválida
     */
    public InvalidInputException(String message) {
        super(message, 400, "INVALID_INPUT");
    }

    /**
     * Constructor con nombre de campo y razón
     * 
     * @param field Nombre del campo que falló validación
     * @param reason Razón por la que falló (ej: "no puede estar vacío", "debe ser válido")
     */
    public InvalidInputException(String field, String reason) {
        super(String.format("Campo '%s' es inválido: %s", field, reason), 400, "INVALID_INPUT");
    }
}
