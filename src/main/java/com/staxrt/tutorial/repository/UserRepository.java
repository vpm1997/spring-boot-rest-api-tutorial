package com.staxrt.tutorial.repository;


import com.staxrt.tutorial.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * The interface User repository.
 */
public interface UserRepository extends R2dbcRepository<User, Long> {

  Mono<User> findByEmail(String email);
}
