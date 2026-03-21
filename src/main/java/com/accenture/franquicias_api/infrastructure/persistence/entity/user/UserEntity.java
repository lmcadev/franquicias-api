package com.accenture.franquicias_api.infrastructure.persistence.entity.user;

import com.accenture.franquicias_api.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * Entidad de persistencia para Usuario en base de datos con R2DBC.
 *
 * <p>
 * Mapeo directo con tabla 'users' en MySQL. Utilizada únicamente en la capa
 * de infraestructura, never expuesta a capas superiores (application).
 * </p>
 *
 * <p>
 * Mapeo a dominio:
 * <ul>
 *   <li>UserEntity ←→ User (entidad de dominio)</li>
 *   <li>Conversión bidireccional: {@link UserEntityMapper}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Campos de auditoría:
 * <ul>
 *   <li>created_at: Seteado automáticamente por BD en INSERT</li>
 *   <li>updated_at: Actualizado automáticamente por BD en UPDATE</li>
 * </ul>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
@Schema(name = "UserEntity", description = "Entidad R2DBC para persistencia de usuarios")
public class UserEntity {
    
    @Id
    @Schema(description = "ID único (primary key en tabla users)", example = "1")
    private Long id;

    @Column("email")
    @Schema(description = "Email único del usuario", example = "usuario@example.com")
    private String email;

    @Column("password")
    @Schema(description = "Contraseña hasheada con BCrypt", example = "$2a$10$...")
    private String password;

    @Column("name")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;

    @Column("role")
    @Schema(description = "Rol de autorición (ADMIN, USER, READ_ONLY)", example = "USER")
    private UserRole role;

    @Column("active")
    @Schema(description = "Estado activo/inactivo del usuario", example = "true")
    private Boolean active;

    @Column("created_at")
    @Schema(description = "Timestamp de creación (seteado por BD)", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Column("updated_at")
    @Schema(description = "Timestamp de última actualización", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;

    @Column("deleted_at")
    @Schema(description = "Timestamp de soft delete (NULL si activo)", example = "null")
    private LocalDateTime deletedAt;
}
