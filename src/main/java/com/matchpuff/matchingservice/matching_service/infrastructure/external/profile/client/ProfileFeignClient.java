package com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.client;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;

@FeignClient(name = "profile-service")
public interface ProfileFeignClient {

    @GetMapping("/profiles/{id}")
    UserMatchProfileDto getProfileById(@PathVariable UUID id);

    @GetMapping("/profiles")
    List<UserMatchProfileDto> getAllProfiles();
}
