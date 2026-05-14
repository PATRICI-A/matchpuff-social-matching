package com.matchpuff.matchingservice.matching_service.application.usecase;

import com.matchpuff.matchingservice.matching_service.domain.exceptions.InvalidInputException;
import com.matchpuff.matchingservice.matching_service.domain.exceptions.NotFoundException;
import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchStatus;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.MatchUseCasePort;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.RecommendationsUseCasePort;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.MatchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchUseCasePort {

    private final MatchRepositoryPort matchRepository;
    private final RecommendationsUseCasePort recommendationsUseCase;

    @Override
    public Match createMatch(UUID requesterId, UUID targetId) {
        if (matchRepository.existsByRequesterIdAndTargetId(requesterId, targetId)) {
            throw new InvalidInputException("Already exists a match request between requester and target");
        }

        Match match = new Match();
        match.setIdMatch(UUID.randomUUID());
        match.setRequesterId(requesterId);
        match.setTargetId(targetId);
        match.setStatus(MatchStatus.PENDING);
        match.setCreatedAt(LocalDateTime.now());
        match.setUpdatedAt(LocalDateTime.now());

        match.setAffinityScore(recommendationsUseCase.calculateAffinityScore(requesterId, targetId));

        return matchRepository.save(match);
    }

    @Override
    public Match getMatch(UUID matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found with ID: " + matchId));
    }

    @Override
    public List<Match> findByRequesterId(UUID requesterId) {
        return matchRepository.findByRequesterId(requesterId);
    }

    @Override
    public List<Match> findByTargetId(UUID targetId) {
        return matchRepository.findByTargetId(targetId);
    }

    @Override
    public Match respondToMatchRequest(UUID matchId, boolean accept) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found with ID: " + matchId));
        if (match.getStatus() != MatchStatus.PENDING) {
            throw new InvalidInputException("Only pending requests can be responded to");
        }
        match.setStatus(accept ? MatchStatus.ACCEPTED : MatchStatus.REJECTED);
        match.setUpdatedAt(LocalDateTime.now());
        return matchRepository.save(match);
    }
}
