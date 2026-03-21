package com.accenture.franquicias_api.infrastructure.persistence.entity.branch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * Entidad de persistencia para Sucursal en base de datos con R2DBC.
 *
 * <p>
 * Mapeo directo con tabla 'branches' en MySQL. Utilizada únicamente en la capa
 * de infraestructura, never expuesta a capas superiores (application).
 * </p>
 *
 * <p>
 * Mapeo a dominio:
 * <ul>
 *   <li>BranchEntity ←→ Branch (entidad de dominio)</li>
 *   <li>Conversión bidireccional: {@link BranchEntityMapper}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Relaciones:
 * <ul>
 *   <li>franchiseId: Foreign Key a tabla 'franchises'</li>
 *   <li>Una franquicia puede tener múltiples sucursales</li>
 *   <li>Una sucursal pertenece a exactamente una franquicia</li>
 * </ul>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("branches")
@Schema(name = "BranchEntity", description = "Entidad R2DBC para persistencia de sucursales")
public class BranchEntity {
    
    @Id
    @Schema(description = "ID único (primary key en tabla branches)", example = "1")
    private Long id;

    @Column("franchise_id")
    @Schema(description = "ID de la franquicia padre (foreign key)", example = "1")
    private Long franchiseId;

    @Column("name")
    @Schema(description = "Nombre de la sucursal", example = "Sucursal Medellín")
    private String name;

    @Column("address")
    @Schema(description = "Dirección completa de la sucursal", example = "Calle 40 #50-60")
    private String address;

    @Column("city")
    @Schema(description = "Ciudad donde está ubicada la sucursal", example = "Medellín")
    private String city;

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
