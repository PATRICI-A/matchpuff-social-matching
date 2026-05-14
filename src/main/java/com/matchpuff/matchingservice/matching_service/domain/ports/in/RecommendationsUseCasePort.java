package com.matchpuff.matchingservice.matching_service.domain.ports.in;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchProfile;

public interface RecommendationsUseCasePort {

    Map<UUID, AffinityScore> getRecommendationsForUser(UUID userId);
    List<MatchProfile> getRecommendedProfilesForUser(UUID userId);
    List<UUID> getRecommendedUserIdsForUser(UUID userId);
    AffinityScore calculateAffinityScore(UUID userId1, UUID userId2);
}
