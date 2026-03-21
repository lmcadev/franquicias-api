package com.accenture.franquicias_api.application.dto.response.franchise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de detalles de franquiciaen operaciones CRUD.
 *
 * <p>
 * Contiene toda la información associated a una franquicia incluyendo metadata
 * de auditoría. Se utiliza en respuestas de endpoints:
 * POST /api/franchises, PUT /api/franchises/{id}, GET /api/franchises/{id}
 * </p>
 *
 * <p>
 * Campos incluidos:
 * <ul>
 *   <li>id: Identificador único de la franquicia</li>
 *   <li>name: Nombre de la franquicia</li>
 *   <li>description: Descripción de la franquicia</li>
 *   <li>created_by: ID del usuario propietario de la franquicia</li>
 *   <li>created_at: Timestamp de creación</li>
 *   <li>updated_at: Timestamp de última actualización</li>
 *   <li>deleted_at: Timestamp de eliminación (null si activa)</li>
 * </ul>
 * </p>
 *
 * @see FranchiseCreateRequest
 * @see FranchiseUpdateRequest
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
