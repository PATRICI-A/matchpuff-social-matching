package com.matchpuff.matchingservice.matching_service.application.dto.request;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.CareerEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserMatchProfileRequest {

    @NotNull(message = "La carrera es requerida")
    private CareerEnum career;

    @NotNull(message = "El semestre es requerido")
    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 10, message = "El semestre máximo es 10")
    private Integer semester;

    @NotEmpty(message = "Los tags no pueden estar vacíos")
    private List<String> tag;

    @NotEmpty(message = "Los horarios no pueden estar vacíos")
    private List<String> schedule;

    @NotNull(message = "La fecha de última sincronización es requerida")
    @Past(message = "La fecha de última sincronización debe ser en el pasado")
    private Date lastSync;
}
