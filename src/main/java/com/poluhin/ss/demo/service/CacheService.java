package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.model.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CacheService {

    private final ReactiveRedisOperations<String, AuthResponse> authOps;

    public void write(AuthResponse authResponse) {
        authOps.opsForValue().set(authResponse.getUsername(), authResponse).subscribe();
    }

    public Mono<AuthResponse> get(String username) {
        return authOps.opsForValue().get(username);
    }

}
