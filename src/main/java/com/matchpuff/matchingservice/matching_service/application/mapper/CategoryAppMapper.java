package com.matchpuff.matchingservice.matching_service.application.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.matchpuff.matchingservice.matching_service.application.dto.request.CreateCategoryRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.CreateTagRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.UpdateCategoryRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.UpdateTagRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.response.CategoryResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.CategoryWithTagsResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.TagResponse;
import com.matchpuff.matchingservice.matching_service.domain.model.Category;
import com.matchpuff.matchingservice.matching_service.domain.model.Tag;

@Mapper(componentModel = "spring")
public interface CategoryAppMapper {

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseCategoryList(List<Category> categories);

    TagResponse toResponse(Tag tag);

    List<TagResponse> toResponseTagList(List<Tag> tags);


    Category toDomain(CreateCategoryRequest request);
    void updateDomain(UpdateCategoryRequest request, @MappingTarget Category category);

    Tag toDomain(CreateTagRequest request);
    void updateDomain(UpdateTagRequest request, @MappingTarget Tag tag);

    default List<CategoryWithTagsResponse> toResponseCategoryWithTags(Map<Category, List<Tag>> categoryWithTags) {
        List<CategoryWithTagsResponse> responses = new ArrayList<>();

        for (Map.Entry<Category, List<Tag>> entry : categoryWithTags.entrySet()) {
            responses.add(new CategoryWithTagsResponse(
                    entry.getKey().getId(),
                    entry.getKey().getName(),
                    toResponseTagList(entry.getValue())));
        }

        return responses;
    }
}
