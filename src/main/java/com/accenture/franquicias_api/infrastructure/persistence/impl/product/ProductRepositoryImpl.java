package com.accenture.franquicias_api.infrastructure.persistence.impl.product;

import com.accenture.franquicias_api.domain.entity.product.Product;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.product.ProductEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.product.ProductEntityMapper;
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
public class ProductRepositoryImpl implements ProductRepository {
    private final R2dbcEntityOperations entityOperations;
    private final ProductEntityMapper mapper;

    @Override
    public Mono<Product> findById(Long id) {
        return entityOperations
            .selectOne(Query.query(
                Criteria.where("id").is(id)
                    .and("deleted_at").isNull()
            ), ProductEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId, Pageable pageable) {
        return entityOperations
            .select(Query.query(
                Criteria.where("branch_id").is(branchId)
                    .and("deleted_at").isNull())
                .with(pageable), ProductEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entityOperations.insert(entity)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id)
            .flatMap(product -> {
                ProductEntity entity = mapper.toEntity(product);
                entity.setDeletedAt(LocalDateTime.now());
                entity.setUpdatedAt(LocalDateTime.now());
                return entityOperations.update(entity).then();
            });
    }

    @Override
    public Mono<Product> update(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        entity.setUpdatedAt(LocalDateTime.now());
        return entityOperations.update(entity)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findMaxStockByBranch(Long branchId) {
        return entityOperations
            .select(Query.query(
                Criteria.where("branch_id").is(branchId)
                    .and("deleted_at").isNull()), ProductEntity.class)
            .sort((p1, p2) -> Integer.compare(p2.getStock(), p1.getStock()))
            .next()
            .map(mapper::toDomain);
    }
}
