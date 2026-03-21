package com.accenture.franquicias_api.application.dto.request.franchise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de actualización de franquicias existentes.
 *
 * <p>
 * Utilizado en el endpoint PUT /api/franchises/{id} para actualizar los datos
 * de una franquicia existente.
 * </p>
 *
 * <p>
 * Validaciones:
 * <ul>
 *   <li>Nombre: requerido, máximo 100 caracteres</li>
 *   <li>Descripción: opcional</li>
 * </ul>
 * </p>
 *
 * <p>
 * Respuesta esperada: {@link FranchiseResponse}
 * </p>
 *
 * @see FranchiseResponse
 * @see com.accenture.franquicias_api.application.usecase.franchise.UpdateFranchiseUseCase
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FranchiseUpdateRequest", description = "Solicitud para actualizar una franquicia")
public class FranchiseUpdateRequest {

    @NotBlank(message = "El nombre de la franquicia es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    @Schema(description = "Nombre de la franquicia", example = "Accenture Medellín")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Descripción de la franquicia", example = "Servicios de consultoría")
    private String description;
}
