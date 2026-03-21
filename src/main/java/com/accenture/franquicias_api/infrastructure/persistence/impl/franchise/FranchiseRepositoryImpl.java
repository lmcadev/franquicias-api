package com.accenture.franquicias_api.infrastructure.persistence.impl.franchise;

import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.franchise.FranchiseEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.franchise.FranchiseEntityMapper;
import com.accenture.franquicias_api.infrastructure.persistence.r2dbc.franchise.FranchiseR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryImpl implements FranchiseRepository {
    private final FranchiseR2dbcRepository r2dbcRepository;
    private final FranchiseEntityMapper mapper;

    @Override
    public Mono<Franchise> findById(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        return r2dbcRepository.findAllNotDeleted(limit, offset)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = mapper.toEntity(franchise);
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
    public Mono<Franchise> update(Franchise franchise) {
        FranchiseEntity entity = mapper.toEntity(franchise);
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findByCreatedBy(Long createdBy, Pageable pageable) {
        return r2dbcRepository.findByCreatedByAndNotDeleted(createdBy)
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .take(pageable.getPageSize())
            .map(mapper::toDomain);
    }
}
