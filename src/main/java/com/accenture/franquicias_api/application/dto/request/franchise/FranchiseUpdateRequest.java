package com.accenture.franquicias_api.application.dto.request.franchise;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar una franquicia
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseUpdateRequest {

    @NotBlank(message = "El nombre de la franquicia es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
