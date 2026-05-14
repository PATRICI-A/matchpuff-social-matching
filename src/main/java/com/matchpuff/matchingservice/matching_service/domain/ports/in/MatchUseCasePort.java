package com.matchpuff.matchingservice.matching_service.domain.ports.in;

import java.util.UUID;
import java.util.List;

import com.matchpuff.matchingservice.matching_service.domain.model.Match;

public interface MatchUseCasePort {

    Match createMatch(UUID requesterId, UUID targetId);
    Match getMatch(UUID matchId);
    List<Match> findByRequesterId(UUID requesterId);
    List<Match> findByTargetId(UUID targetId);
    Match respondToMatchRequest(UUID matchId, boolean accept);

}
