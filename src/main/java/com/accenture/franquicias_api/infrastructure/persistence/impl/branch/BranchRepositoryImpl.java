package com.accenture.franquicias_api.infrastructure.persistence.impl.branch;

import com.accenture.franquicias_api.domain.entity.branch.Branch;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.branch.BranchEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.branch.BranchEntityMapper;
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
public class BranchRepositoryImpl implements BranchRepository {
    private final R2dbcEntityOperations entityOperations;
    private final BranchEntityMapper mapper;

    @Override
    public Mono<Branch> findById(Long id) {
        return entityOperations
            .selectOne(Query.query(
                Criteria.where("id").is(id)
                    .and("deleted_at").isNull()
            ), BranchEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId, Pageable pageable) {
        return entityOperations
            .select(Query.query(
                Criteria.where("franchise_id").is(franchiseId)
                    .and("deleted_at").isNull())
                .with(pageable), BranchEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        BranchEntity entity = mapper.toEntity(branch);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entityOperations.insert(entity)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id)
            .flatMap(branch -> {
                BranchEntity entity = mapper.toEntity(branch);
                entity.setDeletedAt(LocalDateTime.now());
                entity.setUpdatedAt(LocalDateTime.now());
                return entityOperations.update(entity).then();
            });
    }

    @Override
    public Mono<Branch> update(Branch branch) {
        BranchEntity entity = mapper.toEntity(branch);
        entity.setUpdatedAt(LocalDateTime.now());
        return entityOperations.update(entity)
            .map(mapper::toDomain);
    }
}
