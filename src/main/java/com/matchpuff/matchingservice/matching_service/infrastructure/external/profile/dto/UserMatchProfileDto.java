package com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserMatchProfileDto {
    private UUID id;
    private String career;
    private Integer semester;
    private List<String> tags;
}
