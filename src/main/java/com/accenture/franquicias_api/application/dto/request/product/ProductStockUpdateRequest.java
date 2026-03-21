package com.accenture.franquicias_api.application.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de actualización del stock de un producto.
 *
 * <p>
 * Utilizado en el endpoint PATCH /api/products/{id}/stock para actualizar
 * únicamente la cantidad de stock de un producto existente.
 * </p>
 *
 * <p>
 * Validaciones:
 * <ul>
 *   <li>Stock: requerido, mínimo 0 (no puede ser negativo)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Respuesta esperada: {@link ProductResponse}
 * </p>
 *
 * @see ProductResponse
 * @see com.accenture.franquicias_api.application.usecase.product.UpdateProductStockUseCase
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductStockUpdateRequest", description = "Solicitud para actualizar stock del producto")
public class ProductStockUpdateRequest {

    @NotNull(message = "El stock es requerido")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @JsonProperty("stock")
    @Schema(description = "Nueva cantidad de stock", example = "150")
    private Integer stock;
}
