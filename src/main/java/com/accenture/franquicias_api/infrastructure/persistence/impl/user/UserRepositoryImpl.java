package com.accenture.franquicias_api.infrastructure.persistence.impl.user;

import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import com.accenture.franquicias_api.infrastructure.persistence.entity.user.UserEntity;
import com.accenture.franquicias_api.infrastructure.persistence.mapper.user.UserEntityMapper;
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
public class UserRepositoryImpl implements UserRepository {
    private final R2dbcEntityOperations entityOperations;
    private final UserEntityMapper mapper;

    @Override
    public Mono<User> findById(Long id) {
        return entityOperations
            .selectOne(Query.query(
                Criteria.where("id").is(id)
                    .and("deleted_at").isNull()
            ), UserEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return entityOperations
            .selectOne(Query.query(
                Criteria.where("email").is(email)
                    .and("deleted_at").isNull()
            ), UserEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Flux<User> findAll(Pageable pageable) {
        return entityOperations
            .select(Query.query(Criteria.where("deleted_at").isNull())
                .with(pageable), UserEntity.class)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<User> save(User user) {
        UserEntity entity = mapper.toEntity(user);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entityOperations.insert(entity)
            .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id)
            .flatMap(user -> {
                UserEntity entity = mapper.toEntity(user);
                entity.setDeletedAt(LocalDateTime.now());
                entity.setUpdatedAt(LocalDateTime.now());
                return entityOperations.update(entity).then();
            });
    }

    @Override
    public Mono<User> update(User user) {
        UserEntity entity = mapper.toEntity(user);
        entity.setUpdatedAt(LocalDateTime.now());
        return entityOperations.update(entity)
            .map(mapper::toDomain);
    }
}
