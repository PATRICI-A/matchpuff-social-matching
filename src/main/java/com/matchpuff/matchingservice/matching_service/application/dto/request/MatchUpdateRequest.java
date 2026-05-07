package com.matchpuff.matchingservice.matching_service.application.dto.request;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.MatchStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchUpdateRequest {

    @NotNull(message = "El estado del match es requerido")
    private MatchStatus status;
}
