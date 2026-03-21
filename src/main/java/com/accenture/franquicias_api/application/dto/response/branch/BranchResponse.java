package com.accenture.franquicias_api.application.dto.response.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de detalles de sucursal en operaciones CRUD.
 *
 * <p>
 * Contiene toda la información asociada a una sucursal incluyendo metadata
 * de auditoría. Se utiliza en respuestas de endpoints:
 * POST /api/franchises/{franchiseId}/branches, PUT /api/branches/{id},
 * GET /api/franchises/{franchiseId}/branches
 * </p>
 *
 * <p>
 * Campos incluidos:
 * <ul>
 *   <li>id: Identificador único de la sucursal</li>
 *   <li>franchise_id: ID de la franquicia padre</li>
 *   <li>name: Nombre de la sucursal</li>
 *   <li>address: Dirección física de la sucursal</li>
 *   <li>city: Ciudad donde está ubicada</li>
 *   <li>created_at: Timestamp de creación</li>
 *   <li>updated_at: Timestamp de última actualización</li>
 *   <li>deleted_at: Timestamp de eliminación (null si activa)</li>
 * </ul>
 * </p>
 *
 * @see BranchCreateRequest
 * @see BranchUpdateRequest
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BranchResponse", description = "Respuesta con detalles de sucursal")
public class BranchResponse {

    @JsonProperty("id")
    @Schema(description = "ID único de la sucursal", example = "1")
    private Long id;

    @JsonProperty("franchise_id")
    @Schema(description = "ID de la franquicia a la que pertenece", example = "1")
    private Long franchiseId;

    @JsonProperty("name")
    @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
    private String name;

    @JsonProperty("address")
    @Schema(description = "Dirección de la sucursal", example = "Calle 50 #123")
    private String address;

    @JsonProperty("city")
    @Schema(description = "Ciudad donde está ubicada la sucursal", example = "Bogotá")
    private String city;

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
