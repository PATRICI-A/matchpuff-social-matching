package com.matchpuff.matchingservice.matching_service.domain.ports.out;

import java.util.UUID;

import com.matchpuff.matchingservice.matching_service.domain.model.MatchStatus;

public interface MatchEventPublisherPort {

    void publishMatchReceived(UUID senderUserId, UUID receiverUserId, Double affinityPercentage);
    void publishMatchResponse(UUID senderUserId, UUID receiverUserId, MatchStatus status);
}
