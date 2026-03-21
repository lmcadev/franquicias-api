package com.accenture.franquicias_api.infrastructure.persistence.impl.product;

import com.accenture.franquicias_api.domain.entity.product.Product;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.product.ProductEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.product.ProductEntityMapper;
import com.accenture.franquicias_api.infrastructure.persistence.r2dbc.product.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductR2dbcRepository r2dbcRepository;
    private final ProductEntityMapper mapper;

    @Override
    public Mono<Product> findById(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId, Pageable pageable) {
        return r2dbcRepository.findByBranchIdAndNotDeleted(branchId)
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .take(pageable.getPageSize())
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

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

    @Override
    public Mono<Product> update(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findMaxStockByBranch(Long branchId) {
        return r2dbcRepository.findMaxStockProductByBranchId(branchId)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findMaxStockByFranchise(Long franchiseId) {
        return r2dbcRepository.findMaxStockProductByFranchiseId(franchiseId)
            .map(mapper::toDomain);
    }
}
