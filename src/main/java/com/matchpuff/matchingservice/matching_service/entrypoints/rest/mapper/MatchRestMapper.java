package com.matchpuff.matchingservice.matching_service.entrypoints.rest.mapper;

import com.matchpuff.matchingservice.matching_service.application.dto.response.AffinityScoreResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.MatchResponse;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchRestMapper {

    @Mapping(target = "score", source = "totalScore")
    AffinityScoreResponse toResponse(AffinityScore affinityScore);

    @Mapping(target = "affinityScore", source = "affinityScore")
    MatchResponse toResponse(Match match);

    List<MatchResponse> toResponseList(List<Match> matches);
}