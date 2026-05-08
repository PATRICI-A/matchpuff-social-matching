package com.matchpuff.matchingservice.matching_service.application.service;

import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchUpdateRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.response.MatchResponse;

import java.util.List;
import java.util.UUID;

public interface MatchingService {
    MatchResponse createMatch(MatchRequest request);
    MatchResponse getMatch(UUID matchId);
    List<MatchResponse> getMatchesByUser(UUID userId);
    MatchResponse updateMatchStatus(UUID matchId, MatchUpdateRequest request);
    void deleteMatch(UUID matchId);
}
