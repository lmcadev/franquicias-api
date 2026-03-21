package com.accenture.franquicias_api.domain.repository.branch;

import com.accenture.franquicias_api.domain.entity.branch.Branch;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Branch> findById(Long id);
    Flux<Branch> findByFranchiseId(Long franchiseId, Pageable pageable);
    Mono<Branch> save(Branch branch);
    Mono<Void> delete(Long id);
    Mono<Branch> update(Branch branch);
}
