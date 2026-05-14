package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.mapper;


import org.mapstruct.Mapper;

import com.matchpuff.matchingservice.matching_service.domain.model.Category;
import com.matchpuff.matchingservice.matching_service.domain.model.Tag;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.CategoryDocument;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.TagDocument;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {
    CategoryDocument toDocument(Category category);
    Category toDomain(CategoryDocument document);
    TagDocument toTagDocument(Tag tag);
    Tag toTagDomain(TagDocument document);
}
