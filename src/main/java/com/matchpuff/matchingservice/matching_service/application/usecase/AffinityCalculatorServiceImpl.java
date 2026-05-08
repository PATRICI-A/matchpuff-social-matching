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

/**
 * Calcula la afinidad entre dos perfiles usando tres niveles de interés + horario.
 *
 * interestScore = 30% categoría + 50% nombre-tag + 20% género
 * totalScore    = 60% interestScore + 40% scheduleScore
 */
@Service
public class AffinityCalculatorServiceImpl implements AffinityCalculatorService {

    // Pesos del interest score (deben sumar 1.0)
    private static final double W_CATEGORY = 0.30;
    private static final double W_TAG_NAME = 0.50;
    private static final double W_GENDER   = 0.10;

    // Pesos del score total (deben sumar 1.0)
    private static final double W_INTEREST  = 0.60;
    private static final double W_SCHEDULE  = 0.40;

    // Granularidad del horario en minutos (ventana de 30 min)
    private static final int SLOT_MINUTES = 30;

    @Override
    public AffinityScore calculate(UserMatchProfile requester, UserMatchProfile target) {
        double interestScore = calculateInterestScore(requester, target);
        double scheduleScore = calculateScheduleScore(requester.getSchedules(), target.getSchedules());
        double totalScore    = W_INTEREST * interestScore + W_SCHEDULE * scheduleScore;

        AffinityScore result = new AffinityScore();
        result.setInterestScore(round(interestScore));
        result.setScheduleScore(round(scheduleScore));
        result.setScore(round(totalScore));
        return result;
    }

    // -------------------------------------------------------------------------
    // Interest score: combina los tres niveles
    // -------------------------------------------------------------------------

    private double calculateInterestScore(UserMatchProfile a, UserMatchProfile b) {
        double categoryScore = level1CategoryScore(a.getTags(), b.getTags());
        double tagNameScore  = level2TagNameScore(a.getTags(), b.getTags());
        double genderScore   = level3GenderScore(a, b);

        return W_CATEGORY * categoryScore
             + W_TAG_NAME  * tagNameScore
             + W_GENDER    * genderScore;
    }

    /**
     * Nivel 1: similitud de Jaccard sobre las categorías de los tags.
     * Ejemplo: A tiene {Deportes, Música}, B tiene {Deportes, Arte} → 1/3 ≈ 0.33
     */
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

    /**
     * Nivel 2: el nombre de un tag de A coincide con la categoría de B (y viceversa).
     * Ejemplo: A tiene tag name="Voleibol" y B tiene categoría="Voleibol" → hit.
     * Esto detecta cuando el hobby específico de uno es el área general del otro.
     *
     * Score = hits_bidireccionales / total_tags_posibles
     */
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

    /**
     * Nivel 3: compatibilidad mutua de preferencia de género.
     * A acepta el género de B Y B acepta el género de A → 1.0
     * Solo uno acepta al otro → 0.5
     * Ninguno → 0.0
     * Sin preferencias declaradas → acepta cualquier género.
     */
    private double level3GenderScore(UserMatchProfile a, UserMatchProfile b) {
        boolean aAcceptsB = acceptsGender(a.getGenderPreferences(), b.getGender());
        boolean bAcceptsA = acceptsGender(b.getGenderPreferences(), a.getGender());

        if (aAcceptsB && bAcceptsA) return 1.0;
        if (aAcceptsB || bAcceptsA) return 0.5;
        return 0.0;
    }

    // -------------------------------------------------------------------------
    // Schedule score: Jaccard sobre slots de 30 minutos por día
    // -------------------------------------------------------------------------

    /**
     * Convierte cada Schedule en slots discretos de SLOT_MINUTES minutos
     * (ej: Lunes 08:00-09:30 → {LUN-08:00, LUN-08:30, LUN-09:00})
     * y aplica Jaccard sobre el conjunto de slots.
     */
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

    /**
     * Genera un set de strings con formato "DIA-HH:mm" cada SLOT_MINUTES minutos
     * dentro del rango [startTime, endTime).
     */
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
        // sin preferencias → acepta cualquier género
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
