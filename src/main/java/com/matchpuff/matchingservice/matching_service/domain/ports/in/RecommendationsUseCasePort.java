package com.matchpuff.matchingservice.matching_service.domain.ports.in;

import java.util.Map;
import java.util.UUID;

import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;

public interface RecommendationsUseCasePort {

    Map<UUID, AffinityScore> getRecommendationsForUser(UUID userId);
    AffinityScore calculateAffinityScore(UUID userId1, UUID userId2);
}
