package com.matchpuff.matchingservice.matching_service.application.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class AffinityScoreUpdateRequest {

    @DecimalMin(value = "0.0", message = "El score no puede ser negativo")
    @DecimalMax(value = "1.0", message = "El score no puede ser mayor a 1")
    private Double score;

    @DecimalMin(value = "0.0", message = "El interest score no puede ser negativo")
    @DecimalMax(value = "1.0", message = "El interest score no puede ser mayor a 1")
    private Double interestScore;

    @DecimalMin(value = "0.0", message = "El schedule score no puede ser negativo")
    @DecimalMax(value = "1.0", message = "El schedule score no puede ser mayor a 1")
    private Double scheduleScore;
}
