package com.matchpuff.matchingservice.matching_service.domain.model;

import lombok.Data;

@Data
public class Tag {
    private String name;
    private String category;

    public Tag(String name, String category) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("The tag name cannot be blank");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("The tag category cannot be blank");
        if (name.trim().length() > 50) throw new IllegalArgumentException("The tag name must be between 1 and 50 characters");
        if (category.trim().length() > 100) throw new IllegalArgumentException("The tag category must be between 1 and 100 characters");


        this.name = name.trim();
        this.category = category.trim();
    }
}
