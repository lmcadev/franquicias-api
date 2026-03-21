package com.accenture.franquicias_api.application.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo producto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductCreateRequest", description = "Solicitud para crear nuevo producto")
public class ProductCreateRequest {

    @NotBlank(message = "El nombre del producto es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    @Schema(description = "Nombre del producto", example = "Desarrollo web")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Descripción del producto", example = "Servicio de desarrollo web")
    private String description;

    @NotNull(message = "El stock es requerido")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @JsonProperty("stock")
    @Schema(description = "Cantidad de stock inicial", example = "100")
    private Integer stock;

    @JsonProperty("price")
    @Schema(description = "Precio unitario del producto", example = "500.000")
    private Double price;
}
