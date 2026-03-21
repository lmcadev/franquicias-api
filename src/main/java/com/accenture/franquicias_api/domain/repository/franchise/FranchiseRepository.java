package com.accenture.franquicias_api.domain.repository.franchise;

import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz de repositorio para gestionar operaciones de persistencia de Franquicia en contexto reactivo.
 * Proporciona operaciones CRUD y de consulta para entidades {@link Franchise} usando Spring WebFlux y R2DBC.
 *
 * <p>
 * Este repositorio maneja:
 * <ul>
 *   <li>Creación y gestión de marcas de franquicia</li>
 *   <li>Recuperación de franquicias por propietario (usuario creador)</li>
 *   <li>Listado paginado de todas las franquicias activas</li>
 *   <li>Implementación de eliminación suave (timestamp deleted_at)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Todas las operaciones son no-bloqueantes y retornan {@code Mono<T>} (valor único) o
 * {@code Flux<T>} (flujo) para integración con controladores reactivos Spring WebFlux.
 * </p>
 *
 * @see Franchise
 */
public interface FranchiseRepository {
    /**
     * Busca una franquicia por su ID único.
     *
     * @param id el ID de la franquicia
     * @return {@code Mono} conteniendo la {@link Franchise} si se encuentra, vacío en caso contrario
     */
    Mono<Franchise> findById(Long id);

    /**
     * Recupera todas las franquicias activas (no eliminadas) con paginación.
     *
     * @param pageable parámetros de paginación (número de página, tamaño de página, ordenamiento)
     * @return {@code Flux} emitiendo todas las franquicias activas en la página solicitada
     */
    Flux<Franchise> findAll(Pageable pageable);

    /**
     * Guarda una franquicia nueva o actualiza una existente.
     * Establece automáticamente {@code createdAt} para franquicias nuevas y {@code updatedAt} para todas.
     *
     * @param franchise la entidad {@link Franchise} a guardar
     * @return {@code Mono} conteniendo la franquicia guardada con ID generado
     */
    Mono<Franchise> save(Franchise franchise);

    /**
     * Elimina suavemente una franquicia por ID (establece timestamp {@code deleted_at}).
     * No elimina el registro de la base de datos.
     *
     * @param id el ID de la franquicia a eliminar
     * @return {@code Mono<Void>} completando cuando la eliminación termina
     */
    Mono<Void> delete(Long id);

    /**
     * Actualiza una entidad franquicia existente.
     * Establece {@code updatedAt} a la marca de tiempo actual.
     *
     * @param franchise la entidad {@link Franchise} con valores actualizados
     * @return {@code Mono} conteniendo la {@link Franchise} actualizada
     */
    Mono<Franchise> update(Franchise franchise);

    /**
     * Busca todas las franquicias creadas por un usuario específico (propietario).
     * Útil para recuperar franquicias gestionadas por un usuario en particular.
     *
     * @param createdBy el ID del usuario que creó/posee las franquicias
     * @param pageable parámetros de paginación
     * @return {@code Flux} emitiendo todas las franquicias propiedad del usuario especificado
     */
    Flux<Franchise> findByCreatedBy(Long createdBy, Pageable pageable);

    /**
     * Busca una franquicia por su nombre exacto.
     * Retorna la primera franquicia activa (no eliminada) que coincida.
     *
     * @param name el nombre de la franquicia a buscar
     * @return {@code Mono} conteniendo la franquicia si se encuentra, vacío en caso contrario
     */
    Mono<Franchise> findByName(String name);
}
