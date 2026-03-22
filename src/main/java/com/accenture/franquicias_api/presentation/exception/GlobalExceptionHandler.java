package com.accenture.franquicias_api.presentation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;

/**
 * Manejador global de excepciones para la API REST.
 *
 * <p>
 * Captura todas las excepciones lanzadas en controladores y las convierte
 * en respuestas HTTP consistentes:
 * <ul>
 *   <li>BusinessException y subclases: Respuesta con status y errorCode específicos</li>
 *   <li>Excepciones genéricas: 500 Internal Server Error con detalles</li>
 *   <li>Todas incluyen: timestamp, status, message, path, detalles opcionales</li>
 * </ul>
 * </p>
 *
 * <p>
 * Ejemplo de respuesta:
 * <pre>
 * {
 *   "timestamp": "2024-01-15T10:30:00",
 *   "status": 404,
 *   "errorCode": "RESOURCE_NOT_FOUND",
 *   "message": "Franquicia no encontrada",
 *   "path": "/api/franchises/999"
 * }
 * </pre>
 * </p>
 *
 * @see BusinessException
 * @see ErrorResponse
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, ServerWebExchange exchange) {
        log.warn("BusinessException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getHttpStatus())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, ServerWebExchange exchange) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        log.warn("ResponseStatusException: {} {}", status.value(), ex.getReason());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .errorCode(status.name())
                .message(ex.getReason() != null ? ex.getReason() : status.getReasonPhrase())
                .path(exchange.getRequest().getPath().value())
                .details(ex.getMessage())
                .build();

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, ServerWebExchange exchange) {
        log.error("Unhandled exception", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("Error interno del servidor")
                .path(exchange.getRequest().getPath().value())
                .details(ex.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
