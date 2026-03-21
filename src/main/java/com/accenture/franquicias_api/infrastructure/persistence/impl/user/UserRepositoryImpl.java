package com.accenture.franquicias_api.infrastructure.persistence.impl.user;

import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.user.UserEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.user.UserEntityMapper;
import com.accenture.franquicias_api.infrastructure.persistence.r2dbc.user.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserR2dbcRepository r2dbcRepository;
    private final UserEntityMapper mapper;

    @Override
    public Mono<User> findById(Long id) {
        return r2dbcRepository.findByIdAndNotDeleted(id)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return r2dbcRepository.findByEmailAndNotDeleted(email)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<User> findAll(Pageable pageable) {
        return r2dbcRepository.findAll()
            .filter(entity -> entity.getDeletedAt() == null)
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .take(pageable.getPageSize())
            .map(mapper::toDomain);
    }

    @Override
    public Mono<User> save(User user) {
        UserEntity entity = mapper.toEntity(user);
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
    public Mono<User> update(User user) {
        UserEntity entity = mapper.toEntity(user);
        entity.setUpdatedAt(LocalDateTime.now());
        return r2dbcRepository.save(entity)
            .map(mapper::toDomain);
    }
}
