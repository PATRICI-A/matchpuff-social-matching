package com.matchpuff.matchingservice.matching_service.application.usecase;

import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchUpdateRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.response.AffinityScoreResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.MatchResponse;
import com.matchpuff.matchingservice.matching_service.application.service.MatchingService;
import com.matchpuff.matchingservice.matching_service.domain.exceptions.NotFoundException;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import com.matchpuff.matchingservice.matching_service.domain.model.enums.MatchStatus;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.MatchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final MatchRepositoryPort matchRepository;

    @Override
    public MatchResponse createMatch(MatchRequest request) {
        if (matchRepository.existsByRequesterIdAndTargetId(request.getRequesterId(), request.getTargetId())) {
            throw new IllegalArgumentException("Ya existe un match entre estos dos usuarios");
        }

        Match match = new Match();
        match.setIdMatch(UUID.randomUUID());
        match.setRequesterId(request.getRequesterId());
        match.setTargetId(request.getTargetId());
        match.setStatus(MatchStatus.PENDING);
        match.setAffinityScore(new AffinityScore());
        match.setCreatedAt(LocalDateTime.now());
        match.setUpdatedAt(LocalDateTime.now());

        return toResponse(matchRepository.save(match));
    }

    @Override
    public MatchResponse getMatch(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match no encontrado con ID: " + matchId));
        return toResponse(match);
    }

    @Override
    public List<MatchResponse> getMatchesByUser(UUID userId) {
        return matchRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MatchResponse updateMatchStatus(UUID matchId, MatchUpdateRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match no encontrado con ID: " + matchId));

        match.setStatus(request.getStatus());
        match.setUpdatedAt(LocalDateTime.now());

        return toResponse(matchRepository.save(match));
    }

    @Override
    public void deleteMatch(UUID matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new NotFoundException("Match no encontrado con ID: " + matchId);
        }
        matchRepository.deleteById(matchId);
    }

    private MatchResponse toResponse(Match match) {
        AffinityScoreResponse affinityResponse = null;
        if (match.getAffinityScore() != null) {
            AffinityScore a = match.getAffinityScore();
            affinityResponse = AffinityScoreResponse.builder()
                    .score(a.getScore())
                    .interestScore(a.getInterestScore())
                    .academicScore(a.getAcademicScore())
                    .scheduleScore(a.getScheduleScore())
                    .build();
        }

        return MatchResponse.builder()
                .idMatch(match.getIdMatch())
                .requesterId(match.getRequesterId())
                .targetId(match.getTargetId())
                .status(match.getStatus())
                .affinityScore(affinityResponse)
                .createdAt(match.getCreatedAt())
                .updatedAt(match.getUpdatedAt())
                .build();
    }
}
