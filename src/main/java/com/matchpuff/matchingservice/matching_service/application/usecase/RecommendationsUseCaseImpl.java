package com.matchpuff.matchingservice.matching_service.application.usecase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.matchpuff.matchingservice.matching_service.application.service.AffinityCalculator;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.RecommendationsUseCasePort;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.ProfileServicePort;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationsUseCaseImpl implements RecommendationsUseCasePort {

    private final ProfileServicePort profileServicePort;
    private final AffinityCalculator affinityCalculator;


    @Override
    public Map<UUID, AffinityScore> getRecommendationsForUser(UUID userId) {

        // 1. Perfil del usuario base (Feign)
        UserMatchProfileDto requester =
                profileServicePort.getProfileById(userId);

        // 2. Todos los perfiles disponibles (Feign)
        var allProfiles = profileServicePort.getAllProfiles();

        Map<UUID, AffinityScore> recommendations = new HashMap<>();

        // 3. Comparar contra todos
        for (UserMatchProfileDto target : allProfiles) {

            // evitar compararse consigo mismo
            if (target.getId().equals(userId)) continue;

            // calcular afinidad (dominio puro)
            AffinityScore score =
                    affinityCalculator.calculate(requester, target);

            recommendations.put(target.getId(), score);
        }

        return recommendations;
    }

    @Override
    public List<UserMatchProfileDto> getRecommendedProfilesForUser(UUID userId) {
        Map<UUID, AffinityScore> recommendations = getRecommendationsForUser(userId);
        List<UserMatchProfileDto> allProfiles = profileServicePort.getAllProfiles();

        return allProfiles.stream()
                .filter(profile -> !profile.getId().equals(userId))
                .sorted(Comparator.comparingDouble((UserMatchProfileDto profile) ->
                        recommendations.getOrDefault(profile.getId(), new AffinityScore()).getTotalScore()).reversed())
                .toList();
    }

    @Override
    public AffinityScore calculateAffinityScore(UUID userId1, UUID userId2) {

        UserMatchProfileDto a =
                profileServicePort.getProfileById(userId1);

        UserMatchProfileDto b =
                profileServicePort.getProfileById(userId2);

        return affinityCalculator.calculate(a, b);
    }

    @Override
    public List<UUID> getRecommendedUserIdsForUser(UUID userId) {
        return getRecommendedProfilesForUser(userId).stream()
            .map(UserMatchProfileDto::getId)
                .toList();
    }
}
