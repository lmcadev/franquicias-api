package com.accenture.franquicias_api.infrastructure.persistence.impl.branch;

import com.accenture.franquicias_api.domain.entity.branch.Branch;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.branch.BranchEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.branch.BranchEntityMapper;
import com.accenture.franquicias_api.infrastructure.persistence.r2dbc.branch.BranchR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación reactiva del repositorio de Sucursal.
 * Proporciona operaciones CRUD para sucursales traduciendo entre entidades de dominio y persistencia.
 *
 * <p>
 * Esta clase implementa la interfaz {@link BranchRepository} utilizando R2DBC para
 * acceso reactivo a la base de datos. Maneja la conversión bidireccional entre
 * entidades de dominio ({@link Branch}) y entidades de persistencia ({@link BranchEntity}).
 * </p>
 *
 * <p>
 * Características principales:
 * <ul>
 *   <li>Operaciones CRUD no-bloqueantes (Mono/Flux)</li>
 *   <li>Eliminación suave (soft delete) con timestamp deleted_at</li>
 *   <li>Búsqueda de sucursales por franquicia padre</li>
 *   <li>Paginación de resultados</li>
 *   <li>Auditoría automática de fechas (createdAt, updatedAt)</li>
 * </ul>
 * </p>
 *
 * @see BranchRepository
 * @see Branch
 * @see BranchEntity
 * @see BranchEntityMapper
 */
@Repository
@RequiredArgsConstructor
public class BranchRepositoryImpl implements BranchRepository {
    private final BranchR2dbcRepository r2dbcRepository;
    private final BranchEntityMapper mapper;

    /**
     * Busca una sucursal por su ID.
     * Filtra registros eliminados suavemente.
     *
     * @param id el ID de la sucursal
     * @return {@code Mono} conteniendo la sucursal si existe, vacío en caso contrario
     */
    @Override
    public Mono<Branch> findById(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .map(mapper::toDomain);
    }

    /**
     * Recupera todas las sucursales activas de una franquicia específica con paginación.
     *
     * @param franchiseId el ID de la franquicia padre
     * @param pageable parámetros de paginación
     * @return {@code Flux} emitiendo sucursales de la página solicitada
     */
    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId, Pageable pageable) {
        return r2dbcRepository.findByFranchiseIdAndNotDeleted(franchiseId)
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .take(pageable.getPageSize())
            .map(mapper::toDomain);
    }

    /**
     * Guarda una sucursal nueva o actualiza una existente.
     * Establece automáticamente createdAt para nuevas y updatedAt para todas.
     *
     * @param branch la sucursal a guardar
     * @return {@code Mono} conteniendo la sucursal guardada con ID asignado
     */
    @Override
    public Mono<Branch> save(Branch branch) {
        BranchEntity entity = mapper.toEntity(branch);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

    /**
     * Elimina suavemente una sucursal por ID.
     * Establece el timestamp deleted_at sin eliminar el registro.
     *
     * @param id el ID de la sucursal a eliminar
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
     * Actualiza una sucursal existente.
     * Establece el timestamp updatedAt a la hora actual.
     *
     * @param branch la sucursal con valores actualizados
     * @return {@code Mono} conteniendo la sucursal actualizada
     */
    @Override
    public Mono<Branch> update(Branch branch) {
        BranchEntity entity = mapper.toEntity(branch);
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }
}
