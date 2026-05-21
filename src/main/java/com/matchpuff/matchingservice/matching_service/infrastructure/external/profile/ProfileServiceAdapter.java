package com.matchpuff.matchingservice.matching_service.infrastructure.external.profile;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.matchpuff.matchingservice.matching_service.domain.exceptions.ExternalServiceException;
import com.matchpuff.matchingservice.matching_service.domain.exceptions.NotFoundException;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchProfile;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.ProfileServicePort;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.client.ProfileFeignClient;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.client.ProfilePublicFeignClient;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.FriendRequestDto;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileServiceAdapter implements ProfileServicePort {

    private final ProfileFeignClient profileFeignClient;
    private final ProfilePublicFeignClient profilePublicFeignClient;

    @Override
    public MatchProfile getProfileById(UUID userId) {
        try {
            return toMatchProfile(profileFeignClient.getProfileById(userId));
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Profile not found with ID: " + userId);
        } catch (FeignException e) {
            throw new ExternalServiceException("Profile service unavailable: " + e.getMessage());
        }
    }

    @Override
    public List<MatchProfile> getAllProfiles() {
        try {
            return profileFeignClient.getAllProfiles().stream()
                    .map(this::toMatchProfile)
                    .toList();
        } catch (FeignException e) {
            throw new ExternalServiceException("Profile service unavailable: " + e.getMessage());
        }
    }

    @Override
    public void addFriend(UUID userId, UUID friendId) {
        try {
            profilePublicFeignClient.addFriend(userId, new FriendRequestDto(friendId));
        } catch (FeignException e) {
            throw new ExternalServiceException("Profile service unavailable while adding friend: " + e.getMessage());
        }
    }

    private MatchProfile toMatchProfile(UserMatchProfileDto dto) {
        return new MatchProfile(
                dto.getId(),
                dto.getCareer(),
                dto.getSemester(),
                dto.getTags(),
                dto.getSchedulesAvailable()
        );
    }
}
