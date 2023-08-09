package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.entity.ResourceObjectEntity;
import com.poluhin.ss.demo.domain.model.ResourceObject;
import com.poluhin.ss.demo.repository.ResourceObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ResourceObjectService {

    private final ResourceObjectRepository repository;
    private final ReactiveMongoTemplate template;

    public Mono<Integer> save(ResourceObject resourceObject) {
        return repository.save(new ResourceObjectEntity(
            resourceObject.getId(), resourceObject.getValue(),
            resourceObject.getPath()))
            .map(ResourceObjectEntity::getId);

    }

    public Mono<ResourceObject> get(int id) {
        return repository.findById(id)
            .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()));
    }

    public Flux<ResourceObject> getAll() {
        return repository.findAll().map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()));
    }

    public Flux<ResourceObject> searchByValue(String value, String path) {
        Query query = new Query();
        if (value != null) {
            query.addCriteria(
                Criteria.where("value")
                    .regex(value)
            );
        }
        if (path != null) {
            query.addCriteria(
                Criteria.where("path")
                    .regex(path)
            );
        }
        return template.find(query, ResourceObjectEntity.class)
            .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()));
    }
}
