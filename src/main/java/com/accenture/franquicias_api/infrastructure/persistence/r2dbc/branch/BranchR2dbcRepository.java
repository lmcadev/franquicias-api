package com.accenture.franquicias_api.infrastructure.persistence.r2dbc.branch;

import com.accenture.franquicias_api.infrastructure.persistence.entity.branch.BranchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BranchR2dbcRepository extends R2dbcRepository<BranchEntity, Long> {

    @Query("SELECT * FROM branches WHERE franchise_id = :franchiseId AND deleted_at IS NULL")
    Flux<BranchEntity> findByFranchiseIdAndNotDeleted(Long franchiseId);

    @Query("SELECT * FROM branches WHERE id = :id AND deleted_at IS NULL")
    Mono<BranchEntity> findByIdAndNotDeleted(Long id);

    @Query("SELECT * FROM branches WHERE deleted_at IS NULL LIMIT :limit OFFSET :offset")
    Flux<BranchEntity> findAllNotDeleted(int limit, int offset);

    @Query("SELECT COUNT(*) FROM branches WHERE deleted_at IS NULL")
    Mono<Long> countNotDeleted();

    @Query("SELECT COUNT(*) FROM branches WHERE franchise_id = :franchiseId AND deleted_at IS NULL")
    Mono<Long> countByFranchiseIdAndNotDeleted(Long franchiseId);
}
