package com.matchpuff.matchingservice.matching_service.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MatchRequest {

    @NotNull(message = "El ID del solicitante es requerido")
    private UUID requesterId;

    @NotNull(message = "El ID del destinatario es requerido")
    private UUID targetId;
}
