package com.matchpuff.matchingservice.matching_service.application.dto.request;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.DayOfWeekEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ScheduleRequest {

    @NotNull(message = "El día de la semana es requerido")
    private DayOfWeekEnum dayOfWeek;

    @NotBlank(message = "El nombre del horario es requerido")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String name;

    @NotNull(message = "La hora de inicio es requerida")
    private LocalTime startTime;

    @NotNull(message = "La hora de fin es requerida")
    private LocalTime endTime;
}
