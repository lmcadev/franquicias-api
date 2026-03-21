package com.accenture.franquicias_api.presentation.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO genérico para respuestas de error
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ErrorResponse", description = "Respuesta de error estándar de la API")
public class ErrorResponse {

    @JsonProperty("timestamp")
    @Schema(description = "Marca de tiempo del error", example = "2024-01-15T10:30:00")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    @Schema(description = "Código HTTP de estado", example = "404")
    private int status;

    @JsonProperty("error_code")
    @Schema(description = "Código de error interno (máquina-legible)", example = "RESOURCE_NOT_FOUND")
    private String errorCode;

    @JsonProperty("message")
    @Schema(description = "Mensaje de error legible para el usuario", example = "Franquicia no encontrada: 1")
    private String message;

    @JsonProperty("path")
    @Schema(description = "Ruta de la solicitud que causó el error", example = "/api/franchises/1")
    private String path;

    @JsonProperty("details")
    @Schema(description = "Detalles adicionales del error (stack trace, causas raíz, etc.)", example = "null")
    private String details;
}
