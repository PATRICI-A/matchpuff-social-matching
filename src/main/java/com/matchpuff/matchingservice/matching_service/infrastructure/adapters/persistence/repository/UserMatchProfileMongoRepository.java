package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.repository;

import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.UserMatchProfileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserMatchProfileMongoRepository extends MongoRepository<UserMatchProfileDocument, UUID> {
}
