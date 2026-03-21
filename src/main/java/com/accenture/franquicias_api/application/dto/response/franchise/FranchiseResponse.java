package com.accenture.franquicias_api.application.dto.response.franchise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de franquicia
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FranchiseResponse", description = "Respuesta con detalles de franquicia")
public class FranchiseResponse {

    @JsonProperty("id")
    @Schema(description = "ID único de la franquicia", example = "1")
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Nombre de la franquicia", example = "Accenture Medellín")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Descripción de la franquicia", example = "Servicios de consultoría")
    private String description;

    @JsonProperty("created_by")
    @Schema(description = "ID del usuario que creó la franquicia", example = "1")
    private Long createdBy;

    @JsonProperty("created_at")
    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Schema(description = "Fecha de última actualización", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;

    @JsonProperty("deleted_at")
    @Schema(description = "Fecha de eliminación (soft delete)", example = "null")
    private LocalDateTime deletedAt;
}
