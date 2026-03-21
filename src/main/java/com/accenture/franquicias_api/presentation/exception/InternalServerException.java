package com.accenture.franquicias_api.presentation.exception;

/**
 * Excepción para errores internos del servidor (500)
 */
public class InternalServerException extends BusinessException {

    public InternalServerException(String message) {
        super(message, 500, "INTERNAL_SERVER_ERROR");
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, 500, "INTERNAL_SERVER_ERROR");
        initCause(cause);
    }

    public InternalServerException() {
        super("Error interno del servidor", 500, "INTERNAL_SERVER_ERROR");
    }
}
