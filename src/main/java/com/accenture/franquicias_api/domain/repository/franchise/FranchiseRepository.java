package com.accenture.franquicias_api.domain.repository.franchise;

import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> findById(Long id);
    Flux<Franchise> findAll(Pageable pageable);
    Mono<Franchise> save(Franchise franchise);
    Mono<Void> delete(Long id);
    Mono<Franchise> update(Franchise franchise);
    Flux<Franchise> findByCreatedBy(Long createdBy, Pageable pageable);
}
