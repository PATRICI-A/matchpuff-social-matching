package com.matchpuff.matchingservice.matching_service.application.usecase;

import com.matchpuff.matchingservice.matching_service.application.service.AffinityCalculator;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.ProfileServicePort;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationsUseCaseImplTest {

    @Mock
    private ProfileServicePort profileServicePort;

    @Mock
    private AffinityCalculator affinityCalculator;

    @InjectMocks
    private RecommendationsUseCaseImpl recommendationsUseCase;

    private UUID userId;
    private UUID otherId1;
    private UUID otherId2;
    private UserMatchProfileDto userProfile;
    private UserMatchProfileDto otherProfile1;
    private UserMatchProfileDto otherProfile2;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        otherId1 = UUID.randomUUID();
        otherId2 = UUID.randomUUID();

        userProfile = new UserMatchProfileDto();
        userProfile.setId(userId);

        otherProfile1 = new UserMatchProfileDto();
        otherProfile1.setId(otherId1);

        otherProfile2 = new UserMatchProfileDto();
        otherProfile2.setId(otherId2);
    }

    private AffinityScore scoreWith(double total) {
        AffinityScore s = new AffinityScore();
        s.setTotalScore(total);
        return s;
    }

    @Test
    void getRecommendationsForUser_excludesSelf() {
        when(profileServicePort.getProfileById(userId)).thenReturn(userProfile);
        when(profileServicePort.getAllProfiles()).thenReturn(List.of(userProfile, otherProfile1));
        when(affinityCalculator.calculate(any(), any())).thenReturn(scoreWith(0.8));

        Map<UUID, AffinityScore> result = recommendationsUseCase.getRecommendationsForUser(userId);

        assertThat(result).doesNotContainKey(userId);
        assertThat(result).containsKey(otherId1);
    }

    @Test
    void getRecommendationsForUser_returnsMapWithAffinityScores() {
        when(profileServicePort.getProfileById(userId)).thenReturn(userProfile);
        when(profileServicePort.getAllProfiles()).thenReturn(List.of(userProfile, otherProfile1, otherProfile2));
        AffinityScore score1 = scoreWith(0.7);
        AffinityScore score2 = scoreWith(0.5);
        when(affinityCalculator.calculate(userProfile, otherProfile1)).thenReturn(score1);
        when(affinityCalculator.calculate(userProfile, otherProfile2)).thenReturn(score2);

        Map<UUID, AffinityScore> result = recommendationsUseCase.getRecommendationsForUser(userId);

        assertThat(result).hasSize(2);
        assertThat(result.get(otherId1).getTotalScore()).isEqualTo(0.7);
        assertThat(result.get(otherId2).getTotalScore()).isEqualTo(0.5);
    }

    @Test
    void getRecommendedProfilesForUser_sortedByAffinityDescending() {
        when(profileServicePort.getProfileById(userId)).thenReturn(userProfile);
        when(profileServicePort.getAllProfiles())
                .thenReturn(List.of(userProfile, otherProfile1, otherProfile2))
                .thenReturn(List.of(userProfile, otherProfile1, otherProfile2));
        when(affinityCalculator.calculate(userProfile, otherProfile1)).thenReturn(scoreWith(0.3));
        when(affinityCalculator.calculate(userProfile, otherProfile2)).thenReturn(scoreWith(0.9));

        List<UserMatchProfileDto> result = recommendationsUseCase.getRecommendedProfilesForUser(userId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(otherId2); // highest affinity first
        assertThat(result.get(1).getId()).isEqualTo(otherId1);
    }

    @Test
    void getRecommendedProfilesForUser_excludesSelf() {
        when(profileServicePort.getProfileById(userId)).thenReturn(userProfile);
        when(profileServicePort.getAllProfiles())
                .thenReturn(List.of(userProfile, otherProfile1))
                .thenReturn(List.of(userProfile, otherProfile1));
        when(affinityCalculator.calculate(any(), any())).thenReturn(scoreWith(0.5));

        List<UserMatchProfileDto> result = recommendationsUseCase.getRecommendedProfilesForUser(userId);

        assertThat(result).noneMatch(p -> p.getId().equals(userId));
    }

    @Test
    void getRecommendedUserIdsForUser_returnsOrderedIds() {
        when(profileServicePort.getProfileById(userId)).thenReturn(userProfile);
        when(profileServicePort.getAllProfiles())
                .thenReturn(List.of(userProfile, otherProfile1, otherProfile2))
                .thenReturn(List.of(userProfile, otherProfile1, otherProfile2));
        when(affinityCalculator.calculate(userProfile, otherProfile1)).thenReturn(scoreWith(0.2));
        when(affinityCalculator.calculate(userProfile, otherProfile2)).thenReturn(scoreWith(0.8));

        List<UUID> result = recommendationsUseCase.getRecommendedUserIdsForUser(userId);

        assertThat(result).containsExactly(otherId2, otherId1);
    }

    @Test
    void calculateAffinityScore_callsCalculator() {
        UUID userId2 = UUID.randomUUID();
        UserMatchProfileDto profile2 = new UserMatchProfileDto();
        profile2.setId(userId2);

        when(profileServicePort.getProfileById(userId)).thenReturn(userProfile);
        when(profileServicePort.getProfileById(userId2)).thenReturn(profile2);
        AffinityScore expected = scoreWith(0.75);
        when(affinityCalculator.calculate(userProfile, profile2)).thenReturn(expected);

        AffinityScore result = recommendationsUseCase.calculateAffinityScore(userId, userId2);

        assertThat(result.getTotalScore()).isEqualTo(0.75);
    }

    @Test
    void getRecommendationsForUser_onlyUser_returnsEmptyMap() {
        when(profileServicePort.getProfileById(userId)).thenReturn(userProfile);
        when(profileServicePort.getAllProfiles()).thenReturn(List.of(userProfile));

        Map<UUID, AffinityScore> result = recommendationsUseCase.getRecommendationsForUser(userId);

        assertThat(result).isEmpty();
    }
}
