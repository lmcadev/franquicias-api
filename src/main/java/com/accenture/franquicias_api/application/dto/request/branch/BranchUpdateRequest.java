package com.accenture.franquicias_api.application.dto.request.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar una sucursal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchUpdateRequest {

    @NotBlank(message = "El nombre de la sucursal es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;
}
