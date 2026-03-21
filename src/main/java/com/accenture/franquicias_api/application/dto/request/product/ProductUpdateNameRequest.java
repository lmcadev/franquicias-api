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
 * DTO para solicitudes de actualización del nombre de un producto.
 *
 * <p>
 * Utilizado en el endpoint PUT /api/products/{id}/name para actualizar
 * únicamente el nombre/descripción de un producto existente.
 * </p>
 *
 * <p>
 * Validaciones:
 * <ul>
 *   <li>Nombre: requerido, máximo 100 caracteres</li>
 * </ul>
 * </p>
 *
 * <p>
 * Respuesta esperada: {@link ProductResponse}
 * </p>
 *
 * @see ProductResponse
 * @see com.accenture.franquicias_api.application.usecase.product.UpdateProductNameUseCase
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
