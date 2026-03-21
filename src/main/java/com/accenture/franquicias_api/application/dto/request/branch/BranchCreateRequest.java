package com.accenture.franquicias_api.application.dto.request.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de creación de nuevas sucursales.
 *
 * <p>
 * Utilizado en el endpoint POST /api/franchises/{franchiseId}/branches para crear
 * una nueva sucursal dentro de una franquicia existente.
 * </p>
 *
 * <p>
 * Validaciones:
 * <ul>
 *   <li>Nombre: requerido, máximo 100 caracteres</li>
 *   <li>Dirección: opcional</li>
 *   <li>Ciudad: opcional</li>
 *   <li>Franquicia padre: debe existir y estar activa</li>
 * </ul>
 * </p>
 *
 * <p>
 * Respuesta esperada: {@link BranchResponse}
 * </p>
 *
 * @see BranchResponse
 * @see com.accenture.franquicias_api.application.usecase.branch.CreateBranchUseCase
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BranchCreateRequest", description = "Solicitud para crear nueva sucursal")
public class BranchCreateRequest {

    @NotBlank(message = "El nombre de la sucursal es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
    private String name;

    @JsonProperty("address")
    @Schema(description = "Dirección de la sucursal", example = "Calle 3 # 20 30 centro")
    private String address;

    @JsonProperty("city")
    @Schema(description = "Ciudad donde está ubicada la sucursal", example = "Armenia")
    private String city;
}
