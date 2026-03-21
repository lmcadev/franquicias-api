package com.accenture.franquicias_api.domain.repository.user;

import com.accenture.franquicias_api.domain.entity.user.User;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz de repositorio para gestionar operaciones de persistencia de Usuario en contexto reactivo.
 * Proporciona operaciones CRUD y de consulta para entidades {@link User} usando Spring WebFlux y R2DBC.
 *
 * <p>
 * Este repositorio maneja:
 * <ul>
 *   <li>Gestión de autenticación y perfil de usuario</li>
 *   <li>Búsquedas de usuario por email para verificación de login</li>
 *   <li>Recuperación paginada de todos los usuarios activos (no eliminados)</li>
 *   <li>Implementación de eliminación suave (timestamp deleted_at)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Todas las operaciones son no-bloqueantes y retornan {@code Mono<T>} (valor único) o
 * {@code Flux<T>} (flujo) para integración con controladores reactivos Spring WebFlux.
 * </p>
 *
 * @see User
 */
public interface UserRepository {
    /**
     * Busca un usuario por su ID único.
     *
     * @param id el ID del usuario
     * @return {@code Mono} conteniendo el {@link User} si se encuentra, vacío en caso contrario
     */
    Mono<User> findById(Long id);

    /**
     * Busca un usuario por su dirección de email.
     * Se utiliza para autenticación en login y verificación de unicidad de email.
     *
     * @param email la dirección de email del usuario
     * @return {@code Mono} conteniendo el {@link User} si se encuentra, vacío en caso contrario
     */
    Mono<User> findByEmail(String email);

    /**
     * Recupera todos los usuarios activos (no eliminados) con soporte para paginación.
     *
     * @param pageable parámetros de paginación (número de página, tamaño de página, ordenamiento)
     * @return {@code Flux} emitiendo todos los usuarios activos en la página solicitada
     */
    Flux<User> findAll(Pageable pageable);

    /**
     * Guarda un usuario nuevo o actualiza uno existente.
     * Establece automáticamente {@code createdAt} para usuarios nuevos y {@code updatedAt} para todos.
     *
     * @param user la entidad {@link User} a guardar
     * @return {@code Mono} conteniendo el usuario guardado con ID generado
     */
    Mono<User> save(User user);

    /**
     * Elimina suavemente un usuario por ID (establece timestamp {@code deleted_at}).
     * No elimina el registro de la base de datos.
     *
     * @param id el ID del usuario a eliminar
     * @return {@code Mono<Void>} completando cuando la eliminación termina
     */
    Mono<Void> delete(Long id);

    /**
     * Actualiza una entidad usuario existente.
     * Establece {@code updatedAt} a la marca de tiempo actual.
     *
     * @param user la entidad {@link User} con valores actualizados
     * @return {@code Mono} conteniendo el {@link User} actualizado
     */
    Mono<User> update(User user);
}
