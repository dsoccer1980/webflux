package com.poluhin.ss.demo.controller;

import com.poluhin.ss.demo.domain.model.ResourceObject;
import com.poluhin.ss.demo.service.ResourceObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceObjectService service;

    @GetMapping
    public Flux<ResourceObject> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Mono<Integer> createResourceObject(@RequestBody ResourceObject object) {
        return service.save(object);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResourceObject>> getResourceObject(@PathVariable Integer id) {
        return service.get(id).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    public Flux<ResourceObject> searchByValue(@RequestParam(required = false) String value,
        @RequestParam(required = false) String path) {
        return service.searchByValue(value, path);
    }

}
