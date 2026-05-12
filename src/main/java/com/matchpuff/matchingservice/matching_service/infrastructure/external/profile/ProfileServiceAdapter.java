package com.matchpuff.matchingservice.matching_service.infrastructure.external.profile;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.matchpuff.matchingservice.matching_service.domain.ports.out.ProfileServicePort;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.client.ProfileFeignClient;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileServiceAdapter implements ProfileServicePort {

    private final ProfileFeignClient profileFeignClient;

    @Override
    public UserMatchProfileDto getProfileById(UUID userId) {
        return profileFeignClient.getProfileById(userId);
    }

    @Override
    public List<UserMatchProfileDto> getAllProfiles() {
    return profileFeignClient.getAllProfiles();
    }

}
