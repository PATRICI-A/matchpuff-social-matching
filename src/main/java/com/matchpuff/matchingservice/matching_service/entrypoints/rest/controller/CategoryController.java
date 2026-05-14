package com.matchpuff.matchingservice.matching_service.entrypoints.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchpuff.matchingservice.matching_service.domain.ports.in.CategoryUseCasePort;
import com.matchpuff.matchingservice.matching_service.application.mapper.CategoryAppMapper;
import com.matchpuff.matchingservice.matching_service.application.dto.request.CreateCategoryRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.CreateTagRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.UpdateCategoryRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.UpdateTagRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.response.CategoryResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.CategoryWithTagsResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.TagResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "CRUD of categories and tags")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryUseCasePort categoryUseCase;
    private final CategoryAppMapper categoryAppMapper;


    // -------------------- CATEGORIES --------------------

    @PostMapping
    @Operation(summary = "Create a category", description = "Creates a new category with the provided name")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or category already exists")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryAppMapper.toResponse(categoryUseCase.createCategory(request.getName())));
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get a category by ID", description = "Retrieves a category by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Category found")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<CategoryResponse> getCategory(
            @Parameter(description = "ID of the category") @PathVariable UUID categoryId) {
        return ResponseEntity.ok(categoryAppMapper.toResponse(categoryUseCase.getCategory(categoryId)));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryAppMapper.toResponseCategoryList(categoryUseCase.getAllCategories()));
    }

    @PatchMapping("/{categoryId}")
    @Operation(summary = "Update a category", description = "Updates the name of an existing category")
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or category name already exists")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Parameter(description = "ID of the category") @PathVariable UUID categoryId,
            @Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(
                categoryAppMapper.toResponse(categoryUseCase.updateCategory(categoryId, request.getName())));
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete a category", description = "Deletes a category by its unique identifier")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category") @PathVariable UUID categoryId) {
        categoryUseCase.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }


    // -------------------- TAGS --------------------

    @PostMapping("/tags")
    @Operation(summary = "Create a tag", description = "Creates a new tag under a specific category")
    @ApiResponse(responseCode = "201", description = "Tag created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or tag already exists")
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody CreateTagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryAppMapper.toResponse(categoryUseCase.createTag(request.getName(), request.getCategoryId())));
    }

    @GetMapping("/tags/{tagId}")
    @Operation(summary = "Get a tag by ID", description = "Retrieves a tag by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Tag found")
    @ApiResponse(responseCode = "404", description = "Tag not found")
    public ResponseEntity<TagResponse> getTag(
            @Parameter(description = "ID of the tag") @PathVariable UUID tagId) {
        return ResponseEntity.ok(categoryAppMapper.toResponse(categoryUseCase.getTag(tagId)));
    }

    @GetMapping("/tags/all")
    @Operation(summary = "Get all tags", description = "Retrieves a list of all tags")
    @ApiResponse(responseCode = "200", description = "List of tags retrieved successfully")
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(categoryAppMapper.toResponseTagList(categoryUseCase.getAllTags()));
    }

    @PatchMapping("/tags/{tagId}")
    @Operation(summary = "Update a tag", description = "Updates the name of an existing tag")
    @ApiResponse(responseCode = "200", description = "Tag updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or tag name already exists")
    @ApiResponse(responseCode = "404", description = "Tag not found")
    public ResponseEntity<TagResponse> updateTag(
            @Parameter(description = "ID of the tag") @PathVariable UUID tagId,
            @Valid @RequestBody UpdateTagRequest request) {
        return ResponseEntity.ok(
                categoryAppMapper.toResponse(categoryUseCase.updateTag(tagId, request.getName())));
    }

    @DeleteMapping("/tags/{tagId}")
    @Operation(summary = "Delete a tag", description = "Deletes a tag by its unique identifier")
    @ApiResponse(responseCode = "204", description = "Tag deleted successfully")
    @ApiResponse(responseCode = "404", description = "Tag not found")
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "ID of the tag") @PathVariable UUID tagId) {
        categoryUseCase.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categoryId}/tags")
    @Operation(summary = "Get tags by category ID", description = "Retrieves a list of tags that belong to a specific category")
    @ApiResponse(responseCode = "200", description = "List of tags retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<List<TagResponse>> getTagsByCategoryId(
            @Parameter(description = "ID of the category") @PathVariable UUID categoryId) {
        return ResponseEntity.ok(categoryAppMapper.toResponseTagList(categoryUseCase.getTagsByCategoryId(categoryId)));
    }

    @GetMapping("/categories-with-tags")
    @Operation(summary = "Get all categories with their tags", description = "Retrieves a list of all categories along with their associated tags")
    @ApiResponse(responseCode = "200", description = "List of categories with tags retrieved successfully")
    public ResponseEntity<List<CategoryWithTagsResponse>> getAllCategoriesWithTags() {
        return ResponseEntity.ok(categoryAppMapper.toResponseCategoryWithTags(categoryUseCase.getAllCategoriesWithTags()));
    }
}
