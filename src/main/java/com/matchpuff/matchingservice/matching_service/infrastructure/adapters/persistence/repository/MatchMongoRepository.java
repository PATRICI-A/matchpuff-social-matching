package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.repository;

import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.MatchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchMongoRepository extends MongoRepository<MatchDocument, UUID> {
    List<MatchDocument> findByRequesterId(UUID requesterId);

    List<MatchDocument> findByTargetId(UUID targetId);

    boolean existsByRequesterIdAndTargetId(UUID requesterId, UUID targetId);
}
