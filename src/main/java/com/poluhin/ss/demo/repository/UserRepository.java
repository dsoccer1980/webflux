package com.poluhin.ss.demo.repository;

import com.poluhin.ss.demo.domain.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, Long> {

    Mono<User> findByUsername(String username);

}
