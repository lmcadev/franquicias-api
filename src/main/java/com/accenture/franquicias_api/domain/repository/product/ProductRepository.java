package com.accenture.franquicias_api.domain.repository.product;

import com.accenture.franquicias_api.domain.entity.product.Product;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> findById(Long id);
    Flux<Product> findByBranchId(Long branchId, Pageable pageable);
    Mono<Product> save(Product product);
    Mono<Void> delete(Long id);
    Mono<Product> update(Product product);
    Mono<Product> findMaxStockByBranch(Long branchId);
    Mono<Product> findMaxStockByFranchise(Long franchiseId);
}
