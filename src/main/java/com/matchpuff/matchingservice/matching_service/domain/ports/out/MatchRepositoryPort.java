package com.matchpuff.matchingservice.matching_service.domain.ports.out;

import com.matchpuff.matchingservice.matching_service.domain.model.Match;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepositoryPort {
    Match save(Match match);
    Optional<Match> findById(UUID id);
    List<Match> findByTargetId(UUID targetId);
    List<Match> findByRequesterId(UUID requesterId);
    boolean existsByRequesterIdAndTargetId(UUID requesterId, UUID targetId);
    void deleteById(UUID id);
}
