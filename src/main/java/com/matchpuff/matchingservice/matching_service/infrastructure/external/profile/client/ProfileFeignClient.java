package com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.FriendRequestDto;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;

@FeignClient(
    name = "profile-service",
    url = "${PROFILE_SERVICE_URL}",
    path = "${PROFILE_SERVICE_PATH}"
)
public interface ProfileFeignClient {

    @GetMapping("/matching/profiles/{id}")
    UserMatchProfileDto getProfileById(@PathVariable UUID id);

    @GetMapping("/matching/profiles")
    List<UserMatchProfileDto> getAllProfiles();

    @PatchMapping("/users/{userId}/friends")
    void addFriend(@PathVariable UUID userId, @RequestBody FriendRequestDto request);
}
