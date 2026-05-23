package com.matchpuff.matchingservice.matching_service.application.usecase;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.matchpuff.matchingservice.matching_service.application.service.AffinityCalculator;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchProfile;
import com.matchpuff.matchingservice.matching_service.domain.model.NearbyRecommendation;
import com.matchpuff.matchingservice.matching_service.domain.model.NearbyUserDistance;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.RecommendationsUseCasePort;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.GeolocationServicePort;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.ProfileServicePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationsUseCaseImpl implements RecommendationsUseCasePort {

    private final ProfileServicePort profileServicePort;
    private final GeolocationServicePort geolocationServicePort;
    private final AffinityCalculator affinityCalculator;

    @Override
    public Map<UUID, AffinityScore> getRecommendationsForUser(UUID userId) {
        MatchProfile requester = profileServicePort.getProfileById(userId);
        List<MatchProfile> otherProfiles = getAllOtherProfiles(userId);

        Map<UUID, AffinityScore> recommendations = new HashMap<>();
        for (MatchProfile target : otherProfiles) {
            recommendations.put(target.getId(), affinityCalculator.calculate(requester, target));
        }

        return recommendations;
    }

    @Override
    public List<MatchProfile> getRecommendedProfilesForUser(UUID userId) {
        Map<UUID, AffinityScore> scores = getRecommendationsForUser(userId);
        return getAllOtherProfiles(userId).stream()
                .filter(profile -> scores.containsKey(profile.getId()))
                .sorted(Comparator.comparingDouble((MatchProfile profile) ->
                        scores.get(profile.getId()).getTotalScore()).reversed())
                .toList();
    }

    @Override
    public List<UUID> getRecommendedUserIdsForUser(UUID userId) {
        return getRecommendationsForUser(userId).entrySet().stream()
                .sorted(Map.Entry.<UUID, AffinityScore>comparingByValue(
                        Comparator.comparingDouble(AffinityScore::getTotalScore)).reversed())
                .map(Map.Entry::getKey)
                .toList();
    }

        @Override
        public List<NearbyRecommendation> getNearbyRecommendationsForUser(UUID userId) {
        MatchProfile requester = profileServicePort.getProfileById(userId);
        List<NearbyUserDistance> nearbyUsers = geolocationServicePort.getNearbyUsers(userId);
        Map<UUID, Double> distanceByUserId = nearbyUsers.stream()
            .collect(Collectors.toMap(NearbyUserDistance::getUserId, NearbyUserDistance::getDistanceMeters));
        Set<UUID> nearbyUserIds = new HashSet<>(distanceByUserId.keySet());

        return getAllOtherProfiles(userId).stream()
            .filter(profile -> nearbyUserIds.contains(profile.getId()))
            .map(profile -> new NearbyRecommendation(
                profile.getId(),
                distanceByUserId.get(profile.getId()),
                affinityCalculator.calculate(requester, profile)))
            .sorted(Comparator.comparingDouble((NearbyRecommendation recommendation) ->
                recommendation.getAffinityScore().getTotalScore()).reversed())
            .toList();
        }

    @Override
    public AffinityScore calculateAffinityScore(UUID userId1, UUID userId2) {
        MatchProfile a = profileServicePort.getProfileById(userId1);
        MatchProfile b = profileServicePort.getProfileById(userId2);
        return affinityCalculator.calculate(a, b);
    }

    private List<MatchProfile> getAllOtherProfiles(UUID userId) {
        return profileServicePort.getAllProfiles(userId);
    }
}
