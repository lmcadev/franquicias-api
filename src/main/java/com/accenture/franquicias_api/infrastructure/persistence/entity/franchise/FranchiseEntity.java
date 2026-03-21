package com.accenture.franquicias_api.infrastructure.persistence.entity.franchise;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * Entidad de persistencia para Franquicia en base de datos con R2DBC.
 *
 * <p>
 * Mapeo directo con tabla 'franchises' en MySQL. Utilizada únicamente en la capa
 * de infraestructura, never expuesta a capas superiores (application).
 * </p>
 *
 * <p>
 * Mapeo a dominio:
 * <ul>
 *   <li>FranchiseEntity ←→ Franchise (entidad de dominio)</li>
 *   <li>Conversión bidireccional: {@link FranchiseEntityMapper}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Atributos importantes:
 * <ul>
 *   <li>createdBy: ID del usuario propietario (propietario de la franquicia)</li>
 *   <li>deletedAt: NULL si activa, timestamp si fue eliminada (soft delete)</li>
 * </ul>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("franchises")
@Schema(name = "FranchiseEntity", description = "Entidad R2DBC para persistencia de franquicias")
public class FranchiseEntity {
    
    @Id
    @Schema(description = "ID único (primary key en tabla franchises)", example = "1")
    private Long id;

    @Column("name")
    @Schema(description = "Nombre único de la franquicia", example = "Accenture Bogotá")
    private String name;

    @Column("description")
    @Schema(description = "Descripción detallada de la franquicia", example = "servicios de consultoría")
    private String description;

    @Column("created_by")
    @Schema(description = "ID del usuario que creó la franquicia", example = "1")
    private Long createdBy;

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
