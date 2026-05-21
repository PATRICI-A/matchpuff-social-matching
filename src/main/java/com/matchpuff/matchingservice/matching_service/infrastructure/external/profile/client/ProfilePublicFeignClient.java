package com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.FriendRequestDto;

@FeignClient(
    name = "profile-service",
    contextId = "profilePublic",
    url = "${PROFILE_SERVICE_URL}",
    path = "${PROFILE_SERVICE_PUBLIC_PATH}"
)
public interface ProfilePublicFeignClient {

    @PatchMapping("/users/{userId}/friends")
    void addFriend(@PathVariable UUID userId, @RequestBody FriendRequestDto request);
}
