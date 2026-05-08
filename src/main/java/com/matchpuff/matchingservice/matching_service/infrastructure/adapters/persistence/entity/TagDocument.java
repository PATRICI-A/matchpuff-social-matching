package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDocument {
    private String name;
    private String category;
}
