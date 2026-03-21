package com.franquicias.presentation.exception;

/**
 * Excepción para validación fallida (400)
 */
public class InvalidInputException extends BusinessException {

    public InvalidInputException(String message) {
        super(message, 400, "INVALID_INPUT");
    }

    public InvalidInputException(String field, String reason) {
        super(String.format("Campo '%s' es inválido: %s", field, reason), 400, "INVALID_INPUT");
    }
}
