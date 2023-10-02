package com.poluhin.ss.demo.config;

import com.poluhin.ss.demo.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(authorize ->
            authorize
                .pathMatchers("/auth/login").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/v3/**", "swagger-ui/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/resource/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, "/resource/**").hasAnyRole("USER", "ADMIN")
                .anyExchange().authenticated()
        ).csrf(CsrfSpec::disable);
        http.exceptionHandling(e ->
            e.authenticationEntryPoint((swe, ex) ->
                Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))));
        http.addFilterBefore(jwtTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
