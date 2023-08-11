package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.model.AuthResponse;
import com.poluhin.ss.demo.exception.AuthException;
import com.poluhin.ss.demo.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserDetailsService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public Mono<AuthResponse> authenticate(String username, String password) {
        return userService.loadUserByUsername(username)
            .flatMap(user -> {
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    return Mono.error(new AuthException("Invalid password"));
                }

                String token = jwtTokenUtil.generateAccessToken(user);
                return Mono.just(new AuthResponse(user.getUsername(), token));
            })
            .switchIfEmpty(Mono.error(new AuthException("Invalid username")));
    }

}
