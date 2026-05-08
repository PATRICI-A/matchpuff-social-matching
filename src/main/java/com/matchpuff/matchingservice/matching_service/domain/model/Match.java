package com.matchpuff.matchingservice.matching_service.domain.model;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.MatchStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Match {
    private UUID idMatch;
    private UUID requesterId;
    private UUID targetId;
    private MatchStatus status;
    private AffinityScore affinityScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
