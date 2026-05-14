package com.matchpuff.matchingservice.matching_service.application.dto.request;

import com.matchpuff.matchingservice.matching_service.domain.model.MatchStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchUpdateRequest {

    @NotNull(message = "The match status must not be null")
    private MatchStatus status;
}
