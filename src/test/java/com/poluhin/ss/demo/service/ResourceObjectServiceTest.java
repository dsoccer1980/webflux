package com.poluhin.ss.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import com.poluhin.ss.demo.domain.entity.ResourceObjectEntity;
import com.poluhin.ss.demo.domain.model.ResourceObject;
import com.poluhin.ss.demo.repository.ResourceObjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ResourceObjectServiceTest {

    @Autowired
    private ResourceObjectService service;
    @MockBean
    private ResourceObjectRepository repository;
    @MockBean
    private ReactiveMongoTemplate template;

    @Test
    void save() {
        ResourceObject resourceObject = new ResourceObject(1, "test", "path");
        ResourceObjectEntity entity = new ResourceObjectEntity(1, "test", "path");
        doReturn(Mono.just(entity)).when(repository).save(entity);

        Mono<Integer> result = service.save(resourceObject);

        StepVerifier
            .create(result)
            .assertNext(r -> assertEquals(1, r))
            .expectComplete()
            .verify();
    }

    @Test
    void get() {
        ResourceObjectEntity entity = new ResourceObjectEntity(1, "test", "path");
        ResourceObject resourceObject = new ResourceObject(1, "test", "path");
        doReturn(Mono.just(entity)).when(repository).findById(1);

        Mono<ResourceObject> resourceMono = service.get(1);

        StepVerifier
            .create(resourceMono)
            .assertNext(resource -> assertEquals(resourceObject, resource))
            .expectComplete()
            .verify();
    }

    @Test
    void getAll() {
        ResourceObjectEntity entity1 = new ResourceObjectEntity(1, "test", "path");
        ResourceObjectEntity entity2 = new ResourceObjectEntity(2, "test2", "path2");
        ResourceObject resource1 = new ResourceObject(1, "test", "path");
        ResourceObject resource2 = new ResourceObject(2, "test2", "path2");
        doReturn(Flux.just(entity1, entity2)).when(repository).findAll();

        Flux<ResourceObject> all = service.getAll();

        StepVerifier
            .create(all)
            .assertNext(resource -> assertEquals(resource1, resource))
            .assertNext(resource -> assertEquals(resource2, resource))
            .expectComplete()
            .verify();
    }

    @Test
    void searchByValue() {
        ResourceObjectEntity entity = new ResourceObjectEntity(1, "test", "path");
        ResourceObject resourceObject = new ResourceObject(1, "test", "path");
        doReturn(Flux.just(entity)).when(template).find(any(Query.class), eq(ResourceObjectEntity.class));

        Flux<ResourceObject> result = service.searchByValue("test", "path");

        StepVerifier
            .create(result)
            .assertNext(resource -> assertEquals(resourceObject, resource))
            .expectComplete()
            .verify();
    }
}