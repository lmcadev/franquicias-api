package com.accenture.franquicias_api.application.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de producto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductResponse", description = "Respuesta con detalles de producto")
public class ProductResponse {

    @JsonProperty("id")
    @Schema(description = "ID único del producto", example = "1")
    private Long id;

    @JsonProperty("branch_id")
    @Schema(description = "ID de la sucursal a la que pertenece", example = "1")
    private Long branchId;

    @JsonProperty("name")
    @Schema(description = "Nombre del producto", example = "Desarrollo web")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Descripción del producto", example = "Servicio de desarrollo web")
    private String description;

    @JsonProperty("stock")
    @Schema(description = "Cantidad de stock disponible", example = "100")
    private Integer stock;

    @JsonProperty("price")
    @Schema(description = "Precio unitario del producto", example = "500.000")
    private Double price;

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
