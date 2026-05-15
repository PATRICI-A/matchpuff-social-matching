package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.mapper;

import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.AffinityScoreDocument;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.MatchDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchPersistenceMapper {
    
    MatchDocument toDocument(Match match);

    Match toDomain(MatchDocument document);

    AffinityScoreDocument toAffinityDocument(AffinityScore affinityScore);

    AffinityScore toAffinityDomain(AffinityScoreDocument document);
}
