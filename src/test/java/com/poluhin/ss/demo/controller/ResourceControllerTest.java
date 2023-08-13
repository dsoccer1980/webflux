package com.poluhin.ss.demo.controller;

import static org.mockito.Mockito.doReturn;

import com.poluhin.ss.demo.domain.model.ResourceObject;
import com.poluhin.ss.demo.jwt.JwtTokenFilter;
import com.poluhin.ss.demo.jwt.JwtTokenUtil;
import com.poluhin.ss.demo.repository.UserRepository;
import com.poluhin.ss.demo.service.ResourceObjectService;
import com.poluhin.ss.demo.service.UsersFillService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(value = ResourceController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
@Import({JwtTokenUtil.class, JwtTokenFilter.class})
@MockBean({UserRepository.class, UsersFillService.class})
class ResourceControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private ResourceObjectService service;

    @Test
    void getAll() {
        ResourceObject resourceObject = new ResourceObject(1, "test", "path");
        doReturn(Flux.just(resourceObject)).when(service).getAll();

        this.webTestClient.get().uri("/resource")
            .exchange()
            .expectStatus().isOk()
            .expectBody().json("[{\"id\":1,\"value\":\"test\",\"path\": \"path\"}]").returnResult();
    }

    @Test
    void createResourceObject() {
        ResourceObject resourceObject = new ResourceObject(1, "test", "path");
        doReturn(Mono.just(1)).when(service).save(resourceObject);

        this.webTestClient.post().uri("/resource")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(resourceObject), ResourceObject.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json("1").returnResult();
    }

    @Test
    void getResourceObject() {
        ResourceObject resourceObject = new ResourceObject(1, "test", "path");
        doReturn(Mono.just(resourceObject)).when(service).get(1);

        this.webTestClient.get().uri("/resource/{id}", 1)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json("{\"id\":1,\"value\":\"test\",\"path\": \"path\"}").returnResult();
    }

}