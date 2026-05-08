package com.matchpuff.matchingservice.matching_service.application.service;

import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.UserMatchProfile;

public interface AffinityCalculatorService {
    AffinityScore calculate(UserMatchProfile requester, UserMatchProfile target);
}
