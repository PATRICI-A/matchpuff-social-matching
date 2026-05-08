package com.matchpuff.matchingservice.matching_service.domain.ports.out;

import com.matchpuff.matchingservice.matching_service.domain.model.UserMatchProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserMatchProfileRepositoryPort {
    UserMatchProfile save(UserMatchProfile profile);
    Optional<UserMatchProfile> findById(UUID id);
    List<UserMatchProfile> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
