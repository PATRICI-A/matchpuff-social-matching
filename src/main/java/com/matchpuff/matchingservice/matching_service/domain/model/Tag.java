package com.matchpuff.matchingservice.matching_service.domain.model;

import java.util.UUID;

import lombok.Data;

@Data
public class Tag {

    private UUID id;
    private String name;
    private UUID categoryID;

    public Tag(String name, UUID categoryID) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("The tag name cannot be blank");
        if (categoryID == null) throw new IllegalArgumentException("The tag category ID cannot be null");
        if (name.trim().length() > 50) throw new IllegalArgumentException("The tag name must be between 1 and 50 characters");
        
        this.name = name.trim().toLowerCase();
        this.categoryID = categoryID;
    }

}
