package com.accenture.franquicias_api.domain.entity.user;

import com.accenture.franquicias_api.domain.entity.BaseEntity;
import com.accenture.franquicias_api.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad de Usuario de la capa de dominio.
 * 
 * Representa un usuario del sistema que puede crear y gestionar franquicias.
 * 
 * Roles:
 * - ADMIN: Acceso completo a todas las operaciones
 * - USER: Crear y gestionar propias franquicias
 * - READ_ONLY: Solo lectura de información
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "User", description = "Entidad de usuario del sistema con autenticación y roles")
public class User extends BaseEntity {

    @Schema(description = "Email único del usuario (identificador)", example = "usuario@example.com")
    private String email;

    @Schema(description = "Contraseña hasheada con BCrypt", example = "$2a$10$...")
    private String password;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;

    @Schema(description = "Rol del usuario en el sistema (ADMIN, USER, READ_ONLY)", example = "USER")
    private UserRole role;

    @Schema(description = "Estado de activación del usuario (true = activo, false = desactivado)", example = "true")
    private Boolean active;
}
