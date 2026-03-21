package com.accenture.franquicias_api.domain.repository.product;

import com.accenture.franquicias_api.domain.entity.product.Product;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz de repositorio para gestionar operaciones de persistencia de Producto en contexto reactivo.
 * Proporciona operaciones CRUD y de consulta avanzada para entidades {@link Product} usando Spring WebFlux y R2DBC.
 *
 * <p>
 * Este repositorio maneja:
 * <ul>
 *   <li>Creación y gestión de inventario de productos</li>
 *   <li>Recuperación de productos dentro de una sucursal específica</li>
 *   <li>Consultas basadas en stock (encontrar productos con máximo stock por sucursal o franquicia)</li>
 *   <li>Paginación de productos dentro de una sucursal</li>
 *   <li>Implementación de eliminación suave (timestamp deleted_at)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Todas las operaciones son no-bloqueantes y retornan {@code Mono<T>} (valor único) o
 * {@code Flux<T>} (flujo) para integración con controladores reactivos Spring WebFlux.
 * </p>
 *
 * @see Product
 */
public interface ProductRepository {
    /**
     * Busca un producto por su ID único.
     *
     * @param id el ID del producto
     * @return {@code Mono} conteniendo el {@link Product} si se encuentra, vacío en caso contrario
     */
    Mono<Product> findById(Long id);

    /**
     * Recupera todos los productos activos (no eliminados) en una sucursal específica.
     * Los resultados están paginados.
     *
     * @param branchId el ID de la sucursal padre
     * @param pageable parámetros de paginación (número de página, tamaño de página, ordenamiento)
     * @return {@code Flux} emitiendo todos los productos activos en esa sucursal
     */
    Flux<Product> findByBranchId(Long branchId, Pageable pageable);

    /**
     * Guarda un producto nuevo o actualiza uno existente.
     * Establece automáticamente {@code createdAt} para productos nuevos y {@code updatedAt} para todos.
     *
     * @param product la entidad {@link Product} a guardar
     * @return {@code Mono} conteniendo el producto guardado con ID generado
     */
    Mono<Product> save(Product product);

    /**
     * Elimina suavemente un producto por ID (establece timestamp {@code deleted_at}).
     * No elimina el registro de la base de datos.
     *
     * @param id el ID del producto a eliminar
     * @return {@code Mono<Void>} completando cuando la eliminación termina
     */
    Mono<Void> delete(Long id);

    /**
     * Actualiza una entidad producto existente.
     * Establece {@code updatedAt} a la marca de tiempo actual.
     *
     * @param product la entidad {@link Product} con valores actualizados
     * @return {@code Mono} conteniendo el {@link Product} actualizado
     */
    Mono<Product> update(Product product);

    /**
     * Busca el producto con el nivel de stock más alto en una sucursal específica.
     * Útil para análisis y optimización de inventario.
     *
     * @param branchId el ID de la sucursal a buscar
     * @return {@code Mono} conteniendo el producto con máximo stock en la sucursal, vacío si no existen productos
     */
    Mono<Product> findMaxStockByBranch(Long branchId);

    /**
     * Busca el producto con el nivel de stock más alto en todas las sucursales de una franquicia.
     * Útil para análisis de inventario a nivel de franquicia.
     *
     * @param franchiseId el ID de la franquicia a buscar
     * @return {@code Mono} conteniendo el producto con máximo stock en la franquicia, vacío si no existen productos
     */
    Mono<Product> findMaxStockByFranchise(Long franchiseId);
}
