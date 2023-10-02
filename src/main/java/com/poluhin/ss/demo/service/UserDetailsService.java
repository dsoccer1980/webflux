package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.exception.AuthException;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserDetailsService {

    Mono<UserDetails> loadUserByUsername(String username) throws AuthException;

}
