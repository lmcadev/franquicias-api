package com.accenture.franquicias_api.presentation.exception;

/**
 * Excepción base para todas las excepciones de negocio
 */
public abstract class BusinessException extends RuntimeException {

    private final int httpStatus;
    private final String errorCode;

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
