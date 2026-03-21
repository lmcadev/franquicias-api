package com.franquicias.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el registro de nuevos usuarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRequest {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    private String name;
}
