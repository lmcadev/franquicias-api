package com.accenture.franquicias_api.domain.repository.branch;

import com.accenture.franquicias_api.domain.entity.branch.Branch;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz de repositorio para gestionar operaciones de persistencia de Sucursal en contexto reactivo.
 * Proporciona operaciones CRUD y de consulta para entidades {@link Branch} usando Spring WebFlux y R2DBC.
 *
 * <p>
 * Este repositorio maneja:
 * <ul>
 *   <li>Creación y gestión de ubicaciones de sucursales físicas</li>
 *   <li>Recuperación de sucursales asociadas a una franquicia específica</li>
 *   <li>Paginación de sucursales dentro de una franquicia</li>
 *   <li>Implementación de eliminación suave (timestamp deleted_at)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Todas las operaciones son no-bloqueantes y retornan {@code Mono<T>} (valor único) o
 * {@code Flux<T>} (flujo) para integración con controladores reactivos Spring WebFlux.
 * </p>
 *
 * @see Branch
 */
public interface BranchRepository {
    /**
     * Busca una sucursal por su ID único.
     *
     * @param id el ID de la sucursal
     * @return {@code Mono} conteniendo la {@link Branch} si se encuentra, vacío en caso contrario
     */
    Mono<Branch> findById(Long id);

    /**
     * Recupera todas las sucursales activas (no eliminadas) pertenecientes a una franquicia específica.
     * Los resultados están paginados.
     *
     * @param franchiseId el ID de la franquicia padre
     * @param pageable parámetros de paginación (número de página, tamaño de página, ordenamiento)
     * @return {@code Flux} emitiendo todas las sucursales activas en esa franquicia
     */
    Flux<Branch> findByFranchiseId(Long franchiseId, Pageable pageable);

    /**
     * Guarda una sucursal nueva o actualiza una existente.
     * Establece automáticamente {@code createdAt} para sucursales nuevas y {@code updatedAt} para todas.
     *
     * @param branch la entidad {@link Branch} a guardar
     * @return {@code Mono} conteniendo la sucursal guardada con ID generado
     */
    Mono<Branch> save(Branch branch);

    /**
     * Elimina suavemente una sucursal por ID (establece timestamp {@code deleted_at}).
     * No elimina el registro de la base de datos.
     *
     * @param id el ID de la sucursal a eliminar
     * @return {@code Mono<Void>} completando cuando la eliminación termina
     */
    Mono<Void> delete(Long id);

    /**
     * Actualiza una entidad sucursal existente.
     * Establece {@code updatedAt} a la marca de tiempo actual.
     *
     * @param branch la entidad {@link Branch} con valores actualizados
     * @return {@code Mono} conteniendo la {@link Branch} actualizada
     */
    Mono<Branch> update(Branch branch);
}
