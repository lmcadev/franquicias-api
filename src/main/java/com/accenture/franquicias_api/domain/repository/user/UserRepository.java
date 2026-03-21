package com.accenture.franquicias_api.domain.repository.user;

import com.accenture.franquicias_api.domain.entity.user.User;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> findById(Long id);
    Mono<User> findByEmail(String email);
    Flux<User> findAll(Pageable pageable);
    Mono<User> save(User user);
    Mono<Void> delete(Long id);
    Mono<User> update(User user);
}
