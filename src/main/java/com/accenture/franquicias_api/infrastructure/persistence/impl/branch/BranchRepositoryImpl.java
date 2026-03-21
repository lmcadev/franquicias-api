package com.accenture.franquicias_api.infrastructure.persistence.impl.branch;

import com.accenture.franquicias_api.domain.entity.branch.Branch;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.branch.BranchEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.branch.BranchEntityMapper;
import com.accenture.franquicias_api.infrastructure.persistence.r2dbc.branch.BranchR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryImpl implements BranchRepository {
    private final BranchR2dbcRepository r2dbcRepository;
    private final BranchEntityMapper mapper;

    @Override
    public Mono<Branch> findById(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId, Pageable pageable) {
        return r2dbcRepository.findByFranchiseIdAndNotDeleted(franchiseId)
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .take(pageable.getPageSize())
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        BranchEntity entity = mapper.toEntity(branch);
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
    public Mono<Branch> update(Branch branch) {
        BranchEntity entity = mapper.toEntity(branch);
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }
}
