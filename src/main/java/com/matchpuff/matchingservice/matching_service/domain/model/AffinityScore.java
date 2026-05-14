package com.matchpuff.matchingservice.matching_service.domain.model;

import lombok.Data;

@Data
public class AffinityScore {
    private double totalScore;
    private double interestScore;
    private double academicScore;
    private double scheduleScore;
}
