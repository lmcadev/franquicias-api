package com.accenture.franquicias_api.infrastructure.persistence.r2dbc.branch;

import com.accenture.franquicias_api.infrastructure.persistence.entity.branch.BranchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz de repositorio R2DBC para acceso reactivo a la base de datos en la tabla 'branches'.
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
 * @see BranchEntity
 * @see BranchRepositoryImpl
 */
@Repository
public interface BranchR2dbcRepository extends R2dbcRepository<BranchEntity, Long> {

    /**
     * Busca todas las sucursales pertenecientes a una franquicia específica, excluyendo registros eliminados suavemente.
     *
     * @param franchiseId el ID de la franquicia padre
     * @return {@code Flux} emitiendo todas las sucursales activas en esa franquicia
     */
    @Query("SELECT * FROM branches WHERE franchise_id = :franchiseId AND deleted_at IS NULL")
    Flux<BranchEntity> findByFranchiseIdAndNotDeleted(Long franchiseId);

    /**
     * Busca una sucursal por ID, excluyendo registros eliminados suavemente.
     *
     * @param id el ID de la sucursal
     * @return {@code Mono} conteniendo la {@link BranchEntity} si se encuentra
     */
    @Query("SELECT * FROM branches WHERE id = :id AND deleted_at IS NULL")
    Mono<BranchEntity> findByIdAndNotDeleted(Long id);

    /**
     * Recupera una página de sucursales, excluyendo registros eliminados suavemente.
     *
     * @param limit el tamaño de la página
     * @param offset el número de registros a saltar
     * @return {@code Flux} emitiendo sucursales en la página especificada
     */
    @Query("SELECT * FROM branches WHERE deleted_at IS NULL LIMIT :limit OFFSET :offset")
    Flux<BranchEntity> findAllNotDeleted(int limit, int offset);

    /**
     * Cuenta el número total de sucursales activas (no eliminadas).
     *
     * @return {@code Mono} conteniendo el conteo de sucursales activas
     */
    @Query("SELECT COUNT(*) FROM branches WHERE deleted_at IS NULL")
    Mono<Long> countNotDeleted();

    /**
     * Cuenta el número de sucursales activas (no eliminadas) en una franquicia específica.
     *
     * @param franchiseId el ID de la franquicia
     * @return {@code Mono} conteniendo el conteo de sucursales en esa franquicia
     */
    @Query("SELECT COUNT(*) FROM branches WHERE franchise_id = :franchiseId AND deleted_at IS NULL")
    Mono<Long> countByFranchiseIdAndNotDeleted(Long franchiseId);
}
