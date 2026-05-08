package com.matchpuff.matchingservice.matching_service.domain.model;

import com.matchpuff.matchingservice.matching_service.domain.exceptions.InvalidInputException;
import com.matchpuff.matchingservice.matching_service.domain.model.enums.DayOfWeekEnum;
import lombok.Data;

import java.time.LocalTime;

@Data
public class Schedule {
    private DayOfWeekEnum dayOfWeek;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;

    public Schedule(DayOfWeekEnum dayOfWeek, String name, LocalTime startTime, LocalTime endTime) {
        if (dayOfWeek == null) throw new InvalidInputException("Day of week is required");
        if (startTime == null || endTime == null) throw new InvalidInputException("Start and end time are required");
        if (name == null || name.isBlank()) throw new InvalidInputException("Name is required");

        if (!startTime.isBefore(endTime)) {
            throw new InvalidInputException(
                "Start time must be before end time"
            );
        }
        if (name.trim().length() > 100) {
            throw new InvalidInputException(
                "Name must be less than 100 characters"
            );
        }
        this.dayOfWeek = dayOfWeek;
        this.name = name.trim();
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
