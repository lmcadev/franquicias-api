package com.accenture.franquicias_api.presentation.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ErrorResponse {

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    private int status;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("path")
    private String path;

    @JsonProperty("details")
    private String details;
}
