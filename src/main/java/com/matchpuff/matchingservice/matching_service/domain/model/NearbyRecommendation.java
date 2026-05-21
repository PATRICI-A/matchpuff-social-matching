package com.matchpuff.matchingservice.matching_service.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearbyRecommendation {

    private UUID userId;
    private double distanceMeters;
    private AffinityScore affinityScore;
}