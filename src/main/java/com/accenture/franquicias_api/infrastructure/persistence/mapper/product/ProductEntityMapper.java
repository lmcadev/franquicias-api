package com.accenture.franquicias_api.infrastructure.persistence.mapper.product;

import com.accenture.franquicias_api.domain.entity.product.Product;
import com.accenture.franquicias_api.infrastructure.persistence.entity.product.ProductEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre entidad de persistencia R2DBC y entidad de dominio de Producto.
 *
 * <p>
 * Utiliza MapStruct para generar implementaciones en tiempo de compilación.
 * Realiza conversión bidireccional entre:
 * <ul>
 *   <li>Entidad de persistencia ({@link ProductEntity}) ↔ Entidad de dominio ({@link Product})</li>
 * </ul>
 * </p>
 *
 * <p>
 * Esta capa de mapeo desacopla completamente la capa de persistencia (R2DBC)
 * de la lógica de negocio (dominio), permitiendo cambios en la estructura
 * de la base de datos sin afectar el dominio.
 * </p>
 *
 * @see Product
 * @see ProductEntity
 */
@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    /**
     * Convierte una entidad de persistencia a entidad de dominio.
     *
     * @param entity la entidad R2DBC de persistencia
     * @return la entidad de dominio con los mismos datos
     */
    Product toDomain(ProductEntity entity);

    /**
     * Convierte una entidad de dominio a entidad de persistencia.
     *
     * @param domain la entidad de dominio
     * @return la entidad R2DBC de persistencia con los mismos datos
     */
    ProductEntity toEntity(Product domain);
}
