package com.matchpuff.matchingservice.matching_service.domain.model;

import lombok.Data;

@Data
public class AffinityScore {
    private double score;
    private double interestScore;
    private double academicScore;
    private double scheduleScore;
}
