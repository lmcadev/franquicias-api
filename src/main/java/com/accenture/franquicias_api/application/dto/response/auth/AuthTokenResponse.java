package com.accenture.franquicias_api.application.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las respuestas de autenticación (token)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AuthTokenResponse", description = "Respuesta de autenticación con JWT")
public class AuthTokenResponse {

    @JsonProperty("token")
    @Schema(description = "JWT token para autenticación en headers", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @JsonProperty("token_type")
    @Schema(description = "Tipo de token", example = "Bearer")
    private String tokenType;

    @JsonProperty("expires_in")
    @Schema(description = "Segundos hasta que expira el token", example = "3600")
    private Long expiresIn;

    @JsonProperty("user_id")
    @Schema(description = "ID del usuario autenticado", example = "1")
    private Long userId;

    @JsonProperty("email")
    @Schema(description = "Email del usuario autenticado", example = "user@example.com")
    private String email;

    @JsonProperty("name")
    @Schema(description = "Nombre del usuario autenticado", example = "Luis Miguel")
    private String name;

    @JsonProperty("role")
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private String role;
}
