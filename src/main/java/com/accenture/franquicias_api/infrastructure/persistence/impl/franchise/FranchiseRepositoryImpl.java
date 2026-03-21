package com.accenture.franquicias_api.infrastructure.persistence.impl.franchise;

import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.franchise.FranchiseEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.franchise.FranchiseEntityMapper;
import com.accenture.franquicias_api.infrastructure.persistence.r2dbc.franchise.FranchiseR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación reactiva del repositorio de Franquicia.
 * Proporciona operaciones CRUD para franquicias traduciendo entre entidades de dominio y persistencia.
 *
 * <p>
 * Esta clase implementa la interfaz {@link FranchiseRepository} utilizando R2DBC para
 * acceso reactivo a la base de datos. Maneja la conversión bidireccional entre
 * entidades de dominio ({@link Franchise}) y entidades de persistencia ({@link FranchiseEntity}).
 * </p>
 *
 * <p>
 * Características principales:
 * <ul>
 *   <li>Operaciones CRUD no-bloqueantes (Mono/Flux)</li>
 *   <li>Eliminación suave (soft delete) con timestamp deleted_at</li>
 *   <li>Búsqueda de franquicias por propietario (createdBy)</li>
 *   <li>Paginación de resultados</li>
 *   <li>Auditoría automática de fechas (createdAt, updatedAt)</li>
 * </ul>
 * </p>
 *
 * @see FranchiseRepository
 * @see Franchise
 * @see FranchiseEntity
 * @see FranchiseEntityMapper
 */
@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryImpl implements FranchiseRepository {
    private final FranchiseR2dbcRepository r2dbcRepository;
    private final FranchiseEntityMapper mapper;

    /**
     * Busca una franquicia por su ID.
     * Filtra registros eliminados suavemente.
     *
     * @param id el ID de la franquicia
     * @return {@code Mono} conteniendo la franquicia si existe, vacío en caso contrario
     */
    @Override
    public Mono<Franchise> findById(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .map(mapper::toDomain);
    }

    /**
     * Recupera todas las franquicias activas con paginación.
     * Filtra automáticamente registros eliminados suavemente.
     *
     * @param pageable parámetros de paginación
     * @return {@code Flux} emitiendo franquicias de la página solicitada
     */
    @Override
    public Flux<Franchise> findAll(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        return r2dbcRepository.findAllNotDeleted(limit, offset)
            .map(mapper::toDomain);
    }

    /**
     * Guarda una franquicia nueva o actualiza una existente.
     * Establece automáticamente createdAt para nuevas y updatedAt para todas.
     *
     * @param franchise la franquicia a guardar
     * @return {@code Mono} conteniendo la franquicia guardada con ID asignado
     */
    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = mapper.toEntity(franchise);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

    /**
     * Elimina suavemente una franquicia por ID.
     * Establece el timestamp deleted_at sin eliminar el registro.
     *
     * @param id el ID de la franquicia a eliminar
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
     * Actualiza una franquicia existente.
     * Establece el timestamp updatedAt a la hora actual.
     *
     * @param franchise la franquicia con valores actualizados
     * @return {@code Mono} conteniendo la franquicia actualizada
     */
    @Override
    public Mono<Franchise> update(Franchise franchise) {
        FranchiseEntity entity = mapper.toEntity(franchise);
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

    /**
     * Busca todas las franquicias creadas por un usuario específico.
     * Útil para recuperar franquicias propiedad de un usuario en particular.
     *
     * @param createdBy el ID del usuario propietario
     * @param pageable parámetros de paginación
     * @return {@code Flux} emitiendo franquicias del usuario
     */
    @Override
    public Flux<Franchise> findByCreatedBy(Long createdBy, Pageable pageable) {
        return r2dbcRepository.findByCreatedByAndNotDeleted(createdBy)
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .take(pageable.getPageSize())
            .map(mapper::toDomain);
    }
}
