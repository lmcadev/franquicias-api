package com.accenture.franquicias_api.infrastructure.persistence.r2dbc.product;

import com.accenture.franquicias_api.infrastructure.persistence.entity.product.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz de repositorio R2DBC para acceso reactivo a la base de datos en la tabla 'products'.
 * Extiende Spring Data R2DBC para proporcionar operaciones CRUD y consultas reactivas personalizadas.
 *
 * <p>
 * Este repositorio maneja operaciones de base de datos de bajo nivel usando consultas SQL personalizadas
 * con filtrado de eliminación suave (condiciones deleted_at IS NULL).
 * </p>
 *
 * <p>
 * La implementación es auto-generada por Spring Data R2DBC en tiempo de ejecución.
 * </p>
 *
 * @see ProductEntity
 * @see ProductRepositoryImpl
 */
@Repository
public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, Long> {

    /**
     * Busca todos los productos en una sucursal específica, excluyendo registros eliminados suavemente.
     *
     * @param branchId el ID de la sucursal padre
     * @return {@code Flux} emitiendo todos los productos activos en esa sucursal
     */
    @Query("SELECT * FROM products WHERE branch_id = :branchId AND deleted_at IS NULL")
    Flux<ProductEntity> findByBranchIdAndNotDeleted(Long branchId);

    /**
     * Busca un producto por ID, excluyendo registros eliminados suavemente.
     *
     * @param id el ID del producto
     * @return {@code Mono} conteniendo la {@link ProductEntity} si se encuentra
     */
    @Query("SELECT * FROM products WHERE id = :id AND deleted_at IS NULL")
    Mono<ProductEntity> findByIdAndNotDeleted(Long id);

    /**
     * Recupera una página de productos, excluyendo registros eliminados suavemente.
     *
     * @param limit el tamaño de la página
     * @param offset el número de registros a saltar
     * @return {@code Flux} emitiendo productos en la página especificada
     */
    @Query("SELECT * FROM products WHERE deleted_at IS NULL LIMIT :limit OFFSET :offset")
    Flux<ProductEntity> findAllNotDeleted(int limit, int offset);

    /**
     * Cuenta el número total de productos activos (no eliminados).
     *
     * @return {@code Mono} conteniendo el conteo de productos activos
     */
    @Query("SELECT COUNT(*) FROM products WHERE deleted_at IS NULL")
    Mono<Long> countNotDeleted();

    /**
     * Cuenta el número de productos activos (no eliminados) en una sucursal específica.
     *
     * @param branchId el ID de la sucursal
     * @return {@code Mono} conteniendo el conteo de productos en esa sucursal
     */
    @Query("SELECT COUNT(*) FROM products WHERE branch_id = :branchId AND deleted_at IS NULL")
    Mono<Long> countByBranchIdAndNotDeleted(Long branchId);

    @Query("""
        SELECT * FROM products
        WHERE branch_id = :branchId
          AND deleted_at IS NULL
        ORDER BY stock DESC
        LIMIT 1
        """)
    Mono<ProductEntity> findMaxStockProductByBranchId(Long branchId);

    @Query("""
        SELECT p.* FROM products p
        INNER JOIN branches b ON p.branch_id = b.id
        WHERE b.franchise_id = :franchiseId
          AND p.deleted_at IS NULL
          AND b.deleted_at IS NULL
        ORDER BY p.stock DESC
        LIMIT 1
        """)
    Mono<ProductEntity> findMaxStockProductByFranchiseId(Long franchiseId);
}
