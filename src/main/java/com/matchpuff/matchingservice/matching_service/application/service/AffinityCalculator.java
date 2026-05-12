package com.matchpuff.matchingservice.matching_service.application.service;

import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;

public interface AffinityCalculator {
    AffinityScore calculate(
            UserMatchProfileDto requester,
            UserMatchProfileDto target
    );
}
