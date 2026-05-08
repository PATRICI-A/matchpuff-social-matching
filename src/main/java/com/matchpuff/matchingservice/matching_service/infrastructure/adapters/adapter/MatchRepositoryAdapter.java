package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.adapter;

import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.MatchRepositoryPort;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.mapper.MatchPersistenceMapper;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.repository.MatchMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MatchRepositoryAdapter implements MatchRepositoryPort {

    private final MatchMongoRepository mongoRepository;
    private final MatchPersistenceMapper mapper;

    @Override
    public Match save(Match match) {
        return mapper.toDomain(mongoRepository.save(mapper.toDocument(match)));
    }

    @Override
    public Optional<Match> findById(UUID id) {
        return mongoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Match> findByUserId(UUID userId) {
        return mongoRepository.findByRequesterIdOrTargetId(userId, userId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByRequesterIdAndTargetId(UUID requesterId, UUID targetId) {
        return mongoRepository.existsByRequesterIdAndTargetId(requesterId, targetId);
    }

    @Override
    public boolean existsById(UUID id) {
        return mongoRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        mongoRepository.deleteById(id);
    }
}
