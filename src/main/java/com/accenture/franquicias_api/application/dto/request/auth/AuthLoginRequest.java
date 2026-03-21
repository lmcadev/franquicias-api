package com.accenture.franquicias_api.application.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "AuthLoginRequest", description = "Solicitud de inicio de sesión")
public class AuthLoginRequest {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @JsonProperty("email")
    @Schema(description = "Email del usuario", example = "user@example.com")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @JsonProperty("password")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}
