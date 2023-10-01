package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.model.AuthResponse;
import com.poluhin.ss.demo.exception.AuthException;
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

    public Mono<AuthResponse> authenticate(String username, String password) {
        return userService.loadUserByUsername(username)
            .flatMap(user -> {
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    return Mono.error(new AuthException("Password not match"));
                }
                return getAccessToken(user);
            })
            .switchIfEmpty(Mono.error(new AuthException("Invalid username")));
    }

    private Mono<AuthResponse> getAccessToken(UserDetails userDetails) {
        return cache.get(userDetails.getUsername())
            .switchIfEmpty(Mono.defer(() ->
                Mono.just(new AuthResponse(userDetails.getUsername(), jwtTokenUtil.generateAccessToken(userDetails)))));
    }


}
