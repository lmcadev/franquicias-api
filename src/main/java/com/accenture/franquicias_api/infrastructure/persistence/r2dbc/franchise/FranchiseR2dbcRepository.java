package com.accenture.franquicias_api.infrastructure.persistence.r2dbc.franchise;

import com.accenture.franquicias_api.infrastructure.persistence.entity.franchise.FranchiseEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FranchiseR2dbcRepository extends R2dbcRepository<FranchiseEntity, Long> {

    @Query("SELECT * FROM franchises WHERE created_by = :userId AND deleted_at IS NULL")
    Flux<FranchiseEntity> findByCreatedByAndNotDeleted(Long userId);

    @Query("SELECT * FROM franchises WHERE id = :id AND deleted_at IS NULL")
    Mono<FranchiseEntity> findByIdAndNotDeleted(Long id);

    @Query("SELECT * FROM franchises WHERE deleted_at IS NULL LIMIT :limit OFFSET :offset")
    Flux<FranchiseEntity> findAllNotDeleted(int limit, int offset);

    @Query("SELECT COUNT(*) FROM franchises WHERE deleted_at IS NULL")
    Mono<Long> countNotDeleted();
}
