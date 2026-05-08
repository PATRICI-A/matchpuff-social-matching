package com.matchpuff.matchingservice.matching_service.application.usecase;

import com.matchpuff.matchingservice.matching_service.application.service.AffinityCalculatorService;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.Schedule;
import com.matchpuff.matchingservice.matching_service.domain.model.Tag;
import com.matchpuff.matchingservice.matching_service.domain.model.UserMatchProfile;
import com.matchpuff.matchingservice.matching_service.domain.model.enums.GenderEnum;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

// totalScore = 40% interestScore + 30% academicScore + 30% scheduleScore
// interestScore = 30% categoria + 50% nombre-tag + 20% genero
// academicScore = 60% carrera + 40% semestre
@Service
public class AffinityCalculatorServiceImpl implements AffinityCalculatorService {

    // Pesos del score total (suman 1.0)
    private static final double W_INTEREST = 0.40;
    private static final double W_ACADEMIC = 0.30;
    private static final double W_SCHEDULE = 0.30;

    // Pesos internos del interest score (suman 1.0)
    private static final double W_CATEGORY = 0.30;
    private static final double W_TAG_NAME = 0.50;
    private static final double W_GENDER   = 0.20;

    // Pesos internos del academic score (suman 1.0)
    private static final double W_CAREER   = 0.60;
    private static final double W_SEMESTER = 0.40;

    // Diferencia maxima posible entre semestres (semestre 1 al 10)
    private static final double MAX_SEMESTER_DIFF = 9.0;

    // Granularidad del horario en minutos
    private static final int SLOT_MINUTES = 30;

    @Override
    public AffinityScore calculate(UserMatchProfile requester, UserMatchProfile target) {
        double interestScore = calculateInterestScore(requester, target);
        double academicScore = calculateAcademicScore(requester, target);
        double scheduleScore = calculateScheduleScore(requester.getSchedules(), target.getSchedules());
        double totalScore    = W_INTEREST * interestScore
                             + W_ACADEMIC * academicScore
                             + W_SCHEDULE * scheduleScore;

        AffinityScore result = new AffinityScore();
        result.setInterestScore(round(interestScore));
        result.setAcademicScore(round(academicScore));
        result.setScheduleScore(round(scheduleScore));
        result.setScore(round(totalScore));
        return result;
    }

    // -------------------------------------------------------------------------
    // Interest score (40% del total)
    // -------------------------------------------------------------------------

    private double calculateInterestScore(UserMatchProfile a, UserMatchProfile b) {
        double categoryScore = level1CategoryScore(a.getTags(), b.getTags());
        double tagNameScore  = level2TagNameScore(a.getTags(), b.getTags());
        double genderScore   = level3GenderScore(a, b);

        return W_CATEGORY * categoryScore
             + W_TAG_NAME  * tagNameScore
             + W_GENDER    * genderScore;
    }

    // Nivel 1: Jaccard sobre las categorias de los tags
    // Ej: A={Deportes, Musica}, B={Deportes, Arte} -> 1 comun / 3 union = 0.33
    private double level1CategoryScore(List<Tag> tagsA, List<Tag> tagsB) {
        if (isEmpty(tagsA) && isEmpty(tagsB)) return 1.0;
        if (isEmpty(tagsA) || isEmpty(tagsB)) return 0.0;

        Set<String> catA = extractCategories(tagsA);
        Set<String> catB = extractCategories(tagsB);

        Set<String> intersection = new HashSet<>(catA);
        intersection.retainAll(catB);

        Set<String> union = new HashSet<>(catA);
        union.addAll(catB);

        return (double) intersection.size() / union.size();
    }

    // Nivel 2: el nombre de un tag de A coincide con la categoria de B (y viceversa)
    // Ej: A tiene name="Voleibol" y B tiene category="Voleibol" -> hit
    private double level2TagNameScore(List<Tag> tagsA, List<Tag> tagsB) {
        if (isEmpty(tagsA) || isEmpty(tagsB)) return 0.0;

        Set<String> catA   = extractCategories(tagsA);
        Set<String> catB   = extractCategories(tagsB);
        Set<String> namesA = extractNames(tagsA);
        Set<String> namesB = extractNames(tagsB);

        long hitsAtoB = namesA.stream().filter(catB::contains).count();
        long hitsBtoA = namesB.stream().filter(catA::contains).count();

        int total = tagsA.size() + tagsB.size();
        return (double) (hitsAtoB + hitsBtoA) / total;
    }

