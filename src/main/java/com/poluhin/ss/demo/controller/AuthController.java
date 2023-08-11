package com.poluhin.ss.demo.controller;

import com.poluhin.ss.demo.domain.model.AuthRequest;
import com.poluhin.ss.demo.domain.model.AuthResponse;
import com.poluhin.ss.demo.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final SecurityService securityService;

    @PostMapping("/auth/login")
    public Mono<AuthResponse> login(@RequestBody AuthRequest request) {
        return securityService.authenticate(request.getUsername(), request.getPassword());
    }
}
