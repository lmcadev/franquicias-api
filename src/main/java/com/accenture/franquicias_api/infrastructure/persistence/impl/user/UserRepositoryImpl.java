package com.accenture.franquicias_api.infrastructure.persistence.impl.user;

import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.user.UserEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.user.UserEntityMapper;
import com.accenture.franquicias_api.infrastructure.persistence.r2dbc.user.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación reactiva del repositorio de Usuario.
 * Proporciona operaciones CRUD para usuarios traduciendo entre entidades de dominio y persistencia.
 *
 * <p>
 * Esta clase implementa la interfaz {@link UserRepository} utilizando R2DBC para
 * acceso reactivo a la base de datos. Maneja la conversión bidireccional entre
 * entidades de dominio ({@link User}) y entidades de persistencia ({@link UserEntity}).
 * </p>
 *
 * <p>
 * Características principales:
 * <ul>
 *   <li>Operaciones CRUD no-bloqueantes (Mono/Flux)</li>
 *   <li>Eliminación suave (soft delete) con timestamp deleted_at</li>
 *   <li>Búsqueda por email para autenticación</li>
 *   <li>Paginación de resultados</li>
 *   <li>Auditoría automática de fechas (createdAt, updatedAt)</li>
 * </ul>
 * </p>
 *
 * @see UserRepository
 * @see User
 * @see UserEntity
 * @see UserEntityMapper
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserR2dbcRepository r2dbcRepository;
    private final UserEntityMapper mapper;

    /**
     * Busca un usuario por su ID.
     * Filtra registros eliminados suavemente.
     *
     * @param id el ID del usuario
     * @return {@code Mono} conteniendo el usuario si existe, vacío en caso contrario
     */
    @Override
    public Mono<User> findById(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .map(mapper::toDomain);
    }

    /**
     * Busca un usuario por su dirección de email.
     * Utilizado para autenticación de login.
     *
     * @param email el email del usuario a buscar
     * @return {@code Mono} conteniendo el usuario si existe, vacío en caso contrario
     */
    @Override
    public Mono<User> findByEmail(String email) {
        return r2dbcRepository.findByEmailAndNotDeleted(email)
            .map(mapper::toDomain);
    }

    /**
     * Recupera todos los usuarios activos con paginación.
     * Filtra automáticamente registros eliminados suavemente.
     *
     * @param pageable parámetros de paginación
     * @return {@code Flux} emitiendo usuarios de la página solicitada
     */
    @Override
    public Flux<User> findAll(Pageable pageable) {
        return r2dbcRepository.findAll()
            .filter(entity -> entity.getDeletedAt() == null)
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .take(pageable.getPageSize())
            .map(mapper::toDomain);
    }

    /**
     * Guarda un usuario nuevo o actualiza uno existente.
     * Establece automáticamente createdAt para nuevos y updatedAt para todos.
     *
     * @param user el usuario a guardar
     * @return {@code Mono} conteniendo el usuario guardado con ID asignado
     */
    @Override
    public Mono<User> save(User user) {
        UserEntity entity = mapper.toEntity(user);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

    /**
     * Elimina suavemente un usuario por ID.
     * Establece el timestamp deleted_at sin eliminar el registro.
     *
     * @param id el ID del usuario a eliminar
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
     * Actualiza un usuario existente.
     * Establece el timestamp updatedAt a la hora actual.
     *
     * @param user el usuario con valores actualizados
     * @return {@code Mono} conteniendo el usuario actualizado
     */
    @Override
    public Mono<User> update(User user) {
        UserEntity entity = mapper.toEntity(user);
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }
}
