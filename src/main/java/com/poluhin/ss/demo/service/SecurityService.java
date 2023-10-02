package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.model.AuthResponse;
import com.poluhin.ss.demo.exception.AuthException;
import com.poluhin.ss.demo.exception.PasswordWrongException;
import com.poluhin.ss.demo.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserDetailsService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cache;
    private final KafkaService kafkaService;
    private final PrometheusCounter prometheusCounter;

    public Mono<AuthResponse> authenticate(String username, String password) {
        return userService.loadUserByUsername(username)
            .flatMap(user -> {
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    return Mono.error(new PasswordWrongException("Password not match", username));
                }
                return getAccessToken(user);
            })
            .doOnSuccess(x -> {
                kafkaService.sendMessageToAuthEvent(String.format("user %s successful log in", username));
                prometheusCounter.getSuccesLogin().increment();
            })
            .switchIfEmpty(Mono.error(new AuthException("Invalid username", username)));
    }

    private Mono<AuthResponse> getAccessToken(UserDetails userDetails) {
        return cache.get(userDetails.getUsername())
            .switchIfEmpty(Mono.defer(() ->
                Mono.just(new AuthResponse(userDetails.getUsername(), jwtTokenUtil.generateAccessToken(userDetails)))));
    }


}
