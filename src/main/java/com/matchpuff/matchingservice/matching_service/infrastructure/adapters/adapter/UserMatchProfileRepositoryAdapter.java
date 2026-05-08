package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.adapter;

import com.matchpuff.matchingservice.matching_service.domain.model.UserMatchProfile;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.UserMatchProfileRepositoryPort;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.mapper.UserMatchProfilePersistenceMapper;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.repository.UserMatchProfileMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMatchProfileRepositoryAdapter implements UserMatchProfileRepositoryPort {

    private final UserMatchProfileMongoRepository mongoRepository;
    private final UserMatchProfilePersistenceMapper mapper;

    @Override
    public UserMatchProfile save(UserMatchProfile profile) {
        return mapper.toDomain(mongoRepository.save(mapper.toDocument(profile)));
    }

    @Override
    public Optional<UserMatchProfile> findById(UUID id) {
        return mongoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<UserMatchProfile> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
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
