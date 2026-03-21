package com.franquicias.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el stock de un producto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockUpdateRequest {

    @NotNull(message = "El stock es requerido")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @JsonProperty("stock")
    private Integer stock;
}
