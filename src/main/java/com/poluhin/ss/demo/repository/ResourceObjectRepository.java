package com.poluhin.ss.demo.repository;

import com.poluhin.ss.demo.domain.entity.ResourceObjectEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ResourceObjectRepository extends ReactiveMongoRepository<ResourceObjectEntity, Integer> {


}
