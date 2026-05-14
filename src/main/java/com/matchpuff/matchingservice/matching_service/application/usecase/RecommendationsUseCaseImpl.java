package com.matchpuff.matchingservice.matching_service.application.usecase;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.matchpuff.matchingservice.matching_service.application.service.AffinityCalculator;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchProfile;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.RecommendationsUseCasePort;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.ProfileServicePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationsUseCaseImpl implements RecommendationsUseCasePort {

    private final ProfileServicePort profileServicePort;
    private final AffinityCalculator affinityCalculator;

    @Override
    public Map<UUID, AffinityScore> getRecommendationsForUser(UUID userId) {
        MatchProfile requester = profileServicePort.getProfileById(userId);
        List<MatchProfile> allProfiles = profileServicePort.getAllProfiles();

        Map<UUID, AffinityScore> recommendations = new HashMap<>();
        for (MatchProfile target : allProfiles) {
            if (target.getId().equals(userId)) continue;
            recommendations.put(target.getId(), affinityCalculator.calculate(requester, target));
        }

        return recommendations;
    }

    @Override
    public List<MatchProfile> getRecommendedProfilesForUser(UUID userId) {
        MatchProfile requester = profileServicePort.getProfileById(userId);
        List<MatchProfile> allProfiles = profileServicePort.getAllProfiles();

        return allProfiles.stream()
                .filter(profile -> !profile.getId().equals(userId))
                .sorted(Comparator.comparingDouble((MatchProfile profile) ->
                        affinityCalculator.calculate(requester, profile).getTotalScore()).reversed())
                .toList();
    }

    @Override
    public List<UUID> getRecommendedUserIdsForUser(UUID userId) {
        return getRecommendedProfilesForUser(userId).stream()
                .map(MatchProfile::getId)
                .toList();
    }

    @Override
    public AffinityScore calculateAffinityScore(UUID userId1, UUID userId2) {
        MatchProfile a = profileServicePort.getProfileById(userId1);
        MatchProfile b = profileServicePort.getProfileById(userId2);
        return affinityCalculator.calculate(a, b);
    }
}
