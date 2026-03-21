package com.accenture.franquicias_api.application.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de registro de nuevos usuarios.
 *
 * <p>
 * Utilizado en el endpoint POST /api/auth/register para crear nuevos usuarios
 * en el sistema con rol USER y estado activo.
 * </p>
 *
 * <p>
 * Validaciones:
 * <ul>
 *   <li>Email: requerido, debe ser válido y único</li>
 *   <li>Contraseña: requerida, mínimo 8 caracteres</li>
 *   <li>Nombre: requerido, máximo 100 caracteres</li>
 * </ul>
 * </p>
 *
 * <p>
 * Respuesta esperada: {@link AuthTokenResponse} con JWT token para el nuevo usuario
 * </p>
 *
 * @see AuthTokenResponse
 * @see com.accenture.franquicias_api.application.usecase.auth.RegisterUseCase
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AuthRegisterRequest", description = "Solicitud de registro de nuevo usuario")
public class AuthRegisterRequest {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @JsonProperty("email")
    @Schema(description = "Email del usuario (único)", example = "usuario@example.com")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    @JsonProperty("password")
    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "ejmplo123")
    private String password;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("name")
    @Schema(description = "Nombre completo del usuario", example = "Luis Miguel")
    private String name;
}
