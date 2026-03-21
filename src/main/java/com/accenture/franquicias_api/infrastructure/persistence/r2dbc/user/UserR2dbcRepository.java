package com.accenture.franquicias_api.infrastructure.persistence.r2dbc.user;

import com.accenture.franquicias_api.infrastructure.persistence.entity.user.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Interfaz de repositorio R2DBC para acceso reactivo a la base de datos en la tabla 'users'.
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
 * @see UserEntity
 * @see UserRepositoryImpl
 */
@Repository
public interface UserR2dbcRepository extends R2dbcRepository<UserEntity, Long> {

    /**
     * Busca un usuario por ID, excluyendo registros eliminados suavemente.
     *
     * @param id el ID del usuario
     * @return {@code Mono} conteniendo la {@link UserEntity} si se encuentra
     */
    @Query("SELECT * FROM users WHERE id = :id AND deleted_at IS NULL")
    Mono<UserEntity> findByIdAndNotDeleted(Long id);

    /**
     * Busca un usuario por email, excluyendo registros eliminados suavemente.
     * Se utiliza para autenticación en login.
     *
     * @param email la dirección de email del usuario
     * @return {@code Mono} conteniendo la {@link UserEntity} si se encuentra
     */
    @Query("SELECT * FROM users WHERE email = :email AND deleted_at IS NULL")
    Mono<UserEntity> findByEmailAndNotDeleted(String email);

    /**
     * Cuenta el número total de usuarios activos (no eliminados).
     *
     * @return {@code Mono} conteniendo el conteo de usuarios activos
     */
    @Query("SELECT COUNT(*) FROM users WHERE deleted_at IS NULL")
    Mono<Long> countNotDeleted();
}
