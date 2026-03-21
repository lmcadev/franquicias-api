package com.accenture.franquicias_api.infrastructure.persistence.r2dbc.product;

import com.accenture.franquicias_api.infrastructure.persistence.entity.product.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, Long> {

    @Query("SELECT * FROM products WHERE branch_id = :branchId AND deleted_at IS NULL")
    Flux<ProductEntity> findByBranchIdAndNotDeleted(Long branchId);

    @Query("SELECT * FROM products WHERE id = :id AND deleted_at IS NULL")
    Mono<ProductEntity> findByIdAndNotDeleted(Long id);

    @Query("SELECT * FROM products WHERE deleted_at IS NULL LIMIT :limit OFFSET :offset")
    Flux<ProductEntity> findAllNotDeleted(int limit, int offset);

    @Query("SELECT COUNT(*) FROM products WHERE deleted_at IS NULL")
    Mono<Long> countNotDeleted();

    @Query("SELECT COUNT(*) FROM products WHERE branch_id = :branchId AND deleted_at IS NULL")
    Mono<Long> countByBranchIdAndNotDeleted(Long branchId);

    @Query("""
        SELECT * FROM products
        WHERE branch_id = :branchId
          AND deleted_at IS NULL
        ORDER BY stock DESC
        LIMIT 1
        """)
    Mono<ProductEntity> findMaxStockProductByBranchId(Long branchId);

    @Query("""
        SELECT p.* FROM products p
        INNER JOIN branches b ON p.branch_id = b.id
        WHERE b.franchise_id = :franchiseId
          AND p.deleted_at IS NULL
          AND b.deleted_at IS NULL
        ORDER BY p.stock DESC
        LIMIT 1
        """)
    Mono<ProductEntity> findMaxStockProductByFranchiseId(Long franchiseId);
}
