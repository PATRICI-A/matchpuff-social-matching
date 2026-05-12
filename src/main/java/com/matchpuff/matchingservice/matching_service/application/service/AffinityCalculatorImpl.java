package com.matchpuff.matchingservice.matching_service.application.service;

import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.infrastructure.external.profile.dto.UserMatchProfileDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AffinityCalculatorImpl implements AffinityCalculator {

    private static final double W_INTEREST = 0.40;
    private static final double W_ACADEMIC = 0.30;
    private static final double W_SCHEDULE = 0.30;

    private static final double W_CATEGORY = 0.30;
    private static final double W_TAG_NAME = 0.50;
    private static final double W_GENDER = 0.20;

    private static final double W_CAREER = 0.60;
    private static final double W_SEMESTER = 0.40;

    private static final double MAX_SEMESTER_DIFF = 9.0;

    @Override
    public AffinityScore calculate(UserMatchProfileDto a, UserMatchProfileDto b) {

        double interest = calculateInterestScore(a, b);
        double academic = calculateAcademicScore(a, b);
        double schedule = 0.5; // placeholder (luego conectas schedules reales)

        double total = W_INTEREST * interest
                     + W_ACADEMIC * academic
                     + W_SCHEDULE * schedule;

        AffinityScore result = new AffinityScore();
        result.setInterestScore(round(interest));
        result.setAcademicScore(round(academic));
        result.setScheduleScore(round(schedule));
        result.setTotalScore(round(total));

        return result;
    }

    // ---------------- INTEREST ----------------

    private double calculateInterestScore(UserMatchProfileDto a, UserMatchProfileDto b) {

        Set<String> tagsA = new HashSet<>(a.getTags());
        Set<String> tagsB = new HashSet<>(b.getTags());

        Set<String> intersection = new HashSet<>(tagsA);
        intersection.retainAll(tagsB);

        Set<String> union = new HashSet<>(tagsA);
        union.addAll(tagsB);

        double categoryScore = union.isEmpty() ? 0 : (double) intersection.size() / union.size();

        double tagNameScore = categoryScore; // simplificado (misma lógica base)

        double genderScore = 1.0; // placeholder (si agregas gender luego lo activas)

        return W_CATEGORY * categoryScore
             + W_TAG_NAME * tagNameScore
             + W_GENDER * genderScore;
    }

    // ---------------- ACADEMIC ----------------

    private double calculateAcademicScore(UserMatchProfileDto a, UserMatchProfileDto b) {

        double careerScore = a.getCareer().equals(b.getCareer()) ? 1.0 : 0.0;

        double diff = Math.abs(a.getSemester() - b.getSemester());
        double semesterScore = 1.0 - (diff / MAX_SEMESTER_DIFF);

        return W_CAREER * careerScore
             + W_SEMESTER * semesterScore;
    }

    private double round(double v) {
        return Math.round(v * 10000.0) / 10000.0;
    }
}