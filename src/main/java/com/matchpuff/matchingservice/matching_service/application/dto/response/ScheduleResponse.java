package com.matchpuff.matchingservice.matching_service.application.dto.response;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.DayOfWeekEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class ScheduleResponse {
    private DayOfWeekEnum dayOfWeek;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
}
