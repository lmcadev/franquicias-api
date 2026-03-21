package com.accenture.franquicias_api.infrastructure.persistence.impl.franchise;

import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.franchise.FranchiseEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.franchise.FranchiseEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryImpl implements FranchiseRepository {
    private final R2dbcEntityOperations entityOperations;
    private final FranchiseEntityMapper mapper;

    @Override
    public Mono<Franchise> findById(Long id) {
        return entityOperations
            .selectOne(Query.query(
                Criteria.where("id").is(id)
                    .and("deleted_at").isNull()
            ), FranchiseEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll(Pageable pageable) {
        return entityOperations
            .select(Query.query(Criteria.where("deleted_at").isNull())
                .with(pageable), FranchiseEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = mapper.toEntity(franchise);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entityOperations.insert(entity)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id)
            .flatMap(franchise -> {
                FranchiseEntity entity = mapper.toEntity(franchise);
                entity.setDeletedAt(LocalDateTime.now());
                entity.setUpdatedAt(LocalDateTime.now());
                return entityOperations.update(entity).then();
            });
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        FranchiseEntity entity = mapper.toEntity(franchise);
        entity.setUpdatedAt(LocalDateTime.now());
        return entityOperations.update(entity)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findByCreatedBy(Long createdBy, Pageable pageable) {
        return entityOperations
            .select(Query.query(
                Criteria.where("created_by").is(createdBy)
                    .and("deleted_at").isNull())
                .with(pageable), FranchiseEntity.class)
            .map(mapper::toDomain);
    }
}
