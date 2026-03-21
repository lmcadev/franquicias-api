package com.accenture.franquicias_api.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase base abstracta para todas las entidades del dominio.
 * 
 * Proporciona funcionalidad común de auditoría y soft delete:
 * - id: Identificador único generado por la BD
 * - createdAt: Marca de tiempo de creación (AUTO)
 * - updatedAt: Marca de tiempo de última actualización (AUTO)
 * - deletedAt: Marca de tiempo de eliminación lógica (NULL si no eliminado)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Clase base para todas las entidades del dominio (auditoría y soft delete)")
public abstract class BaseEntity {

    @Schema(description = "ID único de la entidad (generado por BD)", example = "1")
    protected Long id;

    @Schema(description = "Fecha y hora de creación (seteado automáticamente)", example = "2024-01-15T10:30:00")
    protected LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización (seteado automáticamente)", example = "2024-01-15T10:30:00")
    protected LocalDateTime updatedAt;

    @Schema(description = "Fecha y hora de eliminación lógica (NULL si no eliminado - soft delete)", example = "null")
    protected LocalDateTime deletedAt;

    /**
     * Verifica si la entidad está eliminada (soft delete)
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * Marca la entidad como eliminada
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
