package com.matchpuff.matchingservice.matching_service.application.dto.response;

import com.matchpuff.matchingservice.matching_service.domain.model.MatchStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MatchResponse {
    private UUID idMatch;
    private UUID requesterId;
    private UUID targetId;
    private MatchStatus status;
    private AffinityScoreResponse affinityScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
