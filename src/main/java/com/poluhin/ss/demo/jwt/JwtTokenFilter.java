package com.poluhin.ss.demo.jwt;

import com.poluhin.ss.demo.domain.entity.User;
import com.poluhin.ss.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter implements WebFilter {

    private final JwtTokenUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!hasAuthorizationBearer(exchange)) {
            return chain.filter(exchange);
        }
        String token = getAccessToken(exchange);
        if (!jwtUtil.validateAccessToken(token)) {
            return chain.filter(exchange);
        }

        return getAuthenticationContext(token)
            .map(ReactiveSecurityContextHolder::withAuthentication)
            .flatMap(context -> chain.filter(exchange).contextWrite(context));
    }

    private boolean hasAuthorizationBearer(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }
        return true;
    }

    private String getAccessToken(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        return header.split(" ")[1].trim();
    }

    private Mono<UsernamePasswordAuthenticationToken> getAuthenticationContext(String token) {
        Mono<User> user = userRepository.findByUsername(jwtUtil.getSubject(token));
        return user
            .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
    }
}
