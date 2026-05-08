package com.matchpuff.matchingservice.matching_service.application.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AffinityScoreRequest {

    @NotNull(message = "El score total es requerido")
    @DecimalMin(value = "0.0", message = "El score no puede ser negativo")
    @DecimalMax(value = "1.0", message = "El score no puede ser mayor a 1")
    private Double score;

    @NotNull(message = "El interest score es requerido")
    @DecimalMin(value = "0.0", message = "El interest score no puede ser negativo")
    @DecimalMax(value = "1.0", message = "El interest score no puede ser mayor a 1")
    private Double interestScore;

    @NotNull(message = "El schedule score es requerido")
    @DecimalMin(value = "0.0", message = "El schedule score no puede ser negativo")
    @DecimalMax(value = "1.0", message = "El schedule score no puede ser mayor a 1")
    private Double scheduleScore;
}