    // Nivel 3: compatibilidad mutua de preferencia de genero
    // Ambos se aceptan -> 1.0 | Solo uno -> 0.5 | Ninguno -> 0.0
    // Sin preferencias declaradas significa que acepta cualquier genero
    private double level3GenderScore(UserMatchProfile a, UserMatchProfile b) {
        boolean aAcceptsB = acceptsGender(a.getGenderPreferences(), b.getGender());
        boolean bAcceptsA = acceptsGender(b.getGenderPreferences(), a.getGender());

        if (aAcceptsB && bAcceptsA) return 1.0;
        if (aAcceptsB || bAcceptsA) return 0.5;
        return 0.0;
    }

    // -------------------------------------------------------------------------
    // Academic score (30% del total)
    // -------------------------------------------------------------------------

    // Carrera: coincidencia binaria (misma carrera = 1.0, diferente = 0.0)
    // Semestre: proximidad normalizada (mismo semestre = 1.0, max diferencia = 0.0)
    private double calculateAcademicScore(UserMatchProfile a, UserMatchProfile b) {
        double careerScore   = calculateCareerScore(a, b);
        double semesterScore = calculateSemesterScore(a, b);
        return W_CAREER * careerScore + W_SEMESTER * semesterScore;
    }

    private double calculateCareerScore(UserMatchProfile a, UserMatchProfile b) {
        if (a.getCareer() == null || b.getCareer() == null) return 0.0;
        return a.getCareer() == b.getCareer() ? 1.0 : 0.0;
    }

    private double calculateSemesterScore(UserMatchProfile a, UserMatchProfile b) {
        if (a.getSemester() == null || b.getSemester() == null) return 0.0;
        double diff = Math.abs(a.getSemester() - b.getSemester());
        return 1.0 - (diff / MAX_SEMESTER_DIFF);
    }

    // -------------------------------------------------------------------------
    // Schedule score (30% del total)
    // Jaccard sobre slots discretos de SLOT_MINUTES minutos por dia
    // -------------------------------------------------------------------------

    private double calculateScheduleScore(List<Schedule> schedulesA, List<Schedule> schedulesB) {
        if (isEmpty(schedulesA) && isEmpty(schedulesB)) return 1.0;
        if (isEmpty(schedulesA) || isEmpty(schedulesB)) return 0.0;

        Set<String> slotsA = toSlots(schedulesA);
        Set<String> slotsB = toSlots(schedulesB);

        Set<String> intersection = new HashSet<>(slotsA);
        intersection.retainAll(slotsB);

        Set<String> union = new HashSet<>(slotsA);
        union.addAll(slotsB);

        if (union.isEmpty()) return 0.0;
        return (double) intersection.size() / union.size();
    }

    // Genera slots "DIA-HH:mm" cada SLOT_MINUTES dentro de [startTime, endTime)
    private Set<String> toSlots(List<Schedule> schedules) {
        Set<String> slots = new HashSet<>();
        for (Schedule s : schedules) {
            LocalTime cursor = s.getStartTime();
            while (cursor.isBefore(s.getEndTime())) {
                slots.add(s.getDayOfWeek().name() + "-" + cursor);
                cursor = cursor.plusMinutes(SLOT_MINUTES);
            }
        }
        return slots;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Set<String> extractCategories(List<Tag> tags) {
        return tags.stream()
                .map(t -> t.getCategory().trim().toLowerCase())
                .collect(Collectors.toSet());
    }

    private Set<String> extractNames(List<Tag> tags) {
        return tags.stream()
                .map(t -> t.getName().trim().toLowerCase())
                .collect(Collectors.toSet());
    }

    private boolean acceptsGender(List<GenderEnum> preferences, GenderEnum gender) {
        if (preferences == null || preferences.isEmpty()) return true;
        return preferences.contains(gender);
    }

    private boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
