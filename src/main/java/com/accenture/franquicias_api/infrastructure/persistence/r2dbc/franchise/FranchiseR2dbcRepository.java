package com.accenture.franquicias_api.infrastructure.persistence.r2dbc.franchise;

import com.accenture.franquicias_api.infrastructure.persistence.entity.franchise.FranchiseEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz de repositorio R2DBC para acceso reactivo a la base de datos en la tabla 'franchises'.
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
 * @see FranchiseEntity
 * @see FranchiseRepositoryImpl
 */
@Repository
public interface FranchiseR2dbcRepository extends R2dbcRepository<FranchiseEntity, Long> {

    /**
     * Busca todas las franquicias creadas por un usuario específico, excluyendo registros eliminados suavemente.
     *
     * @param userId el ID del usuario que creó las franquicias
     * @return {@code Flux} emitiendo todas las franquicias propiedad del usuario
     */
    @Query("SELECT * FROM franchises WHERE created_by = :userId AND deleted_at IS NULL")
    Flux<FranchiseEntity> findByCreatedByAndNotDeleted(Long userId);

    /**
     * Busca una franquicia por ID, excluyendo registros eliminados suavemente.
     *
     * @param id el ID de la franquicia
     * @return {@code Mono} conteniendo la {@link FranchiseEntity} si se encuentra
     */
    @Query("SELECT * FROM franchises WHERE id = :id AND deleted_at IS NULL")
    Mono<FranchiseEntity> findByIdAndNotDeleted(Long id);

    /**
     * Recupera una página de franquicias, excluyendo registros eliminados suavemente.
     * Útil para consultas paginadas.
     *
     * @param limit el tamaño de la página
     * @param offset el número de registros a saltar
     * @return {@code Flux} emitiendo franquicias en la página especificada
     */
    @Query("SELECT * FROM franchises WHERE deleted_at IS NULL LIMIT :limit OFFSET :offset")
    Flux<FranchiseEntity> findAllNotDeleted(int limit, int offset);

    /**
     * Cuenta el número total de franquicias activas (no eliminadas).
     *
     * @return {@code Mono} conteniendo el conteo de franquicias activas
     */
    @Query("SELECT COUNT(*) FROM franchises WHERE deleted_at IS NULL")
    Mono<Long> countNotDeleted();
}
