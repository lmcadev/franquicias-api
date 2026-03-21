package com.accenture.franquicias_api.infrastructure.persistence.r2dbc.user;

import com.accenture.franquicias_api.infrastructure.persistence.entity.user.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserR2dbcRepository extends R2dbcRepository<UserEntity, Long> {

    @Query("SELECT * FROM users WHERE id = :id AND deleted_at IS NULL")
    Mono<UserEntity> findByIdAndNotDeleted(Long id);

    @Query("SELECT * FROM users WHERE email = :email AND deleted_at IS NULL")
    Mono<UserEntity> findByEmailAndNotDeleted(String email);

    @Query("SELECT COUNT(*) FROM users WHERE deleted_at IS NULL")
    Mono<Long> countNotDeleted();
}
