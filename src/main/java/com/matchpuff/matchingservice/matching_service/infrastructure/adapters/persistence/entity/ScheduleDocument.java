package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.DayOfWeekEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDocument {
    private DayOfWeekEnum dayOfWeek;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
}
