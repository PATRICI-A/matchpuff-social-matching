package com.matchpuff.matchingservice.matching_service.application.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private UUID id;
    private String name;

}