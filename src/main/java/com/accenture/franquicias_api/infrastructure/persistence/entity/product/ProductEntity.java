package com.accenture.franquicias_api.infrastructure.persistence.entity.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de persistencia para Producto en base de datos con R2DBC.
 *
 * <p>
 * Mapeo directo con tabla 'products' en MySQL. Utilizada únicamente en la capa
 * de infraestructura, never expuesta a capas superiores (application).
 * </p>
 *
 * <p>
 * Mapeo a dominio:
 * <ul>
 *   <li>ProductEntity ←→ Product (entidad de dominio)</li>
 *   <li>Conversión bidireccional: {@link ProductEntityMapper}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Relaciones y restricciones:
 * <ul>
 *   <li>branchId: Foreign Key a tabla 'branches'</li>
 *   <li>stock: NO puede ser negativo (validado en application layer)</li>
 *   <li>price: Almacenado en BigDecimal para precisión monetaria</li>
 *   <li>deletedAt: NULL si activo, timestamp si fue eliminado (soft delete)</li>
 * </ul>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("products")
@Schema(name = "ProductEntity", description = "Entidad R2DBC para persistencia de productos")
public class ProductEntity {
    
    @Id
    @Schema(description = "ID único (primary key en tabla products)", example = "1")
    private Long id;

    @Column("branch_id")
    @Schema(description = "ID de la sucursal padre (foreign key)", example = "1")
    private Long branchId;

    @Column("name")
    @Schema(description = "Nombre del producto", example = "Desarrollo web")
    private String name;

    @Column("description")
    @Schema(description = "Descripción detallada del producto", example = "Servicio de desarrollo de software")
    private String description;

    @Column("stock")
    @Schema(description = "Cantidad disponible en inventario (>= 0)", example = "100")
    private Integer stock;

    @Column("price")
    @Schema(description = "Precio unitario en moneda local", example = "500.000")
    private BigDecimal price;

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
