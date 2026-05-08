package com.matchpuff.matchingservice.matching_service.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagResponse {
    private String name;
    private String category;
}
