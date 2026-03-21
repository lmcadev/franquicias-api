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
 * DTO para solicitudes de creación de nuevas franquicias.
 *
 * <p>
 * Utilizado en el endpoint POST /api/franchises para crear una nueva franquicia.
 * El usuario autenticado será registrado como propietario (createdBy) de la franquicia.
 * </p>
 *
 * <p>
 * Validaciones:
 * <ul>
 *   <li>Nombre: requerido, único, máximo 100 caracteres</li>
 *   <li>Descripción: opcional</li>
 * </ul>
 * </p>
 *
 * <p>
 * Respuesta esperada: {@link FranchiseResponse}
 * </p>
 *
 * @see FranchiseResponse
 * @see com.accenture.franquicias_api.application.usecase.franchise.CreateFranchiseUseCase
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FranchiseCreateRequest", description = "Solicitud para crear nueva franquicia")
public class FranchiseCreateRequest {

    @NotBlank(message = "El nombre de la franquicia es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    @Schema(description = "Nombre único de la franquicia", example = "Accenture Bogotá")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Descripción de la franquicia", example = "Servicios de consultoría")
    private String description;
}
