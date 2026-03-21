package com.franquicias.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el login de usuarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequest {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @JsonProperty("password")
    private String password;
}
