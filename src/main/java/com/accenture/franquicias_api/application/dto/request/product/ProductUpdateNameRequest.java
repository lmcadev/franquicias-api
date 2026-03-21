package com.accenture.franquicias_api.application.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el nombre de un producto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductUpdateNameRequest", description = "Solicitud para actualizar nombre del producto")
public class ProductUpdateNameRequest {

    @NotBlank(message = "El nombre del producto es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    @Schema(description = "Nuevo nombre del producto", example = "Desarrollo web Premium")
    private String name;
}
