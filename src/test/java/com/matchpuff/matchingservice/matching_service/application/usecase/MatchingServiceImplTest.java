package com.matchpuff.matchingservice.matching_service.application.usecase;

import com.matchpuff.matchingservice.matching_service.domain.exceptions.InvalidInputException;
import com.matchpuff.matchingservice.matching_service.domain.exceptions.NotFoundException;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchStatus;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.MatchRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchingServiceImplTest {

    @Mock
    private MatchRepositoryPort matchRepository;

    @InjectMocks
    private MatchingServiceImpl matchingService;

    private UUID requesterId;
    private UUID targetId;
    private UUID matchId;
    private Match pendingMatch;

    @BeforeEach
    void setUp() {
        requesterId = UUID.randomUUID();
        targetId = UUID.randomUUID();
        matchId = UUID.randomUUID();

        pendingMatch = new Match();
        pendingMatch.setIdMatch(matchId);
        pendingMatch.setRequesterId(requesterId);
        pendingMatch.setTargetId(targetId);
        pendingMatch.setStatus(MatchStatus.PENDING);
        pendingMatch.setAffinityScore(new AffinityScore());
        pendingMatch.setCreatedAt(LocalDateTime.now());
        pendingMatch.setUpdatedAt(LocalDateTime.now());
    }

    // ======================== CREATE MATCH ========================

    @Test
    void createMatch_success() {
        when(matchRepository.existsByRequesterIdAndTargetId(requesterId, targetId)).thenReturn(false);
        when(matchRepository.save(any(Match.class))).thenReturn(pendingMatch);

        Match result = matchingService.createMatch(requesterId, targetId);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(MatchStatus.PENDING);

        ArgumentCaptor<Match> captor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(captor.capture());
        Match saved = captor.getValue();
        assertThat(saved.getRequesterId()).isEqualTo(requesterId);
        assertThat(saved.getTargetId()).isEqualTo(targetId);
        assertThat(saved.getIdMatch()).isNotNull();
    }

    @Test
    void createMatch_alreadyExists_throwsInvalidInputException() {
        when(matchRepository.existsByRequesterIdAndTargetId(requesterId, targetId)).thenReturn(true);

        assertThatThrownBy(() -> matchingService.createMatch(requesterId, targetId))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("Already exists a match request");
    }

    // ======================== GET MATCH ========================

    @Test
    void getMatch_success() {
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(pendingMatch));

        Match result = matchingService.getMatch(matchId);

        assertThat(result).isEqualTo(pendingMatch);
    }

    @Test
    void getMatch_notFound_throwsNotFoundException() {
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchingService.getMatch(matchId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Match not found");
    }

    // ======================== FIND BY IDS ========================

    @Test
    void findByRequesterId_returnsList() {
        when(matchRepository.findByRequesterId(requesterId)).thenReturn(List.of(pendingMatch));

        List<Match> result = matchingService.findByRequesterId(requesterId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(pendingMatch);
    }

    @Test
    void findByTargetId_returnsList() {
        when(matchRepository.findByTargetId(targetId)).thenReturn(List.of(pendingMatch));

        List<Match> result = matchingService.findByTargetId(targetId);

        assertThat(result).hasSize(1);
    }

    // ======================== RESPOND TO MATCH REQUEST ========================

    @Test
    void respondToMatchRequest_accept_setsAccepted() {
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(pendingMatch));
        when(matchRepository.save(any(Match.class))).thenAnswer(inv -> inv.getArgument(0));

        Match result = matchingService.respondToMatchRequest(matchId, true);

        assertThat(result.getStatus()).isEqualTo(MatchStatus.ACCEPTED);
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void respondToMatchRequest_reject_setsRejected() {
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(pendingMatch));
        when(matchRepository.save(any(Match.class))).thenAnswer(inv -> inv.getArgument(0));

        Match result = matchingService.respondToMatchRequest(matchId, false);

        assertThat(result.getStatus()).isEqualTo(MatchStatus.REJECTED);
    }

    @Test
    void respondToMatchRequest_notPending_throwsInvalidInputException() {
        pendingMatch.setStatus(MatchStatus.ACCEPTED);
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(pendingMatch));

        assertThatThrownBy(() -> matchingService.respondToMatchRequest(matchId, true))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("Only pending requests can be responded to");
    }

    @Test
    void respondToMatchRequest_notFound_throwsNotFoundException() {
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchingService.respondToMatchRequest(matchId, true))
                .isInstanceOf(NotFoundException.class);
    }
}
