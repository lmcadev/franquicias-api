package com.accenture.franquicias_api.domain.entity.product;

import com.accenture.franquicias_api.domain.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de Producto de la capa de dominio.
 * 
 * Representa un artículo disponible en una sucursal con precio e inventario.
 * Los productos están asociados a una sucursal específica.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Product", description = "Entidad de producto - artículo de inventario de una sucursal")
public class Product extends BaseEntity {

    @Schema(description = "ID de la sucursal a la que pertenece este producto", example = "1")
    private Long branchId;

    @Schema(description = "Nombre del producto", example = "Desarrollo web")
    private String name;

    @Schema(description = "Descripción detallada del producto", example = "Servicio de desarrollo de software")
    private String description;

    @Schema(description = "Cantidad en stock disponible (>= 0)", example = "100")
    private Integer stock;

    @Schema(description = "Precio unitario del producto en moneda local", example = "500.000")
    private BigDecimal price;
}
