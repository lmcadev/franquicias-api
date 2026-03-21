package com.franquicias.presentation.exception;

/**
 * Excepción para conflictos (409) - ej: nombres duplicados
 */
public class ConflictException extends BusinessException {

    public ConflictException(String message) {
        super(message, 409, "CONFLICT");
    }

    public ConflictException(String resource, String field, String value) {
        super(String.format("%s con %s '%s' ya existe", resource, field, value), 409, "CONFLICT");
    }
}
