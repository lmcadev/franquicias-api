package com.accenture.franquicias_api.presentation.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Excepción base abstracta para todas las excepciones de negocio.
 * 
 * Esta clase proporciona una estructura estándar para las excepciones de la API:
 * - Todas las excepciones de negocio heredan de esta clase
 * - Cada excepción tiene un código HTTP y un errorCode interno
 * - El GlobalExceptionHandler maneja todas las BusinessException y retorna ErrorResponse
 * 
 * @see GlobalExceptionHandler
 */
@Schema(description = "Excepción base para todos los errores de negocio")
public abstract class BusinessException extends RuntimeException {

    private final int httpStatus;
    private final String errorCode;

    /**
     * Constructor de BusinessException
     * 
     * @param message Mensaje de error legible para el usuario
     * @param httpStatus Código HTTP a retornar (400, 401, 403, 404, 409, 500)
     * @param errorCode Código de error interno (ej: INVALID_INPUT, UNAUTHORIZED, RESOURCE_NOT_FOUND)
     */
    public BusinessException(String message, int httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
