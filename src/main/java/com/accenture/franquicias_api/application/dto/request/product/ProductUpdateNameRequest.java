package com.accenture.franquicias_api.application.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProductUpdateNameRequest {

    @NotBlank(message = "El nombre del producto es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    private String name;
}
