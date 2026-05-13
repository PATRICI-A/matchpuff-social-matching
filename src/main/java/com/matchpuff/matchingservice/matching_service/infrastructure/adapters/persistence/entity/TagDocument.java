package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class TagDocument {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String name;
    @Indexed
    private UUID categoryID;
}
