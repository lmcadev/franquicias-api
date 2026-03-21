package com.accenture.franquicias_api.infrastructure.persistence.impl.product;

import com.accenture.franquicias_api.domain.entity.product.Product;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.product.ProductEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.product.ProductEntityMapper;
import com.accenture.franquicias_api.infrastructure.persistence.r2dbc.product.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación reactiva del repositorio de Producto.
 * Proporciona operaciones CRUD y consultas avanzadas para productos.
 *
 * <p>
 * Esta clase implementa la interfaz {@link ProductRepository} utilizando R2DBC para
 * acceso reactivo a la base de datos. Maneja la conversión bidireccional entre
 * entidades de dominio ({@link Product}) y entidades de persistencia ({@link ProductEntity}).
 * </p>
 *
 * <p>
 * Características principales:
 * <ul>
 *   <li>Operaciones CRUD no-bloqueantes (Mono/Flux)</li>
 *   <li>Eliminación suave (soft delete) con timestamp deleted_at</li>
 *   <li>Búsqueda de productos por sucursal</li>
 *   <li>Búsqueda de productos con máximo stock por sucursal o franquicia</li>
 *   <li>Paginación de resultados</li>
 *   <li>Auditoría automática de fechas (createdAt, updatedAt)</li>
 * </ul>
 * </p>
 *
 * @see ProductRepository
 * @see Product
 * @see ProductEntity
 * @see ProductEntityMapper
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductR2dbcRepository r2dbcRepository;
    private final ProductEntityMapper mapper;

    /**
     * Busca un producto por su ID.
     * Filtra registros eliminados suavemente.
     *
     * @param id el ID del producto
     * @return {@code Mono} conteniendo el producto si existe, vacío en caso contrario
     */
    @Override
    public Mono<Product> findById(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .map(mapper::toDomain);
    }

    /**
     * Recupera todos los productos activos de una sucursal específica con paginación.
     *
     * @param branchId el ID de la sucursal padre
     * @param pageable parámetros de paginación
     * @return {@code Flux} emitiendo productos de la página solicitada
     */
    @Override
    public Flux<Product> findByBranchId(Long branchId, Pageable pageable) {
        return r2dbcRepository.findByBranchIdAndNotDeleted(branchId)
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .take(pageable.getPageSize())
            .map(mapper::toDomain);
    }

    /**
     * Guarda un producto nuevo o actualiza uno existente.
     * Establece automáticamente createdAt para nuevos y updatedAt para todos.
     *
     * @param product el producto a guardar
     * @return {@code Mono} conteniendo el producto guardado con ID asignado
     */
    @Override
    public Mono<Product> save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

    /**
     * Elimina suavemente un producto por ID.
     * Establece el timestamp deleted_at sin eliminar el registro.
     *
     * @param id el ID del producto a eliminar
     * @return {@code Mono<Void>} completando cuando se termina la eliminación
     */
    @Override
    public Mono<Void> delete(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .flatMap(entity -> {
                entity.setDeletedAt(LocalDateTime.now());
                entity.setUpdatedAt(LocalDateTime.now());
                return r2dbcRepository.save(entity);
            })
            .then();
    }

    /**
     * Actualiza un producto existente.
     * Establece el timestamp updatedAt a la hora actual.
     *
     * @param product el producto con valores actualizados
     * @return {@code Mono} conteniendo el producto actualizado
     */
    @Override
    public Mono<Product> update(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

    /**
     * Busca el producto con máximo stock en una sucursal específica.
     * Útil para análisis de inventario a nivel de sucursal.
     *
     * @param branchId el ID de la sucursal
     * @return {@code Mono} conteniendo el producto con máximo stock, vacío si no existen
     */
    @Override
    public Mono<Product> findMaxStockByBranch(Long branchId) {
        return r2dbcRepository.findMaxStockProductByBranchId(branchId)
            .map(mapper::toDomain);
    }

    /**
     * Busca el producto con máximo stock en toda una franquicia.
     * Útil para análisis de inventario a nivel de franquicia.
     *
     * @param franchiseId el ID de la franquicia
     * @return {@code Mono} conteniendo el producto con máximo stock, vacío si no existen
     */
    @Override
    public Mono<Product> findMaxStockByFranchise(Long franchiseId) {
        return r2dbcRepository.findMaxStockProductByFranchiseId(franchiseId)
            .map(mapper::toDomain);
    }
}
