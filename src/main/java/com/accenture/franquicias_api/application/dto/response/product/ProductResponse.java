package com.accenture.franquicias_api.application.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de detalles de producto en operaciones CRUD.
 *
 * <p>
 * Contiene toda la información asociada a un producto incluyendo metadata
 * de auditoría. Se utiliza en respuestas de endpoints:
 * POST /api/branches/{branchId}/products, PUT /api/products/{id}/name,
 * PATCH /api/products/{id}/stock, GET /api/branches/{branchId}/products
 * </p>
 *
 * <p>
 * Campos incluidos:
 * <ul>
 *   <li>id: Identificador único del producto</li>
 *   <li>branch_id: ID de la sucursal a la que pertenece</li>
 *   <li>name: Nombre del producto</li>
 *   <li>description: Descripción del producto</li>
 *   <li>stock: Cantidad de stock disponible</li>
 *   <li>price: Precio unitario del producto</li>
 *   <li>created_at: Timestamp de creación</li>
 *   <li>updated_at: Timestamp de última actualización</li>
 *   <li>deleted_at: Timestamp de eliminación (null si activo)</li>
 * </ul>
 * </p>
 *
 * @see ProductCreateRequest
 * @see ProductUpdateNameRequest
 * @see ProductStockUpdateRequest
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
