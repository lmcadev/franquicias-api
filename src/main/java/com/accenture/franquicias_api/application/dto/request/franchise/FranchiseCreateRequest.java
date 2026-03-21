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
 * DTO para crear una nueva franquicia
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
