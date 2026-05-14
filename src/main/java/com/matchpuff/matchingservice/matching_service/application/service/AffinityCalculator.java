package com.matchpuff.matchingservice.matching_service.application.service;

import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchProfile;

public interface AffinityCalculator {
    AffinityScore calculate(MatchProfile requester, MatchProfile target);
}
