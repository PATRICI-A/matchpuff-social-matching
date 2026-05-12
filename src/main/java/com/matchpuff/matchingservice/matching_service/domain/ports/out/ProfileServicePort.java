package com.matchpuff.matchingservice.matching_service.domain.ports.out;

import java.util.List;
import java.util.UUID;

import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;

public interface ProfileServicePort {
    UserMatchProfileDto getProfileById(UUID userId);
    List<UserMatchProfileDto> getAllProfiles();

}
