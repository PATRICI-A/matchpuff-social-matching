package com.matchpuff.matchingservice.matching_service.application.mapper;

import com.matchpuff.matchingservice.matching_service.application.dto.request.AffinityScoreRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.AffinityScoreUpdateRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchUpdateRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.response.AffinityScoreResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.MatchResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.NearbyRecommendationResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.RecommendationResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.RecommendationWithScoreResponse;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import com.matchpuff.matchingservice.matching_service.domain.model.NearbyRecommendation;

import java.util.List;
import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MatchApplicationMapper {

    @Mapping(target = "idMatch", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "affinityScore", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Match toDomain(MatchRequest request);


    @Mapping(target = "totalScore", source = "score")
    @Mapping(target = "academicScore", ignore = true)
    AffinityScore toDomain(AffinityScoreRequest request);

    @Mapping(target = "totalScore", source = "score")
    @Mapping(target = "academicScore", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAffinityScoreFromRequest(AffinityScoreUpdateRequest request, @MappingTarget AffinityScore affinityScore);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "idMatch", ignore = true)
    @Mapping(target = "requesterId", ignore = true)
    @Mapping(target = "targetId", ignore = true)
    @Mapping(target = "affinityScore", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMatchFromRequest(MatchUpdateRequest request, @MappingTarget Match match);


    MatchResponse toResponse(Match match);

    @Mapping(target = "score", source = "totalScore")
    @Mapping(target = "academicScore", ignore = true)
    AffinityScoreResponse toResponse(AffinityScore affinityScore);
    
    List<MatchResponse> toResponseList(List<Match> matches);

    RecommendationResponse toRecommendationResponse(UUID userId, List<UUID> recommendedUserIds);

    @Mapping(target = "targetUserId", source = "userId")
    @Mapping(target = "totalScore", source = "affinityScore.totalScore")
    @Mapping(target = "interestScore", source = "affinityScore.interestScore")
    @Mapping(target = "academicScore", source = "affinityScore.academicScore")
    @Mapping(target = "scheduleScore", source = "affinityScore.scheduleScore")
    NearbyRecommendationResponse toNearbyRecommendationResponse(NearbyRecommendation recommendation);

    @Mapping(target = "targetUserId", source = "targetUserId")
    @Mapping(target = "totalScore", source = "affinityScore.totalScore")
    @Mapping(target = "interestScore", source = "affinityScore.interestScore")
    @Mapping(target = "academicScore", source = "affinityScore.academicScore")
    @Mapping(target = "scheduleScore", source = "affinityScore.scheduleScore")
    RecommendationWithScoreResponse toRecommendationWithScoreResponse(UUID targetUserId, AffinityScore affinityScore);
}