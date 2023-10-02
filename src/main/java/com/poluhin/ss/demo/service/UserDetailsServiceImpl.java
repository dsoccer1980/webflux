package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.exception.AuthException;
import com.poluhin.ss.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public Mono<UserDetails> loadUserByUsername(String username) throws AuthException {
        return userRepository.findByUsername(username).map(u -> (UserDetails) u)
            .switchIfEmpty(Mono.error(new AuthException("User not found", username)));
    }
}
